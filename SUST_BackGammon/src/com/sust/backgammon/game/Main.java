package com.sust.backgammon.game;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * 
 * @author Ahnaf Farhan Rownak
 * @author Mahadi Hasan Nahid
 */

public class Main {

	public Main() {
		try {
			// select Look and Feel
			UIManager
					.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");
			// com.jtattoo.plaf.texture.TextureLookAndFeel
			// com.jtattoo.plaf.bernstein.BernsteinLookAndFeel
			// com.jtattoo.plaf.hifi.HiFiLookAndFeel
			// start application
			StartGame m = new StartGame();
			m.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			m.setVisible(true);
			m.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent we) {
					if (((StartGame) we.getComponent()).getGame().isRunning()) {
						if (JOptionPane.showConfirmDialog(
								we.getComponent(),
								"Current game is still running.Are you sure you want to exit?",
								"Warning", JOptionPane.YES_NO_OPTION) == 0)
							System.exit(0);
					} else
						System.exit(0);
				}
			});

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Main m = new Main();
	}

}
