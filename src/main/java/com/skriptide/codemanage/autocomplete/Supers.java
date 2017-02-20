package com.skriptide.codemanage.autocomplete;

import com.skriptide.Main;

/**
 * Created by Liz3ga on 01.09.2016.
 */
public class Supers {

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
