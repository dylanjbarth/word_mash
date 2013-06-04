package com.dylanbarth.wordmash.model;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;

import com.dylanbarth.wordmash.MainView;

public class Component {
	private int x, y, width, height, index, score, time;
	private String title, displayTitle;
	private boolean isButton, isTouched, clicked;
	private Paint paint = new Paint();
	private ArrayList<Bitmap> images;
	private static final String TAG = MainView.class.getSimpleName();

	public Component(String title, int index, int x, int y, int width, int height, boolean isButton, ArrayList<Bitmap> images){
		this.title = title;
		this.index = index;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isButton = isButton;
		this.isTouched = false;
		ArrayList<Bitmap> scaledImages = new ArrayList<Bitmap>();
		Log.d(TAG, "***** " + title);
		for(int i=0; i<images.size(); i++){
			scaledImages.add(Bitmap.createScaledBitmap(images.get(i), this.width, this.height, true));
		}
		this.images = scaledImages;
		if(this.title == "score"){
			this.score = 0;
		}
		if(this.title == "timer"){
			this.time = 60; 
		}
		Log.d(TAG, "Initialized component:" + title + 
				"coords: (" + x + ", " + y + "), " +
				"h:" + height + " w:" + width);
	}

	public void draw(Canvas canvas, String gameState, String boardState){
		paint.setARGB(0, 51, 153, 255);
		if (isTouched){
			paint.setAlpha(200);
		} else {
			paint.setAlpha(100);
		}

		paint.setStrokeWidth(1);
		if(title != "score" && title != "timer" && title != "logo"){
			int margin = 15;
			canvas.drawRoundRect(new RectF(x, y+margin, x+width, y+height-margin), 15, 15, paint);	
		}
		paint.setAlpha(255);
		paint.setTextSize(40);
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.SANS_SERIF);
		if(title=="timer"){
			paint.setARGB(255, 204, 0, 0);
			canvas.drawBitmap(this.images.get(0), this.x, this.y, paint);
			canvas.drawText(String.valueOf(time) + "s", this.x + this.width/3, this.y+(this.height/3)*2, paint);
		} else if(title=="score"){
			paint.setARGB(255, 102, 178, 255);
			canvas.drawBitmap(this.images.get(0), this.x, this.y, paint);
			canvas.drawText(String.valueOf(score), this.x + this.width/2, this.y+(this.height/3)*2, paint);
		} else if(title =="updateTray"){
			if(boardState == "shuffle"){
				canvas.drawBitmap(this.images.get(1), this.x, this.y, paint);
			} else {
				canvas.drawBitmap(this.images.get(0), this.x, this.y, paint);
			}
		} else if(title =="changeGameState"){
			if(gameState == "preGame"){
				canvas.drawBitmap(this.images.get(0), this.x, this.y, paint);
			} else {
				canvas.drawBitmap(this.images.get(1), this.x, this.y, paint);
			}
		} else if(title =="newTiles"){
			canvas.drawBitmap(this.images.get(0), this.x, this.y, paint);
		} 
		else {
			//			canvas.drawText(title, x+20, y+20, paint);
		}


	}

	/*************************
	 * Getters * 
	 *************************/

	public String getTitle() {
		return title;
	}

	public boolean isButton() {
		return this.isButton;
	}

	public boolean isClicked() {
		return this.clicked;
	}

	public int getTime(){
		return this.time;
	}

	/*************************
	 * Setters * 
	 *************************/

	public void setTitle(String title) {
		this.title = title;
	}

	public void resetClicked(){
		this.clicked = false;
	}

	public void setScore(int score){
		this.score = score;
	}

	public void subtractTime(){
		this.time -= 1;
	}

	/*************************
	 * Helpers * 
	 *************************/

	public void handleActionDown(int eventX, int eventY) {
		if((eventX >= x) && (eventX < x+width) && (eventY >= y) && (eventY < y+height)){
			this.isTouched = true;
			Log.d(TAG, "Setting " + title + " to touched!");
		}
	}

	public void handleActionMove(int eventX, int eventY) {
		if(this.isTouched){
			if(!((eventX >= x) && (eventX < x+width) && (eventY >= y) && (eventY < y+height))){
				this.isTouched = false;
				Log.d(TAG, "Moved outside of " + title + ", setting touched to false!");
				// clicked(); or by button type, do each individual method here, this can be the qb
			}
		}
	}

	public void handleActionUp(int eventX, int eventY) {
		if(this.isTouched){
			if((eventX >= x) && (eventX < x+width) && (eventY >= y) && (eventY < y+height)){
				this.isTouched = false;
				this.clicked = true;
				Log.d(TAG, title + " has been clicked!");
				// clicked(); or by button type, do each individual method here, this can be the qb
			}
		}
	}
}
