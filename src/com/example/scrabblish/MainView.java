package com.example.scrabblish;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.letterscrummage.R;
import com.example.scrabblish.model.Tile;

public class MainView extends SurfaceView implements SurfaceHolder.Callback {
	
	private static final String TAG = MainView.class.getSimpleName();
	
	private MainThread thread;
	private Tile tile;
	
	
	public MainView(Context context){
		super(context);
		getHolder().addCallback(this);
		thread = new MainThread(getHolder(), this);
		tile = new Tile(BitmapFactory.decodeResource(getResources(), R.drawable.letter_tile_a), 50, 50);
		setFocusable(true);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
	}
	
	@Override 
	public void surfaceCreated(SurfaceHolder holder){
		System.out.println("Starting thread");
		thread.setRunning(true);
		thread.start();
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
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			// tile handles action down to see if it's touched 
			tile.handleActionDown((int)event.getX(), (int)event.getY());
			// exit if touch bottom of screen
			if (event.getY() > getHeight() - 50){
				thread.setRunning(false);
				((Activity)getContext()).finish();
			} else {
				Log.d(TAG, "Coords: x=" + event.getX() + ", y=" + event.getY());
			}
		} if (event.getAction() == MotionEvent.ACTION_MOVE){
			if(tile.isTouched()){
				tile.setX((int)event.getX());
				tile.setY((int)event.getY());
			}
		} if (event.getAction() == MotionEvent.ACTION_UP){
			if(tile.isTouched()){
				tile.setTouched(false);
			}
		}
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		canvas.drawColor(Color.BLACK);
		tile.draw(canvas);
	}

}
