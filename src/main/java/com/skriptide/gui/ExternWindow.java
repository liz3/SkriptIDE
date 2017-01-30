package com.skriptide.gui;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.fxmisc.richtext.CodeArea;

import java.util.ArrayList;

/**
 * Created by yannh on 31.12.2016.
 */
public class ExternWindow {

    public static ArrayList<ExternWindow> windows = new ArrayList<>();

    public static ArrayList<ExternWindow> getWindows() {
        return windows;
    }

    public Tab getTab() {
        return tab;
    }

    public Stage getStage() {
        return stage;
    }

    public OpenProject getProject() {
        return project;
    }

    private Tab tab;
    private Stage stage;
    private OpenProject project;
    private CodeArea area;

    public ExternWindow(Tab tab, OpenProject project) {

        this.tab = tab;
        this.project = project;

        BorderPane pane = new BorderPane();
        pane.setCenter(project.getArea());
        stage = new Stage();

        stage.setWidth(600);
        stage.setHeight(500);
        stage.setScene(new Scene(pane));
        stage.setTitle("SkriptIDE " + project.getProject().getName());
        stage.centerOnScreen();
        stage.getScene().getStylesheets().add("/Highlighting.css");
        stage.show();




        windows.add(this);

        this.area = project.getArea();

        stage.setOnCloseRequest(event -> project.reAttach());

    }
    private void setShortCuts() {

        this.area.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {



            }
        });

    }
    public CodeArea getArea() {
        return area;
    }
}
