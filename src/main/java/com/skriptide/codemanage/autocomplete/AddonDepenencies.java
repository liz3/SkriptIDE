package com.skriptide.codemanage.autocomplete;

import com.skriptide.util.skunityapi.*;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.fxmisc.richtext.CodeArea;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by liz3 on 29.10.16.
 */
public class AddonDepenencies {

    private ArrayList<ApiCondition> conditions;
    private ArrayList<ApiEffect> effects;
    private ArrayList<ApiEvent> events;
    private ArrayList<ApiExpression> expressions;
    private ArrayList<ApiType> types;
    private CodeArea area;
    private ListView<String> list;
    private ArrayList<Entry> found = new ArrayList<>();

    public AddonDepenencies(CodeArea area, SkUnityAPI skUnity, ListView<String> list) {

        this.area = area;
        this.list = list;
        conditions = skUnity.getConditions();
        effects = skUnity.getEffects();
        events = skUnity.getEvents();
        expressions = skUnity.getExpressions();
        types = skUnity.getTypes();

        list.setOnMouseClicked(event -> {
            String selected = list.getSelectionModel().getSelectedItem();

            String line = selected.split(" ")[3];
            area.moveTo(Integer.parseInt(line));
            area.requestFocus();

        });
    }

    void perfomCeckAndSet() {

        found.clear();
        String text = area.getText();

        System.out.println(text.replace(" ", "-"));

        Scanner sc = new Scanner(text);

        int all = 0;
        while (sc.hasNextLine()) {

            String line = sc.nextLine();

            if (isValid(line)) {

                while (line.startsWith("\t")) {
                    line = line.substring(1);
                    all++;
                }
                for (String str : line.split(" ")) {


                    int trueLength = str.length();
                    String t = str;
                    while (t.startsWith("\t")) {
                        t = t.substring(1);
                    }
                    System.out.println(str + " " + all);

                    for (ApiCondition con : conditions) {

                        if (str.toLowerCase().contains(con.getId().toLowerCase().trim())) {
                            found.add(new Entry(all, con.getAddon()));
                            break;
                        }
                    }
                    for (ApiEffect effect : effects) {

                        if (str.toLowerCase().contains(effect.getId().toLowerCase().trim())) {

                            found.add(new Entry(all, effect.getAddon()));
                            break;
                        }
                    }
                    for (ApiEvent event : events) {

                        if (str.toLowerCase().contains(event.getId().toLowerCase().trim())) {

                            found.add(new Entry(all, event.getAddon()));
                            break;
                        }
                    }
                    for (ApiExpression expression : expressions) {

                        if (str.toLowerCase().contains(expression.getId().toLowerCase().trim())) {

                            found.add(new Entry(all, expression.getAddon()));
                            break;
                        }
                    }
                    for (ApiType type : types) {

                        if (str.toLowerCase().contains(type.getId().toLowerCase().trim())) {

                            found.add(new Entry(all, type.getAddon()));
                            break;
                        }
                    }
                    all = all + trueLength + 1;

                }

                while (line.endsWith(" ")) {

                    line = line.substring(0, line.length() - 1);
                    all++;
                }
            } else {
                all = all + line.length() + 1;
            }
        }
        sc.close();
        list.getItems().clear();
        System.out.println("lol");
        for (Entry entry : found) {
            list.getItems().add(entry.getType() + " [ At " + entry.getStart() + " ]");
        }

/*
       
                        */


    }

    private class Entry {

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public String getType() {
            return type;
        }

        private int start;
        private int end;
        private String type;

        public Entry(int start, String type) {

            this.start = start;

            this.type = type;
        }
    }

    private boolean isValid(String str) {

        while (str.startsWith("\t")) {
            str = str.substring(1);

        }
        while (str.startsWith(" ")) {
            str = str.substring(1);

        }
        if (!str.equals("") && !str.equals(" ") && !str.equals("#") && !str.equals("\t")) {
            if (!str.startsWith("#")) {

                return true;
            }
        }
        return false;
    }
}
