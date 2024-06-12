package com.liushukov.accounting.controllers;

import com.liushukov.accounting.dtos.TransactionDTO;
import com.liushukov.accounting.dtos.TransactionSortFields;
import com.liushukov.accounting.services.BudgetService;
import com.liushukov.accounting.services.TransactionService;
import com.liushukov.accounting.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private final UserService userService;
    private final BudgetService budgetService;
    private final TransactionService transactionService;

    public TransactionController(UserService userService,
                                 BudgetService budgetService,
                                 TransactionService transactionService) {
        this.userService = userService;
        this.budgetService = budgetService;
        this.transactionService = transactionService;
    }
    @GetMapping("/transaction")
    public ResponseEntity<Object> viewTransaction(Authentication authentication,
                                                  @Valid @RequestBody TransactionDTO transactionDTO){
        logger.info("entrance to view transaction method");
        var user = userService.getUserFromAuthentication(authentication);
        logger.info("getting user");
        var budget = budgetService.getBudgetById(user, transactionDTO.getBudgetId());
        logger.info("getting budget");
        var response = transactionService.getTransactionById(user, budget, transactionDTO.getId());
        logger.info("got response in view transaction method");
        return (response != null) ? ResponseEntity.ok(response) : ResponseEntity.ok("Transaction doesn't exist");
    }
    @GetMapping("/all")
    public ResponseEntity<Object> viewTransactions(
            Authentication authentication,
            @Valid @RequestBody TransactionDTO transactionDTO,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order){
        logger.info("entrance to view transactions method");
        var user = userService.getUserFromAuthentication(authentication);
        logger.info("getting user");
        var budget = budgetService.getBudgetById(user, transactionDTO.getBudgetId());
        logger.info("getting budget");
        var sortField = TransactionSortFields.fromString(sortBy);
        var response = transactionService.getAllTransactionsByBudget(budget, sortField, order);
        logger.info("got response in view transactions method");
        return (!response.isEmpty())
                ? ResponseEntity.ok(response)
                : ResponseEntity.ok("You have no transactions in that budget");
    }
    @PostMapping("/add-transaction")
    public ResponseEntity<Object> addTransaction(Authentication authentication,
                                                 @Valid @RequestBody TransactionDTO transactionDTO){
        logger.info("entrance to add transaction method");
        var user = userService.getUserFromAuthentication(authentication);
        logger.info("getting user");
        var budget = budgetService.getBudgetById(user, transactionDTO.getBudgetId());
        logger.info("getting budget");
        var response = transactionService.saveTransaction(user, budget, transactionDTO);
        logger.info("got response in add transaction method");
        return (response != null)
                ? ResponseEntity.ok(response)
                : ResponseEntity.ok("Transaction couldn't be added to that budget");
    }
    @PutMapping("/update-transaction")
    public ResponseEntity<Object> updateTransaction(Authentication authentication,
                                                    @Valid @RequestBody TransactionDTO transactionDTO){
        logger.info("entrance to update transaction method");
        var user = userService.getUserFromAuthentication(authentication);
        logger.info("getting user");
        var budget = budgetService.getBudgetById(user, transactionDTO.getBudgetId());
        logger.info("getting budget");
        var response = transactionService.updateTransaction(user, budget, transactionDTO.getId(), transactionDTO);
        logger.info("got response in update transaction method");
        return (response != null)
                ? ResponseEntity.ok(response)
                : ResponseEntity.ok("Can't update transaction for some reason");
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteTransaction(Authentication authentication,
                                                    @Valid @RequestBody TransactionDTO transactionDTO){
        logger.info("entrance to delete transaction method");
        var user = userService.getUserFromAuthentication(authentication);
        logger.info("getting user");
        var budget = budgetService.getBudgetById(user, transactionDTO.getBudgetId());
        logger.info("getting budget");
        var response = transactionService.deleteTransaction(user, budget, transactionDTO.getId());
        logger.info("got response in delete transaction method");
        return (response)
                ? ResponseEntity.ok("Successfully deleted transaction")
                : ResponseEntity.ok("Transaction couldn't be deleted for some reason");
    }
}
