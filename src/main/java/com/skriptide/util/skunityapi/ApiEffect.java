package com.skriptide.util.skunityapi;

import java.util.Objects;

/**
 * Created by Liz3ga on 31.08.2016.
 */
public class ApiEffect {

	private final String id;
	private final String feature;
	private final String addon;
	private final String pattern;
	private final String description;
	private final String example;
	private final String requirements;

	public ApiEffect(String id, String feature, String addon, String pattern, String description, String example, String requirements) {
		this.id = id;
		this.feature = feature;
		this.addon = addon;
		this.pattern = pattern;
		this.description = description;
		this.example = example;
		this.requirements = requirements;
	}

	public String getId() {
		if (!Objects.equals(this.id, "")) {
			return this.id;
		}
		return null;
	}

	public String getFeature() {
		if (!Objects.equals(this.feature, "")) {
			return this.feature;
		}
		return null;
	}

	public String getAddon() {
		if (!Objects.equals(this.addon, "")) {
			return this.addon;
		}
		return null;
	}

	public String getPattern() {
		if (!Objects.equals(this.pattern, "")) {
			return this.pattern;
		}
		return null;
	}

	public String getDescription() {
		if (!Objects.equals(this.description, "")) {
			return this.description;
		}
		return null;
	}

	public String getExample() {
		if (!Objects.equals(this.example, "")) {
			return this.example;
		}
		return null;
	}

	public String getRequirements() {
		if (!Objects.equals(this.requirements, "")) {
			return this.requirements;
		}
		return null;
	}
}
