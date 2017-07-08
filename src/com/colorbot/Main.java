package com.colorbot;

import java.awt.*;
import com.colorbot.bot.Bot;
import com.colorbot.script.impl.Menaphites;

public class Main {
    public static void main(String... args) throws Exception {
		Bot.robot = new Robot(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
		
		Bot.runScript(new Menaphites());
    }
}
