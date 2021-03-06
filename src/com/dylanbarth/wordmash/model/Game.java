package com.dylanbarth.wordmash.model;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;

import com.dylanbarth.wordmash.MainView;
import com.dylanbarth.wordmash.R;
import com.dylanbarth.wordmash.model.PauseScreen.RestartButton;
import com.dylanbarth.wordmash.model.PauseScreen.ResumeButton;

public class Game {
	private Board board;
	private LetterTray tray;
	private GameMenu menu;
	private PauseScreen pauseScreen;
	private PostGameScreen postScreen;
	private Context context;
	private Resources resources;
	private ScheduledExecutorService timer;
	private int width, height;
	private int BOARD_SIZE = 7, LETTER_TRAY_SIZE = 7, COMPONENTS = 6, COMPONENT_ROWS = 5, PENALTY = 0;
	private String state;
	private ArrayList<String> wordList;
	private ArrayList<ScoreAnimation> scoreAnimations;
	private ArrayList<PenaltyAnimation> penaltyAnimations;
	private SoundPool sounds;
	private int timerTickFX, chaChingFX, shuffleFX, cashInFX;
	private boolean TIMER_RUNNING = false;

	private static final String TAG = MainView.class.getSimpleName();

	public Game(int screenW, int screenH, Context context, ArrayList<String> wordList){
		this.context = context;
		this.resources = context.getResources();
		this.width = screenW;
		this.height = screenH;
		this.wordList = wordList;
		restartGame();
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
				Bitmap tileSpaceImage;
				String tileName = getTileType(r, c);
				int resID = this.resources.getIdentifier(tileName, "drawable", "com.dylanbarth.wordmash");
				tileSpaceImage = BitmapFactory.decodeResource(resources, resID);
				TileSpace tile = new TileSpace(tileSpaceImage, r, c, imgWidth, imgHeight, tileName);
				tileSpaces[r][c] = tile;
			}
		}
		return tileSpaces;
	}

	public String getTileType(int r, int c){
		String tileType = "tile_space";
		if(c==1 && r==0 || c==5 && r==0 || c==0 && r==1 || c==6 && r==1 || c==0 && r==5 || c==6 && r==5 || c==1 && r==6 || c==5 && r==6){
			tileType = "tw_space";
		} else if(c==3 && r==2 || c==2 && r==3 || c==3 && r==4 || c==4 && r==3){
			tileType = "dw_space";
		} else if(c==3 && r==0 || c==0 && r==3 || c==6 && r==3 || c==3 && r==6){
			tileType = "tl_space";
		} else if(c==2 && r==1 || c==4 && r==1 || c==1 && r==2 || c==5 && r==2 || c==1 && r==4 || c==5 && r==4 || c==2 && r==5 || c==4 && r==5){
			tileType = "dl_space";
		}
		return tileType;
	}

	public LetterTray createLetterTray(){
		// Create tiles that make up letter tray array
		int boardWidth = this.board.getWidth();
		ArrayList<Tile> tray = new ArrayList<Tile>();
		for(int i=0; i < LETTER_TRAY_SIZE; i++){
			tray.add(createTile(i, boardWidth));
		}
		Bitmap backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.tray);
		LetterTray letterTray = new LetterTray(boardWidth, 0, tray, backgroundImage);
		letterTray.ensureVowel();
		return letterTray;
	}

	public Tile createTile(int index, int startX) {
		// return a tile at the appropriate space and index
		int imgWidth = this.width/(BOARD_SIZE*2);
		int imgHeight = this.height/BOARD_SIZE;
		Tile tile = new Tile(resources, imgWidth, imgHeight, index, startX);
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
		Bitmap menuBg = BitmapFactory.decodeResource(resources, R.drawable.menu_texture);
		GameMenu gameMenu = new GameMenu(x, y, gameMenuWidth, this.height, gameMenuComponents, menuBg);
		return gameMenu;
	}

	public Component[] createGameMenuComponents(int startX, int width){
		Component[] components = new Component[COMPONENTS];
		int passedWidth = width;
		for (int i=0; i < COMPONENTS; i++){
			String title = "";
			ArrayList<Bitmap> componentImages = new ArrayList<Bitmap>(); 
			int height = this.height/COMPONENT_ROWS, x = startX, y = height*i;
			boolean isButton = false;
			switch(i){
			case 0: 
				title = "logo";
				break;
			case 1:
				title = "score";
				width = passedWidth/2;
				break;
			case 2:
				title = "timer";
				x = startX+passedWidth/2;
				y = height*1;
				width = passedWidth/2;
				break;
			case 3:
				// new game, pause
				title = "changeGameState";
				y = height*2;
				width = passedWidth;
				isButton = true;
				break;
			case 4:
				// shuffle or recall tiles
				title = "updateTray";
				y = height*3;
				width = passedWidth;
				isButton = true;
				break;
			case 5:
				// allows exchange of tiles
				title = "newTiles";
				y = height*4;
				isButton = true;
				break;
			}
			if(title == "changeGameState"){
				componentImages.add(BitmapFactory.decodeResource(resources, R.drawable.new_game));
				componentImages.add(BitmapFactory.decodeResource(resources, R.drawable.pause));
			} else if(title == "updateTray"){
				componentImages.add(BitmapFactory.decodeResource(resources, R.drawable.clear));
				componentImages.add(BitmapFactory.decodeResource(resources, R.drawable.shuffle));
			} else if(title == "newTiles"){
				componentImages.add(BitmapFactory.decodeResource(resources, R.drawable.cashin));
			} else {
				int resID = this.resources.getIdentifier(title, "drawable", "com.dylanbarth.wordmash");
				componentImages.add(BitmapFactory.decodeResource(resources, resID));
			}
			Log.d(TAG, "*** Attempting to create " + title);
			Component component = new Component(title, x, y, width, height, isButton, componentImages);
			components[i] = component;
		}
		return components;
	}

	public PauseScreen createPauseScreen(){
		Bitmap background = BitmapFactory.decodeResource(resources, R.drawable.background);
		background = Bitmap.createScaledBitmap(background, this.width, this.height, true);

		Bitmap pauseIcon = BitmapFactory.decodeResource(resources, R.drawable.paused);
		pauseIcon = Bitmap.createScaledBitmap(pauseIcon, this.width/4, this.height/6, true);

		Bitmap resumeIcon = BitmapFactory.decodeResource(resources, R.drawable.resume);
		resumeIcon = Bitmap.createScaledBitmap(resumeIcon, this.width/4, this.height/6, true);

		Bitmap restartIcon = BitmapFactory.decodeResource(resources, R.drawable.restart);
		restartIcon = Bitmap.createScaledBitmap(restartIcon, this.width/4, this.height/6, true);

		Bitmap logo = BitmapFactory.decodeResource(resources, R.drawable.logo);
		logo = Bitmap.createScaledBitmap(logo, this.width/2, this.height/3, true);

		PauseScreen pausey = new PauseScreen(this.width, this.height, background, pauseIcon, resumeIcon, restartIcon, logo);
		return pausey;
	}

	public PostGameScreen createPostGameScreen(){
		Bitmap background = BitmapFactory.decodeResource(resources, R.drawable.background);
		background = Bitmap.createScaledBitmap(background, this.width, this.height, true);

		Bitmap restartIcon = BitmapFactory.decodeResource(resources, R.drawable.restart);
		restartIcon = Bitmap.createScaledBitmap(restartIcon, this.width/4, this.height/6, true);

		Bitmap logo = BitmapFactory.decodeResource(resources, R.drawable.logo);
		logo = Bitmap.createScaledBitmap(logo, this.width/2, this.height/3, true);

		PostGameScreen postScreen = new PostGameScreen(board.calculateScore(), this.width, this.height, background, restartIcon, logo);
		return postScreen;
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

	public SoundPool getSoundEffects(){
		SoundPool sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		// private int timerTickFX, chaChingFX, wordCreatedFX, shuffleFX, newGameStartedFX;

		timerTickFX = sounds.load(context, R.raw.shorttick, 0);
		chaChingFX = sounds.load(context, R.raw.chaching, 0);
//		tileDownFX = sounds.load(context, R.raw.tile_place, 0);
		shuffleFX = sounds.load(context, R.raw.shuffle, 0);
		cashInFX = sounds.load(context, R.raw.cashin, 0);
		return sounds;

	}
	
	public String getState(){
		return this.state;
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
		String boardState = tray.getState();
		if(newGameClicked() || menu.checkIfButtonClicked("newTiles")){
			boardState = "shuffle";
		}
		if(this.state == "preGame" || this.state == "inGame"){
			board.draw(canvas);
			menu.draw(canvas, this.state, boardState);
			tray.draw(canvas); // order matters - draw tiles last
			for(int i=0; i<scoreAnimations.size(); i++){
				scoreAnimations.get(i).draw(canvas);
			}
			for(int i=0; i<penaltyAnimations.size(); i++){
				penaltyAnimations.get(i).draw(canvas);
			}
		} else if (this.state == "paused"){
			pauseScreen.draw(canvas);
		} else if(this.state == "postGame"){
			postScreen.updateScore(board.calculateScore());
			try{
				postScreen.draw(canvas);	
			} catch(Exception e){
				// nothing
			}

		}

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
				if(gameState != "paused"){
					tray.deleteTilesInTray();
					addNewTiles();	
				}
				this.state = "inGame";
				menu.resetButtonClicked("changeGameState");
				startGameTimer();
			}
		} else if (gameState == "paused"){
			RestartButton restartButton = pauseScreen.getRestartButton();
			ResumeButton resumeButton = pauseScreen.getResumeButton();
			if (event == MotionEvent.ACTION_DOWN){
				if(restartButton.coordsInside(eventX, eventY)){
					restartButton.setTouched(true);
				} else if(resumeButton.coordsInside(eventX, eventY)){
					resumeButton.setTouched(true);
				}
			} else if (event == MotionEvent.ACTION_MOVE){
				if(!restartButton.coordsInside(eventX, eventY) && restartButton.isTouched()){
					restartButton.setTouched(false);
				} else if(!resumeButton.coordsInside(eventX, eventY) && resumeButton.isTouched()){
					resumeButton.setTouched(false);
				}
			} else if (event == MotionEvent.ACTION_UP){
				if(restartButton.isTouched() && restartButton.coordsInside(eventX, eventY)){
					restartGame();
					restartButton.setTouched(false);
				} else if(resumeButton.isTouched() && resumeButton.coordsInside(eventX, eventY)){
					startGameTimer();
					this.state = "inGame";
					resumeButton.setTouched(false);
				}
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
			com.dylanbarth.wordmash.model.PostGameScreen.RestartButton restartButton = postScreen.getRestartButton();
			if (event == MotionEvent.ACTION_DOWN){
				if(restartButton.coordsInside(eventX, eventY)){
					restartButton.setTouched(true);
				} 
			} else if (event == MotionEvent.ACTION_MOVE){
				if(!restartButton.coordsInside(eventX, eventY) && restartButton.isTouched()){
					restartButton.setTouched(false);
				} 
			} else if (event == MotionEvent.ACTION_UP){
				if(restartButton.isTouched() && restartButton.coordsInside(eventX, eventY)){
					restartGame();
					restartButton.setTouched(false);
				} 
			}
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
				pauseGame();
			} 
			menu.resetButtonClicked("changeGameState");
		} else if(menu.checkIfButtonClicked("newTiles")){
			// calculate penalty for cashing in
			this.penaltyAnimations.clear();
			PENALTY += tray.calculatePenalty();
			this.penaltyAnimations.addAll(tray.getPenaltyAnimations());
			sounds.play(cashInFX, 1f, 1f, 1, 0, 1f);
			int score = board.calculateScore();
			menu.getComponent("score").setScore(score-PENALTY);
			// lock current tiles to board
			tray.lockTilesOnBoard(); // also sets their index to 7
			// erase leftovers (maybe eventually also erase tiles that aren't part of valid words)
			tray.deleteTilesInTray(); // only deletes tiles that are currently in tray
			board.setTilesSpacesTilesToNull(tray.getInvalidTiles());
			tray.deleteInvalidTiles(); // only deletes tiles on board that are not part of a valid word
			// create new letter tiles
			addNewTiles();
			menu.resetButtonClicked("newTiles");
		} else if(menu.checkIfButtonClicked("updateTray")){
			if(tray.getTilesInTray().length == LETTER_TRAY_SIZE){
				tray.shuffleTiles();
				sounds.play(shuffleFX, 1f, 1f, 1, 0, 1f);
			} else {
				board.setTilesSpacesTilesToNull(tray.getUnlockedTiles());
				tray.clearTilesFromBoard();
				board.calculateScore();
			}
			menu.resetButtonClicked("updateTray");
		}
		Tile tile = tray.tileTouched();
		if(tile != null){
			this.scoreAnimations.clear();
			board.snapTileIntoPlace(tile);
			//			sounds.play(tileDownFX, 1f, 1f, 1, 0, 1f);
			tile.setTouched(false);
			tray.setAllTilesValidityToFalse();
			int score = board.calculateScore();
			this.scoreAnimations.addAll(board.getScoreAnimations());
			if(this.scoreAnimations.size() > 0){
				sounds.play(chaChingFX, 1f, 1f, 1, 0, 1f);
			}
			menu.getComponent("score").setScore(score-PENALTY);
			// calculate score
		}
	}

	/*************************
	 * postGame methods * 
	 *************************/

	/*************************
	 * pausedGame methods * 
	 * @return 
	 *************************/

	public void restartGame(){
		// TODO: Play loading animation!
		this.board = createBoard(0, 0, BOARD_SIZE*BOARD_SIZE, wordList);
		this.tray = createLetterTray();
		this.menu = createGameMenu();
		this.state = "preGame";
		this.scoreAnimations = new ArrayList<ScoreAnimation>();
		this.penaltyAnimations = new ArrayList<PenaltyAnimation>();
		this.sounds = getSoundEffects();
		this.pauseScreen = createPauseScreen();
		this.postScreen = createPostGameScreen();
	}
	
	public void pauseGame(){
		pauseGameTimer();
		this.state = "paused";
	}


	/*************************
	 * timer methods * 
	 *************************/
	public void startGameTimer(){
		TIMER_RUNNING = true;
		timer = Executors.newSingleThreadScheduledExecutor();
		Runnable startTimer = new Runnable() {
			public void run() {
				Component timerComponent = menu.getComponent("timer");
				timerComponent.subtractTime();
				sounds.play(timerTickFX, 1f, 1f, 1, 0, 1f);
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
