package com.skriptide.include;

import com.skriptide.Main;
import com.skriptide.config.Config;
import com.skriptide.util.FileUtils;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by yannh on 27.01.2017.
 */
public class Project {
    public File getConfFile() {
        return confFile;
    }

    private File confFile;
    private Config config;

    public String getFolderPath() {
        return folderPath;
    }

    private String name;
    private Server server;
    private Skript skript;
    private String folderPath;
    private String notes;
    private HashMap<String, File> skFiles;

    public HashMap<String, File> getSkFiles() {
        return skFiles;
    }

    public String getName() {
        return name;
    }

    public Server getServer() {
        return server;
    }

    public boolean isHasServer() {
        return hasServer;
    }


    private boolean hasServer;
    public Project(Config config, File f) {

        this.config = config;
        this.skFiles = new HashMap<>();
        this.confFile = f;
        this.name = config.getString("name");
        this.folderPath = config.getString("folder-path");
        this.skript = new Skript(config.getString("sk.name"), config.getString("sk.version"), config.getString("sk.version"));

        String serverPath = config.getString("server-name");
        if(serverPath != null && !serverPath.equals("")) {
            if(Main.manager.getServer().containsKey(serverPath)) {
                this.server = Main.manager.getServer().get(serverPath);
                hasServer = true;
            } else {
                Platform.runLater(() -> Main.sceneManager.infoCheck("Server not found.", "Message from Project: " + Project.this.getName(),
                        "The server the project was bound to, could not be found, so the project has no more a default Server!", Alert.AlertType.ERROR));
                hasServer = false;
            }
        } else {
            hasServer = false;
        }
        for(String str : config.getAll("files")) {

            File file = new File(this.folderPath, config.getString("files." + str));

            if(file.exists()) {
                skFiles.put(config.getString("files." + str), file);
            }
        }
    }

    public void setServer(Server server) {
        if(server == null)  {

            this.server = null;
            return;
        }
        this.server = server;
        this.hasServer = true;
        this.config.set("server-name", server.getName());
        this.config.save();
    }

    public Project(String name, String path, Skript skript) {

        this.name = name;
        this.folderPath = path;
        this.hasServer = false;
        this.skript = skript;
        this.skFiles = new HashMap<>();
    }
    public void createProject() {

        if(new File(this.folderPath).mkdir()) {

            this.confFile = new File(this.folderPath, "SkriptIDE-Project.yaml");
            try {
                this.confFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.config = new Config(this.confFile.getAbsolutePath());

            this.config.set("name", this.name);
            this.config.set("folder-path", new File(this.folderPath).getAbsolutePath());

            this.config.set("sk.name", skript.getName());
            this.config.set("sk.version", skript.getVersion());
            this.config.set("sk.path", skript.getPath());
            this.config.set("files." + this.name, this.name + ".sk");

            System.out.println("Name: "+this.name);
            System.out.println("Path: "+this.folderPath);
            File newSk = new File(this.folderPath, this.name + ".sk");
            try {
                newSk.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.skFiles.put(this.name + ".sk", newSk);
            this.config.save();
            Main.manager.addProject(this);




        }

    }
    public void copyToOutput(File f, Server server) {

        if(server != null) {

            File outpath = new File(server.getFolderPath() + "/plugins/Skript/scripts");

            FileUtils.copyFile(f, new File(outpath, f.getName()), StandardCopyOption.REPLACE_EXISTING);
        }
    }
    public boolean deleteProject() {

        this.config = null;
        this.skript = null;
        this.skFiles.clear();
        this.skFiles = null;
        System.gc();
        File folder = new File(this.folderPath);

        FileUtils.deleteDirectory(folder, true);

        if(!folder.exists()) {

            Main.manager.deleteProject(this.name);

            return true;
        }
        return false;
    }
    public boolean deleteFile(String name) {

        if(name.contains(".sk")) {

            File f = null;

            if((f = skFiles.get(name)) != null) {

                System.gc();
                 if(f.delete()) {
                     config.remove("files." + name.split(Pattern.quote("."))[0]);
                     config.save();
                     return true;
                 }
            }
        }

        return false;
    }
    public String addSkiptFile() {
        TextInputDialog input = new TextInputDialog();
        input.setGraphic(null);
        input.setTitle("New Project File");
        input.setHeaderText("New Project File: ");

        Optional<String> out = input.showAndWait();


       String name = out.get();
        System.out.println(name);
        if(name == null) {
            return null;
        }
        String t = name + ".sk";
        for(String str : skFiles.keySet()) {

            if(t.equalsIgnoreCase(str)) {
                return null;
            }

        }
        File f = new File(folderPath, t);
        try {
            if(f.createNewFile()) {

                if(config == null) {
                    return null;
                }
                config.set("files." + name, t);
                config.save();
                skFiles.put(t, f);
               // IdeGuiController.controller.initGui();

                return t;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void writeCode(String code, String name) {


        File f = null;
        if(skFiles.containsKey(name)) {
            f = skFiles.get(name);
        } else {
            return;
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);
        try {

            bw.write(code);
            if(Main.debugMode) {
                System.out.println("Wrote code: " + f.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getCurentCode(String name) {

        File f = null;
        if(skFiles.containsKey(name)) {
            f = skFiles.get(name);
        } else {
            return null;
        }

        String code = "";
        try {
            InputStream stream = new FileInputStream(f);

            InputStreamReader input = new InputStreamReader(stream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(input);

            try {

                String line;

                do {
                    line = reader.readLine();
                    if (line != null) {

                        code = code + line + System.getProperty("line.separator");
                    }
                }
                while (line != null);

                if(Main.debugMode) {
                    System.out.println("Open code: " + f.getAbsolutePath());
                }
                reader.close();
                input.close();
                stream.close();
                return code;
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Skript getSkript() {
        return skript;
    }

    public void setSkript(Skript skript) {
        this.skript = skript;
    }

    public boolean exsits() {

        if(confFile != null && confFile.exists()) {
            return true;
        }
        return false;
    }
}
