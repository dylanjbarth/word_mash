package com.example.scrabblish.model;

import java.util.Arrays;
import java.util.TreeMap;

import com.example.scrabblish.MainView;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;


@SuppressLint("NewApi")
public class Board {
	private int x;
	private int y;
	private int size;
	private int width;
	private int height;
	private TileSpace[][] tileSpaces;
	private static final String TAG = MainView.class.getSimpleName();

	public Board(int x, int y, int size, TileSpace[][] tileSpaces){
		this.x = x;
		this.y = y;
		this.size = size;
		this.tileSpaces = tileSpaces;
		this.width = boardWidth();
//		this.height = boardHeight(); // for some reason this is skewing board... not important for now.
	}

	public int getX(){
		return x;
	}

	public void setX(int x){
		this.x = x;
	}

	public int getY(){
		return y;
	}

	public void setY(int y){
		this.y = y;
	}

	public int getSize(){
		return size;
	}

	public void setSize(int size){
		this.size = size;
	}

	public int getHeight(){
		return height;
	}

	public int getWidth(){
		return width;
	}

	public TileSpace getTileSpace(int row, int col){
		TileSpace tileSpace = tileSpaces[row][col];
		return tileSpace;
	}

	public TileSpace[][] getAllTileSpaces(){
		return tileSpaces;
	}

	public void setTileSpace(int row, int col, TileSpace tile){
		this.tileSpaces[row][col] = tile;
	}

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
	}
	
	public void snapTileIntoPlace(Tile tile){
		// snaps tile into place
		Log.d(TAG, "attempting to snap tile into place for tile:" + tile.getIndex());
		boolean snapped = false;
		A: for(int r=0; r < Math.sqrt(size); r++){
			for (int c=0; c < Math.sqrt(size); c++){
				TileSpace tileSpace = this.getTileSpace(r, c);
				if (tileSpace.handleTileSnapping(this, tile)){
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
	
	public void checkForExports(int eventX, int eventY){
		TileSpace[][] tileSpaces = this.getAllTileSpaces();
		for(int r=0; r < Math.sqrt(size); r++){
			for (int c=0; c < Math.sqrt(size); c++){
				TileSpace tileSpace = tileSpaces[r][c];
				if(tileSpace.coordsInside(eventX, eventY)){
					tileSpace.setOccupied(false);
				}
			}
		}
	}
	
	public void showOccupation(){
		TileSpace[][] tileSpaces = this.getAllTileSpaces();
		for(int r=0; r < Math.sqrt(size); r++){
			boolean[] row = new boolean[(int) Math.sqrt(size)];
			for (int c=0; c < Math.sqrt(size); c++){
				TileSpace tileSpace = tileSpaces[r][c];
				row[c] = tileSpace.isOccupied();
			}
			Log.d(TAG, "R" + r + ": " + Arrays.toString(row));
		}
	}
}
