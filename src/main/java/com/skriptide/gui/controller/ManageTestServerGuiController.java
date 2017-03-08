package com.skriptide.gui.controller;

import com.skriptide.Main;
import com.skriptide.include.Addon;
import com.skriptide.include.Api;
import com.skriptide.include.Server;
import com.skriptide.include.Skript;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

/**
 * Created by yannh on 14.02.2017.
 */
public class ManageTestServerGuiController {
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
    public Button confirmBtn;
    @FXML
    public TextField nameTextField;
    @FXML
    public TextField portTextField;
    @FXML
    public ComboBox serverVersionComboBox;
    @FXML
    public ComboBox scriptVersionComboBox;
    @FXML
    public TextField serverPathTextField;
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
    public ComboBox difficultyComboBox;
    @FXML
    public ComboBox defaultGameModeComboBox;
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

    Server currentSelected = null;

    public void initGui() {

        serversList.getItems().clear();
        scriptsList.getItems().clear();
        chooseScriptAddonComboBox.getItems().clear();



        deleteServerBtn.setOnAction(event -> {

            Server server = Main.manager.getServer().get(serversList.getSelectionModel().getSelectedItem());
            if(server == null) {
                return;
            }
            if(Main.sceneManager.infoCheck("Delete Server", "Are you sure?",
                    "Are you sure, you want to delete this server? All files will be deleted, if a project is bind to this server, the binding will be deleted.", Alert.AlertType.CONFIRMATION)) {

                serversList.getItems().remove(server.getName());
                server.deleteServer();

            }
        });

        for(Addon addon : Main.manager.getAddons().values()) {

            chooseScriptAddonComboBox.getItems().add(addon.getName() + " - " + addon.getVersion());
        }
        for(Api api : Main.manager.getApis().values()) {

            serverVersionComboBox.getItems().add(api.getName() + " - " + api.getVersion());
        }
        for(Skript skript : Main.manager.getSkripts().values()) {

            scriptVersionComboBox.getItems().add(skript.getName() + " - " + skript.getVersion());
        }
        for(Server server : Main.manager.getServer().values()) {
            serversList.getItems().add(server.getName()
            );
        }


        confirmBtn.setOnAction(event -> confirmBtn.getScene().getWindow().hide());
        remScriptAddonBtn.setOnAction(event -> {

            ObservableList<String> selected = scriptsList.getSelectionModel().getSelectedItems();

            for(String str : selected) {
                scriptsList.getItems().remove(str);
                chooseScriptAddonComboBox.getItems().add(str);
            }
        });
        addScriptAddonBtn.setOnAction(event -> {

            String selection = chooseScriptAddonComboBox.getSelectionModel().getSelectedItem();

            if(selection == null || selection.equals("")) {
                return;
            }

            if(!scriptsList.getItems().contains(selection)) {
                scriptsList.getItems().add(selection);
                chooseScriptAddonComboBox.getItems().remove(selection);
            }
        });
        saveChangesBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Server trueServ = Main.manager.getServer().get(serversList.getSelectionModel().getSelectedItem());

                if(trueServ == null) {
                    return;
                }


                trueServ.setOpPermLevel(Integer.parseInt(opPermissionLevelTextField.getText()));
                trueServ.setGeneratorSettings(generatorSettingsTextField.getText());
                trueServ.setAllowNether(netherCheck.isSelected());
                trueServ.setWorldName(worldNameTextField.getText());
                trueServ.setQuery(queryCheck.isSelected());
                trueServ.setAllowFlight(flightCheck.isSelected());
                trueServ.setAnnounceAchievemnts(achievementsCheck.isSelected());
                trueServ.setPort(Integer.parseInt(portTextField.getText()));
                trueServ.setMaxWorldSize(Long.parseLong(maxWorldSizeTextField.getText()));
                trueServ.setLvlType(levelTypeComboBox.getSelectionModel().getSelectedItem());
                trueServ.setRcon(rconCheck.isSelected());
                trueServ.setLevelSeed(levelSeedTextField.getText());
                trueServ.setForceGamemode(forceGmCheck.isSelected());
                trueServ.setMaxBuildHeight(Long.parseLong(maxHeightTextField.getText()));
                trueServ.setSpawnNPCS(spawnNpcsCheck.isSelected());
                trueServ.setWhitelist(whitelistCheck.isSelected());
                trueServ.setSpawnAnimals(spawnAnimalsCheck.isSelected());
                trueServ.setHardcore(hardcoreCheck.isSelected());
                trueServ.setSnooper(snooperCheck.isSelected());
                trueServ.setPackSHA1(packSHATextField.getText());
                trueServ.setOnlineMode(onlineModeCheck.isSelected());
                trueServ.setPack(packCheck.getText());
                trueServ.setPvp(pvpCheck.isSelected());
                trueServ.setDifficulty(difficultyComboBox.getSelectionModel().getSelectedIndex());
                trueServ.setAllowCMD(cmdBlockCheck.isSelected());
                trueServ.setDefaultGm(defaultGameModeComboBox.getSelectionModel().getSelectedIndex());
                trueServ.setMaxPlayers(Integer.parseInt(maxPlayersTextField.getText()));
                trueServ.setSpawnMonsters(spawnMonstersCheck.isSelected());
                trueServ.setGenerateStructure(generateSctructureCheck.isSelected());
                trueServ.setViewDistance(Integer.parseInt(viewdinstanceTextField.getText()));
                trueServ.setModt(modtTextField.getText());

                ArrayList<Addon> adds = new ArrayList<>();
                for(String str : scriptsList.getItems()) {

                    for(Addon addon : Main.manager.getAddons().values()) {

                        String org = addon.getName() + " - " + addon.getVersion();

                        if(str.equals(org)) {
                            adds.add(addon);
                        }
                    }
                }
                trueServ.setServerAddons(adds);

                trueServ.updateServer();
            }
        });
        serversList.setOnMouseReleased(event -> {


            if(serversList.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            String selection = serversList.getSelectionModel().getSelectedItem();
            Server srv = Main.manager.getServer().get(selection);
            srv.loadConfiguration();

            nameTextField.setText(srv.getName());
            portTextField.setText(String.valueOf(srv.getPort()));
            serverPathTextField.setText(srv.getFolderPath());

            for(Api api : Main.manager.getApis().values()) {
                String org = api.getName() + " - " + api.getVersion();
                String curr = srv.getApi().getName() + " - " + srv.getApi().getVersion();

                if(org.equals(curr)) {
                    serverVersionComboBox.getSelectionModel().select(curr);
                }
            }

            for(Skript sk : Main.manager.getSkripts().values()) {
                String org = sk.getName() + " - " + sk.getVersion();
                String curr = srv.getSkript().getName() + " - " + srv.getSkript().getVersion();

                if(org.equals(curr)) {
                    scriptVersionComboBox.getSelectionModel().select(curr);
                }
            }
            startArgsTextField.setText(srv.getStartArgs());
            generatorSettingsTextField.setText(srv.getGeneratorSettings());
            opPermissionLevelTextField.setText(String.valueOf(srv.getOpPermLevel()));
            maxWorldSizeTextField.setText(String.valueOf(srv.getMaxWorldSize()));
            maxPlayersTextField.setText(String.valueOf(srv.getMaxPlayers()));
            worldNameTextField.setText(srv.getWorldName());
            levelSeedTextField.setText(srv.getLevelSeed());
            maxHeightTextField.setText(String.valueOf(srv.getMaxBuildHeight()));
            viewdinstanceTextField.setText(String.valueOf(srv.getViewDistance()));
            modtTextField.setText(srv.getModt());

            levelTypeComboBox.getItems().clear();
            difficultyComboBox.getItems().clear();
            defaultGameModeComboBox.getItems().clear();
            String choosedLvLType = srv.getLvlType();
            int choosedDifficulty = srv.getDifficulty();
            int defaultGamode = srv.getDefaultGm();
            levelTypeComboBox.getItems().addAll("DEFAULT", "SUPER_FLAT");
            difficultyComboBox.getItems().addAll("Peaceful", "Easy", "Normal", "Hard");
            defaultGameModeComboBox.getItems().addAll("Survival", "Creative", "Hardcore", "Spectator");
            levelTypeComboBox.getSelectionModel().select(choosedLvLType);
            difficultyComboBox.getSelectionModel().select(choosedDifficulty);
            defaultGameModeComboBox.getSelectionModel().select(defaultGamode);
            generateSctructureCheck.setSelected(srv.isGenerateStructure());
            netherCheck.setSelected(srv.isAllowNether());
            queryCheck.setSelected(srv.isQuery());
            flightCheck.setSelected(srv.isAllowFlight());
            achievementsCheck.setSelected(srv.isAnnounceAchievemnts());
            rconCheck.setSelected(srv.isRcon());
            forceGmCheck.setSelected(srv.isForceGamemode());
            whitelistCheck.setSelected(srv.isWhitelist());
            spawnNpcsCheck.setSelected(srv.isSpawnNPCS());
            spawnAnimalsCheck.setSelected(srv.isSpawnAnimals());
            hardcoreCheck.setSelected(srv.isHardcore());
            snooperCheck.setSelected(srv.isSnooper());
            pvpCheck.setSelected(srv.isPvp());
            onlineModeCheck.setSelected(srv.isOnlineMode());
            cmdBlockCheck.setSelected(srv.isAllowCMD());
            spawnMonstersCheck.setSelected(srv.isSpawnMonsters());
            packSHATextField.setText(srv.getPackSHA1());
            packCheck.setText(srv.getPack());

            for(Addon addon : srv.getServerAddons()) {

                String org = addon.getName() + " - " + addon.getVersion();

                for(String str : chooseScriptAddonComboBox.getItems()) {

                    if(str.equalsIgnoreCase(str)) {
                        scriptsList.getItems().addAll(org);
                        chooseScriptAddonComboBox.getItems().remove(org);
                    }
                }
            }
            currentSelected = srv;
        });
    }

}
