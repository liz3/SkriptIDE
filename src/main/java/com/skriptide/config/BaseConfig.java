package com.lapiz.config;

import com.google.common.io.Files;
import com.lapiz.Lapiz;
import com.lapiz.utils.logger.Log;
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
            Lapiz.LOGGER.log(Log.SEVERE, "An error occured loading config: " + getConfFile().getName());

        }

    }

    public void create() {
        try {
            if (!getConfFile().exists()) {
                Files.createParentDirs(getConfFile());
                getConfFile().createNewFile();

            }

        } catch (IOException e) {
            Lapiz.LOGGER.log(Log.SEVERE, "An error occured creating config: " + getConfFile().getName());

        }
    }

    public void save() {
        try {
            yaml.dump(getValues(), new BufferedWriter(new FileWriter(getConfFile())));
        } catch (IOException e) {
            Lapiz.LOGGER.log(Log.SEVERE, "An error occured saving config: " + getConfFile().getName());
        }
    }

}
