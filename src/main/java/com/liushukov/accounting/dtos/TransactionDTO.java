package com.liushukov.accounting.dtos;

import com.liushukov.accounting.models.Budget;
import com.liushukov.accounting.models.TransactionType;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionDTO {
    @Positive(message = "ID must be a positive number")
    private Long id;
    @Size(max = 255, message = "Description should not exceed 255 characters")
    private String description;
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal amount;
    private TransactionType transactionType;
    private Budget budget;
    @Positive(message = "Budget ID must be a positive number")
    private Long budgetId;
}
