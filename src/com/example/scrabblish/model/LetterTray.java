package com.example.scrabblish.model;

import com.example.scrabblish.MainView;

import android.graphics.Canvas;
import android.view.SurfaceView;

public class LetterTray {
	private int x;
	private int y;
	private int width;
	private int size;
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
}
