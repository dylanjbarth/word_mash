package com.dylanbarth.wordmash.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.dylanbarth.wordmash.R;

public class HelpActivity extends Activity {

	Button backButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		backButton = (Button) findViewById(R.id.back);
	    backButton.setOnClickListener(goBackToWelcome);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_help, menu);
		return true;
	}
	
	View.OnClickListener goBackToWelcome = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent myIntent = new Intent(HelpActivity.this, WelcomeMenu.class);
			HelpActivity.this.startActivity(myIntent);
			
		}
	};

}
