package com.example.scrabblish.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.example.scrabblish.MainView;

public class TileSpace {
	private Bitmap bitmap;
	private Tile tile;
	private int row, col, x, y, centerX, centerY, width, height;
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
		this.tile = null;
	}

	/*************************
	 * Getters * 
	 *************************/
	public Bitmap getBitmap(){
		return bitmap;
	}

	public int getRow(){
		return row;
	}

	public int getCol(){
		return col;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public int getWidth(){
		return width;
	}

	public int getHeight(){
		return height;
	}

	public boolean isOccupied(){
		return occupied;
	}

	public int getMultiplier(){
		return multiplier;
	}

	public int[] getCoords(){
		int[] coords = {row, col};
		return coords;
	}

	public Tile getCurrentTile(){
		if(tile != null){
			return tile;
		} else {
			return null;
		}
	}

	/*************************
	 * Setters * 
	 *************************/
	public void setBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
	}

	public void setRow(int row){
		this.row = row;
	}

	public void setCol(int col){
		this.col = col;
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public void setHeight(int height){
		this.height = height;
	}

	public void setOccupied(boolean occupied){
		this.occupied = occupied;
	}

	public void setMultiplier(int multiplier){
		this.multiplier = multiplier;
	}

	public void setTile(Tile tile){
		this.tile = tile;
	}

	/*************************
	 * Helpers * 
	 *************************/	
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

	public TileSpace handleTileSnapping(Board board, Tile tile){
		int tileCenterX = tile.getCenterX();
		int tileCenterY = tile.getCenterY();
		//		Log.d(TAG, "TileCenter: (" + tileCenterX + ", " + tileCenterY + ")");
		//		Log.d(TAG, "TileSpaceCoords: (" + this.y + ", " + this.y + ")");
		if ((tileCenterX >= this.x) && (tileCenterX < (this.x + this.width))) {
			if ((tileCenterY > this.y) && (tileCenterY <= (this.y + this.height))) {
//				Log.d(TAG, "*INSIDE BOTH TESTS*");
//				Log.d(TAG, "tileSpace: " + this.row + "," + this.col);
//				Log.d(TAG, "tileSpace w: " + this.width + ",h:" + this.height);
//				Log.d(TAG, "Passed if: (tileCenterX:" + tileCenterX + " >= tileSpaceX:" + this.x + ") && (tileCenterX:" + tileCenterX + "< (tileSpaceX:"+ this.x + "tileSpaceW:" + this.width + "))");
//				Log.d(TAG, "Passed if: (tileCenterY:" + tileCenterY + " > tileSpaceY:" + this.y + ") && (tileCenterY:" + tileCenterY + "< (tileSpaceY:"+ this.y + "tileSpaceH:" + this.height + "))");
				if(this.occupied){
					TileSpace freeSpace = board.getClosestAvailableTileSpace(tileCenterX, tileCenterY);
					tile.setX(freeSpace.getX());
					tile.setY(freeSpace.getY());
					freeSpace.setOccupied(true);
					Log.d(TAG, "Returning freeSpace.");
					return freeSpace;
				} else {
					tile.setX(this.x);
					tile.setY(this.y);
					this.setOccupied(true);
					Log.d(TAG, "Returning tileSpace.");
					return this;
				}
			} 
		} 
		//		Log.d(TAG, "Returning null.");
		return null;
	}

	public boolean coordsInside(int eventX, int eventY){
		if ((eventX >= x) && (eventX < (x + width))) {
			if ((eventY > y) && (eventY <= (y + height))) {
				return true;
			} 
		}
		return false;
	}
}
