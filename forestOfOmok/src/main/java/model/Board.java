package model;

public abstract class Board {
	Object board[][];
	int row, col;
	
	public void print() {
		if (board == null) return;
		
		for (int r=0; r<row; ++r) {
			for (int c=0; c<col; ++c) {
				System.out.print(board[r][c] + " ");
			}
			System.out.println();
		}
		
	};

}
