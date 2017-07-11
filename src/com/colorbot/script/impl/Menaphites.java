package com.colorbot.script.impl;

import java.awt.Color;

import com.colorbot.bot.Bot;
import com.colorbot.script.Script;

public class Menaphites extends Script {
	
	@Override
	public void process() {
		try {
			Bot.MOUSE_SPEED = Bot.random(2, 5);
			if (Bot.getHealthPercent() > 20) {
				Bot.moveMouse(new Color(64, 201, 243), new Color(90, 46, 210), 10, 20, 30, 10);
				Bot.click();
			} else {
				Bot.pressKey('R');
			}
			if (Bot.random(1000) < 10) {
				Thread.sleep(Bot.random(2000, 5000));
			} else {
				Thread.sleep(Bot.random(400, 900));
			}
		} catch(Exception e) {
			
		}
	}
	
}
