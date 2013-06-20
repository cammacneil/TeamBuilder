package com.teambuilder;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.teambuilder.utilities.DatabaseHandler;

public class PlayersFragment extends Fragment {

	private boolean firstTimeOnCreateOptionsMenu = false;
	private DatabaseHandler db;
	
	ArrayAdapter<Player> mAdapter;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		db = new DatabaseHandler(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.players_fragment, container, false);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		db.open();
		
		ArrayList<Player> list = db.getPlayerList(null);

		db.close();
		
		mAdapter = new ArrayAdapter<Player>(this.getActivity(),
				android.R.layout.simple_list_item_1);
		mAdapter.addAll(list);

		ListView view = (ListView) getActivity().findViewById(R.id.list_players);
		view.setAdapter(mAdapter);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.menu_player_fragment, menu);
		super.onCreateOptionsMenu(menu, inflater);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.menu_addPlayer) {
			Intent intent = new Intent(getActivity(), PlayerEditActivity.class);
			
			getActivity().startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
}
