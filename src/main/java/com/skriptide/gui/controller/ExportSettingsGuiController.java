package com.skriptide.gui.controller;

import com.skriptide.util.ExportSettings;
import com.skriptide.util.ExportType;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashMap;

/**
 * Created by yannh on 28.01.2017.
 */
public class ExportSettingsGuiController {

    @FXML
    private ListView<String> entriesList;
    @FXML
    private TextField hostField;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passField;
    @FXML
    private TextField pathField;
    @FXML
    private Button testConnBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private Button delBtn;
    @FXML
    private Button saveNewBtn;
    @FXML
    private TextField customPath;
    @FXML
    private ComboBox<String> typeBox;

    public void initGui() {


        typeBox.getItems().clear();
        typeBox.getItems().addAll("FTP", "SFTP");
        HashMap<String, ExportSettings> all = ExportSettings.getAll();

        typeBox.getSelectionModel().select(0);
        entriesList.getItems().clear();
        entriesList.getItems().addAll(all.keySet());


        saveNewBtn.setOnAction(event -> {

            String wantedName = customPath.getText();
            for (String str : all.keySet()) {

                if (wantedName.equalsIgnoreCase(str)) {
                    return;
                }

            }
            if(!wantedName.equals("")) {


                ExportSettings.addEntry(new ExportSettings(hostField.getText(), nameField.getText(), passField.getText(), pathField.getText(), ExportType.valueOf(typeBox.getSelectionModel().getSelectedItem())), customPath.getText());
                entriesList.getItems().add(customPath.getText());

                all.put(customPath.getText(), new ExportSettings(hostField.getText(), nameField.getText(), passField.getText(), pathField.getText(),  ExportType.valueOf(typeBox.getSelectionModel().getSelectedItem())));
                customPath.clear();
            }
        });

        delBtn.setOnAction(event -> {

            String selected = entriesList.getSelectionModel().getSelectedItem();

            String t = "";
            for (String n : all.keySet()) {

                if (n.equalsIgnoreCase(selected)) {

                    t = n;

                }
            }
            ExportSettings.removeEntry(t);
            all.remove(t);
            entriesList.getSelectionModel().selectFirst();
            entriesList.getItems().remove(selected);
        });
        saveBtn.setOnAction(event -> {

            String selected = entriesList.getSelectionModel().getSelectedItem();

            String truePath = "";
            for (String n : all.keySet()) {

                if (n.equalsIgnoreCase(selected)) {

                    truePath = n;

                }
            }
            ExportSettings.removeEntry(truePath);
            all.remove(truePath);

            ExportSettings.addEntry(new ExportSettings(hostField.getText(), nameField.getText(), passField.getText(), pathField.getText(),  ExportType.valueOf(typeBox.getSelectionModel().getSelectedItem())), truePath);
            all.put(truePath, new ExportSettings(hostField.getText(), nameField.getText(), passField.getText(), pathField.getText(),  ExportType.valueOf(typeBox.getSelectionModel().getSelectedItem())));


        });
        entriesList.setOnMouseReleased(event -> {
            System.out.println("called");
            String selected = entriesList.getSelectionModel().getSelectedItem();

            for (String n : all.keySet()) {

                if (n.equalsIgnoreCase(selected)) {

                    ExportSettings settings = all.get(n);

                    hostField.setText(settings.getHost());
                    nameField.setText(settings.getUsername());
                    passField.setText(settings.getPassword());
                    pathField.setText(settings.getExportPath());
                    typeBox.getSelectionModel().select(settings.getType().toString());


                }
            }
        });
    }

}
