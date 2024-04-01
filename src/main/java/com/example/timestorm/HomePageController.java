package com.example.timestorm;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePageController {
    
    private static boolean darkMode = false;
    @FXML
    private ToggleGroup viewToggleGroup;
    @FXML
    public ToggleButton dayBtn;
    @FXML
    public ToggleButton weekBtn;
    @FXML
    public ToggleButton monthBtn;
    @FXML
    private Button btnFormation;
    @FXML
    private Button btnSalle;
    @FXML
    private Button btnPersonnel;
    @FXML
    private Button btnHome;
    @FXML
    private Button btnDark;

    @FXML
    public void initialize() {
        viewToggleGroup = new ToggleGroup();

        dayBtn.setToggleGroup(viewToggleGroup);
        weekBtn.setToggleGroup(viewToggleGroup);
        monthBtn.setToggleGroup(viewToggleGroup);

        dayBtn.setSelected(true);
    }

    @FXML
    public void onFormationButtonClick() throws IOException {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("formation-view.fxml"));
       Parent root = loader.load();
   
       Scene scene = new Scene(root);
       Stage stage = (Stage) btnFormation.getScene().getWindow();
       stage.setScene(scene);
       stage.show();
   }

   @FXML
   public void onSalleButtonClick() throws IOException {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("classroom-view.fxml"));
       Parent root = loader.load();
   
       Scene scene = new Scene(root);
       Stage stage = (Stage) btnSalle.getScene().getWindow(); // Replaced btnFormation with btnSalle
       stage.setScene(scene);
       stage.show();
   }
   

    @FXML
    public void onPersonnelButtonClick() throws IOException {
     FXMLLoader loader = new FXMLLoader(getClass().getResource("personnel-view.fxml"));
     Parent root = loader.load();
 
     Scene scene = new Scene(root);
     Stage stage = (Stage) btnPersonnel.getScene().getWindow();
     stage.setScene(scene);
     stage.show();
 }

    @FXML
    public void onHomeButtonClick() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("home-page.fxml"));
    Parent root = loader.load();

    Scene scene = new Scene(root);
    Stage stage = (Stage) btnHome.getScene().getWindow();
    stage.setScene(scene);
    stage.show();
    }
    @FXML
    public void onDarkButtonClick() {
        darkMode = !darkMode;
        setMode(darkMode);
    }


    private void setMode(boolean darkMode) {
        if (darkMode) {
            btnDark.setText("Light");
            // Apply dark mode stylesheet
            Scene scene = btnFormation.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/css/dark.css").toExternalForm());
        } else {
            btnDark.setText("Dark");
            // Apply light mode stylesheet
            Scene scene = btnFormation.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/css/light.css").toExternalForm());
        }
    }

}
