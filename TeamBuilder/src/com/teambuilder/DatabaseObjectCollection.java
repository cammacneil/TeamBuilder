package com.teambuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DatabaseObjectCollection {

	private List<DatabaseObject> dbos;
	
	public DatabaseObjectCollection() {
		dbos = new ArrayList<DatabaseObject>();
	}
	
	public void addAll(List<DatabaseObject> list) {
		dbos.addAll(list);
	}
	
	public void add(DatabaseObject dbo) {
		dbos.add(dbo);
	}
	
	public DatabaseObject get(int location) {
		return dbos.get(location);
	}
	
	public List<DatabaseObject> getAll() {
		return dbos;
	}
	
	public void remove(DatabaseObject dbo) {
		dbos.remove(dbo);
	}
	
	public DatabaseObject getObjectWithId(int id) {
		for (DatabaseObject dbo : dbos) {
			if (dbo.getId() == id) {
				return dbo;
			}
		}
		
		return null;
	}
	
	public DatabaseObject getObjectWithProperty(String key, Object value) {
		for (DatabaseObject dbo : dbos) {
			Map<String, Object> values = dbo.getValues();
			if (values.get(key) == value) {
				return dbo;
			}
		}
		
		return null;
	}
	
	public DatabaseObject getObjectWithProperties(String[] keys, Object[] values) {
		if (keys.length != values.length) {
			return null;
		}
		
		for (DatabaseObject dbo : dbos) {
			boolean match = true;
			Map<String, Object> vals = dbo.getValues();
			for (int i=0;i<keys.length;i++) {
				if (vals.get(keys[i]) != values[i]) {
					match = false;
					break;
				}
			}
			
			if (match) {
				return dbo;
			}
		}
		
		return null;
	}
	
	public List<Integer> getIdsForObjects() {
		List<Integer> ids = new ArrayList<Integer>();
		for (DatabaseObject dbo : dbos) {
			ids.add(dbo.getId());
		}
		
		return ids;
	}
	
	public List<?> getPropertyForObjects(String property) {
		List<Object> properties = new ArrayList<Object>();
		for (DatabaseObject dbo : dbos) {
			if (dbo.getValue(property) != null) {
				properties.add(dbo.getValue(property));
			}
		}
		
		return properties;
	}
}
