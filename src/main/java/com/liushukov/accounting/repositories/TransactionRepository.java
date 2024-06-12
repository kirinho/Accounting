package com.liushukov.accounting.repositories;

import com.liushukov.accounting.models.Budget;
import com.liushukov.accounting.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTransactionsByBudget(Budget budget);
}
