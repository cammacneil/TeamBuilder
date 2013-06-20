package com.teambuilder.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	public static final String databaseName = "TeamBuilderDB";
	public static final int databaseVersion = 1;
	
	public enum Tables {PLAYERS, GROUPS, INGROUPS, ACTIVITIES, SKILLS};
	
	public static final String primaryIndex = "_id";
	public static final String name = "name";
	public static final String groupName = "groupName";
	public static final String groupIndex = "groupIndex";
	public static final String playerIndex = "playerIndex";
	public static final String activityName = "activityName";
	public static final String activityIndex = "activityIndex";
	public static final String skillLevel = "skillLevel";
	
	private static final String createPlayersTable = "CREATE TABLE PLAYERS (" + primaryIndex + " integer primary key " +
			"autoincrement, " + name + " text not null);";
	private static final String createGroupsTable = "CREATE TABLE GROUPS (" + primaryIndex + " integer primary key " +
			"autoincrement, " + groupName + " text not null);";
	private static final String createInGroupsTable = "CREATE TABLE INGROUPS (" + primaryIndex + " integer primary key " +
			"autoincrement, " + groupIndex + " integer not null, " + playerIndex + " integer not null);";
	private static final String createActivitiesTable = "CREATE TABLE ACTIVITIES (" + primaryIndex + " integer primary key " +
			"autoincrement, " + activityName + " text not null);";
	private static final String createSkillsTable = "CREATE TABLE SKILLS (" + primaryIndex + " integer primary key " +
			"autoincrement, " + activityIndex + " integer not null, " + playerIndex + " integer not null, " +
					skillLevel + " integer not null);";
	
	public DatabaseOpenHelper(Context context) {
		super(context, databaseName, null, databaseVersion);
		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(createPlayersTable);
		db.execSQL(createGroupsTable);
		db.execSQL(createInGroupsTable);
		db.execSQL(createActivitiesTable);
		db.execSQL(createSkillsTable);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
