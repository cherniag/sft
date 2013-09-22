package com.gc.textsearcher;

import java.util.HashMap;
import java.util.Map;

public class StaticDataHolder {
	private static Map<String, String> properties = new HashMap<String, String>();
	private static Map<String, Object> data = new HashMap<String, Object>();
	
	public static void setProperty(String name, String value){
		properties.put(name, value);
	}
	
	public static String getProperty(String name) {
		return properties.get(name);
	}
	
	public static String getProperty(String name, String defaultValue) {
		String result = properties.get(name);
		return result != null ? result : defaultValue;
	}
	
	public static void setData(String name, Object value){
		data.put(name, value);
	}
	
	public static Object getData(String name) {
		return data.get(name);
	}
}
