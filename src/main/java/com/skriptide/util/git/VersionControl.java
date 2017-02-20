package com.skriptide.util.git;

import java.io.File;

/**
 * Created by Matthew E on 1/26/2017.
 */
public class VersionControl {

    private File projectFile;

    public VersionControl(File projectFile) {
        this.projectFile = projectFile;
    }

    public VersionControl commit(String message) {

        return this;
    }

    public VersionControl pull() {

        return this;
    }

    public VersionControl merge() {

        return this;
    }

    public VersionControl push() {

        return this;
    }


    public File getProjectFile() {
        return projectFile;
    }

    public void setProjectFile(File projectFile) {
        this.projectFile = projectFile;
    }
}
