package com.mistkeith.solvatz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mistkeith.solvatz.model.MonthlyLimit;

@Repository
public interface IMonthlyLimitRepository extends JpaRepository<MonthlyLimit, Long> {

}
