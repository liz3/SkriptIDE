package com.skriptide.main;

import com.skriptide.config.Config;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by liz3 on 19.09.16.
 */
class ConfigTests {

    public static void makeTests() {

        try {
            String local = new File(".").getCanonicalPath();

            File testFile = new File(local + "/tests.yaml");

            if(!testFile.exists()) {
                testFile.createNewFile();
            } else {
                testFile.delete();
                testFile.createNewFile();
            }
            Config con = new Config(testFile.getAbsolutePath());


            for(int i = 0; i != 35; i++) {
                con.set("lol.project." + i + ".name", "Name-" + i);
                con.set("lol.project." + i + ".version", "1.0.02-" + i);
                con.set("lol.project." + i + ".path", "Path-" + i);

            }
            con.save();

            List<String> list = con.getAll("project");

            for(String current : list) {
                System.out.println(current);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
