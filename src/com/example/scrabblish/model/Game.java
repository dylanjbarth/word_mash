package com.example.scrabblish.model;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.example.letterscrummage.R;
import com.example.scrabblish.MainView;

public class Game {
	private Board board;
	private LetterTray tray;
	private GameMenu menu;
	private Resources resources;
	private ScheduledExecutorService timer;
	private int width, height;
	private int BOARD_SIZE = 7, LETTER_TRAY_SIZE = 7, COMPONENTS = 5, PENALTY = 0;
	private String state;
	private ArrayList<String> wordList;
	private boolean TIMER_RUNNING = false;

	private static final String TAG = MainView.class.getSimpleName();

	public Game(int screenW, int screenH, Resources resources, ArrayList<String> wordList){
		this.resources = resources;
		this.width = screenW;
		this.height = screenH;
		this.board = createBoard(0, 0, BOARD_SIZE*BOARD_SIZE, wordList);
		this.tray = createLetterTray();
		this.menu = createGameMenu();
		this.state = "preGame";
	}

	/*************************
	 * Creators * 
	 *************************/

	public Board createBoard(int x, int y, int size, ArrayList<String> wordList){
		// Create tileSpaces that make up board grid
		TileSpace[][] tileSpaces = createTileSpaces(BOARD_SIZE);
		board = new Board(x, y, size, tileSpaces, wordList);
		return board;
	}

	public TileSpace[][] createTileSpaces(int size){
		// return an 2D object array containing tileSpaces 
		TileSpace[][] tileSpaces = new TileSpace[size][size];
		int imgWidth = (this.width/2)/size;
		int imgHeight = this.height/size;
		for(int c=0; c < size; c++){
			for (int r=0; r < size; r++){
				Bitmap tileSpaceImage = BitmapFactory.decodeResource(resources, R.drawable.tile_space);
				TileSpace tile = new TileSpace(tileSpaceImage, r, c, imgWidth, imgHeight);
				tileSpaces[r][c] = tile;
			}
		}
		return tileSpaces;
	}

	public LetterTray createLetterTray(){
		// Create tiles that make up letter tray array
		int boardWidth = this.board.getWidth();
		ArrayList<Tile> tray = new ArrayList<Tile>();
		for(int i=0; i < LETTER_TRAY_SIZE; i++){
			tray.add(createTile(i, boardWidth));
		}
		LetterTray letterTray = new LetterTray(boardWidth, 0, tray);
		letterTray.ensureVowel();
		return letterTray;
	}

	public Tile createTile(int index, int startX) {
		// return a tile at the appropriate space and index
		int imgWidth = this.width/(BOARD_SIZE*2);
		int imgHeight = this.height/BOARD_SIZE;
		Bitmap tileImage = BitmapFactory.decodeResource(resources, R.drawable.letter_tile);
		Tile tile = new Tile(tileImage, imgWidth, imgHeight, index, startX);
		//		Log.d(TAG, "Creating new tile with index: " + index + ", width: " + imgWidth + "height: " + imgHeight);
		return tile;
	}
	
	public void addNewTiles(){
		int boardWidth = this.board.getWidth();
		for(int i=0; i < LETTER_TRAY_SIZE; i++){
			getTray().getTray().add(0, createTile(i, boardWidth)); // inserts at beginning
		}
	}

	public GameMenu createGameMenu(){
		// Create components
		int gameMenuWidth = this.width - this.board.boardWidth() - this.tray.getWidth();
		int x = this.board.boardWidth()+this.tray.getWidth();
		int y = 0;
		Component[] gameMenuComponents = createGameMenuComponents(x, gameMenuWidth);
		GameMenu gameMenu = new GameMenu(x, y, gameMenuWidth, this.height, gameMenuComponents, this.state);
		return gameMenu;
	}

	public Component[] createGameMenuComponents(int startX, int width){
		Component[] components = new Component[COMPONENTS];
		for (int i=0; i < COMPONENTS; i++){
			String title = "";
			int height = this.height/COMPONENTS, x = startX, y = height*i, index = i;
			boolean isButton = false;
			switch(i){
			case 0: 
				title = "logo";
				break;
			case 1:
				title = "scoreTimer";
				break;
			case 2:
				// shuffle or recall tiles
				title = "updateTray";
				isButton = true;
				break;
			case 3:
				// allows exchange of tiles
				title = "newTiles";
				isButton = true;
				break;
			case 4:
				// new game, pause, resume
				title = "changeGameState";
				isButton = true;
				break;
			}
			Component component = new Component(title, index, x, y, width, height, isButton);
			components[i] = component;
		}
		return components;
	}

	/*************************
	 * Getters * 
	 *************************/

	public Board getBoard() {
		return board;
	}

	public LetterTray getTray() {
		return tray;
	}

	public GameMenu getMenu() {
		return menu;
	}

	/*************************
	 * Setters * 
	 *************************/

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setTray(LetterTray tray) {
		this.tray = tray;
	}

	public void setMenu(GameMenu menu) {
		this.menu = menu;
	}

	/*************************
	 * Helpers * 
	 *************************/

	public void draw(Canvas canvas){
		board.draw(canvas);
		menu.draw(canvas);
		tray.draw(canvas); // order matters - draw tiles last
	}

	public void handleAction(int event, int eventX, int eventY){
		String gameState = this.state;
		if(gameState == "preGame"){
			if(event == MotionEvent.ACTION_DOWN){
				preGameActionDown(eventX, eventY);
			} else if (event == MotionEvent.ACTION_MOVE){
				preGameActionMove(eventX, eventY);
			} else if (event == MotionEvent.ACTION_UP){
				preGameActionUp(eventX, eventY);
			}
			if(newGameClicked()){
				this.state = "inGame";
				menu.resetButtonClicked("changeGameState");
				startGameTimer();
			}
		} else if (gameState == "inGame"){
			if(event == MotionEvent.ACTION_DOWN){
				inGameActionDown(eventX, eventY);
			} else if (event == MotionEvent.ACTION_MOVE){
				inGameActionMove(eventX, eventY);
			} else if (event == MotionEvent.ACTION_UP){
				inGameActionUp(eventX, eventY);
			}
		} else if (gameState == "postGame"){

		}
	}

	/*************************
	 * preGame methods * 
	 *************************/

	public void preGameActionDown(int eventX, int eventY){
		menu.preGameActionDown(eventX, eventY);
	}

	public void preGameActionMove(int eventX, int eventY){
		menu.handleActionMove(eventX, eventY);
	}

	public void preGameActionUp(int eventX, int eventY){
		menu.handleActionUp(eventX, eventY);
	}

	public boolean newGameClicked(){
		return menu.checkIfButtonClicked("changeGameState");
	}

	/*************************
	 * inGame methods * 
	 *************************/

	public void inGameActionDown(int eventX, int eventY){
		board.handleMovingTiles(eventX, eventY);
		menu.handleActionDown(eventX, eventY);
		tray.handleActionDown(eventX, eventY);
	}

	public void inGameActionMove(int eventX, int eventY){
		menu.handleActionMove(eventX, eventY); 
		Tile tile = tray.tileTouched();
		if(tile != null){
			tile.dragSetX(eventX);
			tile.dragSetY(eventY);
		}
	}

	public void inGameActionUp(int eventX, int eventY){
		menu.handleActionUp(eventX, eventY);
		if(menu.checkIfButtonClicked("changeGameState")){
			if(TIMER_RUNNING){
				pauseGameTimer();
				this.state = "preGame";
			} else {
				startGameTimer();
				this.state = "inGame";
			}
			menu.resetButtonClicked("changeGameState");
		} else if(menu.checkIfButtonClicked("newTiles")){
			// calculate penalty for cashing in
			PENALTY += tray.calculatePenalty();
			int score = board.calculateScore();
			menu.getComponent("scoreTimer").setScore(score-PENALTY);
			// lock current tiles to board
			tray.lockTilesOnBoard(); // also sets their index to 7
			// erase leftovers (maybe eventually also erase tiles that aren't part of valid words)
			tray.deleteTilesInTray();
			// create new letter tiles
			addNewTiles();
			menu.resetButtonClicked("newTiles");
		}
		Tile tile = tray.tileTouched();
		if(tile != null){
			board.snapTileIntoPlace(tile);
			tile.setTouched(false);
			tray.setAllTilesValidityToFalse();
			int score = board.calculateScore();
			menu.getComponent("scoreTimer").setScore(score-PENALTY);
			// calculate score
		}
	}

	/*************************
	 * postGame methods * 
	 *************************/
	
	/*************************
	 * timer methods * 
	 *************************/
	public void startGameTimer(){
		TIMER_RUNNING = true;
		timer = Executors.newSingleThreadScheduledExecutor();
		Runnable startTimer = new Runnable() {
			public void run() {
				Component timerComponent = menu.getComponent("scoreTimer");
				timerComponent.subtractTime();
				Log.d(TAG, "Time: " + timerComponent.getTime());
				if(timerComponent.getTime() == 0){
					timer.shutdown();
					TIMER_RUNNING = false;
					Log.d(TAG, "Timer canceled."); 
					state = "postGame";
				}
			}
		};
		timer.scheduleAtFixedRate(startTimer, 0, 1000, TimeUnit.MILLISECONDS);
	}

	public void pauseGameTimer(){
		timer.shutdown();
		TIMER_RUNNING = false;
	}

}
