package com.skriptide.config;

import java.util.List;
import java.util.Map;

public interface ConfigSection {

    Map<String, Object> getValues();

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
