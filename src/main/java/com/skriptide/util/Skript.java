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
public class Skript {


	public String name;
	public String version;
	public String path;

	public Skript(String path) {

		String current = null;


		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");

			Config config = new Config(configFile.getAbsolutePath());


			this.name = config.getString("skripts." + path + ".name");
			this.version = config.getString("skripts." + path + ".version");
			this.path = config.getString("skripts." + path + ".path");

			if(Main.debugMode) {
				System.out.println("loaded Skript version");
			}


		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public static ObservableList<Skript> getSkriptVersions() {
		String current = null;


		try {
            current = new File(".").getCanonicalPath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

		File configFile = new File(current + "/Config.yaml");

		Config config = new Config(configFile.getAbsolutePath());

		ObservableList<Skript> values = FXCollections.observableArrayList();

		for(String str : config.getAll("skripts")) {
            values.add(new Skript(str));
        }

		if(Main.debugMode) {
            System.out.println("returning skript versions");
        }
		return values;


	}

	public static void addScript(String name, String version, File path) {

		String current = null;


		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");

            Config config = new Config(configFile.getAbsolutePath());

            if(config.getString("skripts." + path.getAbsolutePath()) == null ) {


                config.set("skripts." + path.getAbsolutePath() + ".name", name);
                config.set("skripts." + path.getAbsolutePath() + ".version", version);
                config.set("skripts." + path.getAbsolutePath() + ".path", path.getAbsoluteFile());

                config.save();
            } else {


            }



			if(Main.debugMode) {
				System.out.println("added skript version");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void removeScript(String path) {

		String current = null;


		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");

            Config config = new Config(configFile.getAbsolutePath());

            if(config.getString("skript." + path) != null) {

                config.remove("skript." + path);

                config.save();

            }else {

            }

			if(Main.debugMode) {
				System.out.println("removed skript version");
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
