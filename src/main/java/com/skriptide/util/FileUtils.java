package com.skriptide.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Created by yannh on 02.10.2016.
 */
public class FileUtils {

    public static void copyFile(File from, File to, StandardCopyOption option) {

        try {

            Files.copy(from.toPath(), to.toPath(), option);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void copyFile(String from, String to, StandardCopyOption option) {

        try {

            Files.copy(new File(from).toPath(), new File(to).toPath(), option);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void deleteDirectory(File directory, boolean recursively) {
        if (recursively && directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i], recursively);
                    } else {
                        files[i].delete();
                    }
                }
            }
        }

        directory.delete();
    }

}
