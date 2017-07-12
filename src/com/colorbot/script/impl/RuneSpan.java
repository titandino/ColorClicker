package com.colorbot.script.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.colorbot.bot.Bot;
import com.colorbot.bot.Mouse;
import com.colorbot.script.Script;
import com.colorbot.util.ColorUtil;
import com.colorbot.window.Overlay;

public class RuneSpan extends Script {
	
	public enum Siphon {
		AIR_ESSLING(new Color(187, 219, 233), new Color(153, 176, 191));
		
		private Color c1;
		private Color c2;
		
		private Siphon(Color c1, Color c2) {
			this.c1 = c1;
			this.c2 = c2;
		}
		
		public Color getC1() {
			return c1;
		}
		
		public Color getC2() {
			return c2;
		}
	}
	
	Point p1 = null;
	
	@Override
	public void process() {
		try {
//			Bot.MOUSE_SPEED = Bot.random(2, 5);
//			for (Siphon s : Siphon.values()) {
//				p1 = Bot.getPointWithColorCombo(s.getC1(), s.getC2(), 10, 10);
//				if (p1 != null)
//					break;
//			}
//			if (p1 != null) {
//				Bot.moveMouse(p1, 30, 10);
//				Bot.click();
//				p1 = null;
//				if (Bot.random(1000) < 10) {
//					Thread.sleep(Bot.random(25000, 35000));
//				} else {
//					Thread.sleep(Bot.random(10000, 17000));
//				}
//			}
			BufferedImage image = Bot.captureScreen();
			ArrayList<Point> points = new ArrayList<Point>();
			points.addAll(ColorUtil.findAllColorWithinTolerance(image, new Color(192, 106, 94), new Color(10, 10, 10)));
			points.addAll(ColorUtil.findAllColorWithinTolerance(image, new Color(110, 192, 224), new Color(10, 10, 40)));
			if (points.size() > 0) {
				int avgX = 0;
				int avgY = 0;
				for (Point p : points) {
					avgX += p.x;
					avgY += p.y;
				}
				avgX /= points.size();
				avgY /= points.size();
				Point avg = new Point(avgX, avgY);
				
				ArrayList<Point> closePoints = new ArrayList<Point>();
				for (Point p : points) {
					if (ColorUtil.getDistanceBetween(p, avg) < 80)
						closePoints.add(p);
				}
				if (closePoints.isEmpty())
					return;
				for (Point p : closePoints) {
					Overlay.ovl.getGraphics().drawLine(p.x, p.y, p.x, p.y);
				}
				Rectangle rect = ColorUtil.createRectangleFromPoints(closePoints);
				Overlay.drawRect(rect);
				Overlay.ovl.repaint();
				Mouse.moveMouse((int) rect.getCenterX(), (int) rect.getCenterY());
				if (Bot.getHoverOption().contains("Siphon")) {
					Bot.click();
					Thread.sleep(10000);
				}
			}
		} catch(Exception e) {
			
		}
	}
	
	@Override
	public void paint(Graphics g) {
		if (p1 != null) {
			g.setColor(new Color(0, 255, 0));
			g.drawRect(p1.x, p1.y, 10, 10);
		}
	}
	
}
