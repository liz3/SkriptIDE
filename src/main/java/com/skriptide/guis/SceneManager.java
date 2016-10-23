package com.skriptide.guis;

import com.skriptide.codemanage.CompleteList;
import com.skriptide.guis.createprojectgui.CreateProjectGuiController;
import com.skriptide.guis.createserver.CreateServerGuiController;
import com.skriptide.guis.extension.ManageExtensionsController;
import com.skriptide.guis.idegui.IdeGuiController;

import com.skriptide.guis.manageadds.ManageAddsGuiController;
import com.skriptide.guis.manageserver.ManageServerController;
import com.skriptide.guis.settings.SettingsController;
import com.skriptide.guis.startgui.StartGuiController;
import com.skriptide.main.Main;
import com.skriptide.util.ConfigManager;
import com.skriptide.util.IDESystemErr;
import com.skriptide.util.IDESystemOut;
import com.skriptide.util.MCServer;
import com.skriptide.util.skunityapi.SkUnityAPI;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.fxmisc.richtext.CodeArea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.skriptide.main.Main.debugMode;

/**
 * Created by Liz3ga on 27.07.2016.
 */
public class SceneManager extends Application {


    public static final ArrayList<String> openProjects = new ArrayList<>();
    public static CodeArea consoleOut;
    public static MCServer runningServer;
    public static Label runningServerLabel;
    public static ListView projectsList;
    public static ComboBox<String> runninServerList;
    public static TextArea debugArea;
    public static ProgressBar procBar;
    private boolean fast = false;
    private boolean v;
    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private static final int SPLASH_WIDTH = 600;
    private static final int SPLASH_HEIGHT = 300;
    private Stage mainWindow;
    private Stage createNewProjectWindow;
    private Stage createNewServerWindow;
    private Stage addsManager;
    private Stage manageServer;
    private Stage debugger;
    private Stage settings;
    private final FXMLLoader mainLoader = new FXMLLoader();
    private final FXMLLoader welcomeLoader = new FXMLLoader();
    private final FXMLLoader createNewProjectLoader = new FXMLLoader();
    private final FXMLLoader createNewServerLoader = new FXMLLoader();
    private final FXMLLoader addManagerLoader = new FXMLLoader();
    private final FXMLLoader manageServerLoader = new FXMLLoader();
    private final FXMLLoader debuggerLoader = new FXMLLoader();
    private final FXMLLoader settingsLoader = new FXMLLoader();
    private Parent mainParent = null, welcomeWindowParent = null, createNewProjectWindowParent = null, createNewServerWindowParent = null, addsManagerParent = null, manageServerParent = null, debuggerParent = null, settingsParent = null;
    private CreateProjectGuiController createProjectGuiController;
    private CreateServerGuiController createServerGuiController;
    private IdeGuiController ideGuiController;
    private ManageServerController manageServerController;
    private DebuggerController debuggerController;
    private SettingsController settingsController;



    public SceneManager() {
    }


    public static void cleanUP() {

        runningServer = null;

        consoleOut.clear();


    }

    public static void main(String[] args) throws Exception {


        launch(args);
    }

    @Override
    public void init() {
        ImageView splash = new ImageView(new Image(
                getClass().getResource("/splash.png").toExternalForm()));

        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(SPLASH_WIDTH);
        progressText = new Label("Will find friends for peanuts . . .");
        splashLayout = new Pane();
        splashLayout.setPrefSize(600, 300);
        splash.setFitWidth(600);
        splash.setFitHeight(300);
        loadProgress.setPrefSize(320, 10);
        loadProgress.setLayoutX(270);
        loadProgress.setLayoutY(190);
        progressText.setPrefSize(320, 15);
        progressText.setLayoutX(270);
        progressText.setLayoutY(210);
        splashLayout.getChildren().addAll(splash, loadProgress, progressText);
        progressText.setAlignment(Pos.CENTER);

        splashLayout.setEffect(new DropShadow());
    }

    @Override
    public void start(final Stage initStage) throws Exception {

        final String[] path = {""};
        final Task<Runnable> load = new Task<Runnable>() {
            @Override
            protected Runnable call() throws Exception {

                Parameters params = getParameters();
                List<String> parameters = params.getRaw();
                if (parameters.size() != 0) {
                    String cp = parameters.get(0);
                    if (cp.equals("-fast"))
                        fast = true;
                    else
                        path[0] = parameters.get(0);

                }
                updateMessage("Checking the SkUnityAPI...");
                updateProgress(15, 100);

                setSkyUnityApi();

                updateMessage("Loading configuration...");
                updateProgress(35, 100);

                v = checkConfig();

                updateMessage("Loading Gui...");
                updateProgress(65, 100);

                if (!fast)
                    Thread.sleep(2500);


                return null;
            }


        };

        showSplash(
                initStage,
                load,
                () -> showMainStage(path[0])
        );
        new Thread(load).start();
    }

    private void showMainStage(String path) {

        mainWindow = new Stage();


        try {
            mainParent = mainLoader
                    .load(getClass().getResourceAsStream("/IdeGui.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        Scene mainScene = new Scene(mainParent, 980, 550);
        mainScene.getStylesheets().add("Highlighting.css");
        mainScene.getStylesheets().add("Style.css");
        mainWindow.setTitle("SkriptIDE");
        mainWindow.getIcons().add(new Image("http://www.mediafire.com/convkey/9377/kw4v8cwmcocs6b5zg.jpg?size_id=3"));
        mainWindow.setScene(mainScene);
        mainWindow.setMinWidth(980);
        mainWindow.setMinHeight(550);
        mainWindow.centerOnScreen();
        mainWindow.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                CompleteList cl = CompleteList.getCurrentInstance();
                if (cl != null && cl.win.isShowing()) {
                    cl.win.hide();
                }
            }
        });
        ideGuiController = mainLoader.getController();
        ideGuiController.sceneManager = this;
        ideGuiController.setUpWin();
        if (!v) {
            ideGuiController.loadInProjects();
        }
        if (!path.equals("")) {

            ideGuiController.openFromFile(path);
        }



	/*For dark theme.
    ThemeCreator.setTheme(mainScene, new Dark());
	*/


        mainScene.getWindow().setOnCloseRequest(ev -> {

            if (ideGuiController.codeTabPane.getTabs().size() != 0) {
                int t = 0;
                for (Tab tab : ideGuiController.codeTabPane.getTabs()) {
                    String name = tab.getText();


                    if (!name.contains("External: ")) {
                        t++;
                    }
                }
                if (t != 0) {

                    boolean save = infoCheck("Save", "Save Projects", "Do you want to save: " + t + " projects before close?");
                    if (save) {
                        ideGuiController.saveOpenProjects();
                    }
                }

            }
            if (runningServer != null) {
                boolean save = infoCheck("Stop Server", "Stop running Server", "The server: " + runningServer.getname() + " is running, stop the server? Otherwise the process will be killed!");
                if (save) {
                    ideGuiController.comandSendTextField.setText("stop");
                    ideGuiController.sendCommand();
                    Thread t = new Thread(() -> {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Platform.exit();
                        System.exit(0);
                    });
                    t.start();
                } else {
                    runningServer = null;
                    Platform.exit();
                    System.exit(0);
                }
            } else {
                Platform.exit();
                System.exit(0);
            }


        });


        startUp(v);
    }

    private void showSplash(
            final Stage initStage,
            Task<?> task,
            SceneManager.InitCompletionHandler initCompletionHandler
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
            } // todo add code to gracefully handle other task states.
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

    public interface InitCompletionHandler {
        void complete();
    }

    private void setSkyUnityApi() {


        new SkUnityAPI();
        System.out.println("api done");


    }

    public void openSettings() {

        if (settings == null) {

            settings = new Stage();
            try {
                settingsParent = settingsLoader.load(getClass().getResourceAsStream("/SettingsGui.fxml"));

                Scene scene = new Scene(settingsParent, 705, 495);

                settings.setScene(scene);
                settings.setResizable(false);
                settings.initStyle(StageStyle.UTILITY);
                settings.setTitle("SkriptIDE Settings");
                settings.centerOnScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        settings.show();
    }

    private boolean checkConfig() {


        int configState = ConfigManager.checkConfig();


        if (configState == 1) {

            if (debugMode) {
                System.out.println("Opening Welcome Screen");
            }

            return true;

        }
        if (configState == 0) {

            if (debugMode) {
                System.out.println("Trying to loading projects to mainGui");
            }

            return false;
        }
        System.out.println("config done");


        return true;
    }

    private void startUp(boolean v) {

        mainWindow.show();
        if (v) {

            openWelcomeGui();
        }

        if (ConfigManager.isDebug()) {
            Main.debugMode = true;
            openDebugger();
        }

    }

    public boolean infoCheck(String title, String header, String body) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(body);
        alert.setResizable(false);
        alert.setGraphic(null);


        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;

    }

    public void openDebugger() {


        if (debuggerParent == null) {
            debugger = new Stage();
            try {
                debuggerParent = debuggerLoader.load(getClass().getResourceAsStream("/DebuggerGui.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene debuggerSc = new Scene(debuggerParent, 800, 600);
            debuggerSc.getStylesheets().add("Debugger.css");
            debugger.setScene(debuggerSc);
            debugger.initStyle(StageStyle.UTILITY);
            debugger.setTitle("Skript IDE Debugger");

            debugger.centerOnScreen();
            debuggerController = debuggerLoader.getController();


        }

        debugger.show();
        debuggerController.setOut();
        System.setOut(new IDESystemOut());
        System.setErr(new IDESystemErr());

        if (debugMode) {
            System.out.println("loaded debugger window");
        }

    }

    private void openWelcomeGui() {


        Stage welcomeWindow = new Stage();


        try {
            welcomeWindowParent = welcomeLoader.load(getClass().getResourceAsStream("/StartGui.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        welcomeWindow.initOwner(mainWindow);
        welcomeWindow.setScene(new Scene(welcomeWindowParent, 400, 330));
        welcomeWindow.setResizable(false);
        welcomeWindow.centerOnScreen();
        welcomeWindow.initStyle(StageStyle.UTILITY);
        welcomeWindow.show();
        if (debugMode) {
            System.out.println("loaded welcome screen");
        }
        if (debugMode) {
            System.out.println("Welcome screen open");
        }

        StartGuiController startGuiController = welcomeLoader.getController();

        String username = System.getProperty("user.name");

        startGuiController.setValues();
        startGuiController.projectsPathField.setText("C:\\Users\\" + username + "\\Documents\\ScriptIDE\\Projects\\");

        startGuiController.serverPathField.setText("C:\\Users\\" + username + "\\Documents\\ScriptIDE\\Servers\\");
        if (debugMode) {

            System.out.println("Set preset pathes");
        }


    }

    public void openCreateProject() {

        if (createNewProjectWindowParent == null) {
            createNewProjectWindow = new Stage();
            try {
                createNewProjectWindowParent = createNewProjectLoader.load(getClass().getResourceAsStream("/CreateProjectGui.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            createNewProjectWindow.initOwner(mainWindow);
            createNewProjectWindow.setTitle("Create new Project");
            createNewProjectWindow.setScene(new Scene(createNewProjectWindowParent, 580, 300));
            createNewProjectWindow.setResizable(false);
            createNewProjectWindow.initStyle(StageStyle.UTILITY);
            createProjectGuiController = createNewProjectLoader.getController();
        }

        createNewProjectWindow.show();
        if (debugMode) {
            System.out.println("open create project");
        }

        createProjectGuiController.setValues();


    }

    public void openCreateServer() {

        if (createNewServerWindow == null) {
            createNewServerWindow = new Stage();
            try {
                createNewServerWindowParent = createNewServerLoader.load(getClass().getResourceAsStream("/CreateServerGui.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            createNewServerWindow.initOwner(mainWindow);
            createNewServerWindow.setTitle("Create new Server");
            createNewServerWindow.setScene(new Scene(createNewServerWindowParent, 545, 318));
            createNewServerWindow.setResizable(false);
            createNewServerWindow.initStyle(StageStyle.UTILITY);
            createServerGuiController = createNewServerLoader.getController();
        }


        createNewServerWindow.show();

        if (debugMode) {
            System.out.println("create server window open");
        }
        createServerGuiController.setValues();


    }

    public void openManageVersions() throws IOException {


        if (addsManagerParent == null) {

            addsManager = new Stage();

            addsManagerParent = addManagerLoader.load(getClass().getResourceAsStream("/ManageAddsGui.fxml"));
            addsManager.initOwner(mainWindow);
            addsManager.setTitle("Manage Addons");
            addsManager.initStyle(StageStyle.UTILITY);
            addsManager.setScene(new Scene(addsManagerParent, 670, 455));
            addsManager.setResizable(false);
            ManageAddsGuiController manageAddsGuiController;
            manageAddsGuiController = addManagerLoader.getController();
            manageAddsGuiController.setLists();
        }


        addsManager.show();
        if (debugMode) {
            System.out.println("Manage adds window open");
        }
    }
    //Extensions
    private final FXMLLoader manageExtensionsLoader = new FXMLLoader();
    private Stage manageExtensions;
    private Parent manageExtensionParent = null;
    private ManageExtensionsController manageExtensionsController;

    public void openManageExtensions() throws IOException {


        if (manageExtensionParent == null) {
            manageExtensions = new Stage();
            try {
                manageExtensionParent = manageExtensionsLoader.load(getClass().getResourceAsStream("/ManageExtensionsGui.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            manageExtensions.initOwner(mainWindow);
            manageExtensions.setTitle("Manage Extensions");
            manageExtensions.setScene(new Scene(manageExtensionParent, 226, 467));
            manageExtensions.setResizable(false);
            manageExtensions.initStyle(StageStyle.UTILITY);
            manageExtensionsController = manageExtensionsLoader.getController();
        }


        manageExtensions.show();

        if (debugMode) {
            System.out.println("create server window open");
        }




    }

    public void openManageServer() {

        if (manageServerParent == null) {

            manageServer = new Stage();

            try {

                manageServerParent = manageServerLoader.load(getClass().getResourceAsStream("/ManageTestServerGui.fxml"));


            } catch (IOException e) {
                e.printStackTrace();
            }
            manageServer.initOwner(mainWindow);
            manageServer.setTitle("Manage Servers");
            manageServer.setScene(new Scene(manageServerParent, 820, 550));
            manageServer.initStyle(StageStyle.UTILITY);
            manageServer.setResizable(false);
            manageServer.centerOnScreen();
            manageServerController = manageServerLoader.getController();
        }


        manageServer.show();
        if (debugMode) {
            System.out.println("open manage server!");
        }
        manageServerController.setValues();
    }

}


