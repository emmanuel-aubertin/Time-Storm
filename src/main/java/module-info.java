module com.example.timestorm {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires jsoup;
    opens com.example.timestorm to javafx.fxml;
    exports com.example.timestorm;
}