package com.teambuilder.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.teambuilder.Player;
import com.teambuilder.PlayerEditActivity;
import com.teambuilder.R;

public class PlayerListAdapter extends ArrayAdapter<Player> {

	Integer activityId = -1;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.layout_view_playerbar, null);
		
		Player player = this.getItem(position);
		
		TextView nameView = (TextView)v.findViewById(R.id.playerNameView);
		nameView.setText(player.getName());
		nameView.setTag(player);
		nameView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Player player = (Player)view.getTag();
				Intent intent = new Intent(getContext(), PlayerEditActivity.class);
				intent.putExtra(Player.label, player);
				
				getContext().startActivity(intent);
			}
		});
		
		TextView skillView = (TextView)v.findViewById(R.id.playerSkillView);
		String text = "-";
		if (player.getSkill(activityId) != null) {
			text = player.getSkill(activityId).toString();
		}
		skillView.setText(text);
		
		CheckBox checkBox = (CheckBox)v.findViewById(R.id.playerAttendingCheckBox);
		checkBox.setTag(player);
		checkBox.setSelected(player.isSelected());
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Player player = (Player)buttonView.getTag();
				player.setSelected(isChecked);
			}
			
		});
		
		return v;
	}

	public PlayerListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
}
