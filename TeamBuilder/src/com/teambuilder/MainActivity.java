package com.teambuilder;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.teambuilder.listeners.TabListener;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        
        Tab tab = actionBar.newTab()
        		.setText(R.string.action_players)
        		.setTabListener(new TabListener<PlayersFragment>(
        				this, "players", PlayersFragment.class));
        		
        actionBar.addTab(tab);
        
        tab = actionBar.newTab()
        		.setText(R.string.action_teams)
        		.setTabListener(new TabListener<TeamsFragment>(
        				this, "teams", TeamsFragment.class));
        		
        actionBar.addTab(tab);
        
        tab = actionBar.newTab()
        		.setText(R.string.action_settings)
        		.setTabListener(new TabListener<SettingsFragment>(
        				this, "settings", SettingsFragment.class));
        		
        actionBar.addTab(tab);
        
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
    
}
