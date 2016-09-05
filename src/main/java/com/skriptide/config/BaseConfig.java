package com.lapiz.config;

import com.google.common.io.Files;
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

    public Map<String, Object> getValues() {
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
            e.printStackTrace();
        }

    }

    public void create() {
        try {
            if (!getConfFile().exists()) {
                Files.createParentDirs(getConfFile());
                getConfFile().createNewFile();

            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void save() {
        try {
            yaml.dump(getValues(), new BufferedWriter(new FileWriter(getConfFile())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
