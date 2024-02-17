package com.example.timestorm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.DataOutputStream;

public class LoginProvider {
    private String username;
    private String password;
    private String token;
    private String name;
    private boolean isStudentDOSI;
    private boolean isTeacher;

    public LoginProvider() throws IOException {
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
     * Refresh EDT API token
     * @return true the token has been refresh successfully
     * @throws IOException
     */
    public boolean refreshToken() throws IOException {
        return this.tryLogin(username, password);
    }

    /**
     * Get EDT API token from username and password
     * @param username
     * @param password
     * @return true if user is connected
     * @throws IOException
     */
    public boolean tryLogin(String username, String password) throws IOException {
        if (!username.contains("uapv")) {
            System.out.println("Bad username format!");
            return false;
        }
        this.username = username;

        URL url = new URL("http://127.0.0.1:5000/login");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json"); // Set header

        // Enable input and output streams
        con.setDoOutput(true);

        String jsonInputString = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        try (DataOutputStream out = new DataOutputStream(con.getOutputStream())) {
            out.writeBytes(jsonInputString);
            out.flush();
        }

        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String response = readResponse(con);

            this.token = response.split("\"token\":")[1].split("\"")[1];
            this.name = response.split("\"name\":")[1].split("\"")[1];
            String isStudentStr = response.split("\"is_student\":")[1].split(",")[0].trim();
            this.isStudentDOSI = "true".equalsIgnoreCase(isStudentStr);

            System.out.println("Token: " + token + ", Name: " + name + ", isStudentDOSI:" + isStudentDOSI);
            this.password = password;
            return true;
        } else {
            System.out.println("POST request not worked");
            return false;
        }
    }
}

