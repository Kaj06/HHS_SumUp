package com.example.hhs_sumup.controllers;

import com.example.hhs_sumup.Database.DatabaseConnection;
import com.example.hhs_sumup.models.Model;
import com.example.hhs_sumup.models.Studiestof;
import com.example.hhs_sumup.models.Tag;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudiestofSchermController {
    public Text studiestof_naam;
    public Button auteur_account;
    public TextArea studiestof;
    public Button terug_naar_start;
    public Button tags;
    public ListView<String> tags_list;
    public Button feedback_plaatsen;
    public Button feedback_zien;
    public ListView<String> feedback_lijst;
    public TextArea feedback_tf_plaatsen;
    public Button feedback_plaatsen1;

    public void initialize() {
        tags_list.setVisible(false);
        feedback_lijst.setVisible(false);
        feedback_tf_plaatsen.setVisible(false);
        feedback_plaatsen1.setVisible(false);
        feedback_plaatsen.setVisible(false);

        tags.setOnAction(event -> showTagsList());
        feedback_zien.setOnAction(event -> showFeedbackList());
        feedback_plaatsen.setOnAction(event -> showFeedbackTextField());
        terug_naar_start.setOnAction(event -> goToStartWindow());
        auteur_account.setOnAction(event -> goToAnderStudent());

        int studiestofId = Model.getInstance().getSelectedStudieStofId();
        Studiestof studiestof = getStudiestofFromDatabase(studiestofId);
        if (studiestof != null) {
            studiestof_naam.setText(studiestof.getNaam());
            this.studiestof.setText(studiestof.getInhoud());
            List<Tag> tags = getTagsForStudiestof(studiestofId);
            for (Tag tag : tags) {
                tags_list.getItems().add(tag.getT_naam());
            }
        }
        setAuteurAccountText(studiestof.getAuteur_id());

    }

    private Studiestof getStudiestofFromDatabase(int studiestofId) {
        String query = "SELECT studiestof_id, ss_titel, ss_inhoud, ss_auteur_id FROM studiestof WHERE studiestof_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studiestofId);
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

    private List<Tag> getTagsForStudiestof(int studiestofId) {
        String query = "SELECT t.tag_id, t.t_naam FROM tag t JOIN studiestoftag st ON t.tag_id = st.sst_tag_id WHERE st.sst_studiestof_id = ?";
        List<Tag> tags = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studiestofId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("tag_id");
                String naam = resultSet.getString("t_naam");
                tags.add(new Tag(id, naam));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

    public void showTagsList() {
        tags_list.setVisible(!tags_list.isVisible());
    }

    public void showFeedbackList() {
        feedback_lijst.setVisible(!feedback_lijst.isVisible());
        feedback_plaatsen.setVisible(!feedback_plaatsen.isVisible());
        if (feedback_tf_plaatsen.isVisible()) {
            feedback_tf_plaatsen.setVisible(false);
            feedback_plaatsen1.setVisible(false);
        }
    }

    public void showFeedbackTextField() {
        feedback_tf_plaatsen.setVisible(!feedback_tf_plaatsen.isVisible());
        feedback_plaatsen1.setVisible(!feedback_plaatsen1.isVisible());
    }

    private void setAuteurAccountText(int auteurId) {
        String query = "SELECT s_naam FROM student WHERE student_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, auteurId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String auteurNaam = resultSet.getString("s_naam");
                auteur_account.setText(auteurNaam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void goToStartWindow() {
        Stage stage = (Stage) studiestof_naam.getScene().getWindow();
        Model.getInstance().getViewFactory().closeWindow(stage);
        Model.getInstance().getViewFactory().showStartWindow();
    }

    public void goToAnderStudent() {
        Stage stage = (Stage) studiestof_naam.getScene().getWindow();
        Model.getInstance().getViewFactory().closeWindow(stage);
        Model.getInstance().getViewFactory().showAndereStudentWindow();
    }
}