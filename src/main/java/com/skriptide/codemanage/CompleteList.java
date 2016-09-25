package com.skriptide.codemanage;

import com.skriptide.main.Main;
import com.skriptide.util.skunityapi.*;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.PopupAlignment;

import java.util.ArrayList;

/**
 * Created by yannh on 25.09.2016.
 */
public class CompleteList {

    private ListView<String> chooseView;
    private boolean showList;
    private Popup win;
    private ArrayList<String> all = new ArrayList<>();
    private int pos = 0;

    public void setList(Popup win, ListView chooseView, TabPane codeTabPane, boolean showList) {
        SkUnityAPI skUnity = new SkUnityAPI();

        if (win == null) {
            win = new Popup();
            chooseView = new ListView();
        }

        ArrayList<ApiCondition> conditions = skUnity.getConditions();
        ArrayList<ApiEffect> effects = skUnity.getEffects();
        ArrayList<ApiEvent> events = skUnity.getEvents();
        ArrayList<ApiExpression> expressions = skUnity.getExpressions();
        ArrayList<ApiType> types = skUnity.getTypes();
        for (int i = 1; i != conditions.size(); i++) {

            chooseView.getItems().add(conditions.get(i).getId() + " Condition\n" + conditions.get(i).getAddon());
        }
        for (int i = 0; i != effects.size(); i++) {
            chooseView.getItems().add(effects.get(i).getId() + " Effect\n" + effects.get(i).getAddon());
        }
        for (int i = 0; i != events.size(); i++) {
            chooseView.getItems().add(events.get(i).getId() + " Event\n" + events.get(i).getAddon());
        }
        for (int i = 0; i != expressions.size(); i++) {
            chooseView.getItems().add(expressions.get(i).getId() + " Expression\n" + expressions.get(i).getAddon());
        }
        for (int i = 0; i != types.size(); i++) {
            chooseView.getItems().add(types.get(i).getId() + " Type\n" + types.get(i).getAddon());
        }

        chooseView.getItems().addAll(new Supers().getSupervArray());

        chooseView.setPrefSize(180, 200);

        Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
        CodeArea area = (CodeArea) tab.getContent();
        win.getContent().add(chooseView);

        area.setPopupWindow(win);




        if (Main.debugMode) {
            System.out.println("loaded list");
        }
    }

    public void chooseList(TabPane codeTabPane, Button commandSendBtn) {

        Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
        CodeArea area = (CodeArea) tab.getContent();

        if (win == null) {
            win = new Popup();
            chooseView = new ListView();
            setList(win, chooseView, codeTabPane, showList);

            area.setOnMouseClicked(event -> {
                if (showList) {

                    win.hide();
                    chooseView.setVisible(false);
                    showList = false;

                }
            });
        }


        if (!win.isShowing()) {

            Stage stage = (Stage) commandSendBtn.getScene().getWindow();


            area.setPopupAlignment(PopupAlignment.CARET_BOTTOM);
            win.show(stage);
            chooseView.setVisible(true);
            showList = true;
            if (Main.debugMode) {
                System.out.println("showed list");
            }

        }
        updateList(codeTabPane, win, chooseView, showList);


    }

    public void updateList(TabPane codeTabPane, Popup win, ListView<String> chooseView, boolean showList) {

        Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
        CodeArea area = (CodeArea) tab.getContent();


        area.caretPositionProperty().addListener((obs, oldPosition, newPosition) -> {

            String text = area.getText().substring(0, newPosition.intValue());
            int index;
            for (index = text.length() - 1; index >= 0 && !Character.isWhitespace(text.charAt(index)); index--) ;
            String prefix = text.substring(index + 1, text.length());


            if (chooseView.isVisible() && !(chooseView == null)) {
                javafx.application.Platform.runLater(() -> {


                            chooseView.getItems().sorted().stream().filter(str -> !str.toLowerCase().contains(prefix.toLowerCase())).forEach(str -> {
                                chooseView.getItems().remove(str);
                                all.add(str);
                            });

                            ArrayList<String> toRemove = new ArrayList<String>();
                            for (int i = 0; i != all.size(); i++) {
                                String current = all.get(i);
                                if (current.toLowerCase().contains(prefix.toLowerCase())) {
                                    chooseView.getItems().add(current);
                                    toRemove.add(current);
                                    chooseView.refresh();
                                }
                            }
                            for (int i = 0; i != toRemove.size(); i++) {
                                String rem = toRemove.get(i);
                                if (all.contains(rem)) {
                                    all.remove(rem);
                                }
                            }

                        }


                );
            }
            Popup finalWin = win;
            final ListView[] finalChooseView = {chooseView};
            final boolean[] finalShowList = {showList};
            chooseView.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    KeyCode code = event.getCode();

                    if (code == KeyCode.ESCAPE) {


                        if (finalShowList[0]) {

                            win.hide();
                            chooseView.setVisible(false);
                            finalShowList[0] = false;

                        }
                    } else if (code == KeyCode.ENTER) {
                        if (showList) {

                            setWord(codeTabPane, win, chooseView, showList, prefix);

                        }
                    }
                }
            });
            chooseView.setOnMouseClicked(event -> setWord(codeTabPane, win, chooseView, showList, prefix));

        });

        if (Main.debugMode) {
            System.out.println("Updatet list");
        }
    }

    private void setWord(TabPane codeTabPane, Popup win, ListView<String> chooseView, boolean showList, String prefix) {


        Tab tab = codeTabPane.getSelectionModel().getSelectedItem();
        CodeArea area = (CodeArea) tab.getContent();
        String seletion = chooseView.getSelectionModel().getSelectedItem();
        String before;
        if (seletion.contains("Event")) {

            String trueT = "";
            String[] split = seletion.split(" ");
            String[] r = split[0].split("(?=\\p{Upper})");
            for (int i = 0; i != r.length; i++) {
                trueT = trueT + " " + r[i];
            }

            String space = "";
            for (int i = 0; i != trueT.length(); i++) {
                space = space + " ";
            }

            before = area.getText(area.getCaretPosition(), area.getCaretPosition() - prefix.length() + trueT.trim().length() * 2);


            area.replaceText(area.getCaretPosition() - prefix.length(), area.getCaretPosition() - prefix.length() + trueT.trim().length() * 2, trueT.trim() + ":\n" + space + before);


            pos = area.getCaretPosition() - before.length();

        } else {
            String trueT = "";
            String[] split = seletion.split(" ");
            trueT = split[0];


            before = area.getText(area.getCaretPosition(), area.getCaretPosition() - prefix.length() + trueT.trim().length() * 2);

            area.replaceText(area.getCaretPosition() - prefix.length(), area.getCaretPosition() - prefix.length() + trueT.trim().length() * 2, trueT.trim() + ": " + before);


            pos = area.getCaretPosition() - before.length();
        }
        System.out.println(pos);
        area.moveTo(pos);


        if (showList) {
            win.hide();
            chooseView.setVisible(false);


        }
        if (Main.debugMode) {
            System.out.println("set word: " + prefix);
        }
    }
}
