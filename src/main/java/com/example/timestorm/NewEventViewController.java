package com.example.timestorm;

import com.example.timestorm.edtutils.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;


public class NewEventViewController {
    @FXML
    public Button btnDark;
    @FXML
    public DatePicker datePicker;
    @FXML
    private TextField titleField;


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
        LocalDate date = datePicker.getValue();
        String startTime = startTimeComboBox.getValue(); // format : "08:30"
        String endTime = endTimeComboBox.getValue(); // format : "08:30"

        ZoneId zoneId = ZoneId.of("UTC");

        LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.parse(startTime));
        ZonedDateTime startZonedDateTime = ZonedDateTime.of(startDateTime, zoneId).minusHours(2);

        LocalDateTime endDateTime = LocalDateTime.of(date, LocalTime.parse(endTime));
        ZonedDateTime endZonedDateTime = ZonedDateTime.of(endDateTime, zoneId).minusHours(2);

        // Format ZonedDateTime to the desired string format
        String formattedStartTime = startZonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String formattedEndTime = endZonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        String memo = memoField.getText();
        String type = typeField.getText();
        String teacherCode = teacherCodeField.getText();
        String classroomCode = classroomCodeField.getText();
        String promoCode = promoCodeField.getText();

        // Modify requestBody to use formattedStartTime and formattedEndTime
        String requestBody = "{\"title\": \"" + title + "\", \"start\": \"" + formattedStartTime + "\", \"end\": \"" + formattedEndTime + "\", \"memo\": \"" + memo + "\", \"type\": \"" + type + "\", \"teacher_code\": \"" + teacherCode + "\", \"classroom_code\": \"" + classroomCode + "\", \"promo_code\": \"" + promoCode + "\"}";
        System.out.println(requestBody);
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
        if (responseCode == 200 || responseCode == 201) {
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
            startTimeComboBox.getSelectionModel().clearSelection(); // Clear selection from combo box
            endTimeComboBox.getSelectionModel().clearSelection(); // Clear selection from combo box
            memoField.clear();
            typeField.clear();
            teacherCodeField.clear();
            classroomCodeField.clear();
            promoCodeField.clear();
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder responseBody = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                responseBody.append(inputLine);
            }
            in.close();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to create event.");
            alert.setContentText("Response code: " + responseCode +"\n"+ responseBody.toString());
            alert.showAndWait();
        }
    }
    

    @FXML
    private void handleTeacherCodeInput(KeyEvent event) {
        String currentText = teacherCodeField.getText();
        TeacherCollection instance = TeacherCollection.getInstance();
        ArrayList<Teacher> teacherSuggestions = instance.getTeacherLike(currentText);
        Collection<String> suggestions = new ArrayList<>();
        for (Teacher t : teacherSuggestions) {
            suggestions.add(t.getName());
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
            autoCompletionBinding.dispose();
        });
    }

@FXML
private void handlePromoCodeInput(KeyEvent event) {
    String currentText = promoCodeField.getText();
    System.out.println("Input: " + currentText);
    PromotionCollection instance = PromotionCollection.getInstance();
    ArrayList<Promotion> classroomSuggestions = instance.getPromotionLike(currentText);
    Collection<String> suggestions = new ArrayList<>();
    for (Promotion c : classroomSuggestions) {
        suggestions.add(c.getName());
        System.out.println(c.getName());
    }

    if (autoCompletionBinding != null) {
        autoCompletionBinding.dispose();
    }


    Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>> suggestionProvider =
            request -> suggestions.stream()
                    .filter(suggestion -> suggestion.toLowerCase().contains(request.getUserText().toLowerCase()))
                    .toList();

    autoCompletionBinding = TextFields.bindAutoCompletion(promoCodeField, suggestionProvider);

    autoCompletionBinding.setOnAutoCompleted(autoCompleteEvent -> {
        String selectedItem = autoCompleteEvent.getCompletion();
        autoCompletionBinding.dispose();
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
        autoCompletionBinding.dispose();
    });
}

@FXML
    public void onBackButtonClick() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("home-page.fxml"));
    Parent root = loader.load();

    Scene scene = new Scene(root);
    Stage stage = (Stage) backButton.getScene().getWindow();
    if (HelloApplication.darkMode()) {
        scene.getStylesheets().add(getClass().getResource("dark.css").toExternalForm());
    } else {
        scene.getStylesheets().add(getClass().getResource("light.css").toExternalForm());
    }
    stage.setScene(scene);
    stage.show();
    }
    @FXML
    public void onDarkButtonClick() {
        setMode(HelloApplication.toogleDarkModeValue());
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
