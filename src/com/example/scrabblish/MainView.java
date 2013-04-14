package com.example.scrabblish;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.letterscrummage.R;
import com.example.scrabblish.model.Board;
import com.example.scrabblish.model.Tile;
import com.example.scrabblish.model.TileSpace;

public class MainView extends SurfaceView implements SurfaceHolder.Callback {
	
	private static final String TAG = MainView.class.getSimpleName();
	
	private MainThread thread;
	private Tile tile1, tile2, tile3, tile4, tile5, tile6;
	private Board board;
	
	
	public MainView(Context context){
		super(context);
		getHolder().addCallback(this);
		thread = new MainThread(getHolder(), this);
		prepGame(); // instantiates game objects
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
			tile1.handleActionDown((int)event.getX(), (int)event.getY());
			// exit if touch bottom of screen
			if (event.getY() > getHeight() - 50){
				thread.setRunning(false);
				((Activity)getContext()).finish();
			} else {
				Log.d(TAG, "Coords: x=" + event.getX() + ", y=" + event.getY());
			}
		} if (event.getAction() == MotionEvent.ACTION_MOVE){
			if(tile1.isTouched()){
				tile1.setX((int)event.getX());
				tile1.setY((int)event.getY());
			}
		} if (event.getAction() == MotionEvent.ACTION_UP){
			if(tile1.isTouched()){
				tile1.setTouched(false);
			}
		}
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		canvas.drawColor(Color.BLACK);
		tile1.draw(canvas);
	}
	
	public void prepGame(){
		// first, create game board, specifying x, y, and size 
		instantiateBoard(0, 0, 36);
		
		// Finally, create tiles
		instantiateLetterTiles();
	}

	public void instantiateBoard(int x, int y, int size){
		// Create tileSpaces that make up board grid
		Object[][] tileSpaces = instantiateTileSpaces(6, 6);
		board = new Board(x, y, size, tileSpaces);
	}
	
	public Object[][] instantiateTileSpaces(int rows, int columns){
		// returns an object array of tileSpaces 
		Object[][] tileSpaces = new Object[rows][columns];
		for(int r=0; r < rows; r++){
			for (int c=0; c < columns; c++){
				TileSpace tile = new TileSpace(r, c, false, 1);
				tileSpaces[r][c] = tile;
			}
		}
		return tileSpaces;
	}
	
	public void instantiateLetterTiles(){
		Bitmap tileImage = BitmapFactory.decodeResource(getResources(), R.drawable.letter_tile_a);
		tile1 = new Tile(tileImage, 50, 50, 'c');
//		tile2 = new Tile(tileImage, 50, 50, 'c');
//		tile3 = new Tile(tileImage, 50, 50, 'c');
//		tile4 = new Tile(tileImage, 50, 50, 'c');
//		tile5 = new Tile(tileImage, 50, 50, 'c');
//		tile6 = new Tile(tileImage, 50, 50, 'c');
	}

}
