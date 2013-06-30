package com.teambuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ListView;

public class PlayerEditGroupsDialog extends DialogFragment {

	String[] groups;
	List<String> selectedItems;
	boolean[] preSelectedItems;
	
	static PlayerEditGroupsDialog newInstance() {
		
		PlayerEditGroupsDialog dialog = new PlayerEditGroupsDialog();
		
		Bundle args = new Bundle();
		dialog.setArguments(args);
		
		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		selectedItems = new ArrayList<String>();
		
		for (int i=0;i<groups.length;i++) {
			if (preSelectedItems[i]) {
				selectedItems.add(groups[i]);
			}
		}
		
		ListView view = (ListView)getActivity().findViewById(R.id.listview_player_edit_groups);
		
		return new AlertDialog.Builder(getActivity())
			.setTitle(R.string.text_groups_title)
			.setPositiveButton(R.string.dialog_ok, 
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							((PlayerEditActivity)getActivity()).saveManagedGroups(selectedItems);
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
			.setMultiChoiceItems(groups, preSelectedItems, 
					new DialogInterface.OnMultiChoiceClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which, boolean isChecked) {
							if (isChecked) {
								selectedItems.add(groups[which]);
							} else if (selectedItems.contains(groups[which])) {
								selectedItems.remove(groups[which]);
							}
						}
					})
			.setView(view)
			.show();
	}
	
	
	
}
