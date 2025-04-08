module com.example.hhs_sumup {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.hhs_sumup to javafx.fxml;
    exports com.example.hhs_sumup;
    exports com.example.hhs_sumup.controllers;
    opens com.example.hhs_sumup.controllers to javafx.fxml;
    exports com.example.hhs_sumup.Database;
    opens com.example.hhs_sumup.Database to javafx.fxml;
}