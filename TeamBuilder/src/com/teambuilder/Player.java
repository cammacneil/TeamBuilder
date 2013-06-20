package com.teambuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {

	private long id;
	private String name;
	
	private ArrayList<Integer> groups;
	private Map<Integer, Integer> skillsMap;
	
	public Player (String name)
	{
		this(name, null);
	}

	public Player (String name, Map<Integer, Integer> skillsMap) {
		this.name = name;
		if (skillsMap != null)
			this.skillsMap = skillsMap;
		else
			this.skillsMap = new HashMap<Integer, Integer>();
		
		groups = new ArrayList<Integer>();
		
	}
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSkill(Integer activityId, Integer skillLevel) {
		skillsMap.put(activityId, skillLevel);
	}
	
	public Integer getSkill(Integer activityId) {
		if (skillsMap.containsKey(activityId))
			return skillsMap.get(activityId);
		else
			return null;
	}
	
	public void addGroup(Integer groupId) {
		if (!groups.contains(groupId))
			groups.add(groupId);
	}
	
	public void removeGroup(Integer groupId) {
		if (groups.contains(groupId))
			groups.remove(groups.indexOf(groupId));
	}
	
	public ArrayList<Integer> getGroups() {
		return groups;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
