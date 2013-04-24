package com.example.scrabblish;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.scrabblish.model.Board;
import com.example.scrabblish.model.Game;
import com.example.scrabblish.model.GameMenu;
import com.example.scrabblish.model.LetterTray;
import com.example.scrabblish.model.Tile;

public class MainView extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = MainView.class.getSimpleName();

	private MainThread thread;
	private Game game;
	private Resources gameResources;
	private int screenWidth;
	private int screenHeight;

	public MainView(Context context){
		super(context);
		getHolder().addCallback(this);
		gameResources = context.getResources();
		DisplayMetrics metrics = gameResources.getDisplayMetrics();
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		Log.d(TAG, screenWidth + " " + screenHeight);
		game = new Game(screenWidth, screenHeight, gameResources);
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
		float eventX = event.getX();
		float eventY = event.getY();
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			game.handleActionDown((int)eventX, (int)eventY);
		} if (event.getAction() == MotionEvent.ACTION_MOVE){
			game.handleActionMove((int)eventX, (int)eventY);
		} if (event.getAction() == MotionEvent.ACTION_UP){
			game.handleActionUp((int)eventX, (int)eventY);
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas){
		canvas.drawColor(Color.BLACK);
		game.draw(canvas);
	}
}

// how to quit thread:
// thread.setRunning(false);
// how to quit activity:
// ((Activity)getContext()).finish();
