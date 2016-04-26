package Game;

public class Borders {

	final private int top, bottom, left, right;

	Borders(int t, int b, int l, int r) {
		top = t;
		bottom = b;
		left = l;
		right = r;
	}

	public int getTop() {
		return top;
	}

	public int getBottom() {
		return bottom;
	}

	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}
}
