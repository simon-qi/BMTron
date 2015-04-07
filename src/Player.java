import java.awt.*;

// a player of the main game (Arena)
class Player
{
	int x;
	int y; // position
	char dir; // direction as a char
	boolean[][] square; // whether the square is occupied, x goes from 1 to 50, y goes from 1 to 39
	boolean alive;
	int score;
	boolean exist;
	boolean ai;
	String name;
	int keyDown;
	int keyLeft;
	int keyRight;
	int keyUp;
	Color color;



	public Player()
	{
		x = 0;
		y = 0;
		dir = 'a';
		score = 0;
	}

	// the ai determing directions
	private void moveAI()
	{
		if (exist && ai)
		{	
			int xfinal = 0;      	
			int yfinal = 0;
			int nd = 0; // length till you die down
			int nl = 0;
			int nr = 0;
			int nu = 0;
			boolean foundd = false;
			boolean foundl = false; // whether a coordinate taken has by found in left direction
			boolean foundr = false;
			boolean foundu = false;

			xfinal = x;
			yfinal = y;

			for (int i = yfinal + 1; i <= BMTron.HEIGHT && !foundd ; i ++)
			{
				if (BMTron.players[1].square[i][xfinal] || BMTron.players[2].square[i][xfinal] || 
						BMTron.players[3].square[i][xfinal] || BMTron.players[4].square[i][xfinal])
				{
					foundd = true;
					nd = i - yfinal;
				}
			}
			// if no taken coordinate in that direction found, then the wall would be the coordinate
			if (!foundd)
				nd = (BMTron.HEIGHT + 1) - yfinal;		
			for (int i = xfinal - 1; i >= 1 && !foundl ; i --)
			{
				if (BMTron.players[1].square[yfinal][i] || BMTron.players[2].square[yfinal][i] || BMTron.players[3].square[yfinal][i] ||
						BMTron.players[4].square[yfinal][i])
				{
					foundl = true;
					nl = xfinal - i;
				}
			}
			if (!foundl)
				nl = xfinal;		
			for (int i = xfinal + 1; i <= BMTron.WIDTH && !foundr ; i ++)
			{
				if (BMTron.players[1].square[yfinal][i] || BMTron.players[2].square[yfinal][i] || BMTron.players[3].square[yfinal][i] || 
						BMTron.players[4].square[yfinal][i])
				{
					foundr = true;
					nr = i - xfinal;
				}
			}
			if (!foundr)
				nr = (BMTron.WIDTH + 1) - xfinal;		
			for (int i = yfinal - 1; i >= 1 && !foundu ; i --)
			{
				if (BMTron.players[1].square[i][xfinal] || BMTron.players[2].square[i][xfinal] || 
						BMTron.players[3].square[i][xfinal] || BMTron.players[4].square[i][xfinal])
				{
					foundu = true;
					nu = yfinal - i;
				}
			}		

			if (!foundu)
				nu = yfinal;			      
			// going with the longer directions, will pick a random direction between directions that are within 20% of longest
			int longest;
			if (nd > nl && nd > nr && nd > nu)
				longest = nd;
			else if (nl > nr && nl > nu)
				longest = nl;
			else if (nr > nu)
				longest = nr;
			else 
				longest = nu;					
			int eligibles = 0; // how many eligibles
			int d = 0; // the number assigned to each direction, 0 is not a value that can be generated
			int l = 0;
			int r = 0;
			int u = 0;
			if ((double)(longest - nd) / longest < BMTron.AIaccuracy)
			{
				eligibles ++;
				d = eligibles;
			}
			if ((double)(longest - nl) / longest < BMTron.AIaccuracy)
			{
				eligibles ++;
				l = eligibles;
			}
			if ((double)(longest - nr) / longest < BMTron.AIaccuracy)
			{
				eligibles ++;
				r = eligibles;
			}
			if ((double)(longest - nu) / longest < BMTron.AIaccuracy)
			{
				eligibles ++;
				u = eligibles;
			}				
			int random = (int)(Math.random() * eligibles) + 1;

			if (d  == random)
				dir = 'd';
			else if (l == random)
				dir = 'l';
			else if (r == random)
				dir = 'r';
			else if (u == random)
				dir = 'u';	
		}
	}

	// moves the coordinate of the head of the snake
	public void move()
	{		
		moveAI();
		// can only start if everyone playing has a direction		
		boolean canStart = true;
		for (int i = 0; i < 5; i++)
		{
			if (BMTron.players[i].exist && BMTron.players[i].dir == 'a')
				canStart = false;
		}
		BMTron.starting = canStart; 

		if (BMTron.starting)
		{
			if (alive)
			{	
				if (dir == 'u')
					y -= 1;
				else if (dir == 'd')
					y += 1;
				else if (dir == 'l')
					x -= 1;
				else if (dir == 'r')
					x += 1;								   
				// if you try to move to previously taken coordinate, then die
				if (BMTron.players[1].square[y][x] == true || 
						BMTron.players[2].square[y][x] == true ||
						BMTron.players[3].square[y][x] == true ||
						BMTron.players[4].square[y][x] == true )
					alive = false;
				// if you go out of boundary
				if (!(x >= 1 && x <= BMTron.WIDTH && y >= 1 && y <= BMTron.HEIGHT))
					alive = false;
				if (alive)
				{
					square[y][x] = true;
				}							   
			}								
		}
	}
}