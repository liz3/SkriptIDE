package com.skriptide.util;

import com.skriptide.Main;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Liz3ga on 23.08.2016.
 */
public class VersionReader {


	public static String getVersionOfPlugin(File file) {


		try {
			URL url = new URL("jar:file:" + file.getAbsolutePath() + "!/plugin.yml");
			try {
				InputStream is = url.openStream();
				BufferedReader input = new BufferedReader(new InputStreamReader(is));
				Scanner sc = new Scanner(input);

				while (sc.hasNextLine()) {
					String line = sc.nextLine();

					if (line.contains("version")) {

						String[] split = line.split(" ");
						sc.close();
						is.close();
						input.close();
						System.out.println(split[1]);
						if(Main.debugMode) {
							System.out.println("got plugin version");
						}
						return split[1];
					}

				}


			} catch (IOException e) {
				e.printStackTrace();
			}


		} catch (MalformedURLException e) {
			e.printStackTrace();
		}


		return null;
	}

	public static String getVersionOfServer(File file) {
		try {
			JarFile f = new JarFile(file.getAbsolutePath());
			Enumeration<JarEntry> entries = f.entries();
			String name = "";
			while (entries.hasMoreElements()) {
				JarEntry current = entries.nextElement();
				if (current.getName().contains("net/minecraft/server/")) {
					String[] split = current.getName().split("/");
					if (split.length > 3) {
						name = split[3];
						break;
					}

				}
			}
			JarEntry entry = f.getJarEntry("net/minecraft/server/" + name + "/DedicatedServer.class");
			InputStream in = f.getInputStream(entry);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			String trueVer = "";
			boolean isCorrect = false;
			do {
				line = reader.readLine();
				if (line != null) {

					if (line.contains("Starting minecraft server version")) {

						trueVer = line.split(" ")[4];
						while (!isCorrect) {

							try {
								long t = Long.parseLong(trueVer.replace(".", ""));
								System.out.println(trueVer);
								isCorrect = true;
								break;


							} catch (NumberFormatException ex) {
								trueVer = trueVer.substring(0, trueVer.length() - 1);
							}

						}


					}
				}
			}
			while (line != null);

			reader.close();
			if(Main.debugMode) {
				System.out.println("Got server version");
			}
			return trueVer;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getNameOfPlugin(File file) {


		try {
			URL url = new URL("jar:file:" + file.getAbsolutePath() + "!/plugin.yml");
			if(url == null) {
				return null;
			}
			try {
				InputStream is = url.openStream();
				BufferedReader input = new BufferedReader(new InputStreamReader(is));
				Scanner sc = new Scanner(input);

				while (sc.hasNextLine()) {
					String line = sc.nextLine();

					if (line.contains("name")) {

						String[] split = line.split(" ");
						sc.close();
						is.close();
						input.close();
						System.out.println(split[1]);
						if(Main.debugMode) {
							System.out.println("Got name of plugin");
						}
						return split[1];
					}

				}


			} catch (IOException e) {
				e.printStackTrace();
			}


		} catch (MalformedURLException e) {
			e.printStackTrace();
		}


		return null;
	}


}