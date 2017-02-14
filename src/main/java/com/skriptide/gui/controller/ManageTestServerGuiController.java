package com.skriptide.gui.controller;

import com.skriptide.Main;
import com.skriptide.include.Server;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

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
    public ListView scriptsList;
    @FXML
    public Button addScriptAddonBtn;
    @FXML
    public ComboBox chooseScriptAddonComboBox;
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
    public ComboBox levelTypeComboBox;
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



    public void initGui() {

        for(Server server : Main.manager.getServer().values()) {
            serversList.getItems().add(server.getName()
            );
        }

        serversList.setOnMouseReleased(event -> {

            String selection = serversList.getSelectionModel().getSelectedItem();


            Server srv = Main.manager.getServer().get(selection);

            srv.loadConfiguration();
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

        });
    }

}
