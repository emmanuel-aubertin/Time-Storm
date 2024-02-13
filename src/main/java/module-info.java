module com.example.timestorm {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires jsoup;
    requires org.seleniumhq.selenium.chrome_driver;

    opens com.example.timestorm to javafx.fxml;
    exports com.example.timestorm;
}