package com.skriptide.config;

//import com.google.common.io.Files;
import com.skriptide.main.Main;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseConfig {

    private final File confFile;
    private final Yaml yaml = new Yaml();
    private Map<String, Object> values;

    public BaseConfig(String path) {
        confFile = new File(path);

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
                Path pathToFile = Paths.get(getConfFile().getPath());
                Files.createDirectories(pathToFile.getParent());
                Files.createFile(pathToFile);
                File f = new File(pathToFile.toUri());
                f.delete();
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
