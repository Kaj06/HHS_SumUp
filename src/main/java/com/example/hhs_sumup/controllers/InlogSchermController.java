package com.example.hhs_sumup.controllers;

import com.example.hhs_sumup.Database.DatabaseConnection;
import com.example.hhs_sumup.models.Model;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InlogSchermController {
    @FXML
    private TextField student_email;

    @FXML
    private PasswordField student_ww;

    @FXML
    private Button zie_ww;

    @FXML
    private Hyperlink ww_vergeten;

    @FXML
    private Button inloggen;

    @FXML
    private Text Email_wachtwoord_verkeerd;

    @FXML
    private Text ww_vergeten_intevullen;

    @FXML
    private Text Email_vergeten_intevullen;

    @FXML
    private Text Email_wachtwoord_vergeten;

    @FXML
    private Hyperlink registreren;

    @FXML
    public void initialize() {
        ww_vergeten_intevullen.setVisible(false);
        Email_vergeten_intevullen.setVisible(false);
        Email_wachtwoord_vergeten.setVisible(false);
        Email_wachtwoord_verkeerd.setVisible(false);

        inloggen.setOnAction(event -> handleLogin());
        registreren.setOnAction(event -> openRegistratieScherm());
    }

    private void handleLogin() {
        String email = student_email.getText();
        String password = student_ww.getText();

        if (email.isEmpty() || password.isEmpty()) {
            Email_vergeten_intevullen.setVisible(email.isEmpty());
            ww_vergeten_intevullen.setVisible(password.isEmpty());
            return;
        }

        if (isUserExists(email)) {
            if (isPasswordCorrect(email, password)) {
                // Proceed with login
            } else {
                Email_wachtwoord_verkeerd.setVisible(true);
            }
        } else {
            Email_wachtwoord_verkeerd.setVisible(true);
        }
    }

    private boolean isUserExists(String email) {
        String query = "SELECT COUNT(*) FROM student WHERE s_hhsemail = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isPasswordCorrect(String email, String password) {
        String query = "SELECT s_wachtwoord FROM student WHERE s_hhsemail = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("s_wachtwoord").equals(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void openRegistratieScherm() {
        Stage stage = (Stage) student_email.getScene().getWindow();
        Model.getInstance().getViewFactory().closeWindow(stage);
        Model.getInstance().getViewFactory().showRegistratieWindow();
    }
}