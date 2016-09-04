package com.skriptide.guis;

import com.skriptide.guis.createprojectgui.CreateProjectGuiController;
import com.skriptide.guis.createserver.CreateServerGuiController;
import com.skriptide.guis.idegui.IdeGuiController;
import com.skriptide.guis.manageadds.ManageAddsGuiController;
import com.skriptide.guis.manageserver.ManageServerController;
import com.skriptide.guis.startgui.StartGuiController;
import com.skriptide.main.Main;
import com.skriptide.util.Config;
import com.skriptide.util.MCServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

import java.io.IOException;
import java.util.ArrayList;

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


	public Stage mainWindow, welcomeWindow, createNewProjectWindow, createNewServerWindow, addsManager, manageServer;
	private FXMLLoader mainLoader = new FXMLLoader(), welcomeLoader = new FXMLLoader(), createNewProjectLoader = new FXMLLoader(), createNewServerLoader = new FXMLLoader(), addManagerLoader = new FXMLLoader(), manageServerLoader = new FXMLLoader();
	private Parent mainParent = null, welcomeWindowParent = null, createNewProjectWindowParent = null, createNewServerWindowParent = null, addsManagerParent = null, manageServerParent = null;
	private CreateProjectGuiController createProjectGuiController;
	private CreateServerGuiController createServerGuiController;
	private IdeGuiController ideGuiController;
	private ManageAddsGuiController manageAddsGuiController;
	private StartGuiController startGuiController;
	private ManageServerController manageServerController;

	public static void cleanUP() {

		runningServer = null;

		consoleOut.clear();


	}

	public IdeGuiController getIdeGuiController() {
		return this.ideGuiController;
	}

	public SceneManager getSceneManager() {
		return this;
	}

	public void runMain() {

		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {

		mainWindow = stage;

		try {
			mainParent = mainLoader
					.load(SceneManager.class.getResourceAsStream("/IdeGui.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}


		Scene mainScene = new Scene(mainParent, 900, 530);

		mainScene.getStylesheets().add("Highlighting.css");
		mainWindow.setTitle("ScriptIDE");
		mainWindow.getIcons().add(new Image("http://www.mediafire.com/convkey/9377/kw4v8cwmcocs6b5zg.jpg?size_id=3"));
		mainWindow.setScene(mainScene);
		mainWindow.setMinHeight(520);
		mainWindow.setMinWidth(885);
		mainWindow.centerOnScreen();
		mainWindow.show();
		ideGuiController = mainLoader.getController();

		if (Main.debugMode) {
			System.out.println("Main Gui loading finished");
		}


		ideGuiController.setConsoleArea();

		int configState = Config.checkConfig();

		if (configState == 0) {

			if (Main.debugMode) {
				System.out.println("Trying to loading projects to mainGui");
			}

			ideGuiController.loadInProjects();


		} else if (configState == 1) {

			if (Main.debugMode) {
				System.out.println("Opening Welcome Screen");
			}

			openWelcomeGui();


		}


	}

	public void openWelcomeGui() {


		welcomeWindow = new Stage();

		System.out.println("stage change");

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

		if (Main.debugMode) {
			System.out.println("Welcome scrren open");
		}

		startGuiController = welcomeLoader.getController();

		String username = System.getProperty("user.name");

		startGuiController.projectsPathField.setText("C:\\Users\\" + username + "\\Documents\\ScriptIDE\\Projects\\");

		startGuiController.serverPathField.setText("C:\\Users\\" + username + "\\Documents\\ScriptIDE\\Servers\\");
		if (Main.debugMode) {

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
		createServerGuiController.setValues();


	}

	public void openManageVersions() throws IOException {


		if (addsManagerParent == null || addManagerLoader == null) {

			addsManager = new Stage();

			addsManagerParent = addManagerLoader.load(getClass().getResourceAsStream("/ManageAddsGui.fxml"));
			addsManager.setTitle("Manage Addons");
			addsManager.setScene(new Scene(addsManagerParent, 670, 455));
			addsManager.setResizable(false);
			manageAddsGuiController = addManagerLoader.getController();
			manageAddsGuiController.setLists();
		}


		addsManager.show();


	}

	public void openManageServer() {

		if (manageServerParent == null) {
			manageServer = new Stage();

			try {

				manageServerParent = manageServerLoader.load(getClass().getResourceAsStream("/ManageTestServerGui.fxml"));


			} catch (IOException e) {
				e.printStackTrace();
			}

			manageServer.setTitle("Manage Servers");
			manageServer.setScene(new Scene(manageServerParent, 820, 550));

			manageServer.setResizable(false);
			manageServer.centerOnScreen();
			manageServerController = manageServerLoader.getController();
		}


		manageServer.show();

		manageServerController.setValues();
	}


}


