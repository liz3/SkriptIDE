package com.skriptide.codemanage;

import com.skriptide.Main;
import com.skriptide.gui.SceneManager;
import com.skriptide.util.skunityapi.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.PopupAlignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yannh on 25.09.2016.
 */
public class CompleteList {

    private ListView<String> chooseView;
    public Popup win;
    private final ArrayList<String> all = new ArrayList<>();
    private static CompleteList me;
    private AddonDepenencies addonDepenencies = null;



    public static CompleteList getCurrentInstance() {
        return me;
    }

    private void setList(Popup win, ListView<String> chooseView, CodeArea area) {
        SkUnityAPI skUnity = new SkUnityAPI();

        if (win == null) {
            win = new Popup();
            chooseView = new ListView<>();
        }
        all.clear();

        ArrayList<ApiCondition> conditions = skUnity.getConditions();
        ArrayList<ApiEffect> effects = skUnity.getEffects();
        ArrayList<ApiEvent> events = skUnity.getEvents();
        ArrayList<ApiExpression> expressions = skUnity.getExpressions();
        ArrayList<ApiType> types = skUnity.getTypes();
        for (int i = 1; i != conditions.size(); i++) {

            all.add(conditions.get(i).getId() + " Condition\n" + conditions.get(i).getAddon());
        }
        for (int i = 0; i != effects.size(); i++) {
            all.add(effects.get(i).getId() + " Effect\n" + effects.get(i).getAddon());
        }
        for (int i = 0; i != events.size(); i++) {
            all.add(events.get(i).getId() + " Event\n" + events.get(i).getAddon());
        }
        for (int i = 0; i != expressions.size(); i++) {
            all.add(expressions.get(i).getId() + " Expression\n" + expressions.get(i).getAddon());
        }
        for (int i = 0; i != types.size(); i++) {
            all.add(types.get(i).getId() + " Type\n" + types.get(i).getAddon());
        }

        all.addAll(Arrays.asList(new Supers().getSupervArray()));

        chooseView.setPrefSize(180, 200);



        win.getContent().add(chooseView);

        area.setPopupWindow(win);


        updateList(area, win, chooseView);


        if (Main.debugMode) {
            System.out.println("loaded list");
        }
    }

    void chooseList( Button commandSendBtn, AddonDepenencies depenencies, CodeArea area) {

        this.addonDepenencies = depenencies;
        me = this;




        if (win == null) {
            win = new Popup();

            chooseView = new ListView<>();
            setList(win, chooseView, area);

            area.setOnMouseClicked(event -> {
                if (win.isShowing()) {
                    win.hide();
                    chooseView.setVisible(false);
                }
            });
        }


        if (!win.isShowing()) {
            Stage stage = (Stage) commandSendBtn.getScene().getWindow();
            area.setPopupAlignment(PopupAlignment.CARET_BOTTOM);
            win.show(stage);

            if (Main.debugMode) {
                System.out.println("showed list");
            }

        }
    }

    private void updateList(CodeArea area, Popup win, ListView<String> chooseView) {



        final String[] prefix = new String[1];


        area.caretPositionProperty().addListener((obs, oldPosition, newPosition) -> {


            if(area.isFocused()) {
                String text = area.getText().substring(0, newPosition);

                prefix[0] = text;
                if (prefix[0].contains("\n") && !prefix[0].equals("\n")) {

                    String[] parts = prefix[0].split("\n");
                    prefix[0] = parts[parts.length - 1].trim();
                }


                if (prefix[0].contains(" "))
                    prefix[0] = prefix[0].substring(prefix[0].lastIndexOf(" ") + 1);

                String completeText = area.getText();

                Pattern p = Pattern.compile("\"([^\"]*)\"");
                Matcher m = p.matcher(completeText);

                while (m.find()) {
                    //"test"  10 + 4 = 14

                    String g = m.group();
                    int start = completeText.indexOf(g);
                    int end = start + g.length();
                    if(newPosition > start && newPosition < end) {
                        win.hide();
                        chooseView.setVisible(false);

                        return;
                    }
                }
                if(prefix[0].startsWith("/")) {
                    win.hide();
                    chooseView.setVisible(false);

                    return;
                }
                if(!win.isShowing()) {
                    win.show(area.getScene().getWindow());
                    chooseView.setVisible(true);

                }

                if (prefix[0] == null || prefix[0].equalsIgnoreCase("")) {
                    win.hide();
                    chooseView.setVisible(false);

                    return;
                }
                //Moved on 21.12.2016 to prevent the popup if in "" or starting with a /
                chooseView.setVisible(true);
                if (chooseView.isVisible()) {
                    ObservableList<String> tempList = FXCollections.observableArrayList();
                    for (String item : all) {
                        if (item.toLowerCase().contains(prefix[0].toLowerCase()))
                            tempList.add(item);
                    }
                    if (tempList.size() != chooseView.getItems().size()) {
                        chooseView.getItems().clear();
                        chooseView.setItems(tempList);
                    }
                    chooseView.scrollTo(0);
                    chooseView.getSelectionModel().selectFirst();
                    if(chooseView.getItems().isEmpty()) {
                        win.hide();
                        chooseView.setVisible(false);

                    }
                }
            }

        });
        chooseView.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.ESCAPE) {
                if (win.isShowing()) {
                    win.hide();
                    chooseView.setVisible(false);

                }
            } else if (code == KeyCode.ENTER) {
                if (win.isShowing()) {

                    setWord(area, win, chooseView, prefix[0]);
                }
            }
        });
        chooseView.setOnMouseClicked(event -> setWord(area, win, chooseView, prefix[0]));
        if (Main.debugMode) {
            System.out.println("Updatet list");
        }
    }

    private void setWord(CodeArea area, Popup win, ListView<String> chooseView, String prefix) {





        String seletion;

        if (chooseView.getItems().size() == 0) {
            return;
        } else {
            seletion = chooseView.getSelectionModel().getSelectedItem();
        }

        String before;
        int pos;
        if (seletion.contains("Event")) {

            String trueT = "";
            String[] split = seletion.split(" ");
            String[] r = split[0].split("(?=\\p{Upper})");
            for (int i = 0; i != r.length; i++) {
                trueT = trueT + " " + r[i];
            }

            String space = "";
            for (int i = 0; i != trueT.trim().length() + 1; i++) {
                space = space + " ";
            }

            before = area.getText(area.getCaretPosition(), area.getCaretPosition() - prefix.length() + trueT.trim().length() * 2);


            area.replaceText(area.getCaretPosition() - prefix.length(), area.getCaretPosition() - prefix.length() + trueT.trim().length() * 2, trueT.trim() + ":\n" + space + before);


            pos = area.getCaretPosition() - before.length();

        } else {
            String trueT;
            String[] split = seletion.split(" ");
            trueT = split[0];


            before = area.getText(area.getCaretPosition(), area.getCaretPosition() - prefix.length() + trueT.trim().length() * 2);

            area.replaceText(area.getCaretPosition() - prefix.length(), area.getCaretPosition() - prefix.length() + trueT.trim().length() * 2, trueT.trim() + ": " + before);


            pos = area.getCaretPosition() - before.length();
        }

        System.out.println(pos);
        area.moveTo(pos);
        addonDepenencies.perfomCeckAndSet();

        if (win.isShowing()) {
            win.hide();
            chooseView.setVisible(false);


        }

        if (Main.debugMode) {
            System.out.println("set word: " + prefix);
        }
    }
}
