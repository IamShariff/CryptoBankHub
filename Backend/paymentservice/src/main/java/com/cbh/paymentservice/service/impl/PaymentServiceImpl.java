package com.cbh.paymentservice.service.impl;

import com.cbh.paymentservice.requestdto.CryptoTransactionEventDto;
import com.cbh.paymentservice.requestdto.CryptoTransactionRequestDto;
import com.cbh.paymentservice.requestdto.KafkaNotificationRequestDto;
import com.cbh.paymentservice.requestdto.TransactionType;
import com.cbh.paymentservice.requestdto.UpdateBalanceRequestDto;
import com.cbh.paymentservice.requestdto.UserResponseDto;
import com.cbh.paymentservice.service.PaymentService;
import com.cbh.paymentservice.util.Constants;
import com.cbh.exceptionservice.exception.NotFoundException;
import com.cbh.kafkaservice.config.KafkaTopicConfig;
import com.cbh.paymentservice.config.KafkaPublisher;
import com.cbh.paymentservice.config.WebClientConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

	private final KafkaPublisher kafkaPublisher;
	private final WebClientConfig webClientConfig;

	@Override
	public Boolean buy(CryptoTransactionRequestDto cryptoTransactionRequestDto) {
		UserResponseDto user = getUserInfo(cryptoTransactionRequestDto.getUserId());
		log.info("Patment {}",user.getEmail());
		updateBalance(cryptoTransactionRequestDto, "Withdraw");
		KafkaNotificationRequestDto kafkaNotificationRequestDto = createNotificationRequest(user.getEmail(),
				"cryptoBuy", "userNotifications", "Update from CryptoBankHub",
				String.format("Congratulations! You have successfully bought %s %s from CryptobankHub",
						cryptoTransactionRequestDto.getQuantity(), cryptoTransactionRequestDto.getCryptoId()));
		sendNotificationAndLog(kafkaNotificationRequestDto);
		return processTransaction(cryptoTransactionRequestDto, TransactionType.BUY);
	}

	@Override
	public Boolean sell(CryptoTransactionRequestDto cryptoTransactionRequestDto) {

		checkQuantityAndThrowExceptionIfNotEnough(cryptoTransactionRequestDto);
		UserResponseDto user = getUserInfo(cryptoTransactionRequestDto.getUserId());
		updateBalance(cryptoTransactionRequestDto, "Deposit");
		KafkaNotificationRequestDto kafkaNotificationRequestDto = createNotificationRequest(user.getEmail(),
				"cryptoSell", "userNotifications", "Update from CryptoBankHub",
				String.format("Congratulations! You have successfully sold %s %s from CryptobankHub",
						cryptoTransactionRequestDto.getQuantity(), cryptoTransactionRequestDto.getCryptoId()));
		sendNotificationAndLog(kafkaNotificationRequestDto);
		return processTransaction(cryptoTransactionRequestDto, TransactionType.SELL);
	}

	private void updateBalance(CryptoTransactionRequestDto cryptoTransactionRequestDto, String transactioType) {

		BigDecimal totalAmount = cryptoTransactionRequestDto.getPrice()
				.multiply(BigDecimal.valueOf(cryptoTransactionRequestDto.getQuantity()));

		UpdateBalanceRequestDto updateBalanceRequestDto = UpdateBalanceRequestDto.builder()
				.bankAccountNumber(cryptoTransactionRequestDto.getAccountNumber())
				.userId(cryptoTransactionRequestDto.getUserId()).amount(totalAmount.doubleValue())
				.transactionType(transactioType).build();
		webClientConfig.patch("http://BANK-SERVICE/bankaccount/balance", updateBalanceRequestDto, Double.class);

	}

	private void checkQuantityAndThrowExceptionIfNotEnough(CryptoTransactionRequestDto cryptoTransactionRequestDto) {
		Boolean isEnoughQuantity = webClientConfig.get(
				String.format("http://CRYPTO-SERVICE/crypto/check-quantity/%s/%s/%s", cryptoTransactionRequestDto.getUserId(),
						cryptoTransactionRequestDto.getCryptoId(), cryptoTransactionRequestDto.getQuantity()),
				Boolean.class);
		if (Boolean.FALSE.equals(isEnoughQuantity)) {
			throw new NotFoundException("quantity", "Not enough quantity");
		}
	}

	private UserResponseDto getUserInfo(String userId) {
		return webClientConfig.get(String.format("http://USER-SERVICE/user/%s", userId), UserResponseDto.class);
	}

	private KafkaNotificationRequestDto createNotificationRequest(String email, String key, String topic,
			String subject, String message) {
		return KafkaNotificationRequestDto.builder().email(email).key(key).topic(topic).subject(subject)
				.message(message).build();
	}

	private void sendNotificationAndLog(KafkaNotificationRequestDto kafkaNotificationRequestDto) {
		KafkaTopicConfig.createTopic(kafkaNotificationRequestDto.getTopic(), 4, 3);
		kafkaPublisher.sendNotificationToUser(kafkaNotificationRequestDto,
				(topic, response) -> log.info(Constants.PUBLISHED_MESSAGE_LOG_FORMAT, response, topic));
	}

	private Boolean processTransaction(CryptoTransactionRequestDto cryptoTransactionRequestDto,
			TransactionType transactionType) {
		CryptoTransactionEventDto cryptoTransactionDto = CryptoTransactionEventDto.builder()
				.price(cryptoTransactionRequestDto.getPrice()).cryptoId(cryptoTransactionRequestDto.getCryptoId())
				.totalCrypto(cryptoTransactionRequestDto.getQuantity()).userId(cryptoTransactionRequestDto.getUserId())
				.type(transactionType).build();

		kafkaPublisher.sendTransactionToCryptoService(cryptoTransactionDto, "CryptoPortfolio",
				transactionType == TransactionType.BUY ? "Buy" : "Sell",
				(topic, response) -> log.info(Constants.PUBLISHED_MESSAGE_LOG_FORMAT, response, topic));

		return true;
	}
}
