package com.example.hhs_sumup.models;

import com.example.hhs_sumup.views.ViewFactory;

public class Model {
    private static Model instance;
    private ViewFactory viewFactory;

    private Model() {
        viewFactory = new ViewFactory();
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }
}