package com.dylanbarth.wordmash.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.dylanbarth.wordmash.MainView;


@SuppressLint("NewApi")
public class Board {
	private int x, y, size, width, height, gridLength;
	private TileSpace[][] tileSpaces;
	private static final String TAG = MainView.class.getSimpleName();
	private ArrayList<String> masterWordList, oldWords;
	private ArrayList<Animation> animations;

	public Board(int x, int y, int size, TileSpace[][] tileSpaces, ArrayList<String> wordList){
		this.x = x;
		this.y = y;
		this.size = size; // 7 default
		this.gridLength = (int) Math.sqrt(size);
		this.tileSpaces = tileSpaces; // organized by [col][row] somehow :(
		this.width = boardWidth();
		this.masterWordList = wordList;
		this.oldWords = new ArrayList<String>();
		this.animations = new ArrayList<Animation>();
		//		this.height = boardHeight(); // for some reason this is skewing board... not important for now.
	}

	/*************************
	 * Getters * 
	 *************************/

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public int getHeight(){
		return height;
	}

	public int getWidth(){
		return width;
	}

	public int getSize(){
		return size;
	}
	
	public ArrayList<Animation> getAnimations(){
		return animations;
	}

	public TileSpace getTileSpace(int row, int col){
		TileSpace tileSpace = tileSpaces[row][col];
		return tileSpace;
	}

	public TileSpace getCenterTileSpace(){
		int center = ((int) Math.sqrt(this.size)/2);
		//		Log.d(TAG, "CENTER TILE: " + center);
		return getTileSpace(center, center);
	}

	public TileSpace[][] getAllTileSpaces(){
		return tileSpaces;
	}

	public TileSpace getClosestAvailableTileSpace(int tileCenterX, int tileCenterY){
		// Return the closest unoccupied tileSpace
		//		Log.d(TAG, "returning closest unoccupied tileSpace");
		TreeMap<Integer, int[]> freeSpaces = new TreeMap<Integer, int[]>(); // { distance : {row, col} }, naturally ordered
		TileSpace[][] tileSpaces = this.getAllTileSpaces(); 
		// create tree map 
		for(int r=0; r < Math.sqrt(size); r++){
			for (int c=0; c < Math.sqrt(size); c++){
				TileSpace tileSpace = tileSpaces[r][c];
				if(!tileSpace.isOccupied()){
					int tileSpaceX = tileSpaces[r][c].getX();
					int tileSpaceY = tileSpaces[r][c].getY();
					int distanceBetween = (int) Math.sqrt((Math.pow(Math.abs(tileCenterX-tileSpaceX), 2)) + (Math.pow(Math.abs(tileCenterY-tileSpaceY), 2)));
					int[] coords = {r, c};
					freeSpaces.put(distanceBetween, coords);
				}
			}
//			int[] current_leader = (int[]) freeSpaces.firstEntry().getValue();
			//			Log.d(TAG, "After row:" + r + " leader is (" + current_leader[0] + ", " + current_leader[1]+")");
		}
		int[] coords = (int[]) freeSpaces.firstEntry().getValue();
		TileSpace freedom = tileSpaces[coords[0]][coords[1]];
		return freedom;
	}

	/*************************
	 * Setters * 
	 *************************/

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	public void setSize(int size){
		this.size = size;
	}

	public void setTileSpace(int row, int col, TileSpace tile){
		this.tileSpaces[row][col] = tile;
	}

	/*************************
	 * Helpers * 
	 *************************/

	public int boardWidth(){
		int width = 0;
		for(int c=0; c < Math.sqrt(size); c++){
			Object tileSpace = this.getTileSpace(0, c);
			width += ((TileSpace) tileSpace).getWidth();
		}
		return width;
	}

	public int boardHeight(){
		int height = 0;
		for(int r=0; r < Math.sqrt(size); r++){
			Object tileSpace = this.getTileSpace(r, 0);
			height += ((TileSpace) tileSpace).getHeight();
		}
		return height;
	}

	public void draw(Canvas canvas){
		// iterates through tileSpaces and draws them
		for(int r=0; r < Math.sqrt(size); r++){
			for (int c=0; c < Math.sqrt(size); c++){
				TileSpace tileSpace = this.getTileSpace(r, c);
				tileSpace.draw(canvas);
			}
		}
		//temporary
		int centerX = getCenterTileSpace().getX();
		int centerY = getCenterTileSpace().getY();
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		canvas.drawText("C", centerX+25, centerY+40, paint);
	}

	public void snapTileIntoPlace(Tile tile){
		// snaps tile into place
		//		Log.d(TAG, "attempting to snap tile into place for tile:" + tile.getIndex());
		boolean snapped = false;
		A: for(int r=0; r < Math.sqrt(size); r++){
			for (int c=0; c < Math.sqrt(size); c++){
				TileSpace tileSpace = this.getTileSpace(r, c);
				TileSpace snappedTileSpace = tileSpace.handleTileSnapping(this, tile);
				if(snappedTileSpace != null){
					snappedTileSpace.setTile(tile);
					snapped = true;
					break A;
				}
//				Log.d(TAG, "Snapped tile:" + tile.getIndex() + " into tileSpace:" + snappedTileSpace.getRow() + "," + snappedTileSpace.getCol());
			}
		}
		if(!snapped){
			tile.resetPosition();
		}
	}

	public void handleMovingTiles(int eventX, int eventY){
		TileSpace[][] tileSpaces = this.getAllTileSpaces();
		for(int r=0; r < Math.sqrt(size); r++){
			for (int c=0; c < Math.sqrt(size); c++){
				TileSpace tileSpace = tileSpaces[r][c];
				if(tileSpace.coordsInside(eventX, eventY)){
					Tile currentTile = tileSpace.getCurrentTile();
					boolean locked = false;
					if(currentTile != null){
						currentTile.setCurrentTileSpace(null);
						locked = currentTile.isLocked();
					} 
					if(!locked){
						tileSpace.setOccupied(false);
						tileSpace.setTile(null);
					}
				}
			}
		}
	}

	public ArrayList<ArrayList<Tile>> getAllWords(){
		// goes through each row and column, adding every sequence greater than 2 to a string array
		ArrayList<ArrayList<Tile>> currentWords = new ArrayList<ArrayList<Tile>>();
		// first loop horizontal, second is vertical
		for(int t=0; t < 2; t++){
			if(t==0){
				Log.d(TAG, "Horizontal loop");
			} else {
				Log.d(TAG, "Vertical loop");
			}
			for(int r=0; r < this.gridLength; r++){
				ArrayList<Tile> word = new ArrayList<Tile>();
				for (int c=0; c < this.gridLength; c++){
					TileSpace tileSpace;
					if(t==0){
						tileSpace = tileSpaces[c][r];
					} else {
						tileSpace = tileSpaces[r][c];
					}
					Tile tile = tileSpace.getCurrentTile();
					if(tile != null){
						word.add(tile);
//						Log.d(TAG, "Added letter: " + tile.getLetter() + " to word");
					} 
					if(tile == null || c>=this.gridLength-1){
						if(word.size() > 1){
//							Log.d(TAG, "Added word: " + word);
							currentWords.add(word);
						}
						word = new ArrayList<Tile>();
					}
				}
			}
			Log.d(TAG, "t=" + t + ", words=");
		}
		createAnimations(currentWords);
		return currentWords;
	}
	
	public void createAnimations(ArrayList<ArrayList<Tile>> currentWords){
		ArrayList<String> currentWordStrings = new ArrayList<String>();
		for(int i=0; i<currentWords.size(); i++){
			ArrayList<Tile> tileWord = currentWords.get(i);
			String word = getWord(tileWord);
			currentWordStrings.add(word);
			if(!oldWords.contains(word)){
				for(int j=0; j<tileWord.size(); j++){
					Animation animation = new Animation(tileWord.get(j).getTileSpace());
					this.animations.add(animation);
				}
			}
			Log.d(TAG, word);
		}
		oldWords = currentWordStrings;
	}

	public int calculateScore(){
		this.animations = new ArrayList<Animation>();
		int score = 0;
		showLetters();
		showTiles(); 
		ArrayList<ArrayList<Tile>> words = getAllWords();
		ArrayList<ArrayList<Tile>> validTiles = new ArrayList<ArrayList<Tile>>();
		for(int i=0; i < words.size(); i++){
			String word = getWord(words.get(i));
//			Log.d(TAG, "Retrieved string: " + word);
			if(wordIsValid(word)){
//				Log.d(TAG, word + " is a valid word.");
				score += calcWordScore(words.get(i));
				validTiles.add(words.get(i));
			} else {
//				Log.d(TAG, word + " is invalid. Setting tiles to false.");
			}
		}
		for(int i=0; i < validTiles.size(); i++){
			setTilesValidity(validTiles.get(i), true);
		}
//		Log.d(TAG, "***SCORE*** == " + score);
		return score;
	}

	public int calcWordScore(ArrayList<Tile> word){
		int score = 0;
		int wordMultiplier = 1;
		for(int i=0; i < word.size(); i++){
			Tile tile = word.get(i);
			String tileType = tile.getTileSpace().getTileType();
			int letterValue = returnLetterValue(tile.getLetter()); 
			if(tileType == "tw_space"){
				wordMultiplier *= 3;
			} else if (tileType == "dw_space"){
				wordMultiplier *= 2;
			} else if (tileType == "tl_space"){
				letterValue *= 3;
			} else if (tileType == "dl_space"){
				letterValue *= 2;
			}
			score += letterValue;
//			Log.d(TAG, "End of wordScoreLoop:" + i);
//			Log.d(TAG, "tileLetter:" + word.get(i).getLetter());
//			Log.d(TAG, "tileSpaceType: " + tileType);
			
		}
		score *= wordMultiplier;
		return score;
	}
	
	

	public String getWord(ArrayList<Tile> word){
		String text = "";
		for(int i=0; i < word.size(); i++){
			text += word.get(i).getLetter();
		}
		return text;
	}

	public void setTilesValidity(ArrayList<Tile> tiles, boolean validity){
		for(int i=0; i < tiles.size(); i++){
			tiles.get(i).setValidity(validity);
		}
	}

	public int returnLetterValue(String c){
		HashMap<String, Integer> tileValues = new HashMap<String, Integer>();
		tileValues.put("A", 1);
		tileValues.put("B", 3);
		tileValues.put("C", 3);
		tileValues.put("D", 2);
		tileValues.put("E", 1);
		tileValues.put("F", 4);
		tileValues.put("G", 2);
		tileValues.put("H", 4);
		tileValues.put("I", 1);
		tileValues.put("J", 8);
		tileValues.put("K", 5);
		tileValues.put("L", 1);
		tileValues.put("M", 3);
		tileValues.put("N", 1);
		tileValues.put("O", 1);
		tileValues.put("P", 3);
		tileValues.put("Q", 10);
		tileValues.put("R", 1);
		tileValues.put("S", 1);
		tileValues.put("T", 1);
		tileValues.put("U", 1);
		tileValues.put("V", 1);
		tileValues.put("W", 4);
		tileValues.put("X", 8);
		tileValues.put("Y", 4);
		tileValues.put("Z", 10);
		int value = (Integer) tileValues.get(c);
		return value;
	}

	public boolean wordIsValid(String word){
		boolean valid = false;
		if(masterWordList.contains(word.toLowerCase())){
			//			Log.d(TAG, word + " is a valid word!");
			valid = true;
		}
		return valid;
	}

	public void showOccupation(){
		TileSpace[][] tileSpaces = this.getAllTileSpaces();
		for(int r=0; r < this.gridLength; r++){
			boolean[] row = new boolean[(int) this.gridLength];
			for (int c=0; c < this.gridLength; c++){
				TileSpace tileSpace = tileSpaces[c][r];
				row[c] = tileSpace.isOccupied();
			}
			Log.d(TAG, "R" + r + ": " + Arrays.toString(row));
		}
	}

	public void showLetters(){
		TileSpace[][] tileSpaces = this.getAllTileSpaces();
		for(int r=0; r < this.gridLength; r++){
			String[] row = new String[(int) this.gridLength];
			for (int c=0; c < this.gridLength; c++){
				TileSpace tileSpace = tileSpaces[c][r];
				Tile tile = tileSpace.getCurrentTile();
				String letter = "";
				if(tile != null){
					letter = tile.getLetter();
				} else {
					letter = " ";
				}
				row[c] = letter;
			}
			Log.d(TAG, "R" + r + ": " + Arrays.toString(row));
		}
	}

	public void showTiles(){
		TileSpace[][] tileSpaces = this.getAllTileSpaces();
		for(int r=0; r < this.gridLength; r++){
			int[] row = new int[this.gridLength];
			for (int c=0; c < this.gridLength; c++){
				TileSpace tileSpace = tileSpaces[c][r];
				Tile tile = tileSpace.getCurrentTile();
				int index = 7;
				if(tile != null){
					index = tile.getIndex();
				}
				row[c] = index;
			}
			Log.d(TAG, "R" + r + ": " + Arrays.toString(row));
		}
	}

	public void setTilesSpacesTilesToNull(Tile[] invalidTiles) {
		TileSpace[][] tileSpaces = this.getAllTileSpaces();
		for(int r=0; r < Math.sqrt(size); r++){
			for (int c=0; c < Math.sqrt(size); c++){
				Tile currentTile = tileSpaces[c][r].getCurrentTile();
//				Log.d(TAG, "Current tile: " + currentTile);
				if(Arrays.asList(invalidTiles).contains(currentTile)){
//					Log.d(TAG, "Set tilespace tile to null");
					tileSpaces[c][r].setTile(null);
					tileSpaces[c][r].setOccupied(false);
				}
			}
		}
	}
}
