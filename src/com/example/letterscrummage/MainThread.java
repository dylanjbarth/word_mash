package com.example.letterscrummage;

import android.view.SurfaceHolder;

public class MainThread extends Thread {

	private static final String TAG = MainThread.class.getSimpleName();

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
		long tickCount = 0L;
		System.out.println("Starting Game Loop");
		while (running) {
			tickCount ++;
			// update game state
			// render state to the screen
		}
		System.out.println("Exiting. Game loop exectuted " + tickCount + " times.");
	}
}
