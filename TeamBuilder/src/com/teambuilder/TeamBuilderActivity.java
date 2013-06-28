package com.teambuilder;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class TeamBuilderActivity extends Activity {

	protected void populateSpinnerWithDatabaseObjects(Spinner spinner, List<Integer> idList, Map<Integer, DatabaseObject> nameMap) {
		
		ArrayAdapter<DatabaseObject> adapter = new ArrayAdapter<DatabaseObject>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		for (Integer i : idList)
			adapter.add(nameMap.get(i));
		
		spinner.setAdapter(adapter);
	}
	
	protected void populateSpinnerWithStrings(Spinner spinner, List<Integer> idList, Map<Integer, String> nameMap) {
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		for (Integer i : idList)
			adapter.add(nameMap.get(i));
		
		spinner.setAdapter(adapter);
	}
}
