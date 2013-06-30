package com.teambuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
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

public class PlayerEditActivity extends TeamBuilderActivity {

	private Player player;
	DatabaseHandler db;
	ArrayList<Integer> availableActivityList;
	DatabaseObjectCollection activities;
	DatabaseObjectCollection groups;
	DatabaseObjectCollection playerActivities;
	DatabaseObjectCollection playerGroups;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playeredit);
	
		Intent intent = getIntent();
		player = intent.getParcelableExtra(Player.label);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		db = new DatabaseHandler(this);
		
		//New Player
		if (player == null) {
			actionBar.setTitle(R.string.player_add_title);
			player = new Player();
			playerActivities = new DatabaseObjectCollection();
			playerGroups = new DatabaseObjectCollection();
		} else { //Existing player
			actionBar.setTitle(player.getName());
			playerActivities = db.getActivitiesForPlayer(player.getId());
			playerGroups = db.getGroupsForPlayer(player.getId());
		}
		
		populateFields();
		
	}
	
	private void populateFields() {
		db.open();
		activities = db.getActivityList();
		groups = db.getGroupList();
		db.close();
		
		//Display activity information
		availableActivityList = new ArrayList<Integer>();
		availableActivityList.addAll(activities.getIdsForObjects());
		
		Map<Integer, Integer> playerSkills = player.getSkills();
		for (Integer key : playerSkills.keySet()) {
			if (availableActivityList.contains(key))
				availableActivityList.remove(Integer.valueOf(key));
		}
		
		populateActivitySpinner();
		
		//Display group information
		List<Integer> currentGroupList = player.getGroups();
		if (currentGroupList != null) {
			for (Integer i : currentGroupList) {
				addGroupLayout(groups.getObjectWithId(i));
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
				//Add in alert dialog for invalid number
				return;
		} catch (NumberFormatException e) {
			//Add in alert dialog for invalid entry
			return;
		}
		
		Integer id = (Integer)activity.getId();
		availableActivityList.remove(Integer.valueOf(id));
		
		populateActivitySpinner();
		
		DatabaseObject dbo = playerActivities.getObjectWithProperty("activityId", id);
		
		if (dbo == null) { //Brand new skill level
			dbo = new DatabaseObject();
			dbo.setValue("activityId", id);
			dbo.setValue("playerId", player.getId());
			dbo.setValue("skill", number);
			dbo.setValue("name", activity.getValue("name"));
			dbo.setStatus(DatabaseObject.ADD_TO_DATABASE);
			
			playerActivities.add(dbo);
		} else { //Skill level existed but was removed and added back
			dbo.setValue("skill", number);
			dbo.setStatus(DatabaseObject.UPDATE_DATABASE);
		}
		
		addSkillLayout(dbo);
		
		if (availableActivityList.size() == 0) {
			ViewGroup skillsLayout = (ViewGroup)findViewById(R.id.layout_add_skills);
			skillsLayout.setVisibility(View.GONE);
		}
	}
	
	private void addSkillLayout(DatabaseObject dbo) {
		ViewGroup parent = (ViewGroup)findViewById(R.id.layout_skills);
		
		TextView nameView = new TextView(this);
		nameView.setText((String)dbo.getValue("name"));
		
		EditText skillView = new EditText(this);
		skillView.setText(String.valueOf(dbo.getValue("skill")));
		skillView.setOnEditorActionListener(new UpdateActivityDoneListener(dbo));
		skillView.setOnFocusChangeListener(new UpdateActivityFocusListener(dbo));
		skillView.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL);
		
		Button removeButton = new Button(this);
		removeButton.setText("X");
		removeButton.setOnClickListener(new RemoveActivityListener(this, dbo));
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.addView(nameView);
		layout.addView(skillView);
		layout.addView(removeButton);
		
		parent.addView(layout);
	}
	
	private void addGroupLayout(DatabaseObject dbo) {
		ViewGroup parent = (ViewGroup)findViewById(R.id.layout_list_groups);
		
		TextView nameView = new TextView(this);
		nameView.setText((String)dbo.getValue("name"));
		nameView.setTag(dbo);
		
		parent.addView(nameView);
	}
	
	private void removeGroupLayout(DatabaseObject dbo) {
		ViewGroup parent = (ViewGroup)findViewById(R.id.layout_list_groups);
		
		for (int i=0;i<parent.getChildCount();i++) {
			View v = parent.getChildAt(i);
			if (v.getTag() == dbo) {
				v.setVisibility(View.GONE);
				v.setTag(null);
				break;
			}
		}
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
	
	public void removeSkillForPlayer(DatabaseObject dbo) {
		
		if (dbo.getId() == -1) {
			playerActivities.remove(dbo);
		} else {
			dbo.setStatus(DatabaseObject.REMOVE_FROM_DATABASE);
		}

		if (availableActivityList.size() == 0) {
			ViewGroup skillsLayout = (ViewGroup)findViewById(R.id.layout_add_skills);
			skillsLayout.setVisibility(View.VISIBLE);
		}
		
		availableActivityList.add((Integer)dbo.getValue("activityId"));
		
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
		populateSpinnerWithDatabaseObjects(activitySpinner, activities, availableActivityList);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void manageGroups(View v) {
		PlayerEditGroupsDialog dialog = new PlayerEditGroupsDialog();
		dialog.groups = getGroupStringList();
		dialog.preSelectedItems = getPreSelectedList();
		dialog.show(getFragmentManager(), "GroupDialog");
	}
	
	private String[] getGroupStringList() {
		String[] result = new String[groups.getAll().size()];
		
		for (int i=0;i<result.length;i++) {
			DatabaseObject dbo = groups.get(i);
			result[i] = dbo.getName();
		}
		
		return result;
	}
	
	private boolean[] getPreSelectedList() {
		boolean[] result = new boolean[groups.getAll().size()];
		
		for (int i=0;i<result.length;i++) {
			DatabaseObject dbo = groups.get(i);
			if (playerGroups.getObjectWithProperty("groupId", dbo.getId()) != null) {
				result[i] = true;
			} else {
				result[i] = false;
			}
		}
		
		return result;
	}
	
	public void saveManagedGroups(List<String> selectedItems) {
		Iterator<DatabaseObject> iter = playerGroups.getAll().iterator();
		while (iter.hasNext()) {
			DatabaseObject dbo = iter.next();
			if (selectedItems.contains(dbo.getName()) == false) {
				if (dbo.getId() == -1) {
					iter.remove();
				} else {
					dbo.setStatus(DatabaseObject.REMOVE_FROM_DATABASE);
				}
				removeGroupLayout(dbo);
			} else {
				selectedItems.remove(dbo.getName());
			}
		}
		
		for (String name : selectedItems) {
			DatabaseObject dbo = new DatabaseObject();
			dbo.setValue("name", name);
			dbo.setValue("playerId", player.getId());
			dbo.setValue("groupId", groups.getObjectWithProperty("name", name).getId());
			dbo.setStatus(DatabaseObject.ADD_TO_DATABASE);
			playerGroups.add(dbo);
			
			addGroupLayout(dbo);
		}
	}
	
	public void cancelManagedGroups() {

	}
}
