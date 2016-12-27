package com.sust.backgammon.model;

/**
 * 
 * @author Ahnaf Farhan Rownak
 * @author Mahadi Hasan Nahid
 */

public class Dice {
	private int[] diceValues; // up to 4
	private boolean[] usedDices; // shows if and which dice are usedDices(used)
									// during this turn

	public Dice() {
		diceValues = new int[4];
		usedDices = new boolean[4];
	}

	/**
	 * Returns the sum of the dice.
	 * 
	 */
	public int getTotal() {
		int total = 0;
		for (int b : diceValues) {
			total += b;
		}
		return total;
	}

	public int[] getDiceResults() {
		return diceValues;
	}

	public int getIndividualDiceValue(int i) {
		return diceValues[i];
	}

	public int getFirstNotUsedDicesNum() {
		for (int b = 0; b < 4; b++) {
			if (!usedDices[b])
				return b;
		}
		return -1;
	}

	public int totalusedDices() {
		int total = 0;
		for (int b = 0; b < 4; b++) {
			if (usedDices[b]) {
				total++;
			}
		}
		return total;
	}

	public boolean isRollDouble() {
		return diceValues[0] == diceValues[1];
	}

	public boolean isusedDice(int i) {
		return usedDices[i];
	}

	public boolean diceAvailable(int value) {
		if (!this.isRollDouble()) {
			if (this.diceValues[0] == value || this.diceValues[1] == value) {
				return !usedDices[diceValues[0] == value ? 0 : 1];
			} else if (this.diceValues[0] + this.diceValues[1] == value)
				return this.totalusedDices() == 2;
			return false;
		} else // bujhlam na ekhon bujhsi n
		{
			int c = (4 - (value / diceValues[0]));
			return this.totalusedDices() <= c;
		}
	}

	public void useAllDices() {
		for (int b = 0; b < 4; b++) {
			usedDices[b] = true;
		}
	}

	/**
	 * Checks whether the player made all their moves.
	 * 
	 */
	public boolean isAllDicesUsed() {
		for (int i = 0; i < 4; i++) {
			if (!usedDices[i]) {
				return false;
			}
		}
		return true;
	}

	public void useDicesWithARoll(int roll) // bujhlam na ekhon bujhsi
	{
		if (roll == this.getTotal()) // check for multimove and consume all dice
		{
			useAllDices();
		} else if (this.isRollDouble()) // consume the number of dice that were
										// used to perform the move
		{
			int temp = getFirstNotUsedDicesNum();
			for (int i = temp; i < temp + (roll / diceValues[0]); i++) {
				usedDices[i] = true;
			}
		} else {
			for (int i = getFirstNotUsedDicesNum(); i < 2; i++) {
				if (diceValues[i] == roll) {
					usedDices[i] = true;
				}
			}
		}
	}

	/**
	 * Consume dice with number bigger than argument t
	 * 
	 * @param t
	 * @return true if an unusedDices die was found and usedDices,false
	 *         otherwise
	 */
	public boolean useBiggerThan(int value) {
		for (int i = 0; i < 4; i++) {
			if (diceValues[i] > value && !usedDices[i]) {
				usedDices[i] = true;
				return true;
			}
		}
		return false;
	}
	public boolean useBiggerThanCheckPass(int value) {
		for (int i = 0; i < 4; i++) {
			if (diceValues[i] > value && !usedDices[i]) {
				
				return true;
			}
		}
		return false;
	}

	public void useIndividualDice(int i) {
		usedDices[i] = true;
	}

	/**
	 * Reset the dice.
	 */
	public void resetAllDices() {
		for (int i = 0; i < 4; i++) {
			usedDices[i] = false;
		}
	}

	public void setDummyDice(int value1, int value2) {
		resetAllDices();
		if (value1 == value2)
			for (int i = 0; i < 4; i++)
				diceValues[i] = value1;
		else {
			diceValues[0] = value1;
			diceValues[1] = value2;
			diceValues[2] = 0;
			diceValues[3] = 0;
		}
	}
}
