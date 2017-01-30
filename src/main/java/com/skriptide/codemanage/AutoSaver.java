package com.skriptide.codemanage;

import com.skriptide.Main;
import com.skriptide.gui.OpenProject;
import com.skriptide.gui.SceneManager;
import com.skriptide.include.Project;
import javafx.application.Platform;
import javafx.scene.control.Tab;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by yannh on 26.01.2017.
 */
public class AutoSaver {


    public AutoSaver() {


        saver();
    }

    private void saver() {

        Thread saver = new Thread(() -> {

         while (true) {

             Platform.runLater(new Runnable() {
                 @Override
                 public void run() {

                     for(OpenProject pr : Main.sceneManager.getOpenProjects()) {

                         pr.getProject().writeCode(pr.getArea().getText());
                     }
                 }
             });


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


}
