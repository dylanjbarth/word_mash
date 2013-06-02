package com.dylanbarth.wordmash.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.dylanbarth.wordmash.MainView;

public class ScoreAnimation {
	private int x, y, targetY, deltaY = 1;
	private String tileType, multiplier;
	private static final String TAG = MainView.class.getSimpleName();
	private Paint paint;

	public ScoreAnimation(TileSpace tileSpace){
		this.x = tileSpace.getX();
		this.y = tileSpace.getY();
		this.targetY = y-deltaY*30;
		this.tileType = tileSpace.getTileType();
		this.multiplier = getMultiplier();
		this.paint = createPaint();
	}
	
	public void draw(Canvas canvas){
		if(this.y > targetY && this.tileType != "tile_space"){
			Log.d(TAG, "multipler:"+multiplier);
			Log.d(TAG, "paint:" + paint);
			canvas.drawText(multiplier, x, y, paint);
			this.y -= deltaY;
		}
	}
	
	public Paint createPaint(){
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.SANS_SERIF);
		paint.setTextSize(30);
		paint.setColor(Color.GREEN);
		return paint;
	}
	
	public String getMultiplier(){
		if(this.tileType=="tw_space"){
			multiplier = "TW!";
		} else if(this.tileType=="dw_space"){
			multiplier = "DW!";
		} else if(this.tileType=="tl_space"){
			multiplier = "TL!";
		} else if(this.tileType=="dl_space"){
			multiplier = "DL!";
		}
		return multiplier;
	}
	
//	public void animateMultiplier(String tileType) {
//		final int animateSpeed = 20;
//		final int deltaY = 1;
//		final int threadRevolutions = 50;
//		final Handler handler=new Handler();
//		final Runnable r = new Runnable()
//		{
//			public void run() 
//			{	
//				setMultiplierAnimation(true);
//				for(int i=0; i<threadRevolutions; i++){
//					Log.d(TAG, "Animating score! PenaltyY:"+getPenaltyY());
//					setPenaltyY(getPenaltyY()-deltaY);
//					android.os.SystemClock.sleep(10);
//				}
//				setPenaltyY(getY());
//				setMultiplierAnimation(false);
//			}
//		};
//		handler.postDelayed(r, animateSpeed);
//		
//	}
}
