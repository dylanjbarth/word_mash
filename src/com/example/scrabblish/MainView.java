package com.example.scrabblish;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.letterscrummage.R;
import com.example.scrabblish.model.Board;
import com.example.scrabblish.model.LetterTray;
import com.example.scrabblish.model.Tile;
import com.example.scrabblish.model.TileSpace;

public class MainView extends SurfaceView implements SurfaceHolder.Callback {
	
	private static final String TAG = MainView.class.getSimpleName();
	
	private MainThread thread;
	private Tile tile1, tile2, tile3, tile4, tile5, tile6;
	private Board board;
	private LetterTray letterTray;
	private int BOARD_SIZE = 7;
	private int LETTER_TRAY_SIZE = 6;
	private int screenWidth;
	private int screenHeight;	
	
	public MainView(Context context){
		super(context);
		getHolder().addCallback(this);
		thread = new MainThread(getHolder(), this);
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		prepGame(); // creates game objects
		setFocusable(true);
		Log.d(TAG, "Hey Dylan, I exist in case you forget the syntax for message logging :D");
		Log.d(TAG, "ScreenWidth: " + screenWidth);
		Log.d(TAG, "ScreenHeight: " + screenHeight);
		
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
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			// letter tray checks each tile to see if it has been touched
			letterTray.handleActionDown((int)event.getX(), (int)event.getY());
			// exit if touch bottom of screen
//			if (event.getY() > getHeight() - 50){
//				thread.setRunning(false);
//				((Activity)getContext()).finish();
//			} else {
//				Log.d(TAG, "Coords: x=" + event.getX() + ", y=" + event.getY());
//			}
		} if (event.getAction() == MotionEvent.ACTION_MOVE){
			// Get touched tile, if it exists, and reset X & Y based on event
			Object tile = letterTray.tileTouched();
			if(tile != null){
				((Tile) tile).setX((int)event.getX());
				((Tile) tile).setY((int)event.getY());
				Log.d(TAG, "Moving tile index: " + ((Tile) tile).getIndex());
			}
		} if (event.getAction() == MotionEvent.ACTION_UP){
			// first check location of touched tiles to see if it should snap into place
			Object tile = letterTray.tileTouched();
			if(tile != null){
				((Tile) tile).setTouched(false);
			}
			// next set touched to false

		}
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		canvas.drawColor(Color.BLACK);
		board.draw(canvas);
		letterTray.draw(canvas);
	}
	
	public void prepGame(){
		// first, create game board, specifying x, y, and size 
		createBoard(0, 0, BOARD_SIZE*BOARD_SIZE);
		
		// Next, create letter tray (Size based on board size)
		createLetterTray();
	}

	public void createBoard(int x, int y, int size){
		// Create tileSpaces that make up board grid
		Object[][] tileSpaces = createTileSpaces(BOARD_SIZE);
		board = new Board(x, y, size, tileSpaces);
	}
	
	public Object[][] createTileSpaces(int size){
		// returns an 2D object array of tileSpaces 
		Object[][] tileSpaces = new Object[size][size];
		int imgWidth = screenWidth/(size*2);
		int imgHeight = screenHeight/size;
		for(int r=0; r < size; r++){
			for (int c=0; c < size; c++){
				Bitmap tileSpaceImage = BitmapFactory.decodeResource(getResources(), R.drawable.tile_space);
				TileSpace tile = new TileSpace(tileSpaceImage, r, c, imgWidth, imgHeight);
				tileSpaces[r][c] = tile;
			}
		}
		return tileSpaces;
	}
	
	public Bitmap scaleBitmap(Bitmap tileSpaceImage, int imgWidth, int imgHeight){
		Bitmap resizedBitmap=Bitmap.createScaledBitmap(tileSpaceImage, imgWidth, imgHeight, true);
		return resizedBitmap;
	}
	
	public void createLetterTray(){
		int boardWidth = board.getWidth();
		Object[] tray = new Object[LETTER_TRAY_SIZE];
		for(int i=0; i < LETTER_TRAY_SIZE; i++){
			tray[i] = ((Tile) createTile(i, boardWidth));
		}
		letterTray = new LetterTray(boardWidth, 0, LETTER_TRAY_SIZE, tray);
	}
	
	public Tile createTile(int index, int startX) {
		int imgWidth = screenWidth/(BOARD_SIZE*2);
		int imgHeight = screenHeight/BOARD_SIZE;
		Bitmap tileImage = BitmapFactory.decodeResource(getResources(), R.drawable.letter_tile);
		Tile tile = new Tile(tileImage, imgWidth, imgHeight, index, startX);
		return tile;
	}
}
