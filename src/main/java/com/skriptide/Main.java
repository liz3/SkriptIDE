package com.skriptide;

import com.skriptide.codemanage.AutoSaver;
import com.skriptide.gui.SceneManager;
import com.skriptide.include.Server;
import com.skriptide.util.IDESettings;

/**
 * Created by yannh on 27.01.2017.
 */
public class Main {

    public static IDESettings settings;
    public static AutoSaver saver = null;
    public static SceneManager sceneManager;
    public static Manager manager;
    public static boolean debugMode;
    public static Server runningServer = null;
    public static void main(String[] args) {

        System.out.println("Pre gui working....");
        sceneManager = new SceneManager();
        manager = new Manager(sceneManager);

        System.out.println("Starting gui!");
        sceneManager.start();
    }
}
