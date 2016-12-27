package com.sust.backgammon.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import com.sust.backgammon.controller.Expectiminimax;
import com.sust.backgammon.controller.Game;
import com.sust.backgammon.model.Player;
import com.sust.backgammon.view.*;
import com.sust.backgammon.game.*;

/**
 * 
 * @author Ahnaf Farhan Rownak
 * @author Mahadi Hasan Nahid
 */

public class StartGame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JMenuBar menuBar;
	private JMenu newGameMenu;
	private JMenu levelMenu;
	private JMenu helpMenu;
	private JMenuItem aboutMenuItem;
	private JMenuItem helpMenuItem;
	private JMenuItem startGameMenuItem;
	private JMenuItem exitMenuItem;
	
	public static JMenuItem easyMenuItem;
	public static JMenuItem mediumMenuItem;
	public static JMenuItem hardMenuItem;
	

	public JLabel levelLabel;
	public static JLabel bearedOfLabel;
	public JLabel bearedOfLabelPl2;
	// GUI components
	
	private JButton newGameButton;
	
	
	private JButton rollButton;
	private JButton passButton;
	// Board and Game objects
	private Board board;
	private Game game;
	private BoardAnimation boardAnimation;
	private DiceAnimation diceAnimation; // the DiceAnimation object
	private final String BACKGROUND_PATH = "images/background1.jpg";
	private final int WIDTH = 1100;
	private final int HEIGHT = 725;

	/**
	 * Main is treated as an object because it's a JFrame.
	 * 
	 * The GUI components are initiated and set in this constructor
	 */

	public StartGame() {
		// ----------basic layout settings--------------
		setTitle("SUST_BACKGAMMON");
		setSize(WIDTH, HEIGHT);

		// -----------set the ToolBar, Buttons' Background and Foreground
		// variables-------------
		initVariables();
		addActionListeners();

		setLocationRelativeTo(null);

	}// end of Main

	/**
	 * Action listener for the game buttons.
	 * 
	 * @param e
	 *            : the action event
	 */

	private void initVariables() {

		setContentPane(new JLabel(new ImageIcon(
				ClassLoader.getSystemResource(BACKGROUND_PATH))));
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		// ----------------------MenuBar---------------------------

		menuBar = new JMenuBar();
		newGameMenu = new JMenu("New Game");
		helpMenu = new JMenu("Help");

		helpMenuItem = new JMenuItem("Help");
		aboutMenuItem = new JMenuItem("About");
		helpMenu.add(helpMenuItem);
		helpMenu.add(aboutMenuItem);
		levelMenu = new JMenu("Level");

		menuBar.add(newGameMenu);
		menuBar.add(levelMenu);
		menuBar.add(helpMenu);

		setJMenuBar(menuBar);
		startGameMenuItem = new JMenuItem("Start");
		exitMenuItem = new JMenuItem("Exit");
		newGameMenu.add(startGameMenuItem);
		newGameMenu.add(exitMenuItem);

		easyMenuItem = new JMenuItem("Easy");
		mediumMenuItem = new JMenuItem("Medium");
		hardMenuItem = new JMenuItem("Hard");

		levelMenu.add(easyMenuItem);
		levelMenu.add(mediumMenuItem);
		levelMenu.add(hardMenuItem);


		rollButton = new JButton("Roll");
		rollButton.setBackground(new Color(153, 0, 0));
		rollButton.setForeground(Color.BLACK);
		rollButton.setEnabled(false);
		rollButton.addActionListener(this);
		
		passButton = new JButton("No Move Pass");
		passButton.setBackground(new Color(153, 0, 0));
		passButton.setForeground(Color.BLACK);
		passButton.setEnabled(false);
		passButton.addActionListener(this);
		// -----------initialize the Board with the corresponding image and the
		// Game------------
		board = new Board();
		diceAnimation = new DiceAnimation();
		game = new Game(this);
		game.setDice(diceAnimation.getDice());
		boardAnimation = new BoardAnimation();
		// b.setGame(game);

		// -----------set layout constraints and add the
		// components--------------
		//constrints.gridx = 0;
		//constrints.gridy = 0;
		//constrints.insets = new Insets(15, 15, 15, 15);
		// add(gameToolBar, constrints);
		
		if(game.isRunning()) {
			levelMenu.setEnabled(false);
		}
		
		levelLabel = new JLabel("Level: Easy");
		levelLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		levelLabel.setOpaque(true);
		levelLabel.setForeground(Color.WHITE);
		levelLabel.setBackground(new Color(143,83,49));
		
		
		bearedOfLabel = new JLabel("Beared of: W: 0, R: 0");
		bearedOfLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		bearedOfLabel.setOpaque(true);
		bearedOfLabel.setForeground(Color.WHITE);
		bearedOfLabel.setBackground(new Color(143,83,49));
		//bearedOfLabel.setBackground(new Color(153, 0, 0));
		

		constraints.gridx = 1;
		constraints.gridy = 0;
		add(game.getPlayer((byte) 0).getLabel(), constraints);
		constraints.gridx = 2;
		constraints.gridy = 0;
		add(game.getPlayer((byte) 1).getLabel(), constraints);
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(levelLabel, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(boardAnimation, constraints);
		constraints.gridx = 1;
		constraints.gridy = 2;
		add(this.diceAnimation, constraints);
		constraints.gridx = 2;
		constraints.gridy = 2;
		add(rollButton, constraints);
		
		constraints.gridx = 2;
		constraints.gridy = 4;
		add(passButton, constraints);
		

		
		constraints.gridx = 0;
		constraints.gridy = 1;
		add(bearedOfLabel, constraints);
		
		
		
	}

	private void addActionListeners() {
		startGameMenuItem.addActionListener(this);
		exitMenuItem.addActionListener(this);
		easyMenuItem.addActionListener(this);
		mediumMenuItem.addActionListener(this);
		hardMenuItem.addActionListener(this);
		aboutMenuItem.addActionListener(this);
		helpMenuItem.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == startGameMenuItem) {
			if (game.isRunning()) {
				if (JOptionPane
						.showConfirmDialog(
								this,
								"Current game is still running.Are you sure you want to start a new one?",
								"Warning", JOptionPane.YES_NO_OPTION) == 0) {
					boardAnimation.clearBoard();
					board = new Board();
					game.exitCurrent();
				} else {
					return;
				}
			}
			bearedOfLabel.setText("Beared of: W: 0, R: 0");
			board.initCheckers();
			boardAnimation.setCurrentBoard(board);
			boardAnimation.repaint();
			boardAnimation.animateBoard();
			game.startGame();
		} else if (e.getSource() == exitMenuItem) {
			if (JOptionPane
					.showConfirmDialog(
							this,
							"Current game is still running.Are you sure you want to exit?",
							"Warning", JOptionPane.YES_NO_OPTION) == 0)
				System.exit(0);
		}
		if (e.getSource() == easyMenuItem) {
			//if(!game.isRunning()){
				Expectiminimax.MAX_DEPTH = 1;
				levelLabel.setText("Level: Easy");
			//}
		} else if (e.getSource() == mediumMenuItem) {
			//if(!game.isRunning()){
				Expectiminimax.MAX_DEPTH = 3;
				levelLabel.setText("Level: Medium");
		//	}
		} else if (e.getSource() == hardMenuItem) {
			//if(!game.isRunning()){
				Expectiminimax.MAX_DEPTH = 4;
				levelLabel.setText("Level: Hard");
			//}
		}

		if (e.getSource() == aboutMenuItem) {
			JOptionPane.showMessageDialog(null, 
					 "SUST_BACKGAMMON v1.0\n\n"
					+ "=================== Developers =====================\n"
					+ "Ahnaf Farhan Rownak (2010331018)\n"
					+ "afrownak@student.sust.edu\n"
					+ "Md. Mahadi Hasan Nahid (2010331044)\n"
					+ "nahid@student.sust.edu");
		}
		if (e.getSource() == helpMenuItem) {
			//JOptionPane.showMessageDialog(null, "Help: \n");
			new HelpDialog();
		}
		// -----------if the roll button is pushed, roll both
		// dice------------------
		if (e.getSource() == rollButton || e.getSource() == passButton) {
			game.setRolled();
		}

		// ---------if the new game button is pushed, check if there is already
		// a game--------
		// --------------running and reset the board--------------------------

		

	}

	// ---------get methods for Main variables-------------------------
	public Game getGame() {
		return game;
	}

	public BoardAnimation getBoardAnimation() {
		return boardAnimation;
	}

	public Player getPlayer1() {
		return game.getPlayer((byte) 0);
	}

	public Player getPlayer2() {
		return game.getPlayer((byte) 1);
	}

	public JButton getRollButton() {
		return rollButton;
	}
	public JButton getPassButton() {
		return passButton;
	}

	public DiceAnimation getDiceAnimation() {
		return this.diceAnimation;
	}

}
