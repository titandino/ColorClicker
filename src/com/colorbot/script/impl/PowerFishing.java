package com.colorbot.script.impl;

import com.colorbot.bot.Bot;
import com.colorbot.bot.Mouse;
import com.colorbot.script.Script;

public class PowerFishing extends Script {
	
	@Override
	public void process() {
		try {
			Bot.MOUSE_SPEED = 4;
			Mouse.moveMouse(Bot.random(0, 500), Bot.random(0, 500));
			Thread.sleep(5000);
		} catch(Exception e) {
			
		}
	}
	
}
