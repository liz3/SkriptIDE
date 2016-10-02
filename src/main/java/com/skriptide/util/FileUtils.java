package com.skriptide.util;

import java.io.File;

/**
 * Created by yannh on 02.10.2016.
 */
public class FileUtils {

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
