package com.dylanbarth.wordmash;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.dylanbarth.wordmash.model.Game;

public class MainActivity extends Activity {
	
	private MainView mainView; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// probably going to pass in the objects below to the view, board and user
		mainView = new MainView(this);
		setContentView(mainView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override 
	protected void onPause(){
		Game game = mainView.getGame();
		try{
			if(game.getState() == "ingame"){
				game.pauseGame();
			}
		} catch (Exception e) {
			// do nothing!
		}
		
	}

}
