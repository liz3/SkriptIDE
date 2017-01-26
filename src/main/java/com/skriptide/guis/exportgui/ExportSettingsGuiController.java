package com.skriptide.guis.exportgui;

import com.skriptide.util.ExportSettings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.HashMap;

/**
 * Created by yannh on 20.01.2017.
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

    public void initGui() {


        HashMap<String, ExportSettings> all = ExportSettings.getAll();


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


                ExportSettings.addEntry(new ExportSettings(hostField.getText(), nameField.getText(), passField.getText(), pathField.getText()), customPath.getText());
                entriesList.getItems().add(customPath.getText());

                all.put(customPath.getText(), new ExportSettings(hostField.getText(), nameField.getText(), passField.getText(), pathField.getText()));
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

            ExportSettings.addEntry(new ExportSettings(hostField.getText(), nameField.getText(), passField.getText(), pathField.getText()), truePath);
            all.put(truePath, new ExportSettings(hostField.getText(), nameField.getText(), passField.getText(), pathField.getText()));


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


                }
            }
        });
    }
}
