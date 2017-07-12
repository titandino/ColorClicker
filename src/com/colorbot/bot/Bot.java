package com.colorbot.bot;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.colorbot.bot.RSText.FontTypes;
import com.colorbot.script.Script;
import com.colorbot.script.impl.Debug;
import com.colorbot.script.impl.Menaphites;
import com.colorbot.script.impl.RuneSpan;
import com.colorbot.task.TaskExecutor;
import com.colorbot.util.ColorTolerance;
import com.colorbot.util.ColorUtil;
import com.colorbot.util.PointCluster;
import com.colorbot.window.BotFrame;
import com.colorbot.window.Point;

public class Bot {

	public static final Script[] SCRIPT_LIST = new Script[] { new Debug(), new Menaphites(), new RuneSpan() };

	public static Robot robot;

	public static Script currentScript;
	public static int MOUSE_SPEED = 5;

	private static Rectangle healthLocation;

	public static int HP = 9900;

	public static int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	public static void locateHealth() throws IOException {
		BufferedImage hp = ImageIO.read(new File("img/hp.png"));
		BufferedImage screen = captureScreen();
		int[] region = findSubimage(screen, hp);
		if (region[0] != 0 && region[1] != 0) {
			healthLocation = new Rectangle(region[0] + 10, region[1] + 9, 89, 8);
			BotFrame.log("Found health location at: " + Arrays.toString(region));
			// System.out.println("Health: " + OCR.getTextAt(new Rectangle((int)
			// healthLocation.getX(), (int) healthLocation.getY(), (int)
			// healthLocation.getWidth()-70, (int)
			// healthLocation.getHeight()-23)));
			// Overlay.drawRect(new Rectangle((int) healthLocation.getX(), (int)
			// healthLocation.getY(), (int) healthLocation.getWidth()-70, (int)
			// healthLocation.getHeight()-23));
			// TODO make fontset for xp coujnter/hp
		}
	}

	public static PointCluster findClosestColorCluster(int density, int radius, ColorTolerance... colors) {
		ArrayList<PointCluster> clusters = findColorClusters(density, radius, colors);
		PointCluster closest = null;
		int min = 50000;
		for (PointCluster cluster : clusters) {
			int dist = ColorUtil.getDistanceBetween(new Point((int) cluster.getBoundingBox().getCenterX(), (int) cluster.getBoundingBox().getCenterY()), getMousePos());
			if (dist < min) {
				closest = cluster;
				min = dist;
			}
		}
		return closest;
	}

	public static Point getMousePos() {
		return new Point(MouseInfo.getPointerInfo().getLocation());
	}

	public static ArrayList<PointCluster> findColorClusters(int density, int radius, ColorTolerance... colors) {
		BufferedImage b = captureScreen();
		ArrayList<PointCluster> clusters = new ArrayList<PointCluster>();
		HashMap<Integer, Byte> visited = new HashMap<Integer, Byte>();
		for (int x = 0; x < b.getWidth(); x++) {
			for (int y = 0; y < b.getHeight(); y++) {
				if (visited.get(getPackedPoint(x, y)) != null) {
				continue;
				}
				if (ColorUtil.areColorsWithinTolerance(getColorAt(b, x, y), colors)) {
					ArrayList<Point> list = new ArrayList<Point>();
					for (int xOff = x - radius; xOff < x + radius; xOff++) {
						for (int yOff = y - radius; yOff < y + radius; yOff++) {
							if ((xOff < 0 || xOff > Bot.SCREEN_WIDTH) || (yOff < 0 || yOff > Bot.SCREEN_HEIGHT))
								continue;
							if (visited.get(getPackedPoint(xOff, yOff)) != null)
							continue;
							if (ColorUtil.areColorsWithinTolerance(getColorAt(b, xOff, yOff), colors))
								list.add(new Point(xOff, yOff));
							visited.put(getPackedPoint(xOff, yOff), (byte) 0);
						}
					}
					if (list.size() >= density) {
						clusters.add(new PointCluster(list));
					}
				} else {
					visited.put(getPackedPoint(x, y), (byte) 0);
				}
			}
		}
		return clusters;
	}

	public static int getPackedPoint(int x, int y) {
		return ((x << 16) | (y & 0xffff));
	}

	public static String getHoverOption() {
		Rectangle xpTest = new Rectangle(0, 20, 100, 30);
		return OCR.getTextAt(xpTest, FontTypes.UpCharsEx);
	}

	public static void init() throws IOException {
		locateHealth();
	}

	public static int[] findSubimage(BufferedImage im1, BufferedImage im2) {
		int w1 = im1.getWidth();
		int h1 = im1.getHeight();
		int w2 = im2.getWidth();
		int h2 = im2.getHeight();
		assert (w2 <= w1 && h2 <= h1);
		int bestX = 0;
		int bestY = 0;
		double lowestDiff = Double.POSITIVE_INFINITY;
		for (int x = 0; x < w1 - w2; x++) {
			for (int y = 0; y < h1 - h2; y++) {
				double comp = compareImages(im1.getSubimage(x, y, w2, h2), im2);
				if (comp < lowestDiff) {
					bestX = x;
					bestY = y;
					lowestDiff = comp;
				}
			}
		}
		return new int[] { bestX, bestY };
	}

	public static double compareImages(BufferedImage im1, BufferedImage im2) {
		assert (im1.getHeight() == im2.getHeight() && im1.getWidth() == im2.getWidth());
		double variation = 0.0;
		for (int x = 0; x < im1.getWidth(); x++) {
			for (int y = 0; y < im1.getHeight(); y++) {
				variation += getDifference(im1.getRGB(x, y), im2.getRGB(x, y)) / Math.sqrt(3);
			}
		}
		return variation / (im1.getWidth() * im1.getHeight());
	}

	public static Color getColorOnMouse() {
		return robot.getPixelColor((int) MouseInfo.getPointerInfo().getLocation().getX(), (int) MouseInfo.getPointerInfo().getLocation().getY());
	}

	public static Point getPointWithColor(Color color, double distance) {
		int[] pixel;
		BufferedImage image = captureScreen();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				pixel = image.getRaster().getPixel(x, y, new int[3]);
				if (getDifference(color, new Color(pixel[0], pixel[1], pixel[2])) <= distance) {
					return new Point(x, y);
				}
			}
		}
		return null;
	}

	public static Point getPointWithColorCombo(Color color1, Color color2, int radius, double colorVariation) {
		int[] pixel;
		BufferedImage image = captureScreen();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				pixel = image.getRaster().getPixel(x, y, new int[3]);
				if (getDifference(color1, new Color(pixel[0], pixel[1], pixel[2])) <= colorVariation) {
					for (int xOff = x - radius; xOff < x + radius; xOff++) {
						for (int yOff = y - radius; yOff < y + radius; yOff++) {
							if ((xOff < 0 || xOff >= image.getWidth()) || (yOff < 0 || yOff >= image.getHeight()))
								continue;
							pixel = image.getRaster().getPixel(xOff, yOff, new int[3]);
							if (getDifference(color2, new Color(pixel[0], pixel[1], pixel[2])) <= colorVariation) {
								return new Point(x, y);
							}
						}
					}
				}
			}
		}
		return null;
	}

	public static int getLongestConsecutiveColorX(Color color, double distance) {
		return getLongestConsecutiveColorX(captureScreen(), color, distance);
	}

	public static int getLCCRed(BufferedImage image, int r) {
		int[] pixel;
		int consecutive = 0;
		boolean found = false;
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				pixel = image.getRaster().getPixel(x, y, new int[3]);
				if (pixel[0] > r) {
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

	public static int getLongestConsecutiveColorX(BufferedImage image, Color color, double distance) {
		int[] pixel;
		int consecutive = 0;
		boolean found = false;
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
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

	public static double getHealthPercent() {
		if (healthLocation != null) {
			try {
				BufferedImage hpImage = robot.createScreenCapture(healthLocation);
				int curr = getLCCRed(hpImage, 200);
				return (curr * 100) / 79.0;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 100.0;
	}

	private static double getDifference(Color c1, Color c2) {
		return Math.sqrt(Math.pow(c2.getRed() - c1.getRed(), 2) + Math.pow(c2.getGreen() - c1.getGreen(), 2) + Math.pow(c2.getBlue() - c1.getBlue(), 2));
	}

	public static double getDifference(int rgb1, int rgb2) {
		double r1 = ((rgb1 >> 16) & 0xFF) / 255.0;
		double r2 = ((rgb2 >> 16) & 0xFF) / 255.0;
		double g1 = ((rgb1 >> 8) & 0xFF) / 255.0;
		double g2 = ((rgb2 >> 8) & 0xFF) / 255.0;
		double b1 = (rgb1 & 0xFF) / 255.0;
		double b2 = (rgb2 & 0xFF) / 255.0;
		return Math.sqrt((r1 - r2) * (r1 - r2) + (g1 - g2) * (g1 - g2) + (b1 - b2) * (b1 - b2));
	}

	public static BufferedImage captureScreen() {
		final Rectangle rectangle = new Rectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		return robot.createScreenCapture(rectangle);
	}

	public static void moveMouse(Color color, double distance, int dontMoveDist, int random) {
		Point moveTo = getPointWithColor(color, distance);
		if (moveTo != null) {
			if (ColorUtil.getDistanceBetween(Mouse.getMouseLocation(), moveTo) > dontMoveDist)
				Mouse.moveMouse(moveTo.x, moveTo.y, random);
		}
	}

	public static void moveMouse(Point p, int dontMoveDist, int random) {
		if (ColorUtil.getDistanceBetween(Mouse.getMouseLocation(), p) > dontMoveDist)
			Mouse.moveMouse(p.x, p.y, random);
	}

	public static void moveMouse(Color color, Color color2, int radius, double colorVariation, int dontMoveDist, int random) {
		Point moveTo = getPointWithColorCombo(color, color2, radius, colorVariation);
		if (moveTo != null) {
			if (ColorUtil.getDistanceBetween(Mouse.getMouseLocation(), moveTo) > dontMoveDist)
				Mouse.moveMouse(moveTo.x, moveTo.y, random);
		}
	}

	public static void moveMouse(Color color, double distance, int dontMoveDist) {
		Point moveTo = getPointWithColor(color, distance);
		if (moveTo != null) {
			if (ColorUtil.getDistanceBetween(Mouse.getMouseLocation(), moveTo) > dontMoveDist)
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

	public static Random random = new Random();

	public static final int random(int maxValue) {
		if (maxValue <= 0)
			return 0;
		return random.nextInt(maxValue);
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
		} catch (Exception e) {

		}
	}

	public static Color getColorAt(BufferedImage i, Point p) {
		int[] pixel = i.getRaster().getPixel(p.x, p.y, new int[3]);
		return new Color(pixel[0], pixel[1], pixel[2]);
	}

	public static Color getColorAt(BufferedImage i, int x, int y) {
		int[] pixel = i.getRaster().getPixel(x, y, new int[3]);
		return new Color(pixel[0], pixel[1], pixel[2]);
	}
}
