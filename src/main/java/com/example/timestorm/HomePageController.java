package com.example.timestorm;

import com.example.timestorm.edtutils.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class HomePageController {
    private boolean isOk = true;
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
    private ToggleButton btnFormation;
    @FXML
    private ToggleButton btnSalle;
    @FXML
    private ToggleButton btnPersonnel;

    @FXML
    private Button btnDark;

    @FXML
    private TextField inputField;

    private ArrayList<Event> events = new ArrayList<>();
    private int dayNumber = 7;
    private AutoCompletionBinding<String> autoCompletionBinding;

    @FXML
    public void initialize() {
        viewToggleGroup = new ToggleGroup();
        edtToggleGroup = new ToggleGroup();

        dayBtn.setToggleGroup(viewToggleGroup);
        weekBtn.setToggleGroup(viewToggleGroup);

        btnPersonnel.setToggleGroup(edtToggleGroup);
        btnSalle.setToggleGroup(edtToggleGroup);
        btnFormation.setToggleGroup(edtToggleGroup);
        btnEnseignant.setToggleGroup(edtToggleGroup);

        weekBtn.setSelected(true);
        datePicker.setValue(LocalDate.now());
        PersonalEvents personalEvents = new PersonalEvents(HelloApplication.user);
        events = personalEvents.getEvents();
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Call the onChangeDate method whenever the date changes
            onChangeDate(newValue);
        });

        Platform.runLater(() -> {
            updateCalendar("week");
        });
    }

    private void updateCalendar(String type) {
        ArrayList<Event> filteredEvents = new ArrayList<>();
        calendarContainer.getChildren().clear();
        if (Objects.equals(type, "week")) {
            dayNumber = 7;
            filteredEvents = filterEventsByWeek(events, datePicker.getValue());
        } else if (Objects.equals(type, "day")) {
            dayNumber = 1;
            filteredEvents = filterEventsByDay(events, datePicker.getValue());
        }
        GridPane dayCalendar = createDayCalendar(datePicker.getValue(), filteredEvents);
        calendarContainer.getChildren().add(dayCalendar);
    }

    private void onChangeDate(LocalDate newDate) {
        if (dayNumber == 7) {
            updateCalendar("week");
        } else if (dayNumber == 1) {
            updateCalendar("day");
        }
    }

    @FXML
    public void onClickDay() {
        updateCalendar("day");
    }

    @FXML
    public void onClickWeek() {
        updateCalendar("week");

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
        datePicker.setValue(LocalDate.now());
        PersonalEvents personalEvents = new PersonalEvents(HelloApplication.user);
        events = personalEvents.getEvents();
        if (dayNumber == 7) {
            updateCalendar("week");
        } else if (dayNumber == 1) {
            updateCalendar("day");
        }
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

            if (scene != null) { // add a null check
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/css/dark.css").toExternalForm());
            }

        } else {
            btnDark.setText("Dark");
            // Apply light mode stylesheet
            Scene scene = btnFormation.getScene();

            if (scene != null) { // add a null check
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/css/light.css").toExternalForm());
            }

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
        } else if (Objects.equals(selectedButtonText.get(), "btnFormation")) {
            System.out.println(selectedButtonText.get());
            PromotionCollection instance = PromotionCollection.getInstance();
            ArrayList<Promotion> teacherSuggestions = instance.getPromotionLike(currentText);
            for (Promotion t : teacherSuggestions) {
                suggestions.add(t.getName());
            }
        }

        // Dispose of the old autocompletion binding if it exists
        if (autoCompletionBinding != null) {
            autoCompletionBinding.dispose();
        }

        Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>> suggestionProvider = request -> suggestions
                .stream()
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

                events = teacherSuggestions.get(0).getTeacherEvents(HelloApplication.user);


                if (dayNumber == 7) {
                    events = filterEventsByWeek(events, datePicker.getValue());
                    updateCalendar("week");
                } else if (dayNumber == 1) {
                    events = filterEventsByDay(events, datePicker.getValue());
                    updateCalendar("day");
                }
            } else if (Objects.equals(selectedButtonText.get(), "btnSalle")) {
                ClassroomCollection instance = ClassroomCollection.getInstance();
                ArrayList<Classroom> teacherSuggestions = instance.getClassroomLike(currentText);
                System.out.println(teacherSuggestions.get(0).getCode());
                events = teacherSuggestions.get(0).getClassroomEdt(HelloApplication.user);
                if (dayNumber == 7) {
                    events = filterEventsByWeek(events, datePicker.getValue());
                    updateCalendar("week");
                } else if (dayNumber == 1) {
                    events = filterEventsByDay(events, datePicker.getValue());
                    updateCalendar("day");
                }
                System.out.println("GridPane added");
            } else if (Objects.equals(selectedButtonText.get(), "btnFormation")) {
                PromotionCollection instance = PromotionCollection.getInstance();
                ArrayList<Promotion> teacherSuggestions = instance.getPromotionLike(currentText);
                System.out.println(teacherSuggestions.get(0).getCode());
                events = teacherSuggestions.get(0).getPromotionEdt(HelloApplication.user);
                if (dayNumber == 7) {
                    events = filterEventsByWeek(events, datePicker.getValue());
                    updateCalendar("week");
                } else if (dayNumber == 1) {
                    events = filterEventsByDay(events, datePicker.getValue());
                    updateCalendar("day");
                }
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

    private StackPane getEvent(Event event, double rowSpan) {
        double initialWidth = parentContainer.getWidth() - 16;
        Pane eventRectangle = new Pane();
        eventRectangle.setPrefHeight(25 * rowSpan);
        eventRectangle.setMinWidth(0);
        eventRectangle.getStyleClass().add("rect");
        eventRectangle.setOpacity(0.7);

        // Bind the rectangle's width to the parent container's width with adjustments
        eventRectangle.prefWidthProperty().bind(parentContainer.widthProperty().subtract(92).divide(dayNumber));

        Label eventLabel = new Label(String.format("Matière : %s\nEnseignant : %s\nType : %s\nSalle : %s\nType : %s",
                event.getTitle(), event.getTeacher().getName(),
                event.getType(), event.getClassroom().getName(),
                event.getType()));
        eventLabel.setFont(new Font(12));
        eventLabel.setTextAlignment(TextAlignment.CENTER);
        eventLabel.setWrapText(true);

        StackPane eventStackPane = new StackPane(eventRectangle, eventLabel);
        Tooltip tooltip = new Tooltip(eventLabel.getText());
        Tooltip.install(eventStackPane, tooltip);
        eventStackPane.minWidth(0);
        eventStackPane.prefWidthProperty().bind(parentContainer.widthProperty().subtract(86 + 16));
        return eventStackPane;
    }

    private GridPane createDayCalendar(LocalDate date, List<Event> events) {
        List<LocalTime> timeSlots = generateTimeSlots();
        GridPane calendar = new GridPane();
        calendar.setMinWidth(0);
        calendar.prefWidthProperty().bind(parentContainer.widthProperty());
        calendar.setHgap(5);
        calendar.setVgap(5);

        // Adjust maximum width to consider padding/margin
        calendar.setMaxWidth(parentContainer.getWidth() - 16);

        // Set a fixed width for the first column (hours column)
        ColumnConstraints hourColumnConstraints = new ColumnConstraints(60);
        calendar.getColumnConstraints().add(hourColumnConstraints);

        for (int day = 1; day <= dayNumber; day++) {
            ColumnConstraints dayColumnConstraints = new ColumnConstraints();
            dayColumnConstraints.prefWidthProperty().bind(
                    parentContainer.widthProperty().subtract(76).divide(dayNumber));
            calendar.getColumnConstraints().add(dayColumnConstraints);
        }

        for (int rowIndex = 0; rowIndex < timeSlots.size(); rowIndex++) {
            LocalTime timeSlot = timeSlots.get(rowIndex);
            Label timeLabel = new Label(timeSlot.format(DateTimeFormatter.ofPattern("HH:mm")));
            timeLabel.getStyleClass().add("white-background-opacity");
            calendar.add(timeLabel, 0, rowIndex);
            for (Event event : events) {
                ZonedDateTime eventStart = ZonedDateTime.parse(event.getStart()).plusHours(2);
                ZonedDateTime eventEnd = ZonedDateTime.parse(event.getEnd()).plusHours(2);

                if (!eventStart.toLocalDate().isEqual(date) && dayNumber == 1)
                    continue;

                boolean isInTimeSlot = !eventStart.toLocalTime().isBefore(timeSlot)
                        && eventEnd.toLocalTime().isAfter(timeSlot);
                if (!isInTimeSlot)
                    continue;

                int eventStartIndex = Math
                        .max(timeSlots.indexOf(eventStart.toLocalTime().truncatedTo(ChronoUnit.MINUTES)), 0);
                int eventEndIndex = Math.min(timeSlots.indexOf(eventEnd.toLocalTime().truncatedTo(ChronoUnit.MINUTES)),
                        timeSlots.size() - 1);

                if (eventStartIndex == -1 || eventEndIndex == -1)
                    continue;

                int rowSpan = eventEndIndex - eventStartIndex + 1;

                int eventDayColumn = 1;
                if (dayNumber == 7) {
                    eventDayColumn = calculateEventDayColumn(event);
                }

                if (eventDayColumn > 0) {
                    calendar.add(getEvent(event, rowSpan), eventDayColumn, eventStartIndex, 1, rowSpan);
                }
            }
        }

        calendar.setMaxHeight(parentContainer.getHeight() - 50); // Adjust for bottom padding/margin
        return calendar;
    }

    public static int calculateEventDayColumn(Event event) {
        // Assuming event.getStart() returns a date time string in ISO format
        LocalDate eventDate = LocalDate.parse(event.getStart(), DateTimeFormatter.ISO_DATE_TIME);

        // Get the day of the week for the event's start date
        DayOfWeek dayOfWeek = eventDate.getDayOfWeek();

        // Map the day of the week to a column index (1 for Monday, 2 for Tuesday, ...)
        switch (dayOfWeek) {
            case MONDAY:
                return 1;
            case TUESDAY:
                return 2;
            case WEDNESDAY:
                return 3;
            case THURSDAY:
                return 4;
            case FRIDAY:
                return 5;
            case SATURDAY:
                return 6;
            case SUNDAY:
                return 7;
            default:
                return -1; // Default case, though it should never be reached
        }
    }

}
