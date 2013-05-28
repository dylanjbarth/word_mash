package com.example.scrabblish.animation;

import android.view.SurfaceHolder;

public class ShuffleAnimation extends Thread {
	private SurfaceHolder holder;
    private boolean running = true;

    public ShuffleAnimation(SurfaceHolder holder) {
        this.holder = holder;
    }

    @Override
    public void run() {
        // this is where the animation will occur
    }

    public void setRunning(boolean b) {
        running = b;
    }
}
