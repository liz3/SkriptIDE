package com.skriptide.include;

import com.skriptide.Main;
import com.skriptide.config.Config;
import com.skriptide.gui.controller.IdeGuiController;
import com.skriptide.util.FileUtils;
import java.io.*;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

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



    }

    public Server(Config config, File folder) {


        this.config = config;
        this.folderPath = folder.getAbsolutePath();
        this.name = config.getString("name");
        this.startArgs = config.getString("args");
        this.folderFile = new File(config.getString("folder-path"));
        this.skript = Main.manager.getSkripts().get(config.getString("sk.name"));
        this.api = Main.manager.getApis().get(config.getString("api.name"));

    }


    public void startServer() {

        if(Main.runningServer == null) {

            Main.runningServer = this;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

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
                    Thread writer = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Server.this.writer = new BufferedWriter(new OutputStreamWriter(finalP.getOutputStream()));
                        }
                    });


                    Thread reader = new Thread(new Runnable() {
                        @Override
                        public void run() {

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
                        }
                    });

                    reader.setName("Reader Thread");
                    writer.setName("Writer Thread");
                    reader.start();
                    writer.start();

                }
            });
            t.setName("Server-thread");
            t.start();
        }
    }
    public void sendCommamd(String message) {

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

}
