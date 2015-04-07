import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// this class does the minigames (both mazes and survivor)
public class Minigames implements KeyListener
{
	ImageIcon icon = new ImageIcon("files/minigame.png");	
	ImageIcon redh = new ImageIcon("files/RedSnakeBodyHorizontal.jpg");
	ImageIcon redv = new ImageIcon("files/RedSnakeBodyVertical.jpg");
	ImageIcon redtu = new ImageIcon("files/RedSnakeTailUp.jpg");
	ImageIcon redtd = new ImageIcon("files/RedSnakeTailDown.jpg");
	ImageIcon redtl = new ImageIcon("files/RedSnakeTailLeft.jpg");
	ImageIcon redtr = new ImageIcon("files/RedSnakeTailRight.jpg");
	ImageIcon redhu = new ImageIcon("files/RedHeadUp.jpg");
	ImageIcon redhd = new ImageIcon("files/RedHeadDown.jpg");
	ImageIcon redhl = new ImageIcon("files/RedHeadLeft.jpg");
	ImageIcon redhr = new ImageIcon("files/RedHeadRight.jpg");
	ImageIcon rnw = new ImageIcon("files/RedSnakeBodyNW.jpg");
	ImageIcon rse = new ImageIcon("files/RedSnakeBodySE.jpg");
	ImageIcon rsw = new ImageIcon("files/RedSnakeBodySW.jpg");
	ImageIcon rne = new ImageIcon("files/RedSnakeBodyNE.jpg");
	Move move;
	static boolean snake; 
	static boolean starting;
	final static int SPACE = 14; // length of a box
	final static int WIDTH = 52;
	final static int HEIGHT = 40;
	final static int WIDTH_SNAKE = 52;
	final static int HEIGHT_SNAKE = 39;
	static int delayed;
	ImageIcon congratz = new ImageIcon("files/congratzempty.jpg");
	Drawing draw;
	// positions of the player
	static int x1, tempx1, sx1;
	static int y1, tempy1, sy1;
	static char sdir1;
	static JFrame frame;
	// direction as a char
	static char dir1;
	static boolean[][] square1; // if the particular coordinate is taken by the player - for maze game
	static boolean alive1; // if the player hit a previously taken coordinate, then its false
	static int[][] square1c;
	static boolean[][]square1h;
	static char dir1ss[][];
	static int difficulty;
	ImageIcon screen = new ImageIcon("files/gameinterface2.png");
	Font font2 = new Font("SansSerif", Font.BOLD, 40);
	static int delayTime = 50;
	static int x, tempx;
	static int y, tempy;
	static char dir;
	static boolean[][] square; // if the particular coordinate is taken by the player - for survivor (snake) game
	final static int MAX_SCORE = WIDTH_SNAKE * HEIGHT_SNAKE;
	static int[] takenx = new int[MAX_SCORE + 1]; 
	static int[] takeny = new int[MAX_SCORE + 1]; 

	// the total number of moves made
	static int moves;
	static boolean alive = false;
	static int score;
	static int peckx;
	static int pecky;
	Font font = new Font("SansSerif", Font.BOLD, 20);
	Font font3 = new Font("SansSerif", Font.BOLD, 16);	

	// initializing coordinate for the survivor minigame
	public static void initializesnake()
	{
		score = 0;
		x = 26;
		y = 10;
		dir = 'd';
		alive = true;
		square = new boolean[HEIGHT_SNAKE + 2][WIDTH_SNAKE + 2];
		square[y][x] = true;
		moves = 0;
		do
		{
			peckx = (int)(Math.random() * WIDTH_SNAKE) + 1;
			pecky = (int)(Math.random() * HEIGHT_SNAKE) + 1;
		}
		while (peckx == 26 && pecky >= 10 && pecky <= 12);
		delayTime = 50;
		tempx = 0;
		tempy = 0;
		delayed = 0;
	}

	// initializes coordinates for the mazes
	public static void initialize()
	{
		if (difficulty == 1)
		{
			x1 = 24;
			y1 =  1;
		}
		else if (difficulty == 2)
		{
			x1 = 16;
			y1 = 40;
		}
		else if (difficulty == 3)
		{
			x1 = 26;
			y1 =  40;
		}
		else if (difficulty == 4)
		{
			x1 = 9;
			y1 =  40;
		}
		else if (difficulty == 5)
		{
			x1 = 50;
			y1 =  39;
		}
		dir1 = 'a';
		square1 = new boolean[HEIGHT + 2][WIDTH + 2];
		square1[y1][x1] = true;
		alive1 = true;
		delayed = 0;
		square1h = new boolean[HEIGHT + 2][WIDTH + 2];
		square1c = new int[HEIGHT + 2][WIDTH + 2];
		dir1ss = new char[HEIGHT + 2][WIDTH + 2];
		tempx1 = 0;
		tempy1 = 0;
		starting = false;
	}

	public void startMove()
	{
		move = new Move();
		move.start();
	}

	// constructor for the application
	public Minigames()
	{
		frame = new JFrame("Serpentine");
		frame.setSize(793+5,623+25);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.addKeyListener(this);
		frame.setResizable(false);
		draw = new Drawing();
		frame.add(draw);
		frame.setVisible(true);
		startMove();
		if (snake)
			initializesnake();
		else
			initialize();
	}

	// main method runs the program
	public static void main(String[] args)
	{
		new Minigames();
	}

	// moves the snake when playing the maze
	public static void move()
	{	
		if (!Minigames.starting)
		{
			sdir1 = dir1;
			sx1 = x1;
			sy1= y1;
			dir1ss[sy1][sx1] = sdir1;
		}		
		if (dir1 != 'a')
			starting = true;
		if (alive1)
		{
			tempx1 = x1;
			tempy1 = y1;
			// start to move when both dir1 and dir2 are initialized
			if (dir1 != 'a')
			{
				if (dir1 == 'u')
					y1 -= 1;
				else if (dir1 == 'd')
					y1 += 1;
				else if (dir1 == 'l')
					x1 -= 1;
				else if (dir1 == 'r')
					x1 += 1;					
				// if you try to move to previously taken coordinate or outside game, then die
				if (square1[y1][x1] == true)
					alive1 = false;
				if (!(x1 >= 1 && x1 <= WIDTH && y1 >= 1 && y1 <= HEIGHT))
					alive1 = false;				
				if (difficulty == 1)
				{				
					// coordinates of walls 
					if (x1 == 26 && y1 > 0 && y1 < 7) 
						alive1 = false;
					if (y1 == 6 && x1 > 25)
						alive1 = false;
					if (x1 == 37 && y1 > 5 && y1 < 31) 
						alive1 = false;
					if (y1 == 30 && x1 > 25)			      
						alive1 = false;
					if (x1 == 21 && y1 > 25)
						alive1 = false;
					if (y1 == 26 && x1 < 33) 
						alive1 = false;
					if (x1 == 32 && y1 < 27 && y1 > 9) 
						alive1 = false;
					if (x1 == 22 && y1 < 11) 
						alive1 = false;
					if (y1 == 10 && x1 < 33) 
						alive1 = false;
					if (x1 == 26 && y1 > 29) 
						alive1 = false;
				}
				else if (difficulty == 2)
				{				
					if (x1 == 18 && y1 > 6 && y1 < 41)
						alive1 = false;
					if (x1 == 36 && y1 > 6)
						alive1 = false;
					if (x1 == 15)
						alive1 = false;
					if (x1 == 39 && y1 > 3 && y1 < 21)
						alive1 = false;
					if (y1 == 4)
						alive1 = false;
					if (y1 == 7 && x1 > 17 && x1 < 37)
						alive1 = false;
					if (y1 == 22 && x1 > 35)
						alive1 = false;
					if (y1 == 20 && x1 > 38)
						alive1 = false;	
				}					
				else if (difficulty == 3)
				{				
					if (x1 == 23 && y1 >= 32)
						alive1 = false;
					if (x1 == 30 && y1 >= 32)
						alive1 = false;
					if (y1 == 32 && x1 >= 11 && x1 <= 23)
						alive1 = false;
					if (y1 == 32 && x1 >= 30 && x1 <= 40)
						alive1 = false;
					if (x1 == 11 && y1 >= 14 && y1 <= 32)
						alive1 = false; 
					if (x1 == 40 && y1 >= 14 && y1 <= 32)
						alive1 = false;
					if (x1 >= 11 && x1 <= 23 && y1 == 14)
						alive1 = false;
					if (x1 >= 30 && x1 <= 40 && y1 == 14)
						alive1 = false;
					if (x1 == 23 && y1 <= 14)
						alive1 = false;
					if (x1 == 30 && y1 <= 14)
						alive1 = false;
					if (x1 == 13 && y1 >= 17 && y1 <= 27)
						alive1 = false;
					if (y1 == 17 && x1 >= 13 && x1 <= 33)
						alive1 = false;
					if (x1 == 33 && y1 >= 17 && y1 <= 21)
						alive1 = false;
					if (x1 >= 28 && x1 <= 33 && y1 == 21)
						alive1 = false;
					if (x1 == 28 && y1 >= 21 && y1 <= 26)
						alive1 = false;
					if (y1 == 26 && x1 >= 28 && x1 <= 33)
						alive1 = false;
					if (x1 == 33 && y1 >= 26 && y1 <= 29)
						alive1 = false;
					if (y1 == 29 && x1 <= 33 && x1 >= 18)
						alive1 = false;
					if (x1 == 18 && y1 <= 29 && y1 >= 27)
						alive1 = false;
					if (y1 == 27 && x1 >= 13 && x1 <= 18)
						alive1 = false;
					if (x1 == 26 && y1 <= 17)
						alive1 = false;
					if (x1 == 27 && y1 <= 17)
						alive1 = false;
					if (x1 >= 32 && x1 <= 40 && y1 == 24)
						alive1 = false;
				}			
				else if (difficulty == 4)
				{				
					if (x1 == 8 )
						alive1 = false;
					if (x1 == 11 && y1 > 7)
						alive1 = false;
					if (x1 == 13 && y1 > 9)
						alive1 = false;
					if (x1 == 16 && y1 > 12 && y1 < 34 )
						alive1 = false;
					if (x1 == 29 && y1 ==  32)
						alive1 = false;
					if (x1 == 37 && y1 > 12 && y1 < 29)
						alive1 = false;
					if (x1 == 40 && y1 > 9 && y1 < 32)
						alive1 = false;
					if (x1 == 42 && y1 > 7 && y1 < 34)
						alive1 = false;
					if (x1 == 45)
						alive1 = false;
					if (y1 == 5)
						alive1 = false;
					if (y1 == 8 && x1 > 10 && x1 < 43)
						alive1 = false;
					if (y1 == 10 && x1 > 12 && x1 < 41)
						alive1 = false;
					if (y1 == 13 && x1 > 15 && x1 < 38)
						alive1 = false;
					if (y1 == 31 && x1 > 19 && x1 < 41)
						alive1 = false;
					if (y1 == 33 && x1 > 15 && x1 < 43)
						alive1 = false;
					if (y1 == 36 && x1 > 12 && x1 < 46)
						alive1 = false;							
				}				
				else if (difficulty == 5)
				{
					if ((x1 == 49 && y1 > 5)|| ( x1 == 51)) 
						alive1 = false;	
					if (y1 == 3|| x1 == 3|| (y1 == 39 && x1 < 49)) 
						alive1 = false;	
					if (x1 ==5 && y1 > 5 && y1 < 37)
						alive1 = false;
					if (x1 > 4 && y1 == 36 && x1 < 48)
						alive1 = false;			
					if (x1 == 47 && y1 > 7 && y1 < 37)
						alive1 = false;	
					if (x1 == 45 && y1 > 9 && y1 < 35)
						alive1 = false;
					if (x1 == 43 && y1 > 11 && y1 < 33)
						alive1 = false;	
					if (x1 == 35 && y1 < 31 && y1 > 9)
						alive1 = false;	
					if (x1 == 33 && y1 > 11 && y1 < 29)
						alive1 = false;	
					if (x1 == 20 && y1 > 11 && y1 < 31)
						alive1 = false;	
					if (x1 == 18 && y1 > 9 && y1 < 33)
						alive1 = false;	
					if (x1 == 16 && y1 > 11 && y1 < 35)
						alive1 = false;				
					if (x1 == 14 && y1 < 33 && y1 > 9)
						alive1 = false;			
					if (x1 == 12 && y1 > 11 && y1 < 35)
						alive1 = false;	
					if (x1 == 10 && y1 > 9 && y1 < 33)
						alive1 = false;						
					if ( x1 == 7 && y1 < 35 && y1 > 7)
						alive1 = false;				
					if (x1 > 6 && y1 == 34 && x1 < 46)
						alive1 = false;
					if (x1 > 17 && y1 == 32 && x1 < 44)
						alive1 = false;
					if (y1 == 30 && x1 > 19 && x1 < 42)
						alive1 = false;
					if (x1 > 36 && y1 == 27 && x1 < 44)
						alive1 = false;
					if (x1 > 34 && y1 == 25 && x1 < 44 && x1 != 40)
						alive1 = false;				
					if (x1 > 34 && y1 == 22 && x1 < 44)
						alive1 = false;
					if (x1 > 34 && y1 == 19 && x1 < 44 && x1 != 38)
						alive1 = false;
					if (x1 > 34 && y1 == 17 && x1 < 42)
						alive1 = false;
					if (x1 > 34 && y1 == 14 && x1 < 44 && x1 != 39)
						alive1 = false;	
					if (x1 > 6 && y1 == 8 && x1 < 48)
						alive1 = false;
					if (x1 > 4 && y1 == 6 && x1 < 50)
						alive1 = false;	
					if (x1 > 9 && y1 == 10  && x1 < 46)
						alive1 = false;
					if (y1 == 12 && x1 > 19 && x1 < 34)
						alive1 = false;			
					if (x1 >= 26 && x1 <= 28 && y1 >= 19 && y1 <= 21)
						alive1 = false;
				}								
				if (alive1)
				{
					square1[y1][x1] = true;	
					if (dir1 == 'l' || dir1 == 'r')
						square1h[y1][x1] = true;
				}				
				dir1ss[y1][x1] = dir1;
				if (dir1 == 'u' && dir1ss[y1 + 1][x1] == 'l')
					square1c[y1 + 1][x1] = 1;
				else if (dir1 == 'r' && dir1ss[y1][x1 - 1] == 'd')
					square1c[y1][x1 - 1] = 1;
				else if (dir1 == 'd' && dir1ss[y1 - 1][x1] == 'l')
					square1c[y1 - 1][x1] = 2;
				else if ( dir1 == 'r' && dir1ss[y1][x1 - 1] == 'u')
					square1c[y1][x1 - 1] = 2;
				else if (dir1 == 'd'&& dir1ss[y1 - 1][x1] == 'r')
					square1c[y1 - 1][x1] = 3;
				else if (dir1 == 'l'&& dir1ss[y1][x1 + 1] == 'u')
					square1c[y1][x1 + 1] = 3;		   
				else if (dir1 == 'l'&& dir1ss[y1][x1 + 1] == 'd')
					square1c[y1][x1 + 1] = 4;
				else if (dir1 == 'u'&& dir1ss[y1 + 1][x1] == 'r')
					square1c[y1 + 1][x1] = 4;							
			}			
		}
	}	

	// moves the snake when playing survivor
	public static void movesnake()
	{		
		if (alive)
		{  

			tempx = x;
			tempy = y;	
			if (dir == 'u')
			{
				y -= 1;
			}
			else if (dir == 'd')
			{
				y += 1;
			}
			else if (dir == 'l')
			{
				x -= 1;
			}
			else if (dir == 'r')
			{
				x += 1;
			}			
			// when player hits a peck
			if (x == peckx && y == pecky)
			{
				peckx = (int)(Math.random() * WIDTH_SNAKE) + 1;
				pecky = (int)(Math.random() * HEIGHT_SNAKE) + 1;
				score ++;
			}			


			if (square[y][x] == true)
				alive = false;
			if (!(x >= 1 && x <= WIDTH_SNAKE && y >= 1 && y <= HEIGHT_SNAKE))
				alive = false;		

			if (alive)
			{
				// if it hasn't reached 2028 moves yet
				if (moves < MAX_SCORE + 1)
				{
					takenx[moves] = x;
					takeny[moves] = y;
					moves ++;
				}
				else 
				{
					// it's over 2028 moves, then the last one would be 
					// the most recent one (current value of x and y), and each value
					// in the array would get shifted one down
					for (int i = 1; i <  MAX_SCORE + 1; i ++)
					{
						takenx[i - 1] = takenx[i];
						takeny[i - 1] = takeny[i];
					}
					takenx[MAX_SCORE] = x;
					takeny[MAX_SCORE] = y;
				}		

				square = new boolean[HEIGHT_SNAKE + 2][WIDTH_SNAKE + 2];	 

				if (moves < MAX_SCORE + 1)
				{
					for (int i = moves - 1; i >= moves - 1 - score * 2; i --)
						square[takeny[i]][takenx[i]] = true;
				}
				else
				{	
					for (int i = MAX_SCORE; i >= MAX_SCORE - score * 2; i --)
						square[takeny[i]][takenx[i]] = true;
				}
			}	


		}
	}

	class Drawing extends JComponent
	{
		// the paint method draws the graphics
		public void paint(Graphics g)
		{
			// drawing the graphics if playing the mazes
			if (!snake)
			{			
				ImageIcon screen = null;
				if (difficulty == 1)
					screen = new ImageIcon("files/maze1.png");
				else if (difficulty == 2)
					screen = new ImageIcon("files/maze2.jpg");
				else if (difficulty == 3)
					screen = new ImageIcon("files/maze3.png");
				else if (difficulty == 4)
					screen = new ImageIcon("files/maze4.jpg");
				else if (difficulty == 5)
					screen = new ImageIcon("files/maze5.jpg");
				g.drawImage(screen.getImage(), 0, 0, 793, 623, this); 
				if (difficulty == 5)
				{
					g.setColor(Color.yellow); 
					for (int j = 26 ; j <= 28; j ++)
						for (int i = 19; i <= 21; i ++)
							g.fillRect((j -1) * BMTron.SPACE + 30, (i -1) * BMTron.SPACE + 45, BMTron.SPACE, BMTron.SPACE);
				}
				g.setFont(BMTron.font3);
				g.setColor(Color.white);
				g.drawString("Avoid the obstacles to win the maze.", 0, 15);	
				g.drawString("Use arrow keys.", 0, 35);
				// drawing all taken coordinates with appropriate pictures
				for (int i = 1; i <= Minigames.HEIGHT; i ++)
				{
					for (int j = 1; j <= Minigames.WIDTH; j ++)
					{
						if (Minigames.square1[i][j])
						{
							if (square1c[i][j] == 0)
							{
								if (square1h[i][j])
									g.drawImage(redh.getImage(), (j -1) * Minigames.SPACE + 30, (i -1) * Minigames.SPACE + 45, Minigames.SPACE, Minigames.SPACE, this);								
								else
									g.drawImage(redv.getImage(), (j -1) * Minigames.SPACE + 30, (i -1) * Minigames.SPACE + 45, Minigames.SPACE, Minigames.SPACE, this);
							}						
							if (square1c[i][j] == 1)
								g.drawImage(rsw.getImage(), (j -1) * Minigames.SPACE + 30, (i -1) * Minigames.SPACE + 45, Minigames.SPACE, Minigames.SPACE, this);
							else if (square1c[i][j] == 2)
								g.drawImage(rnw.getImage(), (j -1) * Minigames.SPACE + 30, (i -1) * Minigames.SPACE + 45, Minigames.SPACE, Minigames.SPACE, this);
							else if (square1c[i][j] == 3)
								g.drawImage(rne.getImage(), (j -1) * Minigames.SPACE + 30, (i -1) * Minigames.SPACE + 45, Minigames.SPACE, Minigames.SPACE, this);
							else if (square1c[i][j] == 4)
								g.drawImage(rse.getImage(), (j -1) * Minigames.SPACE + 30, (i -1) * Minigames.SPACE + 45, Minigames.SPACE, Minigames.SPACE, this);						
							if (sdir1 == 'u')
								g.drawImage(redtu.getImage(), (Minigames.sx1 -1) * Minigames.SPACE + 30, (Minigames.sy1 -1) * Minigames.SPACE + 45, Minigames.SPACE, Minigames.SPACE, this);
							else if (sdir1 == 'd')
								g.drawImage(redtd.getImage(), (Minigames.sx1 -1) * Minigames.SPACE + 30, (Minigames.sy1 -1) * Minigames.SPACE + 45, Minigames.SPACE, Minigames.SPACE, this);
							else if (sdir1 == 'l')
								g.drawImage(redtl.getImage(), (Minigames.sx1 -1) * Minigames.SPACE + 30, (Minigames.sy1 -1) * Minigames.SPACE + 45, Minigames.SPACE, Minigames.SPACE, this);
							else if (sdir1 == 'r')
								g.drawImage(redtr.getImage(), (Minigames.sx1 -1) * Minigames.SPACE + 30, (Minigames.sy1 -1) * Minigames.SPACE + 45, Minigames.SPACE, Minigames.SPACE, this);
							if (alive1)
							{
								if (dir1 == 'u')
									g.drawImage(redhu.getImage(), (Minigames.x1 -1) * Minigames.SPACE + 30, (Minigames.y1 -1) * Minigames.SPACE + 45, Minigames.SPACE, Minigames.SPACE, this);
								else if (dir1 == 'd')
									g.drawImage(redhd.getImage(), (Minigames.x1 -1) * Minigames.SPACE + 30, (Minigames.y1 -1) * Minigames.SPACE + 45, Minigames.SPACE, Minigames.SPACE, this);
								else if (dir1 == 'l')
									g.drawImage(redhl.getImage(), (Minigames.x1 -1) * Minigames.SPACE + 30, (Minigames.y1 -1) * Minigames.SPACE + 45, Minigames.SPACE, Minigames.SPACE, this);
								else if (dir1 == 'r')
									g.drawImage(redhr.getImage(), (Minigames.x1 -1) * Minigames.SPACE + 30, (Minigames.y1 -1) * Minigames.SPACE + 45, Minigames.SPACE, Minigames.SPACE, this);
							}																		
						}
					}
				}	   

				// after the player dies or wins	
				if (!Minigames.alive1)
				{     
					g.setFont(BMTron.font2);
					g.drawImage(congratz.getImage(), 0, 0, 793, 623, this);
					g.setColor(Color.red);
					if (difficulty == 1 && x1 >= 22 && x1 <= 25 && y1 == 41 || difficulty == 2 && x1 == 53 && y1 == 21 || difficulty == 3 && y1 == 0 && (x1 == 24 || x1 == 25 || x1 == 28 || x1 == 29) || difficulty == 4 && x1 == 29 && y1 ==  32 || difficulty == 5 && x1 >= 26 && x1 <= 28 && y1 >= 19 && y1 <= 21)
						g.drawString("You win!", 250, 300);
					else
						g.drawString("Game Over", 250, 300);
					delayed += 50;
					if (delayed == 2000)
					{
						move.interrupt();
						Minigames.frame.setVisible(false);
						Main.frame.setVisible(true);
					}
				}         
			}
			// draws the graphics if playing survivor
			else
			{		
				g.drawImage(screen.getImage(), 0, 0, 793, 623, this);
				g.setFont(font);
				g.drawString("Score: " + Minigames.score,500, 25);
				g.setColor(Color.white);
				g.setFont(font3);
				g.drawString("Lengthen the snaken by eating the speck.", 0, 25);	
				g.drawString("Use arrow keys.", 0, 50);
				g.setColor(Color.green);
				g.fillOval(Minigames.SPACE * (peckx - 1) + 30, Minigames.SPACE * (pecky - 1) + 60, Minigames.SPACE, Minigames.SPACE);
				for (int i = 1; i <= Minigames.HEIGHT_SNAKE; i ++)
				{
					for (int j = 1; j <= Minigames.WIDTH_SNAKE; j ++)
					{
						if (Minigames.square[i][j])
						{
							g.setColor(Color.red);
							g.fillRect((j -1) * Minigames.SPACE + 30, (i -1) * Minigames.SPACE + 60, Minigames.SPACE, Minigames.SPACE);
						}
					}
				}	

				// after the player dies		
				if (!alive)
				{
					g.setFont(font2);
					g.setColor(Color.red);
					g.drawImage(congratz.getImage(), 0, 0, 793, 623, this);
					g.drawString("Game Over", 250, 250);
					g.drawString("Final Score: " + score, 250, 350);
					Minigames.delayed += 50;
					if (Minigames.delayed == 2000)
					{
						move.interrupt();
						Minigames.frame.setVisible(false);
						Main.frame.setVisible(true);
						delayed = 0;
					}
				}			
			}       
		}			
	}

	// changes the directions according to input
	public void keyPressed(KeyEvent evt)
	{
		int key = evt.getKeyCode(); 
		if (!snake)
		{		 
			if (key == KeyEvent.VK_DOWN && !(y1 == tempy1 - 1))
				dir1 = 'd';
			else if (key == KeyEvent.VK_LEFT && !(x1 == tempx1 + 1))
				dir1 = 'l';
			else if (key == KeyEvent.VK_RIGHT && !(x1 == tempx1 - 1))
				dir1 = 'r';
			else if (key == KeyEvent.VK_UP && !(y1 == tempy1 +1))
				dir1 = 'u';		
		}
		else
		{
			if (key == KeyEvent.VK_DOWN && !(y == tempy - 1))
				dir = 'd';
			if (key == KeyEvent.VK_LEFT && !(x == tempx + 1))
				dir = 'l';
			if (key == KeyEvent.VK_RIGHT && !(x == tempx - 1))
				dir = 'r';
			if (key == KeyEvent.VK_UP && !(y == tempy +1))
				dir = 'u';
		}
	}	
	public void keyTyped(KeyEvent evt)
	{
	}

	public void keyReleased(KeyEvent evt)
	{
	}	

	class Move extends Thread
	{
		public void run()
		{
			try
			{
				while (true)
				{
					sleep(delayTime);
					if (snake)
						movesnake();
					else
						move();

					draw.repaint();
				}
			}
			catch (InterruptedException e)
			{
			}
		}
	}
}







