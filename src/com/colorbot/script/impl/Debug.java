package com.colorbot.script.impl;

import java.awt.Color;

import com.colorbot.bot.Bot;
import com.colorbot.script.Script;
import com.colorbot.window.BotFrame;

public class Debug extends Script {
	@Override
	public void process() {
		try {
			Color c = Bot.getColorOnMouse();
			BotFrame.log("Color you are hovering: (" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + ")");
			Thread.sleep(500);
		} catch (InterruptedException e) {

		}
	}
}
