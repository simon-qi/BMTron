import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// this class allows user to customize settings for one arena game
public class Selection implements MouseListener
{
	ImageIcon icon;
	JTextField p1 = new JTextField(20);
	JTextField p2 = new JTextField(20);
	JTextField p3 = new JTextField(20);
	JTextField p4 = new JTextField(20); 
	static JFrame frame = new JFrame("Serpentine");
	ImageIcon yes = new ImageIcon("files/yes.png"); // image to indicate player exist
	ImageIcon no = new ImageIcon("files/no.png");   // blank image to indicate player doesn't exist
	ImageIcon player = new ImageIcon("files/p.png"); // image to indicate player is human
	ImageIcon aiPic = new ImageIcon("files/c.png"); // image to indicate player is ai
	Font font = new Font("SansSerif", Font.BOLD, 20);
	static boolean[] ai = new boolean[5]; // whether player is an ai
	static boolean team; 	
	static boolean[] exist = new boolean[5]; // whether the player exists

	static String[] name = new String[5];

	// the constructor for the application
	public Selection()
	{	
		// initial default settings   
		for (int i = 0; i < 5; i++) 
		{
			name[i] = "Player " + i;
		}
		ai[1] = false;
		ai[2] = true;
		ai[3] = true;
		ai[4] = false;
		team = false;
		exist[1] = true;
		exist[2] = false;
		exist[3] = false;
		exist[4] = true;
		icon = new ImageIcon("files/Mainpage2.png");   
		JPanel panel = new JPanel()
		{  
			// draws the backround images and custimizations
			protected void paintComponent(Graphics g)
			{
				g.drawImage(icon.getImage(), 0,0, 773, 614, null);
				for (int i = 1; i <= 4; i++) 
				{
					if (exist[i]) 
						g.drawImage(yes.getImage(), 21 - 10, 229 - 30 + (i - 1) * 91, null);
					else
						g.drawImage(no.getImage(), 21 - 10, 229 - 30 + (i - 1) * 91, null);

					if (ai[i]) 
						g.drawImage(aiPic.getImage(), 700 - 10, 229 - 30 + (i - 1) * 91, null); 
					else
						g.drawImage(player.getImage(), 700 - 10, 229 - 30 + (i - 1) * 91, null); 
				}
				g.setColor(Color.white);
				g.drawString("Team 1 (red): Player 1 (top left) and Player 2 (bottom left)", 15, 125);
				g.drawString("Team 2 (green): Player 3 (top right) and Player 4 (bottom right)", 15, 145);
				repaint();
				super.paintComponent(g);
			}
		};    
		panel.setOpaque(false);
		panel.setLayout(null);
		// adding the textboxes
		p1.setOpaque(false);
		p1.setForeground(Color.white);
		p1.setText(name[1]);
		p1.setFont(font);
		p2.setOpaque(false);
		p2.setForeground(Color.white);
		p2.setText(name[2]);
		p2.setFont(font);
		p3.setOpaque(false);
		p3.setForeground(Color.white);
		p3.setText(name[3]);
		p3.setFont(font);
		p4.setOpaque(false);
		p4.setForeground(Color.white);
		p4.setText(name[4]);
		p4.setFont(font);
		p1.setBounds(85, 200, 460, 65);
		p2.setBounds(85, 290, 460, 65);
		p3.setBounds(85, 380, 460, 65);
		p4.setBounds(85, 470, 460, 65); 
		frame.addMouseListener(this);
		frame.setResizable(false);
		panel.add(p1);
		panel.add(p2);
		panel.add(p3);
		panel.add(p4);
		frame.add(panel);   
		frame.setSize(773 + 5, 614 + 25);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	// main method runs the application
	public static void main(String []args) 
	{
		new Selection();
	}   

	// changes settings and updates graphics when user clicked appropriate coordinates
	public void mouseClicked (MouseEvent e)
	{
		int players = 0;   
		for (int i = 0; i < 5; i++)
		{
			if (exist[i])
				players++;
		}
		int x = e.getX();
		int y = e.getY();

		if (x >= 21 && x <= 76 && y >= 229 && y <= 281)
			exist[1] = !exist[1];
		else if (x >= 21 && x <= 76 && y >= 322 && y <= 374)
			exist[2] = !exist[2];
		else if (x >= 21 && x <= 76 && y >= 412 && y <= 466)
			exist[3] = !exist[3];
		else if (x >= 21 && x <= 76 && y >= 503 && y <= 556)
			exist[4] = !exist[4];		
		else if (x >= 700 && x <= 755 && y >= 229 && y <= 281)
			ai[1] = !ai[1];	
		else if (x >= 700 && x <= 755 && y >= 322 && y <= 374)
			ai[2] = !ai[2];
		else if (x >= 700 && x <= 755 && y >= 412 && y <= 466)
			ai[3] = !ai[3];   
		else if (x >= 700 && x <= 755 && y >= 503 && y <= 556)
			ai[4] = !ai[4];		
		else if (x >= 420 && x <= 488 && y >= 600 && y <= 620) // back
		{
			frame.setVisible(false);
			Main.frame.setVisible(true);
		}		   		
		else if (x >= 540 && x <= 614 && y >= 600 && y <= 620) // start
		{
			if (players >= 2)
			{
				team = false;
				name[1] = p1.getText();
				name[2] = p2.getText();
				name[3] = p3.getText();
				name[4] = p4.getText();
				frame.setVisible(false);
				new BMTron();
			}
			else
			{
				JOptionPane.showMessageDialog(frame,"You need at least 2 active players to start the game.");
			}
		}				
		else if (x >= 657 && x <= 741 && y >= 600 && y <= 620) // team
		{
			if (players == 4)
			{
				team = true;
				name[1] = p1.getText();
				name[2] = p2.getText();
				name[3] = p3.getText();
				name[4] = p4.getText();
				frame.setVisible(false);
				new BMTron();
			}			
			else
			{
				JOptionPane.showMessageDialog(frame,			
						"You need all 4 players active to start teams.");
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
