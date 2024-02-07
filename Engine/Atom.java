package eg.edu.guc.atomix.engine;

import java.awt.Color;
import java.util.LinkedList;
import javax.swing.*;

public class Atom {

	private int i;
	private int j;
	int pos;
	private int symbol;
	private char name;
	private String[] bonds;
	private LinkedList<Bond> bondList;
	

	public Atom() {

	}

	public Atom(char name) {

		this.name = name;
		bondList = new LinkedList<Bond>();

	}

	public boolean hasBondMatches(int index, String t) {
		for (int k = 0; k < bondList.size(); k++) {
			if ((index - bondList.get(k).getIndex() == 4 || index
					- bondList.get(k).getIndex() == -4)
					&& bondList.get(k).getType().equals(t)) {
				return true;
			}
		}
		return false;
	}

	public void setBonds(String[] bonds) {
		this.bonds = bonds;
		for (int k = 0; k < bonds.length; k++) {
			if (bonds[k].equals("1")) {
				bondList.add(new Bond(k, "s"));
			} else {
				if (bonds[k].equals("2")) {
					bondList.add(new Bond(k, "d"));
				}
			}
		}
	}
	
	public Atom copyAtom(){
		Atom atom = new Atom(name);
		atom.setBonds(bonds);
		atom.setI(i);
		atom.setJ(j);
		atom.setPos(pos);
		atom.setSymbol(symbol);
		return atom;
	}

	public int getSymbol() {
		return symbol;
	}

	public void setSymbol(int symbol) {
		this.symbol = symbol;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String[] getBonds() {
		return bonds;
	}

	public char getName() {
		return name;
	}

	public void setName(char name) {
		this.name = name;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public LinkedList<Bond> getBondList() {
		return bondList;
	}

	public void setBondList(LinkedList<Bond> bondList) {
		this.bondList = bondList;
	}

	/*
	 * public JButton getUp() { return up; }
	 * 
	 * public void setUp(JButton up) { this.up = up; }
	 * 
	 * public JButton getDown() { return down; }
	 * 
	 * public void setDown(JButton down) { this.down = down; }
	 * 
	 * public JButton getRight() { return right; }
	 * 
	 * public void setRight(JButton right) { this.right = right; }
	 * 
	 * public JButton getLeft() { return left; }
	 * 
	 * public void setLeft(JButton left) { this.left = left; }
	 */

}
