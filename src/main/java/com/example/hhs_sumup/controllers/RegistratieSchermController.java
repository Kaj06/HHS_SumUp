package com.example.hhs_sumup.controllers;

import com.example.hhs_sumup.Database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistratieSchermController {
    public Hyperlink terug_naar_inloggen;
    public ChoiceBox studie_kiezen;
    public Button zie_ww_herhalen;
    public Text niet_hhs_email;
    @FXML
    private TextField student_email;

    @FXML
    private PasswordField student_ww;

    @FXML
    private PasswordField student_ww_herhalen;

    @FXML
    private Button registreren;

    @FXML
    private Text Email_wachtwoord_verkeerd;

    @FXML
    private Text ww_vergeten_intevullen;

    @FXML
    private Text Email_vergeten_intevullen;

    @FXML
    private Text Email_wachtwoord_vergeten;

    @FXML
    private Text ww_herhalen_vergeten_intevullen1;

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

    @FXML
    public void initialize() {
        ww_vergeten_intevullen.setVisible(false);
        Email_vergeten_intevullen.setVisible(false);
        Email_wachtwoord_vergeten.setVisible(false);
        Email_wachtwoord_verkeerd.setVisible(false);
        ww_herhalen_vergeten_intevullen1.setVisible(false);
        niet_hhs_email.setVisible(false);

        registreren.setOnAction(event -> handleRegistration());
        loadStudies(); // Load studies into the ChoiceBox
    }

    private void handleRegistration() {
        String email = student_email.getText();
        String password = student_ww.getText();
        String passwordRepeat = student_ww_herhalen.getText();

        if (email.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty()) {
            Email_vergeten_intevullen.setVisible(email.isEmpty());
            ww_vergeten_intevullen.setVisible(password.isEmpty());
            ww_herhalen_vergeten_intevullen1.setVisible(passwordRepeat.isEmpty());
            return;
        }

        if (!password.equals(passwordRepeat)) {
            Email_wachtwoord_verkeerd.setVisible(true);
            return;
        }

        saveUser(email, password);
    }

    private void saveUser(String email, String password) {
        String query = "INSERT INTO student (s_hhsemail, s_wachtwoord) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}