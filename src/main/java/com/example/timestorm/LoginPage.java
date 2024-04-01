package com.example.timestorm;

import com.example.timestorm.edtutils.ClassroomCollection;
import com.example.timestorm.edtutils.TeacherCollection;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPage {
    @FXML
    private TextField usernameField; // Bind to the TextField in FXML

    @FXML
    private PasswordField passwordField; // Bind to the PasswordField in FXML

    @FXML
    private VBox formContainer; // Assuming you have a VBox container for your form elements


    @FXML
    private void handleLogin() {
        // Remove existing error
        formContainer.getChildren().removeIf(node -> node instanceof Label && node.getStyleClass().contains("error-text"));

        Label loaderLabel = new Label("Logging in...");
        formContainer.getChildren().add(loaderLabel);

        Task<Boolean> loginTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return HelloApplication.user.tryLogin(usernameField.getText(), passwordField.getText());
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                boolean isLogged = getValue();
                if (isLogged) {
                    System.out.println("User connected");
                    // initialisation
                    TeacherCollection.getInstance();
                    ClassroomCollection.getInstance();

            
                    // Load the home page FXML file
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-page.fxml"));
                        Parent root = loader.load();
            
                        // Get the HomePageController instance
                        HomePageController homePageController = loader.getController();
            
                        // Pass any necessary data to the HomePageController, if needed
            
                        // Set the new scene with the home page root
                        Stage primaryStage = (Stage) usernameField.getScene().getWindow();
                        Scene homeScene = new Scene(root);
                        primaryStage.setScene(homeScene);
                        primaryStage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("User not connected");
                    Label errorLabel = new Label("Wrong username or password !");
                    errorLabel.getStyleClass().add("error-text");
                    formContainer.getChildren().add(errorLabel);
                }
                formContainer.getChildren().remove(loaderLabel); // Remove loader from the UI
            }

            @Override
            protected void failed() {
                super.failed();
                System.out.println("Login failed");
                formContainer.getChildren().remove(loaderLabel);
            }
        };

        Thread loginThread = new Thread(loginTask);
        loginThread.start();
    }


    @FXML
    public void initialize() {

        if (usernameField != null) {
            usernameField.sceneProperty().addListener((observable, oldScene, newScene) -> adjustFormWidth(newScene));

            usernameField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    handleLogin();
                }
            });
        }

        if (passwordField != null) {
            passwordField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    handleLogin();
                }
            });
        }

    }

    private void adjustFormWidth(Scene scene) {
        if (scene != null && formContainer != null) {
            // Bind the VBox (formContainer) prefWidthProperty to half of the scene's width
            formContainer.prefWidthProperty().bind(scene.widthProperty().multiply(0.5));
        }
    }


}