package com.example.hhs_sumup.models;

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

    public Studiestof() {
    }

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

    public void setAuteur_id(int auteur_id) {
        this.auteur_id = auteur_id;
    }
}