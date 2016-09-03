package com.skriptide.guis.idegui;

import com.skriptide.codemanage.CodeReader;
import com.skriptide.codemanage.CodeWriter;
import com.skriptide.codemanage.ControlMain;
import com.skriptide.codemanage.Supers;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import com.sun.org.apache.bcel.internal.generic.AALOAD;
import com.skriptide.guis.SceneManager;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.NavigationActions;
import org.fxmisc.richtext.PopupAlignment;
import org.fxmisc.richtext.StyleClassedTextArea;
import com.skriptide.util.MCServer;
import com.skriptide.util.Project;
import com.skriptide.util.skunityapi.*;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Liz3ga on 27.07.2016.
 */
public class IdeGuiController {

    SceneManager sceneManager = new SceneManager();


    @FXML
    public TabPane codeTabPane;
    @FXML
    public Label searchLabel;
    @FXML
    public TextArea consoleOutputTextArea;
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
    private ListView<String> chooseView;
    private boolean showList;
    private Popup win;
    private ArrayList<String> all = new ArrayList<>();
    private int pos = 0;

    private static SkUnityAPI skUnity = new SkUnityAPI();

    private void setList() {


        if (win == null) {
            win = new Popup();
            chooseView = new ListView();
        }

        ArrayList<ApiCondition> conditions = skUnity.getConditions();
        ArrayList<ApiEffect> effects = skUnity.getEffects();
        ArrayList<ApiEvent> events = skUnity.getEvents();
        ArrayList<ApiExpression> expressions = skUnity.getExpressions();
        ArrayList<ApiType> types = skUnity.getTypes();
        for (int i = 1; i != conditions.size(); i++) {
            chooseView.getItems().add(conditions.get(i).getId() + " Condition");
        }
        for (int i = 0; i != effects.size(); i++) {
            chooseView.getItems().add(effects.get(i).getId() + " Effect");
        }
        for (int i = 0; i != events.size(); i++) {
            chooseView.getItems().add(events.get(i).getId() + " Event");
        }
        for (int i = 0; i != expressions.size(); i++) {
            chooseView.getItems().add(expressions.get(i).getId() + " Expression");
        }
        for (int i = 0; i != types.size(); i++) {
            chooseView.getItems().add(types.get(i).getId() + " Type");
        }

        chooseView.getItems().addAll(new Supers().getSupervArray());

        chooseView.setPrefSize(180, 200);
        Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
        CodeArea area = (CodeArea) tab.getContent();
        win.getContent().add(chooseView);

        area.setPopupWindow(win);


        area.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (showList) {

                    win.hide();
                    chooseView.setVisible(false);
                    showList = false;

                }
            }
        });


    }

    public void chooseList() {
        Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
        CodeArea area = (CodeArea) tab.getContent();

        if (win == null) {
            setList();

        }


        if (!win.isShowing()) {

            Stage stage = (Stage) commandSendBtn.getScene().getWindow();


            javafx.application.Platform.runLater(() -> {
                        int caretPosition = area.getCaretPosition();
                        System.out.println(pos);
                        if (0 != caretPosition) {
                            area.positionCaret(caretPosition - 1);
                            area.positionCaret(caretPosition);
                        } else {
                            area.positionCaret(caretPosition + 1);
                            area.positionCaret(caretPosition);
                        }


                    }

            );
            area.setPopupAlignment(PopupAlignment.CARET_BOTTOM);
            win.show(stage);
            chooseView.setVisible(true);
            showList = true;


        }
        updateList();


    }

    public void updateList() {

        Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
        CodeArea area = (CodeArea) tab.getContent();


        area.caretPositionProperty().addListener((obs, oldPosition, newPosition) -> {

            String text = area.getText().substring(0, newPosition.intValue());
            int index;
            for (index = text.length() - 1; index >= 0 && !Character.isWhitespace(text.charAt(index)); index--) ;
            String prefix = text.substring(index + 1, text.length());

            if (chooseView.isVisible()) {
                javafx.application.Platform.runLater(() -> {


                            for (String str : chooseView.getItems().sorted()) {

                                if (!str.toLowerCase().contains(prefix.toLowerCase())) {
                                    chooseView.getItems().remove(str);
                                    all.add(str);
                                    chooseView.refresh();
                                }
                            }
                            ArrayList<String> toRemove = new ArrayList<String>();
                            for (int i = 0; i != all.size(); i++) {
                                String current = all.get(i);
                                if (current.toLowerCase().contains(prefix.toLowerCase())) {
                                    chooseView.getItems().add(current);
                                    toRemove.add(current);
                                    chooseView.refresh();
                                }
                            }
                            for (int i = 0; i != toRemove.size(); i++) {
                                String rem = toRemove.get(i);
                                if (all.contains(rem)) {
                                    all.remove(rem);
                                }
                            }

                        }


                );
            }
            chooseView.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    KeyCode code = event.getCode();

                    if (code == KeyCode.ESCAPE) {

                        System.out.println("casted");
                        if (showList) {

                            win.hide();
                            chooseView.setVisible(false);
                            showList = false;

                        }
                    } else if (code == KeyCode.ENTER) {
                        if (showList) {

                            setWord(prefix);

                        }
                    }
                }
            });
            chooseView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {


                    setWord(prefix);

                }

            });

        });


    }

    private void setWord(String prefix) {


        Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
        CodeArea area = (CodeArea) tab.getContent();
        String seletion = chooseView.getSelectionModel().getSelectedItem();
        String before = "";
        if (seletion.contains("Event")) {

            String trueT = "";
            String[] split = seletion.split(" ");
            String[] r = split[0].split("(?=\\p{Upper})");
            for (int i = 0; i != r.length; i++) {
                trueT = trueT + " " + r[i];
            }


            before = area.getText(area.getCaretPosition(), area.getCaretPosition() - prefix.length() + trueT.trim().length() * 2);


            area.replaceText(area.getCaretPosition() - prefix.length(), area.getCaretPosition() - prefix.length() + trueT.trim().length() * 2, trueT.trim() + ":\n        " + before);


            pos = area.getCaretPosition() - before.length();

        } else {
            String trueT = "";
            String[] split = seletion.split(" ");
            trueT = split[0];


            before = area.getText(area.getCaretPosition(), area.getCaretPosition() - prefix.length() + trueT.trim().length() * 2);

            area.replaceText(area.getCaretPosition() - prefix.length(), area.getCaretPosition() - prefix.length() + trueT.trim().length() * 2, trueT.trim() + " " + before);


            pos = area.getCaretPosition() - before.length();
        }
        System.out.println(pos);
        area.positionCaret(pos);


        if (showList) {
            win.hide();
            chooseView.setVisible(false);
            showList = false;


        }
    }

    public void setConsoleArea() {

        SceneManager.consoleOut = consoleOutputTextArea;
        SceneManager.runningServerLabel = runningServerLabel;
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
    }

    public void loadInProjects() {

        ObservableList<Project> projects = Project.getProjects();


        projectsList.getItems().clear();
        for (Project project : projects.sorted()) {
            if (project.getName() != null) {
                projectsList.getItems().addAll(project.getName());
            }
        }

    }

    public void loadInServers() {

        serverListComboBox.getItems().setAll();
        ObservableList<MCServer> servers = MCServer.getAllServers();
        for (MCServer srv : servers.sorted()) {
            serverListComboBox.getItems().add(srv.getname());
        }

    }

    public void startServer() {

        ObservableList<MCServer> servers = MCServer.getAllServers();
        for (MCServer srv : servers.sorted()) {
            if (srv.getname().equalsIgnoreCase(serverListComboBox.getSelectionModel().getSelectedItem())) {
                srv.startServer();
            }
        }
    }

    public void newProject() {

        sceneManager.openCreateProject();


    }

    public void newServer() {

        sceneManager.openCreateServer();

    }

    public void manageAddons() throws IOException {

        sceneManager.openManageVersions();

    }

    public void manageServers() {

        sceneManager.openManageServer();
    }

    public void manageCode() {

        Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
        CodeArea area = (CodeArea) tab.getContent();


        //  area.setText(ControlMain.controlCode(area.getText()));
    }


    public void openProject() {
        CodeArea area = new CodeArea();

        area.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                KeyCode code = event.getCode();


                chooseList();


            }
        });

        String selection = projectsList.getSelectionModel().getSelectedItem();


        Project project = new Project(selection);
        if (!SceneManager.openProjects.contains(project.getName())) {
            Tab tab = new Tab(project.getName());

            codeTabPane.getTabs().add(tab);

            tab.setContent(area);

            File skript = new File(project.getSkriptPath().replace("/", "/"));


            CodeReader reader = new CodeReader(skript);

            area.appendText(reader.getCode());


            SceneManager.openProjects.add(project.getName());

            ControlMain.controlCode(area);


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
    }


}
