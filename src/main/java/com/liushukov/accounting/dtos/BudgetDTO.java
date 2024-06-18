package com.liushukov.accounting.dtos;

import com.liushukov.accounting.models.User;

public record BudgetDTO(String name, User user) {}
