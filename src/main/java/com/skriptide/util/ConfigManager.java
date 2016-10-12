package com.skriptide.util;

import com.skriptide.config.Config;
import com.skriptide.main.Main;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Liz3ga on 27.07.2016.
 */
public class ConfigManager {

	public static int checkConfig() {


		try {
			String current = new File(".").getCanonicalPath();


			File configFile = new File(current + "/Config.yaml");

			Config con = new Config(configFile.getAbsolutePath());

			if (con.contains("main.settings.lang")) {
				if(Main.debugMode) {
					System.out.println("ConfigManager exists");
				}
				return 0;
			} else {
				if(Main.debugMode) {
					System.out.println("ConfigManager does not exists");
				}
				return 1;

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return 2;

	}

	public static boolean createConfig(String pPath, String sPath, String lang) {

		String current;
		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");



			File projPath = new File(pPath);
			File servPath = new File(sPath);


			if (!projPath.exists()) {
				Path pathToFile = Paths.get(pPath + "\\data.yaml");
				Files.createDirectories(pathToFile.getParent());
				Files.createFile(pathToFile);
				File f = new File(pathToFile.toUri());
				f.delete();
			}
			if (!servPath.exists()) {
				Path pathToFile = Paths.get(sPath + "\\data.yaml");
				Files.createDirectories(pathToFile.getParent());
				Files.createFile(pathToFile);
				File f = new File(pathToFile.toUri());
				f.delete();
			}
            Config config = new Config(configFile.getAbsolutePath());

			config.set("main.paths.projects", pPath);
			config.set("main.paths.servers", sPath);
			config.set("main.settings.lang", lang);
            config.set("main.settings.debugMode", "false");


			config.save();

			if (Main.debugMode) {

				System.out.println("ConfigManager created without errors");
			}

			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}


	}

	public static String getProjectsPath() {

		String current;

		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");


			String path = new Config(configFile.getAbsolutePath()).getString("main.paths.projects");


			if (Main.debugMode) {

				System.out.println("Projects-Path called");
			}

			return path;
		} catch (IOException e) {
			e.printStackTrace();
		}


		return null;
	}

	public static String getServersPath() {

		String current;

		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");


			return new Config(configFile.getAbsolutePath()).getString("main.paths.servers");
		} catch (IOException e) {
			e.printStackTrace();
		}


		return null;
	}

	public static boolean isDebug() {
		String current;

		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");


			return Boolean.valueOf(new Config(configFile.getAbsolutePath()).getString("main.settings.debugMode"));


		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

}
