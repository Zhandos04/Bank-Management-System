package org.example.bankmanagementsystem.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.bankmanagementsystem.Entities.*;
import org.example.bankmanagementsystem.dto.requests.TransactionDepositRequest;
import org.example.bankmanagementsystem.dto.requests.TransactionRequest;
import org.example.bankmanagementsystem.dto.responses.TransactionResponse;
import org.example.bankmanagementsystem.repositories.AccountRepository;
import org.example.bankmanagementsystem.repositories.TransactionRepository;
import org.example.bankmanagementsystem.services.TransactionService;
import org.example.bankmanagementsystem.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    @Override
    @Transactional
    public TransactionResponse deposit(TransactionDepositRequest transactionRequest) {
        Optional<Account> optionalSender = accountRepository.findByAccountNumber(transactionRequest.getSender());
        Optional<Account> optionalRecipient = accountRepository.findByAccountNumber(transactionRequest.getRecipient());
        if(optionalSender.isEmpty() || optionalRecipient.isEmpty()) {
            throw new BadCredentialsException("Account not found!");
        }
        Account sender = optionalSender.get();
        Account recipient = optionalRecipient.get();
        if (sender.getBalance() < transactionRequest.getAmount()) {
            throw new BadCredentialsException("Недостаточно денег");
        }
        sender.setBalance(sender.getBalance() - transactionRequest.getAmount());
        recipient.setBalance(recipient.getBalance() + transactionRequest.getAmount());
        Transaction transaction = new Transaction();
        transaction.setAccount(sender);
        transaction.setRecipientAccount(recipient);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        accountRepository.save(sender);
        accountRepository.save(recipient);
        return modelMapper.map(transactionRepository.save(transaction), TransactionResponse.class);
    }
    @Override
    @Transactional
    public TransactionResponse transfer(TransactionRequest transactionRequest) {
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        Optional<Account> optionalAccount = accountRepository.findByCustomerIdAndType(user.getId(), AccountType.CHECKING);
        Optional<Account> optionalTransferAccount = accountRepository.findByAccountNumber(transactionRequest.getRecipientAccountNumber());
        if(optionalAccount.isEmpty() || optionalTransferAccount.isEmpty()) {
            throw new BadCredentialsException("Account not found!");
        }
        Account account = optionalAccount.get();
        Account transferAccount = optionalTransferAccount.get();
        if (account.getBalance() < transactionRequest.getAmount()) {
            throw new BadCredentialsException("Недостаточно денег");
        }
        account.setBalance(account.getBalance() - transactionRequest.getAmount());
        transferAccount.setBalance(transferAccount.getBalance() + transactionRequest.getAmount());
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setRecipientAccount(transferAccount);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        accountRepository.save(account);
        accountRepository.save(transferAccount);
        return modelMapper.map(transactionRepository.save(transaction), TransactionResponse.class);
    }
}
