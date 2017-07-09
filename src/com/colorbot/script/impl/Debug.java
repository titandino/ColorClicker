package com.colorbot.script.impl;

import com.colorbot.bot.Bot;
import com.colorbot.script.Script;
import com.colorbot.window.BotFrame;

public class Debug extends Script {
	@Override
	public void process() {
		try {
			BotFrame.log("Color you are hovering: " + Bot.getColorOnMouse().toString());
			BotFrame.log("Health distance: " + Bot.getHealthPercent());
			Thread.sleep(500);
		} catch (InterruptedException e) {
			
		}
	}
}
