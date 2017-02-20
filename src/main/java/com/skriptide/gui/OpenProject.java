package com.skriptide.gui;

import com.skriptide.include.Project;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.fxmisc.richtext.CodeArea;

import java.util.HashMap;

/**
 * Created by liz3 on 31.01.17.
 */
public class OpenProject {

    private Project project;
    private HashMap<String, OpenFile> openFiles;

    private TabPane tabPane;
    private Button cmdSendBtn;
    private ListView<String> dependecieList;

    public Project getProject() {
        return project;
    }

    public HashMap<String, OpenFile> getOpenFiles() {
        return openFiles;
    }

    public OpenProject(Project project,  TabPane tabPane, Button cmdSendBtn, ListView<String> dependecieList) {
        this.openFiles = new HashMap<>();
        this.project = project;
        this.tabPane = tabPane;
        this.cmdSendBtn = cmdSendBtn;
        this.dependecieList = dependecieList;
    }

    public void close(String name) {

        System.out.println("casted");
        System.out.println(name);
        if(openFiles.containsKey(name)) {
            System.out.println("Removed");
            openFiles.remove(name);
        }
    }
    public void openFile(String name) {

        System.out.println(name);
        if(!openFiles.containsKey(name)) {

            Tab tab = new Tab(name);
            CodeArea area = new CodeArea();
            area.appendText(project.getCurentCode(name));
            tab.setContent(area);
            OpenFile of = new OpenFile(tab, project.getSkFiles().get(name), area, dependecieList, tabPane, cmdSendBtn, this);
            tabPane.getTabs().add(tab);
            openFiles.put(name, of);
        }
    }
}
