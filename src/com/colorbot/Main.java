package com.colorbot;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import com.colorbot.bot.Bot;
import com.colorbot.window.BotFrame;

public class Main {
	
    public static void main(String... args) throws Exception {
    	LogManager.getLogManager().reset();
    	Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
    	logger.setLevel(Level.OFF);
    	
    	GlobalScreen.registerNativeHook();
		Bot.robot = new Robot(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());	
		GlobalScreen.addNativeKeyListener(new NativeKeyHandler());
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BotFrame frame = new BotFrame();
					frame.setVisible(true);
					BotFrame.log("Ready to go. Select a script.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }
}
