package com.sust.backgammon.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.sust.backgammon.game.StartGame;
import com.sust.backgammon.view.Board;
import com.sust.backgammon.view.BoardAnimation;

/**
 * 
 * @author Ahnaf Farhan Rownak
 * @author Mahadi Hasan Nahid
 */

public class Checker extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// variables used for the checker moving
	private short screenX = 0;
	private short screenY = 0;
	private short panelX = 0;
	private short panelY = 0;
	private Color color;
	private String text; // text for first checker of each group (number of
							// checkers in stack)
	private static boolean isDragged; // true if any checker object is currently
										// being dragged
	private static boolean disabledPress; // true if mouse was pressed while
											// component was disabled
	private boolean top; // marks whether the checker is the first one at its
							// board position
	private int point; // point where the checker is
	public static final int radius = 25; // radius

	private static ArrayList<Move> pmoves;

	/**
	 * Checker constructor.
	 * 
	 * @param owner
	 */
	public Checker(int owner) {
		this.color = owner == 1 ? Color.white : new Color(153, 0, 0);
		setBounds(0, 0, 1 + 2 * radius, 1 + 2 * radius); // set checker
															// graphical bounds
		text = "";
		addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				// check if the right click is used while the checker is enabled
				// and remove it
				if (e.getButton() == MouseEvent.BUTTON3
						&& e.getComponent().isEnabled()) {
					BoardAnimation ba = (BoardAnimation) e.getComponent()
							.getParent();
					ba.removeChecker((Checker) e.getComponent());
				}
				e.consume();
			}

			public void mousePressed(MouseEvent e) {
				// if not left button then consume
				if (e.getModifiers() != InputEvent.BUTTON1_MASK) {
					e.consume();
					return;
				} else if (!e.getComponent().isEnabled()) {
					Checker.disabledPress = true;
					e.consume();
					return;
				}
				Checker.disabledPress = false;
				// if the left click is pressed while the checker is enabled
				// get the coordinates
				// screen X from DEXTOP Resolution
				screenX = (short) e.getXOnScreen();
				screenY = (short) e.getYOnScreen();
				// X from app panel
				panelX = (short) getX();
				panelY = (short) getY();

				BoardAnimation ba = (BoardAnimation) e.getComponent()
						.getParent();
				Checker c = (Checker) e.getComponent();
				ba.addPossibleMovesOf(c);
				ba.repaint();
			}

			public void mouseReleased(MouseEvent e) {
				if (e.getModifiers() != InputEvent.BUTTON1_MASK
						|| !e.getComponent().isEnabled()) {
					e.consume();
					return;
				}
				Checker.disabledPress = false;
				Checker.isDragged = false;
				// when the left click is released, get the new coordinates
				short x = (short) e.getComponent().getX();
				short y = (short) e.getComponent().getY();
				BoardAnimation ba = (BoardAnimation) e.getComponent()
						.getParent();
				ba.clearPossibleMoves();
				ba.repaint();
				pmoves = getPossibleMoves();
				// check if the point where the checker was moved is valid
				int chk = ba.isInside(new Point(x
						+ (e.getComponent().getWidth() / 2), (y + (e
						.getComponent().getHeight() / 2))));
				Boolean isPossible = false;
				for (Move m : pmoves) {
					if (m.getDestination() == chk) {
						// System.out.println("Possible");
						isPossible = true;
					}
				}

				if (isPossible) {
					// move is invalid (chk == -1) or trying to move to current
					// point

					if (chk == -1 || chk == ((Checker) e.getComponent()).point) {
						resetPos();
					} else {
						if (!ba.placeChecker(point, chk,
								((Checker) e.getComponent()).getOwner(), true))
							resetPos();
					}
				} else {
					resetPos();
				}
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}
		}); // end of MouseListener

		addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				if (e.getModifiers() != InputEvent.BUTTON1_MASK
						|| !e.getComponent().isEnabled()
						|| Checker.disabledPress) {
					e.consume();
					return;
				}
				// if the left click is used to drag the checker, define and set
				// the new point for the checker
				short deltaX = (short) (e.getXOnScreen() - screenX);
				short deltaY = (short) (e.getYOnScreen() - screenY);
				short newX = (short) (panelX + deltaX);
				short newY = (short) (panelY + deltaY);
				Checker.isDragged = true;
				((Checker) e.getComponent()).setText("");
				setLocation(newX, newY);
			}

			public void mouseMoved(MouseEvent e) {
			}
		});
	}// end of Checker constructor

	@Override
	protected void paintComponent(Graphics g) {
		// GUI tweaks and coloring
		super.paintComponent(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(new Color(0, 0, 0));
		g.drawOval(0, 0, 2 * radius, 2 * radius);
		// color for the checkers
		g.setColor(this.color);
		g.fillOval(1, 1, (2 * radius) - 1, (2 * radius) - 1);
		// Fill the inner Circle
		if (this.color.equals(Color.white)) {
			g.setColor(new Color(63, 63, 63));
		} else {
			g.setColor(new Color(255, 99, 71));
		}
		g.fillOval(radius - (radius / 2), radius - (radius / 2), radius, radius);
		// TEXT COLOR that appears when there's greater than 5 checkers
		if (this.color.equals(Color.white)) {
			g.setColor(Color.BLACK);
		} else {
			g.setColor(Color.WHITE);
		}
		// text alignment based on text's length
		if (text.length() == 1) {
			g.drawString(text, radius - radius / 7, radius + radius / 5);
		}

		else
			g.drawString(text, radius - radius / 4, radius + radius / 5);
	}// end of paintComponent

	public ArrayList<Move> getPossibleMoves() {
		ArrayList<Move> moves = new ArrayList<Move>();
		int op = (this.getOwner() == 1 ? -1 : 1);
		// get current dice & board obj
		Dice dice = ((StartGame) SwingUtilities.getRoot(this)).getGame()
				.getDice();
		Board board = ((BoardAnimation) this.getParent()).getCurrentBoard();
		int pos, diceToUse = 0;
		if (board.hasCheckerInBar(1)) {
			if (this.getPoint() != board.barNumForPl(1)) {
				return moves;
			}
		}
		if (dice.isRollDouble()) {
			int counter = 1;
			diceToUse = dice.totalusedDices();
			while (diceToUse != 4) {
				pos = (this.getPoint() + (((counter++)) * op * dice
						.getIndividualDiceValue(diceToUse)));
				if (board.inBounds(pos)) {
					if (board.isValid(this.getOwner(),
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
				pos = (this.getPoint() + (op * dice
						.getIndividualDiceValue(diceToUse)));
				if (board.inBounds(pos)) {
					if (board.isValid(this.getOwner(),
							board.getNumOfCheckersInPipe(pos))) {
						moves.add(new Move(pos, dice
								.getIndividualDiceValue(diceToUse)));
					}
				}

			}
			// bujhlam na
			if (moves.size() >= 1 && dice.totalusedDices() == 2) {
				pos = (this.getPoint() + (op * (dice.getIndividualDiceValue(0) + dice
						.getIndividualDiceValue(1))));
				if (board.inBounds(pos)) {
					if (board.isValid(this.getOwner(),
							board.getNumOfCheckersInPipe(pos))) {
						moves.add(new Move(pos, dice
								.getIndividualDiceValue(diceToUse)));
					}
				}
			}
		}
		return moves;
	}
	

	private void resetPos() {
		setLocation(panelX, panelY);
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getOwner() {
		return (this.color.equals(Color.white) ? 1 : 2);
	}

	public static boolean getDraggedStatus() {
		return Checker.isDragged;
	}

	public static void setDraggedStatus(boolean val) {
		Checker.isDragged = val;
	}

	public int getPoint() {
		return this.point;
	}

	public void setPoint(int p) {
		this.point = p;
	}

	public void setTop(boolean val) {
		this.top = val;
	}

	public boolean getTop() {
		return this.top;
	}
}
