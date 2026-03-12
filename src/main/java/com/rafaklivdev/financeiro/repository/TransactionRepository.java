package com.rafaklivdev.financeiro.repository;

import com.rafaklivdev.financeiro.model.Transaction;
import com.rafaklivdev.financeiro.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    @Query("SELECT SUM(t.value) FROM Transaction t WHERE t.type = 'INCOME'")
    BigDecimal sumIncome();

    @Query("SELECT SUM(t.value) FROM Transaction t WHERE t.type = 'EXPENSE'")
    BigDecimal sumExpense();

    @Query("""
    SELECT t FROM Transaction t
    WHERE EXTRACT(MONTH FROM t.date) = :month
    AND EXTRACT(YEAR FROM t.date) = :year
    """)
    List<Transaction> findByMonthAndYear(int month, int year);

    @Query("""
    SELECT t FROM Transaction t
    WHERE t.type = :type
    AND EXTRACT(MONTH FROM t.date) = :month
    AND EXTRACT(YEAR FROM t.date) = :year
""")
    List<Transaction> findByTypeAndMonthAndYear(
            @Param("type") TransactionType type,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("""
    SELECT t FROM Transaction t
    WHERE t.type = :type
    AND EXTRACT(MONTH FROM t.date) = :month
    AND EXTRACT(YEAR FROM t.date) = :year
    AND LOWER(t.description) LIKE LOWER(CONCAT("%", :description,"%"))
""")
    List<Transaction> findBySearch(
            @Param("type") TransactionType type,
            @Param("month") int month,
            @Param("year") int year,
            @Param("description") String description);



    @Query("""
    SELECT t FROM Transaction t
    WHERE EXTRACT(MONTH FROM t.date) = :month
    AND EXTRACT(YEAR FROM t.date) = :year
    AND LOWER(t.description) LIKE LOWER(CONCAT("%", :description,"%"))
""")
    List<Transaction> findBySearchAllType(
            @Param("month") int month,
            @Param("year") int year,
            @Param("description") String description);

    List<Transaction> findByType(TransactionType type);
}
