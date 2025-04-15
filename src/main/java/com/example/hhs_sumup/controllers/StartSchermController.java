package com.example.hhs_sumup.controllers;

import com.example.hhs_sumup.Database.DatabaseConnection;
import com.example.hhs_sumup.models.Model;
import com.example.hhs_sumup.models.Student;
import com.example.hhs_sumup.models.Studiestof;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StartSchermController {
    public Button account_knop;
    public Button notificaties_knop;
    public Label welkom_tekst;
    public TextField zoek_balk;
    public ListView<String> notificaties_lijst;
    public ListView<String> zoek_lijst;
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
        voeg_studiestof_toe.setOnAction(event -> goToStudiestofMakenScherm());
        zoek_balk.setOnMouseClicked(event -> showZoekLijst());

        zoek_balk.textProperty().addListener((observable, oldValue, newValue) -> searchStudiestof());

        zoek_lijst.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Check for double-click
                String selectedTitle = zoek_lijst.getSelectionModel().getSelectedItem();
                if (selectedTitle != null) {
                    goToStudiestofScherm(selectedTitle);
                }
            }
        });
    }

    private void searchStudiestof() {
        String searchText = zoek_balk.getText().trim();
        if (searchText.isEmpty()) {
            zoek_lijst.setItems(FXCollections.observableArrayList());
            return;
        }

        String query = "SELECT ss_titel FROM studiestof WHERE ss_titel LIKE ?";
        ObservableList<String> matchingTitles = FXCollections.observableArrayList();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + searchText + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                matchingTitles.add(resultSet.getString("ss_titel"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        zoek_lijst.setItems(matchingTitles);
    }

    public void showZoekLijst() {
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

    public void goToStudiestofScherm(String selectedTitle) {
        Studiestof studiestof = fetchStudiestofByTitle(selectedTitle);
        if (studiestof != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hhs_sumup/fxml/StudiestofScherm.fxml"));
            try {
                Parent root = loader.load();
                StudiestofSchermController controller = loader.getController();
                controller.setStudiestof(studiestof);

                Stage stage = (Stage) account_knop.getScene().getWindow();
                Model.getInstance().getViewFactory().closeWindow(stage);
                Model.getInstance().getViewFactory().showStudiestofWindow(selectedTitle);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Studiestof fetchStudiestofByTitle(String title) {
        String query = "SELECT studiestof_id, ss_titel, ss_inhoud, ss_auteur_id FROM studiestof WHERE ss_titel = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("studiestof_id");
                String naam = resultSet.getString("ss_titel");
                String inhoud = resultSet.getString("ss_inhoud");
                int auteurId = resultSet.getInt("ss_auteur_id");
                return new Studiestof(id, naam, inhoud, auteurId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

