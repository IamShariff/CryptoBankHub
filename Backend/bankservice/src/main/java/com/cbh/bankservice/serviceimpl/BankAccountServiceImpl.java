package com.cbh.bankservice.serviceimpl;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.cbh.bankservice.config.KafkaPublisher;
import com.cbh.bankservice.config.WebclientConfig;
import com.cbh.bankservice.model.BankAccount;
import com.cbh.bankservice.repository.BankAccountRepository;
import com.cbh.bankservice.requestdto.AddBankAccountRequestDto;
import com.cbh.bankservice.requestdto.KafkaNotificationRequestDto;
import com.cbh.bankservice.requestdto.UpdateBalanceRequestDto;
import com.cbh.bankservice.responsedto.UserResponseDto;
import com.cbh.bankservice.service.BankAccountService;
import com.cbh.exceptionservice.exception.NotFoundException;
import com.cbh.exceptionservice.exception.UserBlockedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

	private final ModelMapper mapper;
	private final BankAccountRepository bankAccountRepository;
	private final WebclientConfig webclientConfig;

	public BankAccount add(AddBankAccountRequestDto addBankAccountRequestDto) {
		BankAccount bankAccount = mapper.map(addBankAccountRequestDto, BankAccount.class);
		bankAccount.setBalance(0.0);
		String kycStatus = webclientConfig.get(
				String.format("http://localhost:1011/user/kyc/%s", addBankAccountRequestDto.getUserId()), String.class);
		if (!"Approved".equals(kycStatus)) {
			throw new UserBlockedException("User has not done kyc userid - ", addBankAccountRequestDto.getUserId());
		}
		bankAccount.setBankAccountStatus("OPENED");
		return bankAccountRepository.save(bankAccount);
	}

	public double checkBalance(String userId, long accountNumber) {
		return bankAccountRepository.findById(accountNumber)
				.filter(bankAccount -> bankAccount.getUserId().equals(userId)).map(BankAccount::getBalance)
				.orElseThrow(() -> new NotFoundException("accountNumber", "Bank account not found or user mismatch"));

	}

	public double updateBalance(UpdateBalanceRequestDto updateBalanceRequestDto) {
		return bankAccountRepository.findById(updateBalanceRequestDto.getBankAccountNumber())
				.map(bankAccount -> extracted(updateBalanceRequestDto, bankAccount))
				.orElseThrow(() -> new NotFoundException("accountId", "Account number not found"));
	}

	private Double extracted(UpdateBalanceRequestDto updateBalanceRequestDto, BankAccount bankAccount) {
		String transactionType = updateBalanceRequestDto.getTransactionType();
		double newBalance = switch (transactionType.toLowerCase()) {
		case "withdraw" -> handleWithdrawal(updateBalanceRequestDto, bankAccount);
		case "deposit" -> handleDeposit(updateBalanceRequestDto, bankAccount);
		default -> throw new NotFoundException("TransactionType", "Invalid transaction type");
		};

		UserResponseDto userInfo = getUserInfo(updateBalanceRequestDto.getUserId());
		String message = getMessageBasedOnTransactionType(transactionType, updateBalanceRequestDto.getAmount());

		KafkaNotificationRequestDto kafkaNotificationRequestDto = KafkaNotificationRequestDto.builder().message(message)
				.topic("userNotifications").key("bankUpdate").email(userInfo.getEmail())
				.subject("Insta alert !! Account Updated").build();

		KafkaPublisher.sendNotificationToUser(kafkaNotificationRequestDto,
				(response, topic) -> log.info("Successfully published response {} on topic {}", response, topic));

		return newBalance;
	}

	private double handleWithdrawal(UpdateBalanceRequestDto updateBalanceRequestDto, BankAccount bankAccount) {
		double newBalance = bankAccount.getBalance() - updateBalanceRequestDto.getAmount();
		if (newBalance < 0) {
			throw new NotFoundException("Balance", "Insufficient balance");
		}
		bankAccount.setBalance(newBalance);
		bankAccountRepository.save(bankAccount);
		return newBalance;
	}

	private double handleDeposit(UpdateBalanceRequestDto updateBalanceRequestDto, BankAccount bankAccount) {
		double newBalance = bankAccount.getBalance() + updateBalanceRequestDto.getAmount();
		bankAccount.setBalance(newBalance);
		bankAccountRepository.save(bankAccount);
		return newBalance;
	}

	private String getMessageBasedOnTransactionType(String transactionType, double amount) {
		return switch (transactionType.toLowerCase()) {
		case "withdraw" -> "Your account has been deducted with amount " + amount;
		case "deposit" -> "Your account has been credited with amount " + amount;
		default -> "Transaction completed";
		};
	}

	public List<BankAccount> getBankAccounts(String userId) {
		return bankAccountRepository.findByUserId(userId);
	}

	private UserResponseDto getUserInfo(String userId) {
		return webclientConfig.get(String.format("http://localhost:1011/user/%s", userId), UserResponseDto.class);
	}

}
