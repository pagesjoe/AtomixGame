package eg.edu.guc.atomix.engine;

import java.io.*;
import java.util.*;

public class Board implements BoardInterface {
	private String name;
	private char[][] board;
	private int boardLength, boardWidth;
	private ArrayList<Atom> atomList;

	public Board() {

		
	}

	public Board(FileReader file) throws Exception {
		makeBoard(file);
	}

	public Board makeBoard(InputStreamReader stream) throws IOException {
		BufferedReader file = new BufferedReader(stream);
		String line = file.readLine();
		name = line;
		line = file.readLine();
		int n = Integer.parseInt(line);
		atomList = new ArrayList<Atom>();
		for (int i = 0; i < n; i++) {
			line = file.readLine();
			atomList.add(new Atom(line.charAt(0)));
			atomList.get(i).setBonds(line.substring(2).split(" "));
		}
		ArrayList<String> temp = new ArrayList<String>();
		line = file.readLine();
		while (line != null) {
			temp.add(line);
			line = file.readLine();
		}
		boardLength = temp.size();
		boardWidth = temp.get(0).length();
		board = new char[boardLength][boardWidth];

		int digit;
		int c = 0;
		for (int i = 0; i < boardLength; i++) {
			for (int j = 0; j < boardWidth; j++) {
				if (temp.get(i).charAt(j) != '#'
						&& temp.get(i).charAt(j) != '.') {
					if (Character.isDigit(temp.get(i).charAt(j))) {
						digit = Integer.parseInt(temp.get(i).charAt(j) + "");
						atomList.get(digit - 1).setI(i);
						atomList.get(digit - 1).setJ(j);
						atomList.get(digit - 1).setPos(c);
						atomList.get(digit - 1).setSymbol(digit);
					} else {
						char symbol = temp.get(i).charAt(j);
						atomList.get(symbol - 56).setI(i);
						atomList.get(symbol - 56).setJ(j);
						atomList.get(symbol - 56).setPos(c);
						atomList.get(symbol - 56).setSymbol(symbol - 55);
					}
				}
				board[i][j] = temp.get(i).charAt(j);
				c++;
			}
		}
		return this;
	}

	


	public boolean move(Object a, char direction) {
		Atom atom = (Atom) a;
		int i = atom.getI();
		int j = atom.getJ();
		switch (direction) {
		case 'U': {
			if (board[i - 1][j] != '.') {
				return false;
			} else {
				board[i][j] = '.';
				while (board[i - 1][j] == '.') {
					i--;
				}
				atom.setI(i);
				atom.setPos(j + (i * boardWidth));
				board[atom.getI()][atom.getJ()] = (atom.getSymbol() + "")
						.charAt(0);
				return true;
			}
		}
		case 'R': {
			if (board[i][j + 1] != '.') {
				return false;
			} else {
				board[i][j] = '.';
				while (board[i][j + 1] == '.') {
					j++;
				}
				atom.setJ(j);
				atom.setPos(j + (i * boardWidth));
				board[atom.getI()][atom.getJ()] = (atom.getSymbol() + "")
						.charAt(0);
				return true;
			}
		}
		case 'L': {
			if (board[i][j - 1] != '.') {
				return false;
			} else {
				board[i][j] = '.';
				while (board[i][j - 1] == '.') {
					j--;
				}
				atom.setJ(j);
				atom.setPos(j + (i * boardWidth));
				board[atom.getI()][atom.getJ()] = (atom.getSymbol() + "")
						.charAt(0);
				return true;
			}
		}
		case 'D': {
			if (board[i + 1][j] != '.') {
				return false;
			} else {
				board[i][j] = '.';
				while (board[i + 1][j] == '.') {
					i++;
				}
				atom.setI(i);
				atom.setPos(j + (i * boardWidth));
				board[atom.getI()][atom.getJ()] = (atom.getSymbol() + "")
						.charAt(0);
				return true;
			}
		}
		default: {
			return false;
		}
		}
	}

	public boolean canMove(Atom atom, char direction) {
		int i = atom.getI();
		int j = atom.getJ();
		switch (direction) {
		case 'U': {
			if (board[i - 1][j] != '.') {
				return false;
			}
			return true;

		}
		case 'R': {
			if (board[i][j + 1] != '.') {
				return false;
			}
			return true;

		}
		case 'L': {
			if (board[i][j - 1] != '.') {
				return false;
			}
			return true;

		}
		case 'D': {
			if (board[i + 1][j] != '.') {
				return false;
			}
			return true;

		}
		default: {
			return false;
		}
		}
	}
	
	
	public boolean cantMove(Atom a){
		if(canMove(a,'U')||canMove(a,'D')||canMove(a,'R')||canMove(a,'L')){
			return false;
		}else{
			return true;
		}
	}

	

	public boolean gameover() {
		Atom current;
		Atom next = null;
		Bond b;
		int x;
		int y;
		// loop in the list of the atoms
		for (int i = 0; i < atomList.size(); i++) {
			current = atomList.get(i);
			x = current.getI();
			y = current.getJ();

			// loop in the bonds of the atom
			for (int j = 0; j < current.getBondList().size(); j++) {
				b = current.getBondList().get(j);

				// switch bond
				if (b.getName().equals("N")) {
					if (!Character.isDigit(board[x - 1][y])) {
						return false;
					} else {
						next = atomList.get(Integer.parseInt(board[x - 1][y]
								+ "") - 1);
					}
				} else {
					if (b.getName().equals("NE")) {
						if (!Character.isDigit(board[x - 1][y + 1])) {
							return false;
						} else {
							next = atomList.get(Integer
									.parseInt(board[x - 1][y + 1] + "") - 1);
						}
					} else {
						if (b.getName().equals("E")) {
							if (!Character.isDigit(board[x][y + 1])) {
								return false;
							} else {
								next = atomList.get(Integer
										.parseInt(board[x][y + 1] + "") - 1);
							}
						} else {
							if (b.getName().equals("SE")) {
								if (!Character.isDigit(board[x + 1][y + 1])) {
									return false;
								} else {
									next = atomList
											.get(Integer
													.parseInt(board[x + 1][y + 1]
															+ "") - 1);
								}
							} else {
								if (b.getName().equals("S")) {
									if (!Character.isDigit(board[x + 1][y])) {
										return false;
									} else {
										next = atomList
												.get(Integer
														.parseInt(board[x + 1][y]
																+ "") - 1);
									}
								} else {
									if (b.getName().equals("SW")) {
										if (!Character
												.isDigit(board[x + 1][y - 1])) {
											return false;
										} else {
											next = atomList
													.get(Integer
															.parseInt(board[x + 1][y - 1]
																	+ "") - 1);

										}
									} else {
										if (b.getName().equals("W")) {
											if (!Character
													.isDigit(board[x][y - 1])) {
												return false;
											} else {
												next = atomList
														.get(Integer
																.parseInt(board[x][y - 1]
																		+ "") - 1);
											}
										} else {
											if (b.getName().equals("NW")) {
												if (!Character
														.isDigit(board[x - 1][y - 1])) {
													return false;
												} else {
													next = atomList
															.get(Integer
																	.parseInt(board[x - 1][y - 1]
																			+ "") - 1);
												}
											}
										}
									}
								}
							}
						}

					}
				}
				if (!next.hasBondMatches(b.getIndex(), b.getType())) {
					return false;
				}
			}
		}
		return true;
	}

	public Collection<Atom> getAtoms() {
		Collection<Atom> temp = atomList;
		return temp;
	}

	public void printBoard() {
		for (int i = 0; i < getBoardLength(); i++) {
			for (int j = 0; j < getBoardWidth(); j++)
				System.out.print(getBoard()[i][j]);
			System.out.println();
		}
	}
	
	
	
	public Board generate(InputStreamReader stream, int moves)throws Exception
	{
		FileReader file = (FileReader)(stream);

		double x;
		String s;
		Atom a;
		while(moves>0){
			do{
				x = Math.random()*atomList.size();
				s = x+"";
				String[] num = s.split("\\.");
				int n = Integer.parseInt(num[0]);
				a = atomList.get(n);
			}while(cantMove(a));
			
			char dir;
			double y;
			do{
				y = Math.random();
				if(y<0.25)
				{
					dir = 'U';
					
				}else{
					if(y<0.5){
						dir = 'R';
					}else{
						if(y<0.75){
							dir = 'L';
						}else{
							dir = 'D';
						}
					}
				}
			}while(!canMove(a, dir));
			move(a,dir);
			
			moves--;
		}
		return this;
	}
	
	public Board makeCopy(){
		
		Board board = new Board();
		board.setName(name);
		board.setBoardLength(boardLength);
		board.setBoardWidth(boardWidth);
		board.setBoard(new char[boardLength][boardWidth]);
		
		board.setBoard(this.copyBoardArray(board));
		
		board.setAtomList(new ArrayList<Atom>());
		for(int i=0;i<this.getAtomList().size();i++){
			board.getAtomList().add(this.getAtomList().get(i).copyAtom());
		}
		return board;
	}
	
	
	public char[][] copyBoardArray(Board board){
		for (int i = 0; i < getBoardLength(); i++) {
			for (int j = 0; j < getBoardWidth(); j++)
				board.getBoard()[i][j]=getBoard()[i][j];
		}
		return board.getBoard();
	}

	public static void main(String[] args) throws Exception {
		FileReader file = new FileReader("D:/GUC/Computer Programming Lab/Atomix/Levels_4384/Levels/level_2.txt");
		Board board = new Board(file);
		board.printBoard();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char[][] getBoard() {
		return board;
	}

	public void setBoard(char[][] board) {
		this.board = board;
	}

	public int getBoardLength() {
		return boardLength;
	}

	public void setBoardLength(int boardLength) {
		this.boardLength = boardLength;
	}

	public int getBoardWidth() {
		return boardWidth;
	}

	public void setBoardWidth(int boardWidth) {
		this.boardWidth = boardWidth;
	}

	public ArrayList<Atom> getAtomList() {
		return atomList;
	}

	public void setAtomList(ArrayList<Atom> atomList) {
		this.atomList = atomList;
	}

}
