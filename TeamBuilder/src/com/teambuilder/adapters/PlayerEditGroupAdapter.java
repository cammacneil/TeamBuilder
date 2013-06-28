package com.teambuilder.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.teambuilder.DatabaseObject;
import com.teambuilder.R;
import com.teambuilder.listeners.EditGroupsOnCheckedChangeListener;

public class PlayerEditGroupAdapter extends ArrayAdapter<DatabaseObject> {

	private final Context context;
	Map<Integer, DatabaseObject> groups;
	List<Integer> currentGroups;
	
	public PlayerEditGroupAdapter(Context context, int textViewResourceId, Map<Integer, DatabaseObject> groups, List<Integer> currentGroups) {
		super(context, textViewResourceId);
		this.groups = groups;
		this.context = context;
		this.addAll(groups.values());
		
		if (currentGroups == null) {
			this.currentGroups = new ArrayList<Integer>();
		} else {
			this.currentGroups = currentGroups;
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.layout_view_text_single_checkbox, parent, false);
		
		DatabaseObject item = (DatabaseObject)getItem(position);
		
		TextView textView = (TextView)rowView.findViewById(R.id.view_text);
		textView.setText(item.getName());
		
		CheckBox checkBox = (CheckBox)rowView.findViewById(R.id.view_checkBox);
		if (currentGroups.contains(item.getId())) {
			checkBox.setChecked(true);
		} else {
			checkBox.setChecked(false);
		}
		checkBox.setOnCheckedChangeListener(new EditGroupsOnCheckedChangeListener(item, currentGroups));
		
		return rowView;
	}
}
