package com.skriptide.gui;

import com.skriptide.Main;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
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

    public OpenFile getProject() {
        return project;
    }

    private Tab tab;
    private Stage stage;
    private OpenFile project;
    private CodeArea area;

    public ExternWindow(Tab tab, OpenFile project) {

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
        setTheme(stage.getScene());
        stage.show();




        windows.add(this);

        this.area = project.getArea();

        stage.setOnCloseRequest(event -> project.reAttach());

    }
    private void setTheme(Scene scene) {

        if(Main.settings.getTheme() == 1) {

            System.out.println("Adding dark style");
            scene.getStylesheets().add("ThemeDark.css");
            scene.getStylesheets().add("DarkHighlighting.css");

        } else {

            scene.getStylesheets().add("Highlighting.css");
        }
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
