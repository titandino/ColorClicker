package com.colorbot.util;

public class Point {

	public int x;
	public int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(java.awt.Point p) {
		this.x = p.x;
		this.y = p.y;
	}
	
	@Override
	public String toString() {
		return "["+x+","+y+"]";
	}

}
