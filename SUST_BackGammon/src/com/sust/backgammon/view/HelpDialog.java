package com.sust.backgammon.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class HelpDialog {
	private JButton okButton;
	private JFrame frame;
	
	private String helpMessage = "Help to Play BackGammon\n\n"
			+ "-------------------------------------------\n"
			+ "BACKGAMMON RULES:\n"
			+ "-------------------------------------------\n"
			+ "1. To start the game, each player throws a single die.\n"
			+ " This determines both the player to go first and the numbers to be played.\n"
			+ "\n2. The numbers on the two dice constitute separate moves. \n"
			+ "For example, if a player rolls 5 and 3, \n"
			+ "he may move one checker five spaces to an open point \n"
			+ "and another checker three spaces to an open point, \n"
			+ "or he may move the one checker a total of eight spaces to an open point, but only if the \n"
			+ "intermediate point (either three or five spaces from the starting point) is also open.\n"
			+ "\n3. A player must use both numbers of a roll if this is legally possible \n"
			+ "(or all four numbers of a double). When only one number can be played,\n "
			+ "the player must play that number.\n"
			+ "\n4. Any time a player has one or more checkers on the bar, "
			+ "his first obligation is to enter those \n"
			+ "checker(s) into the opposing home board. A checker is entered by moving \n"
			+ "it to an open point corresponding to one of the numbers on the rolled dice.\n"
			+ "\n5. Once a player has moved all of his fifteen checkers into his home board, \n"
			+ "he may commence bearing off. A player bears off a checker by rolling a number \n"
			+ "that corresponds to the point on which the checker resides, and then removing that \n"
			+ "checker from the board. Thus, rolling a 6 permits the player to remove a checker from the"
			+ " six point.\n"
			+ "\n\n"
			+ "-------------------------------------------\n"
			+ "CONTROLS :\n"
			+ "-------------------------------------------\n"
			+ "1.Press roll button to roll the dices\n"
			+ "\n2. To move a checker press that checker and drag to the destination. \n"
			+ "\n3.If you do not have any move then press pass button\n"
			+ "\n4.For bearing off the checkers press right click on the checker\n\n\n"
			+ "-------------------------------------------\n"
			+ "REFERENCE :\n"
			+ "-------------------------------------------\n"
			
			
			+ "http://www.bkgm.com/rules.html\n";
	public HelpDialog() {

		JPanel middlePanel = new JPanel();
		middlePanel.setBorder(new TitledBorder(new EtchedBorder(),
				"Help To Play BackGammon"));

		// create the middle panel components

		JTextArea display = new JTextArea(25, 58);
		display.setText(helpMessage);
		
		display.setEditable(false); // set textArea non-editable
		JScrollPane scroll = new JScrollPane(display);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		// Add Textarea in to middle panel
		middlePanel.add(scroll);

		okButton = new JButton("OK");

		middlePanel.add(okButton);
		// My code
		frame = new JFrame();
		frame.setTitle("Help");
		frame.add(middlePanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
	}

	public static void main(String[] args) {
		new HelpDialog();
	}
}
