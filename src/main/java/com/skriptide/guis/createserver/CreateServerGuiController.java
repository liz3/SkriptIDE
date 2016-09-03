package com.skriptide.guis.createserver;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.skriptide.util.Config;
import com.skriptide.util.MCServer;
import com.skriptide.util.ServerVersion;
import com.skriptide.util.Skript;

import java.io.File;

/**
 * Created by Liz3ga on 28.07.2016.
 */
public class CreateServerGuiController {

    @FXML
    private TextField serverNameTextField;
    @FXML
    private TextField serverPortTextField;
    @FXML
    private TextField serverPathTextField;
    @FXML
    private ComboBox<String> serverVersionComboBox;
    @FXML
    private Button customServerVersionBtn;
    @FXML
    private ComboBox<String> scriptVersionComboBox;
    @FXML
    private Button customScriptPluginVersionBtn;
    @FXML
    private CheckBox allowPvPCheckBox;
    @FXML
    private CheckBox spawnMonstersCheckBox;
    @FXML
    private CheckBox spawnAnimalsCheckBox;
    @FXML
    private CheckBox spawnNPCSCheckBox;
    @FXML
    private CheckBox spawnBuildingCheckBox;
    @FXML
    private TextField maxPlayersTextField;
    @FXML
    private TextField modtTextField;
    @FXML
    private ComboBox mainGamemodeComboBox;
    @FXML
    private TextArea notesTextArea;
    @FXML
    private CheckBox createSubFolderCheckBox;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button createServerBtn;
    File serverCustomFile = null;
    File scriptPluginVersionFile = null;

    private String truePath;

    public void updatePath() {

        serverPathTextField.setText(truePath + "/" + serverNameTextField.getText());


    }

    public void setValues() {

        truePath = Config.getServersPath().substring(0, Config.getServersPath().length() -1);;
        serverPathTextField.setText(Config.getServersPath());
        scriptVersionComboBox.getItems().clear();
        serverVersionComboBox.getItems().clear();

        ObservableList<Skript> skVersions = Skript.getSkriptVersions();
        ObservableList<ServerVersion> srvVersions = ServerVersion.getServerVersions();

        for (Skript sk : skVersions.sorted()) {

            scriptVersionComboBox.getItems().add(sk.getVersion());
        }

        for (ServerVersion srv : srvVersions.sorted()) {
            serverVersionComboBox.getItems().add(srv.getVersion());
        }


    }

    public void createServer() {

        boolean canGo = true;
        String error = "";


        ObservableList<Skript> skVersions = Skript.getSkriptVersions();
        ObservableList<ServerVersion> srvVersions = ServerVersion.getServerVersions();

        Skript trueSkript = null;
        ServerVersion trueVer = null;
        for (Skript sk : skVersions.sorted()) {

            String selection = scriptVersionComboBox.getSelectionModel().getSelectedItem();
            if(selection == null || selection == "") {
                canGo = false;
                error = error + "Please choose a Skript Version!";
            }
            if (sk.getVersion().equals(selection)) {
                trueSkript = sk;
            }

        }

        for (ServerVersion srv : srvVersions.sorted()) {

            String selection = serverVersionComboBox.getSelectionModel().getSelectedItem();
            if(selection == null || selection == "") {
                canGo = false;
                error = error + "Please choose a Server Version!";
            }
            if (srv.getVersion().equals(selection)) {
                trueVer = srv;
            }
        }

        try {
            Long.parseLong(serverPortTextField.getText());
        }catch (NumberFormatException ex) {
            canGo = false;
            error = error + "Port has to be a number";

        }


      if(canGo) {
          MCServer server = new MCServer(serverNameTextField.getText(), Long.parseLong(serverPortTextField.getText()), serverPathTextField.getText(), trueVer, trueSkript);


          server.createServer();


          Stage stage = (Stage) createServerBtn.getScene().getWindow();
          // do what you have to do
          stage.close();

      }

    }


    public void cancel() {

        Stage stage = (Stage) createServerBtn.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    public void choosePath() {

        Stage fileChooserWindow = new Stage();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Choose Path for Projects");
        File dir = dirChooser.showDialog(fileChooserWindow);

        serverPathTextField.setText(dir.getAbsolutePath());

        truePath = dir.getAbsolutePath();
    }

    public void chooseServerVersion() {

        Stage fileChooserWindow = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooserWindow.setTitle("Choose Path for the server File");
        serverCustomFile = fileChooser.showOpenDialog(fileChooserWindow);

    }

    public void chooseScriptVersion() {

        Stage fileChooserWindow = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooserWindow.setTitle("Choose Path for the Plugin File");
        scriptPluginVersionFile = fileChooser.showOpenDialog(fileChooserWindow);
    }

}

