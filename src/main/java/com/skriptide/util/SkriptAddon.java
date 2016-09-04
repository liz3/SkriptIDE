package com.skriptide.util;

import com.skriptide.main.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by Liz3ga on 23.08.2016.
 */
public class SkriptAddon {


	public String name;
	public String version;
	public String path;

	public SkriptAddon(String childName) {

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
			Profile.Section sec = cfg != null ? cfg.get("Addons") : null;

			Profile.Section child = sec != null ? sec.getChild(childName) : null;
			this.name = child != null ? child.get("Name") : null;
			this.version = child != null ? child.get("Version") : null;
			this.path = child != null ? child.get("Path") : null;


			if (Main.debugMode) {
				System.out.println("loaded skript addon");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public static void compareAndSet(SkriptAddon[] adds, File plFolder) {


		for (File f : plFolder.listFiles()) {

			if (f.getAbsolutePath().endsWith(".jar") && !f.getName().contains("Skript")) {

				f.delete();


			}


		}
		for (SkriptAddon addon : adds) {

			File from = new File(addon.getPath());
			try {
				Files.copy(from.toPath(), new File(plFolder, addon.getName() + "-" + addon.getVersion() + ".jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (Main.debugMode) {
			System.out.println("compared and loaded addons");
		}
	}

	public static ObservableList<SkriptAddon> getScriptAddons() {
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
			Profile.Section sec = cfg.get("Addons");
			ObservableList<SkriptAddon> values = FXCollections.observableArrayList();
			for (String n : sec.childrenNames()) {

				values.add(new SkriptAddon(n));
			}

			if (Main.debugMode) {
				System.out.println("compared and loaded addons");
			}
			return values;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static void addAddon(String name, String version, File path) {

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
			Profile.Section sec = null;
			if (cfg != null) {
				sec = cfg.get("Addons");
			}
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
			if(Main.debugMode) {
				System.out.println("added Addon");
			}

			cfg.store();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void removeAddon(String path) {

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
			Profile.Section sec = cfg.get("Addons");


			if (sec.getChild(path.toLowerCase()) != null) {


				sec.removeChild(path.toLowerCase());
				if (sec.size() == 0) {
					sec.put("Placeholder", "");
				}
			}
			cfg.store();
			if(Main.debugMode) {
				System.out.println("Removed addon");
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
