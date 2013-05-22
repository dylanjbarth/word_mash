package com.example.scrabblish.model;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Canvas;
import android.util.Log;

import com.example.scrabblish.MainView;

public class LetterTray {
	private int x, y, width;
	private ArrayList<Tile> tray;
	private static final String TAG = MainView.class.getSimpleName();

	public LetterTray(int x, int y, ArrayList<Tile> tray){
		this.x = x;
		this.y = y;
		this.tray = tray;
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

	public int getSize(){
		return this.tray.size();
	}

	public int getWidth(){
		Tile tile = getTileFromTray(0);
		this.width = tile.getWidth();
		return this.width;
	}

	public ArrayList<Tile> getTray(){
		return this.tray;
	}

	public Tile getTileFromTray(int index){
		return this.tray.get(index);
	}

	public void setAllTilesValidityToFalse() {
		ArrayList<Tile> tray = getTray();
		for(int i=0; i<tray.size(); i++){
			tray.get(i).setValidity(false);
		}
	}

	public Tile[] getTilesInTray(){
		ArrayList<Tile> tiles = getTray();
		ArrayList<Tile> tilesInTray = new ArrayList<Tile>();
		for(int i=0; i<tiles.size(); i++){
			if(tiles.get(i).getX() == tiles.get(i).getResetX()){
				tilesInTray.add(tiles.get(i));
			}
		}
		Tile[] tileInTrayArray = new Tile[tilesInTray.size()];
		tileInTrayArray = tilesInTray.toArray(tileInTrayArray);
		return tileInTrayArray;
	}
	
	public Tile[] getTilesOnBoard(){
		ArrayList<Tile> tiles = getTray();
		ArrayList<Tile> tilesInTray = new ArrayList<Tile>();
		for(int i=0; i<tiles.size(); i++){
			if(tiles.get(i).getX() != tiles.get(i).getResetX()){
				tilesInTray.add(tiles.get(i));
			}
		}
		Tile[] tileInTrayArray = new Tile[tilesInTray.size()];
		tileInTrayArray = tilesInTray.toArray(tileInTrayArray);
		return tileInTrayArray;
	}
	
	public Tile[] getInvalidTiles(){
		ArrayList<Tile> invalidTiles = new ArrayList<Tile>();
		Tile[] tilesOnBoard = getTilesOnBoard();
		for(int i=0; i<tilesOnBoard.length; i++){
			Tile tile = tilesOnBoard[i];
			if(!tile.isValid()){
				invalidTiles.add(tile);
//				Log.d(TAG, "Added to invalidTiles: " + tile.getLetter());
			}
		}
		Tile[] invalidTileArray = new Tile[invalidTiles.size()];
		invalidTileArray = invalidTiles.toArray(invalidTileArray);
//		Log.d(TAG, "Invalid tileArray length: " + invalidTileArray.length);
		return invalidTileArray;
	}
	
	public Tile[] getPenaltyTiles(){
		ArrayList<Tile> penaltyTiles = new ArrayList<Tile>();
		Tile[] tilesInTray = getTilesInTray();
		Tile[] invalidTiles = getInvalidTiles();
		for(int i=0; i<tilesInTray.length; i++){
			penaltyTiles.add(tilesInTray[i]);
		}
		for(int i=0; i<invalidTiles.length; i++){
			penaltyTiles.add(invalidTiles[i]);
		}
		Tile[] penaltyTilesArray = new Tile[penaltyTiles.size()];
		penaltyTilesArray = penaltyTiles.toArray(penaltyTilesArray);
		return penaltyTilesArray;
	}
	
	public Tile[] getUnlockedTiles(){
		ArrayList<Tile> unlockedTiles = new ArrayList<Tile>();
		Tile[] tilesOnBoard = getTilesOnBoard();
		for(int i=0; i<tilesOnBoard.length; i++){
			Tile tile = tilesOnBoard[i];
			if(!tile.isLocked()){
				unlockedTiles.add(tile);
			}
		}
		Tile[] unlockedTilesArray = new Tile[unlockedTiles.size()];
		unlockedTilesArray = unlockedTiles.toArray(unlockedTilesArray);
		return unlockedTilesArray;
	}

	/*************************
	 * Helpers * 
	 *************************/

	public void draw(Canvas canvas){
		// iterates through all tile objects and draws them, touched tile last
		Tile touched = null;
		for(int i=0; i<getSize(); i++){
			Tile tile = this.tray.get(i);
			if(!tile.isTouched()){
				tile.draw(canvas);
			} else {
				touched = tile; 
			}
		}
		if (touched != null){ 
			touched.draw(canvas); // for now
			//			this.getTileFromTray(touchedIndex).drawBig(canvas); // for later
		}
	}

	public void handleActionDown(int eventX, int eventY){
		// iterates through all tile objects and asks them to handle the individual action down event
		for(int r=0; r < getSize(); r++){
			Tile tile = this.getTileFromTray(r);
			tile.handleActionDown(eventX, eventY);
		}
	}

	public Tile tileTouched(){
		// iterates through all tile objects and returns the object that is currently touched
		for(int r=0; r < getSize(); r++){
			Tile tile = this.getTileFromTray(r);
			if (tile.isTouched()){
				return tile;
			}
		}
		return null;
	}

	public void ensureVowel() {
		Tile[] tray = getTilesInTray();
		boolean hasVowel = false;
		for(int i=0; i<tray.length; i++){
			String l = tray[i].getLetter();
			if(l == "A" || l == "E" || l == "I" || l == "O" || l == "U" || l == "Y"){
				hasVowel = true;
			}
		}
		if(!hasVowel){
			Tile tile = tray[(int) Math.random()*tray.length]; // pick tile at Random
			int whichLetter = (int) Math.random()*6;
			switch(whichLetter){
			case 0:
				tile.setLetter("A");
			case 1:
				tile.setLetter("E");
			case 2:
				tile.setLetter("I");
			case 3:
				tile.setLetter("O");
			case 4:
				tile.setLetter("U");
			case 5:
				tile.setLetter("Y");
			}
		}
	}

	public int calculatePenalty(){
		int penalty = 0; 
		Tile[] penaltyTiles = getPenaltyTiles();
		for(int i=0; i<penaltyTiles.length; i++){
			penalty += penaltyTiles[i].getValue();
		}
		return penalty;
	}
	
	public void lockTilesOnBoard(){
		Tile[] tilesOnBoard = getTilesOnBoard();
		for(int i=0; i<tilesOnBoard.length; i++){
			Tile tile = tilesOnBoard[i];
			tile.setLocked(true);
//			Log.d(TAG, "Locked tile letter: " + tilesOnBoard[i].getLetter());
		}
	}
	
	public void deleteTilesInTray(){
		Tile[] tiles = getTilesInTray();
		for(int i=0; i < tiles.length; i++){
			this.tray.remove(tiles[i]);
		}
	}
	
	public void deleteInvalidTiles(){
		Tile[] invalidTiles = getInvalidTiles();
		for(int i=0; i<invalidTiles.length; i++){
			this.tray.remove(invalidTiles[i]);
		}
	}

	public void shuffleTiles() {
		Tile[] tiles = getTilesInTray();
		Random rgen = new Random();
		for(int i=0; i<tiles.length; i++){
			int randomPosition = rgen.nextInt(tiles.length);
			Tile toSwitch = tiles[i];
			tiles[i] = tiles[randomPosition];
			tiles[randomPosition] = toSwitch;
		}	
		for(int i=0; i<tiles.length; i++){
			tiles[i].setShuffle(i); // changes their position based on index
		}
		
	}

	public void clearTilesFromBoard() {
		Tile[] tiles = getTilesOnBoard();
		for(int i=0; i<tiles.length; i++){
			Tile tile = tiles[i];
			if(!tile.isLocked()){
				tile.setValidity(false);
				tile.resetPosition();
			}
		}
	}
}
