package com.skriptide.guis.createprojectgui;

import com.skriptide.guis.SceneManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import com.skriptide.util.*;


import java.io.File;

/**
 * Created by Liz3ga on 27.07.2016.
 */
public class CreateProjectGuiController {


    @FXML
    public TextField projectNameTxTField;

    @FXML
    public ComboBox<String> pluginVersionComboBox;

    @FXML
    public ComboBox<String> loadOnServerComboBox;

    @FXML
    public Button createServerBtn;

    @FXML
    public TextField savePathTxTField;

    @FXML
    public TextArea notesTextArea;

    @FXML
    public Button cancelBtn;

    @FXML
    public Button createBtn;

    @FXML
    public CheckBox createSubFolderCheckBox;


    private String truePath;

    public void updatePath() {

     savePathTxTField.setText(truePath + "/" + projectNameTxTField.getText());


    }
    public void createProject() {

        boolean canGo = true;
        String error = "";

        ObservableList<Project> getProjects = Project.getProjects();
        for(Project prj : getProjects.sorted()) {
            String name = prj.getName();
            if (name != null) {
                if (name.equalsIgnoreCase(projectNameTxTField.getText())) {
                    canGo = false;
                    error = error + "This Project Name allready exists!";
                }
            }
        }
        String selected = pluginVersionComboBox.getSelectionModel().getSelectedItem();
        if(selected == null) {
            canGo = false;
            error = error + "Please choose a Skript version!";
        }
        ObservableList<Skript> sk = Skript.getSkriptVersions();
        Skript tSk = null;
        for(int i = 0; i != sk.size(); i++) {

            Skript s = sk.get(i);
            if(s.getVersion().equals(selected)) {
                tSk = s;
            }
        }

        MCServer trueSRV = null;
        ObservableList<MCServer> mcServers = MCServer.getAllServers();
        for(MCServer srv : mcServers.sorted()) {
            String selection = loadOnServerComboBox.getSelectionModel().getSelectedItem();
            if(srv.getname().equalsIgnoreCase(selection)) {
                trueSRV = srv;
            }
        }

       if(canGo) {
           Project.createProject(projectNameTxTField.getText(), tSk, trueSRV, savePathTxTField.getText(), notesTextArea.getText());


           if(Config.checkConfig() == 0) {
               ObservableList<Project> projects = Project.getProjects();
               SceneManager.projectsList.getItems().clear();
               for (Project project : projects.sorted()) {
                   if (project.getName() != null) {
                       SceneManager.projectsList.getItems().addAll(project.getName());
                   }
               }
           }

           Stage stage = (Stage) createBtn.getScene().getWindow();
           // do what you have to do
           stage.close();
       }
    }

    public void cancel() {

        Stage stage = (Stage) createBtn.getScene().getWindow();
        // do what you have to do
        stage.close();

    }

    public void createNewServer() {

        SceneManager sceneManager = new SceneManager();
        sceneManager.openCreateServer();
    }

    public void setValues() {

        truePath = Config.getProjectsPath().substring(0, Config.getProjectsPath().length() -1);
        savePathTxTField.setText(Config.getProjectsPath());
        ObservableList<Skript> sk = Skript.getSkriptVersions();

        pluginVersionComboBox.getItems().clear();
       for(Skript skript : sk.sorted()) {

           Skript s = skript;

           pluginVersionComboBox.getItems().add(s.getVersion());
       }
        ObservableList<MCServer> servers = MCServer.getAllServers();
        loadOnServerComboBox.getItems().clear();

        for(MCServer server : servers.sorted()) {
            loadOnServerComboBox.getItems().add(server.getname());
        }




    }


    public void openProjectPathChooser() {

        Stage fileChooserWindow = new Stage();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Choose Path for Projects");
        File dir = dirChooser.showDialog(fileChooserWindow);

        savePathTxTField.setText(dir.getAbsolutePath());

        truePath = dir.getAbsolutePath();

        updatePath();


    }


}
