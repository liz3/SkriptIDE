package com.skriptide.util;

import com.skriptide.gui.SceneManager;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Liz3 on 04.09.2016.
 */
public class IDESystemErr extends PrintStream {

	String err = "";
	boolean recording = false;
	public IDESystemErr() {
		super(System.err);
	}

	@Override
	public void println(String msg) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("d.M.Y HH:mm:ss");
		if (!msg.startsWith("["))
			msg = " " + msg;
		String f = "[" + sdf.format(cal.getTime()) + " | ERROR]" + msg;
		//TODO Add the DebugArea to the main class or so

		/*if (SceneManager.debugArea != null) {
			SceneManager.debugArea.appendText(f + System.getProperty("line.separator"));
		} */
		err += f + "\n";
		if(!recording) {

			recording = true;

			new Thread(() -> {

				try {
					Thread.sleep(750);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ErrorReport.reportError(err);
                recording = false;
                err = "";

            }).start();

		}

		super.println(f);

	}

	@Override
	public void println(Object msg) {
		if (msg instanceof String)
			println((String) msg);
		else
			super.println(msg);
	}

}
