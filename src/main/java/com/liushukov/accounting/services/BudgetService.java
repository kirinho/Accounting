package com.liushukov.accounting.services;

import com.liushukov.accounting.dtos.BudgetDTO;
import com.liushukov.accounting.dtos.BudgetSortFields;
import com.liushukov.accounting.models.Budget;
import com.liushukov.accounting.models.User;
import com.liushukov.accounting.repositories.BudgetRepository;
import org.springframework.stereotype.Service;
import java.util.Comparator;
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
    public List<Budget> getAllBudgetsByUser(User user, BudgetSortFields sortFields, String order) {
        List<Budget> budgets = budgetRepository.findByUser(user);
        Comparator<Budget> comparator = switch (sortFields) {
            case NAME -> Comparator.comparing(Budget::getName);
            default -> Comparator.comparing(Budget::getId);
        };
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        budgets.sort(comparator);
        return budgets;
    }
    public Budget saveBudget(User user, BudgetDTO budgetDTO){
        if (user.isAccountNonExpired() && user.isAccountNonLocked() && user.isEnabled()){
            var budget = new Budget(
                    budgetDTO.getName(),
                    user
            );
            return budgetRepository.save(budget);
        }
        return null;
    }
    public Budget updateBudget(User user, BudgetDTO budgetDTO){
        if (user.isAccountNonExpired() && user.isAccountNonLocked() && user.isEnabled()){
            var budget = getBudgetById(budgetDTO.getId());
            if (budget != null){
                budget.setName(budgetDTO.getName());
                budget.setUser(user);
                return budgetRepository.save(budget);
            }
        }
        return null;
    }
    public boolean deleteBudget(User user, long id){
        if (user.isAccountNonExpired() && user.isAccountNonLocked() && user.isEnabled()) {
            var budget = getBudgetById(id);
            if (budget != null) {
                budgetRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }
}
