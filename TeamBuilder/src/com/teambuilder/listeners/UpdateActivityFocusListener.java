package com.teambuilder.listeners;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.teambuilder.DatabaseObject;
import com.teambuilder.Player;

public class UpdateActivityFocusListener implements OnFocusChangeListener {

	DatabaseObject activity;
	
	public UpdateActivityFocusListener(DatabaseObject activity) {
		this.activity = activity;
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus == false) {
			String textValue = ((EditText)v).getText().toString();
			
			Integer skill = Integer.parseInt(textValue);
			if (skill < 0 || skill > 100) {
				//Add alert dialog for invalid entry
				((EditText)v).setText((String)activity.getValue("skill"));
			} else {
				if (activity.getId() != -1) {
					activity.setStatus(DatabaseObject.UPDATE_DATABASE);
				}
				activity.setValue("skill", skill);
			}
		}
	}

}
