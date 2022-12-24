package com.mistkeith.solvatz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

public class ExchangeRateController {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @GetMapping("/exchange-rates")
    public Iterable<ExchangeRate> getExchangeRates() {
        // Return all exchange rates from the database
        return exchangeRateRepository.findAll();
    }

    @PostMapping("/exchange-rates")
    public void addExchangeRate(@RequestParam String currency) {
        // Make a request to the Alpha Vantage API to get the current exchange rate for
        // the specified currency
        String apiKey = "96PFC2ULHIGBJQA5";
        String url = "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=" + currency
                + "&to_currency=KZT&apikey=" + apiKey;
        String jsonResponse = sendRequest(url);

        // Parse the response to get the exchange rate
        double exchangeRate = parseResponse(jsonResponse);

        // Save the exchange rate to the database
        ExchangeRate rate = new ExchangeRate(currency, exchangeRate);
        exchangeRateRepository.save(rate);
    }

    private String sendRequest(String url) {
        // Send a request to the specified URL and return the response
        // Implementation details go here
    }

    private double parseResponse(String jsonResponse) {
        // Parse the JSON response from the API to get the exchange rate
        // Implementation details go here
    }

}
