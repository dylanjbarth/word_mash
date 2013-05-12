package com.example.scrabblish.model;

import java.util.ArrayList;
import java.util.Arrays;
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
			//			score += calcWordScore(words[i]);
		}
		return score;
	}

	//	public int calculateScore() {
	//		showOccupation();
	//		ArrayList<ArrayList<int[]>> allWordCoords = new ArrayList<ArrayList<int[]>>();
	//		// Start in top left
	//		int[] position = {0, 0};
	//		// this loop checks for all horizontal words
	////		Log.d(TAG, "Starting calcScore horizontal loop");
	//		for(int i=0; i<this.gridLength; i++){
	//			position[0] = i; // rows
	////			Log.d(TAG, "Position: " + position[0] + ", " + position[1]);
	//			for(int j=0; j<this.gridLength; j++){
	////				Log.d(TAG, "i:" + i + ", j:" + j);
	//				Log.d(TAG, "Position about to be checked: " + position[0] + ", " + position[1]);
	//				ArrayList<int[]> wordCoords = checkForWord("horizontal", position[0], position[1]);
	////				Log.d(TAG, "Got wordCoords from checkForWord");
	//				if(wordCoords != null){
	////					Log.d(TAG, "wordCoords != null");
	//					allWordCoords.add(wordCoords);
	////					Log.d(TAG, "Added wordCoords to master arrayList");
	//					int[] lastElement = wordCoords.get(wordCoords.size() - 1);
	////					Log.d(TAG, "lastElement: " + lastElement[1] + ", " + lastElement[0]);
	//					j = lastElement[0]+1;
	//					position[1] = j;
	//				} else {
	////					Log.d(TAG, "wordCoords = null");
	//					position[1] = j;
	//				}
	//			}
	//		}
	//		//TODO
	//		// add a loop that checks for vertical words
	////		Log.d(TAG, "Starting calcScore vertical loop");
	////		for(int i=0; i<this.size; i++){
	////			position[1] = i; // cols
	////			Log.d(TAG, "Position: " + position[0] + ", " + position[1]);
	////			for(int j=0; j<this.size; j++){
	////				Log.d(TAG, "i: " + i + ", j: " + j);
	////				ArrayList<int[]> wordCoords = checkForWord("vertical", position[1], position[0]);
	////				Log.d(TAG, "Got wordCoords from checkForWord");
	////				if(wordCoords != null){
	////					Log.d(TAG, "wordCoords = null");
	////					allWordCoords.add(wordCoords);
	////					Log.d(TAG, "Added wordCoords to master arrayList");
	////					j = wordCoords.get(wordCoords.size() - 1)[1];
	////					position[0] = j;
	////				} else {
	////					Log.d(TAG, "wordCoords != null");
	////					position[0] = j + 1;
	////				}
	////				Log.d(TAG, "Incremented j: " + j);
	////			}
	////		}
	//		// if valid words, add their score to total
	//		// if not valid, make all of those tilespaces a weird color and don't summate that score.
	//		int score = 0;
	//		Log.d(TAG, "All word coords: ");
	//		for(int i=0; i < allWordCoords.size(); i++){
	//			ArrayList<int[]> wordCoords = allWordCoords.get(i);
	//			String coords = "";
	//			for(int j=0; j < wordCoords.size(); j++){
	//				coords += "(" + wordCoords.get(j)[0] + ", " + wordCoords.get(j)[1] + "), ";
	//			}
	//			Log.d(TAG, coords);
	//		}
	//		return score;
	//	}
	//
	//	public ArrayList<int[]> checkForWord(String direction, int row, int col){
	//		// return an array list of coords if a word exists, otherwise return null
	////		Log.d(TAG, "CheckForWord - direction: " + direction + ", coords:" + row + ", " + col);
	//		TileSpace tileSpace;
	//		ArrayList<int[]> wordCoords = null;
	//		try {
	//			tileSpace = getTileSpace(row, col);
	//		} catch (Exception e){
	//			return null;
	//		}
	//		if(tileSpace.isOccupied()){
	//			Log.d(TAG, "TileSpace is occupied");
	//			wordCoords = new ArrayList<int[]>();
	//			while(tileSpace.isOccupied()){
	//				int[] position = {row, col};
	//				wordCoords.add(position);
	//				Log.d(TAG, "Coords (" + row + ", " + col + ") added to wordCoords.");
	//				if(direction == "horizontal"){
	//					row++;
	//				} else if (direction == "vertical"){
	//					col++;
	//				}
	////				Log.d(TAG, "Checking next space at " + row + ", " + col);
	//				try {
	//					tileSpace = getTileSpace(row, col);
	//				} catch (Exception e){
	//					break; // indexed outside of tileSpace
	//				}
	//			}
	//		} 
	////		Log.d(TAG, "Got outside of the whileLoop");
	//		if(wordCoords != null && wordCoords.size() > 1){
	////			Log.d(TAG, "Returning words coords");
	////			try {
	////				printWord(wordCoords);
	////			} catch (Exception e){
	////				// do nothing
	////			}
	//			String coords = "";
	//			for(int i=0; i<wordCoords.size(); i++){
	//				coords += "(" + wordCoords.get(i)[0] + ", " + wordCoords.get(i)[1] + ") ";
	//			}
	//			Log.d(TAG, "Coords to be returned: " + coords);
	//			return wordCoords;
	//		} else {
	////			Log.d(TAG, "Returning null");
	//			return null;
	//		}
	//
	//	}
	//
	//	public void printWord(ArrayList<int[]> wordCoords){
	//		String word = "";
	//		for(int[] i : wordCoords){
	//			TileSpace tileSpace = getTileSpace(i[0], i[1]);
	//			word += tileSpace.getCurrentTile().getLetter();
	//		}
	//		Log.d(TAG, "Word created: " + word);
	//	}
}
