package org.example.bankmanagementsystem.dto.responses;

import lombok.Data;
import org.example.bankmanagementsystem.Entities.TransactionType;

import java.time.LocalDateTime;

@Data
public class TransactionResponse {
    private TransactionType type;

    private Double amount;

    private LocalDateTime timestamp;
}
