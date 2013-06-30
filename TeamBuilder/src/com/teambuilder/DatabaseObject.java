package com.teambuilder;

import java.util.HashMap;
import java.util.Map;

public class DatabaseObject {

	public static final int NORMAL = 0;
	public static final int ADD_TO_DATABASE = 1;
	public static final int UPDATE_DATABASE = 2;
	public static final int REMOVE_FROM_DATABASE = 3;
	
	public static final int NO_ID = -1;
	
	private int status;
	private Integer id;
	private Map<String, Object> values;
	
	public DatabaseObject() {
		this(NO_ID);
	}
	
	public DatabaseObject (int id) {
		this(id, null);
	}
	
	public DatabaseObject (Integer id, Map<String, Object> values) {
		this.id = id;
		if (values != null) {
			this.values = values;
		} else {
			this.values = new HashMap<String, Object>();
		}
		status = NORMAL;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Object getValue(String key) {
		return values.get(key);
	}
	
	public Map<String, Object> getValues() {
		return values;
	}

	public void setValue(String key, Object value) {
		values.put(key, value);
	}
	
	public String getName() {
		Object name = values.get("name");
		if (name != null) {
			return (String)name;
		} else {
			return null;
		}
	}
	
	@Override
	public String toString() {
		if (values.get("name") != null) {
			return (String)values.get("name");
		} else {
			return super.toString();
		}
	}
	
}
