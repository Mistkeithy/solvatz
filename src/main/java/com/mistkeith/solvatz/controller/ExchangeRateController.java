package com.mistkeith.solvatz.controller;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
        // return all data from db in json
        return exchangeRateRepository.findAll();
    }

    @GetMapping("/exchange-rate")
    public ExchangeRate addExchangeRate(@RequestParam String currency) {
        // Set api key and use the url. Get json string.
        String apiKey = "96PFC2ULHIGBJQA5";
        String url = "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=" + currency
                + "&to_currency=USD&apikey=" + apiKey + "&interval=1day";
        String jsonResponse = sendRequest(url);

        // get exchange rate from json file
        // double exchangeRate = Double.NaN; // force closed day
        double exchangeRate = parseResponse(jsonResponse);

        // if data is not available use previous close data
        if (Double.isNaN(exchangeRate)) {
            url = "https://www.alphavantage.co/query?function=FX_DAILY&from_symbol=" + currency
                    + "&to_symbol=USD&apikey=" + apiKey + "&interval=1day";
            jsonResponse = sendRequest(url);
            exchangeRate = parseDailyResponse(jsonResponse);
        }

        // get current date and format it
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(currentDate);

        // save this exchange rate to db
        ExchangeRate rateObject = new ExchangeRate(currency, exchangeRate, formattedDate);
        exchangeRateRepository.save(rateObject);

        return rateObject;
    }

    private String sendRequest(String urlString) {
        StringBuilder response = new StringBuilder();
        try {
            // connect to http and request data by url
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // read and append response variable
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null)
                response.append(line);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    private double parseResponse(String jsonResponse) {
        try {
            // get value from current date if available
            JSONObject json = new JSONObject(jsonResponse);
            JSONObject rateObject = json.getJSONObject("Realtime Currency Exchange Rate");
            return rateObject.getDouble("5. Exchange Rate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Double.NaN;
    }

    private double parseDailyResponse(String jsonResponse) throws JSONException {
        // get value from recent date if available
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject dailyData = jsonObject.getJSONObject("Time Series FX (Daily)");
        String[] dates = dailyData.keySet().toArray(new String[0]);
        // find recent date by json response
        String recentDate = "";
        for (String date : dates)
            if (date.compareTo(recentDate) > 0)
                recentDate = date;
        // get value from recent date
        JSONObject dateData = dailyData.getJSONObject(recentDate);
        double exchangeRate = Double.valueOf(dateData.getString("4. close"));
        return exchangeRate;
    }

}
