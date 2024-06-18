package com.liushukov.accounting.repositories;

import com.liushukov.accounting.models.Budget;
import com.liushukov.accounting.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUser(User user, Pageable pageable);
}
