package com.skriptide.util;

import com.skriptide.guis.SceneManager;
import com.skriptide.main.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextInputDialog;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
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
	private String folder;
	private String notes;


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
			this.folder = info.get("Folder-Path");
			this.notes = info.get("Notes");
			if (Main.debugMode) {
				System.out.println("Project called");
			}


		} catch (IOException e) {
			System.out.println("no project found");
		}

		if (Main.debugMode) {
			System.out.println("Loaded project");
		}
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
			System.out.println(sec.size());

			for (String n : sec.keySet()) {

				values.add(new Project(n));
			}
			if (Main.debugMode) {
				System.out.println("project list called");
			}

			if (Main.debugMode) {
				System.out.println("returning projects");
			}
			return values;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

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

	public static void updateProjectEntry(String name, File path) {

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
			if (Main.debugMode) {
				System.out.println("Removed project");
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
		info = new File(outPath + "/SkriptIDE-Project.ini");

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
			infoSec.put("Folder-Path", path);
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

		if (Main.debugMode) {
			System.out.println("crated project");
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

	public String folderPath() {
		return this.folder;
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
		if (Main.debugMode) {
			System.out.println("starting");
		}

		this.server.startServer();

	}

	private void updateProject(String newName) {

		File oldfolder = new File(this.folderPath());
		String newPath = oldfolder.getAbsolutePath().substring(0, oldfolder.getAbsolutePath().length() - this.name.length()) + newName + "\\";
		oldfolder.renameTo(new File(newPath));
		File sk = new File(newPath, this.name + ".sk");
		sk.renameTo(new File(newPath, newName + ".sk"));

		String outPath = "";
		File dir = null;
		File script = null;
		File info = null;


		info = new File(newPath + "/SkriptIDE-Project.ini");

		Ini inf = null;

		try {
			inf = new Ini(info);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Profile.Section infoSec = inf.get("Information");

		infoSec.put("Name", newName);
		infoSec.put("Path", newPath + newName + ".sk");
		infoSec.put("Folder-Path", newPath);
		infoSec.put("Skript-Version", this.sk.getVersion());
		infoSec.put("Skript-Path", this.sk.getPath());
		infoSec.put("Server-Path", this.server.getpath());
		infoSec.put("Output-Path", this.server.getPlFolderPath() + "/Skript/scripts");

		try {
			inf.store();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			sec.remove(this.name);
			sec.put(newName, newPath + "\\SkriptIDE-Project.ini");

			cfg.store();
			if (Main.debugMode) {

				System.out.println("Project added to Config");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public void moveProject() {


	}

	public void deleteProject() {


		boolean toDelete = new SceneManager().infoCheck("Delete Project", "Delete Project: " + this.name, "Do you really want to delete this project?");
		if (toDelete) {
			File folder = new File(this.folder);


			deleteDirectory(folder);
			removeProject(this.name);
			updateProjects();

		}

	}

	public static void updateProjects() {

		ObservableList<Project> pr = getProjects();
		SceneManager.projectsList.getItems().clear();
		for (Project prs : pr) {
			if (prs.getName() != null) {
				SceneManager.projectsList.getItems().add(prs.getName());
			}
		}
	}

	public void reNameProject() {

		TextInputDialog input = new TextInputDialog();
		input.setGraphic(null);
		input.setTitle("Rename project");
		input.setHeaderText("Rename Project: " + this.name);
		Optional<String> out = input.showAndWait();
		if (out != null) {
			String trueStr = out.get();


			updateProject(trueStr);
			updateProjects();

		}

	}

	public void changeServer(String servername) {

	}

	private boolean deleteDirectory(File directory) {
		if (directory.exists()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}

		return (directory.delete());
	}

}
