package com.skriptide;

import com.skriptide.config.Config;
import com.skriptide.gui.Gui;
import com.skriptide.gui.GuiType;
import com.skriptide.gui.SceneManager;
import com.skriptide.gui.controller.IdeGuiController;
import com.skriptide.include.*;
import com.skriptide.util.ExportSettings;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by yannh on 27.01.2017.
 */
public class Manager {

    private SceneManager sceneManager;
    private Config settingsConf;
    private Config projectsConf;

    public HashMap<String, Api> getApis() {
        return apis;
    }

    private Config serverConf;
    private Config apisConf;
    private Config skriptsConf;
    private Config addonsConf;


    private HashMap<String, Project> projects;
    private HashMap<String, Server> servers;

    public HashMap<String, Server> getServers() {
        return servers;
    }

    public HashMap<String, Skript> getSkripts() {
        return skripts;
    }

    public HashMap<String, Addon> getAddons() {
        return addons;
    }

    private HashMap<String, Api> apis;
    private HashMap<String, Skript> skripts;
    private HashMap<String, Addon> addons;

    private String projectsPath;
    private String serversPath;

    //0 failed to load project
    //1 failed to load server
    //2 failed to load api
    //3 failed to load skript
    //4 failed to save project
    //5 failed to save server
    //6 failed to save api
    //7 failed to save skript
    //8 failed to save addon
    //9 failed to load addon
    private HashMap<String, Integer> errors;

    public Manager(SceneManager manager) {

        projects = new LinkedHashMap<>();
        servers = new LinkedHashMap<>();
        apis = new LinkedHashMap<>();
        skripts = new LinkedHashMap<>();
        addons = new LinkedHashMap<>();
        errors = new HashMap<>();
        this.sceneManager = manager;
    }

    public boolean checkFiles() {


        ExportSettings.checkFile();
        boolean isNew = false;
        File file = new File("settings.yaml");
        if (!file.exists()) {
            isNew = true;
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        settingsConf = new Config(file.getAbsolutePath());
        file = new File("projects.yaml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        projectsConf = new Config(file.getAbsolutePath());
        file = new File("server.yaml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        serverConf = new Config(file.getAbsolutePath());
        file = new File("apis.yaml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        apisConf = new Config(file.getAbsolutePath());

        file = new File("skripts.yaml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        skriptsConf = new Config(file.getAbsolutePath());
        file = new File("addons.yaml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        addonsConf = new Config(file.getAbsolutePath());

        if(settingsConf.getString("configured") == null)
            isNew = true;

        return isNew;
    }
    public void createFolders() throws IOException {

        System.out.println("open");
        if (!new File(this.projectsPath).exists()) {
            Path pathToFile = Paths.get(this.projectsPath + "\\data.yaml");
            try {
                Files.createDirectories(pathToFile.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Files.createFile(pathToFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            File f = new File(pathToFile.toUri());
            f.delete();
        }
        if (!new File(this.serversPath).exists()) {
            Path pathToFile = Paths.get(this.serversPath + "\\data.yaml");
            Files.createDirectories(pathToFile.getParent());
            Files.createFile(pathToFile);
            File f = new File(pathToFile.toUri());
            f.delete();
        }
    }
    public void loadIn() {

        projectsPath = settingsConf.getString("paths.projects");
        serversPath = settingsConf.getString("paths.servers");

        for(String str : projectsConf.getAll("project")) {

            File f = new File(projectsConf.getString("project." + str));

            if(f.exists()) {

                projects.put(str, new Project(new Config(f.getAbsolutePath()), f));
            } else {
                errors.put(str, 0);
            }

        }
        for(String str : serverConf.getAll("server")) {

            File f = new File(serverConf.getString("server." + str));

            if(f.exists()) {

                servers.put(str, new Server(new Config(f.getAbsolutePath()), f));
            } else {
                errors.put(str, 1);
            }

        }
        for(String str : apisConf.getAll("api")) {

            apis.put(apisConf.getString("api." + str + ".path"), new Api(apisConf.getString("api." + str + ".name"),
                    apisConf.getString("api." + str + ".version"),apisConf.getString("api." + str + ".path")));
        }
        for(String str : skriptsConf.getAll("skript")) {

            skripts.put(skriptsConf.getString("skript." + str + ".path"), new Skript(skriptsConf.getString("skript." + str + ".name"),
                    skriptsConf.getString("skript." + str + ".version"),skriptsConf.getString("skript." + str + ".path")));
        }
        for(String str : addonsConf.getAll("addon")) {

            addons.put(addonsConf.getString("addon." + str + ".path"), new Addon(addonsConf.getString("addon." + str + ".name"),
                    addonsConf.getString("addon." + str + ".version"),addonsConf.getString("addon." + str + ".path")));
        }

        //TODO load in existing project
    }

    public void setProjectsPath(String path) {

        this.projectsPath = path;

        settingsConf.set("paths.projects", path);
        settingsConf.save();
    }

    public void setServersPath(String path) {

        this.serversPath = path;

        settingsConf.set("paths.servers", path);
        settingsConf.save();
    }

    public String getProjectsPath() {
        return projectsPath;
    }

    public String getServersPath() {

        return serversPath;
    }

    public void addProject(Project project) {

        projectsConf.set("project." + project.getName(), project.getConfFile().getAbsolutePath());
        projectsConf.save();
        projects.put(project.getName(), project);


    }

    public void addServer(Server server) {

        //TODO make

    }

    public boolean addSkript(Skript skript) {

        if(skriptsConf.getString("skript." + skript.getName()) != null) {
            return false;
        }
        skriptsConf.set("skript." + skript.getName() + ".name", skript.getName());
        skriptsConf.set("skript." + skript.getName() + ".version", skript.getVersion());
        skriptsConf.set("skript." + skript.getName() + ".path", skript.getPath());
        skriptsConf.save();
        skripts.put(skript.getName(), skript);
        return true;
    }

    public boolean addApi(Api api) {

        if(apisConf.getString("api." + api.getName()) != null) {
            return false;
        }
        apisConf.set("api." + api.getName() + ".name", api.getName());
        apisConf.set("api." + api.getName() + ".version", api.getVersion());
        apisConf.set("api." + api.getName() + ".path", api.getPath());
        apisConf.save();
        apis.put(api.getName(), api);
        return true;

    }
    public boolean addAddon(Addon addon) {

        if(addonsConf.getString("addon." + addon.getName()) != null) {
            return false;
        }
        addonsConf.set("addon." + addon.getName() + ".name", addon.getName());
        addonsConf.set("addon." + addon.getName() + ".version", addon.getVersion());
        addonsConf.set("addon." + addon.getName() + ".path", addon.getPath());
        addonsConf.save();
        addons.put(addon.getName(), addon);
        return true;

    }
    public void removeApi(String path) {


        //TODO Handle if project or server contains
        if(apisConf.getString("api."+ path + ".name") != null) {
            apisConf.remove("api." + path);
            apisConf.save();
            apis.remove(path);
        }
    }
    public void removeSkript(String path) {

        //TODO Handle if project or server contains

        if(skriptsConf.getString("skript."+ path + ".name") != null) {
            skriptsConf.remove("skript." + path);
            skriptsConf.save();
            skripts.remove(path);
        }
    }
    public void removeAddon(String path) {

        //TODO Handle if project or server contains

        if(addonsConf.getString("addon."+ path + ".name") != null) {
            addonsConf.remove("addon." + path);
            addonsConf.save();
            addons.remove(path);
        }
    }
    public void setConfigured() {

        settingsConf.set("configured", "true");
        settingsConf.save();
    }

    public HashMap<String,Project> getProjects() {


        return projects;
    }
    public ObservableList<Api> getApisForGui() {

        ObservableList<Api> values = FXCollections.observableArrayList();
        values.addAll(apis.values());
        return values;
    }
    public ObservableList<Skript> getSkriptsForGui() {

        ObservableList<Skript> values = FXCollections.observableArrayList();
        values.addAll(skripts.values());
        return values;
    }
    public ObservableList<Addon> getAddonsForGui() {

        ObservableList<Addon> values = FXCollections.observableArrayList();
        values.addAll(addons.values());
        return values;
    }
    public HashMap<String,Server> getServer() {


        return servers;
    }

    public HashMap<String, Integer> getErrors() {
        return errors;
    }
}
