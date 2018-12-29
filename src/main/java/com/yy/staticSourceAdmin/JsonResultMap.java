package com.yy.staticSourceAdmin;

import java.util.HashMap;

public class JsonResultMap extends HashMap<String, Object> {
	private static final long serialVersionUID = 4410932462463634427L;

	public JsonResultMap set(String key, Object value) {
		put(key, value);
		return this;
	}
}