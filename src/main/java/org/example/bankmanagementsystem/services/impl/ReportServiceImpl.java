package org.example.bankmanagementsystem.services.impl;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.example.bankmanagementsystem.Entities.Transaction;
import org.example.bankmanagementsystem.repositories.TransactionRepository;
import org.example.bankmanagementsystem.services.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {
    private final TransactionRepository transactionRepository;
    @Override
    @Transactional(readOnly = true)
    public byte[] generateAccountStatement(Long accountId) {
        // Изменяем запрос так, чтобы выбирались транзакции, где account является либо отправителем, либо получателем.
        List<Transaction> transactions = transactionRepository.findByAccountIdOrRecipientAccountId(accountId, accountId);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("Account Statement - Account ID: " + accountId);
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                int y = 720;
                for (Transaction transaction : transactions) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, y);
                    StringBuilder line = new StringBuilder();
                    line.append(transaction.getTimestamp())
                            .append(" | ")
                            .append(transaction.getType())
                            .append(" | ")
                            .append(transaction.getAmount());
                    // Выводим идентификатор получателя вместо вызова toString() всего объекта
                    if (transaction.getRecipientAccount() != null) {
                        line.append(" | Recipient ID: ").append(transaction.getRecipientAccount().getId());
                    }
                    contentStream.showText(line.toString());
                    contentStream.endText();
                    y -= 20;
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generating PDF report", e);
        }
    }
}
