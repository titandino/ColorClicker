package com.colorbot.bot;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.Random;

/**
 * RSBot's human mouse movement calculations
 * @author BenLand100
 */
public class Mouse {

	public static final int DEFAULT_MOUSE_SPEED = 10;
	public static final int DEFAULT_MAX_MOVE_AFTER = 0;
	public static final int msPerBit = 105;
	public static final int reactionTime = 0;
	
	private final static Random random = new java.util.Random();
	
	public static Point getMouseLocation() {
		return new Point((int) MouseInfo.getPointerInfo().getLocation().getX(), (int) MouseInfo.getPointerInfo().getLocation().getY());
	}

	private static void adaptiveMidpoints(final java.util.Vector<Point> points) {
		int i = 0;
		while (i < points.size() - 1) {
			final Point a = points.get(i++);
			final Point b = points.get(i);
			if ((Math.abs(a.x - b.x) > 1) || (Math.abs(a.y - b.y) > 1)) {
				if (Math.abs(a.x - b.x) != 0) {
					final double slope = (double) (a.y - b.y) / (double) (a.x - b.x);
					final double incpt = a.y - slope * a.x;
					for (int c = a.x < b.x ? a.x + 1 : b.x - 1; a.x < b.x ? c < b.x : c > a.x; c += a.x < b.x ? 1 : -1) {
						points.add(i++, new Point(c, (int) Math.round(incpt + slope * c)));
					}
				} else {
					for (int c = a.y < b.y ? a.y + 1 : b.y - 1; a.y < b.y ? c < b.y : c > a.y; c += a.y < b.y ? 1 : -1) {
						points.add(i++, new Point(a.x, c));
					}
				}
			}
		}
	}

	private static Point[] applyDynamism(final Point[] spline, final int msForMove, final int msPerMove) {
		final int numPoints = spline.length;
		final double msPerPoint = (double) msForMove / (double) numPoints;
		final double undistStep = msPerMove / msPerPoint;
		final int steps = (int) Math.floor(numPoints / undistStep);
		final Point[] result = new Point[steps];
		final double[] gaussValues = Mouse.gaussTable(result.length);
		double currentPercent = 0;
		for (int i = 0; i < steps; i++) {
			currentPercent += gaussValues[i];
			final int nextIndex = (int) Math.floor(numPoints * currentPercent);
			if (nextIndex < numPoints) {
				result[i] = spline[nextIndex];
			} else {
				result[i] = spline[numPoints - 1];
			}
		}
		if (currentPercent < 1D) {
			result[steps - 1] = spline[numPoints - 1];
		}
		return result;
	}

	private static double nCk(final int n, final int k) {
		return Mouse.fact(n) / (Mouse.fact(k) * Mouse.fact(n - k));
	}

	private static double fact(final int n) {
		double result = 1;
		for (int i = 1; i <= n; i++) {
			result *= i;
		}
		return result;
	}

	private static long fittsLaw(final double targetDist, final double targetSize) {
		return (long) (Mouse.reactionTime + Mouse.msPerBit * Math.log10(targetDist / targetSize + 1) / Math.log10(2));
	}

	private static double gaussian(double t) {
		t = 10D * t - 5D;
		return 1D / (Math.sqrt(5D) * Math.sqrt(2D * Math.PI)) * Math.exp(-t * t / 20D);
	}

	private static double[] gaussTable(final int steps) {
		final double[] table = new double[steps];
		final double step = 1D / steps;
		double sum = 0;
		for (int i = 0; i < steps; i++) {
			sum += Mouse.gaussian(i * step);
		}
		for (int i = 0; i < steps; i++) {
			table[i] = Mouse.gaussian(i * step) / sum;
		}
		return table;
	}

	private static Point[] generateControls(final int sx, final int sy, final int ex, final int ey, int ctrlSpacing, int ctrlVariance) {
		final double dist = Math.sqrt((sx - ex) * (sx - ex) + (sy - ey) * (sy - ey));
		final double angle = Math.atan2(ey - sy, ex - sx);
		int ctrlPoints = (int) Math.floor(dist / ctrlSpacing);
		ctrlPoints = ctrlPoints * ctrlSpacing == dist ? ctrlPoints - 1 : ctrlPoints;
		if (ctrlPoints <= 1) {
			ctrlPoints = 2;
			ctrlSpacing = (int) dist / 3;
			ctrlVariance = (int) dist / 2;
		}
		final Point[] result = new Point[ctrlPoints + 2];
		result[0] = new Point(sx, sy);
		for (int i = 1; i < ctrlPoints + 1; i++) {
			final double radius = ctrlSpacing * i;
			final Point cur = new Point((int) (sx + radius * Math.cos(angle)), (int) (sy + radius * Math.sin(angle)));
			double percent = 1D - (double) (i - 1) / (double) ctrlPoints;
			percent = percent > 0.5 ? percent - 0.5 : percent;
			percent += 0.25;
			final int curVariance = (int) (ctrlVariance * percent);
			cur.x = (int) (cur.x + curVariance * 2 * random.nextDouble() - curVariance);
			cur.y = (int) (cur.y + curVariance * 2 * random.nextDouble() - curVariance);
			result[i] = cur;
		}
		result[ctrlPoints + 1] = new Point(ex, ey);
		return result;
	}

	private static Point[] generateSpline(final Point[] controls) {
		final double degree = controls.length - 1;
		final java.util.Vector<Point> spline = new java.util.Vector<Point>();
		boolean lastFlag = false;
		for (double theta = 0; theta <= 1; theta += 0.01) {
			double x = 0;
			double y = 0;
			for (double index = 0; index <= degree; index++) {
				final double probPoly = Mouse.nCk((int) degree, (int) index) * Math.pow(theta, index) * Math.pow(1D - theta, degree - index);
				x += probPoly * controls[(int) index].x;
				y += probPoly * controls[(int) index].y;
			}
			final Point temp = new Point((int) x, (int) y);
			try {
				if (!temp.equals(spline.lastElement())) {
					spline.add(temp);
				}
			} catch (final Exception e) {
				spline.add(temp);
			}
			lastFlag = theta != 1.0;
		}
		if (lastFlag) {
			spline.add(new Point(controls[(int) degree].x, controls[(int) degree].y));
		}
		Mouse.adaptiveMidpoints(spline);
		return spline.toArray(new Point[spline.size()]);
	}
	
	public static void moveMouse(int x, int y) {
		moveMouse(x, y, 0);
	}

	public static void moveMouse(int x, int y, int rand) {
		moveMouse(Bot.MOUSE_SPEED, getMouseLocation().x, getMouseLocation().y, x, y, rand, rand);
	}

	public static void moveMouse(final int speed, final int x1, final int y1, final int x2, final int y2, int randX, int randY) {
		if ((x2 == -1) && (y2 == -1)) {
			return;
		}
		if (randX <= 0) {
			randX = 1;
		}
		if (randY <= 0) {
			randY = 1;
		}
		try {
			if ((x2 == x1) && (y2 == y1)) {
				return;
			}
			final Point[] controls = Mouse.generateControls(x1, y1, x2 + random.nextInt(randX), y2 + random.nextInt(randY), 50, 120);
			final Point[] spline = Mouse.generateSpline(controls);
			final long timeToMove = Mouse.fittsLaw(Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)), 10);
			final Point[] path = Mouse.applyDynamism(spline, (int) timeToMove, Mouse.DEFAULT_MOUSE_SPEED);
			//final Point[] path = spline;
			for (final Point aPath : path) {
				Bot.robot.mouseMove(aPath.x, aPath.y);
				try {
					Thread.sleep(Math.max(0, speed - 2 + random.nextInt(4)));
				} catch (final InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		} catch (final Exception e) {

		}
	}

}