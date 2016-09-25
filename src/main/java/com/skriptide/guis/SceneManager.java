package com.skriptide.guis;

import com.skriptide.guis.createprojectgui.CreateProjectGuiController;
import com.skriptide.guis.createserver.CreateServerGuiController;
import com.skriptide.guis.idegui.IdeGuiController;

import com.skriptide.guis.manageadds.ManageAddsGuiController;
import com.skriptide.guis.manageserver.ManageServerController;
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
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.fxmisc.richtext.CodeArea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.regex.Pattern;

import static com.skriptide.main.Main.debugMode;

/**
 * Created by Liz3ga on 27.07.2016.
 */
public class SceneManager extends Application {


    public static ArrayList<String> openProjects = new ArrayList<>();
    public static CodeArea consoleOut;
    public static MCServer runningServer;
    public static Label runningServerLabel;
    public static ListView projectsList;
    public static ComboBox<String> runninServerList;
    public static TextArea debugArea;
    public static ProgressBar procBar;
    private boolean v;
    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private Stage mainStage;
    private static final int SPLASH_WIDTH = 600;
    private static final int SPLASH_HEIGHT = 300;
    public Stage mainWindow, welcomeWindow, createNewProjectWindow, createNewServerWindow, addsManager, manageServer, debugger, info, splash;
    private FXMLLoader mainLoader = new FXMLLoader(), welcomeLoader = new FXMLLoader(), createNewProjectLoader = new FXMLLoader(), createNewServerLoader = new FXMLLoader(), addManagerLoader = new FXMLLoader(), manageServerLoader = new FXMLLoader(), debuggerLoader = new FXMLLoader(), splashLoader = new FXMLLoader();
    private Parent mainParent = null, welcomeWindowParent = null, createNewProjectWindowParent = null, createNewServerWindowParent = null, addsManagerParent = null, manageServerParent = null, debuggerParent = null, splashParent = null;
    private CreateProjectGuiController createProjectGuiController;
    private CreateServerGuiController createServerGuiController;
    private IdeGuiController ideGuiController;
    private ManageAddsGuiController manageAddsGuiController;
    private StartGuiController startGuiController;
    private ManageServerController manageServerController;

    DebuggerController debuggerController;


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

        int mode = 0;
        final String[] path = {""};
        final Task<Runnable> load = new Task<Runnable>() {
            @Override
            protected Runnable call() throws Exception {

                Parameters params = getParameters();
                List<String> parameters = params.getRaw();
                for (int i = 0; i != parameters.size(); i++) {

                    String current = parameters.get(i);
                    System.out.println(current);
                    if(current.equalsIgnoreCase("-file")) {
                        String value = parameters.get(i + 1);
                        path[0] = value;
                        System.out.println(path[0]);
                    }


                }
                updateMessage("Checking the SkUnityAPI...");
                updateProgress(15, 100);

                setSkyUnityApi();

                updateMessage("Loading configuration...");
                updateProgress(35, 100);

                v = checkConfig();

                updateMessage("Loading Gui...");
                updateProgress(65, 100);

                if (ConfigManager.isDebug()) {
                    Main.debugMode = true;
                    openDebugger();
                }
                Thread.sleep(2500);


                return null;
            }


        };

        showSplash(
                initStage,
                load,
                () -> showMainStage(mode, path[0])
        );
        new Thread(load).start();
    }

    private void showMainStage(int mode, String path) {

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


        mainScene.getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent ev) {

                if (ideGuiController.codeTabPane.getTabs().size() != 0) {
                    int t = 0;
                    for(Tab tab : ideGuiController.codeTabPane.getTabs()) {
                        String name = tab.getText();


                        if(!name.contains("External: ")) {
                            t++;
                        }
                    }
                    if( t != 0) {

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
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(10000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Platform.exit();
                                System.exit(0);
                            }
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


    public void setWindow() {


    }

    public void setSkyUnityApi() {


        SkUnityAPI api = new SkUnityAPI();
        System.out.println("api done");


    }

    public boolean checkConfig() {


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

    public void startUp(boolean v) {

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
        if (result.get() == ButtonType.OK) {
            return true;
        }

        return false;
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

            debugger.setTitle("Skript IDE Debugger");

            debugger.centerOnScreen();
            debuggerController = debuggerLoader.getController();


        }

        debugger.show();
        debuggerController.setOut();
        System.setOut(new IDESystemOut(System.out));
        System.setErr(new IDESystemErr(System.err));

        if (debugMode) {
            System.out.println("loaded debugger window");
        }

    }

    public void openWelcomeGui() {


        welcomeWindow = new Stage();


        try {
            welcomeWindowParent = welcomeLoader.load(getClass().getResourceAsStream("/StartGui.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        welcomeWindow.initOwner(mainWindow);
        welcomeWindow.setScene(new Scene(welcomeWindowParent, 400, 330));
        welcomeWindow.setResizable(false);
        welcomeWindow.centerOnScreen();
        welcomeWindow.show();
        if (debugMode) {
            System.out.println("loaded welcome screen");
        }
        if (debugMode) {
            System.out.println("Welcome scrren open");
        }

        startGuiController = welcomeLoader.getController();

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
            createNewProjectWindow.setScene(new Scene(createNewProjectWindowParent, 560, 280));
            createNewProjectWindow.setResizable(false);

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

            createServerGuiController = createNewServerLoader.getController();
        }


        createNewServerWindow.show();

        if (debugMode) {
            System.out.println("create server window open");
        }
        createServerGuiController.setValues();


    }

    public void openManageVersions() throws IOException {


        if (addsManagerParent == null || addManagerLoader == null) {

            addsManager = new Stage();

            addsManagerParent = addManagerLoader.load(getClass().getResourceAsStream("/ManageAddsGui.fxml"));
            addsManager.initOwner(mainWindow);
            addsManager.setTitle("Manage Addons");
            addsManager.setScene(new Scene(addsManagerParent, 670, 455));
            addsManager.setResizable(false);
            manageAddsGuiController = addManagerLoader.getController();
            manageAddsGuiController.setLists();
        }


        addsManager.show();
        if (debugMode) {
            System.out.println("Manage adds window open");
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


