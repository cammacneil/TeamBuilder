package com.teambuilder;

import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.teambuilder.adapters.PlayerEditGroupAdapter;

public class PlayerEditGroupsDialog extends DialogFragment {

	Map<Integer, DatabaseObject> groupList;
	List<Integer> currentGroups;
	
	static PlayerEditGroupsDialog newInstance() {
		
		PlayerEditGroupsDialog dialog = new PlayerEditGroupsDialog();
		
		Bundle args = new Bundle();
		dialog.setArguments(args);
		
		return dialog;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
//		PlayerEditActivity parent = (PlayerEditActivity)getActivity();
//		groupList = parent.groups;
//		
//		currentGroups = parent.getPlayer().getGroups();
//		
//		View v = inflater.inflate(R.layout.fragment_manage_player_groups, container, false);
//		
//		ListView view = (ListView)v.findViewById(R.id.listview_player_edit_groups);
//		
//		PlayerEditGroupAdapter adapter = new PlayerEditGroupAdapter(parent, R.layout.fragment_manage_player_groups, groupList, currentGroups);
//		view.setAdapter(adapter);
//		
//		return v;
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		ListView view = (ListView)getActivity().findViewById(R.id.listview_player_edit_groups);
		
		return new AlertDialog.Builder(getActivity())
			.setTitle(R.string.text_groups_title)
			.setPositiveButton(R.string.dialog_ok, 
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							((PlayerEditActivity)getActivity()).saveManagedGroups();
						}
					}
			)
			.setNegativeButton(R.string.dialog_cancel, 
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							((PlayerEditActivity)getActivity()).cancelManagedGroups();
						}
					}
			)
			.setView(view)
			.show();
	}
	
	
	
}
