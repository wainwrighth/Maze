import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Hashtable;

public class Maze extends JFrame implements ActionListener
{
	private Cell cell;
	private Board gameBoard;

	private JPanel boardView, labelView, percentView;

	private JSlider height, width, speed;
	private JLabel heightLabel, widthLabel, speedLabel;
	static JLabel percentLabel;
	private JButton generate, solve, stop;
	private JCheckBox showGen, showSolve;

	private int rows;
    private int columns;

	private Maze()
	{
		super("Maze");

		boardView = new JPanel();
        labelView = new JPanel();
        percentView = new JPanel();

        height = new JSlider(JSlider.HORIZONTAL, 10, 50, 30);
        width = new JSlider(JSlider.HORIZONTAL, 10, 50, 30);
        speed = new JSlider();

        heightLabel = new JLabel("Height:");
        widthLabel = new JLabel("Width:");
        speedLabel = new JLabel("Speed:");
        percentLabel = new JLabel("Percent Solved: 0%");

        generate = new JButton("Generate");
        generate.addActionListener(this);

        solve = new JButton("Solve");
        solve.addActionListener(this);
        solve.setEnabled(false);

        stop = new JButton("Stop");
        stop.addActionListener(this);
        stop.setEnabled(false);

        showGen = new JCheckBox("Show Maze Generation");
        showSolve = new JCheckBox("Show Maze Solving");

        // Create hashtable to hold strings and respective values
        Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();

        table.put(0, new JLabel("Slow"));
        table.put(50, new JLabel("Medium"));
        table.put(100, new JLabel("Fast"));

        speed.setLabelTable(table);
        speed.setMajorTickSpacing(50);
        speed.setPaintTicks(true);
        speed.setPaintLabels(true);
        speed.setSnapToTicks(true);

        height.setMajorTickSpacing(10);
		height.setMinorTickSpacing(5);
		height.setPaintTicks(true);
		height.setPaintLabels(true);
		height.setSnapToTicks(true);

		width.setMajorTickSpacing(10);
		width.setMinorTickSpacing(5);
		width.setPaintTicks(true);
		width.setPaintLabels(true);
		width.setSnapToTicks(true);

		// Add all sliders and buttons to side
		labelView.add(showGen);
		labelView.add(showSolve);
		labelView.add(speedLabel);
		labelView.add(speed);
        labelView.add(heightLabel);
        labelView.add(height);
        labelView.add(widthLabel);
        labelView.add(width);
        labelView.add(generate);
        labelView.add(solve);
        labelView.add(stop);

        percentView.add(percentLabel);
        percentView.setLayout(new FlowLayout(FlowLayout.LEFT));

        boardView.setBackground(Color.BLACK);
        labelView.setLayout(new GridLayout(11, 1, 1, 1));

		Container c = getContentPane();

		c.add(labelView, BorderLayout.EAST);
        c.add(boardView, BorderLayout.CENTER);
        c.add(percentView, BorderLayout.SOUTH);

		setSize(700, 500);
        setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
    {
    	// If the stop button is clicked stop maze
    	if(e.getActionCommand().equals("Stop"))
    	{
    		// Either stop generate or solver based on action
    		stop.setEnabled(false);
    		gameBoard.generated = true;
    		gameBoard.pos = gameBoard.cells[rows - 1][columns - 1];
    	}

    	// Start generation of the maze if clicked
    	if(e.getActionCommand().equals("Generate"))
    	{
    		percentLabel.setText("Percent Solved: 0%");

    		rows = height.getValue();
	    	columns = width.getValue();

	    	// Reset the board
	    	boardView.removeAll();
	    	gameBoard = new Board(columns, rows);

	    	// Incrementally show maze generation based on checkbox
    		if(!showGen.isSelected())
    		{
    			gameBoard.generateMaze();
    		}
    		else
    		{
    			stop.setEnabled(true);
    			gameBoard.showGenerate(speed.getValue());
    		}

	        gameBoard.fillBoardView(boardView);
	        boardView.setLayout(new GridLayout(rows, columns));

	        boardView.repaint();
	        setVisible(true);
	        solve.setEnabled(true);
    	}
    	if(e.getActionCommand().equals("Solve"))
    	{
    		// Clear visits to solve properly
    		gameBoard.setVisits();

    		// Incrementally show maze solver based on checkbox
    		if(showSolve.isSelected())
    		{
    			stop.setEnabled(true);
	    		gameBoard.showSolve(speed.getValue());
    		}
    		else
    		{
    			gameBoard.solveMaze();
    		}

    		// Repaint board and show the solution
    		boardView.repaint();
		    setVisible(true);
		    solve.setEnabled(false);
    	}
    }

	public static void main(String args[])
    {
        Maze M = new Maze();
        M.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
    }
}