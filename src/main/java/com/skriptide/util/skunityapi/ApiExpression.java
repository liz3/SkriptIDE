package com.skriptide.util.skunityapi;

/**
 * Created by Liz3ga on 31.08.2016.
 */
public class ApiExpression {

    private String id;
    private String feature;
    private String addon;
    private String pattern;
    private String description;
    private String example;
    private String requirements;

    public ApiExpression(String id, String feature, String addon, String pattern, String description, String example, String requirements) {
        this.id = id;
        this.feature = feature;
        this.addon = addon;
        this.pattern = pattern;
        this.description = description;
        this.example = example;
        this.requirements = requirements;
    }

    public String getId() {
        if (this.id != "") {
            return this.id;
        }
        return null;
    }

    public String getFeature() {
        if (this.feature != "") {
            return this.feature;
        }
        return null;
    }

    public String getAddon() {
        if (this.addon != "") {
            return this.addon;
        }
        return null;
    }

    public String getPattern() {
        if (this.pattern != "") {
            return this.pattern;
        }
        return null;
    }

    public String getDescription() {
        if (this.description != "") {
            return this.description;
        }
        return null;
    }

    public String getExample() {
        if (this.example != "") {
            return this.example;
        }
        return null;
    }

    public String getRequirements() {
        if (this.requirements != "") {
            return this.requirements;
        }
        return null;
    }
}
