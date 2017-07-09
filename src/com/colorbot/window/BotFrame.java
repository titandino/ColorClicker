package com.colorbot.window;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.colorbot.bot.Bot;
import com.colorbot.script.Script;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class BotFrame extends JFrame {
	private static final long serialVersionUID = -4685478929788419792L;
	
	public static JPanel contentPane;
	public static JTextArea log = new JTextArea();
	public static JComboBox<Script> scripts;

	public BotFrame() {
		setTitle("Trent's Shitty Color Bot");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel currentScriptLabel = new JLabel("Script:");
		contentPane.add(currentScriptLabel);
		
	    scripts = new JComboBox<Script>(Bot.SCRIPT_LIST);
	    scripts.setVisible(true);
	    contentPane.add(scripts);
		
		JButton stopButton = new JButton("Stop Script (F10)");
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Bot.stopScript();
			}
		});
		
		contentPane.add(stopButton);
		
		JButton startButton = new JButton("Start Script (F9)");
		contentPane.add(startButton);
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Bot.currentScript == null)
					Bot.runScript((Script) scripts.getSelectedItem());
				else
					log("Already running a script.");
			}
		});
		
		log = new JTextArea(10, 30);
		log.setMaximumSize(new Dimension(10, 30));
		log.setEditable(false);
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		log.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		log.setVisible(true);
		JScrollPane scrollPane = new JScrollPane( log );
		contentPane.add(scrollPane);
	}
	
	public static void log(String text) {
		log.append(text+"\n");
		log.setCaretPosition(log.getDocument().getLength());
	}

}
