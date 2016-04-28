package Game;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class Level1 extends Applet implements Level{


	public void display(Graphics g, Image i1[])								// Draw barriers
	{
		//g.setColor(Color.RED);
		//g.fillRect(210, 270, 60, 10);
		g.drawImage(i1[0], 210, 210, 60, 60, this);
	}
	
	public void reflect(Ball ball)											// Write reflecton properties
	{
		float currX = ball.getX();						//get Current position of ball
		float currY = ball.getY();						
		float nextX = ball.getX() + ball.getSpeedX();	//get next position of ball
		float nextY = ball.getY() + ball.getSpeedY();
	
		// The reflection code
		if( ((currY > 270  && nextY < 270) || (currY <210  && nextY > 210))  && (nextX < 270 && nextX >210)){
			ball.setSpeedY(-ball.getSpeedY());
		}
		if( ((currX > 270  && nextX < 270) || (currX <210  && nextX > 210))  && (nextY < 270 && nextY >210)){
		ball.setSpeedX(-ball.getSpeedX());
		}
			
	}
	

}
