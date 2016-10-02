package com.skriptide.guis.createprojectgui;

import com.skriptide.guis.SceneManager;
import com.skriptide.main.Main;
import com.skriptide.util.ConfigManager;
import com.skriptide.util.MCServer;
import com.skriptide.util.Project;
import com.skriptide.util.Skript;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Liz3ga on 27.07.2016.
 */
public class CreateProjectGuiController {


	@FXML
	public  Button chooseSavePathBtn;
	@FXML
	public  TextField projectNameTxTField;

	@FXML
	public  ComboBox<String> pluginVersionComboBox;

	@FXML
	public  ComboBox<String> loadOnServerComboBox;

	@FXML
	public  Button createServerBtn;

	@FXML
	public  TextField savePathTxTField;

	@FXML
	public  TextArea notesTextArea;

	@FXML
	public  Button cancelBtn;

	@FXML
	public  Button createBtn;

	@FXML
	public CheckBox createSubFolderCheckBox;


	private String truePath;

	public CreateProjectGuiController() {
	}

	private void updatePath() {

		savePathTxTField.setText(truePath + "/" + projectNameTxTField.getText());


	}

	private void createProject() {

		boolean canGo = true;
		String error = "";

		ObservableList<Project> getProjects = Project.getProjects();
		for (Project prj : getProjects.sorted()) {
			String name = prj.getName();
			if (name != null) {
				if (name.equalsIgnoreCase(projectNameTxTField.getText())) {
					canGo = false;
					error = error + "This Project Name allready exists!";
				}
			}
		}
		String selected = pluginVersionComboBox.getSelectionModel().getSelectedItem();
		if (selected == null) {
			canGo = false;
			error = error + "Please choose a Skript version!";
		}
		ObservableList<Skript> sk = Skript.getSkriptVersions();
		Skript tSk = null;
		for (int i = 0; i != sk.size(); i++) {

			Skript s = sk.get(i);
			if (s.getVersion().equals(selected)) {
				tSk = s;
			}
		}

		MCServer trueSRV = null;
		ObservableList<MCServer> mcServers = MCServer.getAllServers();
		for (MCServer srv : mcServers.sorted()) {
			String selection = loadOnServerComboBox.getSelectionModel().getSelectedItem();
			if (srv.getname().equalsIgnoreCase(selection)) {
				trueSRV = srv;
			}
		}

		if (canGo) {
			Project.createProject(projectNameTxTField.getText(), tSk, trueSRV, savePathTxTField.getText(), notesTextArea.getText());


			if (ConfigManager.checkConfig() == 0) {
				ObservableList<Project> projects = Project.getProjects();
				SceneManager.projectsList.getItems().clear();
				for (Project project : projects.sorted()) {
					if (project.getName() != null) {
						SceneManager.projectsList.getItems().addAll(project.getName());
						if(Main.debugMode) {
							System.out.println("Project created!");
						}
					}
				}
			}

			Stage stage = (Stage) createBtn.getScene().getWindow();
			// do what you have to do
			stage.close();
		} else {
			if(Main.debugMode) {
				System.out.println("Could not create Project because: " + error);
			}
		}
	}

	private void cancel() {

		Stage stage = (Stage) createBtn.getScene().getWindow();
		// do what you have to do
		stage.close();
		if(Main.debugMode) {
			System.out.println("Canceled create project");
		}

	}

	private void createNewServer() {

		SceneManager sceneManager = new SceneManager();
		sceneManager.openCreateServer();
		if(Main.debugMode) {
			System.out.println("Started create server");
		}
	}

	public void setValues() {

		projectNameTxTField.setOnKeyReleased(event -> updatePath());
		cancelBtn.setOnAction(event -> cancel());
		createBtn.setOnAction(event -> createProject());
		chooseSavePathBtn.setOnAction(event -> openProjectPathChooser());
		createServerBtn.setOnAction(event -> createNewServer());
		truePath = ConfigManager.getProjectsPath();
		savePathTxTField.setText(ConfigManager.getProjectsPath());
		ObservableList<Skript> sk = Skript.getSkriptVersions();

		pluginVersionComboBox.getItems().clear();
		for (Skript skript : sk.sorted()) {

			pluginVersionComboBox.getItems().add(skript.getVersion());
		}
		ObservableList<MCServer> servers = MCServer.getAllServers();
		loadOnServerComboBox.getItems().clear();

		for (MCServer server : servers.sorted()) {
			loadOnServerComboBox.getItems().add(server.getname());
		}
		if(Main.debugMode) {
			System.out.println("Loaded create project");
		}


	}


	private void openProjectPathChooser() {

		Stage fileChooserWindow = new Stage();
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Choose Path for Projects");
		File dir = dirChooser.showDialog(fileChooserWindow);

		if(dir == null)
			return;

		savePathTxTField.setText(dir.getAbsolutePath());

		truePath = dir.getAbsolutePath();

		updatePath();

		if(Main.debugMode) {
			System.out.println("Project save path changed");
		}


	}


}
