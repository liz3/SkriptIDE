package com.skriptide.git;

/**
 * Created by Matthew E on 1/26/2017.
 */
public enum VCType {

    GITHUB("GitHub"),
    BITBUCKET("BitBucket"),
    GITLAB("GitLab");

    private String name;

    VCType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
