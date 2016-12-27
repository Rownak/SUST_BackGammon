package com.sust.backgammon.controller;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import com.sust.backgammon.game.StartGame;
import com.sust.backgammon.model.Dice;
import com.sust.backgammon.model.Player;
import com.sust.backgammon.view.Board;
import com.sust.backgammon.view.BoardAnimation;


/**
*
* @author Ahnaf Farhan Rownak
* @author Mahadi Hasan Nahid 
*/

public class Game implements Runnable {

	private byte currPlayer; // player indicator ('0' for player 1 (/human), '1'
								// for player 2 (/computer)
	private Player[] players;
	private StartGame frame; // the Main object
	private boolean winnerCheck; // winner check variable
	private boolean rolled; // dice roll check variable

	private Dice realDice; // the real dice of the game
	private boolean firstroll; // used for the first two dice rolls (initial
								// player check)
	private boolean gameStarted; // indicates whether a game has been started
	private Thread gameThread; // thread used to balance the flow of the program
	private byte chk; // used to indicate whether a player has won
	private int winner = 0;

	/**
	 * 1 parameter constructor for the Game object.
	 * 
	 * @param frame
	 *            : the Main class JFrame
	 */

	public Game(StartGame frame) {
		// initialize all the Game variables
		this.frame = frame;
		this.winnerCheck = false;
		this.rolled = false;
		this.players = new Player[2];
		this.firstroll = true;
		this.gameStarted = false;
		players[0] = new Player(1, "You", this);
		players[1] = new Computer(2, this);
	}// end of Game

	/**
	 * Exits the current game.
	 * 
	 */
	public void exitCurrent() {
		// reset the dice, interrupt any rolls and set the firstroll variable to
		// true
		frame.getDiceAnimation().showExtra(false);
		frame.getDiceAnimation().cancelAnimatedRoll();
		firstroll = true;
			
		// interrupt the game thread and end any player turns
		gameThread.interrupt();

		players[0].endTurn();
		players[1].endTurn();
		winnerCheck = false;
		
		StartGame.easyMenuItem.setEnabled(true);
		StartGame.mediumMenuItem.setEnabled(true);
		StartGame.hardMenuItem.setEnabled(true);
	}// end of exitCurrent

	/**
	 * Initializes and starts the thread.
	 * 
	 * It is called when the new game button is pressed
	 */
	public void startGame() {
		gameThread = new Thread(this, "GameThread");
		StartGame.easyMenuItem.setEnabled(false);
		StartGame.mediumMenuItem.setEnabled(false);
		StartGame.hardMenuItem.setEnabled(false);
		gameThread.start();
	}

	/**
	 * Runs the thread.
	 * 
	 * Catches any thread exceptions, runs the infinite game loop
	 */

	public void run() {
		this.gameStarted = true;
		this.setEnabledCheckers(false);
		try {
			setInitial();
		} catch (InterruptedException e1) {
			return;
		} catch (ExecutionException e) {
			return;
		} catch (CancellationException e) {
			return;
		}
		rolled = currPlayer == 1;
		frame.getRollButton().setEnabled(currPlayer == 0);
		
		while (!winnerCheck) {
//			StartGame.easyMenuItem.setEnabled(false);
//			StartGame.mediumMenuItem.setEnabled(false);
//			StartGame.hardMenuItem.setEnabled(false);
			while (rolled) {

				if (firstroll) {
					firstroll = false;
					if (this.getCurrentPlayer().getNum() == 1) {
						frame.getRollButton().setEnabled(true);
						continue;
					}
					continue;
				}
				rolled = false;
				if (this.getCurrentPlayer().getNum() == 1) {
					
					this.rollDice((byte) 3);
					this.getCurrentPlayer().startTurn();
					frame.getRollButton().setEnabled(false);
					frame.getPassButton().setEnabled(false);
					while (!this.getDice().isAllDicesUsed()) {
						if (rolled)
							break;
						if (hasWon()) {
							winnerCheck = true;
							break;
						}
						if(!frame.getBoardAnimation().haveAnyMove(this.getDice())){
							frame.getPassButton().setEnabled(true);
						}
						// else if()
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					this.getCurrentPlayer().endTurn();
					frame.getRollButton().setEnabled(false);
					frame.getPassButton().setEnabled(false);
					rolled = true;
				} else {
					if (hasWon()) {
						winnerCheck = true;
						break;
					}
					this.rollDice((byte) 3);
					long time = System.currentTimeMillis();
					//System.out.println("started");
					((Computer) this.getCurrentPlayer()).play();
					System.out.println("done in "
							+ (System.currentTimeMillis() - time)
							+ " ms (including graphical move)");
					rolled = false;
					frame.getRollButton().setEnabled(true);// enable dice again
				}
				chk = changeTurn(); // next turn
			}
			// perform the check every 900 ms
			if (hasWon()) {
				winnerCheck = true;
				break;
			}
			try {
				Thread.sleep(900);
			} catch (InterruptedException e) {
				System.err.println("Thread interrupted");
				return;
			}
		}
		winnerDialog();

	}// end of run
	
	public void winnerDialog(){
		String winnerName = null;
		
		if(winner==1){
			winnerName = "You";
		}
		else {
			winnerName = "Computer";
		}
		JOptionPane.showMessageDialog(frame,
				 winnerName + " WON :)");
		this.exitCurrent();
	}

	/**
	 * Rolls the dice and sets the initial player.
	 * 
	 */
	public void setInitial() throws InterruptedException, ExecutionException,
			CancellationException {
		// this infinite loop only breaks when the two dice sums are not equal
		while (true) {
			// roll the dice properly (1 for each player)
			this.players[0].startTurn();
			rollDice((byte) 1);
			frame.getDiceAnimation().waitForRoll();
			Thread.sleep(1000);
			this.players[0].endTurn();
			this.players[1].startTurn();
			rollDice((byte) 2);
			frame.getDiceAnimation().waitForRoll();
			Thread.sleep(1000);
			this.players[1].endTurn();
			
			

			// get and check the dice sums
			int res[] = getDice().getDiceResults();
			if (res[0] > res[1]) {
				// player 1 starts first
				this.players[0].startTurn();
				currPlayer = 0;
				break;
			} else if (res[0] < res[1]) {
				// player 2 starts first
				this.players[1].startTurn();
				currPlayer = 1;
				break;
			}
		}
	}// end of setInitial

	/**
	 * Turn changing
	 * 
	 * @return the number of the player who won, 0 if none
	 */
	public byte changeTurn() {
		getCurrentPlayer().endTurn();
		currPlayer = (byte) ((currPlayer + 1) % 2); // get next player

		if (hasWon())
			return currPlayer;
		getCurrentPlayer().startTurn();
		if (currPlayer == 0)
			setEnabledCheckers(true);
		return 0;
	}// end of changeTurn

	/**
	 * Rolls one or both of the dice.
	 * 
	 * @param howManyDice
	 *            : number of die to be rolled (1 for d1, 2 for d2, 3 for both)
	 */
	public void rollDice(byte howManyDice) {
		try {
			frame.getDiceAnimation().animatedRoll(howManyDice);
			frame.getDiceAnimation().waitForRoll();
		} catch (CancellationException e) {
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}
	}// end of rollDice

	public void setRolled() {
		this.rolled = true;
	}

	/**
	 * Checks if current player has won by checking if he gathered all of his
	 * checkers
	 */
	public boolean hasWon() {
		int count1 =frame.getBoardAnimation().getCurrentBoard().countNumOfCheckersInPipes(1);
		int count2 =frame.getBoardAnimation().getCurrentBoard().countNumOfCheckersInPipes(2);
		//System.out.println("Main Game : " + "Count1 : " +count1 + "Count2 : "+count2);
		if(count1 == 0){
			winner = 1;
			return true;
		}
		else if(count2 == 0){
			winner = 2;
			return true;
		}	
		else 
			return false;
	}

	/**
	 * Enables only the checkers of the current player.
	 * 
	 * @param status
	 *            : true to enable, false to disable
	 */
	public void setEnabledCheckers(boolean status) {
		frame.getBoardAnimation().setCheckersStatus(status);
	}

	/*
	 * get methods
	 */

	public Player getCurrentPlayer() {
		return this.players[currPlayer];
	}

	public Player getPlayer(byte b) {
		return this.players[b];
	}

	public Dice getDice() {
		return this.realDice;
	}

	public void setDice(Dice d) {
		this.realDice = d;
	}

	public boolean isFirstRoll() {
		return this.firstroll;
	}

	public boolean isRunning() {
		return gameStarted;
	}

	public Board getCurrentBoard() {
		return this.frame.getBoardAnimation().getCurrentBoard();
	}

	public BoardAnimation getBoardAnimation() {
		return this.frame.getBoardAnimation();
	}
}
