package com.teambuilder;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;

import com.teambuilder.adapters.PlayerListAdapter;
import com.teambuilder.utilities.DatabaseHandler;

public class PlayersFragment extends TeamBuilderFragment {

	private Menu menu;
	private DatabaseHandler db;
	private DatabaseObject currentActivity;
	private DatabaseObject currentGroup;

	private SharedPreferences preferences;
	
	PlayerListAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		db = new DatabaseHandler(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_players, container, false);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		db.open();
		
		DatabaseObjectCollection activityList = db.getActivityList();
		DatabaseObjectCollection groupList = db.getGroupList();
		
		DatabaseObject dbo = new DatabaseObject(-1);
		dbo.setValue("groupId", -1);
		dbo.setValue("name", "(All)");
		groupList.add(dbo);
		
		preferences = getActivity().getSharedPreferences("TeamBuilder", Activity.MODE_PRIVATE);
		
		int currentActivityId = -1;
		int currentGroupId = -1;
		
		if (preferences != null) {
			currentActivityId = preferences.getInt("currentActivity", -1);
			if (currentActivityId == -1) {
				currentActivity = activityList.get(0);
			} else {
				currentActivity = activityList.getObjectWithId(currentActivityId);
			}
			currentGroupId = preferences.getInt("currentGroup", -1);
			currentGroup = groupList.getObjectWithId(currentGroupId);
		}
		
		populateActivitySpinner(activityList, currentActivityId);
		populateGroupSpinner(groupList, currentGroupId);
		
		List<Player> playerList = db.getPlayerList(currentGroup.getId());
		createPlayerListArrayAdapter(playerList);
		populatePlayersList();
		
		db.close();
	}

	private void createPlayerListArrayAdapter(List<Player> playerList) {
		mAdapter = new PlayerListAdapter(this.getActivity(),
				android.R.layout.simple_list_item_1);
		mAdapter.setActivityId(currentActivity.getId());
		mAdapter.addAll(playerList);
	}

	private void populatePlayersList() {
		ListView view = (ListView) getActivity().findViewById(R.id.list_players);
		view.setAdapter(mAdapter);
	}

	private void populateActivitySpinner(DatabaseObjectCollection activityList, Integer selectionId) {
		Spinner activitySpinner = (Spinner) getActivity().findViewById(R.id.spinner_activity);
		getTeamBuilderActivity().populateSpinnerWithDatabaseObjects(activitySpinner, activityList, null);
		
		if (selectionId != null) {
			DatabaseObject dbo = activityList.getObjectWithId(selectionId);
			if (dbo != null) {
				int position = activityList.getAll().indexOf(dbo);
				activitySpinner.setSelection(position);
			}
		} else {
			if (activityList.getAll().size() > 0) {
				activitySpinner.setSelection(0);
				currentActivity = (DatabaseObject)activitySpinner.getSelectedItem();
			}
		}
		
		activitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				DatabaseObject dbo = (DatabaseObject)parent.getItemAtPosition(position);
				updateCurrentActivity(dbo);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
	}
	
	private void populateGroupSpinner(DatabaseObjectCollection groupList, Integer selectionId) {
		Spinner groupSpinner = (Spinner) getActivity().findViewById(R.id.spinner_group);
		getTeamBuilderActivity().populateSpinnerWithDatabaseObjects(groupSpinner, groupList, null);
		
		if (selectionId != null) {
			DatabaseObject dbo = groupList.getObjectWithId(selectionId);
			if (dbo != null) {
				int position = groupList.getAll().indexOf(dbo);
				groupSpinner.setSelection(position);
			}
		}
		
		groupSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				DatabaseObject dbo = (DatabaseObject)parent.getItemAtPosition(position);
				updateCurrentGroup(dbo);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
	}

	public void updateCurrentActivity(DatabaseObject dbo) {
		currentActivity = dbo;
		mAdapter.setActivityId(dbo.getId());
		mAdapter.notifyDataSetChanged();
	}
	
	public void updateCurrentGroup(DatabaseObject dbo) {
		currentGroup = dbo;
		
		db.open();
		
		List<Player> playerList = db.getPlayerList(dbo.getId());
		createPlayerListArrayAdapter(playerList);
		populatePlayersList();
		
		db.close();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (menu.findItem(R.menu.menu_player_fragment) == null) {
			inflater.inflate(R.menu.menu_player_fragment, menu);
		}
		
		this.menu = menu;
		super.onCreateOptionsMenu(menu, inflater);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_addPlayer) {
			Intent intent = new Intent(getActivity(), PlayerEditActivity.class);
			
			getActivity().startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroyOptionsMenu() {
		menu.removeItem(R.menu.menu_player_fragment);
		super.onDestroyOptionsMenu();
	}

	@Override
	public void onPause() {
		super.onPause();
		
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("currentGroup", currentGroup.getId());
		editor.putInt("currentActivity", currentActivity.getId());
		editor.commit();
	}
	
	
}
