package com.skriptide.gui.controller;

import com.skriptide.Main;
import com.skriptide.codemanage.AutoSaver;
import com.skriptide.codemanage.autocomplete.CompleteList;
import com.skriptide.codemanage.Search;
import com.skriptide.config.Config;
import com.skriptide.gui.ExternFile;
import com.skriptide.gui.OpenFile;
import com.skriptide.gui.OpenProject;
import com.skriptide.include.Project;
import com.skriptide.include.Server;
import com.skriptide.util.DragResizer;
import com.skriptide.util.ExportSettings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import org.fxmisc.richtext.CodeArea;

import java.util.HashMap;

/**
 * Created by yannh on 27.01.2017.
 */
public class IdeGuiController {

    public static IdeGuiController controller;
    TreeItem<String> rootItem = null;

    @FXML
    private BorderPane absolutePane;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private TabPane codeTabPane;
    @FXML
    private TreeView<String> projectsList;
    @FXML
    private MenuBar mainMenuMenuBar;
    @FXML
    private Menu filePoint;
    @FXML
    private MenuItem newProjectMenuPoint;
    @FXML
    private Menu openResentMenu;
    @FXML
    private MenuItem saveMenuPoint;
    @FXML
    private Menu exportPoint;
    @FXML
    private MenuItem manageAddonsPoint;
    @FXML
    private MenuItem ideSettingsPoint;
    @FXML
    private MenuItem exportSettingsPoint;
    @FXML
    private MenuItem closeMenuPoint;
    @FXML
    private MenuItem runPoint;
    @FXML
    private Menu runMenuMenu;
    @FXML
    private MenuItem createServerMenuPoint;
    @FXML
    private MenuItem manageServerMenuItem;
    @FXML
    private MenuItem debuggingPoint;
    @FXML
    private MenuItem manageExtensions;
    @FXML
    private MenuItem setupVCS;
    @FXML
    private MenuItem commit;
    @FXML
    private MenuItem pull;
    @FXML
    private MenuItem revert;
    @FXML
    private MenuItem compare;
    @FXML
    private Label searchLabel;
    @FXML
    private TextField searchTxTField;
    @FXML
    private Label pathLabel;
    @FXML
    private TabPane lowerTabPane;
    @FXML
    private Label prNameLbl;
    @FXML
    private Label prSkVersionLbl;
    @FXML
    private Label prServerLbl;
    @FXML
    private ListView prDependList;
    @FXML
    private TextArea prNotesArea;
    @FXML
    private TableView errorTable;
    @FXML
    private TextField comandSendTextField;
    @FXML
    private Button commandSendBtn;
    @FXML
    private Label runningServerLabel;
    @FXML
    private ComboBox<String> serverListComboBox;
    @FXML
    private Button startServerBtn;
    @FXML
    private Button stopServerBtn;
    @FXML
    private Button restartServerBtn;
    @FXML
    private ProgressBar mainProcessBar;
    @FXML
    private CodeArea consoleOutputTextArea;

    private ContextMenu menu;
    private ContextMenu tabMenu;


    public void importServers() {

        serverListComboBox.getItems().clear();
        for(Server server : Main.manager.getServer().values()) {

            serverListComboBox.getItems().add(server.getName());
        }
    }
    public void addProject(Project project) {

        TreeItem<String> projectItem = new TreeItem<>(project.getName());

        for (String str : project.getSkFiles().keySet()) {

            TreeItem<String> item1 = new TreeItem<>(str);

            projectItem.getChildren().add(item1);
        }

        rootItem.getChildren().add(projectItem);

    }

    public void initGui() {



        runPoint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Tab selected = codeTabPane.getSelectionModel().getSelectedItem();
                for(OpenProject pr : Main.sceneManager.getOpenFiles()) {

                    for(OpenFile openFile : pr.getOpenFiles().values()) {

                        if(openFile.getTab() != null) {

                            if(openFile.getTab().equals(selected)) {

                                CodeArea area = openFile.getArea();
                                if (Main.runningServer == null) {

                                    if(openFile.getOpenProject().getProject().getServer() != null) {

                                        openFile.getOpenProject().getProject().writeCode(area.getText(), openFile.getProject().getName());

                                        openFile.getOpenProject().getProject().copyToOutput(openFile.getProject(), openFile.getOpenProject().getProject().getServer());

                                        openFile.getOpenProject().getProject().getServer().startServer();

                                    } else {
                                        Main.sceneManager.infoCheck("Error","Failed to launch project", "Please first set a server to the project or run the the skript trough the: \"Run on\" Menu", Alert.AlertType.ERROR);

                                    }
                                } else {

                                    if(openFile.getOpenProject().getProject().getServer() != null) {

                                        Server r = Main.runningServer;
                                        Server s = openFile.getOpenProject().getProject().getServer();

                                        if(r.getName().equals(s.getName())) {


                                            openFile.getOpenProject().getProject().writeCode(area.getText(), openFile.getProject().getName());
                                            openFile.getOpenProject().getProject().copyToOutput(openFile.getProject(), r);
                                            r.sendCommand("skript reload " + openFile.getProject().getName());

                                        } else {

                                            boolean ok = Main.sceneManager.infoCheck("Error", "Failed to launch project",
                                                    "The Running server is not the target server of the skript, do you want to start the skript on the current running serveer?", Alert.AlertType.CONFIRMATION);

                                            if(ok) {
                                                openFile.getOpenProject().getProject().writeCode(area.getText(), openFile.getProject().getName());
                                                openFile.getOpenProject().getProject().copyToOutput(openFile.getProject(), r);
                                                r.sendCommand("skript reload " + openFile.getProject().getName());
                                            }
                                        }

                                    } else {
                                        Main.sceneManager.infoCheck("Error","Failed to launch project", "Please first set a server to the project or run the the skript trough the: \"Run on\" Menu", Alert.AlertType.ERROR);

                                    }

                                }

                                break;
                            }
                        }
                    }
                }
            }
        });
        closeMenuPoint.setOnAction(event -> {

            if(Main.saver != null) {
                Main.saver = null;
            }
            for(OpenProject pr : Main.sceneManager.getOpenFiles()) {

                for(OpenFile file : pr.getOpenFiles().values()) {
                    pr.getProject().writeCode(file.getArea().getText(), file.getProject().getName());
                }
                System.exit(0);
            }

        });
        manageServerMenuItem.setOnAction(event -> Main.sceneManager.openManageServer());
        codeTabPane.setOnMouseClicked(event -> {


            if (event.getButton().name().equals("SECONDARY") && codeTabPane.getTabs().size() != 0) {

                tabMenu = new ContextMenu();

                MenuItem item = new MenuItem("Open in new Tab");
                tabMenu.getItems().add(item);

                item.setOnAction(event1 -> {

                    Tab active = codeTabPane.getSelectionModel().getSelectedItem();
                    OpenFile p = null;
                    for (OpenProject o : Main.sceneManager.getOpenFiles()) {


                        for (OpenFile f : o.getOpenFiles().values()) {

                            if (f.getTab().equals(active)) {
                                p = f;
                            }
                        }


                    }
                    assert p != null;
                    p.toExtern();
                    codeTabPane.getTabs().remove(p.getTab());


                });

                tabMenu.show(codeTabPane, event.getScreenX(), event.getScreenY());
            } else if (tabMenu != null && tabMenu.isShowing()) {
                tabMenu.hide();
            }

        });
        ideSettingsPoint.setOnAction(event -> Main.sceneManager.openSettingsGui());
        createServerMenuPoint.setOnAction(event -> Main.sceneManager.openServerProjectGui());
        exportSettingsPoint.setOnAction(event -> Main.sceneManager.openExportSettings());
        rootItem = new TreeItem<>("Projects");
        rootItem.setExpanded(true);
        projectsList.setRoot(rootItem);
        controller = this;
        newProjectMenuPoint.setOnAction(event -> Main.sceneManager.openCreateProjectGui());
        manageAddonsPoint.setOnAction(event -> Main.sceneManager.openManageVersions());
        importServers();
        commandSendBtn.setOnAction(event -> {
            if(Main.runningServer !=null) {
                Main.runningServer.sendCommand(comandSendTextField.getText());
                comandSendTextField.clear();
            }
        });

        comandSendTextField.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.ENTER ){
                if(Main.runningServer !=null) {
                    Main.runningServer.sendCommand(comandSendTextField.getText());
                    comandSendTextField.clear();
                }
            }
        });
        if(Main.sceneManager.getFromExt() != null) {
            openFile(Main.sceneManager.getFromExt());
        }
        startServerBtn.setOnAction(event -> {
            consoleOutputTextArea.clear();
            String selected = serverListComboBox.getSelectionModel().getSelectedItem();

            Server server = Main.manager.getServer().get(selected);
            server.startServer();
        });


        for (Project pr : Main.manager.getProjects().values()) {
            addProject(pr);
        }

        new DragResizer().makeResizable(lowerTabPane);
        projectsList.setOnMouseClicked(event -> {

            MouseButton btn = event.getButton();
            if (projectsList.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            String selection = projectsList.getSelectionModel().getSelectedItem().getValue();

            TreeItem<String> selectionItem = projectsList.getSelectionModel().getSelectedItem();
            if (projectsList.getSelectionModel().getSelectedItem() != null) {

                if (btn == MouseButton.SECONDARY) {
                    if (menu == null || !menu.isShowing()) {
                        menu = new ContextMenu();

                        MenuItem newFile = new MenuItem("New File");
                        MenuItem del = new MenuItem("Delete");
                        MenuItem rename = new MenuItem("Rename");
                        MenuItem move = new MenuItem("Move");
                        Menu changeServer = new Menu("Change Server");




                        if (selectionItem.getParent() != null && selectionItem.getParent().getValue().equals("Projects")) {



                            Project p = Main.manager.getProjects().get(selection);


                            for(Server server : Main.manager.getServer().values()) {

                                MenuItem s = new MenuItem(server.getName());

                                s.setOnAction(event15 -> p.setServer(server));

                                changeServer.getItems().add(s);

                            }
                            del.setOnAction(event14 -> {

                                if (!Main.sceneManager.infoCheck("Delete", "Delete Project", "Are you sure to delete the project: " + selection + " ?", Alert.AlertType.CONFIRMATION)) {
                                    return;
                                }
                                OpenProject op = null;
                                for (OpenProject project : Main.sceneManager.getOpenFiles()) {
                                    if (project.getProject().equals(p)) {

                                        op = project;
                                        for (OpenFile f : project.getOpenFiles().values()) {

                                            if (f.isExtern())
                                                f.reAttach();

                                            codeTabPane.getTabs().remove(f.getTab());
                                        }
                                    }
                                }

                                if (p.deleteProject()) {

                                    if (op != null)
                                        Main.sceneManager.getOpenFiles().remove(op);

                                    projectsList.getRoot().getChildren().remove(selectionItem);
                                }
                            });
                            newFile.setOnAction(event12 -> {


                                String name = "";
                                if (p != null) {
                                    if ((name = p.addSkiptFile()) != null) {

                                        selectionItem.getChildren().add(new TreeItem<>(name));
                                    }
                                }
                            });

                            menu.getItems().addAll(newFile, move, del, changeServer);
                        } else if (selectionItem.getParent() != null && selectionItem.getParent().getParent().getValue().equals("Projects")) {

                            menu.getItems().addAll(newFile, move, del, rename);



                            del.setOnAction(event13 -> {
                                Project p = Main.manager.getProjects().get(selectionItem.getParent().getValue());

                                if (!Main.sceneManager.infoCheck("Delete", "Delete File", "Are you sure to delete: " + selection + " ?", Alert.AlertType.CONFIRMATION)) {
                                    return;
                                }
                                for (OpenProject project : Main.sceneManager.getOpenFiles()) {
                                    if (project.getProject().equals(p)) {


                                        for (OpenFile f : project.getOpenFiles().values()) {

                                            if(f.getProject().getName().equals(selection)) {

                                                if(f.isExtern()) {
                                                    f.reAttach();

                                                }
                                                codeTabPane.getTabs().remove(f.getTab());
                                            }

                                        }
                                    }
                                }
                                if (p.deleteFile(selection)) {

                                    selectionItem.getParent().getChildren().remove(selectionItem);
                                }
                            });

                        } else {
                            return;
                        }
                        menu.show(projectsList, event.getScreenX(), event.getScreenY());
                    }
                } else if (btn == MouseButton.PRIMARY) {

                    if (menu == null || !menu.isShowing()) {


                        if (projectsList.getSelectionModel().getSelectedItem() == null) {
                            return;
                        }


                        if (selection.endsWith(".sk")) {


                            String fileName = selection;
                            String projectName = projectsList.getSelectionModel().getSelectedItem().getParent().getValue();


                            Project p = Main.manager.getProjects().get(projectName);

                            for (OpenProject o : Main.sceneManager.getOpenFiles()) {

                                if (o.getProject().equals(p)) {

                                    if (p == null) {
                                        return;
                                    }

                                    if (o.getOpenFiles().containsKey(selection)) {
                                        return;
                                    }
                                    o.openFile(selection);
                                    return;
                                }

                            }
                            OpenProject openProject = new OpenProject(p, codeTabPane, commandSendBtn, prDependList);

                            Main.sceneManager.getOpenFiles().add(openProject);
                            openProject.openFile(selection);

                            pathLabel.setText(openProject.getOpenFiles().get(selection).getProject().getAbsolutePath());
                            prNameLbl.setText("Name: " + openProject.getProject().getName());
                            if(openProject.getProject().getServer() != null) {
                                prServerLbl.setText("Server: " + openProject.getProject().getServer().getName());
                            }
                            prSkVersionLbl.setText("Skript version: " + openProject.getProject().getSkript().getVersion());
                            prNotesArea.setText(openProject.getProject().getNotes());
                            if (Main.saver == null) {
                                Main.saver = new AutoSaver();
                            }
                        }
                    } else {
                        if (menu.isShowing() && menu != null) {
                            menu.hide();
                        }
                    }
                }

            }
        });
        filePoint.setOnShown(event -> {


            exportPoint.getItems().clear();

            HashMap<String, ExportSettings> all = ExportSettings.getAll();
            for (String s : all.keySet()) {


                MenuItem item = new MenuItem(s);
                item.setOnAction(eve2 -> {
                    Tab active = codeTabPane.getSelectionModel().getSelectedItem();

                    CodeArea area = (CodeArea) active.getContent();

                    ExportSettings settings = all.get(s);

                    settings.deploy(area.getText(), active.getText());
                });
                exportPoint.getItems().add(item);
            }


        });

        codeTabPane.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
                    OpenFile tOp = null;
                    for (OpenProject o : Main.sceneManager.getOpenFiles()) {


                        for (OpenFile f : o.getOpenFiles().values()) {

                            if (f.getTab().equals(tab)) {
                                tOp = f;
                            }
                        }


                    }

                    if (tab != null) {
                        String name = tab.getText();


                        if (tOp == null) {
                            return;
                        }
                        CompleteList cl = CompleteList.getCurrentInstance();
                        if (cl != null && cl.win.isShowing()) {
                            cl.win.hide();
                        }


                        pathLabel.setText(tOp.getProject().getAbsolutePath());
                        prNameLbl.setText("Name: " + tOp.getProject().getName());
                        if(tOp.getOpenProject().getProject().getServer() != null) {
                            prServerLbl.setText("Server: " + tOp.getOpenProject().getProject().getServer().getName());
                        }
                        prSkVersionLbl.setText("Skript version: " + tOp.getOpenProject().getProject().getSkript().getVersion());
                        prNotesArea.setText(tOp.getOpenProject().getProject().getNotes());


                    }
                }
        );
        searchTxTField.setOnKeyReleased(event -> {
            Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
            OpenFile tOp = null;
            for (OpenProject o : Main.sceneManager.getOpenFiles()) {


                for (OpenFile f : o.getOpenFiles().values()) {

                    if (f.getTab().equals(tab)) {
                        tOp = f;
                    }
                }


            }
            if (tab != null) {
                if (searchTxTField.getText() != null && !searchTxTField.getText().equals("")) {

                    Search.search(tOp, searchTxTField.getText());
                } else {
                    tOp.resetHighlighting();


                }
            }
        });
    }

    public void openFile(String path) {

        System.out.println("called");
       Main.sceneManager.getExternFiles().add(new ExternFile(path, codeTabPane));



    }
    private void resentOpens() {


        Config config = Main.manager.getSettingsConf();

        for(String key : config.getAll("last")) {
            //TODO
        }
    }
    public ComboBox<String> getServerListComboBox() {
        return serverListComboBox;
    }

    public CodeArea getConsoleOutputTextArea() {
        return consoleOutputTextArea;
    }
}
