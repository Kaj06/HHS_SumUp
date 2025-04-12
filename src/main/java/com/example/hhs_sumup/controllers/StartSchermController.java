package com.example.hhs_sumup.controllers;

import com.example.hhs_sumup.Database.DatabaseConnection;
import com.example.hhs_sumup.models.Model;
import com.example.hhs_sumup.models.Student;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StartSchermController {
    public Button account_knop;
    public Button notificaties_knop;
    public Label welkom_tekst;
    public TextField zoek_balk;
    public ListView notificaties_lijst;
    public ListView zoek_lijst;
    public Button voeg_studiestof_toe;

    public void initialize() {
        notificaties_lijst.setVisible(false);
        zoek_lijst.setVisible(false);

        Student loggedInStudent = Model.getInstance().getLoggedInUser();
        String fullName = loggedInStudent.getS_naam();
        String firstName = fullName.split(" ")[0];
        welkom_tekst.setText("Welkom bij SumUp, " + firstName + "!");

        notificaties_knop.setOnAction(event -> onNotificatiesKnop());
        account_knop.setOnAction(event -> goToAccountWindow());
        zoek_balk.setOnAction(event -> searchStudiestof());
        zoek_balk.setOnMouseClicked(event -> setZoek_balk());
        voeg_studiestof_toe.setOnAction(event -> goToStudiestofMakenScherm());
    }

    public void searchStudiestof() {
        String studiestofName = zoek_balk.getText().trim();
        if (studiestofName.isEmpty()) {
            return;
        }

        String query = "SELECT studiestof_id FROM studiestof WHERE ss_titel = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, studiestofName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int studiestofId = resultSet.getInt("studiestof_id");
                Model.getInstance().setSelectedStudieStofId(studiestofId);

                Stage stage = (Stage) zoek_balk.getScene().getWindow();
                Model.getInstance().getViewFactory().closeWindow(stage);
                Model.getInstance().getViewFactory().showStudiestofWindow();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setZoek_balk() {
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

    public void goToStudiestofMakenScherm() {
        Stage stage = (Stage) voeg_studiestof_toe.getScene().getWindow();
        Model.getInstance().getViewFactory().closeWindow(stage);
        Model.getInstance().getViewFactory().showStudiestofMakenWindow();
    }
}

