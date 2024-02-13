package com.example.timestorm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class LoginProvider {
    private String tokenExecution;
    private boolean isTeacher;

    public LoginProvider() throws IOException {
        if(! setTokenExecution()){
            System.out.println("Unable to login!");
        }
        isTeacher = false;
    }

    /**
     * Find the excution token of the university connection portal
     * @param con
     * @return tokenExecution
     * @throws IOException
     */
    private String extractExecutionValue(HttpURLConnection con) throws IOException {
        String htmlResponse = readResponse(con);
        Document doc = Jsoup.parse(htmlResponse);
        Elements executionInput = doc.getElementsByAttributeValue("name", "execution");

        return executionInput.get(0) != null ? executionInput.get(0).attr("value") : null;
    }

    /**
     * Read Http response, and return it in a string
     * @param con
     * @return Http response in a string
     * @throws IOException
     */
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

    /**
     * Set a new execution token
     * @return true if a new one has been set
     * @throws IOException
     */
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

    public boolean tryLogin(String username, String password) throws IOException {
        if (!username.contains("uapv")) {
            System.out.println("Bad username format!");
            return false;
        }
        /*if (!mail.contains("alumni")) {
            isTeacher = true;
        }*/

        URL url = new URL("https://cas.univ-avignon.fr/cas/login?service=https%3A%2F%2Fedt.univ-avignon.fr%2Flogin");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Java client");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String urlParameters = "username=" + URLEncoder.encode(username, "UTF-8") +
                "&password=" + URLEncoder.encode(password, "UTF-8") +
                "&_eventId=submit" +
                "&execution=" + URLEncoder.encode(tokenExecution, "UTF-8");

        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = urlParameters.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String response = readResponse(con);
            System.out.println(response);

            // TODO: Read local Session Storage
            return true;
        } else {
            System.out.println("POST request not worked");
        }

        return false;
    }
}

