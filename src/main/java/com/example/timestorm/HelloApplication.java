package com.example.timestorm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HelloApplication extends Application {

    private static String tokenExecution;

    private static String extractExecutionValue(HttpURLConnection con) throws IOException {
        String htmlResponse = readResponse(con);
    Document doc = Jsoup.parse(htmlResponse);
Elements executionInput = doc.getElementsByAttributeValue("name", "execution");

        return executionInput.get(0) != null ? executionInput.get(0).attr("value") : null;
    }

    private static String readResponse(HttpURLConnection con) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response.toString();
    }

    private static String getToken() throws IOException {
        URL obj = new URL("https://cas.univ-avignon.fr/cas/login?service=https%3A%2F%2Fedt.univ-avignon.fr%2Flogin");

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Java client");

        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String response = extractExecutionValue(con);
            con.disconnect();
            return response;
        }
        return "Error: HTTP " + responseCode;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Time Storm");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        tokenExecution = getToken();
        System.out.println("Token:" + tokenExecution);
        launch();
    }


}