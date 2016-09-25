package com.skriptide.guis.idegui;

import com.skriptide.codemanage.*;
import com.skriptide.guis.SceneManager;
import com.skriptide.main.Main;
import com.skriptide.util.ConfigManager;
import com.skriptide.util.DragResizer;
import com.skriptide.util.MCServer;
import com.skriptide.util.Project;
import com.skriptide.util.skunityapi.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.PopupAlignment;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IdeGuiController {

    @FXML
    public MenuItem manageAddonsPoint;
    @FXML
    public Button startServerBtn;
    @FXML
    public MenuItem createServerMenuPoint;
    @FXML
    public MenuItem manageServerMenuItem;
    @FXML
    public MenuItem saveMenuPoint;
    @FXML
    public MenuItem newProjectMenuPoint;
    @FXML
    public TabPane codeTabPane;
    @FXML
    public Label searchLabel;
    @FXML
    public CodeArea consoleOutputTextArea;
    @FXML
    public Label runningServerLabel;
    @FXML
    public ListView<String> projectsList;
    @FXML
    public TextField comandSendTextField;
    @FXML
    public Button commandSendBtn;
    @FXML
    public Button stopServerBtn;
    @FXML
    public Button restartServerBtn;
    @FXML
    public ComboBox<String> serverListComboBox;
    @FXML
    public MenuItem runPoint;
    @FXML
    public Label pathLabel;
    @FXML
    public MenuItem debuggingPoint;
    @FXML
    public ProgressBar mainProcessBar;
    @FXML
    public BorderPane mainBorderPane;
    @FXML
    public TabPane lowerTabPane;
    @FXML
    public VBox secBox;
    @FXML
    public Label prNameLbl;
    @FXML
    public Label prSkVersionLbl;
    @FXML
    public Label prServerLbl;
    @FXML
    public TextArea prNotesArea;
    @FXML
    public ListView<String> prDependList;


    public SceneManager sceneManager;
    private ContextMenu menu;


    public void setUpWin() {

        serverListComboBox.setOnShowing(event -> loadInServers());
        manageAddonsPoint.setOnAction(event -> {
            try {
                manageAddons();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        commandSendBtn.setOnAction(event -> sendCommand());
        startServerBtn.setOnAction(event -> startServer());
        debuggingPoint.setOnAction(event -> triggerDebugger());
        createServerMenuPoint.setOnAction(event -> newServer());
        runPoint.setOnAction(event -> runProject());
        manageServerMenuItem.setOnAction(event -> manageServers());
        saveMenuPoint.setOnAction(event -> saveOpenProjects());
        newProjectMenuPoint.setOnAction(event -> newProject());

        DragResizer.makeResizable(secBox);
        comandSendTextField.setOnKeyPressed(event -> {

            KeyCode code = event.getCode();

            if (code == KeyCode.ENTER) {
                sendCommand();
            }
        });
        SceneManager.procBar = this.mainProcessBar;
        SceneManager.consoleOut = consoleOutputTextArea;
        SceneManager.runningServerLabel = runningServerLabel;
        SceneManager.projectsList = this.projectsList;
        SceneManager.runninServerList = this.serverListComboBox;


        codeTabPane.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    Tab tab = codeTabPane.getSelectionModel().getSelectedItem();


                    if (tab != null) {
                        String name = tab.getText();
                        ObservableList<Project> prs = Project.getProjects();
                        for (Project project : prs.sorted()) {
                            if (project.getName().equalsIgnoreCase(name)) {

                                pathLabel.setText(project.getSkriptPath());
                                prNameLbl.setText("Name: " + project.getName());
                                prServerLbl.setText("Server: " + project.getServer().getname());
                                prSkVersionLbl.setText("Skript version: " + project.getSk().getVersion());
                                prNotesArea.setText(project.getNotes());
                            }
                        }
                    }
                }
        );

        if (Main.debugMode) {
            System.out.println("Loaded window");
        }
    }

    public void sendCommand() {
        if (SceneManager.runningServer != null) {
            try {
                BufferedWriter writer = SceneManager.runningServer.getWriter();


                writer.write(comandSendTextField.getText());
                writer.newLine();
                System.out.println("wrote:" + comandSendTextField.getText());
                writer.flush();
                comandSendTextField.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Main.debugMode) {
            System.out.println("sendet command");
        }
    }

    public void runProject() {

        Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
        String name = tab.getText();

        ObservableList<Project> prs = Project.getProjects();
        for (Project project : prs.sorted()) {
            if (project.getName().equalsIgnoreCase(name)) {

                serverListComboBox.getSelectionModel().select(project.getServer().getname());
                project.runProject();
            }
        }
        if (Main.debugMode) {

            System.out.println("started project");
        }
    }

    public void loadInProjects() {


        if (ConfigManager.checkConfig() == 0) {
            ObservableList<Project> projects = Project.getProjects();
            System.out.println(projects.size());
            projectsList.getItems().clear();
            for (Project project : projects.sorted()) {
                if (project.getName() != null) {
                    projectsList.getItems().add(project.getName());
                }
            }
        }
        if (Main.debugMode) {

            System.out.println("Loaded projects");
        }

        projectsList.setOnMouseClicked(event -> {

            MouseButton btn = event.getButton();

            if (projectsList.getSelectionModel().getSelectedItem() != null) {
                if (btn == MouseButton.SECONDARY) {
                    if (menu == null || !menu.isShowing()) {
                        menu = new ContextMenu();
                        MenuItem item = new MenuItem("Delete");
                        MenuItem item1 = new MenuItem("Rename");
                        MenuItem item2 = new MenuItem("Move");
                        Menu serverList = new Menu("Change server");
                        item.setOnAction(event12 -> {
                            ObservableList<Project> prs = Project.getProjects();
                            for (Project pr : prs) {
                                if (pr.getName().equals(projectsList.getSelectionModel().getSelectedItem())) {
                                    pr.deleteProject();
                                }
                            }
                        });
                        item1.setOnAction(event13 -> {
                            ObservableList<Project> prs = Project.getProjects();
                            for (Project pr : prs) {
                                if (pr.getName().equals(projectsList.getSelectionModel().getSelectedItem())) {
                                    pr.reNameProject();
                                }
                            }
                        });
                        item2.setOnAction(event14 -> {
                            ObservableList<Project> prs = Project.getProjects();
                            for (Project pr : prs) {
                                if (pr.getName().equals(projectsList.getSelectionModel().getSelectedItem())) {
                                    pr.moveProject();
                                }
                            }
                        });
                        for (MenuItem selected : serverList.getItems()) {

                            selected.setOnAction(event1 -> {
                                ObservableList<Project> prs = Project.getProjects();
                                for (Project pr : prs) {
                                    if (pr.getName().equals(projectsList.getSelectionModel().getSelectedItem())) {
                                        pr.changeServer(selected.getText());
                                    }
                                }
                            });
                        }


                        ObservableList<MCServer> servers = MCServer.getAllServers();
                        ObservableList<Project> prs = Project.getProjects();
                        for (MCServer srv : servers.sorted()) {
                            for (Project pr : prs.sorted()) {
                                if (pr.getName().equalsIgnoreCase(projectsList.getSelectionModel().getSelectedItem())) {
                                    if (pr.getServer().getname().equalsIgnoreCase(srv.getname())) {
                                        MenuItem srvItem = new MenuItem(srv.getname() + " (Current)");
                                        serverList.getItems().add(srvItem);
                                    } else {
                                        MenuItem srvItem = new MenuItem(srv.getname());
                                        serverList.getItems().add(srvItem);
                                    }
                                }
                            }
                        }
                        menu.getItems().clear();
                        menu.getItems().addAll(item, item1, item2, serverList);


                        menu.show(projectsList, event.getScreenX(), event.getScreenY());
                    }
                } else if (btn == MouseButton.PRIMARY) {

                    if (menu == null || !menu.isShowing()) {
                        openProject();
                    } else {
                        if (menu.isShowing() && menu != null) {
                            menu.hide();
                        }
                    }
                }

            }
        });
    }

    public void triggerDebugger() {
        sceneManager.openDebugger();
    }

    public void loadInServers() {

        serverListComboBox.getItems().setAll();
        ObservableList<MCServer> servers = MCServer.getAllServers();
        for (MCServer srv : servers.sorted()) {
            serverListComboBox.getItems().add(srv.getname());
        }

        if (Main.debugMode) {
            System.out.println("Loaded servers");
        }

    }

    public void startServer() {

        ObservableList<MCServer> servers = MCServer.getAllServers();
        for (MCServer srv : servers.sorted()) {
            if (srv.getname().equalsIgnoreCase(serverListComboBox.getSelectionModel().getSelectedItem())) {
                srv.startServer();
            }
        }
        if (Main.debugMode) {

            System.out.println("start server");
        }
    }

    public void newProject() {

        sceneManager.openCreateProject();

        if (Main.debugMode) {
            System.out.println("open new project");
        }

    }

    public void newServer() {

        sceneManager.openCreateServer();
        if (Main.debugMode) {
            System.out.println("open new server");
        }
    }

    public void manageAddons() throws IOException {

        sceneManager.openManageVersions();
        if (Main.debugMode) {

            System.out.println("open manage addons");
        }

    }

    public void manageServers() {

        sceneManager.openManageServer();
        if (Main.debugMode) {
            System.out.println("open manage server");
        }
    }


    public void openProject() {
        CodeArea area = new CodeArea();

        CompleteList completeList = new CompleteList();


        new AutoComplete().setAutoComplete(area, completeList, codeTabPane, commandSendBtn);


        String selection = projectsList.getSelectionModel().getSelectedItem();


        if (selection != null) {
            Project project = new Project(selection);
            if (!SceneManager.openProjects.contains(project.getName())) {
                Tab tab = new Tab(project.getName());

                tab.setOnCloseRequest(event -> {


                    boolean toSave = new SceneManager().infoCheck("Save project?", tab.getText(), "Save the project: " + tab.getText());
                    if (toSave) {
                        CodeWriter writer = new CodeWriter(area.getText(), project);
                        System.out.println("saver called");
                        writer.write();
                    }

                    SceneManager.openProjects.remove(project.getName());
                });
                codeTabPane.getTabs().add(tab);

                tab.setContent(area);

                File skript = new File(project.getSkriptPath().replace("/", "/"));


                CodeReader reader = new CodeReader(skript);

                area.appendText(reader.getCode());


                prNameLbl.setText("Name: " + project.getName());
                prServerLbl.setText("Server: " + project.getServer().getname());
                prSkVersionLbl.setText("Skript version: " + project.getSk().getVersion());
                prNotesArea.setText(project.getNotes());
                SceneManager.openProjects.add(project.getName());

                ControlMain.controlCode(area);

            }

        }
        if (Main.debugMode) {
            System.out.println("Open project");
        }
    }


    public void saveOpenProjects() {

        for (Tab tab : codeTabPane.getTabs().sorted()) {
            StyleClassedTextArea area = (StyleClassedTextArea) tab.getContent();

            Project pr = new Project(tab.getText());


            CodeWriter writer = new CodeWriter(area.getText(), pr);
            System.out.println("saver called");
            writer.write();


        }
        if (Main.debugMode) {
            System.out.println("Saved open projects!");
        }
    }


}