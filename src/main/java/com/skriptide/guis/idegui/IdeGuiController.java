package com.skriptide.guis.idegui;

import com.skriptide.codemanage.*;
import com.skriptide.guis.ExternWindow;
import com.skriptide.guis.SceneManager;
import com.skriptide.main.Main;
import com.skriptide.util.ConfigManager;
import com.skriptide.util.DragResizer;
import com.skriptide.util.MCServer;
import com.skriptide.util.Project;
import com.skriptide.util.skunityapi.SkUnityAPI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Liz3
 * <p>
 * This class ist the main Controller, it is the fxml controller for the Main Window
 * From here all action directly related to the gui are performed
 */
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
    public TextField searchTxTField;
    @FXML
    public MenuItem debuggingPoint;
    @FXML
    public ProgressBar mainProcessBar;
    @FXML
    public BorderPane mainBorderPane;
    @FXML
    public TabPane lowerTabPane;
    @FXML
    public Label prNameLbl;
    @FXML
    public Label prSkVersionLbl;
    @FXML
    public Label prServerLbl;
    @FXML
    public TextArea prNotesArea;
    @FXML
    public MenuItem ideSettingsPoint;
    @FXML
    public MenuItem manageExtensions;
    @FXML
    public ListView<String> prDependList;

    public static SceneManager sceneManager;
    private ContextMenu menu;
    private ContextMenu tabMenu;


    private void openSettings() {

        sceneManager.openSettings();
    }

    /**
     * This method is called first in the Scene manager before showing the actual gui
     * <p>
     * Mainly its setting action listeners and loadings of the projects, see more in the method
     */
    public void setUpWin() {

        /**
        This listeners fires of the search of a specific char sequence in the currently visible Project in the window
        @see com.skriptide.codemanage.Search
         */
        searchTxTField.setOnKeyReleased(event -> {
            Tab tab = codeTabPane.getSelectionModel().getSelectedItem();

            if (searchTxTField.getText() != null && !searchTxTField.getText().equals("")) {
                Search.search(tab, searchTxTField.getText());
            } else {
                ControlMain.controlCode((CodeArea) tab.getContent(), tab);
            }
        });

        /**
        This listener is showing a little menu on the TabPane of the projects,
        to provide the option do open the current project in a extra window
         @see com.skriptide.guis.ExternWindow
         */
        codeTabPane.setOnMouseClicked(event -> {


            if (event.getButton().name().equals("SECONDARY") && codeTabPane.getTabs().size() != 0) {

                tabMenu = new ContextMenu();

                MenuItem item = new MenuItem("Open in new Tab");
                tabMenu.getItems().add(item);

                item.setOnAction(event1 -> {

                    Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
                    String name = tab.getText();
                    ObservableList<Project> prs = Project.getProjects();


                    assert prs != null;
                    for (Project project : prs.sorted()) {
                        if (project.getName().equalsIgnoreCase(name)) {

                            new ExternWindow(tab, project);

                            codeTabPane.getTabs().remove(tab);

                        }
                    }
                });

                tabMenu.show(codeTabPane, event.getScreenX(), event.getScreenY());
            } else if (tabMenu != null && tabMenu.isShowing()) {
                tabMenu.hide();
            }

        });
        /**
         * This listeners is loading in all Servers for several Gui components,
         * when it is showed
         *
         */
        serverListComboBox.setOnShowing(event -> loadInServers());

        /**
         * The following 8 listeners do all server the option do open the other Windows,
         * for example the Windows for the management of the servers
         * @see manageAddons()
         * @see openSettings()
         * @see openExtensions()
         * @see startServer()
         * @see triggerDebugger()
         * @see newServer()
         * @see manageServer()
         * @see newProject()
         */
        manageAddonsPoint.setOnAction(event -> {
            try {
                manageAddons();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        ideSettingsPoint.setOnAction(event -> openSettings());
        manageExtensions.setOnAction(event -> openExtensions());
        startServerBtn.setOnAction(event -> startServer());
        debuggingPoint.setOnAction(event -> triggerDebugger());
        createServerMenuPoint.setOnAction(event -> newServer());
        manageServerMenuItem.setOnAction(event -> manageServers());
        newProjectMenuPoint.setOnAction(event -> newProject());

        /**
         * This listener triggers the method sendCommand()
         * @see sendCommand()
         * @param text
         */
        commandSendBtn.setOnAction(event -> sendCommand(null));

        /**
         * This listener triggers the method to run a project
         * @see runProject()
         */
        runPoint.setOnAction(event -> runProject());

        /**
         * This listener triggers the method which saves all open projects
         * @see saveOpenProjects()
         */
        saveMenuPoint.setOnAction(event -> saveOpenProjects());

        /**
         * This objects invokes the method which makes the Bottom part of the Gui response
         * @see com.skriptide.util.DragResizer
         * @param bottomSec
         */
        DragResizer resizer = new DragResizer();
        resizer.makeResizable(lowerTabPane);

        /**
         * This listener invokes the method to send a command to the currently running server
         * @param text
         */
        comandSendTextField.setOnKeyPressed(event -> {

            KeyCode code = event.getCode();

            if (code == KeyCode.ENTER) {
                sendCommand(null);
            }
        });

        /**
         * In the Scene manage class are some Objects of type Gui,
         * because the are needed outsite the controller, there are public and static
         * and set here to the elements of the actual gui
         */
        SceneManager.procBar = this.mainProcessBar;
        SceneManager.consoleOut = consoleOutputTextArea;
        SceneManager.runningServerLabel = runningServerLabel;
        SceneManager.projectsList = this.projectsList;
        SceneManager.runninServerList = this.serverListComboBox;

        /**
         * This listeners sets the Information in the bottom of the
         * Gui when another tab is selected trough those labels are not bound to the TabPane
         */
        codeTabPane.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    Tab tab = codeTabPane.getSelectionModel().getSelectedItem();


                    if (tab != null) {
                        String name = tab.getText();
                        ObservableList<Project> prs = Project.getProjects();

                        CompleteList cl = CompleteList.getCurrentInstance();
                        if (cl != null && cl.win.isShowing()) {
                            cl.win.hide();
                        }

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


    public void openFromFile(String path) {

        File project = new File(path);
        CodeArea area = new CodeArea();


        if (!SceneManager.openProjects.contains("External: " + project.getName())) {
            Tab tab = new Tab("External: " + project.getName());

            tab.setOnCloseRequest(event -> SceneManager.openProjects.remove("External: " + project.getName()));
            codeTabPane.getTabs().add(tab);

            tab.setContent(area);


            CodeReader reader = new CodeReader(project);

            area.appendText(reader.getCode());

            SceneManager.openProjects.add("External: " + project.getName());

            ControlMain.controlCode(area, tab);


        }
        if (Main.debugMode) {
            System.out.println("Open project");
        }

    }

    public void sendCommand(String text) {
        if (SceneManager.runningServer != null) {
            try {
                BufferedWriter writer = SceneManager.runningServer.getWriter();

                if (text == null) {
                    writer.write(comandSendTextField.getText());
                    comandSendTextField.clear();
                } else {
                    writer.write(text);
                }
                writer.newLine();
                System.out.println("wrote:" + comandSendTextField.getText());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Main.debugMode) {
            System.out.println("sendet command");
        }
    }

    private void runProject() {

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
                                    pr.reNameProject(codeTabPane);
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

    private void triggerDebugger() {
        sceneManager.openDebugger();
    }

    private void loadInServers() {

        serverListComboBox.getItems().setAll();
        ObservableList<MCServer> servers = MCServer.getAllServers();
        for (MCServer srv : servers.sorted()) {
            serverListComboBox.getItems().add(srv.getname());
        }

        if (Main.debugMode) {
            System.out.println("Loaded servers");
        }

    }

    private void startServer() {

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

    private void newProject() {

        sceneManager.openCreateProject();

        if (Main.debugMode) {
            System.out.println("open new project");
        }

    }

    private void newServer() {

        sceneManager.openCreateServer();
        if (Main.debugMode) {
            System.out.println("open new server");
        }
    }

    private void manageAddons() throws IOException {

        sceneManager.openManageVersions();
        if (Main.debugMode) {

            System.out.println("open manage addons");
        }

    }

    private void openExtensions() {

        try {
            sceneManager.openManageExtensions();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void reAttach(ExternWindow window) {

        ExternWindow.windows.remove(window);

        codeTabPane.getTabs().add(window.getTab());

    }

    private void manageServers() {

        sceneManager.openManageServer();
        if (Main.debugMode) {
            System.out.println("open manage server");
        }
    }

    public void setAutoComplete(CodeArea area) {

        CompleteList completeList = new CompleteList();
        AddonDepenencies depenencies = new AddonDepenencies(area, new SkUnityAPI(), prDependList);

        new AutoComplete().setAutoComplete(area, completeList, codeTabPane, commandSendBtn, depenencies);

    }

    private void openProject() {

        CodeArea area = new CodeArea();


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

                ControlMain.controlCode(area, tab);

            }

        }
        if (Main.debugMode) {
            System.out.println("Open project");
        }
    }

    public void saveOpenProjects() {

        codeTabPane.getTabs().sorted().stream().filter(tab -> !tab.getText().contains("External: ")).forEach(tab -> {
            StyleClassedTextArea area = (StyleClassedTextArea) tab.getContent();


            Project pr = new Project(tab.getText());


            CodeWriter writer = new CodeWriter(area.getText(), pr);
            System.out.println("saver called");
            writer.write();
        });
        if (ExternWindow.windows.size() != 0) {
            for (ExternWindow win : ExternWindow.windows) {

                new CodeWriter(win.getArea().getText(), win.getProject()).write();
            }
        }
        if (Main.debugMode) {
            System.out.println("Saved open projects!");
        }
    }


}