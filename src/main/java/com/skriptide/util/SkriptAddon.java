package com.skriptide.util;

import com.skriptide.config.Config;
import com.skriptide.main.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Created by Liz3ga on 23.08.2016.
 */
public class SkriptAddon {


	private String name;
	private String version;
	private String path;

	public SkriptAddon(String childName) {

		String current = null;


		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");


			Config config = new Config(configFile.getAbsolutePath());

			this.name = config.getString("addon." + childName + ".name");
            this.version = config.getString("addon." + childName + ".version");
            this.path = config.getString("addon." + childName + ".path");


			if (Main.debugMode) {
				System.out.println("loaded skript addon");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public static void compareAndSet(SkriptAddon[] adds, File plFolder) {


		for (File f : plFolder.listFiles()) {

			if (f.getAbsolutePath().endsWith(".jar") && !f.getName().contains("Skript.jar")) {

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

			File configFile = new File(current + "/Config.yaml");

            Config config = new Config(configFile.getAbsolutePath());
            List<String> sec = config.getAll("addon");
			ObservableList<SkriptAddon> values = FXCollections.observableArrayList();
			for (String n : sec) {
                System.out.println(n);
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        File configFile = new File(current + "/Config.yaml");

        Config config = new Config(configFile.getAbsolutePath());

        config.set("addon." + path.getAbsolutePath().replace(".","_") + ".name", name);
        config.set("addon." + path.getAbsolutePath().replace(".","_") + ".version", version);
        config.set("addon." + path.getAbsolutePath().replace(".","_") + ".path", path.getAbsolutePath());

		config.save();

        if(Main.debugMode) {
            System.out.println("added Addon");
        }



    }

	public static void removeAddon(String path) {

		String current = null;


		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");


			Config config = new Config(configFile.getAbsolutePath());

            if(config.contains("addon." + path.replace(".", "_"))) {
                config.remove("addon." + path.replace(".", "_"));
            }
			if(Main.debugMode) {
				System.out.println("Removed addon");
			}

			config.save();
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
