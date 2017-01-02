package com.skriptide.theme;

import javafx.scene.Scene;

public class ThemeCreator {

    public static void setTheme(Scene scene, Theme theme) {
        scene.getStylesheets().add(theme.getThemeCss());
    }

}
