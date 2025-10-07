module com.bitejiuyeke.bitefileapi {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.bitejiuyeke.bitefileapi to javafx.fxml;
    exports com.bitejiuyeke.bitefileapi;
}