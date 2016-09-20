package com.skriptide.util;

import com.skriptide.config.Config;
import com.skriptide.guis.SceneManager;
import com.skriptide.main.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextInputDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
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

		File configFile = new File(current + "/Config.yaml");


		Config config = new Config(configFile.getAbsolutePath());


		Config project = new Config(new File(config.getString("project." + name)).getAbsolutePath());


		this.name = project.getString("name");
		this.skriptPath = project.getString("path");
		this.sk = new Skript(project.getString("skript-path"));
		this.outPath = project.getString("output-path");
		this.server = new MCServer(project.getString("server-name"));
		this.folder = project.getString("folder-path");
		this.notes = project.getString("notes");
		if (Main.debugMode) {
            System.out.println("Project called");
        }


		if (Main.debugMode) {
			System.out.println("Loaded project");
		}
	}

	public static ObservableList<Project> getProjects() {
		String current = null;


		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");


			Config config = new Config(configFile.getAbsolutePath());


			List<String> sec = config.getAll("project");
			ObservableList<Project> values = FXCollections.observableArrayList();
			System.out.println(sec.size());

			for (String n : sec) {
                System.out.println(n);
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

			File configFile = new File(current + "/Config.yaml");

			Config config = new Config(configFile.getAbsolutePath());
			if(config.getString("project." + name) == null) {
				config.set("project." + name, path.getAbsolutePath());
			}

			config.save();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void updateProjectEntry(String name, File path) {

		String current = null;


		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");

			Config config = new Config(configFile.getAbsolutePath());
			if(config.getString("project." + name) != null) {
				config.set("project." + name, path.getAbsolutePath());
			}

			config.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void removeProject(String name) {

		String current = null;


		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");

			Config config = new Config(configFile.getAbsolutePath());
			if(config.contains("project." + name)) {
				config.remove("project." + name);
			}

			config.save();


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
		info = new File(outPath + "/SkriptIDE-Project.yaml");

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

		Config config = new Config(info.getAbsolutePath());


		config.set("name", name);
		config.set("path", script.getAbsolutePath());
		config.set("folder-path", path);
		config.set("skript-Version", skript.getVersion());
		config.set("skript-path", skript.getPath());
		config.set("server-name", server.getname());
		config.set("output-path", server.getPlFolderPath() + "/Skript/scripts");

		config.set("notes", notes);

		config.save();

		if (Main.debugMode) {

            System.out.println("A Project file has been created");
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

	public String getNotes() { return this.notes; }

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
		String newPath = oldfolder.getAbsolutePath().substring(0, oldfolder.getAbsolutePath().length() - this.name.length()) + newName + "/";
		oldfolder.renameTo(new File(newPath));
		File sk = new File(newPath, this.name + ".sk");
		sk.renameTo(new File(newPath, newName + ".sk"));

		String outPath = "";
		File dir = null;
		File script = null;
		File info = null;


		info = new File(newPath + "SkriptIDE-Project.yaml");

        Config config = new Config(info.getAbsolutePath());


        config.set("name", newName);
        config.set("path", new File(newPath, newName + ".sk").getAbsolutePath());
        config.set("folder-path", newPath);
        config.set("skript-Version", this.sk.getVersion());
        config.set("skript-path", this.sk.getPath());
        config.set("server-name", server.getname());
        config.set("output-path", server.getPlFolderPath() + "/Skript/scripts");

        config.set("notes", notes);

        config.save();
		String current = null;


		try {
			current = new File(".").getCanonicalPath();

			File configFile = new File(current + "/Config.yaml");

            Config con = new Config(configFile.getAbsolutePath());
			con.set("project." + newName, newPath + "SkriptIDE-Project.yaml");
			con.remove("project." + this.name);
			con.save();

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
