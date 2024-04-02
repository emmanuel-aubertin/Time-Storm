package com.example.timestorm;

import com.example.timestorm.edtutils.ClassroomCollection;
import com.example.timestorm.edtutils.PromotionCollection;
import com.example.timestorm.edtutils.TeacherCollection;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPage {
    @FXML
    public AnchorPane myAnchorPane;
    @FXML
    public Button btnDark;
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
                    PromotionCollection.getInstance();

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-page.fxml"));
                        Parent root = loader.load();
            
                        // Get the HomePageController instance
                        HomePageController homePageController = loader.getController();

                        Stage primaryStage = (Stage) usernameField.getScene().getWindow();
                        Scene homeScene = new Scene(root);
                        primaryStage.setScene(homeScene);
                        if (HelloApplication.darkMode()) {
                            homeScene.getStylesheets().add(getClass().getResource("dark.css").toExternalForm());
                        } else {
                            homeScene.getStylesheets().add(getClass().getResource("light.css").toExternalForm());
                        }
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




    @FXML
    public void onDarkButtonClick() {
        HelloApplication.isDarkMode = !HelloApplication.isDarkMode;
        setMode(HelloApplication.isDarkMode);
    }


    private void setMode(boolean darkMode) {
        if (darkMode) {
            btnDark.setText("Light");
            // Apply dark mode stylesheet
            Scene scene = btnDark.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("dark.css").toExternalForm());
        } else {
            btnDark.setText("Dark");
            // Apply light mode stylesheet
            Scene scene = btnDark.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("light.css").toExternalForm());
        }
    }

}
