package com.skriptide.theme;

import java.io.File;

/**
 * Created by matt1 on 3/4/2017.
 */
public interface Theme {

    Theme load(File file);

    void use();

    void create();

    String getName();
}
