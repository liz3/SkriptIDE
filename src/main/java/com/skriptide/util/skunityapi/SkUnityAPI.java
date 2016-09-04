package com.skriptide.util.skunityapi;


import com.skriptide.util.WebUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Liz3ga on 31.08.2016.
 */
public class SkUnityAPI {

	private File mainFolder = null;
	private boolean folderExists;
	private File conditions;
	private File effects;
	private File events;
	private File expressions;
	private File types;


	public SkUnityAPI() {

		folderExists = exists();

		if (folderExists) {
			String current = null;
			try {
				current = new File(".").getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
			}


			File folder = new File(current + "/SkUnity/output");
			String outputFolder = folder.getAbsolutePath();
			conditions = new File(outputFolder + File.separator + "/conditions.json");
			effects = new File(outputFolder + File.separator + "/effects.json");
			events = new File(outputFolder + File.separator + "/events.json");
			expressions = new File(outputFolder + File.separator + "/expressions.json");
			types = new File(outputFolder + File.separator + "/types.json");
		} else {
			downloadAPI();
		}


	}

	private void downloadAPI() {
		String current = null;
		try {
			current = new File(".").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}


		File folder = new File(current + "/SkUnity");
		mainFolder = folder;
		if (!folder.exists()) {
			folder.mkdirs();
		}
		try {
			WebUtils.downloadFile(new URL("http://nfell2009.uk/skunity/api/zip/skunity.zip"), new File(folder.getAbsolutePath() + "/api.zip"));
			unZip(new File(folder.getAbsolutePath() + "/api.zip").getAbsolutePath(), folder.getAbsolutePath() + "/output");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private boolean exists() {

		String current = null;
		try {
			current = new File(".").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}


		File folder = new File(current + "/SkUnity");

		if (folder.exists()) {
			return true;
		}


		return false;
	}

	private void unZip(String zipFile, String outputFolder) {

		byte[] buffer = new byte[1024];

		try {

			//create output directory is not exists
			File folder = new File(outputFolder);
			if (!folder.exists()) {
				folder.mkdir();
			}

			//get the zip file content
			ZipInputStream zis =
					new ZipInputStream(new FileInputStream(zipFile));
			//get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {

				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator + fileName);

				System.out.println("file unzip : " + newFile.getAbsoluteFile());

				//create all non exists folders
				//else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs();

				FileOutputStream fos = new FileOutputStream(newFile);

				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				fos.close();
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();

			conditions = new File(outputFolder + File.separator + "/conditions.json");
			effects = new File(outputFolder + File.separator + "/effects.json");
			events = new File(outputFolder + File.separator + "/events.json");
			expressions = new File(outputFolder + File.separator + "/expressions.json");
			types = new File(outputFolder + File.separator + "/types.json");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public ArrayList<ApiCondition> getConditions() {

		String code = "";
		ArrayList<ApiCondition> values = new ArrayList<>();
		try {
			Scanner sc = new Scanner(conditions);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();

				code = code + line;
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		JSONArray obj = new JSONArray(code);

		for (int i = 1; i != obj.length(); i++) {
			String id = null;
			String feature = null;
			String addon = null;
			String pattern = null;
			String description = null;
			String example = null;
			String requirements = null;

			JSONObject object = obj.getJSONObject(i);


			if (object.has("id")) {
				id = object.getString("id");
				if (id.trim().equals("")) {
					continue;
				}
			}
			if (object.has("feature")) {
				feature = object.getString("feature");
			}
			if (object.has("feature")) {
				feature = object.getString("feature");
			}
			if (object.has("addon")) {
				addon = object.getString("addon");
			}
			if (object.has("patterns")) {
				pattern = object.getString("patterns");
			}
			if (object.has("description")) {
				description = object.getString("description");
			}
			if (object.has("example")) {
				example = object.getString("example");
			}
			if (object.has("requirements")) {
				requirements = object.getString("requirements");
			}

			values.add(new ApiCondition(id, feature, addon, pattern, description, example, requirements));
		}


		return values;

	}

	public ArrayList<ApiEffect> getEffects() {

		String code = "";
		ArrayList<ApiEffect> values = new ArrayList<>();
		try {
			Scanner sc = new Scanner(effects);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();

				code = code + line;
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		JSONArray obj = new JSONArray(code);

		for (int i = 1; i != obj.length(); i++) {
			String id = null;
			String feature = null;
			String addon = null;
			String pattern = null;
			String description = null;
			String example = null;
			String requirements = null;

			JSONObject object = obj.getJSONObject(i);
			if (object.has("id")) {
				id = object.getString("id");
				if (id.trim().equals("")) {
					continue;
				}
			}
			if (object.has("feature")) {
				feature = object.getString("feature");
			}
			if (object.has("feature")) {
				feature = object.getString("feature");
			}
			if (object.has("addon")) {
				addon = object.getString("addon");
			}
			if (object.has("patterns")) {
				pattern = object.getString("patterns");
			}
			if (object.has("description")) {
				description = object.getString("description");
			}
			if (object.has("example")) {
				example = object.getString("example");
			}
			if (object.has("requirements")) {
				requirements = object.getString("requirements");
			}

			values.add(new ApiEffect(id, feature, addon, pattern, description, example, requirements));
		}


		return values;
	}

	public ArrayList<ApiEvent> getEvents() {

		String code = "";
		ArrayList<ApiEvent> values = new ArrayList<>();
		try {
			Scanner sc = new Scanner(events);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();

				code = code + line;
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		JSONArray obj = new JSONArray(code);

		for (int i = 0; i != obj.length(); i++) {
			String id = null;
			String feature = null;
			String addon = null;
			String pattern = null;
			String description = null;
			String example = null;
			String requirements = null;

			JSONObject object = obj.getJSONObject(i);
			if (object.has("id")) {
				id = object.getString("id");
				if (id.trim().equals("")) {
					continue;
				}
			}
			if (object.has("feature")) {
				feature = object.getString("feature");
			}
			if (object.has("feature")) {
				feature = object.getString("feature");
			}
			if (object.has("addon")) {
				addon = object.getString("addon");
			}
			if (object.has("patterns")) {
				pattern = object.getString("patterns");
			}
			if (object.has("description")) {
				description = object.getString("description");
			}
			if (object.has("example")) {
				example = object.getString("example");
			}
			if (object.has("requirements")) {
				requirements = object.getString("requirements");
			}

			values.add(new ApiEvent(id, feature, addon, pattern, description, example, requirements));
		}


		return values;


	}

	public ArrayList<ApiExpression> getExpressions() {

		String code = "";
		ArrayList<ApiExpression> values = new ArrayList<>();
		try {
			Scanner sc = new Scanner(expressions);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();

				code = code + line;
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		JSONArray obj = new JSONArray(code);

		for (int i = 1; i != obj.length(); i++) {
			String id = null;
			String feature = null;
			String addon = null;
			String pattern = null;
			String description = null;
			String example = null;
			String requirements = null;

			JSONObject object = obj.getJSONObject(i);
			if (object.has("id")) {
				id = object.getString("id");
				if (id.trim().equals("")) {
					continue;
				}
			}
			if (object.has("feature")) {
				feature = object.getString("feature");
			}
			if (object.has("feature")) {
				feature = object.getString("feature");
			}
			if (object.has("addon")) {
				addon = object.getString("addon");
			}
			if (object.has("patterns")) {
				pattern = object.getString("patterns");
			}
			if (object.has("description")) {
				description = object.getString("description");
			}
			if (object.has("example")) {
				example = object.getString("example");
			}
			if (object.has("requirements")) {
				requirements = object.getString("requirements");
			}

			values.add(new ApiExpression(id, feature, addon, pattern, description, example, requirements));
		}


		return values;


	}

	public ArrayList<ApiType> getTypes() {

		String code = "";
		ArrayList<ApiType> values = new ArrayList<>();
		try {
			Scanner sc = new Scanner(types);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();

				code = code + line;
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		JSONArray obj = new JSONArray(code);

		for (int i = 1; i != obj.length(); i++) {
			String id = null;
			String feature = null;
			String addon = null;
			String pattern = null;
			String description = null;
			String example = null;
			String requirements = null;

			JSONObject object = obj.getJSONObject(i);
			if (object.has("id")) {
				id = object.getString("id");
				if (id.trim().equals("")) {
					continue;
				}
			}
			if (object.has("feature")) {
				feature = object.getString("feature");
			}
			if (object.has("feature")) {
				feature = object.getString("feature");
			}
			if (object.has("addon")) {
				addon = object.getString("addon");
			}
			if (object.has("patterns")) {
				pattern = object.getString("patterns");
			}
			if (object.has("description")) {
				description = object.getString("description");
			}
			if (object.has("example")) {
				example = object.getString("example");
			}
			if (object.has("requirements")) {
				requirements = object.getString("requirements");
			}

			values.add(new ApiType(id, feature, addon, pattern, description, example, requirements));
		}


		return values;
	}
}
