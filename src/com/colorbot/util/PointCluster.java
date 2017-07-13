package com.colorbot.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

public class PointCluster {

	private LinkedList<Point> points;
	private Rectangle boundingBox;

	public PointCluster(LinkedList<Point> points) {
		this.points = points;
		this.boundingBox = ColorUtil.createRectangleFromPoints(points);
	}

	public Rectangle getBoundingBox() {
		return boundingBox;
	}

	public LinkedList<Point> getPoints() {
		return points;
	}

	public void render(Graphics g) {
		g.setColor(new Color(0, 255, 0));
		g.drawRect((int) boundingBox.getX(), (int) boundingBox.getY(), (int) boundingBox.getWidth(), (int) boundingBox.getHeight());
		g.setColor(Color.WHITE);
		for (Point p : points) {
			g.drawLine(p.x, p.y, p.x, p.y);
		}
	}

	public Point getCenter() {
		return new Point((int) boundingBox.getCenterX(), (int) boundingBox.getCenterY());
	}

}
