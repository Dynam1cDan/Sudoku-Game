package sudokuproject;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Creates Sudoku GameBoard
 * 
 * @author Daniel Hawkins
 * @author Tyler Kramlich
 */
public class GameBoard extends JFrame {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private static JMenu myClock;

	private int GRID_SIZE = 9;
	private int SUB_GRID_SIZE = 3;

	private static int x = 0;
	private static int y = 0;

	private static int seconds = 0;
	private static int minutes = 0;
	private static int hours = 0;
	private String totalTime;

	private static int horizontal1;
	private static int horizontal2;
	private static int vertical1;
	private static int vertical2;

	private JPanel fileOpener;
	private JPanel fileSaver;

	private ArrayList<SubGrid> subGridList = new ArrayList<SubGrid>();
	private ArrayList<String> permittedNumbers = new ArrayList<>();

	private String[] playableNumberList = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" };

	private int[] oddSubGrids = { 1, 3, 5, 7 };

	private static Timer timer = new Timer();

	private int totalMoves = 0;

	/**
	 * Main method of program
	 * 
	 * @param args
	 *            Main args
	 */
	public static void main(String[] args) {
		GameBoard game = new GameBoard();
		game.setVisible(true);
	}

	/**
	 * Default constructor of GameBoard
	 */
	public GameBoard() {
		setTitle("Sudoku! 1.0");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 600);
		setLocationRelativeTo(null);

		buildFileMenu();
		buildContainer();
		setGrayBackground();
	}

	/**
	 * Creates File Menu
	 */
	private void buildFileMenu() {
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		myClock = new JMenu();
		myClock.setOpaque(true);
		myClock.setEnabled(false);

		GameLogic gl = new GameLogic();

		JMenuItem menuItem = new JMenuItem("New");
		menuItem.addActionListener(gl);
		fileMenu.add(menuItem);

		menuItem = new JMenuItem("Open");
		menuItem.addActionListener(gl);
		fileMenu.add(menuItem);

		menuItem = new JMenuItem("Save");
		menuItem.addActionListener(gl);
		fileMenu.add(menuItem);

		menuItem = new JMenuItem("Exit");
		menuItem.addActionListener(gl);
		fileMenu.add(menuItem);

		menuBar.add(fileMenu);
		menuBar.add(myClock);

		setJMenuBar(menuBar);
	}

	/**
	 * Creates GameBoard container where SubGrid and Numbers are placed
	 */
	private void buildContainer() {
		Container board = getContentPane();
		board.setLayout(new GridLayout(SUB_GRID_SIZE, SUB_GRID_SIZE));

		for (int i = 0; i < GRID_SIZE; i++) {
			SubGrid s = new SubGrid();
			subGridList.add(s);
			board.add(s);
		}

		GameLogic gl = new GameLogic();

		for (SubGrid sg : subGridList) {
			for (int i = 0; i < SUB_GRID_SIZE; i++) {
				for (int j = 0; j < SUB_GRID_SIZE; j++) {
					sg.getNumberObject(i, j).addMouseListener(gl);
					sg.getNumberObject(i, j).addKeyListener(gl);
					sg.getNumberObject(i, j).setBackground(Color.white);
				}
			}
		}
	}

	/**
	 * Sets odd numbered grids to gray, allowing for better visibility of board
	 */
	private void setGrayBackground() {
		for (int value : oddSubGrids) {
			for (int i = 0; i < SUB_GRID_SIZE; i++) {
				for (int j = 0; j < SUB_GRID_SIZE; j++) {
					subGridList.get(value).getNumberObject(i, j).setBackground(Color.LIGHT_GRAY);
				}
			}
		}
	}

	/**
	 * Clears GameBoard display
	 */
	private void newPuzzle() {
		// Reset total moves
		totalMoves = 0;

		// Set all Numbers back to enabled
		for (SubGrid s : subGridList) {
			s.clearThisGrid();
			for (int i = 0; i < SUB_GRID_SIZE; i++) {
				for (int j = 0; j < SUB_GRID_SIZE; j++) {
					s.getNumberObject(i, j).setEnabled(true);
				}
			}
		}

		// Reset timer values
		timer.cancel();
		seconds = 0;
		minutes = 0;
		hours = 0;
		myClock.setText("Time: " + seconds + " seconds");
		timer = new Timer();

		setGrayBackground();
	}

	/**
	 * Opens a puzzle text file
	 */
	private void readPuzzle() {
		newPuzzle();
		BufferedReader input = null;
		String line = "";
		String cvsSplitBy = ",";

		JFileChooser fileOpen = new JFileChooser();
		fileOpen.setCurrentDirectory(new File("Resources"));
		int selection = fileOpen.showOpenDialog(fileOpener);

		if (selection == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileOpen.getSelectedFile();
			try {
				input = new BufferedReader(new FileReader(selectedFile));

				totalMoves = Integer.parseInt(input.readLine());

				seconds = Integer.parseInt(input.readLine());
				minutes = Integer.parseInt(input.readLine());
				hours = Integer.parseInt(input.readLine());

				for (SubGrid s : subGridList) {
					x = 0;
					y = 0;
					if ((line = input.readLine()) != null) {

						String[] temp = line.split(cvsSplitBy);

						for (String t : temp) {
							if (x == 0 && y == 0) {
								s.setGridNumbersData(t, x, y);
								y++;
							} else if (x == 0 && y == 1) {
								s.setGridNumbersData(t, x, y);
								y++;
							} else if (x == 0 && y == 2) {
								s.setGridNumbersData(t, x, y);
								y = 0;
								x++;
							} else if (x == 1 && y == 0) {
								s.setGridNumbersData(t, x, y);
								y++;
							} else if (x == 1 && y == 1) {
								s.setGridNumbersData(t, x, y);
								y++;
							} else if (x == 1 && y == 2) {
								s.setGridNumbersData(t, x, y);
								y = 0;
								x++;
							} else if (x == 2 && y == 0) {
								s.setGridNumbersData(t, x, y);
								y++;
							} else if (x == 2 && y == 1) {
								s.setGridNumbersData(t, x, y);
								y++;
							} else {
								s.setGridNumbersData(t, x, y);
							}
						}
					}

				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
						setTime();
						TimerTask task = new TimerTask() {
							public void run() {
								seconds++;
								setTime();
							}
						};
						timer.scheduleAtFixedRate(task, 1000, 1000);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Saves puzzle state to text file
	 */
	private void savePuzzle() {
		BufferedReader input = null;

		timer.cancel();

		JFileChooser fileSave = new JFileChooser();
		fileSave.setCurrentDirectory(new File("Resources"));
		int selection = fileSave.showSaveDialog(fileSaver);

		timer = new Timer();
		setTime();
		TimerTask task = new TimerTask() {
			public void run() {
				seconds++;
				setTime();
			}
		};
		timer.scheduleAtFixedRate(task, 1000, 1000);

		if (selection == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileSave.getSelectedFile();
			try {
				FileWriter fw = new FileWriter(selectedFile, false);
				fw.write(Integer.toString(totalMoves));
				fw.write("\n");
				fw.write(Integer.toString(seconds));
				fw.write("\n");
				fw.write(Integer.toString(minutes));
				fw.write("\n");
				fw.write(Integer.toString(hours));
				fw.write("\n");
				for (SubGrid s : subGridList)
					fw.write(s.toString());
				fw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Checks each row and column of each SubGrid for and displays valid numbers
	 * 
	 * @param gridNumber
	 *            SubGrid referenced
	 * @param row
	 *            Row
	 * @param col
	 *            Column
	 */
	private void checkNums(int gridNumber, int row, int col) {
		ArrayList<String> invalidNums = new ArrayList<String>();
		ArrayList<String> validNums = new ArrayList<String>();
		permittedNumbers = new ArrayList<String>();

		for (String num : playableNumberList) {
			validNums.add(num);
			permittedNumbers.add(num);
		}

		// Check horizontal numbers
		if (gridNumber == 0 || gridNumber == 3 || gridNumber == 6) {
			horizontal1 = gridNumber + 1;
			horizontal2 = gridNumber + 2;
		}

		if (gridNumber == 1 || gridNumber == 4 || gridNumber == 7) {
			horizontal1 = gridNumber - 1;
			horizontal2 = gridNumber + 1;
		}

		if (gridNumber == 2 || gridNumber == 5 || gridNumber == 8) {
			horizontal1 = gridNumber - 1;
			horizontal2 = gridNumber - 2;
		}

		// Check vertical numbers
		if (gridNumber == 0 || gridNumber == 1 || gridNumber == 2) {
			vertical1 = gridNumber + 3;
			vertical2 = gridNumber + 6;
		}

		if (gridNumber == 3 || gridNumber == 4 || gridNumber == 5) {
			vertical1 = gridNumber - 3;
			vertical2 = gridNumber + 3;
		}

		if (gridNumber == 6 || gridNumber == 7 || gridNumber == 8) {
			vertical1 = gridNumber - 6;
			vertical2 = gridNumber - 3;
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				String s = subGridList.get(gridNumber).getNumberData(row, j);
				String s2 = subGridList.get(horizontal1).getNumberData(row, j);
				String s3 = subGridList.get(horizontal2).getNumberData(row, j);

				String s4 = subGridList.get(gridNumber).getNumberData(i, col);
				String s5 = subGridList.get(vertical1).getNumberData(i, col);
				String s6 = subGridList.get(vertical2).getNumberData(i, col);

				String s7 = subGridList.get(gridNumber).getNumberData(i, j);
				String s8 = subGridList.get(gridNumber).getNumberData(i, j);
				String s9 = subGridList.get(gridNumber).getNumberData(i, j);

				for (String num : playableNumberList) {
					if (s.equals(num)) {
						invalidNums.add(num);
					}
					if (s2.equals(num)) {
						invalidNums.add(num);
					}
					if (s3.equals(num)) {
						invalidNums.add(num);
					}
					if (s4.equals(num)) {
						invalidNums.add(num);
					}
					if (s5.equals(num)) {
						invalidNums.add(num);
					}
					if (s6.equals(num)) {
						invalidNums.add(num);
					}
					if (s7.equals(num)) {
						invalidNums.add(num);
					}
					if (s8.equals(num)) {
						invalidNums.add(num);
					}
					if (s9.equals(num)) {
						invalidNums.add(num);
					}
				}
			}
		}

		for (String s : invalidNums) {
			validNums.remove(s);
			permittedNumbers.remove(s);
		}

		for (int i = 0; i < SUB_GRID_SIZE; i++) {
			for (int j = 0; j < SUB_GRID_SIZE; j++) {
				Number n = subGridList.get(gridNumber).getNumberObject(i, j);
				n.setValidNumbers(permittedNumbers);
				n.setToolTipText("Valid numbers: " + validNums.toString());
			}
		}
	}

	/**
	 * Checks Number data for validity
	 * 
	 * @param gridNumber
	 *            SubGrid referenced
	 * @param cellId
	 *            Number referenced
	 */
	private void checkNumberValidity(int gridNumber, int cellId) {

		if (gridNumber == 0 && cellId <= 3) {
			checkNums(gridNumber, 0, 0);
		}

		if (gridNumber == 0 && cellId >= 4 && cellId <= 6) {
			checkNums(gridNumber, 1, cellId % 4);
		}

		if (gridNumber == 0 && cellId >= 7 && cellId <= 9) {
			checkNums(gridNumber, 2, cellId % 7);
		}

		if (gridNumber > 0 && cellId % 9 == 1 || cellId % 9 == 2 || cellId % 9 == 3) {
			checkNums(gridNumber, 0, cellId % 9 - 1);
		}

		if (gridNumber > 0 && cellId % 9 == 4 || cellId % 9 == 5 || cellId % 9 == 6) {
			checkNums(gridNumber, 1, cellId % 9 % 4);
		}

		if (gridNumber > 0 && cellId % 9 == 7 || cellId % 9 == 8) {
			checkNums(gridNumber, 2, cellId % 9 % 7);
		}

		if (cellId % 9 == 0) {
			checkNums(gridNumber, 2, 2);
		}
	}

	/**
	 * Updates state of GameBoard
	 * 
	 * @param numberObject
	 *            Number causing change
	 */
	private void updateGameBoard(Number numberObject) {
		int id = numberObject.getId();
		for (int i = 0; i < GRID_SIZE; i++) {
			checkNumberValidity(i, id);
		}
	}

	/**
	 * Checks if win condition has been met
	 * 
	 * @return boolean
	 */
	private boolean checkWinCondition() {
		for (SubGrid sg : subGridList) {
			for (int i = 0; i < SUB_GRID_SIZE; i++) {
				for (int j = 0; j < SUB_GRID_SIZE; j++) {
					if (sg.getNumberObject(i, j).getData().equals(""))
						return false;
				}
			}
		}
		return true;
	}

	/**
	 * Displays winning GameBoard
	 */
	private void displayWin() {
		for (SubGrid sg : subGridList) {
			for (int i = 0; i < SUB_GRID_SIZE; i++) {
				for (int j = 0; j < SUB_GRID_SIZE; j++) {
					sg.getNumberObject(i, j).setBackground(Color.green);
					sg.getNumberObject(i, j).setEnabled(false);
					sg.getNumberObject(i, j).setDisabledTextColor(Color.BLACK);
				}
			}
		}
		timer.cancel();
		if (hours > 0) {
			totalTime = hours + " hours, " + minutes + " minutes, " + seconds + " seconds.";
		}
		if (minutes > 0 && hours == 0) {
			totalTime = minutes + " minutes, " + seconds + " seconds.";
		} else {
			totalTime = seconds + " seconds.";
		}
		JOptionPane.showMessageDialog(this,
				"You win!\n" + "Total time: " + totalTime + "\nTotal moves: " + Integer.toString(totalMoves),
				"Congratulations!", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Enables timer
	 */
	private void setTime() {
		if (seconds <= 59 && minutes == 0 && hours == 0) {
			myClock.setText("Time: " + seconds + " seconds");
		}

		if (seconds == 60) {
			minutes++;
			seconds = 0;
			myClock.setText("Time: " + minutes + " minutes and " + seconds + " seconds");
		}

		if (seconds <= 59 && minutes > 0 && hours == 0) {
			myClock.setText("Time: " + minutes + " minutes and " + seconds + " seconds");
		}

		if (minutes == 60) {
			hours++;
			minutes = 0;
			seconds = 0;
			myClock.setText("Time: " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds");
		}

		if (seconds <= 59 && hours > 0) {
			myClock.setText("Time: " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds");
		}

	}

	/**
	 * Handles Sudoku game logic
	 * 
	 * @author Tyler Kramlich
	 * @author Daniel Hawkins
	 */
	private class GameLogic implements ActionListener, MouseListener, KeyListener {
		/**
		 * Handles menu option clicks
		 */
		public void actionPerformed(ActionEvent arg0) {

			if (arg0.getActionCommand().equals("New")) {
				newPuzzle();
			}

			if (arg0.getActionCommand().equals("Open")) {
				readPuzzle();
			}

			if (arg0.getActionCommand().equals("Save")) {
				savePuzzle();
			}

			if (arg0.getActionCommand().equals("Exit")) {
				System.exit(0);
			}
		}

		/**
		 * Displays numbers that are valid to input
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			Number numberSource = (Number) e.getSource();
			updateGameBoard(numberSource);
		}

		/**
		 * Determines which numbers can be entered
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			permittedNumbers = new ArrayList<>();
			Number numberSource = (Number) e.getSource();
			updateGameBoard(numberSource);
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
		}

		/**
		 * Handles user input of numbers, including backspace
		 */
		@Override
		public void keyTyped(KeyEvent e) {
			Number numberSource = (Number) e.getSource();
			char keyPressed = e.getKeyChar();
			String inputValue = String.valueOf(keyPressed);

			updateGameBoard(numberSource);
			permittedNumbers = numberSource.getValidNumbers();

			// Backspace
			if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
				numberSource.setText("");
				totalMoves++;
			}

			// If there are no valid numbers, consume
			if (permittedNumbers.isEmpty() == true) {
				e.consume();
			}

			// Input if within validNumbers, else consume
			for (String validNumber : permittedNumbers) {
				if (inputValue.equals(validNumber)) {
					numberSource.setText(inputValue);
					totalMoves++;
				} else {
					e.consume();
				}
			}

			if (checkWinCondition() == true)
				displayWin();
		}
	}
}