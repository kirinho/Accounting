package com.liushukov.accounting.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private BigDecimal amount;

    @CreationTimestamp
    @Column(updatable = false, name = "added_at")
    private Date addedAt;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;

    public Transaction(String description, BigDecimal amount, Budget budget) {
        this.description = description;
        this.amount = amount;
        this.budget = budget;
    }
}
