package com.skriptide.guis.versioncontrol;

import com.skriptide.git.VCType;
import com.skriptide.guis.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by Matthew E on 10/23/2016 at 2:20 PM.
 */
public class VersionControlController {

    @FXML
    public Button confirmBtn;

    @FXML
    public Button cancelBtn;
    
    @FXML
    public ComboBox vcsTypeComboBox;

    public void init() {
        for (VCType types : VCType.values()) {
            vcsTypeComboBox.getItems().add(types.getName());
        }
    }

    public void onConfirm(ActionEvent actionEvent) {
        String currentItem = (String) vcsTypeComboBox.getSelectionModel().getSelectedItem();
        System.out.println(currentItem);
        Stage stage = (Stage) confirmBtn.getScene().getWindow();
        stage.close();
    }

    public void onCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
        if(SceneManager.debugMode) {
            System.out.println("Cancel vcs setup");
        }
    }
}
