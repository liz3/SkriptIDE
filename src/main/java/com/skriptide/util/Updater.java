package com.skriptide.util;

import com.skriptide.util.systemutils.OperatingSystemType;
import com.skriptide.util.systemutils.OsUtils;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by yannh on 24.01.2017.
 */
public class Updater {

    public static boolean checkUpdates() throws IOException {

        boolean isOld = false;
        String localPath = new File(".").getCanonicalPath();
        Ini ini = null;
        File iniFile = new File(localPath, "version.ini");
        if (!iniFile.exists()) {
            iniFile.createNewFile();
            ini = new Ini(iniFile);
            ini.add("version");
            ini.store();
            System.out.println("Config doesnt exits, created");
        } else {
            System.out.println("config exists");
            ini = new Ini(iniFile);
            if (!ini.containsKey("version")) {
                System.out.println("Config dont contains section, adding");
                ini.add("version");
                ini.store();
            }
        }
        File old = new File("version.txt");
        if (old.exists()) {

            old.delete();
            isOld = true;
        }
        if (!isOld) {
            if (!ini.get("version").containsKey("launcher")) {
                isOld = true;
            }
        }


        String version = null;
        try {
            version = WebUtils.getHTML("https://api.liz3.de/get/skide/launcher/version.php");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("version is " + version);
        if (isOld) {


            File oldLauncher = null;

            if (OsUtils.getOS() == OperatingSystemType.WINDOWS) {

                oldLauncher = new File("launch.exe");
            } else {
                oldLauncher = new File("launch.jar");
            }

            System.out.println(oldLauncher.delete());


            System.out.println("updating");
            update(version, ini);
            return true;
        } else {

            try {

                String local = ini.get("version").get("launcher");

                if (!local.equalsIgnoreCase(version)) {
                    System.out.println("version not the same, updating");
                    update(version, ini);
                    return true;
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Not updating");
        return false;
    }

    private static void update(String version, Ini ini) {

        Profile.Section sec = ini.get("version");

        if (OsUtils.getOS() == OperatingSystemType.WINDOWS) {
            try {
                WebUtils.downloadFile(new URL("https://api.liz3.de/get/skide/launcher/launch.exe"), new File("launch.exe"));
                System.out.println("downloaded the launcher, starting");
                sec.put("launcher", version);
                ini.store();


                ProcessBuilder pb = new ProcessBuilder("Java64/bin/java.exe", "-jar", new File("launch.exe").getAbsolutePath());

                try {
                    Process p = pb.start();
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            try {
                WebUtils.downloadFile(new URL("https://api.liz3.de/get/skide/launcher/launch.jar"), new File("launch.jar"));
                sec.put("launcher", version);
                ini.store();
            } catch (Exception e) {
                e.printStackTrace();
            }


            ProcessBuilder pb = new ProcessBuilder("Java64/bin/java.exe", "-jar", new File("launch.jar").getAbsolutePath());

            try {
                pb.start();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
