package com.teambuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class TeamBuilderActivity extends Activity {

	private ArrayList<Player> players;
	
	/**
	 * @param spinner The spinner which will be populated.
	 * @param dbos The DatabaseObjectCollection used to supply DBOs to the ArrayAdapter.
	 * @param ids An optional list of ids. If specified, only the DatabaseObjects with ids in this list are added to the spinner. If null, all DBOs in the collection are added.
	 */
	protected void populateSpinnerWithDatabaseObjects(Spinner spinner, DatabaseObjectCollection dbos, List<Integer> ids) {
		
		ArrayAdapter<DatabaseObject> adapter = new ArrayAdapter<DatabaseObject>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		if (ids == null) {
			adapter.addAll(dbos.getAll());
		} else {
			for (int id : ids) {
				DatabaseObject dbo = dbos.getObjectWithId(id);
				if (dbo != null) {
					adapter.add(dbo);
				}
			}
		}
		
		spinner.setAdapter(adapter);
	}
	
	protected void populateSpinnerWithStrings(Spinner spinner, List<Integer> idList, Map<Integer, String> nameMap) {
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		for (Integer i : idList)
			adapter.add(nameMap.get(i));
		
		spinner.setAdapter(adapter);
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
}
