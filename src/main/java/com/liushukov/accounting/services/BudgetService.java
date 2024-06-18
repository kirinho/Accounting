package com.liushukov.accounting.services;

import com.liushukov.accounting.dtos.BudgetDTO;
import com.liushukov.accounting.models.Budget;
import com.liushukov.accounting.models.User;
import com.liushukov.accounting.repositories.BudgetRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public Budget getBudgetById(long id){
        return budgetRepository.findById(id).orElse(null);
    }

    public Budget getBudgetById(User user, long id){
        if (user.isAccountNonExpired() && user.isAccountNonLocked() && user.isEnabled()) {
            return getBudgetById(id);
        }
        return null;
    }

    public List<Budget> getAllBudgetsByUser(User user, String sortBy, String order, int pageNumber, int pageSize) {
        Pageable pageable;
        switch (order) {
            case "desc" -> pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
            default -> pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        }
        return budgetRepository.findByUser(user, pageable);
    }

    public Budget saveBudget(User user, BudgetDTO budgetDTO){
        if (user.isAccountNonExpired() && user.isAccountNonLocked() && user.isEnabled()){
            var budget = new Budget(
                    budgetDTO.name(),
                    user
            );
            return budgetRepository.save(budget);
        }
        return null;
    }

    public Budget updateBudget(User user, Budget budget, BudgetDTO budgetDTO){
        if (user.isAccountNonExpired() && user.isAccountNonLocked() && user.isEnabled()){
            budget.setName(budgetDTO.name());
            budget.setUser(user);
            return budgetRepository.save(budget);
        }
        return null;
    }

    public boolean deleteBudget(User user, long id){
        if (user.isAccountNonExpired() && user.isAccountNonLocked() && user.isEnabled()) {
            budgetRepository.deleteById(id);
            return true;
        }
        return false;
    }
}