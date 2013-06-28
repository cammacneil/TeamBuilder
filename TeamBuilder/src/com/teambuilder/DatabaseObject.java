package com.teambuilder;

public class DatabaseObject {

	public static final int NORMAL = 0;
	public static final int ADD_TO_DATABASE = 1;
	public static final int REMOVE_FROM_DATABASE = 2;
	public static final int ADD_VALUE_FOR_PLAYER = 3;
	public static final int UPDATE_VALUE_FOR_PLAYER = 3;
	public static final int REMOVE_VALUE_FOR_PLAYER = 4;
	
	
	private int status;
	private int id;
	private String name;
	
	public DatabaseObject (int id, String name) {
		this.id = id;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
