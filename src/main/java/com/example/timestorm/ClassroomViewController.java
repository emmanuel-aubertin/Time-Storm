

package com.example.timestorm;
import com.example.timestorm.HelloApplication;
import com.example.timestorm.edtutils.Classroom;
import com.example.timestorm.edtutils.ClassroomCollection;
import com.example.timestorm.edtutils.Event;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;



public class ClassroomViewController {

    private LoginProvider user;
    public void setUser(LoginProvider user) {
        this.user = user;
    }

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField searchField;

    @FXML
    private ToggleButton jourToggleButton;
    private ToggleButton semaineToggleButton;
    private ToggleButton moisToggleButton;
    private ToggleGroup viewToggleGroup;
    @FXML
    private VBox parentContainer;

    @FXML
    private StackPane calendarContainer;

    public void initialize() {
        // Définir la date par défaut sur la date actuelle
        datePicker.setValue(LocalDate.now());

        // Bind calendar container dimensions to parent container dimensions
        calendarContainer.prefWidthProperty().bind(parentContainer.widthProperty());
        calendarContainer.prefHeightProperty().bind(parentContainer.heightProperty());
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String searchString = searchField.getText();
        LocalDate selectedDate = datePicker.getValue();

        // Ensure that the classroom data is initialized before searching
        if (!ClassroomCollection.isIsInitialized()) {
            System.out.println("Fetching classroom data...");
            ClassroomCollection.getInstance();
        }

        // Perform the search after ensuring data is initialized
        ArrayList<Classroom> searchResult = ClassroomCollection.getClassroomLike(searchString);
        if (searchResult != null) {
            for (Classroom classroom : searchResult) {
                System.out.println(classroom); // Print the search result in the terminal

                // Fetch the schedule for each classroom
                try {
                    ArrayList<Event> classroomSchedule = classroom.getClassroomEdt(HelloApplication.user);

                    // Filter the events based on the selected toggle button and date picker value
                    ArrayList<Event> filteredEvents = new ArrayList<>();
                    if (jourToggleButton.isSelected()) {
                        filteredEvents = filterEventsByDay(classroomSchedule, selectedDate);
                        GridPane dayCalendar = createDayCalendar(selectedDate, filteredEvents);
                        calendarContainer.getChildren().clear();
                        calendarContainer.getChildren().add(dayCalendar);
                    } else if (semaineToggleButton.isSelected()) {
                        filteredEvents = filterEventsByWeek(classroomSchedule, selectedDate);
                    } else if (moisToggleButton.isSelected()) {
                        filteredEvents = filterEventsByMonth(classroomSchedule, selectedDate);
                    }

                    // Print the filtered events in the terminal or update the UI with the schedule
                    for (Event eventClassroom : filteredEvents) {
                        System.out.println(eventClassroom);
                    }
                } catch (RuntimeException e) {
                    System.out.println("Error fetching classroom schedule: " + e.getMessage());
                }
            }
        } else {
            System.out.println("No classrooms found."); // Inform the user if no classrooms are found
        }
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

    // Set a fixed width for the first column (hours column)
    ColumnConstraints columnConstraints = new ColumnConstraints();
    columnConstraints.setPrefWidth(60); // Adjust the width as needed
    calendar.getColumnConstraints().add(columnConstraints);

    int rowIndex = 0;
    for (LocalTime timeSlot : timeSlots) {
        Label timeLabel = new Label(timeSlot.format(DateTimeFormatter.ofPattern("HH:mm")));
        calendar.add(timeLabel, 0, rowIndex);

        int columnIndex = 1;
        for (Event event : events) {
            ZonedDateTime eventStart = ZonedDateTime.parse(event.getStart());
            ZonedDateTime eventEnd = ZonedDateTime.parse(event.getEnd());

            if (eventStart.toLocalDate().isEqual(date) &&
                (eventStart.toLocalTime().isAfter(timeSlot) && eventStart.toLocalTime().isBefore(timeSlot.plusMinutes(30))) ||
                (eventEnd.toLocalTime().isAfter(timeSlot) && eventEnd.toLocalTime().isBefore(timeSlot.plusMinutes(30))) ||
                (eventStart.toLocalTime().isBefore(timeSlot) && eventEnd.toLocalTime().isAfter(timeSlot.plusMinutes(30)))
            ) {
                int eventStartIndex = timeSlots.indexOf(eventStart.toLocalTime().truncatedTo(ChronoUnit.MINUTES));
                int eventEndIndex = timeSlots.indexOf(eventEnd.toLocalTime().truncatedTo(ChronoUnit.MINUTES));
                int rowSpan = eventEndIndex - eventStartIndex + 1;

                Rectangle eventRectangle = new Rectangle();
                // Set the width of the event rectangle to the parent container's width minus the fixed width of the first column
                eventRectangle.setWidth(parentContainer.getWidth() - 65); // 65 = 60 (first column width) + 5 (hgap)
                eventRectangle.setHeight(25 * rowSpan);
                eventRectangle.setFill(Color.BLUE); // Set the color of the rectangle
                calendar.add(eventRectangle, columnIndex, eventStartIndex, 1, rowSpan);

                StackPane eventStackPane = new StackPane(eventRectangle);

                Label eventLabel = new Label();
                eventLabel.setTextFill(Color.WHITE);
                eventLabel.setFont(new Font(12));
                eventLabel.setTextAlignment(TextAlignment.CENTER);
                eventLabel.setWrapText(true);
                eventLabel.prefWidthProperty().bind(eventRectangle.widthProperty()); // Bind the width of the label to the width of the rectangle

                String eventDetails = "Matière : " + event.getCode() + "\n" +
                        "Enseignant : " + event.getTeacher().getName() + "\n" +
                        "TD : " + event.getType() + "\n" +
                        "Salle : " + event.getClassroom().getName() + "\n" +
                        "Type : " + event.getType();
                Tooltip tooltip = new Tooltip(eventDetails);
                Tooltip.install(eventRectangle, tooltip);
                eventLabel.setText(eventDetails);

                eventStackPane.getChildren().add(eventLabel);

                calendar.add(eventStackPane, columnIndex, rowIndex);

                columnIndex++;
            }
        }

        rowIndex++;
    }

    // Set the maximum height of the calendar based on the available space in the parent container
    calendar.setMaxHeight(parentContainer.getHeight() - 50); // Adjust the value as needed

    return calendar;
}




}

