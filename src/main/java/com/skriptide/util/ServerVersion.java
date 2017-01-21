package com.skriptide.util;

import com.skriptide.config.Config;
import com.skriptide.guis.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;

/**
 * Created by Liz3ga on 23.08.2016.
 */
public class ServerVersion {

	private String name;
	private String version;
	public String path;

	public ServerVersion(String childName) {

		String current;
        System.out.printf("nae: " + childName);


		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");

			Config config = new Config(configFile.getAbsolutePath());

			this.name = config.getString("engine-version." + childName.replace(".", "_") + ".name");
			this.version = config.getString("engine-version." + childName.replace(".", "_") + ".version");
			this.path = config.getString("engine-version." + childName.replace(".", "_") + ".path");
            System.out.printf(config.getString("engine-version." + childName.replace(".", "_") + ".name"));
            if(SceneManager.debugMode) {
				System.out.println("Server version test");
			}



		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public static ObservableList<ServerVersion> getServerVersions() {
		String current;


		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");

			Config con = new Config(configFile.getAbsolutePath());

			ObservableList<ServerVersion> values = FXCollections.observableArrayList();
			for (String n : con.getAll("engine-version")) {
                System.out.println("Server version: " + n);
				values.add(new ServerVersion(n));
			}

            if(SceneManager.debugMode) {
				System.out.println("returing server versions");
			}
			return values;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static void addServerVersion(String name, String version, File path) {

		String current;


		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");

			Config config = new Config(configFile.getAbsolutePath());

			config.set("engine-version." + path.getAbsolutePath().replace(".","_") + ".name", name);
			config.set("engine-version." + path.getAbsolutePath().replace(".","_") + ".version", version);
			config.set("engine-version." + path.getAbsolutePath().replace(".","_") + ".path", path.getAbsolutePath());

			config.save();
			if(SceneManager.debugMode) {
				System.out.println("Added versions");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void removeServerVersion(String path) {

		String current;



		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");

            Config config = new Config(configFile.getAbsolutePath());
            System.out.println("path" + path);
            System.out.println();
            if(config.contains(path.replace(".", "_"))) {
                System.out.println("not null");
                config.remove("engine-version." + path.replace(".", "_"));
                config.save();
            }


            config.save();
			if(SceneManager.debugMode) {
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
