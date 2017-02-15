package com.skriptide.gui;

import com.skriptide.Main;
import com.skriptide.gui.controller.*;
import com.skriptide.util.IDESettings;
import com.skriptide.util.skunityapi.SkUnityAPI;
import com.skriptide.util.systemutils.OperatingSystemType;
import com.skriptide.util.systemutils.OsUtils;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Created by yannh on 27.01.2017.
 */
public class SceneManager extends Application {




    private IdeGuiController mainController;
    private static final int SPLASH_WIDTH = 674;
    private static final int SPLASH_HEIGHT = 393;
    public static OpenFile selected;
    public static SceneManager sceneManager;
    private boolean isNew = false;
    private Pane splashLayout;

    public HashMap<GuiType, Gui> getActiveGuis() {
        return activeGuis;
    }

    public List<OpenProject> getOpenFiles() {
        return openFiles;
    }

    private ProgressBar loadProgress;
    private Label progressText;
    private HashMap<GuiType, Gui> activeGuis;
    private List<OpenProject> openFiles;

    public SceneManager() {

        activeGuis = new HashMap<>();
        openFiles = new ArrayList<>();
        sceneManager = this;


    }

    public void start() {

        launch();
    }

    @Override
    public void init() {
        ImageView splash = new ImageView(new Image(
                getClass().getResource("/splash.png").toExternalForm()));

        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(SPLASH_WIDTH);
        progressText = new Label("Loading...");
        progressText.setTextAlignment(TextAlignment.LEFT);
        splashLayout = new Pane();
        splashLayout.setPrefSize(674, 393);
        splash.setFitWidth(674);
        splash.setFitHeight(393);
        loadProgress.setPrefSize(400, 15);
        loadProgress.setLayoutX(255);
        loadProgress.setLayoutY(280);
        progressText.setPrefSize(320, 15);
        progressText.setLayoutX(430);
        progressText.setLayoutY(300);
        splashLayout.getChildren().addAll(splash, loadProgress, progressText);
        progressText.setAlignment(Pos.CENTER);

        splashLayout.setEffect(new DropShadow());
    }

    @Override
    public void start(Stage initStage) throws Exception {


        final Task<Runnable> load = new Task<Runnable>() {
            @Override
            protected Runnable call() throws Exception {

                Parameters params = getParameters();
                List<String> parameters = params.getRaw();

                updateProgress(0, 100);
                updateMessage("Checking the SkUnityAPI...");
                new SkUnityAPI();
                Thread.sleep(50);
                updateProgress(20, 100);
                updateMessage("Checking config files...");
                isNew = Main.manager.checkFiles();

                Thread.sleep(50);

                if (!isNew) {
                    updateMessage("loading in projects...");
                    Main.manager.loadIn();
                    Thread.sleep(50);
                }
                updateProgress(60, 100);
                updateMessage("Loading gui...");

                Thread.sleep(50);

                return null;
            }


        };

        showSplash(
                initStage,
                load,
                this::showMainStage);
        new Thread(load).start();
    }

    private void showSplash(
            final Stage initStage,
            Task<?> task,
            InitCompletionHandler initCompletionHandler
    ) {
        progressText.textProperty().bind(task.messageProperty());
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            }
        });

        Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.initStyle(StageStyle.TRANSPARENT);
        initStage.setAlwaysOnTop(true);
        initStage.show();
    }

    private void showMainStage() {

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent parent = null;
        try {
            parent = loader.load(getClass().getResourceAsStream("/IdeGui.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        Scene scene = new Scene(parent);
        stage.sizeToScene();
        stage.setTitle("SkriptIDE");
        stage.centerOnScreen();
        scene.getStylesheets().add("DarkHighlighting.css");
        if (OsUtils.getOS() == OperatingSystemType.LINUX) {
            scene.getStylesheets().add("os_styles/LinuxSheet.css");

        }
        if (OsUtils.getOS() == OperatingSystemType.WINDOWS) {
            scene.getStylesheets().add("os_styles/WindowsSheet.css");

        }
        if (OsUtils.getOS() == OperatingSystemType.OSX) {

        }
        stage.setScene(scene);
        IdeGuiController controller = loader.getController();
        controller.initGui();
        this.mainController = controller;

        stage.setOnCloseRequest(event -> {
            for(OpenProject pr : Main.sceneManager.getOpenFiles()) {

                for(OpenFile file : pr.getOpenFiles().values()) {
                    pr.getProject().writeCode(file.getArea().getText(), file.getProject().getName());
                }
                System.exit(0);
            }
        });
        stage.show();


        if(isNew) {
            openWelcomeGui();
        }


        Main.settings = new IDESettings();
        Main.settings.loadIn();
    }

    public void openCreateProjectGui() {

        if(activeGuis.containsKey(GuiType.CREATE_PROJECT)) {

            CreateProjectGuiController c = activeGuis.get(GuiType.CREATE_PROJECT).getController();
            c.initGui();

            activeGuis.get(GuiType.CREATE_PROJECT).getStage().show();
            return;
        }

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        try {
            root = loader.load(getClass().getResourceAsStream("/CreateProjectGui.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        CreateProjectGuiController controller = loader.getController();
        controller.initGui();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle("Create Project");
        stage.centerOnScreen();
        stage.setResizable(false);

        stage.show();

        activeGuis.put(GuiType.CREATE_PROJECT, new Gui(stage, GuiType.CREATE_PROJECT, loader, controller, root));
    }
    public void openServerProjectGui() {

        if(activeGuis.containsKey(GuiType.CREATE_SERVER)) {

            CreateServerGuiController c = activeGuis.get(GuiType.CREATE_SERVER).getController();
            c.initGui();

            activeGuis.get(GuiType.CREATE_SERVER).getStage().show();
            return;
        }

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        try {
            root = loader.load(getClass().getResourceAsStream("/CreateServerGui.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        CreateServerGuiController controller = loader.getController();
        controller.initGui();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle("Create Server");
        stage.centerOnScreen();
        stage.setResizable(false);

        stage.show();

        activeGuis.put(GuiType.CREATE_SERVER, new Gui(stage, GuiType.CREATE_SERVER, loader, controller, root));

    }
    public void openWelcomeGui() {

        if(activeGuis.containsKey(GuiType.WELCOME)) {

            activeGuis.get(GuiType.WELCOME).getStage().show();
            return;
        }

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        try {
            root = loader.load(getClass().getResourceAsStream("/StartGui.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StartGuiController controller = loader.getController();
        controller.initGui();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle("Welcome to SkriptIDE");
        stage.centerOnScreen();
        stage.setResizable(false);

        stage.show();

        activeGuis.put(GuiType.WELCOME, new Gui(stage, GuiType.WELCOME, loader, controller, root));


    }

    public void openManageVersions() {

        if(activeGuis.containsKey(GuiType.MANAGE_VERSIONS)) {

            activeGuis.get(GuiType.MANAGE_VERSIONS).getStage().show();
            ManageAddsGuiController c = activeGuis.get(GuiType.MANAGE_VERSIONS).getController();
            c.setLists();
            return;
        }

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        try {
            root = loader.load(getClass().getResourceAsStream("/ManageAddsGui.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ManageAddsGuiController controller = loader.getController();
        controller.setLists();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle("Manage Versions");
        stage.centerOnScreen();
        stage.setResizable(false);

        stage.show();

        activeGuis.put(GuiType.MANAGE_VERSIONS, new Gui(stage, GuiType.MANAGE_VERSIONS, loader, controller, root));


    }


    public void openExportSettings() {

        if(activeGuis.containsKey(GuiType.EXPORT_SETTING)) {

            activeGuis.get(GuiType.EXPORT_SETTING).getStage().show();
            return;
        }

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        try {
            root = loader.load(getClass().getResourceAsStream("/ExportSettingsGui.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ExportSettingsGuiController controller = loader.getController();
        controller.initGui();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle("Manage SFTP Hosts");
        stage.centerOnScreen();
        stage.setResizable(false);

        stage.show();

        activeGuis.put(GuiType.EXPORT_SETTING, new Gui(stage, GuiType.EXPORT_SETTING, loader, controller, root));
    }
    public void openSettingsGui() {

        if(activeGuis.containsKey(GuiType.SETTINGS)) {

            activeGuis.get(GuiType.SETTINGS).getStage().show();
            return;
        }

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        try {
            root = loader.load(getClass().getResourceAsStream("/SettingsGui.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SettingsGuiController controller = loader.getController();
        controller.init();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle("Settings");
        stage.centerOnScreen();
        stage.setResizable(false);

        stage.show();

        activeGuis.put(GuiType.SETTINGS, new Gui(stage, GuiType.SETTINGS, loader, controller, root));
    }
    public void openManageServer() {

        if(activeGuis.containsKey(GuiType.MANAGE_SERVERS)) {

            activeGuis.get(GuiType.MANAGE_SERVERS).getStage().show();

            ManageTestServerGuiController c = activeGuis.get(GuiType.MANAGE_SERVERS).getController();
            c.initGui();
            return;
        }

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        try {
            root = loader.load(getClass().getResourceAsStream("/ManageTestServerGui.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ManageTestServerGuiController controller = loader.getController();
        controller.initGui();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle("Manage Servers");
        stage.centerOnScreen();
        stage.setResizable(false);

        stage.show();

        activeGuis.put(GuiType.MANAGE_SERVERS, new Gui(stage, GuiType.MANAGE_SERVERS, loader, controller, root));
    }
    private interface InitCompletionHandler {
        void complete();
    }

    public boolean infoCheck(String title, String header, String body, Alert.AlertType type) {

        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(body);
        alert.setResizable(false);
        alert.setGraphic(null);


        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;

    }
}
