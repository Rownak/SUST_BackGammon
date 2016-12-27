package com.sust.backgammon.view;

import com.sust.backgammon.model.Dice;

/**
 * 
 * @author Ahnaf Farhan Rownak
 * @author Mahadi Hasan Nahid
 */

public class Board {
	public int[] numOfCheckInPipe;

	public Board() {
		numOfCheckInPipe = new int[26];
		initCheckers();
	}

	/**
	 * Places all checkers at their initial positions.
	 * 
	 */

	public void initCheckers() {

		numOfCheckInPipe[0] = -2;
		numOfCheckInPipe[5] = 5;
		numOfCheckInPipe[12] = 5;
		numOfCheckInPipe[11] = -5;
		numOfCheckInPipe[18] = -5;
		numOfCheckInPipe[16] = -3;
		numOfCheckInPipe[7] = 3;
		numOfCheckInPipe[23] = 2;
		
//		numOfCheckInPipe[0] = 15;
//
//		numOfCheckInPipe[23] = -15;
		
//		numOfCheckInPipe[0] = 13;
//		numOfCheckInPipe[24] = 2;
//		numOfCheckInPipe[5] = 0;
//		numOfCheckInPipe[12] = 0;
//		numOfCheckInPipe[11] = 0;
//		
//		numOfCheckInPipe[16] = 0;
//		numOfCheckInPipe[7] = -3;
//		numOfCheckInPipe[18] = -2;
//		numOfCheckInPipe[19] = -2;
//		numOfCheckInPipe[20] = -2;
//		numOfCheckInPipe[21] = -2;
//		numOfCheckInPipe[22] = -2;
//		numOfCheckInPipe[23] = -2;
	}// end of initCheckers

	public boolean isValid(int player, int containValue) {
		if (player == 1) {
			if (containValue >= 0 || containValue == -1)
				return true;
		} else {
			if (containValue <= 0 || containValue == 1)
				return true;
		}
		return false;
	}// end of isValid

	// Returns whether the position is inside board bounds or not(excluding
	// graveyards).
	public boolean inBounds(int pos) {
		return pos >= 0 && pos < 24;
	}

	public int barNumForPl(int player) {
		return (player == 1 ? 24 : 25);
	}

	/**
	 * Moves a checker using from/to values
	 * 
	 * @param player
	 *            the number of the player that will make the move
	 * @param from
	 *            the source of the move
	 * @param to
	 *            the destination of the move
	 * @return true if move was successfull and false otherwise
	 */
	public boolean move(int player, int from, int to) {
		int diff = (from - to);
		// check if the direction of movement is correct
		if (diff < 0 && player == 1)
			return false;
		else if (diff > 0 && player == 2 && from != 25)
			return false;
		if (inBounds(to)) {
			if (isValid(player, this.numOfCheckInPipe[to])) {
				int r = Integer.signum(this.numOfCheckInPipe[from]); // the value of r can be -1/1/0
				this.numOfCheckInPipe[from] -= r;
				// check if the pipe contain opposite player's 1 checker in it
				if (-r == this.numOfCheckInPipe[to]) {
					// hit the blot and place into the bar
					this.numOfCheckInPipe[player == 1 ? this.barNumForPl(2)
							: this.barNumForPl(1)] += numOfCheckInPipe[to];
					this.numOfCheckInPipe[to] = 0;
				}
				this.numOfCheckInPipe[to] += r;
				return true;
			}
		}
		return false;
	}// end of move

	/**
	 * Checks if player has graveyard
	 * 
	 * @param numOfCheckInPipe
	 * @param player
	 * @return
	 */
	public boolean hasCheckerInBar(int player) {
		return this.numOfCheckInPipe[barNumForPl(player)] != 0;
	}// end of hasGyard

	// Returns whether player "owns" this checker or not.
	public boolean isPlayerOwnThePipe(int player, int containValue) {
		if (player == 1)
			return containValue > 0;
		else
			return containValue < 0;
	}

	public int getNumOfCheckersInPipe(int index) {
		return this.numOfCheckInPipe[index];
	}

	/**
	 * Counts and returns checkers number
	 * 
	 * @param playerID
	 */
	public int countNumOfPipesContainCheckers(int playerID) // what would this
															// method should
															// return??
	{
		int count = 0;
		for (int i = 0; i < numOfCheckInPipe.length; i++) {
			if (numOfCheckInPipe[i] > 0 && playerID == 1)
				// count += this.numOfCheckInPipe[i];
				count++;
			else if (numOfCheckInPipe[i] < 0 && playerID == 2)
				// count += this.numOfCheckInPipe[i];
				count++;
		}
		// System.out.println("Player : " +playerID + "Count :" +count);
		return count;
	}

	public int countNumOfCheckersInPipes(int playerID) // what would this method
														// should return??
	{
		int count = 0;
		for (int i = 0; i < numOfCheckInPipe.length; i++) {
			if (numOfCheckInPipe[i] > 0 && playerID == 1)
				count += this.numOfCheckInPipe[i];
			// count++;
			else if (numOfCheckInPipe[i] < 0 && playerID == 2)
				count += this.numOfCheckInPipe[i];
			// count++;
		}

		return count;
	}

	// Returns the amount of checkers inside player's home.
	public int homeCount(int player) {
		int count = 0;
		for (int i = 0; i < 6; i++) // white's home
		{
			if (this.isPlayerOwnThePipe(player, this.numOfCheckInPipe[i]))
				count++;
		}
		// System.out.println("Player : " +player + "Home Count :" +count);
		return count;
	}

	/**
	 * Checks if player has checker inside his home before given point argument
	 * 
	 * @param point
	 * @return
	 */
	private boolean hasPrev(int point) {
		for (int i = point + 1; i < 6; i++) {
			if (numOfCheckInPipe[i] > 0)
				return true;
		}
		return false;
	}

	// Return the position to start counting from when moving out of graveyard.
	// THIS METHOD HAVE NEVER BEEN USED
	// public int countFrom(int player)
	// {
	// return player == 1 ? 24 : -1;
	// }

	public boolean canGather(int pos, Dice dice) {
		// check if there are cheackers out of the HOME
		if (homeCount(1) < countNumOfPipesContainCheckers(1)) {
			return false;
		} else {
			int i = 0;
			while (i < 4) {
				if (dice.getIndividualDiceValue(i) - 1 == pos
						&& !dice.isusedDice(i)) {
					dice.useIndividualDice(i);
					return true;
				}
				i++;
			}
			if (!hasPrev(pos) && dice.useBiggerThan(pos)) {
				return true;
			}
			return false;
		}
	}
	public boolean canGatherCheckingPass(int pos, Dice dice) {
		// check if there are cheackers out of the HOME
		if (homeCount(1) < countNumOfPipesContainCheckers(1)) {
			return false;
		} else {
			int i = 0;
			while (i < 4) {
				if (dice.getIndividualDiceValue(i) - 1 == pos
						&& !dice.isusedDice(i)) {
					
					return true;
				}
				i++;
			}
			if (!hasPrev(pos) && dice.useBiggerThanCheckPass(pos)) {
				return true;
			}
			return false;
		}
	}

	/**
	 * Sents the checker at player's point to graveyard
	 * 
	 * @param pos
	 */
	public void sendToBar(int pos) {
		numOfCheckInPipe[pos]--;
		numOfCheckInPipe[this.barNumForPl(1)]++;
	}

	public void removeChecker(int position) {
		if (numOfCheckInPipe[position] > 0)
			numOfCheckInPipe[position]--;
		else if (numOfCheckInPipe[position] < 0)
			numOfCheckInPipe[position]++;
	}
}
