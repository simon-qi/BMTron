import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.net.*;

// this class is the start up menu, allowing for customizations
public class Main implements MouseListener
{
	Drawing draw = new Drawing();
	ImageIcon icon = new ImageIcon("files/main.png");
	static JFrame frame = new JFrame("Serpentine");; 
	AudioClip music;
	boolean help;
	ImageIcon screen = new ImageIcon("files/help1.jpg");
	ImageIcon screen2 = new ImageIcon("files/help2.jpg");
	ImageIcon screen3 = new ImageIcon("files/help3.jpg");
	ImageIcon icon2 = new ImageIcon("files/minigame.png");
	static int screennum;
	boolean minigame;
	Minigames m;

	// initializes the program
	public Main()
	{ 
		frame.setSize(751 + 5, 606 + 25);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(draw);
		frame.setVisible(true);
		frame.addMouseListener(this);
		frame.setResizable(false);
		help = false;		
		// sound
		try
		{ 
			music = Applet.newAudioClip(new URL("file:files/Mt Eden DnB (HD) - When will the storm begin.wav")); 
		} 
		catch (MalformedURLException mfe)
		{ 
		}
	}

	// main method runs the program
	public static void main(String[] args)
	{
		new Main();
	}

	// draw the appropriate images depending on what the user clicks
	class Drawing extends JComponent
	{
		public void paint(Graphics g)
		{
			if (!help)
				g.drawImage(icon.getImage(), 0, 0, this);

			if (help)
			{			
				if (screennum == 1)
					g.drawImage(screen.getImage(), 0, 0, 751, 601, this); 
				else if (screennum == 2)
					g.drawImage(screen2.getImage(), 0, 0, 751, 601, this); 
				else 
					g.drawImage(screen3.getImage(), 0, 0, 751, 601, this); 				
			}			
			if (minigame)
				g.drawImage(icon2.getImage(), 0, 0, this);
			repaint();
		}
	}

	// appropriate actions when clicking a mouse coordinate	
	public void mouseClicked (MouseEvent e)
	{		
		int x = e.getX();
		int y = e.getY();
		// if you're on the minigame page
		if (minigame)
		{
			if (x >= 32 && x <= 267)
			{
				if (y >= 321 && y <= 359)
				{
					Object[] options = {"1","2","3","4","Godly"};
					int n = JOptionPane.showOptionDialog(frame,"What level of difficulty do you want?", "Maze",	
							JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[3]);				
					if (n == 0 || n == 1 || n == 2 || n == 3 || n == 4)
					{
						if (n == 0)
							Minigames.difficulty = 1;
						else if (n == 1)
							Minigames.difficulty = 2;
						else if (n == 2)
							Minigames.difficulty = 3;
						else if (n == 3)
							Minigames.difficulty = 4;
						else if (n == 4)
							Minigames.difficulty = 5;					

						Minigames.snake = false;
						frame.setVisible(false);
						new Minigames();
					}
				}
				else if (y >= 411 && y <= 449)
				{
					Minigames.snake = true;
					frame.setVisible(false);
					new Minigames();
				}
				else if (y >= 491 && y <= 531)
				{
					minigame = false;
					frame.setSize(751 + 5, 606 + 25);
				}
			}
		}	  	   
		// on the main page when no button is pressed
		else if (!help)
		{
			if (x >= 32 && x <= 267)
			{
				if (y >= 291 && y <= 333)
				{
					frame.setVisible(false);
					new Selection();
				}
				else if (y >= 356 && y <= 394)
				{
					minigame = true;
					frame.setSize(769 + 5, 619 + 25);
				}
				else if (y >= 420 && y <= 460)
				{
					Object[] options = {"Yes", "No"};
					int n = JOptionPane.showOptionDialog(frame,"Do you want music?", "Settings", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE,null, options, options[1]);
					if (n == 0)
					{
						music.loop();
					}
					else if (n == 1)
					{
						music.stop();
					}
				}
				else if (y >= 486 && y <= 526)
				{
					help = true;
					screennum = 1;
				}
			}
		}

		// the help page
		else if (help)
		{
			if (x >= 555 && x <= 729 && y >= 545 && y <= 610 && screennum == 3)
			{
				help = false;
			}		
			if (x >= 583 && x <= 729 && y >= 553 && y <= 610)
			{
				if (screennum == 2)
				{
					screennum = 3;
				}			
				if (screennum == 1)
				{
					screennum = 2;
				}
			}	
		}
	}	
	public void mousePressed(MouseEvent e)
	{
	}

	public void mouseReleased (MouseEvent e)
	{
	}

	public void mouseEntered (MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}
} 