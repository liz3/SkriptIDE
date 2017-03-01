package com.skriptide.util;

import com.jcraft.jsch.*;
import com.skriptide.config.Config;
import com.skriptide.gui.controller.IdeGuiController;
import com.skriptide.include.Project;
import javafx.application.Platform;

import java.io.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yannh on 20.01.2017.
 */
public class ExportSettings {

    private String host;
    private String username;
    private String password;
    private String exportPath;


    public ExportSettings(String host, String username, String password, String exportPath) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.exportPath = exportPath;
    }


    public static void addEntry(ExportSettings s, String name) {

        Config conf = new Config("hosts.yaml");


        if (conf.getString("host." + name) == null) {

            conf.set("host." + name + ".address", s.getHost());
            conf.set("host." + name + ".username", s.getUsername());
            conf.set("host." + name + ".password", s.getPassword());
            conf.set("host." + name + ".path", s.getExportPath());

            conf.save();


        }
    }

    public static void removeEntry(String name) {

        Config conf = new Config("hosts.yaml");

        if (conf.getString("host." + name + ".address") != null) {
            System.out.println("deleting");
            conf.remove("host." + name);
            conf.save();
        }
    }

    public static HashMap<String, ExportSettings> getAll() {

        HashMap<String, ExportSettings> values = new HashMap<>();

        Config conf = new Config("hosts.yaml");


        List<String> all = conf.getAll("host");

        for (String entry : all) {

            values.put(entry, new ExportSettings(conf.getString("host." + entry + ".address"), conf.getString("host." + entry + ".username"), conf.getString("host." + entry + ".password"), conf.getString("host." + entry + ".path")));
        }

        return values;
    }

    public static boolean checkFile() {

        File conf = new File("hosts.yaml");

        if (conf.exists()) {
            return true;
        }
        try {
            conf.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void deploy(Project project) {

    }

    public void deploy(String code, String name) {


        Thread deployer = new Thread(new Runnable() {
            @Override
            public void run() {

                ExportSettings instance = ExportSettings.this;
                JSch jsch = new JSch();
                int port = 22;
                String trueHost = "";
                if (instance.host.contains(":")) {

                    port = Integer.parseInt(instance.host.split(":")[1]);
                    trueHost = instance.host.split(":")[0];

                } else {
                    trueHost = instance.host;
                }
                try {
                    Session session = jsch.getSession(instance.username, trueHost, port);
                    java.util.Properties config = new java.util.Properties();
                    config.put("StrictHostKeyChecking", "no");
                    session.setConfig(config);
                    session.setPassword(instance.password);
                    session.connect();
                    Channel channel = session.openChannel("sftp");
                    channel.connect();

                    ChannelSftp c = (ChannelSftp) channel;

                    c.cd(instance.exportPath);
                    File temp = null;
                    try {
                         temp = File.createTempFile("temp" + name, ".sk");

                        FileWriter fw = null;
                        try {
                            fw = new FileWriter(temp.getAbsoluteFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        BufferedWriter bw = new BufferedWriter(fw);
                        try {

                            bw.write(code);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            bw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    c.put(new FileInputStream(temp), name);
                    Platform.runLater(() -> {
                        IdeGuiController.controller.getStateLabel().setText("Successfully Exported to " + ExportSettings.this.host);

                        new Thread(() -> {

                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            Platform.runLater(() -> IdeGuiController.controller.getStateLabel().setText("Ready"));

                        }).start();
                    });
                    c.exit();
                    session.disconnect();

                } catch (JSchException | SftpException | FileNotFoundException e) {
                    Platform.runLater(() -> {
                        IdeGuiController.controller.getStateLabel().setText("Failed export to " + ExportSettings.this.host);

                        new Thread(() -> {

                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException x) {
                                x.printStackTrace();
                            }

                            Platform.runLater(() -> IdeGuiController.controller.getStateLabel().setText("Ready"));

                        }).start();
                    });
                }

            }
        });
        deployer.setName("SFTP Deployer thread");
        deployer.start();


    }

    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getExportPath() {
        return exportPath;
    }
}
