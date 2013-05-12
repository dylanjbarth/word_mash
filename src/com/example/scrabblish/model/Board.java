package com.example.scrabblish.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.scrabblish.MainView;


@SuppressLint("NewApi")
public class Board {
	private int x, y, size, width, height, gridLength;
	private TileSpace[][] tileSpaces;
	private static final String TAG = MainView.class.getSimpleName();

	public Board(int x, int y, int size, TileSpace[][] tileSpaces){
		this.x = x;
		this.y = y;
		this.size = size; // 7 default
		this.gridLength = (int) Math.sqrt(size);
		this.tileSpaces = tileSpaces; // organized by [col][row] somehow :(
		this.width = boardWidth();
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
		Log.d(TAG, "returning closest unoccupied tileSpace");
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
			int[] current_leader = (int[]) freeSpaces.firstEntry().getValue();
			Log.d(TAG, "After row:" + r + " leader is (" + current_leader[0] + ", " + current_leader[1]+")");
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
		Log.d(TAG, "attempting to snap tile into place for tile:" + tile.getIndex());
		boolean snapped = false;
		A: for(int r=0; r < Math.sqrt(size); r++){
			for (int c=0; c < Math.sqrt(size); c++){
				TileSpace tileSpace = this.getTileSpace(r, c);
				if (tileSpace.handleTileSnapping(this, tile)){
					tileSpace.setTile(tile);
					snapped = true;
					break A;
				}
			}
		}
		// or resets its position
		if (!snapped){
			tile.resetPosition();
		}
	}

	public void handleMovingTiles(int eventX, int eventY){
		TileSpace[][] tileSpaces = this.getAllTileSpaces();
		for(int r=0; r < Math.sqrt(size); r++){
			for (int c=0; c < Math.sqrt(size); c++){
				TileSpace tileSpace = tileSpaces[r][c];
				if(tileSpace.coordsInside(eventX, eventY)){
					tileSpace.setOccupied(false);
					tileSpace.setTile(null);
				}
			}
		}
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

	public String[] getAllWords(){
		// goes through each row and column, adding every sequence greater than 2 to a string array
		ArrayList<String> allWords = new ArrayList<String>();
		// horizontally
		for(int r=0; r < this.gridLength; r++){
			String word = "";
			for (int c=0; c < this.gridLength; c++){
				TileSpace tileSpace = tileSpaces[c][r];
				Tile tile = tileSpace.getCurrentTile();
				if(tile != null){
					word += tile.getLetter();
				} 
				if(tile == null || r==this.gridLength-1){
					if(word.length() > 1){
						allWords.add(word);
					}
					word = "";
				}
			}
		}
		// vertically
		for(int c=0; c < this.gridLength; c++){
			String word = "";
			for (int r=0; r < this.gridLength; r++){
				TileSpace tileSpace = tileSpaces[c][r];
				Tile tile = tileSpace.getCurrentTile();
				if(tile != null){
					word += tile.getLetter();
				} 
				if(tile == null || c==this.gridLength-1){
					if(word.length() > 1){
						allWords.add(word);
					}
					word = "";
				}
			}
		}
		String[] words = allWords.toArray(new String[allWords.size()]);
		return words;
	}

	public int calculateScore(){
		// TODO
		// Think might be on to something here.. 
		// use show letters to calc a grid of letters. Check for words horizontally and vertically. 
		// Then look them up in dictionary
		// If valid, add them to score
		int score = 0;
		showLetters();
		String[] words = getAllWords();
		Log.d(TAG, words.toString());
		for(int i=0; i < words.length; i++){
			Log.d(TAG, words[i]);
			score += calcWordScore(words[i]);
		}
		return score;
	}
	
	public int calcWordScore(String word){
		int score = 0;
		for(int i=0; i < word.length(); i++){
			score += returnLetterValue(word.charAt(i));
			Log.d(TAG, "***SCORE*** == " + score);
		}
		Log.d(TAG, "***SCORE*** == " + score);
		return score;
	}
	
	public int returnLetterValue(char c){
		HashMap<Character, Integer> tileValues = new HashMap<Character, Integer>();
		tileValues.put('A', 1);
		tileValues.put('B', 3);
		tileValues.put('C', 3);
		tileValues.put('D', 2);
		tileValues.put('E', 1);
		tileValues.put('F', 4);
		tileValues.put('G', 2);
		tileValues.put('H', 4);
		tileValues.put('I', 1);
		tileValues.put('J', 8);
		tileValues.put('K', 5);
		tileValues.put('L', 1);
		tileValues.put('M', 3);
		tileValues.put('N', 1);
		tileValues.put('O', 1);
		tileValues.put('P', 3);
		tileValues.put('Q', 10);
		tileValues.put('R', 1);
		tileValues.put('S', 1);
		tileValues.put('T', 1);
		tileValues.put('U', 1);
		tileValues.put('V', 1);
		tileValues.put('W', 4);
		tileValues.put('X', 8);
		tileValues.put('Y', 4);
		tileValues.put('Z', 10);
		int value = (Integer) tileValues.get(c);
		return value;
	}
}
