package com.petProject.Accounting.service;

import com.petProject.Accounting.entities.Budget;
import com.petProject.Accounting.entities.Transaction;
import com.petProject.Accounting.entities.User;
import com.petProject.Accounting.repositories.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    public Budget getBudgetById(Long budgetId) {
        return budgetRepository.findById(budgetId).orElse(null);
    }

    public Budget saveOrUpdateBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    public List<Budget> getAllBudgetsByUser(User user) {
        return budgetRepository.findByUser(user);
    }

    public void deleteBudget(Long budgetId) {
        budgetRepository.deleteById(budgetId);
    }


}

