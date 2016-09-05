package com.lapiz.config;

import java.util.List;

public interface ConfigSection {

    Object getObject(String path);

    String getString(String path);

    boolean getBoolean(String path);

    int getInt(String path);

    long getLong(String path);

    double getDouble(String path);

    float getFloat(String path);

    List<String> getStringList(String path);

    List<Integer> getIntList(String path);

    void set(String path, Object value);

}
