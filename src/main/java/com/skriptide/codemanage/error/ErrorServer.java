package com.skriptide.codemanage.error;

import com.skriptide.gui.controller.IdeGuiController;
import com.skriptide.include.Api;
import com.skriptide.include.Skript;
import com.skriptide.util.FileUtils;
import javafx.application.Platform;

import java.io.*;
import java.nio.file.StandardCopyOption;

/**
 * Created by yannh on 25.02.2017.
 */
public class ErrorServer {

    private Thread t;
    private Process process;
    private boolean running;
    private boolean ready;
    private File pluginFolder;
    private InputStream inputStream;
    private OutputStream outputStream;


    private Api api;
    private Skript skript;


    public ErrorServer() {

    }

    public void setup() {


        if (api == null || skript == null) {
            System.out.println("api or skript null");
            return;
        }
        File f = new File("errsrv");
        pluginFolder = new File(f, "plugins");
        if (!f.exists()) {

            if (!f.mkdir()) {
                return;
            }



            if (!pluginFolder.exists()) {
                if (!pluginFolder.mkdir()) {
                    return;
                }
            }


        }
        FileUtils.copyFile(new File(api.getPath()), new File(f, "Server.jar"), StandardCopyOption.REPLACE_EXISTING);
        FileUtils.copyFile(new File(skript.getPath()), new File(pluginFolder, "Skript.jar"), StandardCopyOption.REPLACE_EXISTING);

        //server.properties
        File eula = new File(f, "eula.txt");

        if (!eula.exists()) {

            try {
                if (eula.createNewFile()) {

                    PrintWriter writer = new PrintWriter(new FileWriter(eula));

                    writer.println("#By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula).\n" +
                            "#Wed Feb 15 00:38:27 CET 2017\n" +
                            "eula=true\n");
                    writer.flush();
                    writer.close();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        File props = new File(f, "server.properties");

        if (!props.exists()) {

            try {
                if (props.createNewFile()) {

                    PrintWriter writer = new PrintWriter(new FileWriter(props));

                    writer.println("generator-settings=\n" +
                            "op-permission-level=4\n" +
                            "allow-nether=false\n" +
                            "level-name=world\n" +
                            "enable-query=false\n" +
                            "allow-flight=false\n" +
                            "announce-player-achievements=false\n" +
                            "server-port=11112\n" +
                            "max-world-size=15000\n" +
                            "level-type=SUPER_FLAT\n" +
                            "enable-rcon=false\n" +
                            "level-seed=\n" +
                            "force-gamemode=false\n" +
                            "max-build-height=256\n" +
                            "spawn-npcs=false\n" +
                            "white-list=false\n" +
                            "spawn-animals=false\n" +
                            "hardcore=false\n" +
                            "snooper-enabled=true\n" +
                            "resource-pack-sha1=\n" +
                            "online-mode=true\n" +
                            "resource-pack=\n" +
                            "pvp=false\n" +
                            "difficulty=0\n" +
                            "enable-command-block=false\n" +
                            "gamemode=1\n" +
                            "max-players=1\n" +
                            "spawn-monsters=false\n" +
                            "generate-structures=false\n" +
                            "view-distance=1\n" +
                            "motd=SkriptIDE error Server\n");
                    writer.flush();
                    writer.close();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        t = new Thread(() -> {


            ProcessBuilder builder = new ProcessBuilder("java", "-jar", new File(f, "Server.jar").getAbsolutePath());

            builder.directory(f);
            try {
                process = builder.start();

                inputStream = process.getInputStream();
                outputStream = process.getOutputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }

            running = true;

            waitForReady();
        });
        t.setName("Error Server Thread");


    }

    public void start() {
        if (!running && !ready && api != null && skript != null) {
            t.start();
        } else {
            Platform.runLater(() -> IdeGuiController.controller.getStateLabel().setText("Ready."));
        }

    }

    public void stop() throws IOException {

        if(running && ready) {
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(outputStream));

            w.write("stop");
            w.flush();
            w.close();

        }
    }

    private void waitForReady() {

        if(!running || ready) {
            return;
        }

        new Thread(() -> {

            BufferedReader reeader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";

            try {
                while ((line = reeader.readLine()) != null) {
                    if(line.contains("INFO]: Done")) {
                        ready = true;
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public Thread getT() {
        return t;
    }


    public Process getProcess() {
        return process;
    }

    public boolean isRunning() {
        return running;
    }


    public boolean isReady() {
        return ready;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public void setSkript(Skript skript) {
        this.skript = skript;
    }

    public File getPluginFolder() {
        return pluginFolder;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}
