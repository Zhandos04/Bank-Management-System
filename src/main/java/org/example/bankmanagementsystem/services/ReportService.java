package org.example.bankmanagementsystem.services;

public interface ReportService {
    byte[] generateAccountStatement(Long accountId);
}
