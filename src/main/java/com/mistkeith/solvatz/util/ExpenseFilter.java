package com.mistkeith.solvatz.util;

import java.util.ArrayList;
import java.util.List;

import com.mistkeith.solvatz.model.Expense;

public class ExpenseFilter {

    private List<Expense> expenseList = new ArrayList<Expense>();

    public ExpenseFilter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    public void search(String searching) {
        if (searching == null || searching.isEmpty())
            return;
        searching = searching.toLowerCase();
        List<Expense> searchResults = new ArrayList<Expense>();
        for (Expense expense : expenseList) {
            // search filter
            if (expense.getCurrency().toLowerCase().contains(searching) ||
                    expense.getCategory().toLowerCase().contains(searching))
                searchResults.add(expense);
        }
        expenseList = searchResults;
    }

    public void setExpenses(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    public List<Expense> getExpenses() {
        return expenseList;
    }
}
