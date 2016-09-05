package com.skriptide.guis;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Created by Liz3ga on 04.09.2016.
 */
public class DebuggerController {

	@FXML
	private TextArea area;

	public void setOut() {


		SceneManager.debugArea = area;

	}

}
