package com.skriptide.main;


import com.skriptide.guis.SceneManager;
import com.skriptide.util.Config;

/**
 * Created by Liz3ga on 26.07.2016.
 */
public class Main {

    public static SceneManager sceneManager;
    public static boolean debugMode;

    public static void main(String[] args) {


        try {
            debugMode = Config.isDebug();
        }catch (Exception e) {
            System.err.println("First run?");
        }
        sceneManager = new SceneManager();
        sceneManager.runMain();


    }

}
