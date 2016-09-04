package com.skriptide.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.skriptide.main.Main;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

/**
 * Created by Liz3ga on 23.08.2016.
 */
public class Project {

	private String name;
	private String skriptPath;
	private String infoPath;
	private Skript sk;
	private String outPath;
	private MCServer server;

	public Project(String name) {

		String current = null;


		try {
			current = new File(".").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File configFile = new File(current + "/Config.ini");


		Ini cfg = null;

		try {
			cfg = new Ini(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Profile.Section sec = cfg.get("Projects");

		try {
			Ini proj = new Ini(new File(sec.get(name)));

			Profile.Section info = proj.get("Information");
			this.name = info.get("Name");
			this.skriptPath = info.get("Path");
			this.sk = new Skript(info.get("Skript-Path").toLowerCase());
			this.outPath = info.get("Output-Path");
			this.server = new MCServer(info.get("Server-Path"));
			if (Main.debugMode) {
				System.out.println("Project called");
			}


		} catch (IOException e) {
			System.out.println("no project found");
		}


	}

	public MCServer getServer() {
		return this.server;
	}

	public String getName() {
		return this.name;
	}

	public String getSkriptPath() {
		return this.skriptPath;
	}

	public Skript getSk() {
		return this.sk;
	}

	public String getOutPath() {
		return this.outPath;
	}

	public static ObservableList<Project> getProjects() {
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


			Profile.Section sec = cfg.get("Projects");
			ObservableList<Project> values = FXCollections.observableArrayList();


			values.addAll(sec.keySet().stream().map(Project::new).collect(Collectors.toList()));
			if (Main.debugMode) {
				System.out.println("project list called");
			}

			return values;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public void runProject() {


		FileInputStream jarInPut = null;
		try {
			jarInPut = new FileInputStream(new File(skriptPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		File jarOutPut = new File(outPath + "/" + name + ".sk");
		Path path = jarOutPut.toPath();
		try {
			Files.copy(jarInPut, path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.server.startServer();

	}


	public static void addProject(String name, File path) {

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
			Profile.Section sec = cfg.get("Projects");

			if (sec.containsKey("Placeholder")) {
				sec.remove("Placeholder");
			}
			sec.put(name, path.getAbsolutePath());

			cfg.store();
			if (Main.debugMode) {

				System.out.println("Project added to Config");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void removeProject(String name) {

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
			Profile.Section sec = cfg.get("Projects");


			if (sec.containsKey(name)) {

				sec.remove(name);
				if (sec.size() == 0) {
					sec.put("Placeholder", "");
				}
				cfg.store();
			}


		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void createProject(String name, Skript skript, MCServer server, String path, String notes) {

		String outPath = "";
		File dir = null;
		File script = null;
		File info = null;

		outPath = path;
		dir = new File(outPath);
		dir.mkdirs();


		script = new File(outPath + "/" + name + ".sk");
		info = new File(outPath + "/sriptIDE-Project.ini");

		try {
			script.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			info.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			Ini inf = new Ini(info);

			Profile.Section infoSec = inf.add("Information");

			infoSec.put("Name", name);
			infoSec.put("Path", script.getAbsolutePath());
			infoSec.put("Skript-Version", skript.getVersion());
			infoSec.put("Skript-Path", skript.getPath());
			infoSec.put("Server-Path", server.getpath());
			infoSec.put("Output-Path", server.getPlFolderPath() + "/Skript/scripts");

			infoSec.put("Notes", notes);

			inf.store();

			if (Main.debugMode) {

				System.out.println("A Project file has been created");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		addProject(name, info);


	}

}
