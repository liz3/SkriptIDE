package com.skriptide.codemanage;

import com.skriptide.util.skunityapi.*;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.fxmisc.richtext.CodeArea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

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

        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String selected = list.getSelectionModel().getSelectedItem();

                String line = selected.split(" ")[3];
                area.moveTo(Integer.valueOf(line));
                area.requestFocus();

            }
        });
    }

    public void perfomCeckAndSet() {

        found.clear();
        String text = area.getText();

        String[] lineSplit = text.split(System.getProperty("line.separator"));
        int l = 0;
        for(String line : lineSplit) {
            if(!line.startsWith("#")) {

                String[] spaces = line.split(" ");
                for (String str : spaces) {
                    for (ApiCondition con : conditions) {

                        if (str.toLowerCase().contains(con.getId().toLowerCase().trim())) {
                            found.add(new Entry(l, l + str.length(), con.getAddon()));
                        }
                    }
                    for (ApiEffect effect : effects) {

                        if (str.toLowerCase().contains(effect.getId().toLowerCase().trim())) {

                            found.add(new Entry(l, l + str.length(), effect.getAddon()));
                        }
                    }
                    for (ApiEvent event : events) {

                        if (str.toLowerCase().contains(event.getId().toLowerCase().trim())) {

                            found.add(new Entry(l, l + str.length(), event.getAddon()));
                        }
                    }
                    for (ApiExpression expression : expressions) {

                        if (str.toLowerCase().contains(expression.getId().toLowerCase().trim())) {

                            found.add(new Entry(l, l + str.length(), expression.getAddon()));
                        }
                    }
                    for (ApiType type : types) {

                        if (str.toLowerCase().contains(type.getId().toLowerCase().trim())) {

                            found.add(new Entry(l, l + str.length(), type.getAddon()));
                        }
                    }
                    l = l + str.length() + 1;

                }

            } else {
                l = l + line.length() - 1;
            }
        }
        list.getItems().clear();
        for(Entry entry : found) {
                list.getItems().add(entry.getType() + " [ At " + entry.getStart() + " ]");
            }



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

        public Entry(int start, int end, String type) {

            this.start = start;
            this.end = end;
            this.type = type;
        }
    }
}
