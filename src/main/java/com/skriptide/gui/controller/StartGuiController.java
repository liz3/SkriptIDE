package com.skriptide.gui.controller;

import com.skriptide.Main;
import com.skriptide.util.systemutils.OperatingSystemType;
import com.skriptide.util.systemutils.OsUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Created by yannh on 27.01.2017.
 */
public class StartGuiController {

    @FXML
    private Button okBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private TextField projectsPathField;
    @FXML
    private Button projectsPathBtn;
    @FXML
    private Button serverPathBtn;
    @FXML
    private TextField serverPathField;
    @FXML
    private ComboBox<String> langComboBox;


    public void initGui() {

        okBtn.setOnAction(event -> proceed());
        cancelBtn.setOnAction(event -> cancelAction());
        serverPathBtn.setOnAction(event -> openServersPathChooser());
        projectsPathBtn.setOnAction(event -> openProjectPathChooser());
        langComboBox.getItems().add("English");
        langComboBox.getSelectionModel().select(0);

        String username = System.getProperty("user.name");

        if(OsUtils.getOS() == OperatingSystemType.WINDOWS) {

            projectsPathField.setText("C:\\Users\\" + username + "\\SkriptIDE-Projects");
            serverPathField.setText("C:\\Users\\" + username + "\\SkriptIDE-Servers");
        }
        if(OsUtils.getOS() == OperatingSystemType.LINUX) {

            projectsPathField.setText("/home/" + username + "/ScriptIDE/Projects/");
            serverPathField.setText("/home/" + username + "/ScriptIDE/Servers/");
        }

    }

    private void cancelAction() {

        System.exit(0);

    }

    private void openProjectPathChooser() {

        Stage fileChooserWindow = new Stage();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Choose Path for Projects");
        File dir = dirChooser.showDialog(fileChooserWindow);
        if (dir == null) {
            //They didn't select a file, they hit cancel
            return;
        }

        projectsPathField.setText(dir.getAbsolutePath());
        if(Main.debugMode) {
            System.out.println("changed projects path");
        }

    }

    private void openServersPathChooser() {

        Stage fileChooserWindow = new Stage();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Choose Path for Servers");
        File dir = dirChooser.showDialog(fileChooserWindow);
        if (dir == null) {
            //They didn't select a file, they hit cancel
            return;
        }
        serverPathField.setText(dir.getAbsolutePath());
        if(Main.debugMode) {

            System.out.println("changed servers path");
        }

    }

    private void proceed() {

        File projectsPath = new File( projectsPathField.getText());
        File serversPath = new File( serverPathField.getText());



        Main.manager.setProjectsPath(projectsPath.getAbsolutePath());
        Main.manager.setServersPath(serversPath.getAbsolutePath());
        Main.manager.setConfigured();
        try {
            Main.manager.createFolders();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) okBtn.getScene().getWindow();

        stage.close();

        Main.sceneManager.infoCheck("Info", "Please read exactly", "Hey, to support the Development of SkriptIDE, " +
                "errors are reported to the SkriptIDE Api server. Trough this is a open-source Project. You can prove your self," +
                " we are only sharing your operating system name and the error." +
                "If you are sure you dont want that, please disable it in the config." +
                "Regards Liz3(SkriptIDE Author)", Alert.AlertType.INFORMATION);

    }
}
