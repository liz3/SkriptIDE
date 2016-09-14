package com.skriptide.guis;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Liz3ga on 14.09.2016.
 */
public class SplashController {

	@FXML
	private ProgressBar bar;

	@FXML
	public ImageView img;



	public void setValue(int value) {
		bar.setProgress(value);
	}

}

