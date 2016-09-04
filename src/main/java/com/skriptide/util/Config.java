package com.skriptide.util;

import com.skriptide.main.Main;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Liz3ga on 27.07.2016.
 */
public class Config {

	public static int checkConfig() {


		try {
			String current = new File(".").getCanonicalPath();


			File configFile = new File(current + "/Config.ini");

			if (configFile.exists()) {

				return 0;
			} else {
				return 1;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return 0;

	}

	public static boolean createConfig(String pPath, String sPath, String lang) {

		String current = null;
		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.ini");

			configFile.createNewFile();

			File projPath = new File(pPath);
			File servPath = new File(sPath);


			if (!projPath.exists()) {
				Path pathToFile = Paths.get(pPath + "\\data.ini");
				Files.createDirectories(pathToFile.getParent());
				Files.createFile(pathToFile);
				File f = new File(pathToFile.toUri());
				f.delete();
			}
			if (!servPath.exists()) {
				Path pathToFile = Paths.get(sPath + "\\data.ini");
				Files.createDirectories(pathToFile.getParent());
				Files.createFile(pathToFile);
				File f = new File(pathToFile.toUri());
				f.delete();
			}
			Ini cfg = new Ini(configFile);

			Profile.Section generel = cfg.add("Info");
			Profile.Section srv = cfg.add("Servers");
			Profile.Section proj = cfg.add("Projects");
			Profile.Section scripts = cfg.add("Scripts");
			Profile.Section addons = cfg.add("Addons");
			Profile.Section serverVersion = cfg.add("Server-versions");
			Profile.Section settings = cfg.add("Settings");

			generel.add("Projects-Path", pPath);
			generel.add("Servers-Path", sPath);
			generel.add("Lang", lang);
			generel.add("DebugMode", "false");

			srv.add("Placeholder", "");
			proj.add("Placeholder", "");
			scripts.add("Placeholder", "");
			addons.add("Placeholder", "");
			serverVersion.add("Placeholder", "");


			cfg.store();

			if (Main.debugMode) {

				System.out.println("Config created without errors");
			}

			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}


	}

	public static String getProjectsPath() {

		String current = null;

		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.ini");


			Ini cfg = new Ini(configFile);
			Profile.Section sec = cfg.get("Info");

			String path = sec.get("Projects-Path");


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

		String current = null;

		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.ini");


			Ini cfg = new Ini(configFile);
			Profile.Section sec = cfg.get("Info");

			String path = sec.get("Servers-Path");
			if (Main.debugMode) {
				System.out.println("Servers path called");
			}


			return path;
		} catch (IOException e) {
			e.printStackTrace();
		}


		return null;
	}

	public static boolean isDebug() {
		String current = null;

		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.ini");


			Ini cfg = new Ini(configFile);
			Profile.Section sec = cfg.get("Info");

			String path = sec.get("DebugMode");

			if (path.equals("true")) {
				return true;
			} else {
				return false;
			}


		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

}
