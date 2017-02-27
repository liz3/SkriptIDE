package com.skriptide.codemanage.error;

import com.skriptide.Main;
import com.skriptide.gui.OpenFile;
import com.skriptide.gui.OpenProject;
import com.skriptide.gui.controller.IdeGuiController;
import javafx.application.Platform;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by yannh on 25.02.2017.
 */
public class ErrorHandler {

    private ErrorServer errorServer;
    private HashMap<String, OpenFile> projects;
    private HashMap<String, ErrorEntry> errors;
    private ArrayList<String> toRemove;
    private int count = 0;
    private int errorCount = 0;
    private int old = 0;


    public ErrorHandler() {

        toRemove = new ArrayList<>();
        errors = new HashMap<>();
        projects = new HashMap<>();
        errorServer = new ErrorServer();


    }

    public void start() {
        if (errorServer.isRunning()) {
            return;
        }

        errorServer.start();

        new Thread(() -> {


            int t = 0;
            while (!errorServer.isReady()) {

                t++;
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Error Server ready");

            Platform.runLater(() -> {
                IdeGuiController.controller.getStateLabel().setText("Ready");
                handle();
            });


        }).start();
    }

    public void triggerClicked(String clicked) {


        System.out.println("casted");
        for (ErrorEntry e : errors.values()) {

            String t = "[" + e.getFile().getProject().getName() + "]" + e.getType().toString() + " At: " + e.getLine() + ": " + e.getText();

            if (t.equalsIgnoreCase(clicked)) {

                int line = e.getLine();


                int lenght = 0;
                String[] sp = e.getFile().getArea().getText().split(Pattern.quote("\n"));

                for (int i = 0; i != line; i++) {
                    lenght += sp[i].length();
                }

                int finalLenght = lenght;
                Platform.runLater(() -> {
                    if (e.getFile().isExtern()) {
                        e.getFile().getExternWindow().getStage().requestFocus();
                    } else {
                        e.getFile().getTabPane().getSelectionModel().select(e.getFile().getTab());

                    }

                    e.getFile().getArea().requestFocus();
                    e.getFile().getArea().moveTo(finalLenght);

                });
                return;
            }
        }


    }

    private void handle() {

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(errorServer.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(errorServer.getInputStream()));
        new Thread(() -> {


            while (errorServer.isRunning() && errorServer.isReady()) {


                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
           /*     for(String str : projects.keySet()) {
                    try {
                        writer.write("skript disable " + str);
                        writer.newLine();
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } */
                projects.clear();
                errors.clear();

                for (OpenProject p : Main.sceneManager.getOpenFiles()) {

                    for (OpenFile f : p.getOpenFiles().values()) {

                        projects.put(f.getOpenProject().getProject().getName() + "-" + f.getProject().getName(), f);


                    }
                }


                File scripts = new File(errorServer.getPluginFolder() + "/Skript", "scripts");

                for (File s : scripts.listFiles()) {
                    if (s.delete()) {


                    } else {
                        System.out.println("failed to reload");
                    }

                }
                System.gc();
                for (String name : projects.keySet()) {

                    OpenFile f = projects.get(name);

                    File skriptFile = new File(scripts, name);


                    try {
                        skriptFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        PrintWriter w = new PrintWriter(new FileWriter(skriptFile));

                        w.println(f.getArea().getText());

                        w.flush();
                        w.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                try {
                    writer.write("rl");
                    writer.newLine();
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                System.out.println("enabled all");


                if (projects.size() == 0) {
                    System.out.println("No projects, continuing");
                    errors.clear();
                    IdeGuiController.controller.getErrorList().getItems().clear();
                    toRemove.clear();
                    continue;
                }

                new Thread(() -> {
                    String line = "";

                    try {
                        while ((line = reader.readLine()) != null) {


                            if (line.contains("WARN]: [Skript]") || line.contains(" ERROR]: [Skript]")) {


                                errorCount++;

                                String[] split = line.split(" ");
                                ErrorType type = null;
                                for (int i = 0; i != split.length; i++) {
                                    try {
                                        String c = split[i];
                                        if (c.endsWith("ERROR]")) {
                                            type = ErrorType.ERROR;
                                        }
                                        if (c.equalsIgnoreCase("line")) {

                                            int l = Integer.valueOf(split[i + 1].substring(0, split[i + 1].length() - 1));
                                            String from = split[i - 1].substring(1, split[i - 1].length() - 1);
                                            String msg = "";
                                            for (int x = 2; x != split.length; x++) {
                                                msg += " " + split[x];
                                            }
                                            OpenFile f = projects.get(from);

                                            if (type == null) {
                                                type = ErrorType.WARN;
                                            }

                                            if (!errors.containsKey(msg)) {

                                                errors.put(msg, new ErrorEntry(l, type, f, msg.substring(1)));
                                            }

                                        }
                                    } catch (Exception e) {
                                        continue;
                                    }
                                }

                            } else if (line.contains("[Skript] Loaded")) {
                                break;
                            }
                        }


                        errorCount = 0;
                        Platform.runLater(() -> {


                            ArrayList<String> toRem = new ArrayList<>();
                            for (String item : IdeGuiController.controller.getErrorList().getItems()) {

                                if (toRemove.contains(item)) {

                                    boolean found = false;
                                    String trueK = "";
                                    for (String key : errors.keySet()) {
                                        ErrorEntry e = errors.get(key);
                                        String t = "[" + e.getFile().getProject().getName() + "]" + e.getType().toString() + " At: " + e.getLine() + ": " + e.getText();
                                        if (t.equalsIgnoreCase(item)) {
                                            found = true;
                                            trueK = key;
                                        }
                                    }
                                    if (!found) {
                                        toRem.add(item);
                                        toRemove.remove(item);
                                    } else {
                                        toRemove.remove(item);
                                        errors.remove(trueK);
                                    }

                                } else {


                                    boolean f = false;
                                    String truek = "";
                                    for (String key : errors.keySet()) {
                                        ErrorEntry e = errors.get(key);
                                        String t = "[" + e.getFile().getProject().getName() + "]" + e.getType().toString() + " At: " + e.getLine() + ": " + e.getText();
                                        if (t.equalsIgnoreCase(item)) {

                                            f = true;
                                            truek = key;
                                        }
                                    }
                                    if (!f) {
                                        toRemove.add(item);
                                    } else {
                                        errors.remove(truek);
                                    }
                                }
                            }

                            for (String item : toRem) {
                                IdeGuiController.controller.getErrorList().getItems().remove(item);
                            }
                            toRem = null;


                            for (ErrorEntry e : errors.values()) {

                                try {
                                    IdeGuiController.controller.getErrorList().getItems().add("[" + e.getFile().getProject().getName() + "]" + e.getType().toString() + " At: " + e.getLine() + ": " + e.getText());

                                } catch (Exception w) {
                                    continue;
                                }
                            }

                            old = errors.size();

                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }).start();
    }

    public ErrorServer getErrorServer() {
        return errorServer;
    }

}
