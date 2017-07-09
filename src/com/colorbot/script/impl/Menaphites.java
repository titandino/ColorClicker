package com.colorbot.script.impl;

import java.awt.Color;

import com.colorbot.bot.Bot;
import com.colorbot.script.Script;

public class Menaphites extends Script {
	
	@Override
	public void process() {
		try {
			Bot.MOUSE_SPEED = 4;
			if (Bot.getHealthPercent() > 20) {
				Bot.moveMouse(new Color(64, 201, 243), 10, 20);
				Bot.click();
			} else {
				Bot.pressKey('R');
			}
			Thread.sleep(Bot.random(400, 900));
		} catch(Exception e) {
			
		}
	}
	
}
