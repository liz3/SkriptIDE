package com.skriptide.util;

import com.skriptide.config.Config;
import com.skriptide.main.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Liz3ga on 23.08.2016.
 */
public class ServerVersion {

	public String name;
	public String version;
	public String path;

	public ServerVersion(String childName) {

		String current = null;


		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");

			Config config = new Config(configFile.getAbsolutePath());

			this.name = config.getString("engine-version." + childName + ".name");
			this.version = config.getString("engine-version." + childName + ".version");
			this.path = config.getString("engine-version." + childName + ".path");

			if(Main.debugMode) {
				System.out.println("Server version test");
			}



		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public static ObservableList<ServerVersion> getServerVersions() {
		String current = null;


		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");

			Config con = new Config(configFile.getAbsolutePath());

			List<String> sec = con.getAll("engine-version");
			ObservableList<ServerVersion> values = FXCollections.observableArrayList();
			for (String n : sec) {

				values.add(new ServerVersion(n));
			}
			if(Main.debugMode) {
				System.out.println("returing server versions");
			}
			return values;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static void addServerVersion(String name, String version, File path) {

		String current = null;


		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");

			Config config = new Config(configFile.getAbsolutePath());

			config.set("engine-version." + path.getAbsolutePath() + ".name", name);
			config.set("engine-version." + path.getAbsolutePath() + ".version", version);
			config.set("engine-version." + path.getAbsolutePath() + ".path", path.getAbsolutePath());

			config.save();
			if(Main.debugMode) {
				System.out.println("Added versions");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void removeServerVersion(String path) {

		String current = null;


		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");

            Config config = new Config(configFile.getAbsolutePath());

            if(config.getString("engine-version." + path) != null) {
                config.remove("engine-version." + path);
            }


            config.save();
			if(Main.debugMode) {
				System.out.println("removed server version");
			}


		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getName() {
		return this.name;
	}

	public String getVersion() {
		return this.version;
	}

	public String getPath() {
		return this.path;
	}
}
