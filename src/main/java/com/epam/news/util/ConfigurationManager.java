package com.epam.news.util;

import java.util.ResourceBundle;

/*
 * This class extracts data from config.properties file.
 */
public class ConfigurationManager {

	private final static ResourceBundle resourceBundle = ResourceBundle
			.getBundle("config");

	
	private ConfigurationManager() {
	}

	public static String getProperty(String key) {
		return resourceBundle.getString(key);
	}

}
