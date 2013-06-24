package com.teambuilder;

import java.util.ArrayList;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.teambuilder.utilities.DatabaseHandler;
import com.teambuilder.utilities.Utilities;

public class PlayerEditActivity extends Activity {

	private Player player;
	DatabaseHandler db;
	ArrayList<Integer> availableActivityList;
	Map<Integer, String> activities;
	Map<Integer, String> groups;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playeredit);
	
		Intent intent = getIntent();
		player = intent.getParcelableExtra(Player.label);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		//New Player
		if (player == null) {
			actionBar.setTitle(R.string.player_add_title);
			player = new Player();
		} else { //Existing player
			actionBar.setTitle(player.getName());
		}
		
		db = new DatabaseHandler(this);
		populateFields();
		
	}
	
	private void populateFields() {
		db.open();
		activities = db.getActivityList();
		groups = db.getGroupList();
		db.close();
		
		availableActivityList = new ArrayList<Integer>();
		availableActivityList.addAll(activities.keySet());
		
		Spinner activitySpinner = (Spinner)(findViewById(R.id.spinner_skills));
		
		Map<Integer, Integer> playerSkills = player.getSkillsMap();
		for (Integer key : playerSkills.keySet()) {
			if (availableActivityList.contains(key))
				availableActivityList.remove(Integer.valueOf(key));
			
		}
		
		populateSpinner(activitySpinner, availableActivityList, activities);
	}
	
	private void createSkillLayout(Integer key, Integer skill) {
		LinearLayout layout = new LinearLayout(this);
		TextView name = new TextView(this);
		name.setText(activities.get(key));
		TextView skillView = new TextView(this);
		skillView.setText(skill);
		Button deleteButton = new Button(this);
		deleteButton.setText("X");
		
		layout.addView(name);
		layout.addView(skillView);
		layout.addView(deleteButton);
		
		LinearLayout parent = (LinearLayout)findViewById(R.id.layout_skills);
		parent.addView(layout);
	}
	
	private void populateSpinner(Spinner spinner, ArrayList<Integer> idList, Map<Integer, String> nameMap) {
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		for (Integer i : idList)
			adapter.add(nameMap.get(i));
		
		spinner.setAdapter(adapter);
	}
	
	public void addSkill(View view) {
		Spinner spinner = (Spinner)findViewById(R.id.spinner_skills);
		String value = (String)spinner.getSelectedItem();
		
		
		Integer key = Utilities.getKeyByValue(activities, value);
		availableActivityList.remove(Integer.valueOf(key));
		
		
	}
	
	public void save(View view) {
		if (!checkFields()) {
			return;
		}
		
		String name = ((EditText)findViewById(R.id.text_name)).getText().toString();
		
		if (player == null) {
			player = new Player(name);
		}
		
		db.open();
		player = db.createPlayer(player);
		db.close();
		
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	public boolean checkFields() {
		boolean fieldsOK = true;
		
		//Check that the name field isn't empty
		if (((EditText)findViewById(R.id.text_name)).getText().toString() == "")
			fieldsOK = false;
		return fieldsOK;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
    }

	}
}
