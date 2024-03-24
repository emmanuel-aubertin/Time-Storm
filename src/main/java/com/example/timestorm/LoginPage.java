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
import javafx.scene.layout.VBox;

public class LoginPage {
    @FXML
    private TextField usernameField; // Bind to the TextField in FXML

    @FXML
    private PasswordField passwordField; // Bind to the PasswordField in FXML

    @FXML
    private VBox formContainer; // Assuming you have a VBox container for your form elements

    public LoginProvider user = new LoginProvider();

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

                    try {
                        // Load the InputPage FXML
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("InputPage.fxml"));
                        Parent root = loader.load();

                        // Get the current scene and set the new root
                        Scene currentScene = usernameField.getScene();
                        if (currentScene != null) {
                            currentScene.setRoot(root);
                        } else {
                            System.out.println("Error: Scene is null");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Failed to load InputPage.fxml");
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
        // Ensure that the usernameField has been injected correctly
        if (usernameField != null) {
            // Add a listener to the scene property to ensure we have a scene
            usernameField.sceneProperty().addListener((observable, oldScene, newScene) -> adjustFormWidth(newScene));
        }
    }

    private void adjustFormWidth(Scene scene) {
        if (scene != null && formContainer != null) {
            // Bind the VBox (formContainer) prefWidthProperty to half of the scene's width
            formContainer.prefWidthProperty().bind(scene.widthProperty().multiply(0.5));
        }
    }
}
