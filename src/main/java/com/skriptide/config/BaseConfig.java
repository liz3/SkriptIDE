package com.skriptide.config;

import com.google.common.io.Files;
import com.skriptide.main.Main;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseConfig {

    private final File confFile;
    private final Yaml yaml = new Yaml();
    private Map<String, Object> values;

    public BaseConfig(String path) {
        confFile = new File("config/" + path);

        create();
        load();

    }

    public File getConfFile() {
        return confFile;
    }

    public Map<String, Object> getMappedValues() {
        return values;
    }

    @SuppressWarnings("unchecked")
    private void load() {
        try {
            values = (Map<String, Object>) yaml.load(new FileInputStream(getConfFile()));

            if (values == null) {
                values = new HashMap<>();
            }

        } catch (FileNotFoundException e) {
            if (Main.debugMode) {
                System.out.println("An error occurred loading: " + getConfFile());
            }
        }

    }

    public void create() {
        try {
            if (!getConfFile().exists()) {
                Files.createParentDirs(getConfFile());
                getConfFile().createNewFile();

            }

        } catch (IOException e) {
            if (Main.debugMode) {
                System.out.println("An error occurred creating: " + getConfFile());
            }

        }
    }

    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getConfFile()));
            writer.write(yaml.dumpAsMap(getMappedValues()));
            writer.flush();
            writer.close();

        } catch (IOException e) {
            if (Main.debugMode) {
                System.out.println("An error occurred saving: " + getConfFile());
            }

        }
    }

}
