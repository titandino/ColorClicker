package com.colorbot.util;

public class Point {

	public short x;
	public short y;

	public Point(int x, int y) {
		x = (short) x;
		y = (short) y;
	}

	public Point(java.awt.Point p) {
		this.x = (short) p.x;
		this.y = (short) p.y;
	}

}
