package com.colorbot.util;

import java.awt.Color;

public class ColorTolerance {

	private Color color;
	private Color tolerance;

	public ColorTolerance(Color color, Color tolerance) {
		this.color = color;
		this.tolerance = tolerance;
	}

	public ColorTolerance(Color color, int tolerance) {
		this(color, new Color(tolerance, tolerance, tolerance));
	}

	public Color getColor() {
		return color;
	}

	public Color getTolerance() {
		return tolerance;
	}
}
