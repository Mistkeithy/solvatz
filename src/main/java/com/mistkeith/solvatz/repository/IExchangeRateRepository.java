package com.mistkeith.solvatz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mistkeith.solvatz.model.ExchangeRate;

@Repository
public interface IExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

}