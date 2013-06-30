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
	
	public boolean addPlayerToGroup(Integer playerId, Integer groupId) {
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.playerIndex, playerId);
		values.put(DatabaseOpenHelper.groupIndex, groupId);
		
		return database.insert(DatabaseOpenHelper.Tables.INGROUPS.toString(), null, values) > 0;
	}
	
	public boolean addSkillLevel(Integer playerId, Integer activityId, Integer skill) {
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.playerIndex, playerId);
		values.put(DatabaseOpenHelper.activityIndex, activityId);
		values.put(DatabaseOpenHelper.skillLevel, skill);
		
		return database.insert(DatabaseOpenHelper.Tables.SKILLS.toString(), null, values) > 0;
	}
	
	public boolean removePlayerFromGroup(Integer playerId, Integer groupId) {
		String whereClause = DatabaseOpenHelper.playerIndex + " = " + playerId + " AND " + DatabaseOpenHelper.groupIndex + " = " + groupId;
		return database.delete(DatabaseOpenHelper.Tables.INGROUPS.toString(), whereClause, null) > 0;
	}
	
	public boolean removeSkillLevel(Integer playerId, Integer activityId) {
		String whereClause = DatabaseOpenHelper.playerIndex + " = " + playerId + " AND " + DatabaseOpenHelper.activityIndex + " = " + activityId;
		return database.delete(DatabaseOpenHelper.Tables.SKILLS.toString(), whereClause, null) > 0;
	}
	
	public boolean deletePlayer(Integer playerId) {
		String whereClause = DatabaseOpenHelper.playerIndex + " = " + playerId;
		
		database.delete(DatabaseOpenHelper.Tables.INGROUPS.toString(), whereClause, null);
		database.delete(DatabaseOpenHelper.Tables.SKILLS.toString(), whereClause, null);
		return database.delete(DatabaseOpenHelper.Tables.PLAYERS.toString(), whereClause, null) > 0;
	}
	
	public boolean deleteGroup(Integer groupId) {
		String whereClause = DatabaseOpenHelper.groupIndex + " = " + groupId;
		
		database.delete(DatabaseOpenHelper.Tables.INGROUPS.toString(), whereClause, null);
		return database.delete(DatabaseOpenHelper.Tables.GROUPS.toString(), whereClause, null) > 0;
	}
	
	public boolean deleteActivity(Integer activityId) {
		String whereClause = DatabaseOpenHelper.groupIndex + " = " + activityId;
		
		database.delete(DatabaseOpenHelper.Tables.SKILLS.toString(), whereClause, null);
		return database.delete(DatabaseOpenHelper.Tables.ACTIVITIES.toString(), whereClause, null) > 0;
	}
	
	public ArrayList<Player> getPlayerList(Integer groupId) {
		String playerTable = DatabaseOpenHelper.Tables.PLAYERS.toString();
		String skillsTable = DatabaseOpenHelper.Tables.SKILLS.toString();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(playerTable + ", " + skillsTable);
		queryBuilder.appendWhere(playerTable + "." + DatabaseOpenHelper.primaryIndex + "=" + skillsTable + "." + DatabaseOpenHelper.playerIndex);
		
		Cursor c = database.rawQuery("SELECT * FROM PLAYERS", null);
//		Cursor c = queryBuilder.query(database, null, null, null, null, null, null);
		
		return createPlayerList(c);
	}
	
	private ArrayList<Player> createPlayerList(Cursor c) {
		ArrayList<Player> playerList = new ArrayList<Player>();
		c.moveToFirst();
		while(!c.isAfterLast()) {
			String name = c.getString(1);
			Player player = new Player(name);
			
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
		Cursor c = database.rawQuery("SELECT * FROM SKILLS INNER JOIN ACTIVITIES WHERE SKILLS.playerIndex=" + playerId + ";", null);
		c.moveToFirst();
		while(!c.isAfterLast()) {
			DatabaseObject dbo = new DatabaseObject(c.getInt(0));
			dbo.setValue("activityId", c.getInt(1));
			dbo.setValue("playerId", c.getInt(2));
			dbo.setValue("skill", c.getInt(3));
			dbo.setValue("name", c.getString(5));
			activities.add(dbo);
		}
		
		return activities;
	}
	
	public DatabaseObjectCollection getGroupsForPlayer(long playerId) {
		DatabaseObjectCollection groups = new DatabaseObjectCollection();
		Cursor c = database.rawQuery("SELECT * FROM INGROUPS INNER JOIN GROUPS WHERE INGROUPS.playerIndex=" + playerId + ";", null);
		c.moveToFirst();
		while(!c.isAfterLast()) {
			DatabaseObject dbo = new DatabaseObject(c.getInt(0));
			dbo.setValue("groupId", c.getInt(1));
			dbo.setValue("playerId", c.getInt(2));
			dbo.setValue("name", c.getString(4));
			groups.add(dbo);
		}
		
		return groups;
	}
	
	private boolean isSkillMappedForPlayer(long playerId, Integer activityId) {
		Cursor c = database.rawQuery("SELECT * FROM SKILLS WHERE playerIndex=" + playerId + " AND activityIndex=" + activityId + ";", null);
		if (c.getCount() > 0) {
			return true;
		}
		
		return false;
	}
	
	private boolean isGroupMappedForPlayer(long playerId, Integer groupId) {
		Cursor c = database.rawQuery("SELECT * FROM INGROUPS WHERE playerIndex=" + playerId + " AND groupIndex=" + groupId + ";", null);
		if (c.getCount() > 0) {
			return true;
		}
		
		return false;
	}
}
