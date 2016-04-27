package Game;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

@SuppressWarnings("serial")
public class StartingClass extends Applet implements Runnable, MouseListener,
		MouseMotionListener, KeyListener {

	Paddle paddle, comp_paddle, left_paddle, right_paddle;
	int n_balls = 1;
	Ball ball[] = new Ball[n_balls];
	private Image Ball, Paddle, image;
	private URL base;
	private Graphics second;
	int anim_time = 5;
	boolean started, clicked_ip_box,player2,player3,player4,to_start,create_not_join;
	String ip_text = "Your IP Goes Here";
	final int barrier_height = 50;

	final int border_top = 0, border_bottom = 480, border_left = 0,
			border_right = 480;

	@Override
	public void init() {
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setSize(border_right, border_bottom);
		setBackground(Color.BLACK);
		setFocusable(true);
		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("Juggler");
		try {
			base = getDocumentBase();
		} catch (Exception e) {
		}
		Ball = getImage(base, "Data/Ball.png");
		Paddle = getImage(base, "Data/Paddle.png");
	}

	@Override
	public void start() {
		started = false;
		clicked_ip_box = false;
		player2 = false;
		player3 = false;
		player4 = false;
		to_start = false;
		create_not_join = false;
		paddle = new Paddle(80, border_right / 2,
				border_right - barrier_height, barrier_height, 20, -1);
		comp_paddle = new Paddle(80, border_right / 2, border_right
				- barrier_height, barrier_height, 20, -1);
		left_paddle = new Paddle(80, border_right / 2, border_right
				- barrier_height, barrier_height, 20, -1);
		right_paddle = new Paddle(80, border_right / 2, border_right
				- barrier_height, barrier_height, 20, -1);
		int radius = 10;
		for (int b = 0; b < n_balls; b++)
			ball[b] = new Ball(paddle.getPos()
					+ (2 * paddle.getSize() * (2 * b - n_balls))
					/ (5 * n_balls),
					(int) (border_bottom - paddle.height - radius), radius);
		gameNotStarted();
		Thread thread = new Thread(this);
		thread.start();
	}

	private void gameNotStarted() {
		Thread thread = new Thread() {
			public void run() {
				while (!started)
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		};
		thread.start();
	}

	@Override
	public void paint(Graphics g) {
		for (int i = 0; i < n_balls; i++)
			g.drawImage(Ball, (int) ball[i].getX() - ball[i].getSize(),
					(int) ball[i].getY() - ball[i].getSize(),
					2 * ball[i].getSize(), 2 * ball[i].getSize(), this);
		g.drawImage(Paddle, paddle.getPos() - paddle.getSize() / 2,
				border_bottom - paddle.height, paddle.getSize(), paddle.height,
				this);
		g.drawImage(
				Paddle,
				border_right - comp_paddle.getPos() - comp_paddle.getSize() / 2,
				border_top, comp_paddle.getSize(), comp_paddle.height, this);
		g.drawImage(Paddle, border_left,
				left_paddle.getPos() - left_paddle.getSize() / 2,
				left_paddle.height, left_paddle.getSize(), this);
		g.drawImage(Paddle, border_right - right_paddle.height, border_bottom
				- right_paddle.getPos() - right_paddle.getSize() / 2,
				right_paddle.height, right_paddle.getSize(), this);
		// barriers
		g.drawImage(Paddle, border_left, border_top, barrier_height,
				barrier_height, this);
		g.drawImage(Paddle, border_right - barrier_height, border_top,
				barrier_height, barrier_height, this);
		g.drawImage(Paddle, border_left, border_bottom - barrier_height,
				barrier_height, barrier_height, this);
		g.drawImage(Paddle, border_right - barrier_height, border_bottom
				- barrier_height, barrier_height, barrier_height, this);
	}

	@Override
	public void update(Graphics g) {
		if (image == null) {
			image = createImage(this.getWidth(), this.getHeight());
			second = image.getGraphics();
		}

		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());

		if (started)
			paint(second);
		else if(to_start && create_not_join)
			new OpeningScreen(Paddle, ip_text,player2,player3,player4).paint(second);
		else if(to_start && !create_not_join)
			new JoinGame(Paddle, ip_text).paint(second);
		else
			new Choose_Game_Type(Paddle).paint(second);
		g.drawImage(image, 0, 0, this);

	}

	@Override
	public void run() {
		float x, y, radius = ball[0].getSize(), paddle_pos, paddle_size, comp_paddle_pos, comp_paddle_size, right_paddle_pos, right_paddle_size, left_paddle_size, left_paddle_pos;
		boolean flag = true;
		long t1, t2, t3 = 0, t4;
		while (true) {
			t1 = System.currentTimeMillis();
			if (flag)
				repaint();
			for (int b = 0; b < n_balls; b++) {
				x = ball[b].getX();
				y = ball[b].getY();
				// System.out.println(b + "  " + x + "  " + y);
				paddle_pos = paddle.getPos();
				paddle_size = paddle.getSize();

				// setOnPaddle() calls
				if (ball[b].getSpeedX() == 0 && ball[b].getSpeedY() == 0
						&& y + radius + paddle.height >= border_bottom)
					ball[b].setOnPaddle(true);
				else
					ball[b].setOnPaddle(false);

				// paddle reflections
				if (y + paddle.height + radius >= border_bottom
						&& (Math.abs(x - paddle_pos) <= paddle_size / 2)) {
					float sx = ball[b].getSpeedX(), sy = ball[b].getSpeedY();
					float speed = (float) Math.sqrt(sx * sx + sy * sy);
					double angle = 5 * Math.PI / 6 * (x - paddle_pos)
							/ paddle_size;
					ball[b].setSpeedY((float) (-speed * Math.cos(angle)));
					ball[b].setSpeedX((float) (speed * Math.sin(angle)));
				}

				// comp_paddle
				comp_paddle_pos = border_right - comp_paddle.getPos();
				comp_paddle_size = comp_paddle.getSize();

				// comp_paddle reflections
				if (y - comp_paddle.height - radius <= border_top
						&& (Math.abs(x - comp_paddle_pos) <= comp_paddle_size / 2)) {
					float sx = ball[b].getSpeedX(), sy = ball[b].getSpeedY();
					float speed = (float) Math.sqrt(sx * sx + sy * sy);
					double angle = 5 * Math.PI / 6 * (x - comp_paddle_pos)
							/ comp_paddle_size;
					ball[b].setSpeedY((float) (speed * Math.cos(angle)));
					ball[b].setSpeedX((float) (speed * Math.sin(angle)));
				}

				// left_paddle
				left_paddle_pos = left_paddle.getPos();
				left_paddle_size = left_paddle.getSize();

				// left_paddle reflections
				if (x - left_paddle.height - radius <= border_left
						&& (Math.abs(y - left_paddle_pos) <= left_paddle_size / 2)) {
					float sx = ball[b].getSpeedX(), sy = ball[b].getSpeedY();
					float speed = (float) Math.sqrt(sx * sx + sy * sy);
					double angle = 5 * Math.PI / 6 * (y - left_paddle_pos)
							/ left_paddle_size;
					ball[b].setSpeedX((float) (speed * Math.cos(angle)));
					ball[b].setSpeedY((float) (speed * Math.sin(angle)));
					// System.out.println(b+" "+comp_paddle_pos + " " + x + " "
					// +
					// comp_paddle.getSize() + " " + ball[b].getSpeedX() + " "+
					// ball[b].getSpeedY());
				}
				// right_paddle
				right_paddle_pos = border_bottom - right_paddle.getPos();
				right_paddle_size = right_paddle.getSize();

				// left_paddle reflections
				if (x + right_paddle.height + radius >= border_right
						&& (Math.abs(y - right_paddle_pos) <= right_paddle_size / 2)) {
					float sx = ball[b].getSpeedX(), sy = ball[b].getSpeedY();
					float speed = (float) Math.sqrt(sx * sx + sy * sy);
					double angle = 5 * Math.PI / 6 * (y - right_paddle_pos)
							/ right_paddle_size;
					ball[b].setSpeedX((float) (-speed * Math.cos(angle)));
					ball[b].setSpeedY((float) (speed * Math.sin(angle)));
					// System.out.println(b+" "+comp_paddle_pos + " " + x + " "
					// +
					// comp_paddle.getSize() + " " + ball[b].getSpeedX() + " "+
					// ball[b].getSpeedY());
				}

				// border reflections
				if (x - radius <= border_left) {
					ball[b].setSpeedX(Math.abs(ball[b].getSpeedX()));
					ball[b].setX(border_left + radius);
				}
				if ((border_top + barrier_height >= y - radius || border_bottom
						- barrier_height <= y + radius)
						&& x - radius <= border_left + barrier_height
						&& x - radius - ball[b].getSpeedX() > border_left
								+ barrier_height) {
					ball[b].setSpeedX(Math.abs(ball[b].getSpeedX()));
					ball[b].setX(border_left + barrier_height + radius);
				}
				if (x + radius >= border_right) {
					ball[b].setSpeedX(-Math.abs(ball[b].getSpeedX()));
					ball[b].setX(border_right - radius);
				}
				if ((border_top + barrier_height >= y - radius || border_bottom
						- barrier_height <= y + radius)
						&& x + radius >= border_right - barrier_height
						&& x + radius - ball[b].getSpeedX() < border_right
								- barrier_height) {
					ball[b].setSpeedX(-Math.abs(ball[b].getSpeedX()));
					ball[b].setX(border_right - barrier_height - radius);
				}
				if (y - radius <= border_top) {
					ball[b].setSpeedY(Math.abs(ball[b].getSpeedY()));
					ball[b].setY(border_top + radius);
				}
				if ((x - radius <= border_left + barrier_height || x + radius >= border_right
						- barrier_height)
						&& border_top + barrier_height >= y - radius
						&& border_top + barrier_height < y - radius
								- ball[b].getSpeedY()) {
					ball[b].setSpeedY(Math.abs(ball[b].getSpeedY()));
					ball[b].setY(border_top + barrier_height + radius);
				}
				if (y + radius >= border_bottom) {
					ball[b].setSpeedY(-Math.abs(ball[b].getSpeedY()));
					ball[b].setY(border_bottom - radius);
				}
				if ((x - radius < border_left + barrier_height || x + radius > border_right
						- barrier_height)
						&& border_bottom - barrier_height <= y + radius
						&& border_bottom - barrier_height > y + radius
								- ball[b].getSpeedY()) {
					ball[b].setSpeedY(-Math.abs(ball[b].getSpeedY()));
					ball[b].setY(border_bottom - barrier_height - radius);
				}
				// update ball position
				ball[b].update();
				// System.out.println(b + " " + ball[b].getX() + " " +
				// ball[b].getY());
			}
			if (ball[0].getX() != border_right - comp_paddle.getPos())
				comp_paddle
						.setPos((int) (border_right - ball[0].getX() + (ball[0]
								.getX() - border_right - comp_paddle.getPos() >= 0 ? 10
								: -10)));
			if (ball[0].getY() != border_bottom - comp_paddle.getPos())
				left_paddle
						.setPos((int) (ball[0].getY() + (ball[0].getY()
								- border_right - left_paddle.getPos() >= 0 ? 10
								: -10)));
			if (ball[0].getY() != border_bottom - comp_paddle.getPos())
				right_paddle
						.setPos((int) (border_bottom - ball[0].getY() - (ball[0]
								.getY() - border_right - right_paddle.getPos() >= 0 ? 10
								: -10)));
			t2 = System.currentTimeMillis();
			t3 += t2 - t1;
			System.out.println(t3);
			if (t3 <= anim_time) {
				flag = true;
				t4 = t3;
				t3 = 0;
				try {
					Thread.sleep(anim_time - t4);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Missed Frame");
				flag = false;
				t3 -= anim_time;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (to_start && !started && arg0.getX() > 80 && arg0.getX() < 340
				&& arg0.getY() > 100 && arg0.getY() < 130) {
			clicked_ip_box = true;
			ip_text = "";
		}
		else if (to_start && create_not_join && !started && arg0.getX() > 80 && arg0.getX() < 400
				&& arg0.getY() > 140 && arg0.getY() < 170) {
			player2=!player2;
		}
		else if (to_start && create_not_join && !started && arg0.getX() > 80 && arg0.getX() < 400
				&& arg0.getY() > 180 && arg0.getY() < 210) {
			player3=!player3;
		}
		else if (to_start && create_not_join && !started && arg0.getX() > 80 && arg0.getX() < 400
				&& arg0.getY() > 220 && arg0.getY() < 250) {
			player4=!player4;
		}
		else if(to_start && !started && !(arg0.getX() > 80 && arg0.getX() < 400
				&& arg0.getY() > 80 && arg0.getY() < 280)) {
			started = true;
			clicked_ip_box = false;
		}
		else if(!to_start && (arg0.getX() > 100 && arg0.getX() < 340
				&& arg0.getY() > 100 && arg0.getY() < 130))
		{
			to_start=true;
			create_not_join=true;
		}
		else if(!to_start && (arg0.getX() > 100 && arg0.getX() < 340
				&& arg0.getY() > 140 && arg0.getY() < 170))
		{
			to_start=true;
			create_not_join=false;
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		for (int b = 0; b < n_balls; b++) {
			if (ball[b].isOnPaddle()) {
				ball[b].setSpeedY(-2.5f);
				ball[b].setSpeedX((ball[b].getX() - paddle.getPos())
						/ ((float) paddle.getSize()) * 2);
				ball[b].setOnPaddle(false);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		paddle.setPos(arg0.getX());
		for (int b = 0; b < n_balls; b++) {
			if (ball[b].isOnPaddle())
				ball[b].setX(paddle.getPos()
						+ (2 * paddle.getSize() * (2 * b - n_balls))
						/ (5 * n_balls));
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		for (int b = 0; b < n_balls; b++) {
			paddle.setPos(arg0.getX());
			// comp_paddle.setPos(arg0.getX());
			// left_paddle.setPos(arg0.getX());
			// right_paddle.setPos(arg0.getX());
			if (ball[b].isOnPaddle())
				ball[b].setX(paddle.getPos()
						+ (2 * paddle.getSize() * (2 * b - n_balls))
						/ (5 * n_balls));
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (clicked_ip_box) {
			switch (arg0.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				started = true;
				break;
			case KeyEvent.VK_BACK_SPACE:
				ip_text = (ip_text.length() == 0) ? "" : ip_text.substring(0,
						ip_text.length() - 1);
				break;
			default:
				ip_text = ip_text.length()==15?ip_text:ip_text + arg0.getKeyChar();
			}
		}
		else if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {
			paddle.setPos(paddle.getPos() - 10);
		} else if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
			paddle.setPos(paddle.getPos() + 10);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
