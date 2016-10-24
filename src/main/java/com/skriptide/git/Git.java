package com.skriptide.git;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2016 Matthew E Development - All Rights Reserved
 * You may NOT use, distribute and modify this code.
 * <p>
 * Created by Matthew E on 10/23/2016 at 7:08 PM.
 */
public abstract class Git {

    public List<File> files;

    public Git() {
        this.files = new ArrayList<>();
    }

    public abstract void push();

    public abstract void pull();

    public void addFile(File file) {
        files.add(file);
    }

    public void removeFile(File file) {
        files.remove(file);
    }
}
