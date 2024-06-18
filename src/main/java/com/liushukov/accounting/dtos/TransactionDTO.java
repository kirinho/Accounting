package com.liushukov.accounting.dtos;

import com.liushukov.accounting.models.Budget;
import com.liushukov.accounting.models.TransactionType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record TransactionDTO(
        @Size(max = 255, message = "Description should not exceed 255 characters")
        String description,
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
        BigDecimal amount,
        TransactionType transactionType,
        Budget budget,
        @Positive(message = "Budget ID must be a positive number")
        Long budgetId
) {}
