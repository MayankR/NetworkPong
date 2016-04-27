package Game;

import java.awt.Graphics;
import java.awt.Image;

public interface Level {

	public void display(Graphics g,Image i1[]);
	
	public void reflect(Ball b);
	
}
