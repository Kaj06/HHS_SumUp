package com.example.hhs_sumup.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class StartSchermController {
    public Button account_knop;
    public Button notificaties_knop;
    public Label welkom_tekst;
    public TextField zoek_balk;
    public ListView notificaties_lijst;
    public ListView zoek_lijst;

    public void initialize() {
        notificaties_lijst.setVisible(false);
        zoek_lijst.setVisible(false);


    }

    public void zoek_lijst() {
        zoek_lijst.setVisible(true);
        notificaties_lijst.setVisible(false);
    }
}

