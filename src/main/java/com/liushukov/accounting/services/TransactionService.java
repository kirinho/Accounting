package com.liushukov.accounting.services;

import com.liushukov.accounting.dtos.TransactionDTO;
import com.liushukov.accounting.dtos.TransactionSortFields;
import com.liushukov.accounting.models.Budget;
import com.liushukov.accounting.models.Transaction;
import com.liushukov.accounting.models.TransactionType;
import com.liushukov.accounting.models.User;
import com.liushukov.accounting.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    public Transaction getTransactionById(long id){
        return transactionRepository.findById(id).orElse(null);
    }
    public List<Transaction> getAllTransactionsByBudget(Budget budget, TransactionSortFields sortFields, String order){
        List<Transaction> transactions = transactionRepository.findTransactionsByBudget(budget);
        Comparator<Transaction> comparator = switch (sortFields) {
            case DESCRIPTION -> Comparator.comparing(Transaction::getDescription);
            case AMOUNT -> Comparator.comparing(Transaction::getAmount);
            case ADDEDAT -> Comparator.comparing(Transaction::getAddedAt);
            default -> Comparator.comparing(Transaction::getId);
        };
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        transactions.sort(comparator);
        return transactions;
    }
    public Transaction getTransactionById(User user, Budget budget, long id){
        if (user.isAccountNonExpired() && user.isAccountNonLocked() && user.isEnabled()) {
            if (budget != null) {
                return getTransactionById(id);
            }
        } return null;
    }
    public Transaction saveTransaction(User user, Budget budget, TransactionDTO transactionDTO){
        if (user.isAccountNonExpired() && user.isAccountNonLocked() && user.isEnabled()){
            if (budget != null) {
                BigDecimal adjustedAmount = adjustAmountByTransactionType(transactionDTO.getAmount(),
                        transactionDTO.getTransactionType());
                var transaction = new Transaction(
                        transactionDTO.getDescription(),
                        adjustedAmount,
                        budget
                );
                return transactionRepository.save(transaction);
            }
        } return null;
    }
    public Transaction updateTransaction(User user, Budget budget, long id, TransactionDTO transactionDTO){
        if (user.isAccountNonExpired() && user.isAccountNonLocked() && user.isEnabled()){
            if (budget != null) {
                var transaction = getTransactionById(id);
                if (transaction != null){
                    BigDecimal adjustedAmount = adjustAmountByTransactionType(transactionDTO.getAmount(),
                            transactionDTO.getTransactionType());
                    transaction.setDescription(transactionDTO.getDescription());
                    transaction.setAmount(adjustedAmount);
                    transaction.setBudget(budget);
                    return transactionRepository.save(transaction);
                }
            }
        } return null;
    }
    public boolean deleteTransaction(User user, Budget budget, long id){
        if (user.isAccountNonExpired() && user.isAccountNonLocked() && user.isEnabled()) {
            if (budget != null) {
                var transaction = getTransactionById(id);
                if (transaction != null){
                    transactionRepository.deleteById(id);
                    return true;
                }
            }
        } return false;
    }
    private BigDecimal adjustAmountByTransactionType(BigDecimal amount, TransactionType transactionType) {
        return (transactionType == TransactionType.POSITIVE) ? amount : amount.negate();
    }
}
