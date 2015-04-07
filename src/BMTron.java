import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// this class plays the main game (arena)
public class BMTron implements KeyListener
{
	Drawing draw;
	static Font font = new Font("SansSerif", Font.BOLD, 20);
	static Font font3 = new Font("SansSerif", Font.BOLD, 16);
	static Font font2 = new Font("SansSerif", Font.BOLD, 40);
	static String st = "First team to 15 points wins!";
	static String si = "First player to 10 points wins!";
	ImageIcon screen = new ImageIcon("files/gameinterface2.png");
	ImageIcon congratz = new ImageIcon("files/congratzempty.jpg");
	static int delayed;
	static int scoreteam1;
	static int scoreteam2;
	final static int delayTime = 50;
	static boolean starting;
	static final double AIaccuracy = 0.1;  // threadhold for randomness of AI
	static int playerNum;
	final static int SPACE = 14; // length of a box
	final static int WIDTH = 52; // width of game
	final static int HEIGHT = 39; // height of game
	static JFrame frame;
	static Player[] players; // the players
	Move[] move;
	Repaint repaint;

	// sets the variables to the default values that it would be initialized to, used when entering the arena game each time
	// values do not change throughout the games 
	public static void initial()
	{
		scoreteam1 = 0; scoreteam2 = 0;
		delayed = 0;
		players = new Player[5];
		for (int i = 0; i < 5; i++) 
		{
			players[i] = new Player();
		}

		// finding how many players there are (2, 3, or 4) and initializing status
		playerNum = 0;
		for (int i = 0; i < 5; i++)
		{
			if (Selection.exist[i])
			{
				playerNum++;
			}
			players[i].exist = Selection.exist[i];  
			players[i].ai = Selection.ai[i];
			players[i].name = Selection.name[i];		 
		}


		players[3].keyDown = KeyEvent.VK_SEMICOLON;
		players[3].keyLeft = KeyEvent.VK_L;
		players[3].keyRight = KeyEvent.VK_QUOTE;
		players[3].keyUp = KeyEvent.VK_P;
		players[2].keyDown = KeyEvent.VK_H;
		players[2].keyLeft = KeyEvent.VK_G;
		players[2].keyRight = KeyEvent.VK_J;
		players[2].keyUp = KeyEvent.VK_Y;
		players[1].keyDown = KeyEvent.VK_S;
		players[1].keyLeft = KeyEvent.VK_A;
		players[1].keyRight = KeyEvent.VK_D;
		players[1].keyUp = KeyEvent.VK_W;
		players[4].keyDown = KeyEvent.VK_DOWN;
		players[4].keyLeft = KeyEvent.VK_LEFT;
		players[4].keyRight = KeyEvent.VK_RIGHT;
		players[4].keyUp = KeyEvent.VK_UP;
		players[1].color = Color.red;
		players[3].color = Color.green;

		if (Selection.team)
		{
			players[2].color = Color.red;
			players[4].color = Color.green;
		} 
		else
		{
			players[2].color = Color.blue;
			players[4].color = Color.yellow;
		}

	}

	// initializes values for 1 round of gameplay (until someone dies)
	public static void initialize()
	{

		int[] actualPlayers = new int[4]; // indices of the existing players
		int index = 0;
		for (int i = 0; i < 5; i++)
		{
			if (players[i].exist)
				actualPlayers[index++] = i;
		}

		if (playerNum == 4)
		{
			players[2].x = 16;
			players[2].y = 30;
			players[1].x = 16;
			players[1].y = 10;
			players[3].x = 37;
			players[3].y = 10;
			players[4].x = 37;
			players[4].y = 30;
		}
		else if (playerNum == 2)
		{			
			players[actualPlayers[0]].x = 16;
			players[actualPlayers[0]].y = 20;
			players[actualPlayers[1]].x = 37;
			players[actualPlayers[1]].y = 20;			    
		}
		else if (playerNum == 3)
		{
			players[actualPlayers[0]].x = 16;
			players[actualPlayers[0]].y = 20;
			players[actualPlayers[1]].x = 37;
			players[actualPlayers[1]].y = 10;
			players[actualPlayers[2]].x = 37;
			players[actualPlayers[2]].y = 30;	
		}

		for (int i = 0; i < 5; i++)
		{
			players[i].square = new boolean[HEIGHT + 2][WIDTH + 2];
			if (players[i].exist)
			{
				players[i].alive = true;
				players[i].square[players[i].y][players[i].x] = true;
			}
			players[i].dir = 'a';  // 'a' means no direction yet
		}

		starting = false;

	}

	// constructor for the game
	public BMTron()
	{
		frame = new JFrame("Serpentine");
		frame.setVisible(true);
		frame.setSize(793 + 5, 623 + 25);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.addKeyListener(this);
		draw = new Drawing();
		frame.add(draw);
		initial();
		initialize();
		startMove();
	}	

	// starts the threads
	public void startMove()
	{
		move = new Move[5];
		for (int i = 0; i < 5; i++) 
		{ 
			if (players[i].exist) 
			{
				move[i] = new Move(i);
				move[i].start();
			}
		}
		repaint = new Repaint();
		repaint.start();
	}

	// end the threads and close window
	public void endMove()
	{
		BMTron.delayed += 50;
		if (delayed == 2000)
		{
			for (int i = 0; i < 5; i++) 
			{
				if (players[i].exist)
				{
					move[i].interrupt();
				}
			}
			repaint.interrupt();
			BMTron.frame.setVisible(false);
			Selection.frame.setVisible(true);
			delayed = 0;
		}
	}


	// main method runs the program
	public static void main(String []args) 
	{      
		new BMTron();
	}   

	// change directions if you are a human 
	public void keyPressed(KeyEvent evt)
	{
		int key = evt.getKeyCode(); 	
		for (int i = 0; i < 5; i++)
		{
			if (players[i].exist && !players[i].ai)
			{
				if (key == players[i].keyDown)
					players[i].dir = 'd';
				else if (key == players[i].keyLeft)
					players[i].dir = 'l';
				else if (key == players[i].keyRight)	
					players[i].dir = 'r';
				else if (key == players[i].keyUp)
					players[i].dir = 'u';
			}
		}
	}

	public void keyTyped(KeyEvent evt)
	{
	}

	public void keyReleased(KeyEvent evt)
	{
	}

	class Drawing extends JComponent
	{
		// draws the graphics
		public void paint(Graphics g)
		{	  	 
			g.drawImage(screen.getImage(), 0, 0, 793, 623, this);         
			g.setFont(font);
			if (Selection.team)
			{
				g.drawString("Score: " + BMTron.scoreteam1 + ":" + BMTron.scoreteam2,500, 25);	
				g.setColor(Color.white);
				g.setFont(font3);
				g.drawString(BMTron.st, 50, 25);
			}
			else
			{
				String s = "Score: ";
				for (int i = 0; i < 5; i++)
				{
					if (players[i].exist)
						s += players[i].score + ":";
				}

				s = s.substring(0, s.length() - 1);
				g.drawString(s , 500, 25);	
				g.setColor(Color.white);
				g.setFont(font3);
				g.drawString(BMTron.si, 50, 25);
			}

			// drawing all taken coordinates with appropriate pictures
			for (int i = 1; i <= BMTron.HEIGHT; i ++)
			{
				for (int j = 1; j <= BMTron.WIDTH; j ++)
				{
					for (int k = 0; k < 5; k++)
					{
						if (players[k].square[i][j])
						{
							g.setColor(players[k].color);
							g.fillRect((j -1) * BMTron.SPACE + 30, (i -1) * BMTron.SPACE + 60, BMTron.SPACE, BMTron.SPACE);			
						}
					}
				}
			}


			// after one game is over
			if (Selection.team)
			{
				// if Selection.team mode, after one Selection.team totally dies
				if (!((players[1].alive || players[2].alive) && (players[3].alive || players[4].alive)))
				{
					if (players[1].alive || players[2].alive)
					{
						if (players[1].alive && players[2].alive)
							BMTron.scoreteam1 +=2;
						else
							BMTron.scoreteam1 ++;
					}
					else if (players[3].alive || players[4].alive)
					{
						if (players[3].alive && players[4].alive)
							BMTron.scoreteam2 += 2;
						else
							BMTron.scoreteam2 ++;
					}
					BMTron.initialize();
				}	
			} 
			else
			{
				// if only one player is still alive in non Selection.team mode
				int alive = 0;
				for (int i = 0; i < 5; i++)
				{
					if (players[i].alive)
						alive++;
				}

				if (alive == 1 || alive == 0)
				{
					for (int i = 0; i < 5; i++)
					{
						if (players[i].alive)
						{
							players[i].score++;
						}
					}

					BMTron.initialize();
				}
			}		
			// after either Selection.team (reach score of 15) or player (reach score 10) wins
			if (Selection.team)
			{
				if (BMTron.scoreteam1 == 15 || BMTron.scoreteam1 == 16 || BMTron.scoreteam2 == 15 || BMTron.scoreteam2 == 16)
				{
					for (int i = 0; i < 5; i++)
					{
						players[i].alive = false;
					}

					g.drawImage(congratz.getImage(), 0, 0, 793, 623, this);
					g.setColor(Color.red);
					g.setFont(font2);
					g.drawImage(congratz.getImage(), 0, 0, 793, 623, this);
					g.drawString("Congratulations!", 250, 250);
					if (scoreteam1 == 15 || scoreteam1 == 16)
						g.drawString("Team 1 won!", 250, 350);
					else if (scoreteam2 == 15 || scoreteam2 == 16)
						g.drawString("Team 2 won!", 250, 350);

					endMove();
				}		  
			}
			else
			{
				if (players[1].score == 10 || players[2].score == 10 || players[3].score == 10 || players[4].score == 10)
				{
					for (int i = 0; i < 5; i++)
					{
						players[i].alive = false;
					}
					g.setColor(Color.red);
					g.setFont(font2);
					g.drawImage(congratz.getImage(), 0, 0, 793, 623, this);
					g.drawString("Congratulations!", 250, 250);
					for (int i = 0; i < 5; i++)
					{
						if (players[i].score == 10)
							g.drawString(players[i].name + " " + "won!", 250, 350);
					}

					endMove();

				}
			} 
		}
	}

	class Repaint extends Thread
	{
		public void run()
		{
			try
			{
				while (true)
				{
					sleep(BMTron.delayTime);
					draw.repaint();
				}
			}
			catch (InterruptedException e)
			{
			}
		}
	}

	class Move extends Thread
	{
		int playerNum;

		public Move(int pn) 
		{
			playerNum = pn;
		}

		public void run()
		{
			try
			{
				while (true)
				{
					sleep(BMTron.delayTime);
					players[playerNum].move();
				}
			}
			catch (InterruptedException e)
			{
			}
		}
	}
}



