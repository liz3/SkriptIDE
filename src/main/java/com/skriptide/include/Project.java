package com.skriptide.include;

import com.skriptide.Main;
import com.skriptide.config.Config;
import com.skriptide.gui.GuiType;
import com.skriptide.gui.controller.IdeGuiController;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by yannh on 27.01.2017.
 */
public class Project {
    public File getConfFile() {
        return confFile;
    }

    private File confFile;
    private Config config;
    private String name;
    private Server server;
    private Skript skript;
    private String folderPath;
    private String notes;


    public String getName() {
        return name;
    }

    public Server getServer() {
        return server;
    }

    public boolean isHasServer() {
        return hasServer;
    }

    private File skFile;
    private boolean hasServer;
    public Project(Config config, File f) {

        this.confFile = f;
        this.name = config.getString("name");
        this.folderPath = config.getString("folder-path");
        this.skript = new Skript(config.getString("sk.name"), config.getString("sk.version"), config.getString("sk.version"));
        skFile = new File(folderPath, this.name + ".sk");
        String serverPath = config.getString("server-path");
        if(serverPath != null && !serverPath.equals("")) {
            this.server = new Server(new Config(serverPath), new File(serverPath));
            hasServer = true;
        } else {
            hasServer = false;
        }
    }

    public void setServer(Server server) {
        this.server = server;
        this.hasServer = true;
    }

    public Project(String name, String path, Skript skript) {

        this.name = name;
        this.folderPath = path;
        this.hasServer = false;
        this.skript = skript;
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
            if(this.server != null) {
                this.config.set("server-path", this.server.getPath());
            }
            this.config.set("sk.name", skript.getName());
            this.config.set("sk.version", skript.getVersion());
            this.config.set("sk.path", skript.getPath());
            this.config.save();

            this.skFile = new File(this.folderPath, this.name + ".sk");
            try {
                this.skFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Main.manager.addProject(this);




        }

    }
    public void writeCode(String code) {



        FileWriter fw = null;
        try {
            fw = new FileWriter(skFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);
        try {

            bw.write(code);
            if(Main.debugMode) {
                System.out.println("Wrote code: " + skFile.getAbsolutePath());
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
    public String getCurentCode() {

        String code = "";
        try {
            InputStream stream = new FileInputStream(skFile);

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
                    System.out.println("Open code: " + skFile.getAbsolutePath());
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
