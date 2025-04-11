package com.example.hhs_sumup.controllers;

import com.example.hhs_sumup.Database.DatabaseConnection;
import com.example.hhs_sumup.models.Model;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistratieSchermController {
    public Hyperlink terug_naar_inloggen;
    public ChoiceBox studie_kiezen;
    public Text niet_hhs_email;
    public TextField student_achternaam;
    public TextField student_voornaam;
    public Text wachtwoord_niet_gelijk;
    public Text Email_wachtwoord_vergeten;
    public TextField student_email;
    public PasswordField student_ww;
    public PasswordField student_ww_herhalen;
    public Button registreren;


    @FXML
    public void initialize() {
        niet_hhs_email.setVisible(false);
        wachtwoord_niet_gelijk.setVisible(false);
        Email_wachtwoord_vergeten.setVisible(false);

        terug_naar_inloggen.setOnAction(event -> terugnaarInloggen());
        registreren.setOnAction(event -> handleRegistration());
        loadStudies(); // Load studies into the ChoiceBox
    }

    private void loadStudies() {
        String query = "SELECT st_naam FROM studie";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String studyName = resultSet.getString("st_naam"); // Correct column name
                studie_kiezen.getItems().add(studyName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleRegistration() {
        String name = student_voornaam.getText() + " " + student_achternaam.getText();
        String email = student_email.getText();
        String password = student_ww.getText();
        String passwordRepeat = student_ww_herhalen.getText();
        String selectedStudy = (String) studie_kiezen.getValue();

        niet_hhs_email.setVisible(false);
        wachtwoord_niet_gelijk.setVisible(false);

        if (!email.endsWith("@student.hhs.nl")) {
            niet_hhs_email.setVisible(true);
            return;
        }

        if (email.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty() || name.isEmpty()) {
            Email_wachtwoord_vergeten.setVisible(email.isEmpty() || password.isEmpty());
            wachtwoord_niet_gelijk.setVisible(passwordRepeat.isEmpty());
            return;
        }

        if (!password.equals(passwordRepeat)) {
            wachtwoord_niet_gelijk.setVisible(true);
            return;
        }

        saveUser(name, email, password, selectedStudy);

        goToStartWindow();
    }

    private void saveUser(String name, String email, String password, String studyName) {
        String query = "INSERT INTO student (s_naam, s_hhsemail, s_wachtwoord, s_st_id) " +
                "VALUES (?, ?, ?, (SELECT studie_id FROM studie WHERE st_naam = ?))";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name); // Set the name
            statement.setString(2, email); // Set the email
            statement.setString(3, password); // Set the password
            if (studyName != null) {
                statement.setString(4, studyName); // Set the study name
            } else {
                statement.setNull(4, java.sql.Types.VARCHAR); // Handle null study
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void terugnaarInloggen() {
        Stage stage = (Stage) student_email.getScene().getWindow();
        Model.getInstance().getViewFactory().closeWindow(stage);
        Model.getInstance().getViewFactory().showInlogWindow();
    }

    private void goToStartWindow() {
        Stage stage = (Stage) registreren.getScene().getWindow();
        Model.getInstance().getViewFactory().closeWindow(stage);
        Model.getInstance().getViewFactory().showStartWindow();
    }
}