package com.example.scrabblish.model;

import android.graphics.Canvas;


public class Board {
	private int x;
	private int y;
	private int size;
	private int width;
	private int height;
	private TileSpace[][] tileSpaces;

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
		boolean snapped = false;
		for(int r=0; r < Math.sqrt(size); r++){
			for (int c=0; c < Math.sqrt(size); c++){
				TileSpace tileSpace = this.getTileSpace(r, c);
				if (tileSpace.handleTileSnapping(this, tile)){
					snapped = true;
				}
			}
		}
		// or resets its position
		if (!snapped){
			tile.resetPosition();
		}
	}
}
