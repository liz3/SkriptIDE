package com.skriptide.guis.startgui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import com.skriptide.util.Config;
import com.skriptide.util.skunityapi.SkUnityAPI;

import java.io.File;

/**
 * Created by Liz3ga on 27.07.2016.
 */
public class StartGuiController {

    @FXML
    public TextField projectsPathField;
    @FXML
    public TextField serverPathField;
    @FXML
    public ComboBox langComboBox;
    @FXML
    public Button okBtn;
    @FXML
    public Button projectsPathBtn;
    @FXML
    public Button serverPathBtn;


    public void cancelAction() {

        System.exit(0);

    }

    public void openProjectPathChooser() {

        Stage fileChooserWindow = new Stage();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Choose Path for Projects");
        File dir = dirChooser.showDialog(fileChooserWindow);
        if (dir == null){
            //They didn't select a file, they hit cancel
            return;
        }

        projectsPathField.setText(dir.getAbsolutePath());


    }

    public void openServersPathChooser() {

        Stage fileChooserWindow = new Stage();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Choose Path for Servers");
        File dir = dirChooser.showDialog(fileChooserWindow);
        if (dir == null){
            //They didn't select a file, they hit cancel
            return;
        }
        serverPathField.setText(dir.getAbsolutePath());


    }


    public void proceed() {


        String projectsPath = projectsPathField.getText().replace("\\", "/");
        String serversPath = serverPathField.getText().replace("\\", "/");
        String lang = langComboBox.getId();


        Config.createConfig(projectsPath, serversPath, lang);

        Stage stage = (Stage) okBtn.getScene().getWindow();

        stage.close();

        Thread t = new Thread(() -> {

            SkUnityAPI skUnityAPI = new SkUnityAPI();


        });
        t.start();


    }


}
