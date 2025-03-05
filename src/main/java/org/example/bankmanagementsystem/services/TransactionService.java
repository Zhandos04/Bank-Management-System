package org.example.bankmanagementsystem.services;

import org.example.bankmanagementsystem.dto.requests.TransactionDepositRequest;
import org.example.bankmanagementsystem.dto.requests.TransactionRequest;
import org.example.bankmanagementsystem.dto.responses.TransactionResponse;

public interface TransactionService {
    TransactionResponse deposit(TransactionDepositRequest transactionRequest);
    TransactionResponse transfer(TransactionRequest transactionRequest);
}
