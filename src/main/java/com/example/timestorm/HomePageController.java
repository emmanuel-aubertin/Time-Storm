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

    @FXML
    private ToggleGroup viewToggleGroup;
    @FXML
    private ToggleGroup edtToggleGroup;
    @FXML
    public ToggleButton dayBtn;
    @FXML
    public ToggleButton weekBtn;
    @FXML
    public ToggleButton monthBtn;
    @FXML
    private ToggleButton btnFormation;
    @FXML
    private ToggleButton btnSalle;
    @FXML
    private ToggleButton btnPersonnel;
    @FXML
    private ToggleButton btnHome;
    @FXML
    private Button btnDark;

    @FXML
    public void initialize() {
        viewToggleGroup = new ToggleGroup();
        edtToggleGroup = new ToggleGroup();

        dayBtn.setToggleGroup(viewToggleGroup);
        weekBtn.setToggleGroup(viewToggleGroup);
        monthBtn.setToggleGroup(viewToggleGroup);

        btnPersonnel.setToggleGroup(edtToggleGroup);
        btnHome.setToggleGroup(edtToggleGroup);
        btnSalle.setToggleGroup(edtToggleGroup);
        btnFormation.setToggleGroup(edtToggleGroup);


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
        HelloApplication.darkMode = !HelloApplication.darkMode;
        setMode(HelloApplication.darkMode);
    }


    private void setMode(boolean darkMode) {
        if (darkMode) {
            btnDark.setText("Light");
            // Apply dark mode stylesheet
            Scene scene = btnFormation.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("dark.css").toExternalForm());
        } else {
            btnDark.setText("Dark");
            // Apply light mode stylesheet
            Scene scene = btnFormation.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("light.css").toExternalForm());
        }
    }

}
