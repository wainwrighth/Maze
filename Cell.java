import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;

class Cell extends JPanel
{
	// Initialize variables
	private int x, y;
	public int borders[] = {1, 1, 1, 1};

	// Default constructor
    public Cell() 
    { 
    	super();
    	setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
    }

    // Draws the cells with their respective borders even when they change during generation
	public void draw()
	{
		setBorder(BorderFactory.createMatteBorder(borders[0], borders[1], borders[2], borders[3], Color.BLACK));
	}
	
	// Sets the x and y positions for cells for future use
	public int posX() { return x; }
    public int posY() { return y; }

    // Gets the x and y positions of the desired cell
	public void setPos(int px, int py)
    { 
    	x = px;
    	y = py;
    }

    // Sets color for cells that are part of final path
    public void setPath()
    {
    	setBackground(Color.PINK);
    }

    // Sets color of cells that are part of backtracking
    public void setBacktrack()
    {
    	setBackground(Color.GRAY);
    }

    // Sets the color of the final goal cell
    public void setGoal()
    {
    	setBackground(Color.RED);
    }

	// Paint the cell when it is initialized
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
	}
	
}