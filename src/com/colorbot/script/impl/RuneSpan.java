package com.colorbot.script.impl;

import java.awt.Color;
import java.util.ArrayList;

import com.colorbot.bot.Bot;
import com.colorbot.bot.Mouse;
import com.colorbot.script.Script;
import com.colorbot.util.ColorTolerance;
import com.colorbot.util.PointCluster;

public class RuneSpan extends Script {

	private Siphon siphon = Siphon.EARTH_ESSLING;

	@Override
	public void process() {
		try {
			Bot.MOUSE_SPEED = Bot.random(10, 20);
			ArrayList<PointCluster> clusters = Bot.findColorClustersByDistance(20, 30, siphon.getCTs());
			if (!clusters.isEmpty()) {
				for (PointCluster cluster : clusters) {
					Mouse.moveMouse(cluster.getCenter());
					if (Bot.getHoverOption().contains("Siphon")) {
						Bot.click();
						Thread.sleep(Bot.random(31000, 45000));
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public enum Siphon {
		WATER_ESSLING(new ColorTolerance(new Color(192, 105, 92), 10), new ColorTolerance(new Color(108, 196, 226), new Color(10, 10, 20))),
		EARTH_ESSLING(new ColorTolerance(new Color(190, 233, 193), 5), new ColorTolerance(new Color(174, 92, 61), 5)),
		;

		private ColorTolerance[] cts;

		private Siphon(ColorTolerance... cts) {
			this.cts = cts;
		}

		public ColorTolerance[] getCTs() {
			return cts;
		}
	}
}
