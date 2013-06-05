package com.dylanbarth.wordmash.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.dylanbarth.wordmash.MainView;
public class PenaltyAnimation {
	private int x, y, targetY, deltaY = 1, tileValue;
	private static final String TAG = MainView.class.getSimpleName();
	private Paint paint;

	public PenaltyAnimation(Tile tile){
		this.x = tile.getX();
		this.y = tile.getY();
		this.targetY = y-deltaY*30;
		this.tileValue = tile.getValue();
		this.paint = createPaint();
	}

	public void draw(Canvas canvas){
		if(this.y > targetY){
			canvas.drawText("-" + Integer.toString(this.tileValue), x, y, paint);
			this.y -= deltaY;
		}
	}

	public Paint createPaint(){
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.SANS_SERIF);
		paint.setTextSize(30);
		paint.setColor(Color.RED);
		return paint;
	}
}
