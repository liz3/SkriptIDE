package com.skriptide.include;

import com.skriptide.config.Config;

import java.io.File;

/**
 * Created by yannh on 27.01.2017.
 */
public class Server {

    private String name;
    private String path;

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Api getApi() {
        return api;
    }

    public Skript getSkript() {
        return skript;
    }

    private Api api;
    private Skript skript;

    public Server(Config config, File f) {


    }

}
