package com.skriptide.gui.controller;

import com.skriptide.Main;
import com.skriptide.include.Project;
import com.skriptide.include.Skript;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;


public class CreateProjectGuiController {
    @FXML
    private TextField projectNameTxTField;
    @FXML
    private ComboBox<String> pluginVersionComboBox;
    @FXML
    private ComboBox<String> loadOnServerComboBox;
    @FXML
    private Button createServerBtn;
    @FXML
    private TextField savePathTxTField;
    @FXML
    private Button chooseSavePathBtn;
    @FXML
    private Button createBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private TextArea notesTextArea;

    private String truePath = Main.manager.getProjectsPath();

    public void initGui() {


        cancelBtn.setOnAction(event -> {

            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        });
        pluginVersionComboBox.getItems().clear();
        for(Skript sk : Main.manager.getSkripts().values()){

            pluginVersionComboBox.getItems().add(sk.getName() + " - " + sk.getVersion());
        }
        projectNameTxTField.setOnKeyReleased(event -> updatePath());
        savePathTxTField.setText(truePath);

        chooseSavePathBtn.setOnAction(event -> choosePath());
        createBtn.setOnAction(event -> {


            String name = projectNameTxTField.getText();
            String path = savePathTxTField.getText();
            File dir = new File(path, name);

            if(dir.exists()) {
                return;
            }
            String current = pluginVersionComboBox.getSelectionModel().getSelectedItem();
            Skript s = null;
            for(Skript sk : Main.manager.getSkripts().values()) {

                String b = sk.getName() + " - " + sk.getVersion();
                if(b.equals(current)) {
                    s = sk;
                }
            }
            Project project = new Project(name, path, s);
            if(loadOnServerComboBox.getSelectionModel().getSelectedItem() != null)  {
                project.setServer(Main.manager.getServer().get(loadOnServerComboBox.getSelectionModel().getSelectedItem()));
            }
            if(!project.exsits()) {
                project.createProject();
            }
            IdeGuiController.controller.addProject(project);
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        });

    }
    private void updatePath() {

        savePathTxTField.setText(truePath + "/" + projectNameTxTField.getText());


    }
    private void choosePath() {



        Stage fileChooserWindow = new Stage();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Choose save path for the Project");
        File dir = dirChooser.showDialog(fileChooserWindow);

        if (dir == null)
            return;

        savePathTxTField.setText(dir.getAbsolutePath());

        truePath = dir.getAbsolutePath();


        if (Main.debugMode) {
            System.out.println("Project save path changed");
        }


    }
}
