package Game;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class Level1 extends Applet implements Level{

	public void display(Graphics g, Image i1[])
	{
		g.setColor(Color.RED);
		g.fillRect(300, 300, 100, 100);
		g.drawImage(i1[0], 100, 100, 100, 100, this);
	}
	
	public void reflect(Ball ball)
	{
		if(ball.getY()<300)
			ball.setSpeedY(-ball.getSpeedY());
	}
	

}
