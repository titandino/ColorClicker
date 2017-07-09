package com.colorbot;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import com.colorbot.bot.Bot;

public class NativeKeyHandler implements NativeKeyListener {
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
			Bot.stopScript();
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		
	}
}
