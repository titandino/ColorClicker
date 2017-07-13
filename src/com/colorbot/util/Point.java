package com.colorbot.util;

public class Point {

	public short x;
	public short y;

	public Point(int x, int y) {
		this.x = (short) x;
		this.y = (short) y;
	}

	public Point(java.awt.Point p) {
		this.x = (short) p.x;
		this.y = (short) p.y;
	}
	
	@Override
	public String toString() {
		return "["+x+","+y+"]";
	}

}
