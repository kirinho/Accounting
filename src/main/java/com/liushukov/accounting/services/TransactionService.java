package com.liushukov.accounting.services;

import com.liushukov.accounting.dtos.TransactionDTO;
import com.liushukov.accounting.models.Budget;
import com.liushukov.accounting.models.Transaction;
import com.liushukov.accounting.models.TransactionType;
import com.liushukov.accounting.models.User;
import com.liushukov.accounting.repositories.TransactionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
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

    public List<Transaction> getAllTransactionsByBudget(Budget budget, List<String> sortBy, String order, int pageNumber, int pageSize){
        Pageable pageable;
        Sort sort = Sort.by(sortBy.stream().map(Sort.Order::by).toArray(Sort.Order[]::new));
        if (order.equalsIgnoreCase("desc")) {
            sort = sort.descending();
        }
        pageable = PageRequest.of(pageNumber, pageSize, sort);
        return transactionRepository.findTransactionsByBudget(budget, pageable);
    }

    public Transaction getTransactionById(User user, long id){
        if (user.isAccountNonExpired() && user.isAccountNonLocked() && user.isEnabled()) {
            return getTransactionById(id);
        } return null;
    }

    public Transaction saveTransaction(User user, Budget budget, TransactionDTO transactionDTO){
        if (user.isAccountNonExpired() && user.isAccountNonLocked() && user.isEnabled()){
            BigDecimal adjustedAmount = adjustAmountByTransactionType(transactionDTO.amount(),
                    transactionDTO.transactionType());
            var transaction = new Transaction(
                    transactionDTO.description(),
                    adjustedAmount,
                    budget
            );
            return transactionRepository.save(transaction);
        } return null;
    }

    public Transaction updateTransaction(User user, Budget budget, Transaction transaction, TransactionDTO transactionDTO){
        if (user.isAccountNonExpired() && user.isAccountNonLocked() && user.isEnabled()){
            BigDecimal adjustedAmount = adjustAmountByTransactionType(transactionDTO.amount(),
                    transactionDTO.transactionType());
            transaction.setDescription(transactionDTO.description());
            transaction.setAmount(adjustedAmount);
            transaction.setBudget(budget);
            return transactionRepository.save(transaction);
        } return null;
    }

    public boolean deleteTransaction(User user, long id){
        if (user.isAccountNonExpired() && user.isAccountNonLocked() && user.isEnabled()) {
            transactionRepository.deleteById(id);
            return true;
        } return false;
    }

    private BigDecimal adjustAmountByTransactionType(BigDecimal amount, TransactionType transactionType) {
        return (transactionType == TransactionType.POSITIVE) ? amount : amount.negate();
    }
}
