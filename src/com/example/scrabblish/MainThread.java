package com.example.scrabblish;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

	private MainView mainView;
	private SurfaceHolder surfaceHolder;
	// flag to hold game state
	private boolean running;

	public void setRunning(boolean running) {
		this.running = running;
	}

	public MainThread(SurfaceHolder surfaceHolder, MainView mainView){
		super();
		this.surfaceHolder = surfaceHolder;
		this.mainView = mainView;
	}

	@Override
	public void run() {
		Canvas canvas;
		System.out.println("Starting Game Loop");
		while (running) {
			canvas=null;
			try {
				canvas = this.surfaceHolder.lockCanvas();
				synchronized(surfaceHolder){
					// update game state
					// render state to the screen
					this.mainView.onDraw(canvas);
				} 
			} finally {
				if (canvas != null){
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
}
