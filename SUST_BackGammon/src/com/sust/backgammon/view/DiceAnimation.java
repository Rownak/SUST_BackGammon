package com.sust.backgammon.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sust.backgammon.model.Dice;


/**
 * 
 * @author Ahnaf Farhan Rownak
 * @author Mahadi Hasan Nahid
 */

public class DiceAnimation extends JPanel {

	private static final long serialVersionUID = 1L;
	// the GUI components for the dice
	private JLabel die1Label, die2Label, die3Label, die4Label; // extra dice for
																// doubles

	private GridBagConstraints constraints;
	private RollDice rollDice; // animation for the dice
	private Dice dice; // dice object to animate
	private boolean done;

	/**
	 * Dice constructor.
	 * 
	 */
	public DiceAnimation() {
		// set all the GUI variables for the dice JPanel
		setBackground(Color.BLACK);
		constraints = new GridBagConstraints();
		die1Label = new JLabel(new ImageIcon(
				ClassLoader.getSystemResource("images/1.gif")));
		die2Label = new JLabel(new ImageIcon(
				ClassLoader.getSystemResource("images/1.gif")));
		die3Label = new JLabel(new ImageIcon(
				ClassLoader.getSystemResource("images/1.gif")));
		die4Label = new JLabel(new ImageIcon(
				ClassLoader.getSystemResource("images/1.gif")));

		setLayout(new GridBagLayout());

		die1Label.setPreferredSize(new Dimension(48, 48));
		die2Label.setPreferredSize(new Dimension(48, 48));
		showExtra(false);
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(die3Label, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		add(die1Label, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(die2Label, constraints);
		constraints.gridx = 0;
		constraints.gridy = 3;
		add(die4Label, constraints);
		dice = new Dice();
	}// end of constructor

	/**
	 * Executes the swing worker for the animation.
	 */
	public void animatedRoll(byte howManyDice) throws InterruptedException,
			ExecutionException, CancellationException {
		rollDice = new RollDice(this, howManyDice);
		this.done = false;
		rollDice.execute();
	}

	/**
	 * The current thread waits for the dice to be rolled.
	 */
	public void waitForRoll() throws InterruptedException,
			CancellationException, ExecutionException {
		rollDice.get();
		this.done = true;
	}

	/**
	 * Cancels the roll if necessary.
	 */
	public void cancelAnimatedRoll() {
		rollDice.cancel(true);
	}

	/**
	 * Show the extra dice for doubles.
	 */
	public void showExtra(boolean show) {
		die3Label.setVisible(show);
		die4Label.setVisible(show);
	}

	public Dice getDice() {
		return this.dice;
	}

	public boolean isRollingDone() {
		return this.done;
	}

	public void setRollingDone(boolean b) {
		this.done = b;
	}

	/**
	 * Set the dice icon depending on the rolled number.
	 */
	public void setDieIcon(byte dieIconNum, byte dieResult) {
		switch (dieIconNum) {
		case 0:
			die1Label.setIcon(new ImageIcon(ClassLoader
					.getSystemResource("images/" + dieResult + ".gif")));
			break;
		case 1:
			die2Label.setIcon(new ImageIcon(ClassLoader
					.getSystemResource("images/" + dieResult + ".gif")));
			break;
		case 2:
			die3Label.setIcon(new ImageIcon(ClassLoader
					.getSystemResource("images/" + dieResult + ".gif")));
			break;
		case 3:
			die4Label.setIcon(new ImageIcon(ClassLoader
					.getSystemResource("images/" + dieResult + ".gif")));
			break;
		}
	}
}
