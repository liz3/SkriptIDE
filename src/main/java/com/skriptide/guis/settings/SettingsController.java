package com.skriptide.guis.settings;

import com.skriptide.theme.Theme;
import com.skriptide.theme.ThemeCreator;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Created by Liz3ga on 01.09.2016.
 */
public class SettingsController {

    @FXML
    public ComboBox languageComboBox;

    @FXML
    public ComboBox styleComboBox;

    @FXML
    public TextField autoSaveField;

    @FXML
    public ComboBox autoSaveComboBox;

    @FXML
    public CheckBox updatesCheckBox;

    @FXML
    public CheckBox skriptMsgMarkCheck;

    @FXML
    public CheckBox startSrvAfterCreateCheck;

    @FXML
    public CheckBox clearPrsAfterServerStopCheck;

    @FXML
    public TextField preSetSavePathField;

    @FXML
    public CheckBox outputSaveCheck;

    @FXML
    public CheckBox highlightCheck;

    @FXML
    public CheckBox autoCompleteCheck;

    @FXML
    public TreeTableView settingsTable;

    @FXML
    public ComboBox remoteExportComboBox;

    @FXML
    public TextField exportHostField;

    @FXML
    public TextField exportUsernameField;

    @FXML
    public PasswordField exportPasswordField;

    @FXML
    public ListView settingsList;

    @FXML
    public Button saveBtn;

    @FXML
    public Button cnlBtn;

    @FXML
    public Button applyBtn;

    public void init() {

    }
}
