package com.teambuilder.listeners;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.teambuilder.DatabaseObject;
import com.teambuilder.Player;

public class UpdateActivityDoneListener implements OnEditorActionListener {

	private DatabaseObject activity;
	
	public UpdateActivityDoneListener(DatabaseObject activity) {
		this.activity = activity;
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		boolean handled = false;
		if (actionId == EditorInfo.IME_ACTION_DONE){
			Integer skill = Integer.parseInt(v.getText().toString());
			if (skill < 0 || skill > 100) {
				//Add alert dialog for invalid entry
				v.setText((String)activity.getValue("skill"));
			} else {
				if (activity.getId() != -1) {
					activity.setStatus(DatabaseObject.UPDATE_DATABASE);
				}
				activity.setValue("skill", skill);
			}
		}
		
		return handled;
	}

}
