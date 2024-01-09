package com.cbh.paymentservice.service;

import com.cbh.paymentservice.requestdto.CryptoTransactionRequestDto;

public interface PaymentService {

	Boolean buy(CryptoTransactionRequestDto cryptoTransactionRequestDto);

	Boolean sell(CryptoTransactionRequestDto cryptoTransactionRequestDto);

}
