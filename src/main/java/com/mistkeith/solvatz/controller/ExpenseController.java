package com.mistkeith.solvatz.controller;

import org.hibernate.boot.archive.scan.spi.ClassDescriptor.Categorization;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mistkeith.solvatz.model.Expense;
import com.mistkeith.solvatz.repository.IExpenseRepository;
import com.mistkeith.solvatz.util.ExpenseFilter;

@RestController
public class ExpenseController {

    @Autowired
    private IExpenseRepository expenseRepository;

    public ExpenseController(IExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping("/expense")
    public ResponseEntity<Iterable<Expense>> getExpenses(String currency, String category) {

        // load all expenses to Expense Filter
        ExpenseFilter expenseFilter = new ExpenseFilter(this.expenseRepository.findAll());

        // filter by params
        expenseFilter.search(currency);
        expenseFilter.search(category);

        // return all data from db in json
        return ResponseEntity.status(HttpStatus.FOUND).body(expenseFilter.getExpenses());
    }

    @PostMapping("/expense")
    public ResponseEntity<Expense> newExpense(Double amount, String currency, String date, String description,
            String category) {

        // request is empty
        if (amount == null && currency == null && date == null && category == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(expenseRepository.save(new Expense(amount, currency, date, description, category)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/expense/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable(value = "id") Long id) {

        Optional<Expense> expense = expenseRepository.findById(id);

        // Does it exist?
        if (expense.isPresent())
            return ResponseEntity.ok(expense.get());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
