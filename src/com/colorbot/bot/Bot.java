package com.colorbot.bot;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.colorbot.script.Script;
import com.colorbot.script.impl.*;
import com.colorbot.task.TaskExecutor;
import com.colorbot.window.BotFrame;

public class Bot {
	
	public static final Script[] SCRIPT_LIST = new Script[] { new Debug(), new Menaphites(), new PowerFishing() };

	public static Robot robot;
	
	public static Script currentScript;
	public static int MOUSE_SPEED = 5;
	
	public static int HP = 100;
	
	public static int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	public static Color getColorOnMouse() {
		return robot.getPixelColor((int) MouseInfo.getPointerInfo().getLocation().getX(), (int) MouseInfo.getPointerInfo().getLocation().getY());
	}
	
	public static Point getPointWithColor(Color color, double distance) {
		int[] pixel;
		BufferedImage image = captureScreen();
		for (int x = 0;x < image.getWidth();x++) {
			for (int y = 0;y < image.getHeight();y++) {
				pixel = image.getRaster().getPixel(x, y, new int[3]);
				if (getDifference(color, new Color(pixel[0], pixel[1], pixel[2])) <= distance) {
					return new Point(x, y);
				}
			}
		}
		return null;
	}
	
	public static int getLongestConsecutiveColorX(Color color, double distance) {
		int[] pixel;
		int consecutive = 0;
		boolean found = false;
		BufferedImage image = captureScreen();
		for (int y = 0;y < image.getHeight();y++) {
			for (int x = 0;x < image.getWidth();x++) {
				pixel = image.getRaster().getPixel(x, y, new int[3]);
				if (getDifference(color, new Color(pixel[0], pixel[1], pixel[2])) <= distance) {
					found = true;
					consecutive++;
				} else {
					if (found)
						break;
				}
			}
		}
		return consecutive;
	}
	
	public static int getHealthPercent() {
		int longest = getLongestConsecutiveColorX(new Color(75, 190, 0), 30);
		if (longest >= 0 && longest <= 40)
			HP = longest;
		return HP;
	}
	
	private static double getDifference(Color c1, Color c2) {
		return Math.sqrt(Math.pow(c2.getRed() - c1.getRed(), 2) + Math.pow(c2.getGreen() - c1.getGreen(), 2) + Math.pow(c2.getBlue() - c1.getBlue(), 2));
	}
	
    private static BufferedImage captureScreen() {
        final Rectangle rectangle = new Rectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        return robot.createScreenCapture(rectangle);
    }
	
	public static void moveMouse(Color color, double distance, int dontMoveDist) {
		Point moveTo = getPointWithColor(color, distance);
		if (moveTo != null) {
			if (Mouse.getMouseLocation().distance(new Point(moveTo)) > dontMoveDist)
				Mouse.moveMouse(moveTo.x, moveTo.y);
		}
	}
	
	public static void runScript(Script script) {
		if (TaskExecutor.getEventExecutor().isShutdown())
			TaskExecutor.initializeEventExecutor();
		currentScript = script;
		currentScript.start();
		TaskExecutor.getEventExecutor().scheduleAtFixedRate(script, 1, 1, TimeUnit.MILLISECONDS);
		BotFrame.log("Started script: " + script.getClass().getSimpleName());
	}
	
	public static void stopScript() {
		TaskExecutor.getEventExecutor().shutdownNow();
		if (currentScript != null) {
			BotFrame.log("Stopped script: " + currentScript.getClass().getSimpleName());
			currentScript.stop();
		}
		currentScript = null;
	}
	
	public static final int random(int maxValue) {
		return (int) (Math.random() * (maxValue + 1));
	}

	public static final int random(int min, int max) {
		final int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random(n));
	}

	public static final double random(double min, double max) {
		final double n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random((int) n));
	}
	
	public static void pressKey(int keyCode) throws InterruptedException {
		robot.keyPress(keyCode);
		Thread.sleep(random(10, 50));
		robot.keyRelease(keyCode);
	}

	public static void click() {
		try {
			int mask = InputEvent.BUTTON1_DOWN_MASK;
			robot.mousePress(mask);
			Thread.sleep(random(10, 50));
			robot.mouseRelease(mask);
		} catch(Exception e) {
			
		}
	}

}
