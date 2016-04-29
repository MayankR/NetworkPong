package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

public class Choose_Game_Type {
	
//	private URL base;
	Image ball,b1;
	String ip_dat;
	boolean player2,player3,player4;
	Choose_Game_Type(Image i1)
	{
//		try {
//			this.base = base;
//		} catch (Exception e) {
//		}
//		Toolkit t=Toolkit.getDefaultToolkit();  
//        ball=t.getImage("/Data/Ball.png");
//		ball = getImage(base, "Data/Ball.png");
        b1=i1;
	}
			
	public void paint(Graphics g) {
		g.drawImage(b1, 100, 100, 240, 30, null);
		g.drawImage(b1, 100, 140, 240, 30, null);
//		g.drawImage(b1, 100, 180, 240, 30, this);
//		g.drawImage(b1, 100, 220, 240, 30, this);
		g.setColor(Color.WHITE);
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g.drawString("Create a New Game",110,120);
		g.drawString("Join Existing Game",110,160);
	}
}
