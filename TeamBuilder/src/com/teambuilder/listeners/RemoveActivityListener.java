package com.teambuilder.listeners;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.teambuilder.DatabaseObject;
import com.teambuilder.PlayerEditActivity;

public class RemoveActivityListener implements OnClickListener {

	PlayerEditActivity parentActivity;
	DatabaseObject activity;
	
	public RemoveActivityListener(PlayerEditActivity parentActivity, DatabaseObject activity) {
		this.activity = activity;
		this.parentActivity = parentActivity;
	}
	
	@Override
	public void onClick(View v) {
		ViewGroup layout = (ViewGroup)v.getParent();
		ViewGroup parent = (ViewGroup)layout.getParent();
		
		parent.removeView(layout);
		
		parentActivity.removeSkillForPlayer(activity);
		
	}

}
