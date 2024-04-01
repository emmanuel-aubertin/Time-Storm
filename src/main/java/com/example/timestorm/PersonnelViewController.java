package com.example.timestorm;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PersonnelViewController {
    

    @FXML
    private Button btnFormation;
    @FXML
    private Button btnSalle;
    @FXML
    private Button btnPersonnel;
    @FXML
    private Button btnHome;
    @FXML
    private Button btnAjout;

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
    public void onAjoutButtonClick() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("newevent-view.fxml"));
    Parent root = loader.load();

    Scene scene = new Scene(root);
    Stage stage = (Stage) btnHome.getScene().getWindow();
    stage.setScene(scene);
    stage.show();
    }
}
