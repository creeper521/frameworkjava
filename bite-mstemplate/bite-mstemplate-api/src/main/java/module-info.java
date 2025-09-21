module com.example.bitemstemplateapi {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.bitemstemplateapi to javafx.fxml;
    exports com.example.bitemstemplateapi;
}