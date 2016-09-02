package com.skriptide.util;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipInputStream;

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

    public static String getNameOfPlugin(File file) {


        try {
            URL url = new URL("jar:file:" + file.getAbsolutePath() + "!/plugin.yml");
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