package com.sust.backgammon.model;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JLabel;

import com.sust.backgammon.controller.Game;

/**
 * 
 * @author Ahnaf Farhan Rownak
 * @author Mahadi Hasan Nahid
 */

public class Player {
	private ArrayList<Checker> checkers;
	private int playerNum;
	private String name;
	private JLabel jlabel;
	private Game game;
	private boolean hasCheckerInBar;
	private int score; // number of checkers gathered

	/**
	 * Player constructor.
	 * 
	 * Initializes the player checkers.
	 */
	public Player(int playerNum, String str, Game game) {
		this.playerNum = playerNum;
		this.name = str;
		this.game = game;
		this.hasCheckerInBar = false;
		this.score = 0;
		jlabel = new JLabel(this.name);
		jlabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		jlabel.setOpaque(true);
		jlabel.setBackground(new Color(153, 0, 0));
		// jl.setForeground(Color.BLUE);
	}

	public Checker getChecker(int index) {
		return checkers.get(index);
	}

	public JLabel getLabel() {
		return jlabel;
	}

	public String getName() {
		return name;
	}

	/**
	 * Switch the background color of the names for each turn.
	 */
	public void startTurn() {
		jlabel.setBackground(Color.GREEN);
	}

	public void endTurn() {
		jlabel.setBackground(new Color(153, 0, 0));
	}

	public Game getGame() {
		return this.game;
	}

	public int getNum() {
		return playerNum;
	}

	public void setGraveyard(boolean status) {
		this.hasCheckerInBar = status;
	}

	public boolean hasGraveyard() {
		return this.hasCheckerInBar;
	}
}
