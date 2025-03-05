package org.example.bankmanagementsystem.controllers;

import lombok.RequiredArgsConstructor;
import org.example.bankmanagementsystem.dto.requests.TransactionDepositRequest;
import org.example.bankmanagementsystem.dto.requests.TransactionRequest;
import org.example.bankmanagementsystem.dto.responses.TransactionResponse;
import org.example.bankmanagementsystem.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestBody TransactionDepositRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.deposit(transactionRequest));
    }
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@RequestBody TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.transfer(transactionRequest));
    }
}
