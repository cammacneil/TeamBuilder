package com.teambuilder;

public class Group {

	public static final int NORMAL = 0;
	public static final int ADD_OR_UPDATE_VALUE = 1;
	public static final int REMOVE_FROM_DATABASE = 2;
	
	private int status;
	private int id;
	private String name;
	
	public Group (int id, String name) {
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
	
}
