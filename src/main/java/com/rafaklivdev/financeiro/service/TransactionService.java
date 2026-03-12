package com.rafaklivdev.financeiro.service;

import com.rafaklivdev.financeiro.dto.BalanceResponse;
import com.rafaklivdev.financeiro.model.Transaction;
import com.rafaklivdev.financeiro.model.TransactionType;
import com.rafaklivdev.financeiro.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    public Transaction save(Transaction transaction){ // Salvar no banco
        return transactionRepository.save(transaction);
    }
    public List<Transaction> findAll(){ // Consultar transações
        return transactionRepository.findAll();
    }
    public Optional<Transaction> findById(Long id){ // Consultar por ID
        return transactionRepository.findById(id);
    }
    public void deleteById(Long id){ // Deletar por ID
        transactionRepository.deleteById(id);
    }
    public BalanceResponse getBalance(){ // Calcular os valores
        BigDecimal income = transactionRepository.sumIncome();
        BigDecimal expense = transactionRepository.sumExpense();
        if(income == null){
            income = BigDecimal.ZERO;
        }
        if(expense == null){
            expense = BigDecimal.ZERO;
        }
        BigDecimal balance = income.subtract(expense);
        return new BalanceResponse(income,expense,balance);
    }
    public Transaction updateTransaction(Long id, Transaction transaction){ // Atualizar transação
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Transaction not found"));
        existingTransaction.setDate(LocalDate.now());
        existingTransaction.setDescription(transaction.getDescription());
        existingTransaction.setType(transaction.getType());
        existingTransaction.setValue(transaction.getValue());
        return transactionRepository.save(existingTransaction);
    }

    public List<Transaction> findByType(TransactionType type){ // Buscar pelo Tipo
        return transactionRepository.findByType(type);
    }

    public List<Transaction> findByTypeAndMonthAndYear(TransactionType type, int month,int year){
        return transactionRepository.findByTypeAndMonthAndYear(type,month,year);
    }

    public List<Transaction> findByMonthAndYear(int month,int year){ // Busca personalizada por mês e ano
        return transactionRepository.findByMonthAndYear(month,year);
    }
    public List<Transaction> findBySearch(TransactionType type, int month, int year, String description){
        return transactionRepository.findBySearch(type,month,year,description);
    }
    public List<Transaction> findBySearchAll(int month, int year,String description){
        return transactionRepository.findBySearchAllType(month,year,description);
    }
}
