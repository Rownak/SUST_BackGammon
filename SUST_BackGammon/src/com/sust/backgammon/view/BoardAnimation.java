package com.sust.backgammon.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.sust.backgammon.game.StartGame;
import com.sust.backgammon.model.Checker;
import com.sust.backgammon.model.Dice;
import com.sust.backgammon.model.Move;

/**
 * 
 * @author Ahnaf Farhan Rownak
 * @author Mahadi Hasan Nahid
 */

public class BoardAnimation extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// board img
	private static final Image boardImg = new ImageIcon(
			ClassLoader.getSystemResource("images/board.jpg")).getImage();
	// image that represents a possible move
	private static final Image pssblMoveImgU = new ImageIcon(
			ClassLoader.getSystemResource("images/arrowDown.png")).getImage();
	// same image but flipped
	private static final Image pssblMoveImgL = new ImageIcon(
			ClassLoader.getSystemResource("images/arrowUp.png")).getImage();
	private static ArrayList<Move> possibleMoves; // possible moves to draw
	
	private static Board currentBoard; // current board to animate
	private static Rectangle[] checkerGroup; // checker groups are represented
												// by a rectangle
	private static ArrayList<Checker> whiteCheckers;
	private static ArrayList<Checker> redCheckers;

	public BoardAnimation() {
		possibleMoves = new ArrayList<Move>();
		
		whiteCheckers = new ArrayList<Checker>();
		redCheckers = new ArrayList<Checker>();
		Dimension size = new Dimension(800, 600);
		createPoints();
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
		this.setVisible(true);
	}

	/**
	 * Creates all the checker positions/points and the "graveyards".
	 * 
	 */
	private void createPoints() {
		// initial coordinates on the board

		// short startX = 733;
		// short startY = 330;
		short startX = 730;
		short startY = 335;

		// short width = 55;
		// short height = 250;

		short widthOfChecker = 62;
		short heightOfChecker = 245;
		checkerGroup = new Rectangle[26];
		int b = 0;
		for (; b < 24; b++) // foreach board position
		{
			// at positions 6, 12 and 18 there's a gap on the board
			// that has to be skipped
			if (b == 6) {
				// startX -= 2 * width;
				startX -= 40;
			} else if (b == 12) {
				// startY -= 310;
				// startX = 17;
				startY = 21;
				startX = 14;
			} else if (b == 18) {
				// startX += 2 * width;
				startX += 40;
			}

			// for each position make a new checker group
			checkerGroup[b] = new Rectangle(startX, startY, widthOfChecker,
					heightOfChecker);

			// proceed to next position
			if (b < 11) {
				startX -= widthOfChecker;
			} else {
				startX += widthOfChecker;
			}

		}

		// create the graveyard areas
		for (; b < checkerGroup.length; b++) {
			if (b == 24) {
				// White graveyard
				// startX = 378;
				// startY = 20;
				startX = 373;
				startY = 16;
			} else {
				// Red graveyard

				startX = 373;
				startY = 340;
			}
			checkerGroup[b] = new Rectangle(startX, startY, widthOfChecker,
					heightOfChecker);
		}
	}// end of createPoints

	/**
	 * Animates currBoard by adding Checkers(JLabels) to the JPanel
	 */
	public void animateBoard() {
		for (int i = 0; i < 15; i++) {
			whiteCheckers.add(new Checker(1));
			this.add(whiteCheckers.get(i));
			redCheckers.add(new Checker(2));
			redCheckers.get(i).setEnabled(false);
			this.add(redCheckers.get(i));
		}
	}

	/**
	 * The Board "painter".
	 * 
	 * @param g
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		short pointX = 0, pointY = 0;
		g.drawImage(boardImg, 0, 0, 800, 600, null);
		for (Move move : possibleMoves) {
			// System.out.println("Move To : "+m.getDestination());
			pointX = (short) (checkerGroup[move.getDestination()].getX()
					+ (checkerGroup[move.getDestination()].getWidth() / 2) - 12);
			if (move.getDestination() < 12) // bottom half
			{
				pointY = (short) (checkerGroup[move.getDestination()].getY() + checkerGroup[move
						.getDestination()].getHeight());
				g.drawImage(pssblMoveImgL, pointX, pointY, null, this);
			} else
			// top half
			{
				pointY = (short) (checkerGroup[move.getDestination()].getY() - 22);
				g.drawImage(pssblMoveImgU, pointX, pointY, null, this);
			}

		}
		if (Checker.getDraggedStatus() || currentBoard == null) {
			return;
		}

		decorateTheCheckers(pointX, pointY);

	}

	// calculate X and Y and place the checker at right place
	private void decorateTheCheckers(int pointX, int pointY) {
		int index1 = 0, index2 = 0;
		ArrayList<Checker> arr = null;
		for (int b = 0; b < 26; b++) {
			int total = currentBoard.getNumOfCheckersInPipe(b);
			int index;
			if (total == 0)
				continue;
			else if (total > 0) {
				arr = whiteCheckers;
				index = index1;
			} else {
				arr = redCheckers;
				index = index2;
			}
			total = Math.abs(total);
			int cNum = 0;
			pointX = (short) (checkerGroup[b].getX());
			while (total != cNum) {
				/*
				 * check if the checker is placed within the bottom , the
				 * graveyards or the top half of the board and calculate the
				 * right y value
				 */
				if (b < 12) // bottom half
				{
					pointY = (short) (checkerGroup[b].getY()
							+ checkerGroup[b].getHeight()
							- arr.get(index).getHeight() - ((cNum >= 5) ? 4
							: cNum) * arr.get(index).getHeight());
				} else if (b >= 12 && b < 24)// top half
				{
					pointY = (short) (checkerGroup[b].getY() + ((cNum >= 5) ? 4
							: cNum) * arr.get(index).getHeight());
				} else if (b == 24) {
					pointY = (short) (checkerGroup[b].getY()
							+ checkerGroup[b].getHeight()
							- arr.get(index).getHeight() - ((cNum >= 5) ? 4
							: cNum) * arr.get(index).getHeight());
				} else {
					pointY = (short) (checkerGroup[b].getY() + ((cNum >= 5) ? 4
							: cNum) * arr.get(index).getHeight());
				}
				arr.get(index).setText("");
				arr.get(index).setLocation(pointX, pointY);
				arr.get(index).setPoint(b);
				if (cNum >= 5) // if more than 5 checkers in this group
				{
					// show a counter that indicates how many checkers are in
					// this group.The counter is shown only at the last (first
					// added) checker
					arr.get(index - cNum).setText(String.valueOf(total));
				}
				if (arr == whiteCheckers) {
					index1++;
					index++;
				} else {
					index2++;
					index++;
				}
				cNum++;
			}
		}

	}

	/**
	 * Using interpolation formula p(t) = p1*(1-t) + p2*t to animate Computer's
	 * move t is normalized ( t[0,1] )
	 */
	// It's Only for Bot

	/**
	 * Checks and controls the movement of a checker.
	 * 
	 * @param from
	 *            point on the board where the checker will be moved
	 * @param to
	 *            point on the board where the checker was taken
	 * @param owner
	 *            the owner of the checker (player 1 or 2)
	 * @param diceCheck
	 *            true if used to validate the move
	 * @return true if the checker was moved successfully(valid move),false if
	 *         the move was invalid thus the checker wasn't moved
	 */
	public boolean placeChecker(int from, int to, int owner, boolean diceCheck) {
		if (diceCheck) {
			Dice dice = ((StartGame) SwingUtilities.getRoot(this)).getGame()
					.getDice();
			int toCheck = Math.abs(to - from);
			if (dice.diceAvailable(toCheck)) {
				if (currentBoard.move(owner, from, to)) {
					dice.useDicesWithARoll(toCheck);
					return true;
				}
			}
		} else {
			if (currentBoard.move(owner, from, to))
				return true;
		}
		return false;
	}

	/**
	 * Checks whether the point given is inside the board.
	 * 
	 * @param p
	 *            : the point
	 * @return the number of the checker group if the point is valid, -1 else
	 */
	public int isInside(Point p) {
		for (short i = 0; i < 24; i++) {
			if (checkerGroup[i].contains(p)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Sets the status(enabled/disabled) of player's checkers
	 * 
	 * @param status
	 *            true=enabled , false=disabled
	 */
	public void setCheckersStatus(boolean status) {
		ArrayList<Checker> checkers = whiteCheckers;
		for (int b = 0; b < checkers.size(); b++) {
			checkers.get(b).setEnabled(status);
		}
	}


	/**
	 * Removes the checker from its stack. This method is used for player's
	 * gathering state.
	 * 
	 * @param point
	 *            : the current point of the checker
	 */
	public void removeChecker(Checker chk) {
		Dice dice = ((StartGame) SwingUtilities.getRoot(this)).getGame()
				.getDice();
		if (currentBoard.canGather(chk.getPoint(), dice)) {
			whiteCheckers.remove(chk);
			currentBoard.removeChecker(chk.getPoint());
			this.remove(chk);
			String s = "Beard of: W: "+(15-getCurrentBoard().countNumOfCheckersInPipes(1))+", R: "+(15-Math.abs(getCurrentBoard().countNumOfCheckersInPipes(2)));
			StartGame.bearedOfLabel.setText(s);
			this.revalidate();
			this.repaint();
		}
	}
	public void removeChecker(int point) {
		
		Checker checkToRemv = this.getTopChecker(point);
//		System.out.println("Erorrrrrrrrrrrr Point : remove From : " + point
//				+ "CHecker Point : " + checkToRemv.getPoint());
		redCheckers.remove(checkToRemv);
		currentBoard.removeChecker(point);
		this.remove(checkToRemv);
		String s = "Beard of: W: "+(15-getCurrentBoard().countNumOfCheckersInPipes(1))+", R: "+(15-Math.abs(getCurrentBoard().countNumOfCheckersInPipes(2)));
		StartGame.bearedOfLabel.setText(s);
		this.revalidate();
		this.repaint();
	}

	/**
	 * Removes the checker from its stack. This method is used for computer's
	 * gathering state.
	 * 
	 * @param point
	 *            : the current point of the checker
	 */
	public void animateMove(int from, int to) {
		Checker.setDraggedStatus(true); // repaint of the board is disabled
										// during move operation
		//System.out.println("From : " + from + "To : " + to);
		final Checker src = this.getTopChecker(from);
		Checker dest = this.getTopChecker(to);
		final short x0 = (short) src.getX();
		final short y0 = (short) src.getY();
		final short x1;
		final short y1;
		if (dest != null) // there is at least one checker in checker group
		{
			x1 = (short) dest.getX();
			y1 = (short) dest.getY();
		} else
		// checker group is empty
		{
			x1 = (short) checkerGroup[to].getX();
			y1 = (short) checkerGroup[to].getY();
		}
		Timer t = new Timer(100, new ActionListener() {
			float t = 0.0f;

			public void actionPerformed(ActionEvent e) {
				short x = (short) (x0 * (1 - t) + x1 * t);
				short y = (short) (y0 * (1 - t) + y1 * t);
				t += 0.04f;
				src.setLocation(x, y);
				if (t >= 1.0f) {
					((Timer) e.getSource()).stop();
					Checker.setDraggedStatus(false);

				}
			}
		});
		t.start();
		while (t.isRunning()) {

		}
		this.placeChecker(from, to, src.getOwner(), false);
		this.validate();
		this.repaint();
	}

	public Checker getTopChecker(int targetIndex) {
		int total = currentBoard.getNumOfCheckersInPipe(targetIndex);
		if (total >= 0) {
			//System.out.println("Nulllllllll");
			return null;
		}

		int b = 0;
		// which chekcer to move to targetIndex
		for (; b < redCheckers.size(); b++) {
			if (redCheckers.get(b).getPoint() > targetIndex) {
//				System.out.println("b-1 : " + (b - 1));
//				System.out.println("Point : "
//						+ redCheckers.get(b - 1).getPoint());
				return redCheckers.get(b - 1);
			}
		}
		//System.out.println("OUt Of Loop  b-1 : " + (b - 1));
		return redCheckers.get(b - 1);
	}



	/**
	 * Adds all possible moves that can be performed by Checker c
	 * 
	 * @param c
	 */
	public void addPossibleMovesOf(Checker c) {
		possibleMoves.addAll(c.getPossibleMoves());
	}
	public Boolean haveAnyMove(Dice dice) {
		 ArrayList<Move> allPossibleMoves = new ArrayList<Move>();
		

		boolean canGather = false;
		for(int i=0; i<25; i++){
			if(this.getCurrentBoard().numOfCheckInPipe[i]>0){
				allPossibleMoves.addAll(getAllPossibleMoves(i));
				canGather=this.getCurrentBoard().canGatherCheckingPass(i, dice);
				
			}
		}
		if(canGather || allPossibleMoves.size()>0){
			//System.out.println("Have move..........................");
			allPossibleMoves = null;
			return true;
		}
		else{
			//System.out.println("No move..........................");
			allPossibleMoves = null;
			return false;
		}
		
//		System.out.println("PossibleMoves:");
//		for(int i=0; i<allPossibleMoves.size(); i++){
//			System.out.println("move : " +allPossibleMoves.get(i).getDestination());
//		}
	}

	/**
	 * Clears all possible moves inside pmoves ArrayList.
	 * 
	 */
	public void clearPossibleMoves() {
		possibleMoves.clear();
	}

	/**
	 * Clears all the checker groups (empties board).
	 * 
	 */
	public void clearBoard() {
		for (int b = 0; b < whiteCheckers.size(); b++) {
			this.remove(whiteCheckers.get(b));
		}
		for (int b = 0; b < redCheckers.size(); b++) {
			this.remove(redCheckers.get(b));
		}
		// repaint and validate the board
		this.validate();
		this.repaint();
	}// end of clear Board

	/**
	 * Sets the current board
	 * 
	 * @param b
	 */
	public void setCurrentBoard(Board board) {
		currentBoard = board;
	}

	public Board getCurrentBoard() {
		return currentBoard;
	}
	public ArrayList<Move> getAllPossibleMoves(int point) {
		ArrayList<Move> moves = new ArrayList<Move>();

		int op = -1;
		
		// get current dice & board obj
		Dice dice = ((StartGame) SwingUtilities.getRoot(this)).getGame()
				.getDice();
		Board board = this.getCurrentBoard();
		int pos, diceToUse = 0;
		if (board.hasCheckerInBar(1)) {
			if (point != board.barNumForPl(1)) {
				return moves;
			}
		}
		if (dice.isRollDouble()) {
			int counter = 1;
			diceToUse = dice.totalusedDices();
			while (diceToUse != 4) {
				pos = (point + (((counter++)) * op * dice
						.getIndividualDiceValue(diceToUse)));
				if (board.inBounds(pos)) {
					if (board.isValid(1,
							board.getNumOfCheckersInPipe(pos))) {
						moves.add(new Move(pos, dice
								.getIndividualDiceValue(diceToUse)));
						diceToUse++;
					} else {
						break;
					}
				} else {
					break;
				}
			}
		} else {
			for (diceToUse = 0; diceToUse < 2; diceToUse++) {
				if (dice.isusedDice(diceToUse)) {
					continue;
				}
				pos = (point + (op * dice
						.getIndividualDiceValue(diceToUse)));
				if (board.inBounds(pos)) {
					if (board.isValid(1,
							board.getNumOfCheckersInPipe(pos))) {
						moves.add(new Move(pos, dice
								.getIndividualDiceValue(diceToUse)));
					}
				}

			}
			
			if (moves.size() >= 1 && dice.totalusedDices() == 2) {
				pos = (point + (op * (dice.getIndividualDiceValue(0) + dice
						.getIndividualDiceValue(1))));
				if (board.inBounds(pos)) {
					if (board.isValid(1,
							board.getNumOfCheckersInPipe(pos))) {
						moves.add(new Move(pos, dice
								.getIndividualDiceValue(diceToUse)));
					}
				}
			}
		}
		return moves;
	}
}
