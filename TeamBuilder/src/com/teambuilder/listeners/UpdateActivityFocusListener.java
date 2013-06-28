package com.teambuilder.listeners;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.teambuilder.DatabaseObject;
import com.teambuilder.Player;

public class UpdateActivityFocusListener implements OnFocusChangeListener {

	Player player;
	DatabaseObject activity;
	
	public UpdateActivityFocusListener(Player player, DatabaseObject activity) {
		this.player = player;
		this.activity = activity;
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus == false) {
			String textValue = ((EditText)v).getText().toString();
			player.setSkill(activity.getId(), Integer.parseInt(textValue));
			activity.setStatus(DatabaseObject.UPDATE_VALUE_FOR_PLAYER);
		}
	}

}
