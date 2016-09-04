package com.skriptide.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.IOException;

/**
 * Created by Liz3ga on 23.08.2016.
 */
public class Skript {


	public String name;
	public String version;
	public String path;

	public Skript(String childName) {

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
			Profile.Section sec = cfg.get("Scripts");

			Profile.Section child = sec.getChild(childName);
			this.name = child.get("Name");
			this.version = child.get("Version");
			this.path = child.get("Path");

			System.out.println(this.name);
			System.out.println(this.version);


		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public static ObservableList<Skript> getSkriptVersions() {
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
			Profile.Section sec = cfg.get("Scripts");
			ObservableList<Skript> values = FXCollections.observableArrayList();
			for (String n : sec.childrenNames()) {

				values.add(new Skript(n));
			}

			return values;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static void addScript(String name, String version, File path) {

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
			Profile.Section sec = cfg.get("Scripts");
			if (sec.containsKey("Placeholder")) {
				sec.remove("Placeholder");
			}
			if (sec.getChild(path.getAbsolutePath().toLowerCase()) != null) {


			} else {
				Profile.Section child = sec.addChild(path.getAbsolutePath().toLowerCase());

				child.put("Name", name);
				child.put("Version", version);
				child.put("Path", path);

			}

			cfg.store();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void removeScript(String path) {

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
			Profile.Section sec = cfg.get("Scripts");

			if (sec.getChild(path.toLowerCase()) != null) {


				sec.removeChild(path.toLowerCase());

				if (sec.size() == 0) {
					sec.put("Placeholder", "");
				}
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
