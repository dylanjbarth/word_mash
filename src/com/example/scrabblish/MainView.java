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
import com.example.scrabblish.model.Tile;
import com.example.scrabblish.model.TileSpace;

public class MainView extends SurfaceView implements SurfaceHolder.Callback {
	
	private static final String TAG = MainView.class.getSimpleName();
	
	private MainThread thread;
	private Tile tile1, tile2, tile3, tile4, tile5, tile6;
	private Board board;
	private int BOARD_SIZE = 7;
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
		board.draw(canvas);
		tile1.draw(canvas);
	}
	
	public void prepGame(){
		// first, create game board, specifying x, y, and size 
		createBoard(0, 0, BOARD_SIZE*BOARD_SIZE);
		
		// Next, create letter tiles
		createLetterTiles();
	}

	public void createBoard(int x, int y, int size){
		// Create tileSpaces that make up board grid
		Object[][] tileSpaces = createTileSpaces(BOARD_SIZE);
		board = new Board(x, y, size, tileSpaces);
	}
	
	public Object[][] createTileSpaces(int size){
		// returns an object array of tileSpaces 
		Object[][] tileSpaces = new Object[size][size];
		int imgWidth = screenWidth/(size*2);
		int imgHeight = screenHeight/size;
		for(int r=0; r < size; r++){
			for (int c=0; c < size; c++){
				Bitmap tileSpaceImage = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.tile_space), imgWidth, imgHeight);
				int[] tilePos = positionTile(r, c, imgWidth, imgHeight);
				TileSpace tile = new TileSpace(tileSpaceImage, r, c, tilePos[0], tilePos[1], false, 1);
				tileSpaces[r][c] = tile;
			}
		}
		return tileSpaces;
	}
	
	public Bitmap scaleBitmap(Bitmap tileSpaceImage, int imgWidth, int imgHeight){
		Bitmap resizedBitmap=Bitmap.createScaledBitmap(tileSpaceImage, imgWidth, imgHeight, true);
		return resizedBitmap;
	}
	
	public int[] positionTile(int row, int col, int imgWidth, int imgHeight){
		// gives center of tile in x, y coordinates
		int x = imgWidth*row;
		int y = imgHeight*col;
		int[] position = {x, y};
		return position;
	}
	
	public void createLetterTiles(){
		Bitmap tileImage = BitmapFactory.decodeResource(getResources(), R.drawable.letter_tile_a);
		tile1 = new Tile(tileImage, 500, 300, 'c');
//		tile2 = new Tile(tileImage, 50, 50, 'c');
//		tile3 = new Tile(tileImage, 50, 50, 'c');
//		tile4 = new Tile(tileImage, 50, 50, 'c');
//		tile5 = new Tile(tileImage, 50, 50, 'c');
//		tile6 = new Tile(tileImage, 50, 50, 'c');
	}

}
