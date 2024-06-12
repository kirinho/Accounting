package com.liushukov.accounting.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionSortFields {
    ID("id"),
    DESCRIPTION("description"),
    AMOUNT("amount"),
    ADDEDAT("addedAt");

    private final String field;

    public static TransactionSortFields fromString(String field) {
        for (TransactionSortFields sortField : TransactionSortFields.values()) {
            if (sortField.getField().equalsIgnoreCase(field)) {
                return sortField;
            }
        }
        return ID;
    }
}
