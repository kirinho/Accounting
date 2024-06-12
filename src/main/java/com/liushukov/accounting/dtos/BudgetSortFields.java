package com.liushukov.accounting.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BudgetSortFields {
    ID("id"),
    NAME("name");

    private final String field;

    public static BudgetSortFields fromString(String field) {
        for (BudgetSortFields sortField : BudgetSortFields.values()) {
            if (sortField.getField().equalsIgnoreCase(field)) {
                return sortField;
            }
        }
        return ID;
    }
}
