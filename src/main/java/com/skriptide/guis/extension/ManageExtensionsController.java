package com.skriptide.guis.extension;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

/**
 * Created by Matthew E on 10/23/2016 at 2:20 PM.
 */
public class ManageExtensionsController {

    @FXML
    public Button scriptAddBtn;

    @FXML
    public Label infoLabel;

    @FXML
    public Pane pane;

    @FXML
    public Button removeButton;

    @FXML
    public Tab tab;

    @FXML
    public TableView<String> scriptTable;

}
