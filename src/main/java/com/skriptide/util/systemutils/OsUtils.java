package com.skriptide.util.systemutils;

/**
 * Created by liz3 on 28.10.16.
 */
public class OsUtils {

    public static OperatingSystemType getOS() {

        String os = System.getProperty("os.name").toLowerCase();

        if(os.contains("windows")) {
            return OperatingSystemType.WINDOWS;
        }
        if(os.contains("linux") || os.contains("unix")) {
            return OperatingSystemType.LINUX;
        }
        if(os.contains("osx") || os.contains("mac") || os.contains("ios")) {
            return OperatingSystemType.OSX;
        }




        return null;
    }
}
