package game;

public class Intersection {
	private static final Intersection OUT_OF_BOUNDS = new Intersection();

	int player = Board.OUT_OF_BOUNDS;

	int x;
	int y;

	Intersection upper;
	Intersection lower;
	Intersection left;
	Intersection right;

	private Intersection() {
	}

	public Intersection(int x, int y, int player) {
		this.x = x;
		this.y = y;
		this.player = player;
	}

	public void setLiberties(Board board) {
		upper = y == 0 ? OUT_OF_BOUNDS : board.intersections[x][y - 1];
		lower = y == board.getBoardSize() - 1 ? OUT_OF_BOUNDS : board.intersections[x][y + 1];
		left = x == 0 ? OUT_OF_BOUNDS : board.intersections[x - 1][y];
		right = x == board.getBoardSize() - 1 ? OUT_OF_BOUNDS : board.intersections[x + 1][y];
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	public boolean isAdjacent(int x2, int y2) {
		return (y == y2 && (x + 1 == x2 || x - 1 == x2)) || (x == x2 && (y + 1 == y2 || y - 1 == y2));
	}

	public int countLiberties() {
		return upper.liberty() + lower.liberty() + left.liberty() + right.liberty();
	}

	public int liberty() {
		return player == Board.UNPLAYED ? 1 : 0;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}