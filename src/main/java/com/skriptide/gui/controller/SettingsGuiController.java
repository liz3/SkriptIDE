package com.skriptide.gui.controller;

import com.skriptide.Main;
import com.skriptide.util.IDESettings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

/**
 * Created by yannh on 15.02.2017.
 */
public class SettingsGuiController {
    IDESettings s = null;
    @FXML
    private ComboBox languageComboBox;
    @FXML
    private ComboBox styleComboBox;
    @FXML
    private TextField autoSaveField;
    @FXML
    private ComboBox<String> autoSaveComboBox;
    @FXML
    private CheckBox updatesCheckBox;
    @FXML
    private CheckBox skriptMsgMarkCheck;
    @FXML
    private CheckBox startSrvAfterCreateCheck;
    @FXML
    private CheckBox clearPrsAfterServerStopCheck;
    @FXML
    private CheckBox outputSaveCheck;
    @FXML
    private CheckBox minifyOutputCheck;
    @FXML
    private CheckBox codeManageCheck;
    @FXML
    private CheckBox highlightCheck;
    @FXML
    private CheckBox autoCompleteCheck;
    @FXML
    private TreeTableView settingsTable;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cnlBtn;
    @FXML
    private Button applyBtn;
    @FXML
    private ListView<String> sList;
    @FXML
    private TabPane tabPane;
    @FXML
    private CheckBox complexeCheck;

    public void init() {

        sList.getItems().addAll("Main Settings", "Server", "Code");

        sList.setOnMouseReleased(event -> {

            tabPane.getSelectionModel().select(sList.getSelectionModel().getSelectedIndex());

        });
        s = Main.settings;

        autoSaveComboBox.getItems().addAll("Seconds", "Minutes", "Hours");

        switch (String.valueOf(s.getDelayMultiPlier())) {
            case "1000":
                autoSaveComboBox.getSelectionModel().select("Seconds");
                break;
            case "60000":
                autoSaveComboBox.getSelectionModel().select("Minutes");
                break;
            case "3600000":
                autoSaveComboBox.getSelectionModel().select("Hours");
                break;

        }
        autoSaveField.setText(String.valueOf(s.getDelay()));
        skriptMsgMarkCheck.setSelected(s.isMarkMessage());
        startSrvAfterCreateCheck.setSelected(s.isStartAfterCreate());
        clearPrsAfterServerStopCheck.setSelected(s.isClearAfterStop());
        outputSaveCheck.setSelected(s.isSaveOutput());
        minifyOutputCheck.setSelected(s.isMinifyOutput());
        codeManageCheck.setSelected(s.isCodeManagement());
        highlightCheck.setSelected(s.isHighlight());
        autoCompleteCheck.setSelected(s.isAutoComplete());
        complexeCheck.setSelected(s.isComplexeAutoComplete());

        applyBtn.setOnAction(event -> save());

        saveBtn.setOnAction(event -> {
            save();
            saveBtn.getScene().getWindow().hide();
        });
        cnlBtn.setOnAction(event -> saveBtn.getScene().getWindow().hide());
    }

    private void save() {

        s.setCodeManagement(codeManageCheck.isSelected());
        s.setHighlight(highlightCheck.isSelected());
        s.setAutoComplete(autoCompleteCheck.isSelected());
        s.setMinifyOutput(minifyOutputCheck.isSelected());
        s.setMarkMessage(skriptMsgMarkCheck.isSelected());
        s.setStartAfterCreate(startSrvAfterCreateCheck.isSelected());
        s.setClearAfterStop(clearPrsAfterServerStopCheck.isSelected());
        s.setSaveOutput(outputSaveCheck.isSelected());
        s.setDelay(Long.parseLong(autoSaveField.getText()));
        s.setComplexeAutoComplete(complexeCheck.isSelected());
        String sel = autoSaveComboBox.getSelectionModel().getSelectedItem();

        switch (sel) {

            case "Seconds":
                s.setDelayMultiPlier(1000);
                break;
            case "Minutes":
                s.setDelayMultiPlier(60000);
                break;
            case "Hours":
                s.setDelayMultiPlier(3600000);
                break;
        }

        s.saveSettings();
    }
}
