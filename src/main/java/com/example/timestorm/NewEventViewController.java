package com.example.timestorm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import javafx.scene.control.DatePicker;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import com.example.timestorm.edtutils.Classroom;
import com.example.timestorm.edtutils.ClassroomCollection;
import com.example.timestorm.edtutils.Promotion;
import com.example.timestorm.edtutils.Teacher;
import com.example.timestorm.edtutils.TeacherCollection;


public class NewEventViewController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField startTimeField;

    @FXML
    private TextField endTimeField;

    @FXML
    private TextField memoField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField teacherCodeField;

    @FXML
    private TextField classroomCodeField;

    @FXML
    private TextField promoCodeField;

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<String> startTimeComboBox;

    @FXML
    private ComboBox<String> endTimeComboBox;

    @FXML
    private AutoCompletionBinding<String> autoCompletionBinding;
    private Text seeChoice;


    public void initialize() {
        // Populate the start and end time combo boxes with options from 8:00 to 20:00 with 30 minute intervals
        for (int hour = 8; hour < 20; hour++) {
            for (int minute : new int[]{0, 30}) {
                String time = String.format("%02d:%02d", hour, minute);
                startTimeComboBox.getItems().add(time);
                endTimeComboBox.getItems().add(time);
            }
        }

        // Select the first option by default
        startTimeComboBox.getSelectionModel().selectFirst();
        endTimeComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    void onCreateEventButtonClick(ActionEvent event) throws IOException {
        String title = titleField.getText();
        String startTime = startTimeField.getText();
        String endTime = endTimeField.getText();
        String memo = memoField.getText();
        String type = typeField.getText();
        String teacherCode = teacherCodeField.getText();
        String classroomCode = classroomCodeField.getText();
        String promoCode = promoCodeField.getText();

        // Create the request body
        String requestBody = "{\"title\": \"" + title + "\", \"start\": \"" + startTime + "\", \"end\": \"" + endTime + "\", \"memo\": \"" + memo + "\", \"type\": \"" + type + "\", \"teacher_code\": \"" + teacherCode + "\", \"classroom_code\": \"" + classroomCode + "\", \"promo_code\": \"" + promoCode + "\"}";

        // Set up the connection
        URL url = new URL("http://127.0.0.1:5000/event/create");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + HelloApplication.user.getToken());
        con.setDoOutput(true);

        // Send the request
        try (DataOutputStream out = new DataOutputStream(con.getOutputStream())) {
            out.writeBytes(requestBody);
            out.flush();
        }

        // Check the response code
        int responseCode = con.getResponseCode();
        if (responseCode == 200) {
            // Read the response body
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder responseBody = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                responseBody.append(inputLine);
            }
            in.close();

            // Show a success alert with the response body
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Event created successfully!");
            alert.setContentText("Response body: " + responseBody.toString());
            alert.showAndWait();

            // Clear the input fields
            titleField.clear();
            startTimeField.clear();
            endTimeField.clear();
            memoField.clear();
            typeField.clear();
            teacherCodeField.clear();
            classroomCodeField.clear();
            promoCodeField.clear();
        } else {
            // Show an error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to create event.");
            alert.setContentText("Response code: " + responseCode);
            alert.showAndWait();
        }

    }

    @FXML
    private void handleTeacherCodeInput(KeyEvent event) {
        String currentText = teacherCodeField.getText();
        System.out.println("Input: " + currentText);
        TeacherCollection instance = TeacherCollection.getInstance();
        ArrayList<Teacher> teacherSuggestions = instance.getTeacherLike(currentText);
        Collection<String> suggestions = new ArrayList<>();
        for (Teacher t : teacherSuggestions) {
            suggestions.add(t.getName());
            System.out.println(t.getName());
        }

        // Dispose of the old autocompletion binding if it exists
        if (autoCompletionBinding != null) {
            autoCompletionBinding.dispose();
        }

        Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>> suggestionProvider =
                request -> suggestions.stream()
                        .filter(suggestion -> suggestion.toLowerCase().contains(request.getUserText().toLowerCase()))
                        .toList();

        autoCompletionBinding = TextFields.bindAutoCompletion(teacherCodeField, suggestionProvider);

        autoCompletionBinding.setOnAutoCompleted(autoCompleteEvent -> {
            String selectedItem = autoCompleteEvent.getCompletion();
            seeChoice.setText(selectedItem);
        });
    }

@FXML
private void handleClassroomCodeInput(KeyEvent event) {
    String currentText = classroomCodeField.getText();
    System.out.println("Input: " + currentText);
    ClassroomCollection instance = ClassroomCollection.getInstance();
    ArrayList<Classroom> classroomSuggestions = instance.getClassroomLike(currentText);
    Collection<String> suggestions = new ArrayList<>();
    for (Classroom c : classroomSuggestions) {
        suggestions.add(c.getName());
        System.out.println(c.getName());
    }

    // Dispose of the old autocompletion binding if it exists
    if (autoCompletionBinding != null) {
        autoCompletionBinding.dispose();
    }

    Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>> suggestionProvider =
            request -> suggestions.stream()
                    .filter(suggestion -> suggestion.toLowerCase().contains(request.getUserText().toLowerCase()))
                    .toList();

    autoCompletionBinding = TextFields.bindAutoCompletion(classroomCodeField, suggestionProvider);

    autoCompletionBinding.setOnAutoCompleted(autoCompleteEvent -> {
        String selectedItem = autoCompleteEvent.getCompletion();
        seeChoice.setText(selectedItem);
    });
}

@FXML
private void handlePromoCodeInput(KeyEvent event) {
 System.out.println("blaba");
}

@FXML
    public void onBackButtonClick() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("newevent-view.fxml"));
    Parent root = loader.load();

    Scene scene = new Scene(root);
    Stage stage = (Stage) backButton.getScene().getWindow();
    stage.setScene(scene);
    stage.show();
    }

}
