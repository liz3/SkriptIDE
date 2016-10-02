package com.skriptide.guis.manageadds;

import com.skriptide.main.Main;
import com.skriptide.util.ServerVersion;
import com.skriptide.util.Skript;
import com.skriptide.util.SkriptAddon;
import com.skriptide.util.VersionReader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Liz3ga on 29.07.2016.
 */
public class ManageAddsGuiController {

	@FXML
	public TableView<Skript> scriptTable;
	@FXML
	public Button scriptRemoveBtn;
	@FXML
	public Button scriptAddBtn;

	@FXML
	public TableView<ServerVersion> serverTable;
	@FXML
	public Button serverRemove;
	@FXML
	public Button serverAddBtn;

	@FXML
	public TableView<SkriptAddon> addonsTable;
	@FXML
	public Button addonsRemoveBtn;
	@FXML
	public Button addonAddBtn;

	@FXML
	public Button confirmBtn;


	private void confirmAction() {

		Stage stage = (Stage) confirmBtn.getScene().getWindow();
		stage.close();

	}


	private void addScript() {

		Stage fileChooserWindow = new Stage();
		FileChooser dirChooser = new FileChooser();
		dirChooser.setTitle("Choose Path to Script-plugin");
		dirChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("JAR", "*.jar")

		);
		File f = dirChooser.showOpenDialog(fileChooserWindow);

		if (f != null) {
			Skript.addScript(VersionReader.getNameOfPlugin(f), VersionReader.getVersionOfPlugin(f), f);
			refreshList();

			if (Main.debugMode) {
				System.out.println("Added Skript!");
			}
		}
	}

	private void addServer() {

		Stage fileChooserWindow = new Stage();
		FileChooser dirChooser = new FileChooser();
		dirChooser.setTitle("Choose Path to Bukkit or Spigot Plugin");
		dirChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("JAR", "*.jar")
		);
		File f = dirChooser.showOpenDialog(fileChooserWindow);

		if (f != null) {
			VersionReader.getVersionOfServer(f);
			ServerVersion.addServerVersion(f.getName(), VersionReader.getVersionOfServer(f), f);
			refreshList();
			if (Main.debugMode) {
				System.out.println("added Bukkit/spigot version");
			}
		}
	}

	private void addAddon() {

		Stage fileChooserWindow = new Stage();
		FileChooser dirChooser = new FileChooser();
		dirChooser.setTitle("Choose Path to Addon-Plugin");
		dirChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("JAR", "*.jar")
		);
		File f = dirChooser.showOpenDialog(fileChooserWindow);
		if (f != null) {
			SkriptAddon.addAddon(VersionReader.getNameOfPlugin(f), VersionReader.getVersionOfPlugin(f), f);
            System.out.println("LoL es geht");
            refreshList();
			if (Main.debugMode) {

				System.out.println("added addon");
			}
		} else {
            System.out.println("Lol, das geht nicht");
        }

	}

	private void remScript() {

		ObservableList<Skript> selected, all;
		all = scriptTable.getItems();
		selected = scriptTable.getSelectionModel().getSelectedItems();
		System.out.println("lol");
		for (int i = 0; i != selected.size(); i++) {
			Skript skript = selected.get(i);
			String path = skript.getPath();
			System.out.println(path);
			Skript.removeScript(path);
		}
		selected.forEach(all::remove);
		if (Main.debugMode) {
			System.out.println("removed skripts versions");
		}
	}

	private void remServer() {


		ObservableList<ServerVersion> selected, all;
		all = serverTable.getItems();
		selected = serverTable.getSelectionModel().getSelectedItems();

		for (int i = 0; i != selected.size(); i++) {
			ServerVersion skript = selected.get(i);
			String path = skript.getPath();
			System.out.println(path);
			ServerVersion.removeServerVersion(path);
		}
		selected.forEach(all::remove);
		if (Main.debugMode) {
			System.out.println("removed bukkit/spigot versions");
		}

	}

	private void remAddon() {

		ObservableList<SkriptAddon> selected, all;
		all = addonsTable.getItems();
		selected = addonsTable.getSelectionModel().getSelectedItems();
		System.out.println("lol");
		for (int i = 0; i != selected.size(); i++) {
			SkriptAddon skript = selected.get(i);
			String path = skript.getPath();
			System.out.println(path);
			SkriptAddon.removeAddon(path);
		}
		selected.forEach(all::remove);
		if (Main.debugMode) {
			System.out.println("removed addons");
		}
	}


	private void setServerVersions() {

		//MC-Server Vrsionen
		TableColumn<ServerVersion, String> nameColum = new TableColumn<>("Name");
		nameColum.setMinWidth(450);
		nameColum.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<ServerVersion, String> versionColum = new TableColumn<>("Version");
		versionColum.setMinWidth(450);
		versionColum.setCellValueFactory(new PropertyValueFactory<>("version"));

		TableColumn<ServerVersion, String> pathColum = new TableColumn<>("Path");
		pathColum.setMinWidth(450);
		pathColum.setCellValueFactory(new PropertyValueFactory<>("path"));


		serverTable.setItems(ServerVersion.getServerVersions());
		serverTable.getColumns().addAll(nameColum, versionColum, pathColum);
		if (Main.debugMode) {
			System.out.println("set server versions ");
		}

	}

	private void setSkriptVersions() {

		//MC-Server Vrsionen
		TableColumn<Skript, String> nameColum = new TableColumn<>("Name");
		nameColum.setMinWidth(450);
		nameColum.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Skript, String> versionColum = new TableColumn<>("Version");
		versionColum.setMinWidth(450);
		versionColum.setCellValueFactory(new PropertyValueFactory<>("version"));

		TableColumn<Skript, String> pathColum = new TableColumn<>("Path");
		pathColum.setMinWidth(450);
		pathColum.setCellValueFactory(new PropertyValueFactory<>("path"));


		scriptTable.setItems(Skript.getSkriptVersions());
		scriptTable.getColumns().addAll(nameColum, versionColum, pathColum);

		if (Main.debugMode) {
			System.out.println("set skript versions");
		}

	}

	private void setSkriptAddons() {

		//MC-Server Vrsionen
		TableColumn<SkriptAddon, String> nameColum = new TableColumn<>("Name");
		nameColum.setMinWidth(450);
		nameColum.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<SkriptAddon, String> versionColum = new TableColumn<>("Version");
		versionColum.setMinWidth(450);
		versionColum.setCellValueFactory(new PropertyValueFactory<>("version"));

		TableColumn<SkriptAddon, String> pathColum = new TableColumn<>("Path");
		pathColum.setMinWidth(450);
		pathColum.setCellValueFactory(new PropertyValueFactory<>("path"));


		addonsTable.setItems(SkriptAddon.getScriptAddons());
		addonsTable.getColumns().addAll(nameColum, versionColum, pathColum);
		if (Main.debugMode) {

			System.out.println("set skript addons");
		}

	}

	public void setLists() {


		scriptAddBtn.setOnAction(event -> addScript());
		scriptRemoveBtn.setOnAction(event -> remScript());
		serverAddBtn.setOnAction(event -> addServer());
		serverRemove.setOnAction(event -> remServer());
		addonsRemoveBtn.setOnAction(event -> remAddon());
		addonAddBtn.setOnAction(event -> addAddon());
		confirmBtn.setOnAction(event -> confirmAction());

		setSkriptAddons();
		setServerVersions();
		setSkriptVersions();


	}

	private void refreshList() {


		addonsTable.refresh();
		scriptTable.refresh();
		serverTable.refresh();

		serverTable.setItems(ServerVersion.getServerVersions());
		scriptTable.setItems(Skript.getSkriptVersions());
		addonsTable.setItems(SkriptAddon.getScriptAddons());

		if (Main.debugMode) {

			System.out.println("refreshed list");
		}
	}
}
