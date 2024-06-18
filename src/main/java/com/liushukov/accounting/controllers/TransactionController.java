package com.liushukov.accounting.controllers;

import com.liushukov.accounting.dtos.TransactionDTO;
import com.liushukov.accounting.services.BudgetService;
import com.liushukov.accounting.services.TransactionService;
import com.liushukov.accounting.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<Object> viewTransaction(Authentication authentication, @PathVariable Long transactionId){
        try {
            logger.info("entrance to view transaction method");
            var user = userService.getUserFromAuthentication(authentication);
            logger.info("getting user");
            var response = transactionService.getTransactionById(user, transactionId);
            logger.info("got response in view transaction method");
            return (response != null)
                    ? ResponseEntity.status(HttpStatus.OK).body(response)
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found");
        } catch (CannotCreateTransactionException exception) {
            logger.info("exception in connectivity with database in view transaction method");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connectivity exception");
        } catch (IllegalArgumentException exception){
            logger.info("invalid request data in view transaction method");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request data");
        } catch (Exception exception) {
            logger.info("unexpected error in view transaction method");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> viewTransactions(
            Authentication authentication,
            @Valid @RequestBody TransactionDTO transactionDTO,
            @RequestParam(defaultValue = "id") List<String> sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        try {
            logger.info("entrance to view transactions method");
            var user = userService.getUserFromAuthentication(authentication);
            logger.info("getting user");
            var budget = budgetService.getBudgetById(user, transactionDTO.budgetId());
            logger.info("getting budget");
            if (budget != null) {
                var response = transactionService.getAllTransactionsByBudget(budget, sortBy, order, pageNumber, pageSize);
                logger.info("got response in view transactions method");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Budget not found");
        } catch (CannotCreateTransactionException exception) {
            logger.info("exception in connectivity with database in view transactions method");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connectivity exception");
        } catch (IllegalArgumentException exception){
            logger.info("invalid request data in view transactions method");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request data");
        } catch (Exception exception) {
            logger.info("unexpected error in view transactions method");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PostMapping("/add-transaction/{budgetId}")
    public ResponseEntity<Object> addTransaction(Authentication authentication,
                                                 @PathVariable Long budgetId,
                                                 @Valid @RequestBody TransactionDTO transactionDTO){
        try {
            logger.info("entrance to add transaction method");
            var user = userService.getUserFromAuthentication(authentication);
            logger.info("getting user");
            var budget = budgetService.getBudgetById(user, budgetId);
            logger.info("getting budget");
            if (budget != null) {
                var response = transactionService.saveTransaction(user, budget, transactionDTO);
                logger.info("got response in add transaction method");
                return (response != null)
                        ? ResponseEntity.status(HttpStatus.CREATED).body(response)
                        : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is unauthorized");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Budget not found");
        } catch (CannotCreateTransactionException exception) {
            logger.info("exception in connectivity with database in add transaction method");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connectivity exception");
        } catch (IllegalArgumentException exception){
            logger.info("invalid request data in add transaction method");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request data");
        } catch (Exception exception) {
            logger.info("unexpected error in add transaction method");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PutMapping("/update-transaction/{transactionId}")
    public ResponseEntity<Object> updateTransaction(Authentication authentication,
                                                    @PathVariable Long transactionId,
                                                    @Valid @RequestBody TransactionDTO transactionDTO){
        try {
            logger.info("entrance to update transaction method");
            var user = userService.getUserFromAuthentication(authentication);
            logger.info("getting user");
            var transaction = transactionService.getTransactionById(transactionId);
            if (transaction != null) {
                var budget = budgetService.getBudgetById(user, transactionDTO.budgetId());
                logger.info("getting budget");
                if (budget != null) {
                    var response = transactionService.updateTransaction(user, budget, transaction, transactionDTO);
                    logger.info("got response in update transaction method");
                    return (response != null)
                            ? ResponseEntity.status(HttpStatus.OK).body(response)
                            : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is unauthorized");
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Budget not found");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found");
        } catch (CannotCreateTransactionException exception) {
            logger.info("exception in connectivity with database in update transaction method");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connectivity exception");
        } catch (IllegalArgumentException exception){
            logger.info("invalid request data in update transaction method");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request data");
        } catch (Exception exception) {
            logger.info("unexpected error in update transaction method");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/delete/{transactionId}")
    public ResponseEntity<Object> deleteTransaction(Authentication authentication, @PathVariable Long transactionId){
        try {
            logger.info("entrance to delete transaction method");
            var user = userService.getUserFromAuthentication(authentication);
            logger.info("getting user");
            var transaction = transactionService.getTransactionById(transactionId);
            if (transaction != null) {
                var response = transactionService.deleteTransaction(user, transactionId);
                logger.info("got response in delete transaction method");
                return (response)
                        ? ResponseEntity.status(HttpStatus.OK).body("Successfully deleted transaction")
                        : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is unauthorized");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found");
        } catch (CannotCreateTransactionException exception) {
            logger.info("exception in connectivity with database in delete transaction method");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database connectivity exception");
        } catch (IllegalArgumentException exception){
            logger.info("invalid request data in delete transaction method");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request data");
        } catch (Exception exception) {
            logger.info("unexpected error in delete transaction method");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}
