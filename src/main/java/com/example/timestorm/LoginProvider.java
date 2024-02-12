package com.example.timestorm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginProvider {
    private String tokenExecution;

    public LoginProvider() throws IOException {
        if(! setTokenExecution()){
            System.out.println("Unable to login!");
        }
    }

    public String getTokenExecution() {
        return tokenExecution;
    }

    private String extractExecutionValue(HttpURLConnection con) throws IOException {
        String htmlResponse = readResponse(con);
        Document doc = Jsoup.parse(htmlResponse);
        Elements executionInput = doc.getElementsByAttributeValue("name", "execution");

        return executionInput.get(0) != null ? executionInput.get(0).attr("value") : null;
    }

    private String readResponse(HttpURLConnection con) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response.toString();
    }

    private boolean setTokenExecution() throws IOException {
        URL obj = new URL("https://cas.univ-avignon.fr/cas/login?service=https%3A%2F%2Fedt.univ-avignon.fr%2Flogin");

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Java client");

        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            tokenExecution = extractExecutionValue(con);
            con.disconnect();
            return true;
        }
        return false;
    }




}
