package Game;

public class Ball {

	private int size;
	private float x, y, speedx, speedy;
	private boolean onPaddle;

	Ball(float x, float y, int size) {
		this.x = x;
		this.y = y;
		this.speedx = 0;
		this.speedy = 0;
		this.size = size;
		setOnPaddle(true);
	}

	public float getY() {
		return y;
	}

	public void update() {
		x += speedx;
		y += speedy;
	}

	public void reflect() {

	}

	public void setY(float y) {
		this.y = y;
	}

	public float getSpeedX() {
		return speedx;
	}

	public void setSpeedX(float speedx) {
		this.speedx = speedx;
	}

	public float getSpeedY() {
		return speedy;
	}

	public void setSpeedY(float speedy) {
		this.speedy = speedy;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isOnPaddle() {
		return onPaddle;
	}

	public void setOnPaddle(boolean onPaddle) {
		this.onPaddle = onPaddle;
	}

}