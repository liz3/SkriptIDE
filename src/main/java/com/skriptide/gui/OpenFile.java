package com.skriptide.gui;

import com.skriptide.Main;
import com.skriptide.codemanage.AddonDepenencies;
import com.skriptide.codemanage.AutoComplete;
import com.skriptide.codemanage.CompleteList;
import com.skriptide.codemanage.ControlMain;
import com.skriptide.util.skunityapi.SkUnityAPI;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.fxmisc.richtext.CodeArea;

import java.io.File;

/**
 * Created by yannh on 27.01.2017.
 */
public class OpenFile {

    private Tab tab;
    private File project;
    private CodeArea area;
    private OpenProject openProject;
    private AutoComplete complete;
    private ControlMain controlMain;
    private CompleteList completeList;
    private AddonDepenencies depenencies;
    private TabPane tabPane;
    private Button cmdSendBtn;
    private ListView<String> dependecieList;
    private ExternWindow externWindow;

    public void setTab(Tab tab) {
        this.tab = tab;
    }

    public OpenFile(Tab tab, File project, CodeArea area, ListView<String> dependecieList, TabPane tabPane, Button cmdSendBtn, OpenProject openProject) {
        externWindow = null;
        this.tab = tab;
        this.project = project;
        this.area = area;
        this.dependecieList = dependecieList;
        this.tabPane = tabPane;
        this.cmdSendBtn = cmdSendBtn;
        this.openProject = openProject;
        controlMain = new ControlMain();
        completeList = new CompleteList();
        depenencies = new AddonDepenencies(area, new SkUnityAPI(), dependecieList);
        complete = new AutoComplete();
        controlMain.controlCode(area, tab, this, true);


        tab.setOnCloseRequest(event -> {

            this.openProject.close(this.project.getName());

            boolean toSave = new SceneManager().infoCheck("Save project?", tab.getText(), "Save the project: " + tab.getText());
            if(toSave) {

                this.openProject.getProject().writeCode(this.area.getText(), this.project.getName());

            }

        });

    }


    public void toExtern() {

        externWindow = new ExternWindow(this.tab, this);

    }
    public void reAttach() {

        if(externWindow == null) {
            return;
        }
        setArea(externWindow.getArea());
        externWindow.getStage().close();
        tabPane.getTabs().add(tab);
    }
    public void resetHighlighting() {

        controlMain = new ControlMain();
        completeList = new CompleteList();
        depenencies = new AddonDepenencies(area, new SkUnityAPI(), dependecieList);
        complete = new AutoComplete();
        controlMain.controlCode(area, tab, this, true);
    }
    public void setAutoComplete() {
        complete.setAutoComplete(area, completeList, tabPane, cmdSendBtn, depenencies);

    }
    public Tab getTab() {
        return tab;
    }

    public File getProject() {
        return project;
    }

    public CodeArea getArea() {
        return area;
    }

    public void setArea(CodeArea area) {
        this.area = area;
    }


}
