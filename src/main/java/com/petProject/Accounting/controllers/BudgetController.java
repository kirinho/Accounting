package com.petProject.Accounting.controllers;

import com.petProject.Accounting.entities.Budget;
import com.petProject.Accounting.entities.User;
import com.petProject.Accounting.service.BudgetService;
import com.petProject.Accounting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/updateBudgetName")
    public String updateBudgetName(
            @RequestParam("budgetId") Long budgetId,
            @RequestParam("updatedName") String updatedName,
            Authentication authentication
    ) {
        User user = userService.getUserFromAuthentication(authentication);
        Budget existingBudget = budgetService.getBudgetById(budgetId);
        if (existingBudget != null && existingBudget.getUser().equals(user)) {
            existingBudget.setName(updatedName);
            budgetService.saveOrUpdateBudget(existingBudget);
        }
        return "redirect:/createBudgets";
    }

    @GetMapping("/deleteBudget/{budgetId}")
    public String deleteBudget(@PathVariable Long budgetId) {
        budgetService.deleteBudget(budgetId);

        return "redirect:/createBudgets";
    }

}

