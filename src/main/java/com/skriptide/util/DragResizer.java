package com.skriptide.util;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

/**
 * Created by Liz3ga on 11.09.2016.
 */
public class DragResizer {

	private static final int RESIZE_MARGIN = 5;

	private final Region region;

	private double y;

	private boolean initMinHeight;

	private boolean dragging;

	private DragResizer(Region aRegion) {
		region = aRegion;
	}

	public static void makeResizable(Region region) {
		final DragResizer resizer = new DragResizer(region);

		region.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				resizer.mousePressed(event);
			}});
		region.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				resizer.mouseDragged(event);
			}});
		region.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				resizer.mouseOver(event);
			}});
		region.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				resizer.mouseReleased();
			}});
	}

	private void mouseReleased() {
		dragging = false;
		region.setCursor(Cursor.DEFAULT);
	}

	private void mouseOver(MouseEvent event) {
		if(isInDraggableZone(event) || dragging) {
			region.setCursor(Cursor.S_RESIZE);
		}
		else {
			region.setCursor(Cursor.DEFAULT);
		}
	}

	private boolean isInDraggableZone(MouseEvent event) {
		return event.getY() > (region.getHeight() - RESIZE_MARGIN);
	}

	private void mouseDragged(MouseEvent event) {
		if(!dragging) {
			return;
		}

		double mousey = event.getY();

		double newHeight = region.getMinHeight() + (mousey - y);

		region.setMinHeight(newHeight);

		y = mousey;
	}

	private void mousePressed(MouseEvent event) {

		// ignore clicks outside of the draggable margin
		if(!isInDraggableZone(event)) {
			return;
		}

		dragging = true;

		// make sure that the minimum height is set to the current height once,
		// setting a min height that is smaller than the current height will
		// have no effect
		if (!initMinHeight) {
			region.setMinHeight(region.getHeight());
			initMinHeight = true;
		}

		y = event.getY();
	}

}
