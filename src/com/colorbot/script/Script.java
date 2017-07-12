package com.colorbot.script;

import java.awt.Graphics;

import com.colorbot.window.Overlay;

public abstract class Script implements Runnable {

	public String[] args;
	private boolean running = true;

	@Override
	public void run() {
		if (running) {
			paint(Overlay.ovl.getGraphics());
			Overlay.ovl.repaint();
			process();
		}
	}

	public void stop() {
		running = false;
	}

	public void start() {
		running = true;
	}

	public void paint(Graphics g) {

	}

	public abstract void process();

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}