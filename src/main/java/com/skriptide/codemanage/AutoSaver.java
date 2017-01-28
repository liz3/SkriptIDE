package com.skriptide.codemanage;

import com.skriptide.gui.SceneManager;
import com.skriptide.include.Project;
import javafx.application.Platform;
import javafx.scene.control.Tab;

import java.util.LinkedHashMap;

/**
 * Created by yannh on 26.01.2017.
 */
public class AutoSaver {

    private LinkedHashMap<Project, Tab> openProjects = null;
    public AutoSaver() {

        openProjects = new LinkedHashMap<>();
        saver();
    }

    private void saver() {

        Thread saver = new Thread(() -> {

         while (true) {

             //TODO Auto save
           //  Platform.runLater(() -> SceneManager.mainWindowController.saveOpenProjects());

             System.out.println("saved");
             try {
                 Thread.sleep(15000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }

         }
        });
        saver.start();
    }

    public LinkedHashMap<Project, Tab> getOpenProjects() {
        return openProjects;
    }
}
