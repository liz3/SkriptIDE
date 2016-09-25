package com.skriptide.guis.startgui;

import com.skriptide.main.Main;
import com.skriptide.util.ConfigManager;
import com.skriptide.util.skunityapi.SkUnityAPI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Liz3ga on 27.07.2016.
 */
public class StartGuiController {

	@FXML
	public TextField projectsPathField;
	@FXML
	public TextField serverPathField;
	@FXML
	public ComboBox<String> langComboBox;
	@FXML
	public Button okBtn;
	@FXML
	public Button cancelBtn;
	@FXML
	public Button projectsPathBtn;
	@FXML
	public Button serverPathBtn;


	public void setValues() {
		okBtn.setOnAction(event -> proceed());
		cancelBtn.setOnAction(event -> cancelAction());
		serverPathBtn.setOnAction(event -> openServersPathChooser());
		projectsPathBtn.setOnAction(event -> openProjectPathChooser());
		langComboBox.getItems().add("English");
		langComboBox.getSelectionModel().select(0);
	}
	public void cancelAction() {

		System.exit(0);

	}

	public void openProjectPathChooser() {

		Stage fileChooserWindow = new Stage();
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Choose Path for Projects");
		File dir = dirChooser.showDialog(fileChooserWindow);
		if (dir == null) {
			//They didn't select a file, they hit cancel
			return;
		}

		projectsPathField.setText(dir.getAbsolutePath());
		if(Main.debugMode) {
			System.out.println("changed projects path");
		}

	}

	public void openServersPathChooser() {

		Stage fileChooserWindow = new Stage();
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Choose Path for Servers");
		File dir = dirChooser.showDialog(fileChooserWindow);
		if (dir == null) {
			//They didn't select a file, they hit cancel
			return;
		}
		serverPathField.setText(dir.getAbsolutePath());
		if(Main.debugMode) {

			System.out.println("changed servers path");
		}

	}


	public void proceed() {


		String projectsPath = projectsPathField.getText().replace("\\", "/");
		String serversPath = serverPathField.getText().replace("\\", "/");
		String lang = langComboBox.getSelectionModel().getSelectedItem();


		ConfigManager.createConfig(projectsPath, serversPath, lang);

		Stage stage = (Stage) okBtn.getScene().getWindow();

		stage.close();

		Thread t = new Thread(() -> {

			SkUnityAPI skUnityAPI = new SkUnityAPI();


		});
		t.start();
		if(Main.debugMode) {

			System.out.println("proceeded to start");
		}

	}


}
