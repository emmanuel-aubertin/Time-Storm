package com.example.timestorm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class FormationViewController {
    @FXML
    private TextField searchBar;
    private ToggleGroup viewToggleGroup;

    // Add event handler methods and other logic

    @FXML
    private void onViewToggleButtonAction(ActionEvent event) {
    ToggleButton selectedButton = (ToggleButton) viewToggleGroup.getSelectedToggle();
    String selectedView = selectedButton.getText();

    // Implement the logic to update the UI based on the selected view
    switch (selectedView) {
        case "Jour":
            // Display the single day calendar view
            break;
        case "Semaine":
            // Display the weekly calendar view
            break;
        case "Mois":
            // Display the monthly calendar view
            break;
    }
}

}
