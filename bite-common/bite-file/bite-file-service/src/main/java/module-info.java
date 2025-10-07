module com.bitejiuyeke.bitefileservice {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.bitejiuyeke.bitefileservice to javafx.fxml;
    exports com.bitejiuyeke.bitefileservice;
}