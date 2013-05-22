package com.example.scrabblish.model;

import java.util.ArrayList;

import android.graphics.Canvas;

import com.example.scrabblish.MainView;

public class LetterTray {
	private int x, y, width, size;
	private Tile[] tray;
	private static final String TAG = MainView.class.getSimpleName();

	public LetterTray(int x, int y, int size, Tile[] tray){
		this.x = x;
		this.y = y;
		this.size = size;
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
		return size;
	}

	public int getWidth(){
		Tile tile = getTileFromTray(0);
		this.width = tile.getWidth();
		return this.width;
	}

	public Tile[] getTray(){
		return this.tray;
	}

	public Tile getTileFromTray(int index){
		return this.tray[index];
	}

	public void setAllTilesValidityToFalse() {
		Tile[] tray = getTray();
		for(int i=0; i<tray.length; i++){
			tray[i].setValidity(false);
		}
	}

	public Tile[] getTilesInTray(){
		Tile[] tiles = getTray();
		ArrayList<Tile> tilesInTray = new ArrayList<Tile>();
		for(int i=0; i<tiles.length; i++){
			if(tiles[i].getX() == tiles[i].getResetX()){
				tilesInTray.add(tiles[i]);
			}
		}
		Tile[] tileInTrayArray = new Tile[tilesInTray.size()];
		tileInTrayArray = tilesInTray.toArray(tileInTrayArray);
		return tileInTrayArray;
	}

	/*************************
	 * Helpers * 
	 *************************/

	public void draw(Canvas canvas){
		// iterates through all tile objects and draws them, touched tile last
		int touchedIndex = 7;
		for(int r=0; r < size; r++){
			Tile tile = this.getTileFromTray(r);
			if(!tile.isTouched()){
				tile.draw(canvas);
			} else {
				touchedIndex = r; 
			}
		}
		if (touchedIndex < 7){ // this is a total hack
			this.getTileFromTray(touchedIndex).draw(canvas); // for now
			//			this.getTileFromTray(touchedIndex).drawBig(canvas); // for later
		}
	}

	public void handleActionDown(int eventX, int eventY){
		// iterates through all tile objects and asks them to handle the individual action down event
		for(int r=0; r < size; r++){
			Tile tile = this.getTileFromTray(r);
			tile.handleActionDown(eventX, eventY);
		}
	}

	public Tile tileTouched(){
		// iterates through all tile objects and returns the object that is currently touched
		for(int r=0; r < size; r++){
			Tile tile = this.getTileFromTray(r);
			if (tile.isTouched()){
				return tile;
			}
		}
		return null;
	}

	public void ensureVowel() {
		Tile[] tray = getTray();
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
		Tile[] tilesInTray = getTilesInTray();
		for(int i=0; i<tilesInTray.length; i++){
			penalty += tilesInTray[i].getValue();
		}
		return penalty;
	}
}
