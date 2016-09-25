package com.skriptide.codemanage;

import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import org.fxmisc.richtext.CodeArea;

import java.awt.event.KeyEvent;

/**
 * Created by yannh on 25.09.2016.
 */
public class AutoComplete {

    public void setAutoComplete(CodeArea area, CompleteList completeList, TabPane codeTabPane, Button commandSendBtn) {

        area.setOnKeyPressed(event -> {

            KeyCode code = event.getCode();
            if (event.isShiftDown()) {
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

            }
            if (event.isShortcutDown()) {
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


            } else if (isChar(code) && !(event.isShiftDown()) && !(event.isAltDown()) && !(event.isControlDown()) && !(event.isShortcutDown())) {
                completeList.chooseList(codeTabPane, commandSendBtn);
            }


        });
    }

    private boolean isChar(KeyCode code) {


        switch (code) {
            case A:
                return true;
            case B:
                return true;
            case C:
                return true;
            case D:
                return true;
            case F:
                return true;
            case G:
                return true;
            case H:
                return true;
            case I:
                return true;
            case J:
                return true;
            case K:
                return true;
            case L:
                return true;
            case M:
                return true;
            case N:
                return true;
            case O:
                return true;
            case P:
                return true;
            case Q:
                return true;
            case R:
                return true;
            case S:
                return true;
            case T:
                return true;
            case U:
                return true;
            case V:
                return true;
            case W:
                return true;
            case X:
                return true;
            case Y:
                return true;
            case Z:
                return true;
            case DIGIT0:
                return true;
            case DIGIT1:
                return true;
            case DIGIT2:
                return true;
            case DIGIT3:
                return true;
            case DIGIT4:
                return true;
            case DIGIT5:
                return true;
            case DIGIT6:
                return true;
            case DIGIT7:
                return true;
            case DIGIT8:
                return true;
            case DIGIT9:
                return true;


        }
        return false;
    }


}
