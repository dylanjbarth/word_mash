package com.example.scrabblish;

import java.util.Arrays;

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
import com.example.scrabblish.model.Component;
import com.example.scrabblish.model.GameMenu;
import com.example.scrabblish.model.LetterTray;
import com.example.scrabblish.model.Tile;
import com.example.scrabblish.model.TileSpace;

public class MainView extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = MainView.class.getSimpleName();

	private MainThread thread;
	private Board board;
	private LetterTray letterTray;
	private GameMenu gameMenu;
	private int BOARD_SIZE = 7; // note that changing either of these may make it necessary to change buffer size in TileSpace.handleTileSnapping
	private int LETTER_TRAY_SIZE = 6;
	private int screenWidth;
	private int screenHeight;
	private int boardWidth;
	private int letterTrayWidth;
	private int gameMenuWidth;

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

	/*************************
	 * Create Game Objects * 
	 *************************/

	public void prepGame(){
		createBoard(0, 0, BOARD_SIZE*BOARD_SIZE);
		createLetterTray();
		createGameMenu();
	}

	public void createBoard(int x, int y, int size){
		// Create tileSpaces that make up board grid
		TileSpace[][] tileSpaces = createTileSpaces(BOARD_SIZE);
		board = new Board(x, y, size, tileSpaces);
	}

	public TileSpace[][] createTileSpaces(int size){
		// return an 2D object array containing tileSpaces 
		TileSpace[][] tileSpaces = new TileSpace[size][size];
		int imgWidth = screenWidth/(size*2);
		int imgHeight = screenHeight/size;
		for(int c=0; c < size; c++){
			for (int r=0; r < size; r++){
				Bitmap tileSpaceImage = BitmapFactory.decodeResource(getResources(), R.drawable.tile_space);
				TileSpace tile = new TileSpace(tileSpaceImage, r, c, imgWidth, imgHeight);
				tileSpaces[r][c] = tile;
			}
		}
		return tileSpaces;
	}

	public void createLetterTray(){
		// Create tiles that make up letter tray array
		boardWidth = board.getWidth();
		Tile[] tray = new Tile[LETTER_TRAY_SIZE];
		for(int i=0; i < LETTER_TRAY_SIZE; i++){
			tray[i] = ((Tile) createTile(i, boardWidth));
		}
		letterTray = new LetterTray(boardWidth, 0, LETTER_TRAY_SIZE, tray);
		letterTrayWidth = letterTray.getWidth();
	}

	public Tile createTile(int index, int startX) {
		// return a tile at the appropriate space and index
		int imgWidth = screenWidth/(BOARD_SIZE*2);
		int imgHeight = screenHeight/BOARD_SIZE;
		Bitmap tileImage = BitmapFactory.decodeResource(getResources(), R.drawable.letter_tile);
		Tile tile = new Tile(tileImage, imgWidth, imgHeight, index, startX);
		//		Log.d(TAG, "Creating new tile with index: " + index + ", width: " + imgWidth + "height: " + imgHeight);
		return tile;
	}

	public void createGameMenu(){
		// Create components
		gameMenuWidth = screenWidth - boardWidth - letterTrayWidth;
		int x = boardWidth+letterTrayWidth;
		int y = 0;
		Component[] gameMenuComponents = createGameMenuComponents(x, gameMenuWidth);
		gameMenu = new GameMenu(x, y, gameMenuWidth, screenHeight, gameMenuComponents);
	}

	public Component[] createGameMenuComponents(int startX, int width){
		int NUMCOMPONENTS = 5;
		Component[] components = new Component[NUMCOMPONENTS];
		for (int i=0; i < NUMCOMPONENTS; i++){
			String title = "";
			int height = screenHeight/NUMCOMPONENTS, x = startX, y = height*i;
			boolean isButton = false;
			switch(i){
			case 0: 
				title = "logo";
				break;
			case 1:
				title = "score";
				break;
			case 2:
				title = "shuffle";
				isButton = true;
				break;
			case 3:
				title = "newTiles";
				isButton = true;
				break;
			case 4:
				title = "menu";
				isButton = true;
				break;
			}
			Component component = new Component(title, x, y, width, height, isButton);
			components[i] = component;
		}
		return components;
	}

	/*************************
	 * Game logic * 
	 *************************/

	@Override
	public boolean onTouchEvent(MotionEvent event){
		float eventX = event.getX();
		float eventY = event.getY();
		Log.d(TAG, "Touch: x=" + eventX + ", y=" + eventY);
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			board.checkForExports((int)eventX, (int)eventY);
			letterTray.handleActionDown((int)eventX, (int)eventY);
			// exit if touch bottom of screen
			//			if (event.getY() > getHeight() - 50){
			//				thread.setRunning(false);
			//				((Activity)getContext()).finish();
			//			}
		} if (event.getAction() == MotionEvent.ACTION_MOVE){
			// Update for dragging
			Tile tile = letterTray.tileTouched();
			if(tile != null){
				tile.dragSetX((int)event.getX());
				tile.dragSetY((int)event.getY());
			}
		} if (event.getAction() == MotionEvent.ACTION_UP){
			// snap tile into place
			Tile tile = letterTray.tileTouched();
			if(tile != null){
				board.snapTileIntoPlace(tile);
				tile.setTouched(false);
			}
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas){
		canvas.drawColor(Color.BLACK);
		board.draw(canvas);
		gameMenu.draw(canvas);
		letterTray.draw(canvas);
	}
}
