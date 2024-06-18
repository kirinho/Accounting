package com.liushukov.accounting.controllers;

import com.liushukov.accounting.dtos.BudgetDTO;
import com.liushukov.accounting.services.BudgetService;
import com.liushukov.accounting.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.CannotCreateTransactionException;
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
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
            ){
        try {
            logger.info("entrance to view budgets method");
            var user = userService.getUserFromAuthentication(authentication);
            logger.info("getting user");
            var response = budgetService.getAllBudgetsByUser(user, sortBy, order, pageNumber, pageSize);
            logger.info("getting response and returning response in view budgets method");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (CannotCreateTransactionException exception) {
            logger.error("exception in connectivity with database in view budgets method: " + exception);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connectivity exception");
        } catch (IllegalArgumentException exception){
            logger.error("invalid request data in view budgets method: " + exception);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request data");
        } catch (Exception exception) {
            logger.error("unexpected error in view budgets method: " + exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("/view-budget/{budgetId}")
    public ResponseEntity<Object> viewBudget(Authentication authentication,  @PathVariable Long budgetId){
        try {
            logger.info("entrance to view budget method");
            var user = userService.getUserFromAuthentication(authentication);
            logger.info("getting user");
            var response = budgetService.getBudgetById(user, budgetId);
            logger.info("getting response and returning response in view budget method");
            return (response != null)
                    ? ResponseEntity.status(HttpStatus.OK).body(response)
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Budget not found");
        } catch (CannotCreateTransactionException exception) {
            logger.error("exception in connectivity with database in view budget method: " + exception);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connectivity exception");
        } catch (IllegalArgumentException exception){
            logger.error("invalid request data in view budget method: " + exception);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request data");
        } catch (Exception exception) {
            logger.error("unexpected error in view budget method: " + exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PostMapping("/add-budget")
    public ResponseEntity<Object> addBudget(Authentication authentication, @RequestBody BudgetDTO budgetDTO){
        try {
            logger.info("entrance to add budget method");
            var user = userService.getUserFromAuthentication(authentication);
            logger.info("getting user");
            var response = budgetService.saveBudget(user, budgetDTO);
            logger.info("getting response and returning response in add budget method");
            return (response != null)
                    ? ResponseEntity.status(HttpStatus.CREATED).body(response)
                    : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is unauthorized");
        } catch (CannotCreateTransactionException exception) {
            logger.error("exception in connectivity with database in add budget method: " + exception);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connectivity exception");
        } catch (IllegalArgumentException exception){
            logger.error("invalid request data in add budget method: " + exception);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request data");
        } catch (Exception exception) {
            logger.error("unexpected error in add budget method: " + exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PutMapping("/update-budget/{budgetId}")
    public ResponseEntity<Object> updateBudget(Authentication authentication,
                                               @PathVariable Long budgetId,
                                               @RequestBody BudgetDTO budgetDTO){
        try {
            logger.info("entrance to update budget method");
            var user = userService.getUserFromAuthentication(authentication);
            logger.info("getting user");
            var budget = budgetService.getBudgetById(budgetId);
            if (budget != null) {
                var response = budgetService.updateBudget(user, budget, budgetDTO);
                logger.info("getting response and returning response in update budget method");
                return (response != null)
                        ? ResponseEntity.status(HttpStatus.OK).body(response)
                        : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is unauthorized");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Budget not found");
        } catch (CannotCreateTransactionException exception) {
            logger.error("exception in connectivity with database in update budget method: " + exception);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connectivity exception");
        } catch (IllegalArgumentException exception){
            logger.error("invalid request data in update budget method: " + exception);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request data");
        } catch (Exception exception) {
            logger.error("unexpected error in update budget method: " + exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/delete/{budgetId}")
    public ResponseEntity<Object> deleteBudget(Authentication authentication, @PathVariable Long budgetId){
        try {
            logger.info("entrance to delete budget method");
            var user = userService.getUserFromAuthentication(authentication);
            logger.info("getting user");
            var budget = budgetService.getBudgetById(budgetId);
            if (budget != null) {
                var response = budgetService.deleteBudget(user, budgetId);
                logger.info("getting response and returning response in delete budget method");
                return (response)
                        ? ResponseEntity.status(HttpStatus.OK).body("Successfully deleted budget")
                        : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is unauthorized");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Budget not found");
        } catch (CannotCreateTransactionException exception) {
            logger.error("exception in connectivity with database in delete budget method: " + exception);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connectivity exception");
        } catch (IllegalArgumentException exception){
            logger.error("invalid request data in delete budget method: " + exception);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request data");
        } catch (Exception exception) {
            logger.error("unexpected error in delete budget method: " + exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}
