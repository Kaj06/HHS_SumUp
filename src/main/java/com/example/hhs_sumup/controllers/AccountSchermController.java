package com.example.hhs_sumup.controllers;

import com.example.hhs_sumup.Database.DatabaseConnection;
import com.example.hhs_sumup.models.Model;
import com.example.hhs_sumup.models.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountSchermController {
    public Label hallo_naam;
    public Label naam;
    public Button verander_naam;
    public Label email;
    public Button verander_email;
    public Button verander_wachtwoord;
    public Button terug_naar_start;
    public ListView<String> jouw_studiestof;
    public Button verwijder_studiestof;

    public void initialize() {
        Student loggedInStudent = Model.getInstance().getLoggedInUser();
        String fullName = loggedInStudent.getS_naam();
        String firstName = fullName.split(" ")[0];
        hallo_naam.setText("Hallo, " + firstName + "!");

        naam.setText(loggedInStudent.getS_naam());
        email.setText(loggedInStudent.getS_hhsemail());

        terug_naar_start.setOnAction(event -> goToStartWindow());
        verwijder_studiestof.setOnAction(event -> deleteStudiestof(loggedInStudent.getStudent_id()));
        loadUserStudiestof(loggedInStudent.getStudent_id());
    }

    private void loadUserStudiestof(int studentId) {
        ObservableList<String> studiestofList = FXCollections.observableArrayList();
        String query = "SELECT ss_titel FROM studiestof WHERE ss_auteur_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                studiestofList.add(resultSet.getString("ss_titel"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        jouw_studiestof.setItems(studiestofList);
    }

    private void deleteStudiestof(int studentId) {
        String selectedStudiestof = jouw_studiestof.getSelectionModel().getSelectedItem();
        if (selectedStudiestof != null) {
            String query = "DELETE FROM studiestof WHERE ss_titel = ? AND ss_auteur_id = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, selectedStudiestof);
                statement.setInt(2, studentId);
                statement.executeUpdate();
                loadUserStudiestof(studentId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void goToStartWindow() {
        Stage stage = (Stage) email.getScene().getWindow();
        Model.getInstance().getViewFactory().closeWindow(stage);
        Model.getInstance().getViewFactory().showStartWindow();
    }
}