package com.colorbot;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;

import com.colorbot.bot.Bot;
import com.colorbot.window.BotFrame;
import com.colorbot.window.Overlay;

public class Main {
	
    public static void main(String... args) throws Exception {
    	BotFrame frame = new BotFrame();
		frame.setVisible(true);
    	LogManager.getLogManager().reset();
    	Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
    	logger.setLevel(Level.OFF);
    	GlobalScreen.registerNativeHook();
		Bot.robot = new Robot(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());	
		GlobalScreen.addNativeKeyListener(new NativeKeyHandler());
		Overlay.init();
		Bot.init();
		BotFrame.log("Ready to go. Select a script.");
    }
}
