package com.liushukov.accounting.dtos;

import com.liushukov.accounting.models.User;
import lombok.Data;

@Data
public class BudgetDTO {
    private long id;
    private String name;
    private User user;
}
