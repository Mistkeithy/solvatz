package com.mistkeith.solvatz.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String currency;
    private String date;
    private Double rate;

    // Default constructor
    public ExchangeRate() {
    }

    public ExchangeRate(String currency, Double rate, String date) {
        this.currency = currency;
        this.date = date;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}