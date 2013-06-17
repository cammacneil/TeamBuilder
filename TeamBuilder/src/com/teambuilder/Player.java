package com.teambuilder;

public class Player {

	private String sFirst_Name;
	private String sLast_Name;
	private int int_Skill;
	
	public Player (String sFirst_Name, String sLast_Name, int int_Skill)
	{
		this.sFirst_Name = sFirst_Name;
		this.sLast_Name = sLast_Name;
		this.int_Skill = int_Skill;
	}

	public String getsFirst_Name() {
		return sFirst_Name;
	}

	public void setsFirst_Name(String sFirst_Name) {
		this.sFirst_Name = sFirst_Name;
	}

	public String getsLast_Name() {
		return sLast_Name;
	}

	public void setsLast_Name(String sLast_Name) {
		this.sLast_Name = sLast_Name;
	}

	public int getInt_Skill() {
		return int_Skill;
	}

	public void setInt_Skill(int int_Skill) {
		this.int_Skill = int_Skill;
	}
	
	@Override
	public String toString() {
		return sFirst_Name + " " + sLast_Name + " - " + int_Skill;
	}
}
