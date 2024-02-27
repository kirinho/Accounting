package com.petProject.Accounting.service;

import com.petProject.Accounting.entities.Transaction;
import com.petProject.Accounting.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getTransactionsByBudgetId(Long budgetId){
        return transactionRepository.findByBudgetId(budgetId);
    }

    public void deleteTransaction(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }

}
