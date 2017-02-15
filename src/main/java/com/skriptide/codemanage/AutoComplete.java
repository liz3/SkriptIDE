package com.skriptide.codemanage;

import com.skriptide.Main;
import com.skriptide.gui.OpenFile;
import com.skriptide.include.Server;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import org.fxmisc.richtext.CodeArea;

import java.util.regex.Pattern;

/**
 * Created by yannh on 25.09.2016.
 */
public class AutoComplete {


    public void setAutoComplete(CodeArea area, CompleteList completeList, TabPane codeTabPane, Button commandSendBtn, AddonDepenencies depenencies, OpenFile openFile) {


        area.setOnKeyReleased(event -> {


            KeyCode code = event.getCode();


            if (code == KeyCode.F5) {

                if(Main.runningServer == null) {

                    if(openFile.getOpenProject().getProject().getServer() != null) {

                        openFile.getOpenProject().getProject().writeCode(area.getText(), openFile.getProject().getName());

                        openFile.getOpenProject().getProject().copyToOutput(openFile.getProject(), openFile.getOpenProject().getProject().getServer());

                        openFile.getOpenProject().getProject().getServer().startServer();

                    } else {
                        Main.sceneManager.infoCheck("Error","Failed to launch project", "Please first set a server to the project or run the the skript trough the: \"Run on\" Menu", Alert.AlertType.ERROR);

                    }
                } else {

                    if(openFile.getOpenProject().getProject().getServer() != null) {

                        Server r = Main.runningServer;
                        Server s = openFile.getOpenProject().getProject().getServer();

                        if(r.getName().equals(s.getName())) {


                            openFile.getOpenProject().getProject().writeCode(area.getText(), openFile.getProject().getName());
                            openFile.getOpenProject().getProject().copyToOutput(openFile.getProject(), r);
                            r.sendCommand("skript reload " + openFile.getProject().getName());

                        } else {

                            boolean ok = Main.sceneManager.infoCheck("Error", "Failed to launch project",
                                    "The Running server is not the target server of the skript, do you want to start the skript on the current running serveer?", Alert.AlertType.CONFIRMATION);

                            if(ok) {
                                openFile.getOpenProject().getProject().writeCode(area.getText(), openFile.getProject().getName());
                                openFile.getOpenProject().getProject().copyToOutput(openFile.getProject(), r);
                                r.sendCommand("skript reload " + openFile.getProject().getName());
                            }
                        }

                    } else {
                        Main.sceneManager.infoCheck("Error","Failed to launch project", "Please first set a server to the project or run the the skript trough the: \"Run on\" Menu", Alert.AlertType.ERROR);

                    }

                }


                return;
            }
            if (event.isShiftDown()) {
                event.consume();

                if (code == KeyCode.DIGIT8) {

                    javafx.application.Platform.runLater(() -> {

                        area.replaceText(area.getCaretPosition(), area.getCaretPosition(), area.getText(area.getCaretPosition(), area.getCaretPosition()) + ")");
                        area.moveTo(area.getCaretPosition() - 1);
                    });

                }
                if (code == KeyCode.DIGIT2) {

                    javafx.application.Platform.runLater(() -> {

                        area.replaceText(area.getCaretPosition(), area.getCaretPosition(), area.getText(area.getCaretPosition(), area.getCaretPosition()) + "\"");
                        area.moveTo(area.getCaretPosition() - 1);
                    });
                }

                event.consume();
            }
            if (event.isShortcutDown()) {
                event.consume();
                if (code == KeyCode.DIGIT7) {


                    javafx.application.Platform.runLater(() -> {

                        area.replaceText(area.getCaretPosition(), area.getCaretPosition(), area.getText(area.getCaretPosition(), area.getCaretPosition()) + "}");
                        area.moveTo(area.getCaretPosition() - 1);
                    });

                }
                if (code == KeyCode.DIGIT8) {


                    javafx.application.Platform.runLater(() -> {

                        area.replaceText(area.getCaretPosition(), area.getCaretPosition(), area.getText(area.getCaretPosition(), area.getCaretPosition()) + "]");
                        area.moveTo(area.getCaretPosition() - 1);
                    });
                }


            } else if (isChar(code) && !(event.isAltDown()) && !(event.isControlDown()) && !(event.isShortcutDown()) && code != KeyCode.BACK_SPACE && code != KeyCode.ENTER) {

                if(Main.settings.isAutoComplete()) {

                    completeList.chooseList(commandSendBtn, depenencies, area);
                }
            }


        });

        area.setOnKeyPressed(event -> {


            KeyCode code = event.getCode();

            if (code == KeyCode.ENTER && completeList.win == null) {


                int current = area.getCaretPosition();
                String currentTotal = area.getText(0, current);
                String[] split = currentTotal.split(Pattern.quote("\n"));
                String[] splitTotal = area.getText().split(Pattern.quote("\n"));
                String currentLine = split[split.length - 1];
                String[] wordSplit = currentLine.split(" ");
                String lastWord = wordSplit[wordSplit.length - 1];

                if (splitTotal.length > split.length) {


                } else {
                    event.consume();
                    System.out.println("same length");
                    if (currentLine.startsWith("\t") || currentLine.startsWith(" ")) {

                        int count = 0;
                        for (int i = 0; i != currentLine.length(); i++) {

                            if (currentLine.charAt(i) == '\t') {

                                count++;

                            }
                        }

                        String txt = area.getText();
                        String replacement = txt + "\n";
                        for (int i = 0; i != count + 1; i++) {
                            replacement += "\t";
                        }
                        area.replaceText(replacement);


                    } else {
                        String txt = area.getText();
                        area.replaceText(txt + "\n\t");
                    }
                }

            } else if (code == KeyCode.ENTER && !completeList.win.isShowing()) {

                int current = area.getCaretPosition();
                String currentTotal = area.getText(0, current);
                String[] split = currentTotal.split(Pattern.quote("\n"));
                String[] splitTotal = area.getText().split(Pattern.quote("\n"));
                String currentLine = split[split.length - 1];
                String[] wordSplit = currentLine.split(" ");
                String lastWord = wordSplit[wordSplit.length - 1];

                if (splitTotal.length > split.length) {


                } else {
                    event.consume();
                    System.out.println("same length");
                    if (currentLine.startsWith("\t") || currentLine.startsWith(" ")) {

                        int count = 0;
                        for (int i = 0; i != currentLine.length(); i++) {

                            if (currentLine.charAt(i) == '\t') {

                                count++;

                            }
                        }

                        String txt = area.getText();
                        String replacement = txt + "\n";
                        for (int i = 0; i != count + 1; i++) {
                            replacement += "\t";
                        }
                        area.replaceText(replacement);


                    } else {
                        String txt = area.getText();
                        area.replaceText(txt + "\n\t");
                    }
                }


            }
        });
    }

    private boolean isChar(KeyCode code) {
        return code.isLetterKey() || code.isDigitKey();
    }


}
