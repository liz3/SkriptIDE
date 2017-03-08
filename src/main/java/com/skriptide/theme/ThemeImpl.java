package com.skriptide.theme;

import java.io.File;
import java.io.IOException;

/**
 * Created by matt1 on 3/4/2017.
 */
public class ThemeImpl implements Theme {

    private File file;
    private String name;

    @Override
    public Theme load(File file) {
        this.file = file;
        this.name = file.getName().split(".yml")[0];
        return this;
    }

    public Theme load(String name) {
        this.name = name;
        // this.name = file.getName().split(".yml")[0];
        return this;
    }
    @Override
    public void use() {

    }

    @Override
    public void create() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
