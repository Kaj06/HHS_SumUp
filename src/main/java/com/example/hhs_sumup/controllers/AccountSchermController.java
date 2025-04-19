package com.example.hhs_sumup.controllers;

import com.example.hhs_sumup.Database.DatabaseConnection;
import com.example.hhs_sumup.InterfaceController;
import com.example.hhs_sumup.models.Model;
import com.example.hhs_sumup.models.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountSchermController implements InterfaceController {
    public Label hallo_naam;
    public Label naam;
    public Button verander_naam;
    public Label email;
    public Button verander_email;
    public Button verander_wachtwoord;
    public Button terug_naar_start;
    public ListView<String> jouw_studiestof;
    public Button verwijder_studiestof;
    public AnchorPane checkwachtwoord;
    public TextField wachtwoord_invullen;
    public Button check_wachtwoord;
    public Button cancel_btn1;
    public AnchorPane verander_gegevens_anchor;
    public TextField verander_gegevens;
    public Button verander_gegevens_btn;
    public Button cancel_btn;
    public Label check_ww_label;

    public void initialize() {
        Student loggedInStudent = Model.getInstance().getLoggedInUser();
        String fullName = loggedInStudent.getS_naam();
        String firstName = fullName.split(" ")[0];
        hallo_naam.setText("Hallo, " + firstName + "!");

        naam.setText(loggedInStudent.getS_naam());
        email.setText(loggedInStudent.getS_hhsemail());

        terug_naar_start.setOnAction(event -> goToStartWindow());
        loadUserStudiestof(loggedInStudent.getStudent_id());
        verwijder_studiestof.setOnAction(event -> onVerwijderStudiestof());
        verander_naam.setOnAction(event -> onVeranderNaam());
        verander_email.setOnAction(event -> onVeranderEmail());
        verander_wachtwoord.setOnAction(event -> onVeranderWachtwoord());
        cancel_btn.setOnAction(event -> cancelBtn());
        cancel_btn1.setOnAction(event -> cancelBtn());
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

    private void onVeranderNaam() {
        checkwachtwoord.setVisible(true);
        check_ww_label.setVisible(false);

        check_wachtwoord.setOnAction(event -> {
            String ingevuldWachtwoord = wachtwoord_invullen.getText();
            Student loggedInStudent = Model.getInstance().getLoggedInUser();

            if (loggedInStudent.getS_wachtwoord().equals(ingevuldWachtwoord)) {
                checkwachtwoord.setVisible(false);
                verander_gegevens_anchor.setVisible(true);

                verander_gegevens.setPromptText("Vul hier je nieuwe naam in");
                verander_gegevens_btn.setOnAction(e -> {
                    String nieuweNaam = verander_gegevens.getText();
                    if (!nieuweNaam.isEmpty()) {
                        String query = "UPDATE student SET s_naam = ? WHERE student_id = ?";
                        try (Connection connection = DatabaseConnection.getConnection();
                             PreparedStatement statement = connection.prepareStatement(query)) {
                            statement.setString(1, nieuweNaam);
                            statement.setInt(2, loggedInStudent.getStudent_id());
                            statement.executeUpdate();
                            naam.setText(nieuweNaam);
                            verander_gegevens_anchor.setVisible(false);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            } else {
                check_ww_label.setText("Verkeerd wachtwoord ingevuld");
                check_ww_label.setVisible(true);
            }
        });
    }

    private void onVeranderEmail() {
        checkwachtwoord.setVisible(true);
        check_ww_label.setVisible(!check_ww_label.isVisible());

        String ingevuldWachtwoord = wachtwoord_invullen.getText();
        Student loggedInStudent = Model.getInstance().getLoggedInUser();

        if (loggedInStudent.getS_wachtwoord().equals(ingevuldWachtwoord)) {
            checkwachtwoord.setVisible(!checkwachtwoord.isVisible());
            verander_gegevens_anchor.setVisible(true);

            verander_gegevens.setPromptText("Vul hier je nieuwe email in");
            verander_gegevens_btn.setOnAction(event -> {
                String nieuweEmail = verander_gegevens.getText();
                if (!nieuweEmail.isEmpty()) {
                    String query = "UPDATE student SET s_hhsemail = ? WHERE student_id = ?";
                    try (Connection connection = DatabaseConnection.getConnection();
                         PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setString(1, nieuweEmail);
                        statement.setInt(2, loggedInStudent.getStudent_id());
                        statement.executeUpdate();
                        email.setText(nieuweEmail);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            check_ww_label.setText("Verkeerd wachtwoord ingevuld");
            check_ww_label.setVisible(true);
            System.out.println("Wachtwoord is incorrect");
        }
    }

    public void onVeranderWachtwoord() {
        checkwachtwoord.setVisible(true);
        check_ww_label.setVisible(!check_ww_label.isVisible());

        String ingevuldWachtwoord = wachtwoord_invullen.getText();
        Student loggedInStudent = Model.getInstance().getLoggedInUser();

        if (loggedInStudent.getS_wachtwoord().equals(ingevuldWachtwoord)) {
            checkwachtwoord.setVisible(!checkwachtwoord.isVisible());
            verander_gegevens_anchor.setVisible(true);

            verander_gegevens.setPromptText("Vul hier je nieuwe wachtwoord in");
            verander_gegevens_btn.setOnAction(event -> {
                String nieuwWachtwoord = verander_gegevens.getText();
                if (!nieuwWachtwoord.isEmpty()) {
                    String query = "UPDATE student SET s_wachtwoord = ? WHERE student_id = ?";
                    try (Connection connection = DatabaseConnection.getConnection();
                         PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setString(1, nieuwWachtwoord);
                        statement.setInt(2, loggedInStudent.getStudent_id());
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            check_ww_label.setText("Verkeerd wachtwoord ingevuld");
            check_ww_label.setVisible(true);
            System.out.println("Wachtwoord is incorrect");
        }
    }

    public void cancelBtn() {
        checkwachtwoord.setVisible(false);
        verander_gegevens_anchor.setVisible(false);
    }

    public void onVerwijderStudiestof() {
        String selectedStudiestof = jouw_studiestof.getSelectionModel().getSelectedItem();
        if (selectedStudiestof != null) {
            String query = "DELETE FROM studiestof WHERE ss_titel = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, selectedStudiestof);
                statement.executeUpdate();
                loadUserStudiestof(Model.getInstance().getLoggedInUser().getStudent_id());
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