package org.example.bankmanagementsystem.dto.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionDepositRequest {
    @NotNull(message = "Account ID cannot be null")
    private String sender;
    @Min(value = 0, message = "Amount must be non-negative")
    private Double amount;
    private String recipient;
}
