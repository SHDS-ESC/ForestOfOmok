package model;

public class OmokBoard extends Board {
	int board[][];
	
	public OmokBoard(int row, int col) {
		this.row = row;
		this.col = col;
		board = new int[row][col];
	}
	
	@Override
	public void print() {
		if (board == null) return;
		
		int row=board.length;
		int col=board[0].length;
		
		for (int r=0; r<row; ++r) {
			for (int c=0; c<col; ++c) {
				System.out.print(board[r][c] + " ");
			}
			System.out.println();
		}
		
	};

}
