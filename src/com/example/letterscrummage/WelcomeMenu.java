package com.example.letterscrummage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.scrabblish.MainActivity;

public class WelcomeMenu extends Activity {
	
	Button startGame, helpButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_menu);
		startGame = (Button) findViewById(R.id.new_game);
	    helpButton = (Button) findViewById(R.id.help);
	    startGame.setOnClickListener(startNewGame);
	    helpButton.setOnClickListener(startHelp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_welcome_menu, menu);
		return true;
	}
	
	View.OnClickListener startNewGame = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent myIntent = new Intent(WelcomeMenu.this, MainActivity.class);
			WelcomeMenu.this.startActivity(myIntent);
			
		}
	};
	
	View.OnClickListener startHelp = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent myIntent = new Intent(WelcomeMenu.this, HelpActivity.class);
			WelcomeMenu.this.startActivity(myIntent);
		}
	};
	
	

}