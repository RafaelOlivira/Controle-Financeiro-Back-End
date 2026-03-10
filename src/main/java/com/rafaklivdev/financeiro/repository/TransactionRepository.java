package com.rafaklivdev.financeiro.repository;

import com.rafaklivdev.financeiro.model.Transaction;
import com.rafaklivdev.financeiro.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    @Query("SELECT SUM(t.value) FROM Transaction t WHERE t.type = 'INCOME'")
    BigDecimal sumIncome();

    @Query("SELECT SUM(t.value) FROM Transaction t WHERE t.type = 'EXPENSE'")
    BigDecimal sumExpense();

    List<Transaction> findByType(TransactionType type);
}
