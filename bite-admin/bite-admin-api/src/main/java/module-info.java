module com.bitejiuyeke.biteadminapi {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.bitejiuyeke.biteadminapi to javafx.fxml;
    exports com.bitejiuyeke.biteadminapi;
}