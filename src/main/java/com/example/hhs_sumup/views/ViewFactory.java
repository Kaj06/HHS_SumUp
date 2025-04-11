package com.example.hhs_sumup.views;

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

    public void closeWindow(Stage stage) {
        if (stage != null) {
            stage.close();
        }
    }
}
