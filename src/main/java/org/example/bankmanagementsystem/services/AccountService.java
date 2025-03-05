package org.example.bankmanagementsystem.services;

import org.example.bankmanagementsystem.dto.responses.AccountResponse;

import java.util.List;

public interface AccountService {

    AccountResponse createSavingsAccount();

    List<AccountResponse> myAccounts();

    AccountResponse accountDetail(Long id);
}
