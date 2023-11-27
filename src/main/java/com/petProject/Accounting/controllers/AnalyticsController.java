package com.petProject.Accounting.controllers;

import com.petProject.Accounting.entities.Budget;
import com.petProject.Accounting.entities.Transaction;
import com.petProject.Accounting.entities.User;
import com.petProject.Accounting.repositories.TransactionRepository;
import com.petProject.Accounting.service.BudgetService;
import com.petProject.Accounting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.*;

@Controller
public class AnalyticsController {
    @Autowired
    private UserService userService;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private TransactionRepository transactionRepository;


    @GetMapping("/analytics")
    public String showAnalyticsPage(Model model, Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        if (user.getPhoto() != null) {
            String base64Photo = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(user.getPhoto());
            model.addAttribute("userPhoto", base64Photo);
        } else {
            model.addAttribute("userPhoto", "/images/userAva.png");
        }
        List<Budget> budgets = budgetService.getAllBudgetsByUser(user);
        Map<String, List<Transaction>> budgetTransactions = new HashMap<>();
        List<Transaction> transactions;
        ArrayList<BigDecimal> sumTransactions = new ArrayList<>();
        ArrayList<Integer> countTransactions = new ArrayList<>();
        int countOfTransactions = 0;
        for (Budget budget : budgets) {
            transactions = transactionRepository.findByBudget(budget);
            Collections.reverse(transactions);
            budgetTransactions.put(budget.getName(), transactions);
            BigDecimal summ = BigDecimal.ZERO;
            for (int i = 0; i < transactions.size(); i++) {
                summ = summ.add(transactions.get(i).getAmount());
                countOfTransactions += 1;
            }
            sumTransactions.add(summ);
            countTransactions.add(countOfTransactions);
            countOfTransactions = 0;
        }

        ArrayList<String> listOfKeys = new ArrayList<>(budgetTransactions.keySet());
        Collections.reverse(sumTransactions);
        BigDecimal[] sumTransactionsArray = sumTransactions.toArray(new BigDecimal[0]);
        String[] listOfKeysArray = listOfKeys.toArray(new String[0]);
        Collections.reverse(countTransactions);
        Integer[] countTransactionsArray = countTransactions.toArray(new Integer[0]);
        model.addAttribute("budgetTransactions", budgetTransactions);
        model.addAttribute("sumTransactions", sumTransactions);
        model.addAttribute("listOfKeysArray", listOfKeysArray);
        model.addAttribute("sumTransactionsArray", sumTransactionsArray);
        model.addAttribute("countTransactionsArray", countTransactionsArray);
        return "analytics";
    }

}
