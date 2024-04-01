module com.example.timestorm {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires jsoup;
    requires okhttp3;
    requires org.json;
    requires java.desktop;
    opens com.example.timestorm to javafx.fxml;
    exports com.example.timestorm;
}