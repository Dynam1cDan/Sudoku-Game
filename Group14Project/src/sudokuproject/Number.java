package sudokuproject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Represents cell of a Sudoku puzzle
 * 
 * @author Tyler Kramlich
 * @author Daniel Hawkins
 */
public class Number extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static int height = 60;
	private static int width = 60;

	private static int numberCount;
	private int numId;

	private ArrayList<String> validNumbers = new ArrayList<>();

	private String data;

	/**
	 * Default constructor
	 * 
	 * @param num
	 *            Data for Number
	 */
	public Number(String num) {
		numberCount++;
		this.numId = numberCount;

		setPreferredSize(new Dimension(height, width));
		setHorizontalAlignment(JTextField.CENTER);
		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		setTransferHandler(null);

		Font font = new Font("TimesRoman", Font.BOLD, 30);
		setFont(font);
		setDocument(new NumberInputLimiter(1));

		data = num;
	}

	/**
	 * Sets data and display text of number
	 */
	@Override
	public void setText(String number) {
		data = number;
		super.setText(data);
	}

	/**
	 * Gets Number data
	 * 
	 * @return data
	 */
	public String getData() {
		return data;
	}

	/**
	 * Sets Number Id
	 * 
	 * @param id
	 */
	public void setId(int id) {
		numId = id;
	}

	/**
	 * Gets Number ID
	 * 
	 * @return Number ID
	 */
	public int getId() {
		return numId;
	}

	/**
	 * Sets Valid Number ArrayList
	 * 
	 * @param validNums
	 *            ArrayList of valid numbers
	 */
	public void setValidNumbers(ArrayList<String> validNums) {
		validNumbers = validNums;
	}

	/**
	 * Gets Valid Number ArrayList
	 * 
	 * @return validNumbers
	 */
	public ArrayList<String> getValidNumbers() {
		return validNumbers;
	}

	/**
	 * Limits input of Number
	 * 
	 * @author Tyler Kramlich
	 * @author Daniel Hawkins
	 */
	private class NumberInputLimiter extends PlainDocument {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private int limit;

		/**
		 * Constructor, sets input
		 * 
		 * @param maximumInput
		 *            max input
		 */
		public NumberInputLimiter(int maximumInput) {
			this.limit = maximumInput;
		}

		/**
		 * Inserts string if not over limit
		 */
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			if (str == null) {
				return;
			}

			if ((getLength() + str.length()) <= limit) {
				super.insertString(offs, str, a);
			}
		}
	}
}