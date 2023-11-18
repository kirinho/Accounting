package com.petProject.Accounting.repositories;

import com.petProject.Accounting.entities.Budget;

import com.petProject.Accounting.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUser(User user);

}
