package com.example.hhs_sumup.controllers;

import com.example.hhs_sumup.Database.DatabaseConnection;
import com.example.hhs_sumup.InterfaceController;
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

public class StudiestofSchermController implements InterfaceController {
    public Text studiestof_naam;
    public Button auteur_account;
    public Button terug_naar_start;
    public Button tags;
    public ListView<String> tags_list;
    public Button feedback_plaatsen;
    public Button feedback_zien;
    public ListView<String> feedback_lijst;
    public TextArea feedback_tf_plaatsen;
    public Button feedback_plaatsen1;
    public TextArea tf_studiestof;

    private Studiestof studiestof;

    public void initialize() {
        tags_list.setVisible(false);
        feedback_lijst.setVisible(false);
        feedback_tf_plaatsen.setVisible(false);
        feedback_plaatsen1.setVisible(false);
        feedback_plaatsen.setVisible(false);

        tags.setOnAction(event -> toggleVisibility(tags_list));
        feedback_zien.setOnAction(event -> toggleFeedbackVisibility());
        feedback_plaatsen.setOnAction(event -> toggleVisibility(feedback_tf_plaatsen, feedback_plaatsen1));
        terug_naar_start.setOnAction(event -> goToStartWindow());

        int studiestofId = Model.getInstance().getSelectedStudieStofId();
        this.studiestof = getStudiestofFromDatabase(studiestofId);
        if (this.studiestof != null) {
            updateUIWithStudiestofData();
        }
    }

    private Studiestof getStudiestofFromDatabase(int studiestofId) {
        String query = "SELECT studiestof_id, ss_titel, ss_inhoud, ss_auteur_id FROM studiestof WHERE studiestof_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studiestofId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Studiestof(
                    resultSet.getInt("studiestof_id"),
                    resultSet.getString("ss_titel"),
                    resultSet.getString("ss_inhoud"),
                    resultSet.getInt("ss_auteur_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Tag> getTagsForStudiestof(int studiestofId) {
        String query = "SELECT t.t_naam FROM tag t JOIN studiestoftag st ON t.tag_id = st.sst_tag_id WHERE st.sst_studiestof_id = ?";
        List<Tag> tags = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studiestofId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tags.add(new Tag(0, resultSet.getString("t_naam")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

    public void setStudiestof(Studiestof studiestof) {
        this.studiestof = studiestof;
        if (studiestof != null) {
            updateUIWithStudiestofData();
        }
    }

    private void updateUIWithStudiestofData() {
        studiestof_naam.setText(studiestof.getNaam());
        tf_studiestof.setText(studiestof.getInhoud());
        setAuteurAccountText(studiestof.getAuteur_id());
        tags_list.getItems().clear();
        for (Tag tag : getTagsForStudiestof(studiestof.getSs_id())) {
            tags_list.getItems().add(tag.getT_naam());
        }
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
                auteur_account.setOnAction(event -> goToAnderStudent(auteurId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void toggleVisibility(ListView<String> listView) {
        listView.setVisible(!listView.isVisible());
    }

    private void toggleVisibility(TextArea textArea, Button button) {
        textArea.setVisible(!textArea.isVisible());
        button.setVisible(!button.isVisible());
    }

    private void toggleFeedbackVisibility() {
        feedback_lijst.setVisible(!feedback_lijst.isVisible());
        feedback_plaatsen.setVisible(!feedback_plaatsen.isVisible());
        if (feedback_tf_plaatsen.isVisible()) {
            feedback_tf_plaatsen.setVisible(false);
            feedback_plaatsen1.setVisible(false);
        }
    }

    public void goToStartWindow() {
        Stage stage = (Stage) studiestof_naam.getScene().getWindow();
        Model.getInstance().getViewFactory().closeWindow(stage);
        Model.getInstance().getViewFactory().showStartWindow();
    }

    public void goToAnderStudent(int auteurId) {
        Stage stage = (Stage) studiestof_naam.getScene().getWindow();
        Model.getInstance().getViewFactory().closeWindow(stage);
        Model.getInstance().getViewFactory().showAndereStudentWindow(auteurId);
    }
}