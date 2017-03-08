package com.skriptide.config;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    @Override
    public void remove(String path) {
        ArrayList<String> toRemove = new ArrayList<>();
        for (String str : getValues().keySet()) {
            if (str.contains(path)) {
                toRemove.add(str);
            }
        }
        for (String str : toRemove) {
            getValues().remove(str);
        }
    }

    @Override
    public List<String> getNextOf(String next) {

        Set<String> allEntries = getValues().keySet();
        ArrayList<String> found = new ArrayList<>();
        for (String currentKey : allEntries) {

            if (currentKey.contains(next)) {

                String truePath = "";
                String[] split = currentKey.split(Pattern.quote("."));
                for (int i = 0; i != split.length; i++)
                    if (split[i].equals(next)) {

                        truePath = truePath + "." + split[i] + "." + split[i + 1];

                        found.add(truePath.substring(1));
                    } else {
                        truePath = truePath + "." + split[i];
                    }

            }
        }


        return found;
    }

    @Override
    public List<String> getAll(String next) {

        Set<String> allEntries = getValues().keySet();
        ArrayList<String> found = new ArrayList<>();
        for (String currentKey : allEntries) {

            if (currentKey.contains(next)) {


                String[] split = currentKey.split(Pattern.quote("."));
                for (int i = 0; i != split.length; i++) {
                    if (split[i].equals(next)) {

                        if (!found.contains(split[i + 1])) {
                            found.add(split[i + 1]);
                        }
                    }
                }

            }
        }


        return found;
    }

    public List<String> containsAll(String arg) {
        List<String> founds = null;
        founds.addAll(getValues().keySet().stream().filter(str -> str.contains(arg)).collect(Collectors.toList()));
        return founds;
    }

    public boolean contains(String arg) {

        for (String str : getValues().keySet()) {
            if (str.contains(arg)) {
                return true;
            }

        }
        return false;
    }
}
