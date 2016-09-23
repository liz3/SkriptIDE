package com.skriptide.util;

import com.skriptide.config.Config;
import com.skriptide.guis.SceneManager;
import com.skriptide.main.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Liz3ga on 28.07.2016.
 */
public class MCServer {


	private String name;
	private long port;
	private String path;

	private boolean spawnMonsters;

	private ServerVersion version;
	private Skript skript;
	private String notes;
	private SkriptAddon[] skriptAddons;

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
	private BufferedReader reader;
	private BufferedWriter writer;
	private String plFolderPath;


	public MCServer(String name, long port, String path, ServerVersion serverVersion,
					Skript scriptPluginVersion, String notes, String startArgs) {

		this.name = name;
		this.port = port;
		this.path = path;
		this.version = serverVersion;
		this.skript = scriptPluginVersion;
		this.plFolderPath = path + "/plugins";
		this.notes = notes;
		this.startArgs = startArgs;

		if (Main.debugMode) {
			System.out.println("Server values set");
		}
	}

	public MCServer(String name) {

		String current = null;
		try {
			current = new File(".").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File configFile = new File(current + "/Config.yaml");
		Config config = new Config(configFile.getAbsolutePath());
        System.out.println(name);
        System.out.println(config.getString("server." + name + ".info"));
        File info = new File(config.getString("server." + name + ".info"));
        System.out.println(info.getAbsolutePath());
        Config server = new Config(info.getAbsolutePath());

        this.name = server.getString("name");
        this.path = server.getString("path");
        this.version = new ServerVersion(server.getString("engine-path"));
        System.out.println("1. " + this.version.path);
        this.skript = new Skript(server.getString("skript"));
        System.out.println("2. " + this.skript.path);
        this.plFolderPath = server.getString("plugins-path");
        this.notes = server.getString("notes");
        this.startArgs = server.getString("start-args");


        List<String> adds = server.getAll("addon");
        int addonsSize = adds.size();
        skriptAddons = new SkriptAddon[addonsSize];
        int i = 0;
        for (String str : adds) {


            if (!str.equals(null) && !str.equals("")) {


                skriptAddons[i] = new SkriptAddon(server.getString("addon." + str + ".path").replace(".", "_"));
                i++;
            }
        }


        if (Main.debugMode) {

            System.out.println("Loaded Server from path" + path);
        }

        if (Main.debugMode) {
			System.out.println("Loaded settings from server");
		}

	}

	public static ObservableList<MCServer> getAllServers() {
		String current = null;

		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");


            Config config = new Config(configFile.getAbsolutePath());

			List<String> sec = config.getAll("server");
			ObservableList<MCServer> values = FXCollections.observableArrayList();
			for (String n : sec) {
                System.out.println(n);
                values.add(new MCServer(n));
			}
			if (Main.debugMode) {
				System.out.println("Returning all servers");
			}
			return values;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	private static void addServer(String name, String version, File path) {

		String current = null;


		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");

            Config config = new Config(configFile.getAbsolutePath());

            if(config.getString("server." + name) == null) {

                config.set("server." + name + ".info", path + "/SkriptIDE-Server.yaml");
                config.set("server." + name + ".name", name);

                config.save();
            }


		} catch (IOException e) {
			e.printStackTrace();
		}
		if (Main.debugMode) {
			System.out.println("Added server");
		}

	}

	private static void updateServer(String name, String version, File path) {


        String current = null;


        try {
            current = new File(".").getCanonicalPath();

            File configFile = new File(current + "/Config.yaml");

            Config config = new Config(configFile.getAbsolutePath());

            if(config.getString("server." + name) == null) {

                config.set("server." + name + ".info", path + "/SkriptIDE-Server.yaml");
                config.set("server." + name + ".version", version);
                config.set("server." + name + ".name", name);

                config.save();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


	}

	private static String getServer(String name) {


		String current = null;


		try {
            current = new File(".").getCanonicalPath();

            File configFile = new File(current + "/Config.yaml");

            Config config = new Config(configFile.getAbsolutePath());


			return config.getString("server." + name + ".info");


		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void loadServer() {
        File props = null;

         props = new File(path + "/server.properties");
// PROPERTIES

		try {
			Scanner sc = new Scanner(props);

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String value = "";
				if (line.contains("=")) {
					String[] split = line.split(Pattern.quote("="));
					String key = split[0];  //=
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
							this.port = Long.parseLong(value);
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

	public void updateServer() {

		File props = new File(path + "/server.properties");
		File infoFile = new File(path + "/SkriptIDE-Server.yaml");

		try {
			PrintWriter pw = new PrintWriter(new FileWriter(props));

			pw.println("generator-settings=" + this.getgeneratorSettings());
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

			Config config = new Config(infoFile.getAbsolutePath());


			config.set("name", name);
			config.set("engine-path", version.getPath());
			config.set("path", path);
			config.set("skript", skript.getPath());
			config.set("Plugin-folder", this.plFolderPath);

			config.remove("addon");

			if (this.skriptAddons.length != 0) {

				for (int i = 0; i != skriptAddons.length; i++) {

					if (skriptAddons[i] != null) {
						config.set("addon." + skriptAddons[i].getName() + ".name", skriptAddons[i].getName());
                        config.set("addon." + skriptAddons[i].getName() + ".path", skriptAddons[i].getPath());
                        config.set("addon." + skriptAddons[i].getName() + ".version", skriptAddons[i].getVersion());
					}
				}
			}

			config.save();

			updateServer(this.name, this.version.getVersion(), new File(this.path));
			SkriptAddon.compareAndSet(skriptAddons, new File(this.plFolderPath));

			if (Main.debugMode) {
				System.out.println("uptaded server" + this.getname());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public void startServer() {


		SceneManager.cleanUP();

		Thread t = new Thread(() -> {


			ProcessBuilder pb = new ProcessBuilder(System.getProperty("java.home") + "/bin/java", "-jar", this.path + "/spigot.jar");
			pb.directory(new File(path));

			Process p = null;
			try {
				p = pb.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (Main.debugMode) {
				System.out.println("Server Process started");
			}
			SceneManager.runningServer = MCServer.this;
			Process finalP = p;
			Thread writer = new Thread(() -> MCServer.this.writer = new BufferedWriter(new OutputStreamWriter(finalP.getOutputStream())));
			writer.start();


			this.reader =
					new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = null;


			try {
				while ((line = MCServer.this.reader.readLine()) != null) {
					String finalLine = line;

					Thread sender = new Thread(() -> {

						if (finalLine != "" && finalLine != null) {

							javafx.application.Platform.runLater(() -> {
								SceneManager.consoleOut.appendText(finalLine + System.getProperty("line.separator"));
								if (finalLine.contains("[Skript]")) {
									SceneManager.consoleOut.setStyleClass(SceneManager.consoleOut.getText().length() - finalLine.length(), SceneManager.consoleOut.getText().length(), "console");
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
				System.out.println("Server stopped?");
				SceneManager.runningServer = null;
				this.writer.close();
				this.reader.close();
				p.destroy();
				Thread.currentThread().interrupt();
			} catch (IOException e) {

				try {
					reader.reset();
				} catch (IOException e1) {
					System.out.println("fail1");
				}
				try {
					line = MCServer.this.reader.readLine();
				} catch (IOException e1) {
					System.out.println("fail2");
				}
			}

		});
		t.start();

	}

	public void createServer() {


		if (Main.debugMode) {
			System.out.println("Create Server called");
		}
		if (Main.debugMode) {

			System.out.println("Try to create Data-File for a minecraft-server");
		}


		File folder = new File(path);
		if (!folder.exists()) {
			Path pathToFile = Paths.get(path + "/data.yaml");
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

		} else {
			deleteFolder(folder);


		}
		if (Main.debugMode) {
			System.out.println("Data file for a minecraft server has been created");
		}
		if (Main.debugMode) {

			System.out.println("Trying to create a spigot.jar file for a Server");
		}


		try {
			FileInputStream jarInPut = new FileInputStream(new File(version.getPath()));
			File jarOutPut = new File(path + "/spigot.jar");
			Path path = jarOutPut.toPath();
			try {
				Files.copy(jarInPut, path, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (Main.debugMode) {
			System.out.println("Spigot.jar for a server has been created");
		}

		File infoFile = new File(path + "/SkriptIDE-Server.yaml");
		try {
			infoFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
        Config config = new Config(infoFile.getAbsolutePath());


        config.set("name", name);
        config.set("engine-path", version.getPath());
        config.set("path", path);
        config.set("skript", skript.getPath());
        config.set("plugins-path", this.plFolderPath);
        config.set("notes", this.notes);
        config.set("start-args", this.startArgs);
		config.set("Plugin-folder", this.plFolderPath);

        config.save();

        File plFolder = new File(path + "/plugins");
		plFolder.mkdir();
		try {
			FileInputStream jarInPut = new FileInputStream(new File(skript.getPath()));
			File jarOutPut = new File(path + "/plugins/" + skript.getName() + "_" + skript.getVersion() + ".jar");
			Path path = jarOutPut.toPath();
			try {
				Files.copy(jarInPut, path, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		File startSequenz = new File(path + "/run.bat");
		try {
			startSequenz.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(startSequenz, true), true);
			pw.println("@echo Off");
			pw.println("java -Xmx2G -jar spigot.jar");

			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File eula = new File(path + "/eula.txt");
		try {
			eula.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(eula, true), true);
			pw.println("#By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula).");
			pw.println("eula=true");

			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		startServer();
		addServer(this.name, this.version.getVersion(), new File(this.path));
		if (Main.debugMode) {
			System.out.println("Server created: " + this.getname());
		}
	}

	public void deleteServer() {

		ObservableList<Project> projects = Project.getProjects();
		int t = 0;
		for (Project pr : projects) {
			if (pr.getServer().getname().equalsIgnoreCase(this.getname())) {
				t++;
			}
		}
		if (t != 0) {
			boolean isToDelete = new SceneManager().infoCheck("Delete Server", "Delete Server: " + this.getname(), "This server is bounded to: " + t
					+ " Projects, you will have to change the Server manually to another?");

			if (isToDelete) {
				File folder = new File(this.path);
				System.out.println(path);

				deleteDirectory(folder);
				removeServer(this.path);
				if (Main.debugMode) {
					System.out.println("Deleted server!");
				}
			} else {

			}
		} else {
			File folder = new File(this.path);
			System.out.println(path);

			deleteDirectory(folder);
			removeServer(this.name);
			if (Main.debugMode) {
				System.out.println("Deleted server!");
			}
		}

	}

	private boolean deleteDirectory(File directory) {
		if (directory.exists()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}

		return (directory.delete());
	}

	public SkriptAddon[] getSkriptAddons() {
		if (this.skriptAddons != null) {
			return this.skriptAddons;
		}
		return null;
	}

	public String getPlFolderPath() {
		return this.plFolderPath;
	}

	public void setPlFolderPath(String value) {
		this.plFolderPath = value;
	}

	public BufferedWriter getWriter() {
		return this.writer;
	}

	public BufferedReader getReader() {
		return this.reader;
	}

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	public String getname() {
		return this.name;
	}

	public long getport() {
		return this.port;
	}

	public String getpath() {
		return this.path;
	}

	public ServerVersion getversion() {
		return this.version;
	}

	public Skript getskript() {
		return this.skript;
	}

	public String getnotes() {
		return this.notes;
	}

	public String getstartArgs() {
		return this.startArgs;
	}

	public String getgeneratorSettings() {
		return this.generatorSettings;
	}

	public int getopPermLevel() {
		return this.opPermLevel;
	}

	public long getmaxWorldSize() {
		return this.maxWorldSize;
	}

	public int getmaxPlayers() {
		return this.maxPlayers;
	}

	public String getworldName() {
		return this.worldName;
	}

	public String getlevelSeed() {
		return this.levelSeed;
	}

	public long getmaxBuildHeight() {
		return this.maxBuildHeight;
	}

	public int getviewDistance() {
		return this.viewDistance;
	}

	public String getmodt() {
		return this.modt;
	}

	public String getlvlType() {
		return this.lvlType;
	}

	public int getdifficulty() {
		return this.difficulty;
	}

	public int getdefaultGm() {
		return this.defaultGm;
	}

	public String getpack() {
		return this.pack;
	}

	public String getpackSHA1() {
		return this.packSHA1;
	}

	public boolean getgenerateStructure() {
		return this.generateStructure;
	}

	public boolean getallowNether() {
		return this.allowNether;
	}

	public boolean getquery() {
		return this.query;
	}

	public boolean getallowFlight() {
		return this.allowFlight;
	}

	public boolean getannounceAchievemnts() {
		return this.announceAchievemnts;
	}

	public boolean getrcon() {
		return this.rcon;
	}

	public boolean getforceGamemode() {
		return this.forceGamemode;
	}

	public boolean getwhitelist() {
		return this.whitelist;
	}

	public boolean getspawnNPCS() {
		return this.spawnNPCS;
	}

	public boolean getspawnAnimals() {
		return this.spawnAnimals;
	}

	public boolean gethardcore() {
		return this.hardcore;
	}

	public boolean getsnooper() {
		return this.snooper;
	}

	public boolean getpvp() {
		return this.pvp;
	}

	public boolean getallowCMD() {
		return this.allowCMD;
	}

	public boolean getonlineMode() {
		return this.onlineMode;
	}

	public boolean getspawnMonsters() {
		return this.spawnMonsters;
	}

	public void setname(String value) {
		this.name = value;
	}

	public void setport(long value) {
		this.port = value;
	}

	public void setpath(String value) {
		this.path = value;
	}

	public void setversion(ServerVersion value) {
		this.version = value;
	}

	public void setskript(Skript value) {
		this.skript = value;
	}

	public void setnotes(String value) {
		this.notes = value;
	}

	public void setstartArgs(String value) {
		this.startArgs = value;
	}

	public void setgeneratorSettings(String value) {
		this.generatorSettings = value;
	}

	public void setopPermLevel(int value) {
		this.opPermLevel = value;
	}

	public void setmaxWorldSize(long value) {
		this.maxWorldSize = value;
	}

	public void setmaxPlayers(int value) {
		this.maxPlayers = value;
	}

	public void setworldName(String value) {
		this.worldName = value;
	}

	public void setlevelSeed(String value) {
		this.levelSeed = value;
	}

	public void setmaxBuildHeight(long value) {
		this.maxBuildHeight = value;
	}

	public void setviewDistance(int value) {
		this.viewDistance = value;
	}

	public void setmodt(String value) {
		this.modt = value;
	}

	public void setlvlType(String value) {
		this.lvlType = value;
	}

	public void setdifficulty(int value) {
		this.difficulty = value;
	}

	public void setdefaultGm(int value) {
		this.defaultGm = value;
	}

	public void setpack(String value) {
		this.pack = value;
	}

	public void setpackSHA1(String value) {
		this.packSHA1 = value;
	}

	public void setgenerateStructure(boolean value) {
		this.generateStructure = value;
	}

	public void setallowNether(boolean value) {
		this.allowNether = value;
	}

	public void setquery(boolean value) {
		this.query = value;
	}

	public void setallowFlight(boolean value) {
		this.allowFlight = value;
	}

	public void setannounceAchievemnts(boolean value) {
		this.announceAchievemnts = value;
	}

	public void setrcon(boolean value) {
		this.rcon = value;
	}

	public void setforceGamemode(boolean value) {
		this.forceGamemode = value;
	}

	public void setwhitelist(boolean value) {
		this.whitelist = value;
	}

	public void setspawnNPCS(boolean value) {
		this.spawnNPCS = value;
	}

	public void setspawnAnimals(boolean value) {
		this.spawnAnimals = value;
	}

	public void sethardcore(boolean value) {
		this.hardcore = value;
	}

	public void setsnooper(boolean value) {
		this.snooper = value;
	}

	public void setpvp(boolean value) {
		this.pvp = value;
	}

	public void setallowCMD(boolean value) {
		this.allowCMD = value;
	}

	public void setonlineMode(boolean value) {
		this.onlineMode = value;
	}

	public void setspawnMonsters(boolean value) {
		this.spawnMonsters = value;
	}

	public void setAddons(SkriptAddon[] addons) {
		this.skriptAddons = addons;
	}

	private void removeServer(String name) {

		String current = null;


		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");

            Config con = new Config(configFile.getAbsolutePath());

            System.out.println(name);
            if(con.contains("server." + name)) {
                con.remove("server." + name);
				con.save();

			}



		} catch (IOException e) {
			e.printStackTrace();
		}
		if (Main.debugMode) {
			System.out.println("Removed server from config");
		}
	}

	private void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) { //some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}

	}

}
