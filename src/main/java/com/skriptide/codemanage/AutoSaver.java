package com.skriptide.codemanage;

import com.skriptide.Main;
import com.skriptide.gui.OpenFile;
import com.skriptide.gui.OpenProject;
import javafx.application.Platform;

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

                     for(OpenProject pr : Main.sceneManager.getOpenFiles()) {

                         for(OpenFile file : pr.getOpenFiles().values()) {
                             pr.getProject().writeCode(file.getArea().getText(), file.getProject().getName());
                         }
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
