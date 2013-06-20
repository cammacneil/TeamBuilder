package com.teambuilder;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.teambuilder.utilities.DatabaseHandler;

public class PlayerEditActivity extends Activity {

	private Player player;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playeredit);
	
		Intent intent = getIntent();
		PlayerParcelable playerParcelable = intent.getParcelableExtra(PlayerParcelable.label);
		
		if (playerParcelable != null) {
			player = playerParcelable.player;
		}
		
		ActionBar actionBar = getActionBar();
		
		//New Player
		if (player == null) {
			actionBar.setTitle(R.string.player_add_title);
		} else { //Existing player
			actionBar.setTitle(player.getName());
		}
		
	}
	
	public void save(View view) {
		if (!checkFields()) {
			return;
		}
		
		String name = ((EditText)findViewById(R.id.text_name)).getText().toString();
		
		if (player == null) {
			player = new Player(name);
		}
		
		DatabaseHandler db = new DatabaseHandler(this);
		db.open();
		player = db.createPlayer(player);
		db.close();
		
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	public boolean checkFields() {
		boolean fieldsOK = true;
		
		//Check that the name field isn't empty
		if (((EditText)findViewById(R.id.text_name)).getText().toString() == "")
			fieldsOK = false;
		return fieldsOK;
	}
	
	
	
	
}
