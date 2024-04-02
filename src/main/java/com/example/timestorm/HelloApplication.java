package com.example.timestorm;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HelloApplication extends Application {

    public static LoginProvider user = new LoginProvider();

    public static boolean isDarkMode = true;
    public static boolean darkMode() {
        try {
            URL url = new URL("http://127.0.0.1:5000/isDark"); // Server URL
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // Assuming you have a method getAuthToken() to obtain the auth token
            String authToken = "Bearer " + user.getToken(); // Replace getAuthToken() with your actual method
            con.setRequestProperty("Authorization", authToken);

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Read JSON response and return the "darkMode" value
                JSONObject myResponse = new JSONObject(response.toString());
                return myResponse.getBoolean("darkMode");
            } else {
                System.out.println("GET request not worked, Response Code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("Error during HTTP request: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static boolean toogleDarkModeValue() {
        try {
            URL url = new URL("http://127.0.0.1:5000/isDark/Toggle"); // Endpoint URL
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // Assuming you have a method getAuthToken() that retrieves the authorization token
            String authToken = "Bearer " + user.getToken(); // Replace getAuthToken() with your actual token retrieval method
            con.setRequestProperty("Authorization", authToken);

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // Success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the JSON response and extract the "darkMode" value
                JSONObject myResponse = new JSONObject(response.toString());
                return myResponse.getBoolean("darkMode");
            } else {
                System.out.println("GET request not successful. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("Error during HTTP request: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // Return false in case of any error
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        if(isDarkMode) {
            scene.getStylesheets().add(getClass().getResource("dark.css").toExternalForm());
        } else {
            scene.getStylesheets().add(getClass().getResource("light.css").toExternalForm());
        }
        stage.setTitle("Time Storm");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        launch();
    }

}