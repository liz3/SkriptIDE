package com.skriptide.guis;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

/**
 * Created by Liz3ga on 14.09.2016.
 */
public class SplashController {

	@FXML
	private ProgressBar bar;

	@FXML
	private ImageView img;

	@FXML
	private Label lbl;




	public void setValue(double value, String v) {
		bar.setProgress(value);

		lbl.setText(v);
	}

	public void setImg() {


		img.setImage(new Image(getClass().getResource("/Export_1.png").toExternalForm()));


	}
}

