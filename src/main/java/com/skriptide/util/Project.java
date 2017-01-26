package com.skriptide.util;

import com.skriptide.config.Config;
import com.skriptide.git.VersionControl;
import com.skriptide.guis.SceneManager;
import com.skriptide.guis.idegui.IdeGuiController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by Liz3ga on 23.08.2016.
 */
public class Project {

    private String name;
    private String skriptPath;
    private Skript sk;
    private String outPath;
    private MCServer server;
    private String folder;
    private String notes;
    private VersionControl versionControl;


    public Project(String name) {

        String current = null;


        try {
            current = new File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File configFile = new File(current + "/Config.yaml");


        Config config = new Config(configFile.getAbsolutePath());


        Config project = new Config(new File(config.getString("project." + name)).getAbsolutePath());


        this.name = project.getString("name");
        this.skriptPath = project.getString("path");
        this.sk = new Skript(project.getString("skript-path"));
        this.outPath = project.getString("output-path");
        this.server = new MCServer(project.getString("server-name"));
        this.folder = project.getString("folder-path");
        this.notes = project.getString("notes");
        if (SceneManager.debugMode) {
            System.out.println("Project called");
        }


        if (SceneManager.debugMode) {
            System.out.println("Loaded project");
        }
    }

    public static ObservableList<Project> getProjects() {
        String current;


        try {
            current = new File(".").getCanonicalPath();

            File configFile = new File(current + "/Config.yaml");


            Config config = new Config(configFile.getAbsolutePath());


            List<String> sec = config.getAll("project");
            ObservableList<Project> values = FXCollections.observableArrayList();
            System.out.println(sec.size());

            for (String n : sec) {
                System.out.println(n);
                values.add(new Project(n));
            }
            if (SceneManager.debugMode) {
                System.out.println("project list called");
            }

            if (SceneManager.debugMode) {
                System.out.println("returning projects");
            }
            return values;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    private static void addProject(String name, File path) {

        String current;


        try {
            current = new File(".").getCanonicalPath();

            File configFile = new File(current + "/Config.yaml");

            Config config = new Config(configFile.getAbsolutePath());
            if (config.getString("project." + name) == null) {
                config.set("project." + name, path.getAbsolutePath());
            }

            config.save();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateProjectEntry(String name, File path) {

        String current = null;


        try {
            current = new File(".").getCanonicalPath();

            File configFile = new File(current + "/Config.yaml");

            Config config = new Config(configFile.getAbsolutePath());
            if (config.getString("project." + name) != null) {
                config.set("project." + name, path.getAbsolutePath());
            }

            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void removeProject(String name) {

        String current = null;


        try {
            current = new File(".").getCanonicalPath();

            File configFile = new File(current + "/Config.yaml");

            Config config = new Config(configFile.getAbsolutePath());
            if (config.contains("project." + name)) {
                config.remove("project." + name);
            }

            config.save();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void createProject(String name, Skript skript, MCServer server, String path, String notes) {

        for(File f :  new File( ConfigManager.getProjectsPath()).listFiles()) {

            if(f.getName().equalsIgnoreCase(name) && f.isDirectory()) {

               boolean t =  new SceneManager().infoCheck("Failed to create project","Cannot create new project",
                       "Please restart SkriptIDE to create a project with this name trough " +
                       "there has been a project with this name before the last restart.\n" +
                               "For developers: Sadly the folder of the old project is still locked in the ide somewhere, " +
                               "so i Liz3(SkriptIDE Author (31.12.2016)) tried a lot of things to delete it, but nothing worked, the old folder will be deleted on the " +
                               "restart so please restart the ide :D");

               return;
            }
        }
        String outPath = "";
        File dir = null;
        File script = null;
        File info = null;

        outPath = path;
        dir = new File(outPath);
        dir.mkdirs();


        script = new File(outPath + "/" + name + ".sk");
        info = new File(outPath + "/SkriptIDE-Project.yaml");

        try {
            script.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            info.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Config config = new Config(info.getAbsolutePath());


        config.set("name", name);
        config.set("path", script.getAbsolutePath());
        config.set("folder-path", path);
        config.set("skript-Version", skript.getVersion());
        config.set("skript-path", skript.getPath());
        config.set("server-name", server.getname());
        config.set("output-path", server.getPlFolderPath() + "/Skript/scripts");

        config.set("notes", notes);

        config.save();

        if (SceneManager.debugMode) {

            System.out.println("A Project file has been created");
        }

        addProject(name, info);

        if (SceneManager.debugMode) {
            System.out.println("crated project");
        }

    }

    public MCServer getServer() {
        return this.server;
    }

    public String getName() {
        return this.name;
    }

    public String getSkriptPath() {
        return this.skriptPath;
    }

    public Skript getSk() {
        return this.sk;
    }

    public String getOutPath() {
        return this.outPath;
    }

    private String folderPath() {
        return this.folder;
    }

    public String getNotes() {
        return this.notes;
    }

    public void runProject() {


        FileInputStream jarInPut = null;
        try {
            jarInPut = new FileInputStream(new File(skriptPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File jarOutPut = new File(outPath + "/" + name + ".sk");
        Path path = jarOutPut.toPath();
        try {
            Files.copy(jarInPut, path, REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (SceneManager.debugMode) {
            System.out.println("starting");
        }

        if(SceneManager.runningServer == null) {
            this.server.startServer();
            System.out.println("test");
        } else {

            IdeGuiController.sceneManager.sendCommand("skript reload " + this.name);
        }
    }

    private void updateProject(String newName) {

        File oldfolder = new File(this.folderPath());
        String oldPath = oldfolder.getAbsolutePath();
        String newPath = oldfolder.getAbsolutePath().substring(0, oldfolder.getAbsolutePath().length() - this.name.length()) + newName;
        System.out.println("Path is " + newPath);
        new File(newPath).mkdir();
        try {
            new File(newPath  + File.separator + newName + ".sk").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Files.move(new File(this.skriptPath).toPath(), new File(newPath  + File.separator + newName + ".sk").toPath(), REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.gc();
        System.out.println("tying to delete: "  + oldfolder.getAbsolutePath());


        oldfolder.setWritable(true);
        deleteDirectory(oldfolder);
        String outPath = "";
        File dir = null;
        File script = null;
        File info = null;


        info = new File(newPath , "SkriptIDE-Project.yaml");

        Config config = new Config(info.getAbsolutePath());


        config.set("name", newName);
        config.set("path", new File(newPath, newName + ".sk").getAbsolutePath());
        config.set("folder-path", newPath);
        config.set("skript-Version", this.sk.getVersion());
        config.set("skript-path", this.sk.getPath());
        config.set("server-name", server.getname());
        config.set("output-path", server.getPlFolderPath() + "/Skript/scripts");

        config.set("notes", notes);

        config.save();

        this.skriptPath = new File(newPath, newName + ".sk").getAbsolutePath();
        String current = null;


        try {
            current = new File(".").getCanonicalPath();

            File configFile = new File(current + "/Config.yaml");

            Config con = new Config(configFile.getAbsolutePath());
            con.set("project." + newName, newPath + File.separator +  "SkriptIDE-Project.yaml");
            con.remove("project." + this.name);
            con.save();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void moveProject() {
        //TODO

    }

    public void deleteProject() {


        boolean toDelete = new SceneManager().infoCheck("Delete Project", "Delete Project: " + this.name, "Do you really want to delete this project?");
        if (toDelete) {
            File folder = new File(this.folder);


            deleteDirectory(folder);
            removeProject(this.name);
            updateProjects();

        }

    }

    private static void updateProjects() {

        ObservableList<Project> pr = getProjects();
        SceneManager.projectsList.getItems().clear();
        for (Project prs : pr) {
            if (prs.getName() != null) {
                SceneManager.projectsList.getItems().add(prs.getName());
            }
        }
    }

    public void reNameProject(TabPane pane) {

        TextInputDialog input = new TextInputDialog();
        input.setGraphic(null);
        input.setTitle("Rename project");
        input.setHeaderText("Rename Project: " + this.name);
        Optional<String> out = input.showAndWait();

        if (out != null) {
            String trueStr = out.get();

            for(File f :  new File( ConfigManager.getProjectsPath()).listFiles() ) {

                if(f.getName().equalsIgnoreCase(trueStr) && f.isDirectory()) {

                new SceneManager().infoCheck("Failed to rename project","Cannot rename  project",
                            "Please restart SkriptIDE to rename this project with this name trough " +
                                    "there has been a project with this name before the last restart." +
                                    "For developers: Sadly the folder of the old project is still locked in the ide somewhere, " +
                                    "so i Liz3(SkriptIDE Author (31.12.2016)) tried a lot of things to delete it, but nothing worked, the old folder will be deleted on the " +
                                    "restart so please restart the ide :D");

                    return;
                }
            }
            for(Tab tab : pane.getTabs()) {
                if(tab.getText().equals(this.name)) {
                    tab.setText(trueStr);
                    break;
                }
            }
            updateProject(trueStr);
            updateProjects();
            ConfigManager.deleteEmpty();

        }

    }

    public void changeServer(String servername) {
        //TODO
    }

    private static void deleteDirectory(File directory) {
        FileUtils.deleteDirectory(directory, true);
    }

    public VersionControl getVersionControl() {
        return versionControl;
    }

    public void setVersionControl(VersionControl versionControl) {
        this.versionControl = versionControl;
    }

    public void setupVersionControl() {

    }
}
