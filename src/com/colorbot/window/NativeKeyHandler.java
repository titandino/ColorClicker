package com.colorbot.window;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import com.colorbot.bot.Bot;
import com.colorbot.script.Script;

public class NativeKeyHandler implements NativeKeyListener {
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		if (e.getKeyCode() == NativeKeyEvent.VC_F10) {
			Bot.stopScript();
		}
		if (e.getKeyCode() == NativeKeyEvent.VC_F9) {
			if (Bot.currentScript == null)
				Bot.runScript((Script) BotFrame.scripts.getSelectedItem());
			else
				BotFrame.log("Already running a script.");
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {

	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {

	}
}
