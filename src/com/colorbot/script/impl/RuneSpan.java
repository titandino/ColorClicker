package com.colorbot.script.impl;

import java.awt.Color;

import com.colorbot.bot.Bot;
import com.colorbot.bot.Mouse;
import com.colorbot.script.Script;
import com.colorbot.util.ColorTolerance;
import com.colorbot.util.PointCluster;
import com.colorbot.window.BotFrame;
import com.colorbot.window.Overlay;

public class RuneSpan extends Script {

	private Siphon siphon = Siphon.WATER_ESSLING;

	@Override
	public void process() {
		try {
			Bot.MOUSE_SPEED = 2;
			PointCluster closest = Bot.findClosestColorCluster(20, 50, siphon.getCTs());
			BotFrame.log("Closest cluster: " + closest);
			if (closest != null) {
				closest.render(Overlay.ovl.getGraphics());
				Overlay.ovl.repaint();
				Mouse.moveMouse(closest.getCenter());
				if (Bot.getHoverOption().contains("Siphon")) {
					Bot.click();
					Thread.sleep(Bot.random(7000, 16000));
				}
			}
		} catch (Exception e) {

		}
	}

	public enum Siphon {
		WATER_ESSLING(new ColorTolerance(new Color(192, 105, 92), 10), new ColorTolerance(new Color(108, 196, 226), new Color(10, 10, 20)));

		private ColorTolerance[] cts;

		private Siphon(ColorTolerance... cts) {
			this.cts = cts;
		}

		public ColorTolerance[] getCTs() {
			return cts;
		}
	}
}
