package com.colorbot.script.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import com.colorbot.bot.Bot;
import com.colorbot.script.Script;

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
			Bot.MOUSE_SPEED = Bot.random(2, 5);
			for (Siphon s : Siphon.values()) {
				p1 = Bot.getPointWithColorCombo(s.getC1(), s.getC2(), 10, 10);
				if (p1 != null)
					break;
			}
			if (p1 != null) {
				Bot.moveMouse(p1, 30, 10);
				Bot.click();
				p1 = null;
				if (Bot.random(1000) < 10) {
					Thread.sleep(Bot.random(25000, 35000));
				} else {
					Thread.sleep(Bot.random(10000, 17000));
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
