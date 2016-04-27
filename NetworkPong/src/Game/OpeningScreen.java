package Game;

import java.applet.Applet;
import java.awt.Color;
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

public class OpeningScreen extends Applet {
	
	private URL base;
	Image ball,b1;
	OpeningScreen(Image i1)
	{
		try {
			this.base = base;
		} catch (Exception e) {
		}
		Toolkit t=Toolkit.getDefaultToolkit();  
        ball=t.getImage("/Data/Ball.png");
//		ball = getImage(base, "Data/Ball.png");
        b1=i1;
		System.out.println("#@#!@!#!@#!@#!#1   "+ball+"   "+i1);
	}
			
	public void paint(Graphics g) {
		System.out.println("#@#!@!#!@#!@#!#1   "+ball+"   "+b1);
		g.drawImage(ball, 0, 0, 100, 100, this);
		g.drawImage(b1, 100, 100, 100, 100, this);
		g.setColor(Color.BLUE);
//		g.fillRect(0, 0, 400, 400);
	}


}
