package com.skriptide.gui.controller;

import com.skriptide.Main;
import com.skriptide.codemanage.AutoSaver;
import com.skriptide.codemanage.CompleteList;
import com.skriptide.codemanage.Search;
import com.skriptide.gui.ExternWindow;
import com.skriptide.gui.OpenProject;
import com.skriptide.gui.SceneManager;
import com.skriptide.include.Project;
import com.skriptide.util.DragResizer;
import com.skriptide.util.ExportSettings;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.WindowEvent;
import org.fxmisc.richtext.CodeArea;

import java.util.HashMap;
import java.util.regex.Pattern;

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
    private ComboBox serverListComboBox;
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


    public void addProject(Project project) {

        TreeItem<String> projectItem = new TreeItem<>(project.getName());

        TreeItem<String> item = new TreeItem<>(project.getName() + ".sk");

        projectItem.getChildren().add(item);

        rootItem.getChildren().add(projectItem);

    }

    public void initGui() {


        codeTabPane.setOnMouseClicked(event -> {


            if (event.getButton().name().equals("SECONDARY") && codeTabPane.getTabs().size() != 0) {

                tabMenu = new ContextMenu();

                MenuItem item = new MenuItem("Open in new Tab");
                tabMenu.getItems().add(item);

                item.setOnAction(event1 -> {

                    Tab active = codeTabPane.getSelectionModel().getSelectedItem();
                    OpenProject p = null;
                    for(OpenProject pr : Main.sceneManager.getOpenProjects()) {

                        if(pr.getTab().equals(active)) {
                            p = pr;
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
        exportSettingsPoint.setOnAction(event -> Main.sceneManager.openExportSettings());
        rootItem = new TreeItem<>("Projects");
        rootItem.setExpanded(true);
        projectsList.setRoot(rootItem);
        controller = this;
        newProjectMenuPoint.setOnAction(event -> Main.sceneManager.openCreateProjectGui());
        manageAddonsPoint.setOnAction(event -> Main.sceneManager.openManageVersions());

        codeTabPane.getScene().getWindow().setOnCloseRequest(event -> {

            for(OpenProject op : Main.sceneManager.getOpenProjects()) {

                op.getProject().writeCode(op.getArea().getText());
            }

            System.exit(0);
        });

        for (Project pr : Main.manager.getProjects().values()) {
            addProject(pr);
        }

       new DragResizer().makeResizable(lowerTabPane);
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


                        menu.getItems().clear();
                        menu.getItems().addAll(item, item1, item2, serverList);


                        menu.show(projectsList, event.getScreenX(), event.getScreenY());
                    }
                } else if (btn == MouseButton.PRIMARY) {

                    if (menu == null || !menu.isShowing()) {


                        if (projectsList.getSelectionModel().getSelectedItem() == null) {
                            return;
                        }
                        String selection = projectsList.getSelectionModel().getSelectedItem().getValue();

                        if (selection.endsWith(".sk")) {

                            String tName = selection.split(Pattern.quote("."))[0];


                            Project p = Main.manager.getProjects().get(tName);

                            for (OpenProject o : Main.sceneManager.getOpenProjects()) {

                                if (o.getProject().equals(p)) {
                                    return;
                                }

                            }
                            if (p != null) {
                                Tab tab = new Tab(p.getName());
                                CodeArea area = new CodeArea();
                                tab.setContent(area);
                                area.appendText(p.getCurentCode());
                                codeTabPane.getTabs().add(tab);

                                OpenProject op = new OpenProject(tab, p, area, prDependList, codeTabPane, commandSendBtn);
                                Main.sceneManager.getOpenProjects().add(op);
                                if(Main.saver == null) {
                                    Main.saver = new AutoSaver();

                                }
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
                    OpenProject tOp = null;
                    for (OpenProject op : Main.sceneManager.getOpenProjects()) {

                        if (op.getTab().equals(tab)) {
                            tOp = op;
                        }
                    }

                    if (tab != null) {
                        String name = tab.getText();


                        if(tOp == null) {
                            return;
                        }
                        CompleteList cl = CompleteList.getCurrentInstance();
                        if (cl != null && cl.win.isShowing()) {
                            cl.win.hide();
                        }

                        pathLabel.setText(tOp.getProject().getConfFile().getAbsolutePath());
                        prNameLbl.setText("Name: " + tOp.getProject().getName());
                        prServerLbl.setText("Server: " /* TODO get server name */);
                        prSkVersionLbl.setText("Skript version: " + tOp.getProject().getSkript().getVersion());
                        prNotesArea.setText(tOp.getProject().getNotes());


                    }
                }
        );
        searchTxTField.setOnKeyReleased(event -> {
            Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
            OpenProject tOp = null;
            for (OpenProject op : Main.sceneManager.getOpenProjects()) {

                if (op.getTab().equals(tab)) {
                    tOp = op;
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

}
