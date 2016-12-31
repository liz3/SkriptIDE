package com.skriptide.util;

import javafx.scene.Cursor;
import javafx.scene.control.TabPane;


public class DragResizer {

    private double windowHeight = 0;
    private double mousePosY = 0;
    private double bottomSecHeight = 0;


    public void makeResizable(TabPane bottomSec) {
        windowHeight = bottomSec.getScene().getHeight();

        bottomSec.setMinHeight(60);

        bottomSec.getScene().heightProperty().addListener((observable, oldValue, newValue) -> {
            if (bottomSec.getHeight() > newValue.doubleValue() - 60) {
                bottomSec.setMinHeight(newValue.doubleValue() - 60);
            }
        });
        bottomSec.setOnMouseMoved(event -> {
            windowHeight = bottomSec.getScene().getHeight();
            mousePosY = event.getSceneY();
            bottomSecHeight = bottomSec.getHeight();

            if(canDrag(windowHeight, mousePosY, bottomSecHeight) && bottomSec.getCursor() != Cursor.S_RESIZE) {
                bottomSec.setCursor(Cursor.S_RESIZE);
            } else if(bottomSec.getCursor() == Cursor.S_RESIZE) {
                bottomSec.setCursor(Cursor.DEFAULT);
            }

        });

        bottomSec.setOnMouseDragged(event -> {
            windowHeight = bottomSec.getScene().getHeight();
            mousePosY = event.getSceneY();
            bottomSecHeight = bottomSec.getHeight();
            if (canDrag(windowHeight, mousePosY, bottomSecHeight)) {
                bottomSec.setMinHeight(mousePosY < 60 ? windowHeight - 60 : windowHeight - event.getSceneY());
            }
        });
    }

    private boolean canDrag(double windowHeight, double mousePosY, double bottomSecHeight) {
        double bottomSecOffsetTop = windowHeight - bottomSecHeight;
        double mousePosYRelativeToBottomSec = mousePosY - bottomSecOffsetTop;
        return mousePosYRelativeToBottomSec <= 10;
    }
}
