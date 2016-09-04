package com.skriptide.guis.manageserver;

import com.skriptide.util.MCServer;
import com.skriptide.util.ServerVersion;
import com.skriptide.util.Skript;
import com.skriptide.util.SkriptAddon;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Created by Liz3ga on 23.08.2016.
 */
public class ManageServerController {

	@FXML
	public ListView<String> serversList;

	@FXML
	public Button createServerBtn;

	@FXML
	public Button deleteServerBtn;

	@FXML
	public ListView<String> scriptsList;

	@FXML
	public Button addScriptAddonBtn;

	@FXML
	public ComboBox<String> chooseScriptAddonComboBox;

	@FXML
	public Button remScriptAddonBtn;

	@FXML
	public TextField nameTextField;

	@FXML
	public TextField portTextField;

	@FXML
	public ComboBox<String> serverVersionComboBox;

	@FXML
	public ComboBox<String> scriptVersionComboBox;

	@FXML
	public TextArea notesTexrArea;

	@FXML
	public Button saveChangesBtn;

	@FXML
	public TextField startArgsTextField;

	@FXML
	public TextField generatorSettingsTextField;

	@FXML
	public TextField opPermissionLevelTextField;

	@FXML
	public CheckBox netherCheck;

	@FXML
	public TextField worldNameTextField;

	@FXML
	public CheckBox queryCheck;

	@FXML
	public CheckBox flightCheck;

	@FXML
	public CheckBox achievementsCheck;

	@FXML
	public TextField maxWorldSizeTextField;

	@FXML
	public ComboBox<String> levelTypeComboBox;

	@FXML
	public CheckBox rconCheck;

	@FXML
	public TextField levelSeedTextField;

	@FXML
	public CheckBox forceGmCheck;

	@FXML
	public TextField maxHeightTextField;

	@FXML
	public CheckBox whitelistCheck;

	@FXML
	public CheckBox spawnNpcsCheck;

	@FXML
	public CheckBox spawnAnimalsCheck;

	@FXML
	public CheckBox hardcoreCheck;

	@FXML
	public CheckBox snooperCheck;

	@FXML
	public CheckBox onlineModeCheck;

	@FXML
	public TextField packCheck;

	@FXML
	public TextField packSHATextField;

	@FXML
	public CheckBox pvpCheck;

	@FXML
	public ComboBox<String> difficultyComboBox;

	@FXML
	public ComboBox<String> defaultGameModeComboBox;

	@FXML
	public CheckBox cmdBlockCheck;

	@FXML
	public TextField maxPlayersTextField;

	@FXML
	public CheckBox spawnMonstersCheck;

	@FXML
	public CheckBox generateSctructureCheck;

	@FXML
	public TextField viewdinstanceTextField;

	@FXML
	public TextField modtTextField;

	@FXML
	public TextField serverPathTextField;

	@FXML
	public Button confirmBtn;


	public void setValues() {

		serversList.getItems().clear();

		ObservableList<MCServer> servers = MCServer.getAllServers();
		for (MCServer server : servers.sorted()) {
			serversList.getItems().add(server.getname());
		}


	}

	public void proceed() {

		Stage stage = (Stage) confirmBtn.getScene().getWindow();
		// do what you have to do
		stage.close();
	}

	public void loadAddons() {

		chooseScriptAddonComboBox.getItems().clear();
		ObservableList<SkriptAddon> addons = SkriptAddon.getScriptAddons();
		for (SkriptAddon current : addons.sorted()) {
			if (!scriptsList.getItems().contains(current.getName()) && !scriptsList.getItems().contains(current.getVersion())) {
				chooseScriptAddonComboBox.getItems().add(current.getName() + "-" + current.getVersion());
			}
		}

	}

	public void saveChangesToServer() {

		String selection = serversList.getSelectionModel().getSelectedItem();

		ObservableList<MCServer> servers = MCServer.getAllServers();

		MCServer trueServ = null;
		for (MCServer server : servers.sorted()) {

			if (server.getname().equals(selection)) {
				trueServ = server;
			}
		}

		trueServ.setname(nameTextField.getText());
		String selectedItem = serverVersionComboBox.getSelectionModel().getSelectedItem();

		ObservableList<ServerVersion> vers = ServerVersion.getServerVersions();
		for (ServerVersion srv : vers.sorted()) {
			if (srv.getVersion().equals(selectedItem)) {
				trueServ.setversion(srv);
			}
		}
		String selctSK = scriptVersionComboBox.getSelectionModel().getSelectedItem();
		ObservableList<Skript> skripts = Skript.getSkriptVersions();
		for (Skript sk : skripts.sorted()) {
			if (sk.getVersion().equals(selctSK)) {
				trueServ.setskript(sk);
			}
		}
		trueServ.setnotes(notesTexrArea.getText());

		trueServ.setopPermLevel(Integer.parseInt(opPermissionLevelTextField.getText()));
		trueServ.setgeneratorSettings(generatorSettingsTextField.getText());
		trueServ.setallowNether(netherCheck.isSelected());
		trueServ.setworldName(worldNameTextField.getText());
		trueServ.setquery(queryCheck.isSelected());
		trueServ.setallowFlight(flightCheck.isSelected());
		trueServ.setannounceAchievemnts(achievementsCheck.isSelected());
		trueServ.setport(Integer.parseInt(portTextField.getText()));
		trueServ.setmaxWorldSize(Long.parseLong(maxWorldSizeTextField.getText()));
		trueServ.setlvlType(levelTypeComboBox.getSelectionModel().getSelectedItem());
		trueServ.setrcon(rconCheck.isSelected());
		trueServ.setlevelSeed(levelSeedTextField.getText());
		trueServ.setforceGamemode(forceGmCheck.isSelected());
		trueServ.setmaxBuildHeight(Long.parseLong(maxHeightTextField.getText()));
		trueServ.setspawnNPCS(spawnNpcsCheck.isSelected());
		trueServ.setwhitelist(whitelistCheck.isSelected());
		trueServ.setspawnAnimals(spawnAnimalsCheck.isSelected());
		trueServ.sethardcore(hardcoreCheck.isSelected());
		trueServ.setsnooper(snooperCheck.isSelected());
		trueServ.setpackSHA1(packSHATextField.getText());
		trueServ.setonlineMode(onlineModeCheck.isSelected());
		trueServ.setpack(packCheck.getText());
		trueServ.setpvp(pvpCheck.isSelected());
		trueServ.setdifficulty(difficultyComboBox.getSelectionModel().getSelectedIndex());
		trueServ.setallowCMD(cmdBlockCheck.isSelected());
		trueServ.setdefaultGm(defaultGameModeComboBox.getSelectionModel().getSelectedIndex());
		trueServ.setmaxPlayers(Integer.parseInt(maxPlayersTextField.getText()));
		trueServ.setspawnMonsters(spawnMonstersCheck.isSelected());
		trueServ.setgenerateStructure(generateSctructureCheck.isSelected());
		trueServ.setviewDistance(Integer.parseInt(viewdinstanceTextField.getText()));
		trueServ.setmodt(modtTextField.getText());
		ObservableList<SkriptAddon> addons = SkriptAddon.getScriptAddons();
		SkriptAddon[] allAdds = new SkriptAddon[scriptsList.getItems().size()];
		int t = 0;
		for (int i = 0; i != addons.size(); i++) {

			SkriptAddon skriptAddon = addons.get(i);

			for (String str : scriptsList.getItems().sorted()) {
				if (str.contains(skriptAddon.getName()) && str.contains(skriptAddon.getVersion())) {
					allAdds[t] = skriptAddon;
					t++;
				}
			}
		}
		trueServ.setAddons(allAdds);

		trueServ.updateServer();

		setValues();


	}

	public void deleteServer() {
		String selection = serversList.getSelectionModel().getSelectedItem();

		ObservableList<MCServer> servers = MCServer.getAllServers();
		for (MCServer server : servers.sorted()) {
			if (server.getname().equalsIgnoreCase(selection)) {

				server.deleteServer();
				serversList.getItems().remove(selection);
			}
		}

	}

	public void addAddon() {

		String selectedItem = chooseScriptAddonComboBox.getSelectionModel().getSelectedItem();
		scriptsList.getItems().add(selectedItem);
		chooseScriptAddonComboBox.getSelectionModel().clearSelection();
		chooseScriptAddonComboBox.getItems().remove(selectedItem);


	}

	public void remAddon() {

		String selected = scriptsList.getSelectionModel().getSelectedItem();
		scriptsList.getItems().remove(selected);
		chooseScriptAddonComboBox.getItems().add(selected);


	}

	public void loadSettings() {

		MCServer srv = null;
		ObservableList<MCServer> servers = MCServer.getAllServers();

		for (MCServer server : servers.sorted()) {


			if (server.getname().equals(serversList.getSelectionModel().getSelectedItem())) {
				srv = server;
			}
		}
		if (srv != null) {
			srv.loadServer();

			nameTextField.setText(srv.getname());
			portTextField.setText(String.valueOf(srv.getport()));


			ObservableList<ServerVersion> srvVers = ServerVersion.getServerVersions();
			for (ServerVersion ver : srvVers.sorted()) {
				serverVersionComboBox.getItems().add(ver.getVersion());
				if (ver.getVersion().equals(srv.getversion().getVersion())) {
					serverVersionComboBox.getSelectionModel().select(ver.getVersion());
				}
			}

			ObservableList<Skript> skripts = Skript.getSkriptVersions();
			for (Skript sk : skripts.sorted()) {

				scriptVersionComboBox.getItems().add(sk.getVersion());
				if (sk.getVersion().equals(srv.getskript().getVersion())) {
					scriptVersionComboBox.getSelectionModel().select(sk.getVersion());
				}
			}
			serverPathTextField.setText(srv.getpath());
			notesTexrArea.setText(srv.getnotes());

			startArgsTextField.setText(srv.getstartArgs());
			generatorSettingsTextField.setText(srv.getgeneratorSettings());
			opPermissionLevelTextField.setText(String.valueOf(srv.getopPermLevel()));
			maxWorldSizeTextField.setText(String.valueOf(srv.getmaxWorldSize()));
			maxPlayersTextField.setText(String.valueOf(srv.getmaxPlayers()));
			worldNameTextField.setText(srv.getworldName());
			levelSeedTextField.setText(srv.getlevelSeed());
			maxHeightTextField.setText(String.valueOf(srv.getmaxBuildHeight()));
			viewdinstanceTextField.setText(String.valueOf(srv.getviewDistance()));
			modtTextField.setText(srv.getmodt());

			levelTypeComboBox.getItems().clear();
			difficultyComboBox.getItems().clear();
			defaultGameModeComboBox.getItems().clear();
			String choosedLvLType = srv.getlvlType();
			int choosedDifficulty = srv.getdifficulty();
			int defaultGamode = srv.getdefaultGm();
			levelTypeComboBox.getItems().addAll("DEFAULT", "SUPER_FLAT");
			difficultyComboBox.getItems().addAll("Peaceful", "Easy", "Normal", "Hard");
			defaultGameModeComboBox.getItems().addAll("Survival", "Creative", "Hardcore", "Spectator");
			levelTypeComboBox.getSelectionModel().select(choosedLvLType);
			difficultyComboBox.getSelectionModel().select(choosedDifficulty);
			defaultGameModeComboBox.getSelectionModel().select(defaultGamode);

			generateSctructureCheck.setSelected(srv.getgenerateStructure());
			netherCheck.setSelected(srv.getallowNether());
			queryCheck.setSelected(srv.getquery());
			flightCheck.setSelected(srv.getallowFlight());
			achievementsCheck.setSelected(srv.getannounceAchievemnts());
			rconCheck.setSelected(srv.getrcon());
			forceGmCheck.setSelected(srv.getforceGamemode());
			whitelistCheck.setSelected(srv.getwhitelist());
			spawnNpcsCheck.setSelected(srv.getspawnNPCS());
			spawnAnimalsCheck.setSelected(srv.getspawnAnimals());
			hardcoreCheck.setSelected(srv.gethardcore());
			snooperCheck.setSelected(srv.getsnooper());
			pvpCheck.setSelected(srv.getpvp());
			onlineModeCheck.setSelected(srv.getonlineMode());
			cmdBlockCheck.setSelected(srv.getallowCMD());
			spawnMonstersCheck.setSelected(srv.getspawnMonsters());

			packSHATextField.setText(srv.getpackSHA1());
			packCheck.setText(srv.getpack());

			scriptsList.getItems().clear();

			SkriptAddon[] addons = srv.getSkriptAddons();


			for (int i = 0; i != addons.length; i++) {
				if (addons[i] != null) {
					SkriptAddon add = addons[i];
					scriptsList.getItems().add(add.getName() + "-" + add.getVersion());
				}
			}
			loadAddons();
		}


	}


}
