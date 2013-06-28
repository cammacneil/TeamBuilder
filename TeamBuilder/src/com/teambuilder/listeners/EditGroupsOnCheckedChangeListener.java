package com.teambuilder.listeners;

import java.util.List;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.teambuilder.DatabaseObject;

public class EditGroupsOnCheckedChangeListener implements
		OnCheckedChangeListener {

	private DatabaseObject group;
	private List<Integer> currentGroups;
	
	public EditGroupsOnCheckedChangeListener(DatabaseObject group, List<Integer> currentGroups) {
		this.group = group;
		this.currentGroups = currentGroups;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			if (currentGroups.contains(group.getId())) {
				group.setStatus(DatabaseObject.NORMAL);
			} else {
				group.setStatus(DatabaseObject.ADD_VALUE_FOR_PLAYER);
			}
		} else {
			if (currentGroups.contains(group.getId())) {
				group.setStatus(DatabaseObject.REMOVE_VALUE_FOR_PLAYER);
			} else {
				group.setStatus(DatabaseObject.NORMAL);
			}
		}
	}

}
