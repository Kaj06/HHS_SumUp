package com.example.hhs_sumup.views;

import com.example.hhs_sumup.controllers.AndereStudentSchermController;
import com.example.hhs_sumup.controllers.StudiestofSchermController;
import com.example.hhs_sumup.models.Studiestof;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {

    public void showRegistratieWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hhs_sumup/fxml/RegistratieScherm.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Registratie Scherm");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showInlogWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hhs_sumup/fxml/InlogScherm.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Inlog Scherm");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStartWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hhs_sumup/fxml/StartScherm.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Start Scherm");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAccountWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hhs_sumup/fxml/AccountScherm.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Account Scherm");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAndereStudentWindow(int auteurId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hhs_sumup/fxml/AndereStudentScherm.fxml"));
            Parent root = loader.load();

            AndereStudentSchermController controller = loader.getController();
            controller.setAuteurId(auteurId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStudiestofWindow(String selectedTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hhs_sumup/fxml/StudiestofScherm.fxml"));
            Parent root = loader.load();

            StudiestofSchermController controller = loader.getController();
            Studiestof studiestof = Studiestof.fetchByTitle(selectedTitle); // Use the static method
            controller.setStudiestof(studiestof);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStudiestofMakenWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hhs_sumup/fxml/StudiestofMakenScherm.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Studiestof toevoegen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeWindow(Stage stage) {
        if (stage != null) {
            stage.close();
        }
    }
}
