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
    public ListView studiestof;
    public Button open_studiestof;

    public void initialize() {


        terug_naar_start.setOnAction(event -> goToStartWindow());
        open_studiestof.setOnAction(event -> openStudiestofWindow());

        int selectedStudiestofId = Model.getInstance().getSelectedStudieStofId();
        if (selectedStudiestofId != -1) {
            loadStudentInfo(selectedStudiestofId);
        }
    }

    private void loadStudentInfo(int studiestofId) {
        String query = "SELECT s.s_naam, s.s_hhsemail FROM student s " +
                       "JOIN studiestof ss ON s.student_id = ss.ss_auteur_id " +
                       "WHERE ss.studiestof_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studiestofId);
            ResultSet resultSet = statement.executeQuery();
            String FullName = Model.getInstance().getLoggedInUser().getS_naam();
            String firstName = FullName.split(" ")[0];
            if (resultSet.next()) {
                String studentName = resultSet.getString("s_naam");
                String studentEmail = resultSet.getString("s_hhsemail");

                dit_is.setText("Dit is: " + studentName);
                email_van_student.setText("Email van " + firstName + ":");
                email.setText(studentEmail);
                studiestof_van_student.setText("Studiestof van " + firstName + ":");
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

    private void openStudiestofWindow() {
        Stage stage = (Stage) terug_naar_start.getScene().getWindow();
        Model.getInstance().getViewFactory().closeWindow(stage);
        Model.getInstance().getViewFactory().showStudiestofWindow();
    }
}