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


import java.util.*;



@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/account")
    public String account(Authentication authentication, Model model) {
        User user = userService.getUserFromAuthentication(authentication);

        model.addAttribute("username", user.getUsername());
        if (user != null) {
            model.addAttribute("name", user.getName());
            model.addAttribute("surname", user.getSurname());
            model.addAttribute("dateOfBirth", user.getDateOfBirth());
            if(user.getQuotes() != null){
                model.addAttribute("quotes", user.getQuotes());
            }
            else{
                model.addAttribute("quotes", "Input some quotes if you wanna");
            }
            if (user.getPhoto() != null) {
                String base64Photo = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(user.getPhoto());
                model.addAttribute("userPhoto", base64Photo);
            } else {
                model.addAttribute("userPhoto", "images/userAva.png");
            }
            List<Budget> budgets = budgetService.getAllBudgetsByUser(user);
            Map<String, List<Transaction>> budgetTransactions = new HashMap<>();
            List<Transaction> transactions;
            for (Budget budget : budgets) {
                transactions = transactionRepository.findByBudget(budget);
                Collections.reverse(transactions);
                budgetTransactions.put(budget.getName(), transactions);
            }

            model.addAttribute("budgetTransactions", budgetTransactions);
        }
        return "account";
    }
}

