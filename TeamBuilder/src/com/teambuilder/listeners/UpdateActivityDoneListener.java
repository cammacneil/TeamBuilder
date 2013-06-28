package com.teambuilder.listeners;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.teambuilder.DatabaseObject;
import com.teambuilder.Player;

public class UpdateActivityDoneListener implements OnEditorActionListener {

	private Player player;
	private DatabaseObject activity;
	
	public UpdateActivityDoneListener(Player player, DatabaseObject activity) {
		this.player = player;
		this.activity = activity;
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		boolean handled = false;
		if (actionId == EditorInfo.IME_ACTION_DONE){
			activity.setStatus(DatabaseObject.UPDATE_VALUE_FOR_PLAYER);
			player.setSkill(activity.getId(), Integer.parseInt(v.getText().toString()));
		}
		
		return handled;
	}

}
