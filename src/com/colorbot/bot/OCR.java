package com.colorbot.bot;

import java.awt.Color;
import java.awt.Rectangle;

public class OCR {

	public static String getUpText() {
		if (RSText.hasFonts()) {
			return RSText.findString(new Rectangle(0, 0, 700, 500), null);
		} else {
			return getUpText();
		}
	}

	public static String getOptionText() {
		return RSText.getOptionsText();
	}

	public static String getTextAt(Rectangle loc) {
		if (RSText.hasFonts()) {
			return RSText.findString(loc, null);
		} else {
			return getUpText();
		}
	}

	public static String getTextFromColor(Rectangle loc, Color c) {
		if (RSText.hasFonts()) {
			return RSText.findString(loc, c);
		} else {
			return getUpText();
		}
	}
}