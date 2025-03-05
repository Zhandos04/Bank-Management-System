package org.example.bankmanagementsystem.controllers;

import lombok.RequiredArgsConstructor;
import org.example.bankmanagementsystem.dto.responses.AccountResponse;
import org.example.bankmanagementsystem.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/create-savings")
    public ResponseEntity<AccountResponse> createSavingsAccount() {
        return ResponseEntity.ok(accountService.createSavingsAccount());
    }
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> accountDetail(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.accountDetail(id));
    }
    @GetMapping("/my")
    public ResponseEntity<List<AccountResponse>> myAccounts() {
        return ResponseEntity.ok(accountService.myAccounts());
    }
}
