package com.example.hhs_sumup.controllers;

import com.example.hhs_sumup.Database.DatabaseConnection;
import com.example.hhs_sumup.models.Model;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AndereStudentSchermController {
    public Label dit_is;
    public Label email_van_student;
    public Label email;
    public Button terug_naar_start;
    public Text studiestof_van_student;
    public ListView<String> studiestof;
    public Button open_studiestof;
    private int auteurId;

    public void initialize() {
        terug_naar_start.setOnAction(event -> goToStartWindow());
        open_studiestof.setOnAction(event -> {
            String selectedTitle = studiestof.getSelectionModel().getSelectedItem();
            if (selectedTitle != null) {
                openStudiestofWindow(selectedTitle);
            }
        });
    }

    public void setAuteurId(int auteurId) {
        this.auteurId = auteurId;
        loadStudentInfo(auteurId);
    }

private void loadStudentInfo(int auteurId) {
    String studentQuery = "SELECT s.s_naam, s.s_hhsemail FROM student s WHERE s.student_id = ?";
    String studiestofQuery = "SELECT ss_titel FROM studiestof WHERE ss_auteur_id = ?";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement studentStatement = connection.prepareStatement(studentQuery);
         PreparedStatement studiestofStatement = connection.prepareStatement(studiestofQuery)) {

        studentStatement.setInt(1, auteurId);
        ResultSet studentResultSet = studentStatement.executeQuery();
        if (studentResultSet.next()) {
            String studentName = studentResultSet.getString("s_naam");
            String studentEmail = studentResultSet.getString("s_hhsemail");
            String fullName = Model.getInstance().getLoggedInUser().getS_naam();
            String firstName = fullName.split(" ")[0];

            dit_is.setText("Dit is: " + studentName);
            email_van_student.setText("Email van " + firstName + ":");
            email.setText(studentEmail);
            studiestof_van_student.setText("Studiestof van " + firstName + ":");
        }

        studiestofStatement.setInt(1, auteurId);
        ResultSet studiestofResultSet = studiestofStatement.executeQuery();
        studiestof.getItems().clear();
        while (studiestofResultSet.next()) {
            String studiestofTitle = studiestofResultSet.getString("ss_titel");
            studiestof.getItems().add(studiestofTitle);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    private void goToStartWindow() {
        Stage stage = (Stage) email.getScene().getWindow();
        Model.getInstance().getViewFactory().closeWindow(stage);
        Model.getInstance().getViewFactory().showStartWindow();
    }

    private void openStudiestofWindow(String selectedTitle) {
        Stage stage = (Stage) terug_naar_start.getScene().getWindow();
        Model.getInstance().getViewFactory().closeWindow(stage);
        Model.getInstance().getViewFactory().showStudiestofWindow(selectedTitle);
    }
}