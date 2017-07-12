package com.colorbot.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Window;

import javax.swing.JComponent;

import com.colorbot.bot.Bot;
import com.sun.awt.AWTUtilities;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;

public class Overlay {

	private static Window w = new Window(null);

	public static JComponent ovl = new JComponent() {
		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g) {

		}

		public Dimension getPreferredSize() {
			return new Dimension(Bot.SCREEN_WIDTH, Bot.SCREEN_HEIGHT);
		}
	};

	public static void init() {
		w.add(ovl);
		w.pack();
		w.setLocationRelativeTo(null);
		w.setVisible(true);
		w.setAlwaysOnTop(true);
		AWTUtilities.setWindowOpaque(w, false);
		WinDef.HWND hwnd = getHWnd(w);
		int wl = User32.INSTANCE.GetWindowLong(hwnd, WinUser.GWL_EXSTYLE);
		wl = wl | WinUser.WS_EX_LAYERED | WinUser.WS_EX_TRANSPARENT;
		User32.INSTANCE.SetWindowLong(hwnd, WinUser.GWL_EXSTYLE, wl);
		BotFrame.log("Overlay initialized.");
	}

	private static HWND getHWnd(Component w) {
		HWND hwnd = new HWND();
		hwnd.setPointer(Native.getComponentPointer(w));
		return hwnd;
	}

	public static void drawRect(Rectangle r) {
		Graphics g = ovl.getGraphics();
		g.setColor(new Color(0, 255, 0));
		g.drawRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
		ovl.paint(g);
		ovl.repaint();
	}
}