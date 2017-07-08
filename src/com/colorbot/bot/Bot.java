package com.colorbot.bot;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.util.concurrent.TimeUnit;

import com.colorbot.script.Script;
import com.colorbot.task.TaskExecutor;

public class Bot {
	
	public static Robot robot;
	
	public static int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	public static Color getColor(int x, int y) {
		return robot.getPixelColor((int) MouseInfo.getPointerInfo().getLocation().getX(), (int) MouseInfo.getPointerInfo().getLocation().getY());
	}
	
	public static void runScript(Script script) {
		if (TaskExecutor.getEventExecutor().isShutdown())
			TaskExecutor.initializeEventExecutor();
		TaskExecutor.getEventExecutor().scheduleAtFixedRate(script, 1, 1, TimeUnit.MILLISECONDS);
	}

}
