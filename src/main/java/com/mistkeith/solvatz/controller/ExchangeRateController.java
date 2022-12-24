package com.mistkeith.solvatz.controller;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mistkeith.solvatz.model.ExchangeRate;
import com.mistkeith.solvatz.repository.IExchangeRateRepository;

@RestController
public class ExchangeRateController {

    @Autowired
    private IExchangeRateRepository exchangeRateRepository;

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
                + "&to_currency=USD&apikey=" + apiKey;
        String jsonResponse = sendRequest(url);

        // Parse the response to get the exchange rate
        double exchangeRate = parseResponse(jsonResponse);

        // Save the exchange rate to the database
        ExchangeRate rate = new ExchangeRate(currency, exchangeRate);
        exchangeRateRepository.save(rate);
    }

    private String sendRequest(String url) {
        String jsonResponse = "";
        try {
            // Send GET request to the specified URL
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            // If the request was successful read the response and return
            if (connection.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                jsonResponse = response.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }

    private double parseResponse(String jsonResponse) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject ratesObject = jsonObject.getJSONObject("rates");
        double exchangeRate = ratesObject.getDouble("KZT");
        return exchangeRate;
    }

}
