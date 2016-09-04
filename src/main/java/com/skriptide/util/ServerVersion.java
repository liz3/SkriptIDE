package com.skriptide.util;

import com.skriptide.main.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.IOException;

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

			File configFile = new File(current + "/Config.ini");


			Ini cfg = null;
			try {
				cfg = new Ini(configFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Profile.Section sec = cfg.get("Server-versions");

			Profile.Section child = sec.getChild(childName);
			this.name = child.get("Name");
			this.version = child.get("Version");
			this.path = child.get("Path");

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

			File configFile = new File(current + "/Config.ini");


			Ini cfg = null;
			try {
				cfg = new Ini(configFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Profile.Section sec = cfg.get("Server-versions");
			ObservableList<ServerVersion> values = FXCollections.observableArrayList();
			for (String n : sec.childrenNames()) {

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

			File configFile = new File(current + "/Config.ini");


			Ini cfg = null;
			try {
				cfg = new Ini(configFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Profile.Section sec = cfg.get("Server-versions");

			if (sec.containsKey("Placeholder")) {
				sec.remove("Placeholder");
			}
			if (sec.getChild(path.getAbsolutePath().toLowerCase()) != null) {

			} else {
				Profile.Section child = sec.addChild(path.getAbsolutePath().toLowerCase());

				child.put("Name", name);
				child.put("Version", version);
				child.put("Path", path.getAbsolutePath());

			}


			cfg.store();
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

			File configFile = new File(current + "/Config.ini");


			Ini cfg = null;
			try {
				cfg = new Ini(configFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Profile.Section sec = cfg.get("Server-versions");


			if (sec.getChild(path.toLowerCase()) != null) {
				Profile.Section child = sec.getChild(path.toLowerCase());

				sec.removeChild(path.toLowerCase());

				if (sec.size() == 0) {
					sec.put("Placeholder", "");
				}
			}
			if(Main.debugMode) {
				System.out.println("removed server version");
			}
			cfg.store();

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
