package com.sust.backgammon.model;

/**
 * 
 * @author Ahnaf Farhan Rownak
 * @author Mahadi Hasan Nahid
 */

public class Move {
	private int to;
	private int diceNeeded;
	private int scenario;

	public Move(int destination, int individualDiceValue) {
		this.to = destination;
		this.diceNeeded = individualDiceValue;
		// this.scenario = sc;
	}

	public int getDestination() {
		return this.to;
	}

	public int getDiceNeeded() {
		return this.diceNeeded;
	}

	public String toString() {
		return "{ dest = " + to + " dice needed " + diceNeeded + " scenario "
				+ scenario + " }";
	}
}
