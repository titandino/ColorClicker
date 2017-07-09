package com.colorbot.script.impl;

import java.awt.Color;

import com.colorbot.bot.Bot;
import com.colorbot.script.Script;

public class Menaphites extends Script {
	
	@Override
	public void process() {
		try {
			Bot.MOUSE_SPEED = 4;
			Bot.moveMouse(new Color(140, 110, 251), 5, 15);
			Bot.click();
			if (Bot.getHP() < 500) {
				Bot.pressKey('R');
			}
			Thread.sleep(Bot.random(400, 1000));
		} catch(Exception e) {
			
		}
	}
	
}
