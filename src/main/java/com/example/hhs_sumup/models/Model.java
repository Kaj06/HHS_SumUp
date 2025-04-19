package com.example.hhs_sumup.models;

import com.example.hhs_sumup.views.ViewFactory;

public class Model {
    private static Model instance;
    private ViewFactory viewFactory;
    private Student loggedInUser;
    private int selectedStudieStofId;

    private Model() {
        viewFactory = new ViewFactory();
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    public int getSelectedStudieStofId() {
        return selectedStudieStofId;
    }

    public void setSelectedStudieStofId(int selectedStudieStofId) {
        this.selectedStudieStofId = selectedStudieStofId;
    }

    public Student getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(Student loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }
}