import java.awt.event.*;
import java.awt.FlowLayout;
import javax.swing.*;
import java.util.Random;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedList;

public class Board
{
	// Initialize all variables
	protected Cell cells[][];
	private boolean visits[][];
	private ArrayList<Cell> neighbors;
	private int rows, columns;
	private Timer timer;
	private ActionListener timeListen;

	private Cell newPos;
   	protected Cell pos;
    private LinkedList<Cell> stack;
    protected boolean generated;
   	private Random rand;
    private int num;
    protected int percent;

    private Cell currentCell;

	// Constructor for Board
	public Board(int column, int row)
	{
		percent = 0;
		rows = row;
		columns = column;
		cells = new Cell[row][column];
		visits = new boolean[row][column];

		// Loop through and insert cells
	 	for(int i = 0; i < row; i += 1)
	 	{
	 		for(int j = 0; j < column; j += 1)
	 		{
	 			// Add cell value to array and set visit values for future use
	 			Cell c;
	 			c = new Cell();
	 			c.setPos(i, j);
	 			cells[i][j] = c;
	 			visits[i][j] = false;
	 		}
	 	}
	}

	// Properly fill board
	public void fillBoardView(JPanel view)
    {
    	for(int i = 0; i < cells.length; i += 1)
		{
			for(int j = 0; j < cells[i].length; j += 1)
			{
				view.add(cells[i][j]);
			}
		}
    }

    public void solveMaze()
    {
    	// Set initial values
    	setVisits();
    	pos = cells[0][0];
    	stack = new LinkedList();
    	rand = new Random();

    	// Run loop until the position value is the end of the maze
    	while(pos != cells[rows - 1][columns - 1])
    	{
    		// Put value on stack, set its visited value to true, and get neighbors
    		stack.push(pos);
    		visits[pos.posX()][pos.posY()] = true;
    		getPath(pos);

    		// If no neighbors are found, check other stack cell values
    		if(neighbors.isEmpty())
    		{
    			// Run through stack
    			while(stack.isEmpty() == false)
    			{
    				// Get cell from stack and get its neighbors
    				Cell tmp = stack.pop();
    				tmp.setBacktrack();
    				getPath(tmp);
    				
    				// If there are neighobrs, set it as new position value
    				if(neighbors.isEmpty() == false)
    				{
    					pos = tmp;
    					break;
    				}
    				if(tmp == null)
    				{
    					break;
    				}
    			}
    		}

    		// If no neighbors, the maze is generated so quit
    		if(neighbors.size() == 0)
    		{
    			break;
    		}

    		// pick the first value on the neighbors
    		newPos = neighbors.get(0);

    		pos.setPath();
    		newPos.setPath();
    		percent += 1;

    		// Change neighbor to position value
    		pos = newPos;
    	}

    	// Show percentage filled in at the end
    	pos.setGoal();
    	Maze.percentLabel.setText("Percent Solved: " + ((percent * 100) / (rows * columns)) + "%");
		Maze.percentLabel.repaint();
    }

    // Same regular solve but paced on a timer to give incremental solution
    public void showSolve(int speed)
    {
    	setVisits();
    	pos = cells[0][0];
    	stack = new LinkedList();
    	rand = new Random();

    	timeListen = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	if(pos != cells[rows - 1][columns - 1])
		    	{
		    		stack.push(pos);
		    		visits[pos.posX()][pos.posY()] = true;
		    		getPath(pos);

		    		if(neighbors.isEmpty())
		    		{
		    			while(stack.isEmpty() == false)
		    			{
		    				Cell tmp = stack.pop();
		    				tmp.setBacktrack();
		    				getPath(tmp);
		    				if(neighbors.isEmpty() == false)
		    				{
		    					pos = tmp;
		    					break;
		    				}
		    				if(tmp == null)
		    				{
		    					break;
		    				}
		    			}
		    		}

		    		if(neighbors.size() == 0)
		    		{
		    			timer.stop();
		    		}

		    		newPos = neighbors.get(0);

		    		pos.setPath();
		    		newPos.setPath();
		    		percent += 1;
		    		Maze.percentLabel.setText("Percent Solved: " + ((percent * 100) / (rows * columns)) + "%");
		    		Maze.percentLabel.repaint();

		    		pos = newPos;
		        }
		        else
		    	{
		    		pos.setGoal();
		    		timer.stop();
		    	}
		    }
        };

        // Based on speed, run the timer at a certain rate
        if(speed == 0)
        {
        	timer = new Timer(150, timeListen);
        	timer.start();
        }
        if(speed == 50)
        {
        	timer = new Timer(90, timeListen);
        	timer.start();
        }
        if(speed == 100)
        {
        	timer = new Timer(40, timeListen);
        	timer.start();
        }
    }

    public void getPath(Cell c)
    {
    	neighbors = new ArrayList();
    	int x = c.posX();
    	int y = c.posY();

    	// Add neighbors to array based on missing walls and add right and down first
        if(c.borders[3] == 0 && visits[x][y + 1] == false) // right
        {
            neighbors.add(cells[x][y + 1]);
        }
        if(c.borders[2] == 0 && visits[x + 1][y] == false) // down
        {
            neighbors.add(cells[x + 1][y]);
        }
        if(c.borders[1] == 0 && visits[x][y - 1] == false) // left
        {
            neighbors.add(cells[x][y - 1]);
        }
        if(c.borders[0] == 0 && visits[x - 1][y] == false) // up
        {
            neighbors.add(cells[x - 1][y]);
        }
    }

    // Reset all visit values to false for solving
    public void setVisits()
    {
    	for(int i = 0; i < rows; i++)
    	{
    		for(int j = 0; j < columns; j++)
    		{
    			visits[i][j] = false;
    		}
    	}
    }

    public void generateMaze()
    {
    	// Set initial values
    	pos = cells[0][0];
    	stack = new LinkedList();
    	rand = new Random();
    	generated = false;

    	while(!generated)
    	{
    		// Put value on stack, set its visited value to true, and get neighbors
    		stack.push(pos);
    		visits[pos.posX()][pos.posY()] = true;
    		getAdjacent(pos);

    		// If no neighbors go through stack
    		if(neighbors.isEmpty())
    		{
    			// If the stack isnt empty go through it
    			while(stack.isEmpty() == false)
    			{
    				// Get adjacent values of the cell
    				Cell tmp = stack.pop();
    				getAdjacent(tmp);

    				if(neighbors.isEmpty() == false)
    				{
    					pos = tmp;
    					break;
    				}
    				if(tmp == null)
    				{
    					generated = true;
    					break;
    				}
    			}
    		}

    		if(neighbors.size() == 0)
    		{
    			break;
    		}

    		// Randomly pick a value to go to next and then break walls
    		num = rand.nextInt(neighbors.size());
    		newPos = neighbors.get(num);

    		breakWall(pos, newPos);
    		pos.draw();
    		newPos.draw();
    		pos = newPos;
    	}
    }

    // Same regular solve but paced on a timer to give incremental generation
    public void showGenerate(int speed)
    {
    	pos = cells[0][0];
    	stack = new LinkedList();
    	rand = new Random();
    	generated = false;

        timeListen = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	if(!generated)
		    	{
		    		stack.push(pos);
		    		visits[pos.posX()][pos.posY()] = true;
		    		getAdjacent(pos);

		    		if(neighbors.isEmpty())
		    		{
		    			while(stack.isEmpty() == false)
		    			{
		    				Cell tmp = stack.pop();
		    				getAdjacent(tmp);
		    				if(neighbors.isEmpty() == false)
		    				{
		    					pos = tmp;
		    					break;
		    				}
		    				if(tmp == null)
		    				{
		    					generated = true;
		    					break;
		    				}
		    			}
		    		}

		    		// If there are no more neighbors, return to stop generation
		    		if(neighbors.size() == 0)
		    		{
		    			timer.stop();
		    		}

		    		num = rand.nextInt(neighbors.size());
		    		newPos = neighbors.get(num);

		    		breakWall(pos, newPos);
		    		pos.draw();
		    		newPos.draw();
		    		pos = newPos;
		    	}
		    	else
		    	{
		    		timer.stop();
		    	}   
		    }
        };

        if(speed == 0)
        {
        	timer = new Timer(150, timeListen);
        	timer.start();
        }
        if(speed == 50)
        {
        	timer = new Timer(90, timeListen);
        	timer.start();
        }
        if(speed == 100)
        {
        	timer = new Timer(40, timeListen);
        	timer.start();
        }
    }

    public void getAdjacent(Cell c)
    {
    	neighbors = new ArrayList();
    	int x = c.posX();
    	int y = c.posY();

    	// If the values are in bounds, add them
    	if(x > 0 && visits[x - 1][y] == false) // check left
    	{
    		neighbors.add(cells[x - 1][y]);
    	}
    	if(y > 0 && visits[x][y - 1] == false) // check top
    	{
    		neighbors.add(cells[x][y - 1]);
    	}
    	if(x < rows - 1 && visits[x + 1][y] == false) // check right
    	{
    		neighbors.add(cells[x + 1][y]);
    	}
    	if(y < columns - 1 && visits[x][y + 1] == false) // check bottom
    	{
    		neighbors.add(cells[x][y + 1]);
    	}
    }

    // Destroy wall between the two cells
    public void breakWall(Cell c, Cell adj)
    {
    	int xC = c.posX();
    	int yC = c.posY();
    	int xA = adj.posX();
    	int yA = adj.posY();

    	// Potentially break left and right walls
    	if(xC == xA)
    	{
    		if(yC < yA)
    		{
    			c.borders[3] = 0;
    			adj.borders[1] = 0;

    		}
    		if(yC > yA)
    		{
    			c.borders[1] = 0;
    			adj.borders[3] = 0;
    		}
    	}

    	// Potentially break top and bottom walls
    	if(yC == yA)
    	{
    		if(xC < xA)
    		{
    			c.borders[2] = 0;
    			adj.borders[0] = 0;
    		}
    		if(xC > xA)
    		{
    			c.borders[0] = 0;
    			adj.borders[2] = 0;
    		}
    	}
    }
}