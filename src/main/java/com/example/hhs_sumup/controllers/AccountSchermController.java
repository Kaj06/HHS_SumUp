package com.example.hhs_sumup.controllers;

import com.example.hhs_sumup.models.Model;
import com.example.hhs_sumup.models.Student;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class AccountSchermController {
    public Label hallo_naam;
    public Label naam;
    public Button verander_naam;
    public Label email;
    public Button verander_email;
    public Button verander_wachtwoord;
    public Button terug_naar_start;
    public ListView jouw_studiestof;
    public Button verwijder_studiestof;

    public void initialize() {
        Student loggedInStudent = Model.getInstance().getLoggedInUser();
        String fullName = loggedInStudent.getS_naam();
        String firstName = fullName.split(" ")[0];
        hallo_naam.setText("Hallo, " + firstName + "!");

        naam.setText(loggedInStudent.getS_naam());
        email.setText(loggedInStudent.getS_hhsemail());

        terug_naar_start.setOnAction(event -> goToStartWindow());
    }

    public void veranderNaam() {

    }

    public void veranderEmail() {

    }

    public void veranderWachtwoord() {

    }

    private void goToStartWindow() {
        Stage stage = (Stage) email.getScene().getWindow();
        Model.getInstance().getViewFactory().closeWindow(stage);
        Model.getInstance().getViewFactory().showStartWindow();
    }
}
