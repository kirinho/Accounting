package com.petProject.Accounting.controllers;

import com.petProject.Accounting.entities.Budget;
import com.petProject.Accounting.entities.Transaction;
import com.petProject.Accounting.entities.User;
import com.petProject.Accounting.service.BudgetService;
import com.petProject.Accounting.service.TransactionService;
import com.petProject.Accounting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

@Controller
public class TransactionController {
    @Autowired
    private UserService userService;

    @Autowired
    private BudgetService budgetService;
    @Autowired
    private TransactionService transactionService;


    @GetMapping("/editBudget/{budgetId}")
    public String editBudget(@PathVariable Long budgetId, Model model, Authentication authentication) {
        Budget budget = budgetService.getBudgetById(budgetId);
        model.addAttribute("budget", budget);
        User user = userService.getUserFromAuthentication(authentication);
        if (user.getPhoto() != null) {
            String base64Photo = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(user.getPhoto());
            model.addAttribute("userPhoto", base64Photo);
        } else {
            model.addAttribute("userPhoto", "/images/userAva.png");
        }
        List<Transaction> transactions = transactionService.getTransactionsByBudgetId(budgetId);
        model.addAttribute("transactions", transactions);
        return "editBudget";
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

    @GetMapping("/deleteTransaction/{transactionId}")
    public String deleteTransaction(@PathVariable Long transactionId, @RequestParam Long budgetId) {
        transactionService.deleteTransaction(transactionId);
        return "redirect:/editBudget/" + budgetId;
    }
}
