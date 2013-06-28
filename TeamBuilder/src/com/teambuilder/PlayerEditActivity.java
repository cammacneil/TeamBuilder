package com.teambuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.teambuilder.listeners.RemoveActivityListener;
import com.teambuilder.listeners.UpdateActivityDoneListener;
import com.teambuilder.listeners.UpdateActivityFocusListener;
import com.teambuilder.utilities.DatabaseHandler;
import com.teambuilder.utilities.Utilities;

public class PlayerEditActivity extends TeamBuilderActivity {

	private Player player;
	DatabaseHandler db;
	ArrayList<Integer> availableActivityList;
	Map<Integer, DatabaseObject> activities;
	Map<Integer, DatabaseObject> groups;
	Map<Integer, DatabaseObject> oldGroups;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		
		//Display activity information
		availableActivityList = new ArrayList<Integer>();
		availableActivityList.addAll(activities.keySet());
		
		Map<Integer, Integer> playerSkills = player.getSkillsMap();
		for (Integer key : playerSkills.keySet()) {
			if (availableActivityList.contains(key))
				availableActivityList.remove(Integer.valueOf(key));
		}
		
		populateActivitySpinner();
		
		//Display group information
		List<Integer> currentGroupList = player.getGroups();
		if (currentGroupList != null) {
			for (Integer i : currentGroupList) {
				addGroupLayout(groups.get(i));
			}
		}
	}
	
	public void addSkill(View view) {
		Spinner spinner = (Spinner)findViewById(R.id.spinner_skills);
		DatabaseObject activity = (DatabaseObject)spinner.getSelectedItem();
		
		EditText skillText = (EditText)findViewById(R.id.text_skills);
		
		Integer number;
		try {
			number = Integer.parseInt(skillText.getText().toString());
			if (number < 0 || number > 100)
				return;
		} catch (NumberFormatException e) {
			return;
		}
		
		Integer key = Utilities.getKeyByValue(activities, activity);
		availableActivityList.remove(Integer.valueOf(key));
		
		populateActivitySpinner();
		
		player.setSkill(key, number);
		
		activity.setStatus(DatabaseObject.ADD_VALUE_FOR_PLAYER);
		
		addSkillLayout(activity);
		
		if (availableActivityList.size() == 0) {
			ViewGroup skillsLayout = (ViewGroup)findViewById(R.id.layout_add_skills);
			skillsLayout.setVisibility(View.GONE);
		}
	}
	
	private void addSkillLayout(DatabaseObject activity) {
		ViewGroup parent = (ViewGroup)findViewById(R.id.layout_skills);
		
		TextView nameView = new TextView(this);
		nameView.setText(activity.getName());
		
		EditText skillView = new EditText(this);
		skillView.setText(String.valueOf(player.getSkill(activity.getId())));
		skillView.setOnEditorActionListener(new UpdateActivityDoneListener(player, activity));
		skillView.setOnFocusChangeListener(new UpdateActivityFocusListener(player, activity));
		skillView.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL);
		
		Button removeButton = new Button(this);
		removeButton.setText("X");
		removeButton.setOnClickListener(new RemoveActivityListener(this, activity));
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.addView(nameView);
		layout.addView(skillView);
		layout.addView(removeButton);
		
		parent.addView(layout);
	}
	
	private void addGroupLayout(DatabaseObject group) {
		ViewGroup parent = (ViewGroup)findViewById(R.id.layout_list_groups);
		
		TextView nameView = new TextView(this);
		nameView.setText(group.getName());
		
		parent.addView(nameView);
	}
	
	public void save(View view) {
		if (!checkFields()) {
			return;
		}
		
		String name = ((EditText)findViewById(R.id.text_name)).getText().toString();
		
		db.open();
		
		if (player.getId() == 0) {
			player.setName(name);
			player = db.createPlayer(player);
		}
		
		db.updatePlayer(player);
		
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
	
	public void removeSkillForPlayer(DatabaseObject activity) {
		
		player.removeSkill(activity.getId());
		activity.setStatus(DatabaseObject.REMOVE_VALUE_FOR_PLAYER);

		if (availableActivityList.size() == 0) {
			ViewGroup skillsLayout = (ViewGroup)findViewById(R.id.layout_add_skills);
			skillsLayout.setVisibility(View.VISIBLE);
		}
		
		availableActivityList.add(activity.getId());
		
		populateActivitySpinner();
		
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
	
	private void populateActivitySpinner() {
		
		Spinner activitySpinner = (Spinner)(findViewById(R.id.spinner_skills));
		populateSpinnerWithDatabaseObjects(activitySpinner, availableActivityList, activities);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void manageGroups(View v) {
		oldGroups = groups;
		
		PlayerEditGroupsDialog dialog = PlayerEditGroupsDialog.newInstance();
		dialog.show(getFragmentManager(), "GroupDialog");
	}
	
	public void saveManagedGroups() {
		oldGroups = groups;
	}
	
	public void cancelManagedGroups() {
		groups = oldGroups;
	}
}
