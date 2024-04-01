package com.example.timestorm;

import com.example.timestorm.edtutils.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class HomePageController {

    @FXML
    public ToggleButton btnEnseignant;
    @FXML
    public VBox parentContainer;
    @FXML
    public StackPane calendarContainer;
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
    private TextField inputField;

    private List<Event> events = new ArrayList<>();

    private AutoCompletionBinding<String> autoCompletionBinding;
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
        btnEnseignant.setToggleGroup(edtToggleGroup);


        dayBtn.setSelected(true);
    }


    @FXML
    public void onFormationButtonClick() throws IOException {
        inputField.setVisible(true);
   }

   @FXML
   public void onSalleButtonClick() throws IOException {
       inputField.setVisible(true);
   }
   

    @FXML
    public void onPersonnelButtonClick() throws IOException {
        inputField.setVisible(false);
 }

    @FXML
    public void onHomeButtonClick() throws IOException {
        inputField.setVisible(false);
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

    @FXML
    private void handleInput(KeyEvent event) {
        String currentText = inputField.getText();
        AtomicReference<String> selectedButtonText = new AtomicReference<>("");
        ToggleButton selectedToggleButton = (ToggleButton) edtToggleGroup.getSelectedToggle();
        if (selectedToggleButton != null) {
            selectedButtonText.set(selectedToggleButton.getId());
        }
        Collection<String> suggestions = new ArrayList<>();

        if (Objects.equals(selectedButtonText, "btnEnseignant")) {
            System.out.println(selectedButtonText.get());
            TeacherCollection instance = TeacherCollection.getInstance();
            ArrayList<Teacher> teacherSuggestions = instance.getTeacherLike(currentText);
            for (Teacher t : teacherSuggestions) {
                System.out.println(t.getName());
                suggestions.add(t.getName());
            }
        } else if (Objects.equals(selectedButtonText, "btnSalle")) {
            System.out.println(selectedButtonText.get());
            ClassroomCollection instance = ClassroomCollection.getInstance();
            ArrayList<Classroom> teacherSuggestions = instance.getClassroomLike(currentText);
            for (Classroom t : teacherSuggestions) {
                System.out.println(t.getName());
                suggestions.add(t.getName());
            }
        }




        // Dispose of the old autocompletion binding if it exists
        if (autoCompletionBinding != null) {
            autoCompletionBinding.dispose();
        }


        Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>> suggestionProvider =
                request -> suggestions.stream()
                        .filter(suggestion -> suggestion.toLowerCase().contains(request.getUserText().toLowerCase()))
                        .toList();

        autoCompletionBinding = TextFields.bindAutoCompletion(inputField, suggestionProvider);

        autoCompletionBinding.setOnAutoCompleted(autoCompleteEvent -> {
            String selectedItem = autoCompleteEvent.getCompletion();
            System.out.println(selectedButtonText.get());
            if (Objects.equals(selectedButtonText, "btnEnseignant")) {
                TeacherCollection instance = TeacherCollection.getInstance();
                ArrayList<Teacher> teacherSuggestions = instance.getTeacherLike(currentText);

                if(Objects.equals(teacherSuggestions.get(0).getName(), selectedItem)){
                    events = teacherSuggestions.get(0).getTeacherEvents(HelloApplication.user);
                }
            } else if (Objects.equals(selectedButtonText, "btnSalle")) {
                ClassroomCollection instance = ClassroomCollection.getInstance();
                ArrayList<Classroom> teacherSuggestions = instance.getClassroomLike(currentText);

                if(Objects.equals(teacherSuggestions.get(0).getName(), selectedItem)){
                    events = teacherSuggestions.get(0).getClassroomEdt(HelloApplication.user);
                }
            }
        });
    }



    private List<LocalTime> generateTimeSlots() {
        List<LocalTime> timeSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(20, 0);

        while (startTime.isBefore(endTime)) {
            timeSlots.add(startTime);
            startTime = startTime.plusMinutes(30);
        }

        return timeSlots;
    }


}
