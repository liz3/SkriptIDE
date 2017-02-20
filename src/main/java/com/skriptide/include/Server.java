package com.skriptide.include;

import com.skriptide.Main;
import com.skriptide.config.Config;
import com.skriptide.gui.controller.IdeGuiController;
import com.skriptide.util.FileUtils;
import javafx.scene.control.Alert;

import java.io.*;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by yannh on 27.01.2017.
 */
public class Server {

    private Config config;
    private File configFile;
    private File folderFile;
    private String name;
    private String folderPath;
    private String skriptsPath;
    private Skript skript;
    private Api api;
    private int port;
    private String pluginsPath;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean configured;

    private List<Addon> serverAddons;
    private String startArgs;
    private String generatorSettings;
    private int opPermLevel;
    private long maxWorldSize;
    private int maxPlayers;
    private String worldName;
    private String levelSeed;
    private long maxBuildHeight;
    private int viewDistance;
    private String modt;
    private String lvlType;
    private int difficulty;
    private int defaultGm;
    private String pack;
    private String packSHA1;
    private boolean generateStructure;
    private boolean allowNether;
    private boolean query;
    private boolean allowFlight;
    private boolean announceAchievemnts;
    private boolean rcon;
    private boolean forceGamemode;
    private boolean whitelist;
    private boolean spawnNPCS;
    private boolean spawnAnimals;
    private boolean hardcore;
    private boolean snooper;
    private boolean pvp;
    private boolean allowCMD;
    private boolean onlineMode;
    private boolean spawnMonsters;

    public Server(String name, Api api, Skript skript, File folderPath, String startArgs, int port) {

        this.name = name;
        this.api = api;
        this.skript = skript;
        this.folderPath = folderPath.getAbsolutePath();
        this.configured = false;
        this.startArgs = startArgs;
        this.port = port;
        this.configFile = new File(this.folderPath, "SkriptIDE-server.yaml");
        folderFile = folderPath;
        serverAddons = new ArrayList<>();
        this.pluginsPath = new File(this.folderPath, "plugins").getAbsolutePath();

    }

    public Server(Config config, File folder) {


        this.config = config;
        this.folderPath = new File(config.getString("folder-path")).getAbsolutePath();
        this.name = config.getString("name");
        this.startArgs = config.getString("args");
        this.folderFile = new File(config.getString("folder-path"));
        this.skript = Main.manager.getSkripts().get(config.getString("sk.name"));
        this.api = Main.manager.getApis().get(config.getString("api.name"));
        serverAddons = new ArrayList<>();
        this.pluginsPath = new File(this.folderPath, "plugins").getAbsolutePath();
        for(String key : config.getAll("addon")) {

            serverAddons.add(Main.manager.getAddons().get(config.getString("addon." + key)));
        }

    }
    public void loadConfiguration() {

        File props;

        props = new File(this.folderPath + "/server.properties");
        try {
            Scanner sc = new Scanner(props);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String value;
                if (line.contains("=")) {
                    String[] split = line.split(Pattern.quote("="));
                    String key = split[0];
                    if (split.length == 2) {
                        value = split[1];
                    } else {
                        value = "";
                    }


                    switch (key) {

                        case "generator-settings":
                            this.generatorSettings = value;
                            break;

                        case "op-permission-level":
                            this.opPermLevel = Integer.parseInt(value);
                            break;

                        case "allow-nether":
                            this.allowNether = Boolean.valueOf(value);
                            break;

                        case "level-name":
                            this.worldName = value;
                            break;

                        case "enable-query":
                            this.query = Boolean.valueOf(value);
                            break;

                        case "allow-flight":
                            this.allowFlight = Boolean.valueOf(value);
                            break;

                        case "announce-player-achievements":
                            this.announceAchievemnts = Boolean.valueOf(value);
                            break;

                        case "server-port":
                            this.port = Integer.parseInt(value);
                            break;

                        case "max-world-size":
                            this.maxWorldSize = Long.parseLong(value);
                            break;

                        case "level-type":
                            this.lvlType = value;
                            break;

                        case "enable-rcon":
                            this.rcon = Boolean.valueOf(value);
                            break;

                        case "level-seed":
                            this.levelSeed = value;
                            break;

                        case "force-gamemode":
                            this.forceGamemode = Boolean.valueOf(value);
                            break;

                        case "max-build-height":
                            this.maxBuildHeight = Integer.parseInt(value);
                            break;

                        case "spawn-npcs":
                            this.spawnNPCS = Boolean.valueOf(value);

                            break;

                        case "white-list":
                            this.whitelist = Boolean.valueOf(value);
                            break;

                        case "spawn-animals":
                            this.spawnAnimals = Boolean.valueOf(value);
                            break;

                        case "hardcore":
                            this.hardcore = Boolean.valueOf(value);
                            break;

                        case "snooper-enabled":
                            this.snooper = Boolean.valueOf(value);
                            break;

                        case "resource-pack-sha1":
                            this.packSHA1 = value;
                            break;

                        case "online-mode":
                            this.onlineMode = Boolean.valueOf(value);
                            break;

                        case "resource-pack":
                            this.pack = value;
                            break;

                        case "pvp":
                            this.pvp = Boolean.valueOf(value);
                            break;

                        case "difficulty":
                            this.difficulty = Integer.parseInt(value);
                            break;

                        case "enable-command-block":
                            this.allowCMD = Boolean.valueOf(value);
                            break;

                        case "gamemode":
                            this.defaultGm = Integer.parseInt(value);
                            break;

                        case "max-players":
                            this.maxPlayers = Integer.parseInt(value);
                            break;

                        case "spawn-monsters":
                            this.spawnMonsters = Boolean.valueOf(value);
                            break;

                        case "generate-structures":
                            this.generateStructure = Boolean.valueOf(value);
                            break;

                        case "view-distance":
                            this.viewDistance = Integer.parseInt(value);
                            break;

                        case "motd":
                            this.modt = value;
                            break;
                    }
                }

            }
            sc.close();

            if (Main.debugMode) {

                System.out.println("Loaded propeties File for a minecraft Server from path:" + props.getAbsolutePath());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void deleteServer() {

        if(Main.runningServer != null) {
            Main.sceneManager.infoCheck("Error","Failed to delete Server", "A server cannot be deleted while another or the server is running!", Alert.AlertType.ERROR);
            return;
        }
        String folderPath = folderFile.getAbsolutePath();
        this.folderFile = null;
        this.skript = null;
        this.api = null;
        this.serverAddons = null;
        this.config = null;
        System.gc();
        FileUtils.deleteDirectory(new File(folderPath), true);
        for(Project pr : Main.manager.getProjects().values()) {
            if(pr.getServer() == null) {
                continue;
            }
            if(pr.getServer().getName().equals(this.name)) {
                pr.setServer(null);
            }
        }
        Main.manager.deleteServer(this.name);
        IdeGuiController.controller.importServers();

    }
    public void updateServer() {

        if(Main.runningServer != null) {
            Main.sceneManager.infoCheck("Error", "Failed to save Settnigs",
                    "Could not save the configuration of the server, because another server is running, please stop it before!", Alert.AlertType.ERROR);
            return;
        }
        File pluginsDir = new File(pluginsPath);

        for(File f : pluginsDir.listFiles()) {

            if(f.getName().equals("Skript.jar") || f.getName().equals("Skript")) {
                continue;
            }

            f.delete();
        }
        File props = new File(this.folderPath + "/server.properties");
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(props));

            pw.println("generator-settings=" + this.getGeneratorSettings());
            pw.println("op-permission-level=" + this.opPermLevel);
            pw.println("allow-nether=" + Boolean.toString(this.allowNether));
            pw.println("level-name=" + this.worldName);
            pw.println("enable-query=" + Boolean.toString(this.query));
            pw.println("allow-flight=" + Boolean.toString(allowFlight));
            pw.println("announce-player-achievements=" + Boolean.toString(announceAchievemnts));
            pw.println("server-port=" + this.port);
            pw.println("max-world-size=" + this.maxWorldSize);
            pw.println("level-type=" + this.lvlType);
            pw.println("enable-rcon=" + Boolean.toString(this.rcon));
            pw.println("level-seed=" + this.levelSeed);
            pw.println("force-gamemode=" + Boolean.toString(this.forceGamemode));
            pw.println("max-build-height=" + this.maxBuildHeight);
            pw.println("spawn-npcs=" + Boolean.toString(this.spawnNPCS));
            pw.println("white-list=" + Boolean.toString(this.whitelist));
            pw.println("spawn-animals=" + Boolean.toString(this.spawnAnimals));
            pw.println("hardcore=" + Boolean.toString(this.hardcore));
            pw.println("snooper-enabled=" + Boolean.toString(this.snooper));
            pw.println("resource-pack-sha1=" + this.packSHA1);
            pw.println("online-mode=" + Boolean.toString(this.onlineMode));
            pw.println("resource-pack=" + this.pack);
            pw.println("pvp=" + Boolean.toString(this.pvp));
            pw.println("difficulty=" + this.difficulty);
            pw.println("enable-command-block=" + Boolean.toString(this.allowCMD));
            pw.println("gamemode=" + this.defaultGm);
            pw.println("max-players=" + this.maxPlayers);
            pw.println("spawn-monsters=" + Boolean.toString(this.spawnMonsters));
            pw.println("generate-structures=" + Boolean.toString(this.generateStructure));
            pw.println("view-distance=" + this.viewDistance);
            pw.println("motd=" + this.modt);
            pw.flush();
            pw.close();

            this.config.set("name", this.getName());
            this.config.set("sk.name", this.skript.getPath());
            this.config.set("api.name", this.api.getPath());
            this.config.set("args", this.startArgs);

            this.config.save();
            this.config.remove("addon");
            for (Addon addon : this.serverAddons) {

                this.config.set("addon." + addon.getName(), addon.getPath());
                this.config.save();


                FileUtils.copyFile(new File(addon.getPath()), new File(pluginsDir, addon.getName() + ".jar"), StandardCopyOption.REPLACE_EXISTING);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void startServer() {

        if(Main.runningServer == null) {

            Main.runningServer = this;
            Thread t = new Thread(() -> {

                int length = startArgs.split(" ").length;
                String[] cmd = new String[3];

                cmd[0] = "java";
                cmd[1] = "-jar";
                cmd[2] = "Server.jar";
               /* for(int i = 3; i != 3 + length; i++) {

                    cmd[i] = startArgs.split(" ")[i];
                } */
                ProcessBuilder pb = new ProcessBuilder(cmd);
                pb.directory(folderFile);
                Process p = null;

                try {
                    p = pb.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Process finalP = p;
                Thread writer = new Thread(() -> {
                    assert finalP != null;
                    Server.this.writer = new BufferedWriter(new OutputStreamWriter(finalP.getOutputStream()));
                });


                Thread reader = new Thread(() -> {

                    assert finalP != null;
                    Server.this.reader = new BufferedReader(new InputStreamReader(finalP.getInputStream()));

                    String line = "";

                    try {
                        while ((line = Server.this.reader.readLine()) != null) {
                            String finalLine = line;
                            Thread sender = new Thread(() -> {

                                if (!Objects.equals(finalLine, "")) {

                                    javafx.application.Platform.runLater(() -> {
                                        IdeGuiController.controller.getConsoleOutputTextArea().appendText(finalLine + System.getProperty("line.separator"));
                                        if (finalLine.contains("[Skript]")) {
                                            IdeGuiController.controller.getConsoleOutputTextArea().setStyleClass(IdeGuiController.controller.getConsoleOutputTextArea().getText().length() - finalLine.length(), IdeGuiController.controller.getConsoleOutputTextArea().getText().length(), "console");
                                        }
                                    });


                                } else {
                                    System.out.println("");
                                }


                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            });
                            sender.start();
                        }
                        System.out.println("Server stopped");
                        Main.runningServer = null;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                reader.setName("Reader Thread");
                writer.setName("Writer Thread");
                reader.start();
                writer.start();

            });
            t.setName("Server-thread");
            t.start();
        }
    }
    public void sendCommand(String message) {

        try {
            this.writer.write(message);
            this.writer.newLine();
            this.writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void createServer() {

        System.out.println("trying to create");
        File folder = this.folderFile;


        if(folder.mkdir()) {
           // this.configFile = new File(folder, "SkriptIDE-server.yaml");

            try {
                System.gc();
                System.out.println("inline");
                if(this.configFile.createNewFile()) {

                    System.out.println("lolc");
                    this.pluginsPath = this.folderPath + "/plugins";
                    File plugins = new File(this.pluginsPath);
                    if(plugins.mkdir()) {

                        this.config = new Config(this.configFile.getAbsolutePath());
                        this.config.set("folder-path", this.folderPath);
                        this.config.set("name", this.name);
                        this.config.set("sk.name", this.skript.getPath());
                        this.config.set("api.name", this.api.getPath());
                        this.config.set("args", this.startArgs);
                        this.config.save();

                        FileUtils.copyFile(this.api.getPath(), this.folderPath + "/Server.jar", StandardCopyOption.REPLACE_EXISTING);

                        FileUtils.copyFile(this.skript.getPath(), this.pluginsPath + "/Skript.jar", StandardCopyOption.REPLACE_EXISTING);


                        this.configured = true;
                    }

                } else {
                    System.out.println("etf");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void setStartArgs(String startArgs) {
        this.startArgs = startArgs;
    }

    public void setGeneratorSettings(String generatorSettings) {
        this.generatorSettings = generatorSettings;
    }

    public void setOpPermLevel(int opPermLevel) {
        this.opPermLevel = opPermLevel;
    }

    public void setMaxWorldSize(long maxWorldSize) {
        this.maxWorldSize = maxWorldSize;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public void setLevelSeed(String levelSeed) {
        this.levelSeed = levelSeed;
    }

    public void setMaxBuildHeight(long maxBuildHeight) {
        this.maxBuildHeight = maxBuildHeight;
    }

    public void setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
    }

    public void setModt(String modt) {
        this.modt = modt;
    }

    public void setLvlType(String lvlType) {
        this.lvlType = lvlType;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setDefaultGm(int defaultGm) {
        this.defaultGm = defaultGm;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public void setPackSHA1(String packSHA1) {
        this.packSHA1 = packSHA1;
    }

    public void setGenerateStructure(boolean generateStructure) {
        this.generateStructure = generateStructure;
    }

    public void setAllowNether(boolean allowNether) {
        this.allowNether = allowNether;
    }

    public void setQuery(boolean query) {
        this.query = query;
    }

    public void setAllowFlight(boolean allowFlight) {
        this.allowFlight = allowFlight;
    }

    public void setAnnounceAchievemnts(boolean announceAchievemnts) {
        this.announceAchievemnts = announceAchievemnts;
    }

    public void setRcon(boolean rcon) {
        this.rcon = rcon;
    }

    public void setForceGamemode(boolean forceGamemode) {
        this.forceGamemode = forceGamemode;
    }

    public void setWhitelist(boolean whitelist) {
        this.whitelist = whitelist;
    }

    public void setSpawnNPCS(boolean spawnNPCS) {
        this.spawnNPCS = spawnNPCS;
    }

    public void setSpawnAnimals(boolean spawnAnimals) {
        this.spawnAnimals = spawnAnimals;
    }

    public void setHardcore(boolean hardcore) {
        this.hardcore = hardcore;
    }

    public void setSnooper(boolean snooper) {
        this.snooper = snooper;
    }

    public void setPvp(boolean pvp) {
        this.pvp = pvp;
    }

    public void setAllowCMD(boolean allowCMD) {
        this.allowCMD = allowCMD;
    }

    public void setOnlineMode(boolean onlineMode) {
        this.onlineMode = onlineMode;
    }
    public String getName() {
        return name;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public String getSkriptsPath() {
        return skriptsPath;
    }

    public Skript getSkript() {
        return skript;
    }

    public Api getApi() {
        return api;
    }

    public int getPort() {
        return port;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public List<Addon> getServerAddons() {
        return serverAddons;
    }

    public String getStartArgs() {
        return startArgs;
    }

    public String getGeneratorSettings() {
        return generatorSettings;
    }

    public int getOpPermLevel() {
        return opPermLevel;
    }

    public long getMaxWorldSize() {
        return maxWorldSize;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getWorldName() {
        return worldName;
    }

    public String getLevelSeed() {
        return levelSeed;
    }

    public long getMaxBuildHeight() {
        return maxBuildHeight;
    }

    public int getViewDistance() {
        return viewDistance;
    }

    public String getModt() {
        return modt;
    }

    public String getLvlType() {
        return lvlType;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getDefaultGm() {
        return defaultGm;
    }

    public String getPack() {
        return pack;
    }

    public String getPackSHA1() {
        return packSHA1;
    }

    public boolean isGenerateStructure() {
        return generateStructure;
    }

    public boolean isAllowNether() {
        return allowNether;
    }

    public boolean isQuery() {
        return query;
    }

    public boolean isAllowFlight() {
        return allowFlight;
    }

    public boolean isAnnounceAchievemnts() {
        return announceAchievemnts;
    }

    public boolean isRcon() {
        return rcon;
    }

    public boolean isForceGamemode() {
        return forceGamemode;
    }

    public boolean isWhitelist() {
        return whitelist;
    }

    public boolean isSpawnNPCS() {
        return spawnNPCS;
    }

    public boolean isSpawnAnimals() {
        return spawnAnimals;
    }

    public boolean isHardcore() {
        return hardcore;
    }

    public boolean isSnooper() {
        return snooper;
    }

    public boolean isPvp() {
        return pvp;
    }

    public boolean isAllowCMD() {
        return allowCMD;
    }

    public boolean isOnlineMode() {
        return onlineMode;
    }

    public boolean isConfigured() {
        return configured;
    }

    public Config getConfig() {
        return config;
    }

    public File getConfigFile() {
        return configFile;
    }

    public String getPluginsPath() {
        return pluginsPath;
    }

    public boolean isSpawnMonsters() {
        return spawnMonsters;
    }

    public void setSpawnMonsters(boolean spawnMonsters) {
        this.spawnMonsters = spawnMonsters;
    }

    public void setServerAddons(List<Addon> serverAddons) {
        this.serverAddons = serverAddons;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
