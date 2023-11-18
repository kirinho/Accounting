package com.petProject.Accounting.controllers;

import com.petProject.Accounting.entities.Budget;
import com.petProject.Accounting.entities.Transaction;
import com.petProject.Accounting.entities.User;
import com.petProject.Accounting.service.BudgetService;
import com.petProject.Accounting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Base64;

@Controller
public class BudgetController {

    @Autowired
    private UserService userService;

    @Autowired
    private BudgetService budgetService;

    @GetMapping("/createBudgets")
    public String showCreateBudgetsPage(Model model, Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        model.addAttribute("user", user);
        if (user.getPhoto() != null) {
            String base64Photo = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(user.getPhoto());
            model.addAttribute("userPhoto", base64Photo);
        } else {
            model.addAttribute("userPhoto", "images/userAva.png");
        }
        return "createBudgets";
    }

    @PostMapping("/createBudget")
    public String createBudget(@RequestParam("budgetName") String budgetName, Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);

        Budget budget = new Budget();
        budget.setName(budgetName);
        budget.setUser(user);
        budgetService.saveOrUpdateBudget(budget);

        return "redirect:/createBudgets";
    }


    @GetMapping("/editBudget/{budgetId}")
    public String editBudget(@PathVariable Long budgetId, Model model, Authentication authentication) {
        Budget budget = budgetService.getBudgetById(budgetId);

        model.addAttribute("budget", budget);
        User user = userService.getUserFromAuthentication(authentication);
        if (user.getPhoto() != null) {
            String base64Photo = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(user.getPhoto());
            model.addAttribute("userPhoto", base64Photo);
        } else {
            model.addAttribute("userPhoto", "images/userAva.png");
        }

        return "editBudget";
    }


    @GetMapping("/deleteBudget/{budgetId}")
    public String deleteBudget(@PathVariable Long budgetId) {
        budgetService.deleteBudget(budgetId);

        return "redirect:/createBudgets";
    }
    @PostMapping("/saveTransaction")
    public String saveTransaction(@RequestParam Long budgetId,
                                  @RequestParam String transactionType,
                                  @RequestParam String transactionDescription,
                                  @RequestParam BigDecimal transactionAmount,
                                  RedirectAttributes redirectAttributes) {
        Budget budget = budgetService.getBudgetById(budgetId);

        if ("minus".equals(transactionType)) {
            transactionAmount = transactionAmount.negate();
        }

        Transaction transaction = new Transaction();
        transaction.setDescription(transactionDescription);
        transaction.setAmount(transactionAmount);
        transaction.setBudget(budget);

        budget.getTransactions().add(transaction);
        budgetService.saveOrUpdateBudget(budget);
        redirectAttributes.addFlashAttribute("successMessage", "Transaction saved successfully.");

        return "redirect:/editBudget/" + budgetId;
    }
}

