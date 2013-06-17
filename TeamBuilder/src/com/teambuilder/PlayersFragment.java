package com.teambuilder;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PlayersFragment extends Fragment {

	ArrayAdapter<Player> mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.players_fragment, container, false);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		ArrayList<Player> list = new ArrayList<Player>();
		list.add(new Player("Chris", "Gwozdecky", 90));
		list.add(new Player("Cam", "MacNeil", 80));
		list.add(new Player("Eric", "Soutar", 60));

		mAdapter = new ArrayAdapter<Player>(this.getActivity(),
				android.R.layout.simple_list_item_1);
		mAdapter.addAll(list);

		ListView view = (ListView) getActivity().findViewById(R.id.list_players);
		view.setAdapter(mAdapter);
	}
}
