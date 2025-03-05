package org.example.bankmanagementsystem.dto.responses;

import lombok.Data;
import org.example.bankmanagementsystem.Entities.AccountType;

@Data
public class AccountResponse {
    private Long id;
    private AccountType type; // Тип (SAVINGS, CHECKING)
    private String accountNumber;
    private Double balance;
}
