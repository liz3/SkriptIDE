package com.skriptide.codemanage;

import com.skriptide.main.Main;

import java.util.ArrayList;

/**
 * Created by Liz3ga on 01.09.2016.
 */
class Supers {

	private final String[] supervArray;

	public Supers() {

		String[] list = new String[]{
				"if", "else", "loop", "trigger", "stop", "set"
		};
		supervArray = list;

		if(Main.debugMode) {
			System.out.println("Loaded supers!");
		}

	}

	public String[] getSupervArray() {
		return this.supervArray;
	}


}
