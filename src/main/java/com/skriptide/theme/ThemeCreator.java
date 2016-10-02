package com.skriptide.theme;

import javafx.scene.Scene;

class ThemeCreator {

    public static void setTheme(Scene scene, Theme theme) {
        scene.getStylesheets().add(theme.getThemeCss());
    }

}
