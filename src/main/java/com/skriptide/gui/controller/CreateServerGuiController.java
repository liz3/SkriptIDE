package com.skriptide.gui.controller;

import com.skriptide.Main;
import com.skriptide.include.Api;
import com.skriptide.include.Server;
import com.skriptide.include.Skript;
import com.skriptide.util.VersionReader;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by yannh on 04.02.2017.
 */
public class CreateServerGuiController {

    private File serverCustomFile = null;
    private File scriptPluginVersionFile = null;
    @FXML
    private TextField serverNameTextField;
    @FXML
    private TextField serverPortTextField;
    @FXML
    private ComboBox<String> serverVersionComboBox;
    @FXML
    private ComboBox<String> scriptVersionComboBox;
    @FXML
    private Button customServerVersionBtn;
    @FXML
    private Button customScriptPluginVersionBtn;
    @FXML
    private TextField serverPathTextField;
    @FXML
    private Button createServerBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private TextArea notesTextArea;
    @FXML
    private Button choosePathBtn;
    @FXML
    private TextField startParameterTextField;
    @FXML
    private Label infoLabel;

    private String path = "";

    public void initGui() {

        serverVersionComboBox.getItems().clear();
        for(Api api : Main.manager.getApis().values()) {

            serverVersionComboBox.getItems().add(api.getName() + " - " + api.getVersion());
        }
        scriptVersionComboBox.getItems().clear();

        for(Skript skript : Main.manager.getSkripts().values()) {

            scriptVersionComboBox.getItems().add(skript.getName() + " - " + skript.getVersion());
        }
        serverNameTextField.setOnKeyReleased(event -> updatePath());
        choosePathBtn.setOnAction(event -> choosePath());
        customServerVersionBtn.setOnAction(event -> chooseServerVersion());
        customScriptPluginVersionBtn.setOnAction(event -> chooseScriptVersion());
        path = Main.manager.getServersPath();
        cancelBtn.setOnAction(event -> {
            Stage s = (Stage) cancelBtn.getScene().getWindow();
            s.close();

        });

        createServerBtn.setOnAction(event -> {

            String name = serverNameTextField.getText();
            String path = serverPathTextField.getText();
            String args = startParameterTextField.getText();
            String api = serverVersionComboBox.getSelectionModel().getSelectedItem();
            String skript = scriptVersionComboBox.getSelectionModel().getSelectedItem();
            if(serverPortTextField.getText().equals("")) {
                return;
            }
            int port = Integer.parseInt(serverPortTextField.getText());

            Api apii = null;
            Skript sk = null;

            for(Api a : Main.manager.getApis().values()) {
                String s = a.getName() + " - " + a.getVersion();
                if(s.equals(api)) {
                    apii = a;
                }
            }
            for(Skript a : Main.manager.getSkripts().values()) {
                String s = a.getName() + " - " + a.getVersion();
                if(s.equals(skript)) {
                    sk = a;
                }
            }
            if(apii == null || sk == null) {
                return;
            }
            System.out.println("lol");
            for(String str : Main.manager.getServer().keySet()) {
                if(str.equalsIgnoreCase(name)) {
                    return;
                }
            }
            if(name.equalsIgnoreCase("")) {
                return;
            }

            File folder = new File(path);


            Server server = new Server(name, apii, sk, folder, args, port);
            Main.manager.addServer(server);

            server.createServer();

            IdeGuiController.controller.getServerListComboBox().getItems().add(server.getName());

            scriptVersionComboBox.getScene().getWindow().hide();
            if(Main.settings.isStartAfterCreate() && Main.runningServer == null) {

                IdeGuiController.controller.getServerListComboBox().getSelectionModel().select(server.getName());
                server.startServer();
            } else {

                if(Main.sceneManager.infoCheck("Start","Start server", "Should SkriptIDE start the Server?", Alert.AlertType.CONFIRMATION)) {

                    IdeGuiController.controller.getServerListComboBox().getSelectionModel().select(server.getName());
                    server.startServer();
                }
            }
        });
    }

    private void chooseServerVersion() {

        Stage fileChooserWindow = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooserWindow.setTitle("Choose Path for the server File");
        serverCustomFile = fileChooser.showOpenDialog(fileChooserWindow);

        if(serverCustomFile == null) {
            return;
        }
        String version = null;
        try {
            version = VersionReader.getVersionOfServer(serverCustomFile);
        }catch (Exception e) {
            Main.sceneManager.infoCheck("Error", "Failed to read Version",
                    "The read of the Version was not successful, please make sure its a valid bukkit or spigot version!", Alert.AlertType.ERROR);
            return;
        }


        Api newApi = new Api(serverCustomFile.getName().replace(".", "-"), version, serverCustomFile.getPath());
        Main.manager.addApi(newApi);

        serverVersionComboBox.getItems().clear();

        for(Api api : Main.manager.getApis().values()) {

            serverVersionComboBox.getItems().add(api.getName() + " - " + api.getVersion());
        }
        serverVersionComboBox.getSelectionModel().select(newApi.getName() + " - " + newApi.getVersion());
        if(Main.debugMode) {

            System.out.println("Set custom server version");
        }

    }

    private void chooseScriptVersion() {

        Stage fileChooserWindow = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooserWindow.setTitle("Choose Path for the Plugin File");
        scriptPluginVersionFile = fileChooser.showOpenDialog(fileChooserWindow);
        if(scriptPluginVersionFile == null) {
            return;
        }


        String version;

        try {
            version = VersionReader.getVersionOfPlugin(scriptPluginVersionFile);
        }catch (Exception e) {
            Main.sceneManager.infoCheck("Error", "Failed to read Version",
                    "The read of the Version was not successful, please make sure its a valid plugin with a plugin.yml", Alert.AlertType.ERROR);
            return;
        }

        if(version == null) {

            Main.sceneManager.infoCheck("Error", "Failed to read Version",
                    "The read of the Version was not successful, please make sure its a valid plugin with a plugin.yml", Alert.AlertType.ERROR);
            return;
        }

        Skript newSkript = new Skript(scriptPluginVersionFile.getName().replace(".", "-"), version, scriptPluginVersionFile.getPath());
        Main.manager.addSkript(newSkript);

        scriptVersionComboBox.getItems().clear();

        for(Skript skript : Main.manager.getSkripts().values()) {

            scriptVersionComboBox.getItems().add(skript.getName() + " - " + skript.getVersion());
        }
        scriptVersionComboBox.getSelectionModel().select(newSkript.getName() + " - " + newSkript.getVersion());
        if(Main.debugMode) {

            System.out.println("custom skript version");
        }
    }
    private void choosePath() {

        Stage fileChooserWindow = new Stage();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Choose Path for Projects");
        File dir = dirChooser.showDialog(fileChooserWindow);

        serverPathTextField.setText(dir.getAbsolutePath());

        path = dir.getAbsolutePath();
        if(Main.debugMode) {
            System.out.println("changed the save path");
        }
    }
    private void updatePath() {

        serverPathTextField.setText(path + "/" + serverNameTextField.getText());


    }
}
