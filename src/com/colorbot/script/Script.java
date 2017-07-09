package com.colorbot.script;

public abstract class Script implements Runnable {
	
	public String[] args;
	private boolean running = true;
	
	@Override
	public void run() {
		if (running)
			process();
	}
	
	public void stop() {
		running = false;
	}
	
	public void start() {
		running = true;
	}
	
	public abstract void process();
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}