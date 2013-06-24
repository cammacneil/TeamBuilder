package com.teambuilder;

import android.app.Activity;
import android.app.Fragment;

public class TeamBuilderFragment extends Fragment {

	protected TeamBuilderActivity getTeamBuilderActivity() {
		Activity parentActivity = getActivity();
		
		if(parentActivity instanceof TeamBuilderActivity) {
			return (TeamBuilderActivity) parentActivity;
		}
		
		throw new RuntimeException("Parnet activity is not a team builder activity.");
	}
}
