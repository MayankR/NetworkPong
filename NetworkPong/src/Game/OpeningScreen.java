package Game;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

public class OpeningScreen{
	
	private URL base;
	Image ball,b1;
	String ip_dat;
	boolean player2,player3,player4;
	OpeningScreen(Image i1,String x,boolean pl2,boolean pl3,boolean pl4)
	{
		player2=pl2;
		player3=pl3;
		player4=pl4;
		ip_dat=x;
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
		g.drawImage(b1, 180, 300, 100, 30, null);
//		g.drawImage(b1, 100, 140, 240, 30, this);
//		g.drawImage(b1, 100, 180, 240, 30, this);
//		g.drawImage(b1, 100, 220, 240, 30, this);
		g.setColor(Color.WHITE);
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g.drawString(ip_dat,110,120);
		g.drawString("PLAYER 2 : "+(player2?"Human":"Computer"),110,160);
		g.drawString("PLAYER 3 : "+(player3?"Human":"Computer"),110,200);
		g.drawString("PLAYER 4 : "+(player4?"Human":"Computer"),110,240);
		g.drawString("START",192,322);
	}
}
