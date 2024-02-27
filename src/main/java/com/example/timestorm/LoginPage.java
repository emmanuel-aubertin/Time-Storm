package com.example.timestorm;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class LoginPage {
    @FXML
    private TextField usernameField; // Bind to the TextField in FXML

    @FXML
    private PasswordField passwordField; // Bind to the PasswordField in FXML

    @FXML
    private VBox formContainer; // Assuming you have a VBox container for your form elements

    public LoginProvider user = new LoginProvider();

    @FXML
    private void handleLogin() throws IOException {
        System.out.println("Username : " + usernameField.getText());
        System.out.println("Password : " + passwordField.getText());
    }

    @FXML
    public void initialize() {
        // Ensure that the usernameField has been injected correctly
        if (usernameField != null) {
            // Add a listener to the scene property to ensure we have a scene
            usernameField.sceneProperty().addListener((observable, oldScene, newScene) -> adjustFormWidth(newScene));
        }
    }

    private void adjustFormWidth(Scene scene) {
        System.out.println("Widows has been resized !");
        if (scene != null && formContainer != null) {
            // Bind the VBox (formContainer) prefWidthProperty to half of the scene's width
            formContainer.prefWidthProperty().bind(scene.widthProperty().multiply(0.5));
        }
    }
}
