package com.example.hhs_sumup.controllers;

import com.example.hhs_sumup.models.Model;
import com.example.hhs_sumup.models.Student;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StartSchermController {
    public Button account_knop;
    public Button notificaties_knop;
    public Label welkom_tekst;
    public TextField zoek_balk;
    public ListView notificaties_lijst;
    public ListView zoek_lijst;

    public void initialize() {
        notificaties_lijst.setVisible(false);
        zoek_lijst.setVisible(false);

        Student loggedInStudent = Model.getInstance().getLoggedInUser();
        String fullName = loggedInStudent.getS_naam();
        String firstName = fullName.split(" ")[0];
        welkom_tekst.setText("Welkom bij SumUp, " + firstName + "!");

        notificaties_knop.setOnAction(event -> onNotificatiesKnop());
        account_knop.setOnAction(event -> goToAccountWindow());
        zoek_balk.setOnAction(event -> onZoeken());
    }

    public void onZoeken() {
        zoek_lijst.setVisible(!zoek_lijst.isVisible());
    }

    public void onNotificatiesKnop() {
        notificaties_lijst.setVisible(!notificaties_lijst.isVisible());
    }

    private void goToAccountWindow() {
        Stage stage = (Stage) welkom_tekst.getScene().getWindow();
        Model.getInstance().getViewFactory().closeWindow(stage);
        Model.getInstance().getViewFactory().showAccountWindow();
    }
}

