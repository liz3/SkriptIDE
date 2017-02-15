package com.skriptide.gui.controller;

import com.skriptide.Main;
import com.skriptide.include.Addon;
import com.skriptide.include.Api;
import com.skriptide.include.Skript;
import com.skriptide.util.VersionReader;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by yannh on 28.01.2017.
 */
public class ManageAddsGuiController {

    @FXML
    private Button scriptAddBtn;
    @FXML
    private Button scriptRemoveBtn;
    @FXML
    private TableView scriptTable;
    @FXML
    private Button serverAddBtn;
    @FXML
    private Button serverRemove;
    @FXML
    private TableView serverTable;
    @FXML
    private Button addonAddBtn;
    @FXML
    private Button addonsRemoveBtn;
    @FXML
    private TableView addonsTable;
    @FXML
    private Button confirmBtn;

    private void confirmAction() {

        Stage stage = (Stage) confirmBtn.getScene().getWindow();
        stage.close();

    }


    private void addScript() {

        Stage fileChooserWindow = new Stage();
        FileChooser dirChooser = new FileChooser();
        dirChooser.setTitle("Choose Path to Script-plugin");
        dirChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JAR", "*.jar")

        );
        File f = dirChooser.showOpenDialog(fileChooserWindow);

        if (f != null) {
            Main.manager.addSkript(new Skript(f.getName().replace(".","-"), VersionReader.getVersionOfPlugin(f), f.getAbsolutePath()));
            refreshList();

            if (Main.debugMode) {
                System.out.println("Added Skript!");
            }
        }
    }

    private void addServer() {

        Stage fileChooserWindow = new Stage();
        FileChooser dirChooser = new FileChooser();
        dirChooser.setTitle("Choose Path to Bukkit or Spigot Plugin");
        dirChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JAR", "*.jar")
        );
        File f = dirChooser.showOpenDialog(fileChooserWindow);

        if (f != null) {

            Main.manager.addApi(new Api(f.getName().replace(".","-"), VersionReader.getVersionOfServer(f), f.getAbsolutePath()));
            refreshList();
            if (Main.debugMode) {
                System.out.println("added Bukkit/spigot version");
            }
        }
    }

    private void addAddon() {

        Stage fileChooserWindow = new Stage();
        FileChooser dirChooser = new FileChooser();
        dirChooser.setTitle("Choose Path to Addon-Plugin");
        dirChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JAR", "*.jar")
        );
        File f = dirChooser.showOpenDialog(fileChooserWindow);
        if (f != null) {
            Main.manager.addAddon(new Addon(VersionReader.getNameOfPlugin(f), VersionReader.getVersionOfPlugin(f), f.getAbsolutePath()));
            System.out.println("LoL es geht");
            refreshList();
            if (Main.debugMode) {

                System.out.println("added addon");
            }
        } else {
            System.out.println("Lol, das geht nicht");
        }

    }

    private void remScript() {

        ObservableList<Skript> selected, all;
        all = scriptTable.getItems();
        selected = scriptTable.getSelectionModel().getSelectedItems();

        for (int i = 0; i != selected.size(); i++) {
            Skript skript = selected.get(i);
            String path = skript.getName();
            System.out.println(path);
            Main.manager.removeSkript(path);
        }
        selected.forEach(all::remove);
        if (Main.debugMode) {
            System.out.println("removed skripts versions");
        }
    }

    private void remServer() {


        ObservableList<Api> selected, all;
        all = serverTable.getItems();
        selected = serverTable.getSelectionModel().getSelectedItems();

        for (int i = 0; i != selected.size(); i++) {
            Api skript = selected.get(i);
            String path = skript.getName();
            System.out.println(path);
            Main.manager.removeApi(path);
        }
        selected.forEach(all::remove);
        if (Main.debugMode) {
            System.out.println("removed bukkit/spigot versions");
        }

    }

    private void remAddon() {

        ObservableList<Addon> selected, all;
        all = addonsTable.getItems();
        selected = addonsTable.getSelectionModel().getSelectedItems();
        System.out.println("lol");
        for (int i = 0; i != selected.size(); i++) {
            Addon skript = selected.get(i);
            String path = skript.getName();
            System.out.println(path);
            Main.manager.removeAddon(path);
        }
        selected.forEach(all::remove);
        if (Main.debugMode) {
            System.out.println("removed addons");
        }
    }


    private void setServerVersions() {

        //MC-Server Vrsionen
        TableColumn<Api, String> nameColum = new TableColumn<>("Name");
        nameColum.setMinWidth(450);
        nameColum.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Api, String> versionColum = new TableColumn<>("Version");
        versionColum.setMinWidth(450);
        versionColum.setCellValueFactory(new PropertyValueFactory<>("version"));

        TableColumn<Api, String> pathColum = new TableColumn<>("Path");
        pathColum.setMinWidth(450);
        pathColum.setCellValueFactory(new PropertyValueFactory<>("path"));


        serverTable.setItems(Main.manager.getApisForGui());
        serverTable.getColumns().addAll(nameColum, versionColum, pathColum);
        if (Main.debugMode) {
            System.out.println("set server versions ");
        }

    }

    private void setSkriptVersions() {

        //MC-Server Vrsionen
        TableColumn<Skript, String> nameColum = new TableColumn<>("Name");
        nameColum.setMinWidth(450);
        nameColum.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Skript, String> versionColum = new TableColumn<>("Version");
        versionColum.setMinWidth(450);
        versionColum.setCellValueFactory(new PropertyValueFactory<>("version"));

        TableColumn<Skript, String> pathColum = new TableColumn<>("Path");
        pathColum.setMinWidth(450);
        pathColum.setCellValueFactory(new PropertyValueFactory<>("path"));


        scriptTable.setItems(Main.manager.getSkriptsForGui());
        scriptTable.getColumns().addAll(nameColum, versionColum, pathColum);

        if (Main.debugMode) {
            System.out.println("set skript versions");
        }

    }

    private void setSkriptAddons() {

        //MC-Server Vrsionen
        TableColumn<Addon, String> nameColum = new TableColumn<>("Name");
        nameColum.setMinWidth(450);
        nameColum.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Addon, String> versionColum = new TableColumn<>("Version");
        versionColum.setMinWidth(450);
        versionColum.setCellValueFactory(new PropertyValueFactory<>("version"));

        TableColumn<Addon, String> pathColum = new TableColumn<>("Path");
        pathColum.setMinWidth(450);
        pathColum.setCellValueFactory(new PropertyValueFactory<>("path"));


        addonsTable.setItems(Main.manager.getAddonsForGui());
        addonsTable.getColumns().addAll(nameColum, versionColum, pathColum);
        if (Main.debugMode) {

            System.out.println("set skript addons");
        }

    }

    public void setLists() {


        scriptAddBtn.setOnAction(event -> addScript());
        scriptRemoveBtn.setOnAction(event -> remScript());
        serverAddBtn.setOnAction(event -> addServer());
        serverRemove.setOnAction(event -> remServer());
        addonsRemoveBtn.setOnAction(event -> remAddon());
        addonAddBtn.setOnAction(event -> addAddon());
        confirmBtn.setOnAction(event -> confirmAction());

        setSkriptAddons();
        setServerVersions();
        setSkriptVersions();


    }

    private void refreshList() {


        addonsTable.getItems().clear();
        scriptTable.getItems().clear();
        serverTable.getItems().clear();

        serverTable.setItems(Main.manager.getApisForGui());
        scriptTable.setItems(Main.manager.getSkriptsForGui());
        addonsTable.setItems(Main.manager.getAddonsForGui());

        if (Main.debugMode) {

            System.out.println("refreshed list");
        }
    }
}
