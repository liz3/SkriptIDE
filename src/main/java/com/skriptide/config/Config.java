package com.skriptide.config;


import java.util.List;
import java.util.Map;

public class Config extends BaseConfig implements ConfigSection {

    public Config(String path) {
        super(path);
    }

    @Override
    public Map<String, Object> getValues() {
        return getMappedValues();
    }

    @Override
    public Object getObject(String path) {
        return getValues().get(path);
    }

    @Override
    public String getString(String path) {
        return (String) getValues().get(path);
    }

    @Override
    public boolean getBoolean(String path) {
        return Boolean.valueOf((String) getValues().get(path));
    }

    @Override
    public int getInt(String path) {
        return Integer.valueOf((String) getValues().get(path));
    }

    @Override
    public long getLong(String path) {
        return Long.valueOf((String) getValues().get(path));
    }

    @Override
    public double getDouble(String path) {
        return Double.valueOf((String) getValues().get(path));
    }

    @Override
    public float getFloat(String path) {
        return Float.valueOf((String) getValues().get(path));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getStringList(String path) {
        return (List<String>) getValues().get(path);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Integer> getIntList(String path) {
        return (List<Integer>) getValues().get(path);
    }

    @Override
    public void set(String path, Object value) {
        getValues().put(path, value);
    }

}
