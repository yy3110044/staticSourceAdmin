package com.yy.staticSourceAdmin;

import java.util.HashMap;

public class MyMap extends HashMap<String, Object> {
	private static final long serialVersionUID = -1219116404809105891L;

	public MyMap set(String key, Object value) {
		put(key, value);
		return this;
	}
}