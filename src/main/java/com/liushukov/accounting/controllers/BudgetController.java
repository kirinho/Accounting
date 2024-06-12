package com.liushukov.accounting.controllers;

import com.liushukov.accounting.dtos.BudgetDTO;
import com.liushukov.accounting.dtos.BudgetSortFields;
import com.liushukov.accounting.services.BudgetService;
import com.liushukov.accounting.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestMapping("/api/budgets")
@RestController
public class BudgetController {
    private static final Logger logger = LoggerFactory.getLogger(BudgetController.class);
    private final UserService userService;
    private final BudgetService budgetService;

    public BudgetController(UserService userService, BudgetService budgetService) {
        this.userService = userService;
        this.budgetService = budgetService;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> viewBudgets(
            Authentication authentication,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order){
        logger.info("entrance to view budgets method");
        var user = userService.getUserFromAuthentication(authentication);
        logger.info("getting user");
        var sortField = BudgetSortFields.fromString(sortBy);
        var response = budgetService.getAllBudgetsByUser(user, sortField, order);
        logger.info("getting response and returning response in view budgets method");
        return (!response.isEmpty()) ? ResponseEntity.ok(response) : ResponseEntity.ok("You have no budgets");
    }

    @GetMapping("/view-budget")
    public ResponseEntity<Object> viewBudget(Authentication authentication,  @RequestBody BudgetDTO budgetDTO){
        logger.info("entrance to view budget method");
        var user = userService.getUserFromAuthentication(authentication);
        logger.info("getting user");
        var response = budgetService.getBudgetById(user, budgetDTO.getId());
        logger.info("getting response and returning response in view budget method");
        return (response != null) ? ResponseEntity.ok(response) : ResponseEntity.ok("Budget doesn't exist!");
    }
    @PostMapping("/add-budget")
    public ResponseEntity<Object> addBudget(Authentication authentication, @RequestBody BudgetDTO budgetDTO){
        logger.info("entrance to add budget method");
        var user = userService.getUserFromAuthentication(authentication);
        logger.info("getting user");
        var response = budgetService.saveBudget(user, budgetDTO);
        logger.info("getting response and returning response in add budget method");
        return (response != null) ? ResponseEntity.ok(response) : ResponseEntity.ok("User isn't valid");
    }
    @PutMapping("/update-budget")
    public ResponseEntity<Object> updateBudget(Authentication authentication, @RequestBody BudgetDTO budgetDTO){
        logger.info("entrance to update budget method");
        var user = userService.getUserFromAuthentication(authentication);
        logger.info("getting user");
        var response = budgetService.updateBudget(user, budgetDTO);
        logger.info("getting response and returning response in update budget method");
        return (response != null)
                ? ResponseEntity.ok(response)
                : ResponseEntity.ok("The budget couldn't be updated for some reason");
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteBudget(Authentication authentication, @RequestBody BudgetDTO budgetDTO){
        logger.info("entrance to delete budget method");
        var user = userService.getUserFromAuthentication(authentication);
        logger.info("getting user");
        var response = budgetService.deleteBudget(user, budgetDTO.getId());
        logger.info("getting response and returning response in delete budget method");
        return (response)
                ? ResponseEntity.ok("Successfully deleted budget!")
                : ResponseEntity.ok("Budget couldn't be deleted for some reason :(");
    }
}
