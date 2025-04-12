package com.example.hhs_sumup.controllers;

import com.example.hhs_sumup.Database.DatabaseConnection;
import com.example.hhs_sumup.models.Model;
import com.example.hhs_sumup.models.Studiestof;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class StudiestofMakenSchermController {
    public TextArea studiestof;
    public Button terug_naar_start, tags, voeg_tag_toe, plaats_studiestof;
    public ListView<String> tags_list;
    public TextField studiestof_naam, tf_tag_toevoegen;

    private Studiestof currentStudiestof;
    private final Set<String> selectedTags = new HashSet<>();

    public void initialize() {
        tags.setOnAction(event -> toggleTagsListVisibility());
        terug_naar_start.setOnAction(event -> goToStartWindow());
        voeg_tag_toe.setOnAction(event -> addTagToDatabase());
        plaats_studiestof.setOnAction(event -> saveStudiestofAndTagsToDatabase());

        ObservableList<String> tags = FXCollections.observableArrayList(getAllTagsFromDatabase());
        tags_list.setItems(tags);
        setupTagsListCellFactory();

        tags_list.setVisible(false);
        voeg_tag_toe.setVisible(false);
        tf_tag_toevoegen.setVisible(false);

        currentStudiestof = new Studiestof();
    }

    private void toggleTagsListVisibility() {
        boolean isVisible = tags_list.isVisible();
        tags_list.setVisible(!isVisible);
        voeg_tag_toe.setVisible(!isVisible);
        tf_tag_toevoegen.setVisible(!isVisible);
    }

    private void setupTagsListCellFactory() {
        tags_list.setCellFactory(listView -> new ListCell<>() {
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle(selectedTags.contains(item) ? "-fx-background-color: #4B5320; -fx-text-fill: white;" : "");
                    setOnMouseClicked(event -> {
                        if (selectedTags.contains(item)) selectedTags.remove(item);
                        else selectedTags.add(item);
                        tags_list.refresh();
                    });
                }
            }
        });
    }

    private void saveStudiestofAndTagsToDatabase() {
        if (currentStudiestof != null) {
            saveStudiestofToDatabase();
            if (isStudiestofSaved()) {
                saveTagsToDatabase();
            }
        }
    }

    private void saveStudiestofToDatabase() {
        String query = "INSERT INTO studiestof (ss_titel, ss_inhoud, ss_auteur_id, ss_datum) VALUES (?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE ss_titel = VALUES(ss_titel), ss_inhoud = VALUES(ss_inhoud), ss_auteur_id = VALUES(ss_auteur_id), ss_datum = VALUES(ss_datum)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            currentStudiestof.setNaam(studiestof_naam.getText().trim());
            currentStudiestof.setInhoud(studiestof.getText().trim());
            statement.setString(1, currentStudiestof.getNaam());
            statement.setString(2, currentStudiestof.getInhoud());
            statement.setInt(3, Model.getInstance().getLoggedInUser().getStudent_id());
            statement.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    currentStudiestof.setSs_id(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isStudiestofSaved() {
        String query = "SELECT COUNT(*) FROM studiestof WHERE studiestof_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, currentStudiestof.getSs_id());
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveTagsToDatabase() {
        if (!selectedTags.isEmpty()) {
            String query = "INSERT INTO studiestoftag (sst_studiestof_id, sst_tag_id) VALUES (?, ?)";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                for (String tag : selectedTags) {
                    int tagId = getTagIdFromDatabase(tag);
                    if (tagId != -1) {
                        statement.setInt(1, currentStudiestof.getSs_id());
                        statement.setInt(2, tagId);
                        statement.addBatch();
                    }
                }
                statement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void addTagToDatabase() {
        String tag = tf_tag_toevoegen.getText().trim();
        if (!tag.isEmpty()) {
            String query = "INSERT INTO tag (t_naam) VALUES (?)";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, tag);
                statement.executeUpdate();

                tags_list.getItems().add(tag);

                tf_tag_toevoegen.clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private int getTagIdFromDatabase(String tagName) {
        String query = "SELECT tag_id FROM tag WHERE t_naam = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, tagName);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("tag_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private ObservableList<String> getAllTagsFromDatabase() {
        ObservableList<String> tags = FXCollections.observableArrayList();
        String query = "SELECT t_naam FROM tag";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                tags.add(resultSet.getString("t_naam"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

    private void goToStartWindow() {
        Stage stage = (Stage) studiestof.getScene().getWindow();
        Model.getInstance().getViewFactory().closeWindow(stage);
        Model.getInstance().getViewFactory().showStartWindow();
    }
}