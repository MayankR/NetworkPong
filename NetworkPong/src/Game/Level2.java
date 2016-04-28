package Game;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class Level2 extends Applet implements Level{

	public void display(Graphics g, Image i1[])
	{
		//g.setColor(Color.RED);
		//g.fillRect(300, 300, 100, 100);
		g.drawImage(i1[0], 220, 190, 40, 100, this);
		g.drawImage(i1[0], 190, 220, 100, 40, this);
	}
	
	public void reflect(Ball ball)
	{
		float currX = ball.getX();						//get Current position of ball
		float currY = ball.getY();						
		float nextX = ball.getX() + ball.getSpeedX();	//get next position of ball
		float nextY = ball.getY() + ball.getSpeedY();
	
		// The reflection code for wall 1
		if( ((currY > 290  && nextY < 290) || (currY < 190  && nextY > 190))  && (nextX < 260 && nextX > 220)){
			ball.setSpeedY(-ball.getSpeedY());
		}
		else if( ((currX > 260  && nextX < 260) || (currX < 220  && nextX > 220))  && (nextY < 290 && nextY > 190)){
			ball.setSpeedX(-ball.getSpeedX());
		}
		
		// The reflection code for wall 1
		if( ((currX > 290  && nextX < 290) || (currX < 190  && nextX > 190))  && (nextY < 260 && nextY > 220)){
			ball.setSpeedX(-ball.getSpeedX());
		}
		else if( ((currY > 260  && nextY < 260) || (currY < 220  && nextY > 220))  && (nextX < 290 && nextX > 190)){
			ball.setSpeedY(-ball.getSpeedY());
		}
	}
	

}
