package com.example.hhs_sumup.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class StudiestofSchermController {
    public Text studiestof_naam;
    public Button auteur_account;
    public TextArea studiestof;
    public Button terug_naar_start;
    public Button tags;
    public ListView tags_list;
    public Button feedback_plaatsen;
    public Button feedback_zien;
    public ListView feedback_lijst;
    public TextArea feedback_tf_plaatsen;
    public Button feedback_plaatsen1;

    public void initialize() {
        tags_list.setVisible(false);
        feedback_lijst.setVisible(false);
        feedback_tf_plaatsen.setVisible(false);
        feedback_plaatsen1.setVisible(false);

    }

    public void showTagsList() {
        tags_list.setVisible(!tags_list.isVisible());
    }

    public void showFeedbackList() {
        feedback_lijst.setVisible(!feedback_lijst.isVisible());
        feedback_tf_plaatsen.setVisible(!feedback_tf_plaatsen.isVisible());
        feedback_plaatsen1.setVisible(!feedback_plaatsen1.isVisible());
    }

    public void showFeedbackTextField() {
        feedback_tf_plaatsen.setVisible(!feedback_tf_plaatsen.isVisible());
        feedback_plaatsen1.setVisible(!feedback_plaatsen1.isVisible());
    }

}
