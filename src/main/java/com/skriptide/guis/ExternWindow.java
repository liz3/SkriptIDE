package com.skriptide.guis;

import com.skriptide.util.Project;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
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

    public Project getProject() {
        return project;
    }

    private Tab tab;
    private Stage stage;
    private Project project;
    private CodeArea area;

    public ExternWindow(Tab tab, Project project) {

        this.tab = tab;
        this.project = project;

        BorderPane pane = new BorderPane();
        CodeArea area = (CodeArea) tab.getContent();
        String text = area.getText();
        pane.setCenter(area);
        stage = new Stage();

        stage.setWidth(600);
        stage.setHeight(500);
        stage.setScene(new Scene(pane));
        stage.setTitle("SkriptIDE " + project.getName());
        stage.centerOnScreen();
        stage.getScene().getStylesheets().add("/Highlighting.css");
        stage.show();


        stage.setOnCloseRequest(event -> SceneManager.mainWindowController.reAttach(ExternWindow.this));

        windows.add(this);

        this.area = area;


    }

    public CodeArea getArea() {
        return area;
    }
}
