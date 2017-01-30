package com.skriptide;

import com.skriptide.codemanage.AutoSaver;
import com.skriptide.gui.SceneManager;

/**
 * Created by yannh on 27.01.2017.
 */
public class Main {

    public static AutoSaver saver = null;
    public static SceneManager sceneManager;
    public static Manager manager;
    public static boolean debugMode;
    public static void main(String[] args) {

        System.out.println("Pre gui working....");
        sceneManager = new SceneManager();
        manager = new Manager(sceneManager);

        System.out.println("Starting gui!");
        sceneManager.start();
    }
}
