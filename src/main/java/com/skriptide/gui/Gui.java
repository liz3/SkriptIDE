package com.skriptide.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Created by yannh on 27.01.2017.
 */
public class Gui {

    private Parent parent;
    private Stage stage;
    private GuiType type;
    private FXMLLoader loader;
    private Object controller;

    public Gui(Stage stage, GuiType type, FXMLLoader loader, Object controller, Parent parent) {
        this.stage = stage;
        this.type = type;
        this.loader = loader;
        this.controller = controller;
        this.parent = parent;
    }

    public Stage getStage() {
        return stage;
    }

    public GuiType getType() {
        return type;
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    @SuppressWarnings("unchecked")
    public <T> T getController() {
        return (T)controller;
    }

    public Parent getParent() {
        return parent;
    }
}
