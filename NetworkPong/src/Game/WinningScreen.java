package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class WinningScreen {

	public void paint(Graphics g, int Winner) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g.drawString("Player "+ Winner + " Wins",110,120);
	}
}
