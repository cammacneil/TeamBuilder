package com.teambuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable {

	private long id;
	private String name;
	
	private List<Integer> groups;
	private Map<Integer, Integer> skillsMap;
	
	public static String label = "Player";
	
	public Player() {
		this(null, null, null);
	}
	
	public Player (String name)
	{
		this(name, null, null);
	}

	public Player (String name, Map<Integer, Integer> skillsMap, ArrayList<Integer> groups) {
		this.name = name;
		if (skillsMap != null)
			this.skillsMap = skillsMap;
		else
			this.skillsMap = new HashMap<Integer, Integer>();
		
		if (groups != null)
			this.groups = groups;
		else
			groups = new ArrayList<Integer>();
		
	}
	
	private Player(Parcel in) {
		id = in.readLong();
        name = in.readString();
        skillsMap = in.readHashMap(HashMap.class.getClassLoader());
        groups = in.readArrayList(ArrayList.class.getClassLoader());
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
	
	public void removeSkill(Integer activityId) {
		if (skillsMap.containsKey(activityId))
			skillsMap.remove(activityId);
	}
	
	public Map<Integer, Integer> getSkillsMap() {
		return skillsMap;
	}

	public void addGroup(Integer groupId) {
		if (!groups.contains(groupId))
			groups.add(groupId);
	}
	
	public void removeGroup(Integer groupId) {
		if (groups.contains(groupId))
			groups.remove(groups.indexOf(groupId));
	}
	
	public List<Integer> getGroups() {
		return groups;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		out.writeString(name);
		out.writeMap(skillsMap);
		out.writeList(groups);
	}

	public static final Player.Creator<Player> CREATOR = new Player.Creator<Player>() {
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}
