package com.example.hhs_sumup.models;

import com.example.hhs_sumup.Database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Studiestof {
    private int ss_id;
    private String naam;
    private String inhoud;
    private int auteur_id;

    public Studiestof(int ss_id, String naam, String inhoud, int auteur_id) {
        this.ss_id = ss_id;
        this.naam = naam;
        this.inhoud = inhoud;
        this.auteur_id = auteur_id;
    }

    public Studiestof() {}

    public int getSs_id() {
        return ss_id;
    }

    public void setSs_id(int ss_id) {
        this.ss_id = ss_id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getInhoud() {
        return inhoud;
    }

    public void setInhoud(String inhoud) {
        this.inhoud = inhoud;
    }

    public int getAuteur_id() {
        return auteur_id;
    }

    public static Studiestof fetchByTitle(String title) {
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