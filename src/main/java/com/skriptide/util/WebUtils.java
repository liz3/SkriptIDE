package com.skriptide.util;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * Created by Liz3ga on 31.08.2016.
 */
public class WebUtils {

	public static String getHTML(String urlToRead) throws Exception {
		StringBuilder result = new StringBuilder();
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		return result.toString();
	}

	public static void openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void downloadFile(URL url, File f) throws IOException, InterruptedException {
		if (!f.exists()) {
			f.createNewFile();
		}

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.connect();
		BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
		FileOutputStream fis = new FileOutputStream(f);
		byte[] buffer = new byte[1024];
		boolean count = false;

		int count1;
		while ((count1 = bis.read(buffer, 0, 1024)) != -1) {
			fis.write(buffer, 0, count1);
		}

		fis.close();
		bis.close();
	}
}
