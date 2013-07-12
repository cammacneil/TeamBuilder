package com.teambuilder.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	public static final String databaseName = "TeamBuilderDB";
	public static final int databaseVersion = 1;
	
	public enum Tables {PLAYERS, GROUPS, INGROUPS, ACTIVITIES, SKILLS};
	
	public static final String primaryId = "_id";
	public static final String name = "name";
	public static final String groupName = "groupName";
	public static final String groupId = "groupId";
	public static final String playerId = "playerId";
	public static final String activityName = "activityName";
	public static final String activityId = "activityId";
	public static final String skillLevel = "skillLevel";
	
	private static final String createPlayersTable = "CREATE TABLE PLAYERS (" + primaryId + " integer primary key " +
			"autoincrement, " + name + " text not null);";
	private static final String createGroupsTable = "CREATE TABLE GROUPS (" + primaryId + " integer primary key " +
			"autoincrement, " + groupName + " text not null);";
	private static final String createInGroupsTable = "CREATE TABLE INGROUPS (" + primaryId + " integer primary key " +
			"autoincrement, " + groupId + " integer not null, " + playerId + " integer not null);";
	private static final String createActivitiesTable = "CREATE TABLE ACTIVITIES (" + primaryId + " integer primary key " +
			"autoincrement, " + activityName + " text not null);";
	private static final String createSkillsTable = "CREATE TABLE SKILLS (" + primaryId + " integer primary key " +
			"autoincrement, " + activityId + " integer not null, " + playerId + " integer not null, " +
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

		//Create sample groups and activities for testing
		db.execSQL("INSERT INTO GROUPS (groupName) VALUES " +
				"(\"HockeyGroup\")," +
				"(\"BaseballGroup\")," +
				"(\"FootballGroup\")," +
				"(\"SoccerGroup\");");
		
		db.execSQL("INSERT INTO ACTIVITIES (activityName) VALUES " +
				"(\"Hockey\")," +
				"(\"Baseball\")," +
				"(\"Football\")," +
				"(\"Soccer\");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
