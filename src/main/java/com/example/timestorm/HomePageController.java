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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class HomePageController {

    @FXML
    public ToggleButton btnEnseignant;
    @FXML
    public VBox parentContainer;
    @FXML
    public StackPane calendarContainer;
    @FXML
    public DatePicker datePicker;
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

    private ArrayList<Event> events = new ArrayList<>();

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
        System.out.println(currentText);
        AtomicReference<String> selectedButtonText = new AtomicReference<>("");
        ToggleButton selectedToggleButton = (ToggleButton) edtToggleGroup.getSelectedToggle();
        if (selectedToggleButton != null) {
            selectedButtonText.set(selectedToggleButton.getId());
        }
        Collection<String> suggestions = new ArrayList<>();
        System.out.println(selectedButtonText.get());
        if (Objects.equals(selectedButtonText.get(), "btnEnseignant")) {

            TeacherCollection instance = TeacherCollection.getInstance();
            ArrayList<Teacher> teacherSuggestions = instance.getTeacherLike(currentText);
            for (Teacher t : teacherSuggestions) {
                suggestions.add(t.getName());
            }
        } else if (Objects.equals(selectedButtonText.get(), "btnSalle")) {
            System.out.println(selectedButtonText.get());
            ClassroomCollection instance = ClassroomCollection.getInstance();
            ArrayList<Classroom> teacherSuggestions = instance.getClassroomLike(currentText);
            for (Classroom t : teacherSuggestions) {
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
            System.out.println("SELECTED : " + selectedItem);
            System.out.println(selectedButtonText.get());
            if (Objects.equals(selectedButtonText.get(), "btnEnseignant")) {
                TeacherCollection instance = TeacherCollection.getInstance();
                ArrayList<Teacher> teacherSuggestions = instance.getTeacherLike(currentText);

                if(Objects.equals(teacherSuggestions.get(0).getName(), selectedItem)){
                    events = teacherSuggestions.get(0).getTeacherEvents(HelloApplication.user);
                }
            } else if (Objects.equals(selectedButtonText.get(), "btnSalle")) {
                ClassroomCollection instance = ClassroomCollection.getInstance();
                ArrayList<Classroom> teacherSuggestions = instance.getClassroomLike(currentText);
                System.out.println(teacherSuggestions.get(0).getCode());
                events = teacherSuggestions.get(0).getClassroomEdt(HelloApplication.user);
                ArrayList<Event> filteredEvents = new ArrayList<>();
                LocalDate selectedDate = datePicker.getValue();
                System.out.println("Date selected");
                filteredEvents = filterEventsByDay(events, selectedDate);
                for (Event e: filteredEvents
                     ) {
                    System.out.println(e.toString());
                }
                System.out.println("filteredEvents");
                GridPane dayCalendar = createDayCalendar(selectedDate, filteredEvents);
                System.out.println(dayCalendar.toString());
                calendarContainer.getChildren().clear();
                calendarContainer.getChildren().add(dayCalendar);
                System.out.println("GridPane added");
                }
        });
    }



    private ArrayList<Event> filterEventsByDay(ArrayList<Event> events, LocalDate date) {
        ArrayList<Event> filteredEvents = new ArrayList<>();
        ZonedDateTime startOfDay = date.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        for (Event event : events) {
            ZonedDateTime eventStart = ZonedDateTime.parse(event.getStart());
            if (eventStart.isAfter(startOfDay) && eventStart.isBefore(endOfDay)) {
                filteredEvents.add(event);
            }
        }

        return filteredEvents;
    }

    private ArrayList<Event> filterEventsByWeek(ArrayList<Event> events, LocalDate date) {
        // Calculate the start and end of the week (Monday to Friday)
        LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));

        ArrayList<Event> filteredEvents = new ArrayList<>();

        for (Event event : events) {
            ZonedDateTime eventStart = ZonedDateTime.parse(event.getStart());
            LocalDate eventDate = eventStart.toLocalDate();

            if (eventDate.isAfter(startOfWeek.minusDays(1)) && eventDate.isBefore(endOfWeek.plusDays(1))) {
                filteredEvents.add(event);
            }
        }

        return filteredEvents;
    }

    private ArrayList<Event> filterEventsByMonth(ArrayList<Event> events, LocalDate date) {
        // Calculate the start and end of the month
        LocalDate startOfMonth = date.withDayOfMonth(1);
        LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());

        ArrayList<Event> filteredEvents = new ArrayList<>();

        for (Event event : events) {
            ZonedDateTime eventStart = ZonedDateTime.parse(event.getStart());
            LocalDate eventDate = eventStart.toLocalDate();

            if (eventDate.isAfter(startOfMonth.minusDays(1)) && eventDate.isBefore(endOfMonth.plusDays(1))) {
                filteredEvents.add(event);
            }
        }

        return filteredEvents;
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

    private GridPane createDayCalendar(LocalDate date, List<Event> events) {
        List<LocalTime> timeSlots = generateTimeSlots();
        GridPane calendar = new GridPane();
        calendar.setHgap(5);
        calendar.setVgap(5);

        // Set the maximum width of the calendar to the parent container's width
        calendar.setMaxWidth(parentContainer.getWidth());
        double parentWidth = parentContainer.getWidth();
        double parentHeight = parentContainer.getHeight();
        // Set a fixed width for the first column (hours column)
        ColumnConstraints columnConstraints = new ColumnConstraints(60); // Fixed width for the first column
        calendar.getColumnConstraints().add(columnConstraints);

        for (int rowIndex = 0; rowIndex < timeSlots.size(); rowIndex++) {
            LocalTime timeSlot = timeSlots.get(rowIndex);
            Label timeLabel = new Label(timeSlot.format(DateTimeFormatter.ofPattern("HH:mm")));
            calendar.add(timeLabel, 0, rowIndex);

            // Track used columns in this row to avoid overlapping
            HashSet<Integer> usedColumns = new HashSet<>();

            for (Event event : events) {
                ZonedDateTime eventStart = ZonedDateTime.parse(event.getStart()).plusHours(2);
                ZonedDateTime eventEnd = ZonedDateTime.parse(event.getEnd()).plusHours(2);

                if (!eventStart.toLocalDate().isEqual(date)) continue;

                // Simplified event in timeslot check
                boolean isInTimeSlot = !eventStart.toLocalTime().isBefore(timeSlot) && eventEnd.toLocalTime().isAfter(timeSlot);
                if (!isInTimeSlot) continue;

                int eventStartIndex = Math.max(timeSlots.indexOf(eventStart.toLocalTime().truncatedTo(ChronoUnit.MINUTES)), 0);
                int eventEndIndex = Math.min(timeSlots.indexOf(eventEnd.toLocalTime().truncatedTo(ChronoUnit.MINUTES)), timeSlots.size() - 1);

                if (eventStartIndex == -1 || eventEndIndex == -1) continue; // Event times not in slots

                int rowSpan = eventEndIndex - eventStartIndex + 1;
                Rectangle eventRectangle = new Rectangle(parentWidth - 65, 25 * rowSpan, Color.BLUE);
                Label eventLabel = new Label(String.format("Matière : %s\nEnseignant : %s\nTD : %s\nSalle : %s\nType : %s",
                        event.getCode(), event.getTeacher().getName(),
                        event.getType(), event.getClassroom().getName(),
                        event.getType()));
                eventLabel.setTextFill(Color.WHITE);
                eventLabel.setFont(new Font(12));
                eventLabel.setTextAlignment(TextAlignment.CENTER);
                eventLabel.setWrapText(true);

                StackPane eventStackPane = new StackPane(eventRectangle, eventLabel);
                Tooltip tooltip = new Tooltip(eventLabel.getText());
                Tooltip.install(eventStackPane, tooltip);



                calendar.add(eventStackPane, 1, eventStartIndex, 1, rowSpan);
            }
        }

        calendar.setMaxHeight(parentHeight - 50);
        return calendar;
    }



}
