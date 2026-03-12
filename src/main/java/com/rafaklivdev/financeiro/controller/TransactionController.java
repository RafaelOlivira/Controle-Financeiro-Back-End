package com.rafaklivdev.financeiro.controller;


import com.rafaklivdev.financeiro.dto.BalanceResponse;
import com.rafaklivdev.financeiro.model.Transaction;
import com.rafaklivdev.financeiro.model.TransactionType;
import com.rafaklivdev.financeiro.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping // Salvar no banco
    public Transaction createTransaction(@Valid @RequestBody Transaction transaction){
        return transactionService.save(transaction);
    }
    @GetMapping
    public List<Transaction> getAllTransactions(){ // Consultar todas as transações
        return transactionService.findAll();
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable Long id){ // Consultar por ID
        return transactionService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Transaction not found"));
    }
    @GetMapping("/balance")
    public BalanceResponse getBalance(){
        return transactionService.getBalance();
    }
    @GetMapping("/type/{type}")
    public List<Transaction> findByType(@PathVariable TransactionType type){
        return transactionService.findByType(type);
    }

    @GetMapping("/date")
    public List<Transaction> findByMonthAndYear(@RequestParam int month, @RequestParam int year){
        return transactionService.findByMonthAndYear(month,year);
    }

    @GetMapping("/search")
    public List<Transaction> findByTypeAndMonthAndYear(@RequestParam TransactionType type,
                                                       @RequestParam int month,
                                                       @RequestParam int year){
        return transactionService.findByTypeAndMonthAndYear(type,month,year);
    }

    @GetMapping("/search/description") // Ao digitar a descrição irá buscar no banco
    public List<Transaction> findBySearchDescription(@RequestParam TransactionType type,
                                                     @RequestParam int month,
                                                     @RequestParam int year,
                                                     @RequestParam String description){
        return transactionService.findBySearch(type,month,year,description);
    }

    @GetMapping("/search/description/all")
    public List<Transaction> findBySearchDescriptionAll(@RequestParam int month,
                                                        @RequestParam int year,
                                                        @RequestParam String description){
        return transactionService.findBySearchAll(month,year,description);

    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id){ // Deletar por ID
        if(transactionService.findById(id).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Transaction not found");
        }
        transactionService.deleteById(id);
    }

    @PutMapping("/{id}")
    public Transaction updateTransaction(@PathVariable Long id, @Valid @RequestBody Transaction transaction){
        return transactionService.updateTransaction(id,transaction);
    }
}
