package com.mistkeith.solvatz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mistkeith.solvatz.model.Expense;

@Repository
public interface IExpenseRepository extends JpaRepository<Expense, Long> {

}