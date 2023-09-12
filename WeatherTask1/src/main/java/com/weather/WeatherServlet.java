package com.weather;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class WeatherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String apiKey = request.getParameter("apiKey");
        String coordinates = request.getParameter("coordinates");

        String urlString = "http://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + coordinates + "&aqi=no";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String output = br.readLine();
        JSONObject jsonObject = new JSONObject(output);
        double temp = jsonObject.getJSONObject("current").getDouble("temp_c");

        String weatherCondition = getWeatherCondition(temp);
        response.getWriter().append("Temperature is: ").append(weatherCondition);
    }

    private String getWeatherCondition(double temp) {
        if (temp < 0) {
            return "cold";
        } else if (temp > 30) {
            return "hot";
        } else {
            return "fine";
        }
    }
}