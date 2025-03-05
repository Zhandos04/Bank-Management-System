package org.example.bankmanagementsystem.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.bankmanagementsystem.Entities.Account;
import org.example.bankmanagementsystem.Entities.AccountType;
import org.example.bankmanagementsystem.Entities.User;
import org.example.bankmanagementsystem.dto.responses.AccountResponse;
import org.example.bankmanagementsystem.repositories.AccountRepository;
import org.example.bankmanagementsystem.repositories.UserRepository;
import org.example.bankmanagementsystem.services.AccountService;
import org.example.bankmanagementsystem.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public AccountResponse createSavingsAccount() {
        User customer = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found!"));
        Account account = new Account();
        account.setCustomer(customer);
        account.setBalance(0.0);
        account.setType(AccountType.SAVINGS);
        account.setAccountNumber(generateUniqueAccountNumber());
        accountRepository.save(account);
        return modelMapper.map(account, AccountResponse.class);
    }

    @Override
    public List<AccountResponse> myAccounts() {
        User customer = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found!"));
        return accountRepository.findAllByCustomerId(customer.getId()).stream().map(this::convertToAccountResponse).collect(Collectors.toList());
    }

    @Override
    public AccountResponse accountDetail(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if(optionalAccount.isEmpty()) {
            throw new BadCredentialsException("Account not found!");
        }
        return convertToAccountResponse(optionalAccount.get());
    }

    private AccountResponse convertToAccountResponse(Account account) {
        return modelMapper.map(account, AccountResponse.class);
    }
    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = generateRandomAccountNumber();
        } while(accountRepository.findByAccountNumber(accountNumber).isPresent());
        return accountNumber;
    }
    private String generateRandomAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
