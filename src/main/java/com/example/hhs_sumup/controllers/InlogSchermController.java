package com.example.hhs_sumup.controllers;

import com.example.hhs_sumup.Database.DatabaseConnection;
import com.example.hhs_sumup.InterfaceController;
import com.example.hhs_sumup.models.Model;
import com.example.hhs_sumup.models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InlogSchermController implements InterfaceController {
    public TextField student_email;
    public PasswordField student_ww;
    public Hyperlink ww_vergeten;
    public Button inloggen;
    public Text Email_wachtwoord_verkeerd;
    public Text ww_vergeten_intevullen;
    public Text Email_vergeten_intevullen;
    public Text Email_wachtwoord_vergeten;
    public Hyperlink registreren;


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

    Email_vergeten_intevullen.setVisible(false);
    ww_vergeten_intevullen.setVisible(false);
    Email_wachtwoord_verkeerd.setVisible(false);

    if (email.isEmpty() || password.isEmpty()) {
        Email_vergeten_intevullen.setVisible(email.isEmpty());
        ww_vergeten_intevullen.setVisible(password.isEmpty());
        return;
    }

    if (isUserExists(email)) {
        if (isPasswordCorrect(email, password)) {
            String query = "SELECT * FROM student WHERE s_hhsemail = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, email);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    Model.getInstance().setLoggedInUser(new Student(
                        resultSet.getInt("student_id"),
                        resultSet.getString("s_naam"),
                        resultSet.getString("s_hhsemail"),
                        resultSet.getString("s_wachtwoord"),
                        resultSet.getString("s_st_id")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            goToStartWindow();
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

    private void goToStartWindow() {
        Stage stage = (Stage) inloggen.getScene().getWindow();
        Model.getInstance().getViewFactory().closeWindow(stage);
        Model.getInstance().getViewFactory().showStartWindow();
    }
}