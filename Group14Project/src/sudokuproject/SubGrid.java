package sudokuproject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Represents 3x3 Sudoku grid
 * 
 * @author Daniel Hawkins
 * @author Tyler Kramlich
 * 
 * @version 1.0
 */
public class SubGrid extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int SUB_GRID_SIZE = 3;

	private int height = 50;
	private int width = 50;

	private Number[][] numbersInSubGrid = new Number[SUB_GRID_SIZE][SUB_GRID_SIZE];

	/**
	 * Default SubGrid constructor
	 */
	public SubGrid() {

		setLayout(new GridLayout(SUB_GRID_SIZE, SUB_GRID_SIZE));
		setBorder(BorderFactory.createLineBorder(Color.black, 2));
		setPreferredSize(new Dimension(height, width));

		addNumbersToSubGrid();

		setVisible(true);
		setOpaque(true);
	}

	/**
	 * Adds Number objects to the SubGrid
	 */
	private void addNumbersToSubGrid() {
		for (int i = 0; i < SUB_GRID_SIZE; i++) {
			for (int j = 0; j < SUB_GRID_SIZE; j++) {
				Number newNum = new Number("");
				numbersInSubGrid[i][j] = newNum;
				add(numbersInSubGrid[i][j]);
			}
		}
	}

	/**
	 * Sets Data of each SubGrid Number
	 * 
	 * @param data
	 *            Sudoku number
	 * @param x
	 *            Row
	 * @param y
	 *            Column
	 */
	public void setGridNumbersData(String data, int x, int y) {
		String str;
		if (data.equals("+")) {
			numbersInSubGrid[x][y].setText("");
		} else {
			numbersInSubGrid[x][y].setText(data);
		}
		if (data.length() > 1) {
			str = data.replace("x", "");
			numbersInSubGrid[x][y].setText(str);
			numbersInSubGrid[x][y].setBackground(Color.YELLOW);
			numbersInSubGrid[x][y].setEnabled(false);
			numbersInSubGrid[x][y].setDisabledTextColor(Color.BLACK);
		}
	}

	/**
	 * Resets Numbers to default state
	 */
	public void clearThisGrid() {
		for (Number[] grid : numbersInSubGrid)
			for (Number n : grid) {
				n.setText("");
				n.setEnabled(true);
				n.setBackground(Color.white);
			}
	}

	/**
	 * Gets Number Object **ID**
	 * 
	 * @param x
	 *            Row
	 * @param y
	 *            Column
	 * @return number id
	 */
	public int getGridCell(int x, int y) {
		return numbersInSubGrid[x][y].getId();
	}

	/**
	 * Gets Number Object
	 * 
	 * @param x
	 *            Row
	 * @param y
	 *            Column
	 * @return number
	 */
	public Number getNumberObject(int x, int y) {
		return numbersInSubGrid[x][y];
	}

	/**
	 * Gets Number data
	 * 
	 * @param x
	 *            Row
	 * @param y
	 *            Column
	 * @return number data
	 */
	public String getNumberData(int x, int y) {
		return numbersInSubGrid[x][y].getData();
	}

	/**
	 * Outputs SubGrid as a string
	 */
	public String toString() {
		String str = "";
		for (int i = 0; i < SUB_GRID_SIZE; i++) {
			for (int j = 0; j < SUB_GRID_SIZE; j++) {
				if (numbersInSubGrid[i][j].getData().equals(""))
					str += "+";
				if (numbersInSubGrid[i][j].isEnabled() == false) {
					str += numbersInSubGrid[i][j].getData() + "x" + ",";
				} else {
					str += numbersInSubGrid[i][j].getData() + ",";
				}
			}
		}
		str += "\n";
		return str;
	}
}