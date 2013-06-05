package com.dylanbarth.wordmash;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dylanbarth.wordmash.model.Game;

public class MainView extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = MainView.class.getSimpleName();

	private MainThread thread;
	private Game game;
	private int screenWidth;
	private int screenHeight;

	public MainView(Context context){
		super(context);
		getHolder().addCallback(this);
		Resources gameResources = context.getResources();
		DisplayMetrics metrics = gameResources.getDisplayMetrics();
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		Log.d(TAG, screenWidth + " " + screenHeight);
		ArrayList<String> wordList = createWordList(context.getAssets());
		game = new Game(screenWidth, screenHeight, context, wordList);
		thread = new MainThread(getHolder(), this);
		setFocusable(true);
	}

	@Override 
	public void surfaceCreated(SurfaceHolder holder){
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
		// necessary for MainView
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder){
		boolean retry = true;
		while(retry){
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again
			}
		}
	}

	/*************************
	 * Game logic * 
	 *************************/

	@Override
	public boolean onTouchEvent(MotionEvent event){
		// Check game state, then handle touches appropriately.
		int eventX = ((int) event.getX());
		int eventY = ((int) event.getY());
		game.handleAction(event.getAction(), eventX, eventY);
		return true;
	}

	@Override
	public void onDraw(Canvas canvas){
		canvas.drawColor(Color.BLACK);
		game.draw(canvas);
	}

	/*************************
	 * Helper * 
	 *************************/

	public ArrayList<String> createWordList(AssetManager assets){
		ArrayList<String> words = new ArrayList<String>();
		InputStream inputStream = null;
		try {
			inputStream = assets.open("wordlist.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader buffReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = null;
		try {
			while ((line = buffReader.readLine()) != null) {
				words.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return words;
	}
}

// how to quit thread:
// thread.setRunning(false);
// how to quit activity:
// ((Activity)getContext()).finish();
