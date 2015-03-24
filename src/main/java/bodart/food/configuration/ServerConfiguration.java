/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodart.food.configuration;

import bodart.food.application.FoodApplication;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author Gilles
 */
public class ServerConfiguration {
	public static String SERVER_FILE_SEPARATOR = File.separator;
	public static String SERVER_URL_CHARS_RANGE;

	private static void load(Properties properties) {
		SERVER_URL_CHARS_RANGE = properties
				.getProperty("SERVER_URL_CHARS_RANGE");
	}
	public static void configureProperties() {
		try {
			Logger.getLogger(FoodApplication.class).info(
					"Configuration of the properties");
			Properties properties = new Properties();
			FileInputStream input = new FileInputStream("server.properties");
			properties.load(input);
			ServerConfiguration.load(properties);
		} catch (IOException e) {
			Logger.getLogger(FoodApplication.class).error(e);
		}
	}

}
