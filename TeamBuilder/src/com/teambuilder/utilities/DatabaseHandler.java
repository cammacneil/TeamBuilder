package com.teambuilder.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.teambuilder.DatabaseObject;
import com.teambuilder.DatabaseObjectCollection;
import com.teambuilder.Player;

public class DatabaseHandler {

	private SQLiteDatabase database;
	private DatabaseOpenHelper dbHelper;
	
	public DatabaseHandler(Context context) {
		dbHelper = new DatabaseOpenHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Player createPlayer(Player player) {
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.name, player.getName());
		
		long id = database.insert(DatabaseOpenHelper.Tables.PLAYERS.toString(), null, values);
		player.setId(id);
		
		updatePlayer(player);
		
		return player;
	}
	
	public void updatePlayer(Player player) {
		
		
		ContentValues values = new ContentValues();
		
	}
	
	public long createGroup(String name) {
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.groupName, name);
		
		return database.insert(DatabaseOpenHelper.Tables.GROUPS.toString(), null, values);
	}
	
	public long createActivity(String name) {
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.activityName, name);
		
		return database.insert(DatabaseOpenHelper.Tables.ACTIVITIES.toString(), null, values);
	}
	
	public boolean addPlayerToGroup(long playerId, Integer groupId) {
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.playerId, playerId);
		values.put(DatabaseOpenHelper.groupId, groupId);
		
		return database.insert(DatabaseOpenHelper.Tables.INGROUPS.toString(), null, values) > 0;
	}
	
	public boolean addSkillLevel(long playerId, Integer activityId, Integer skill) {
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.playerId, playerId);
		values.put(DatabaseOpenHelper.activityId, activityId);
		values.put(DatabaseOpenHelper.skillLevel, skill);
		
		return database.insert(DatabaseOpenHelper.Tables.SKILLS.toString(), null, values) > 0;
	}
	
	public int updateSkillLevel(long playerId, Integer activityId, Integer skill) {
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.skillLevel, skill);
		String whereClause = DatabaseOpenHelper.playerId + " = " + playerId + " AND " + DatabaseOpenHelper.activityId + " = " + activityId;
		return database.update(DatabaseOpenHelper.Tables.SKILLS.toString(), values, whereClause, null);
	}
	
	public boolean removePlayerFromGroup(long playerId, Integer groupId) {
		String whereClause = DatabaseOpenHelper.playerId + " = " + playerId + " AND " + DatabaseOpenHelper.groupId + " = " + groupId;
		return database.delete(DatabaseOpenHelper.Tables.INGROUPS.toString(), whereClause, null) > 0;
	}
	
	public boolean removeSkillLevel(long playerId, Integer activityId) {
		String whereClause = DatabaseOpenHelper.playerId + " = " + playerId + " AND " + DatabaseOpenHelper.activityId + " = " + activityId;
		return database.delete(DatabaseOpenHelper.Tables.SKILLS.toString(), whereClause, null) > 0;
	}
	
	public boolean deletePlayer(long playerId) {
		String whereClause = DatabaseOpenHelper.playerId + " = " + playerId;
		
		database.delete(DatabaseOpenHelper.Tables.INGROUPS.toString(), whereClause, null);
		database.delete(DatabaseOpenHelper.Tables.SKILLS.toString(), whereClause, null);
		return database.delete(DatabaseOpenHelper.Tables.PLAYERS.toString(), whereClause, null) > 0;
	}
	
	public boolean deleteGroup(Integer groupId) {
		String whereClause = DatabaseOpenHelper.groupId + " = " + groupId;
		
		database.delete(DatabaseOpenHelper.Tables.INGROUPS.toString(), whereClause, null);
		return database.delete(DatabaseOpenHelper.Tables.GROUPS.toString(), whereClause, null) > 0;
	}
	
	public boolean deleteActivity(Integer activityId) {
		String whereClause = DatabaseOpenHelper.groupId + " = " + activityId;
		
		database.delete(DatabaseOpenHelper.Tables.SKILLS.toString(), whereClause, null);
		return database.delete(DatabaseOpenHelper.Tables.ACTIVITIES.toString(), whereClause, null) > 0;
	}
	
	public ArrayList<Player> getPlayerList(Integer groupId) {
		String sql = "SELECT * FROM PLAYERS";
				
		if (groupId > -1) {
			sql += " INNER JOIN INGROUPS ON PLAYERS._id=INGROUPS.playerId WHERE INGROUPS.groupId=" + groupId;
		}
		
		Cursor c = database.rawQuery(sql, null);
		
		return createPlayerList(c);
	}
	
	private ArrayList<Player> createPlayerList(Cursor c) {
		ArrayList<Player> playerList = new ArrayList<Player>();
		c.moveToFirst();
		while(!c.isAfterLast()) {
			String name = c.getString(1);
			Player player = new Player(name);
			player.setId(c.getLong(0));

			DatabaseObjectCollection dbos = getActivitiesForPlayer(c.getLong(0));
			for (DatabaseObject dbo : dbos.getAll()) {
				Integer activityId = (Integer)dbo.getValue("activityId");
				Integer skill = (Integer)dbo.getValue("skill");
				player.setSkill(activityId, skill);
			}
			
			playerList.add(player);
			c.moveToNext();
		}
		
		c.close();
		
		return playerList;
	}
	
	public DatabaseObjectCollection getActivityList() {
		DatabaseObjectCollection activities = new DatabaseObjectCollection();
		Cursor c = database.rawQuery("SELECT * FROM ACTIVITIES", null);
		c.moveToFirst();
		while(!c.isAfterLast()) {
			DatabaseObject dbo = new DatabaseObject(c.getInt(0));
			dbo.setValue("name", c.getString(1));
			activities.add(dbo);
			c.moveToNext();
		}
		
		return activities;
	}
	
	public DatabaseObjectCollection getGroupList() {
		DatabaseObjectCollection groups = new DatabaseObjectCollection();
		Cursor c = database.rawQuery("SELECT * FROM GROUPS", null);
		c.moveToFirst();
		while(!c.isAfterLast()) {
			DatabaseObject dbo = new DatabaseObject(c.getInt(0));
			dbo.setValue("name", c.getString(1));
			groups.add(dbo);
			c.moveToNext();
		}
		
		return groups;
	}
	
	public DatabaseObjectCollection getActivitiesForPlayer(long playerId) {
		DatabaseObjectCollection activities = new DatabaseObjectCollection();
		Cursor c = database.rawQuery("SELECT * FROM SKILLS INNER JOIN ACTIVITIES ON SKILLS.activityId=ACTIVITIES._id WHERE SKILLS.playerId=" + playerId + ";", null);
		c.moveToFirst();
		while(!c.isAfterLast()) {
			DatabaseObject dbo = new DatabaseObject(c.getInt(0));
			dbo.setValue("activityId", c.getInt(1));
			dbo.setValue("playerId", c.getInt(2));
			dbo.setValue("skill", c.getInt(3));
			dbo.setValue("name", c.getString(5));
			activities.add(dbo);
			c.moveToNext();
		}
		
		return activities;
	}
	
	public DatabaseObjectCollection getGroupsForPlayer(long playerId) {
		DatabaseObjectCollection groups = new DatabaseObjectCollection();
		Cursor c = database.rawQuery("SELECT * FROM INGROUPS INNER JOIN GROUPS ON INGROUPS.groupId=GROUPS._id WHERE INGROUPS.playerId=" + playerId + ";", null);
		c.moveToFirst();
		while(!c.isAfterLast()) {
			DatabaseObject dbo = new DatabaseObject(c.getInt(0));
			dbo.setValue("groupId", c.getInt(1));
			dbo.setValue("playerId", c.getInt(2));
			dbo.setValue("name", c.getString(4));
			groups.add(dbo);
			c.moveToNext();
		}
		
		return groups;
	}
	
	private boolean isSkillMappedForPlayer(long playerId, Integer activityId) {
		Cursor c = database.rawQuery("SELECT * FROM SKILLS WHERE playerId=" + playerId + " AND activityId=" + activityId + ";", null);
		if (c.getCount() > 0) {
			return true;
		}
		
		return false;
	}
	
	private boolean isGroupMappedForPlayer(long playerId, Integer groupId) {
		Cursor c = database.rawQuery("SELECT * FROM INGROUPS WHERE playerId=" + playerId + " AND groupId=" + groupId + ";", null);
		if (c.getCount() > 0) {
			return true;
		}
		
		return false;
	}
}
