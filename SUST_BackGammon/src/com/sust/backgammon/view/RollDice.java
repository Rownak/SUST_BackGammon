package com.sust.backgammon.view;

import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.sust.backgammon.controller.Game;
import com.sust.backgammon.game.StartGame;

/**
 * 
 * @author Ahnaf Farhan Rownak
 * @author Mahadi Hasan Nahid
 */

class RollDice extends SwingWorker<Void, Void> {
	private Random random;
	private byte count;
	private Clip clip;
	private byte randNum1, randNum2; // die numbers
	private DiceAnimation diceAnimation;
	private byte diceToRoll;

	public RollDice(DiceAnimation diceAnimation, byte diceToRoll) {
		random = new Random();

		this.diceToRoll = diceToRoll;
		this.diceAnimation = diceAnimation;
		this.count = 20;
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			System.err.println("Cannot open audio line");
			e.printStackTrace();
		}
	}

	@Override
	protected Void doInBackground() throws Exception {
		Game game = ((StartGame) SwingUtilities.getRoot(diceAnimation))
				.getGame();
		if (!game.isFirstRoll())
			game.setEnabledCheckers(false);
		diceAnimation.showExtra(false);
		diceAnimation.getDice().resetAllDices(); // reset to not consumed
		while (this.count > 0) {
			generateRandForDices(this.diceToRoll);
			playSound("shake.wav");
			this.count--;
			Thread.sleep(100);
		}

		if (clip.isRunning()) {
			clip.stop();
		}
		playSound("roll.wav");

		int[] diceValues = diceAnimation.getDice().getDiceResults();
		if (this.diceToRoll == 1) {
			diceValues[0] = randNum1;
		} else if (this.diceToRoll == 2) {
			diceValues[1] = randNum2;
		} else {
			diceValues[0] = randNum1;
			diceValues[1] = randNum2;
		}

		// code for doubles
		if (randNum1 == randNum2 && this.diceToRoll > 2) {
			diceAnimation.showExtra(true);
			diceAnimation.setDieIcon((byte) 2, randNum1);
			diceAnimation.setDieIcon((byte) 3, randNum1);
			diceValues[2] = randNum1;
			diceValues[3] = randNum2;
		} else {
			diceValues[2] = 0;
			diceValues[3] = 0;
			diceAnimation.getDice().useIndividualDice((byte) 2);
			diceAnimation.getDice().useIndividualDice((byte) 3);
		}
		if (!game.isFirstRoll() && game.getCurrentPlayer().getNum() != 2)
			game.setEnabledCheckers(true);
		return null;
	}

	public void playSound(String soundName) {
		if (clip.isRunning()) {
			return;
		}
		clip.close();
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(ClassLoader
					.getSystemResource("audio/" + soundName));
			clip.open(audioInputStream);
		} catch (Exception ex) {
			System.err.println("Sound error.");
			ex.printStackTrace();
		}
		clip.start();
	}

	/**
	 * Decide which dice to roll.
	 * 
	 * @param diceToRoll
	 *            which dice to roll 1 = upper die, 2 = lower die, any other
	 *            value = both
	 */
	private void generateRandForDices(byte diceToRoll) {
		switch (diceToRoll) {
		case 1:
			randNum1 = (byte) (1 + random.nextInt(6));
			// randNum1 = (byte)3;
			diceAnimation.setDieIcon((byte) 0, randNum1);
			break;
		case 2:
			randNum2 = (byte) (1 + random.nextInt(6));
			// randNum2 = (byte)3;
			diceAnimation.setDieIcon((byte) 1, randNum2);
			break;
		default:
			randNum1 = (byte) (1 + random.nextInt(6));
			// randNum1 = (byte)3;
			diceAnimation.setDieIcon((byte) 0, randNum1);
			randNum2 = (byte) (1 + random.nextInt(6));
			// randNum2 = (byte)3;
			diceAnimation.setDieIcon((byte) 1, randNum2);
			break;
		}
	}
}
