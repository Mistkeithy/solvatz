package com.mistkeith.solvatz.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MonthlyLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String month;
    private double goodsLimit;
    private double servicesLimit;

    public MonthlyLimit() {

    }

    public MonthlyLimit(String month, double goodsLimit, double servicesLimit) {
        this.month = month;
        this.goodsLimit = goodsLimit;
        this.servicesLimit = servicesLimit;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonth() {
        return month;
    }

    public void setGoodsLimit(double goodsLimit) {
        this.goodsLimit = goodsLimit;
    }

    public double getGoodsLimit() {
        return goodsLimit;
    }

    public void setServicesLimit(double servicesLimit) {
        this.servicesLimit = servicesLimit;
    }

    public double getServicesLimit() {
        return servicesLimit;
    }
}
