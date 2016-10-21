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

			if (con.contains("settings.main.lang")) {
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
			config.set("settings.main.lang", lang);
            config.set("settings.main.debugMode", "false");
			config.set("settings.main.style","Standard");
			config.set("settings.main.auto-save-interval", 5);
			config.set("settings.main.auto-save-interval", "Second");
			config.set("settings.main.update-check", true);
			config.set("settings.server-settings.mark-skript-msg", true);
			config.set("settings.server-settings.start-after-create", true);
			config.set("settings.server-settings.clear-projects-after-stop", false);
			config.set("settings.server-settings.save-path", sPath);
			config.set("settings.server-settings.save-output", true);
			config.set("settings.code.use-code-management", true);
			config.set("settings.code.use-highlight", true);
			config.set("settings.code.use-auto-complete", true);
			config.set("settings.export.method", "SFTP");
			config.set("settings.export.host", "");
			config.set("settings.export.username", "");
			config.set("settings.export.password", "");
			
			


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
