package com.example.scrabblish.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.scrabblish.MainView;

public class TileSpace {
	private Bitmap bitmap;
	private int row;
	private int col;
	private int x;
	private int y;
	private int centerX;
	private int centerY;
	private int width;
	private int height;
	private boolean occupied;
	private int multiplier;
	private static final String TAG = MainView.class.getSimpleName();

	public TileSpace(Bitmap passedBitmap, int row, int col, int width, int height){
		this.bitmap = scaleBitmap(passedBitmap, width, height);
		this.row = row;
		this.col = col;
		this.width = width;
		this.height = height;
		this.occupied = false;
		this.multiplier = 1;
		this.x = positionTileX(row, width);
		this.y = positionTileY(col, height);
		this.centerX = x+width/2;
		this.centerY = y+height/2;
	}

	public Bitmap getBitmap(){
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
	}

	public int getRow(){
		return row;
	}

	public void setRow(int row){
		this.row = row;
	}

	public int getCol(){
		return col;
	}

	public void setCol(int col){
		this.col = col;
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

	public int getWidth(){
		return width;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public int getHeight(){
		return height;
	}

	public void setHeight(int height){
		this.height = height;
	}

	public boolean isOccupied(){
		return occupied;
	}

	public void setOccupied(boolean occupied){
		this.occupied = occupied;
	}

	public int getMultiplier(){
		return multiplier;
	}

	public void setMultiplier(int multiplier){
		this.multiplier = multiplier;
	}

	public int positionTileX(int row, int imgWidth){
		// gives top left x of tile
		int x = imgWidth*row;
		return x;
	}

	public int positionTileY(int col, int imgHeight){
		// gives center of tile in x, y coordinates
		int y = imgHeight*col;
		return y;
	}

	public Bitmap scaleBitmap(Bitmap tileSpaceImage, int imgWidth, int imgHeight){
		Bitmap resizedBitmap=Bitmap.createScaledBitmap(tileSpaceImage, imgWidth, imgHeight, true);
		return resizedBitmap;
	}

	public void draw(Canvas canvas){
		canvas.drawBitmap(bitmap, x, y, null);
	}

	public boolean handleTileSnapping(Board board, Tile tile){
		int tileCenterX = tile.getCenterX();
		int tileCenterY = tile.getCenterY();
		if ((tileCenterX >= x) && (tileCenterX < (x + width))) {
			if ((tileCenterY > y) && (tileCenterY <= (y + height))) {
				if(isOccupied()){
					TileSpace freeSpace = board.getClosestAvailableTileSpace(tileCenterX, tileCenterY);
					tile.setX(freeSpace.getX());
					tile.setY(freeSpace.getY());
					freeSpace.setOccupied(true);
				} else {
					tile.setX(x);
					tile.setY(y);
					setOccupied(true);
				}
				return true;
			} 
		} 
		return false;
	}
}
