package eg.edu.guc.atomix.gui;

import eg.edu.guc.atomix.engine.*;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.ArrayList;

public class AtomixGui extends JFrame implements ActionListener {

	private JPanel gamePanel;
	private JPanel extraPanel;
	private JPanel shape;
	
	private Board board;
	private ArrayList<Board> prevBoard = null;
	private int length;
	private int width;
	Atom prevAtom;
	JButton prevAtomBtn;
	
	private int levCounter=1;

	private JButton nextLevel;
	private JButton prevLevel;
	private JButton up;
	private JButton down;
	private JButton right;
	private JButton left;
	private JButton undo;
	private JButton redo;
	
	private int redoCount;
	private boolean redoFlag;
	
	private JPanel panel1;
	private String type="";
	
	Timer timer;
	int startTime;
	int elapsedTime;
	int step;
	
	
	public AtomixGui() throws Exception {
		
		/*timer=new Timer(1000,new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int endTime=(int)System.currentTimeMillis()/1000;
				elapsedTime = endTime-startTime;
				timeLabel.setText("  "+read.time(elapsedTime));
			}
		});*/
		
		super();
		setSize(250,150);
		this.setLayout(null);
		this.validate();
		WindowDestroyer exitListener = new WindowDestroyer();
		
		up = new JButton(new ImageIcon("D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/Untitled.png"));
		up.setBackground(Color.WHITE);
		up.setBorder(null);
		up.addActionListener(this);
		up.setActionCommand("U");

		down = new JButton(new ImageIcon("D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/down.png"));
		down.setBackground(Color.WHITE);
		down.setBorder(null);
		down.addActionListener(this);
		down.setActionCommand("D");

		left = new JButton(new ImageIcon("D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/left.png"));
		left.setBackground(Color.WHITE);
		left.setBorder(null);
		left.addActionListener(this);
		left.setActionCommand("L");

		right = new JButton(new ImageIcon("D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/right.png"));
		right.setBackground(Color.WHITE);
		right.setBorder(null);
		right.addActionListener(this);
		right.setActionCommand("R");

		nextLevel = new JButton("next Level");
		nextLevel.addActionListener(this);
		nextLevel.setActionCommand("next");
		
		prevLevel = new JButton("Prev Level");
		prevLevel.addActionListener(this);
		prevLevel.setActionCommand("previous");
		
		undo= new JButton("Undo");
		undo.addActionListener(this);
		undo.setActionCommand("undo");
		
		redo= new JButton("Redo");
		redo.addActionListener(this);
		redo.setActionCommand("redo");
				
		
		extraPanel = new JPanel();
		extraPanel.setVisible(true);
		extraPanel.setBackground(Color.GRAY);
		extraPanel.setLayout(new GridLayout(4,0));
		extraPanel.add(undo);
		extraPanel.add(redo);
		extraPanel.add(nextLevel);
		extraPanel.add(prevLevel);
		
		panel1 = new JPanel();
		panel1.setVisible(true);
		panel1.setBackground(Color.GRAY);
		panel1.setSize(250, 150);
		JButton classic = new JButton("classic");
		classic.setActionCommand("classic");
		JButton random = new JButton("random");
		random.setActionCommand("random");
		classic.addActionListener(this);
		random.addActionListener(this);
		panel1.add(classic);
		panel1.add(random);
		getContentPane().add(panel1);
		

		addWindowListener(exitListener);

	}
	
	
	public void setLevel()throws Exception{
		
		this.setTitle("Level "+levCounter);
		prevAtom=null;
		
		prevBoard = new ArrayList<Board>();
		step=0;
		
		Board board;
		FileReader file;
		
		if(type.equals("random")){
			file = new FileReader("D:/GUC/Computer Programming Lab/Atomix/SolvedLevels_4494/SolvedLevels/SolvedLevel_"+levCounter+".txt");
			board = new Board(file);
			board = board.generate(file, 20);
		}else{
			file = new FileReader("D:/GUC/Computer Programming Lab/Atomix/Levels/Levels/level_"+levCounter+".txt");
			board = new Board(file);
		}
		
		this.board = board;
		
		prevBoard.add(step, board.makeCopy());
		step++;
		
		
		//Setting all sizes
		setGamePanel();
		this.setSize(gamePanel.getWidth()+220, gamePanel.getHeight()+70);
		
		extraPanel.setBounds(gamePanel.getWidth()+30, 0, 120, 200);
		
		shape = new JPanel();
		JLabel label = new JLabel(new ImageIcon("D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/"+levCounter+".png"));
		shape.setBounds(gamePanel.getWidth()+5, extraPanel.getHeight(), 200, 200);
		shape.add(label);
		shape.setEnabled(false);
		
		//Draw the game
		getContentPane().add(drawBoard());
		
		getContentPane().add(extraPanel);
		getContentPane().add(shape);

	}
	
	public void setGamePanel(){

		gamePanel = new JPanel();
		length = board.getBoardLength();
		width = board.getBoardWidth();
		gamePanel.setBounds(0, 0, 39 * width, 34 * length);
		gamePanel.setLayout(new GridLayout(length, width));
	}
	
	
	public void startTimer()
	{
		startTime=(int)System.currentTimeMillis()/1000;
		timer.start();
	}
	public void stopTimer()
	{
		timer.stop();
	}

	
	
	public JPanel drawBoard() {

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < width; j++) {
				// if it's a wall
				if (board.getBoard()[i][j] == '#') {
					JButton wall = new JButton();
					wall.setBackground(Color.DARK_GRAY);
					// wall.setBorder(null);
					wall.setEnabled(false);
					gamePanel.add(wall);
				} else {
					// if it's space
					if (board.getBoard()[i][j] == '.') {
						gamePanel.add(new SpaceButton());
					} else {
						// if it is an atom
						ImageIcon img = null;
						JButton btn = null;
						int digit = 0;
						if (Character.isDigit(board.getBoard()[i][j])) {
							digit = Integer.parseInt((board.getBoard()[i][j])
									+ "");

						} else {
							digit = (board.getBoard()[i][j]) - 55;
						}

						Atom atom = board.getAtomList().get(digit - 1);
						char atomName = board.getAtomList().get(digit - 1)
								.getName();

						switch (atomName) {
						case 'H': {
							switch (atom.getBondList().get(0).getIndex()) {
							case 0:
								img = new ImageIcon(
										"D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/h-u.png");
								break;
							case 1:
								img = new ImageIcon(
										"D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/h-ne.png");
								break;
							case 2:
								img = new ImageIcon(
										"D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/h-r.png");
								break;
							case 3:
								img = new ImageIcon(
										"D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/h-se.png");
								break;
							case 4:
								img = new ImageIcon(
										"D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/h-d.png");
								break;
							case 5:
								img = new ImageIcon(
										"D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/h-sw.png");
								break;
							case 6:
								img = new ImageIcon(
										"D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/h-l.png");
								break;
							case 7:
								img = new ImageIcon(
										"D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/h-nw.png");
								break;
							}
							break;
						}

						case 'O': {
							if (atom.getBondList().get(0).getType().equals("d")) {
								switch (atom.getBondList().get(0).getIndex()) {
								case 0:
									img = new ImageIcon(
											"D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/o(=u).png");
									break;
								case 4:
									img = new ImageIcon(
											"D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/o(=d).png");
									break;
								case 6:
									img = new ImageIcon(
											"D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/=o.png");
									break;
								}
							} else {
								img = new ImageIcon(
										"D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/o.png");
							}
							break;
						}

						case 'C': {

							if (atom.getBonds()[0].equals("2")) {
								img = new ImageIcon(
										"D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/c(=u)r-l.png");
							} else {
								if (atom.getBonds()[2].equals("2")) {
									if (atom.getBondList().get(0).getType()
											.equals("s")) {
										img = new ImageIcon(
												"D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/c=u-l.png");
									} else {
										img = new ImageIcon(
												"D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/kc=.png");
									}
								} else {
									if (atom.getBonds()[4].equals("2")) {
										img = new ImageIcon(
												"D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/c(=d)r-l.png");
									} else {
										if (atom.getBonds()[6].equals("2")) {
											img = new ImageIcon(
													"D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/=ck.png");
										} else {
											img = new ImageIcon(
													"D:/GUC/Computer Programming Lab/Joe2/Atomix/Images/c-reg.png");
										}
									}
								}

							}
							break;
						}

						}
						btn = new JButton(img);
						btn.setName((digit) + "");
						btn.setBackground(Color.WHITE);
						btn.addActionListener(this);
						btn.setActionCommand("atom");
						btn.setBorder(null);
						gamePanel.add(btn);

					}
				}
			}
		}
		return gamePanel;
	}

	
	
	public void playSound() throws Exception {
		URL path = new URL(
				"C:/Users/Public/Music/Sample Music/Maid with the Flaxen Hair.mp3");
		AudioClip song = Applet.newAudioClip(path);
		song.loop();
	}

	
	
	public void actionPerformed(ActionEvent e) {

		Atom prsAtom = null;

		// removing previous atom directions
		if (prevAtom != null) {
			int pos = prevAtom.getPos();

			if (board.canMove(prevAtom, 'U')) {
				gamePanel.remove(pos - board.getBoardWidth());
				gamePanel.add(new SpaceButton(), pos - board.getBoardWidth());
			}
			if (board.canMove(prevAtom, 'D')) {
				gamePanel.remove(pos + board.getBoardWidth());
				gamePanel.add(new SpaceButton(), pos + board.getBoardWidth());
			}
			if (board.canMove(prevAtom, 'R')) {
				gamePanel.remove(pos + 1);
				gamePanel.add(new SpaceButton(), pos + 1);
			}
			if (board.canMove(prevAtom, 'L')) {
				gamePanel.remove(pos - 1);
				gamePanel.add(new SpaceButton(), pos - 1);
			}
		}

		if(e.getActionCommand().equals("classic")){
			type="classic";
			getContentPane().remove(panel1);
			try{setLevel();
			}catch(Exception eo){
				System.out.println("That post will never be shown isA cuz my code has no exceptions :p");
			}return;

		}else{
			if(e.getActionCommand().equals("random")){
				type="random";
				getContentPane().remove(panel1);
				try{setLevel();
				}catch(Exception eo){
					//System.out.println("LOL");
				}
				return;

			}else{
				if (e.getActionCommand().equals("atom")) {
					// pressed button to get the name
					JButton prs = (JButton) e.getSource();
					// getting the name of button to get the atom
					int digit = Integer.parseInt(prs.getName());

					prsAtom = board.getAtomList().get(digit - 1);
					prevAtom = prsAtom;
					prevAtomBtn = prs;

				}
				else
				{
					if(e.getActionCommand().equals("undo"))
					{
						if(step==1){
							return;
						}
						else
						{
							undo();
							return;
						}
					}
					else
					{
						if(e.getActionCommand().equals("redo"))
						{
							if(redoCount>0&&redoFlag)
							{
								redo();
								return;
							}
							else
							{
								return;
							}
						}else{
							if(e.getActionCommand().equals("next")){
								if(levCounter<10)
								{
									levCounter++;
									getContentPane().remove(gamePanel);
									getContentPane().remove(shape);
									try{
										setLevel();
									}catch(Exception exc){
										System.out.println("LOL");
									}
								}return;
							}
							else{
								if(e.getActionCommand().equals("previous")){
									if(levCounter>1)
									{
										levCounter--;
										getContentPane().remove(gamePanel);
										getContentPane().remove(shape);
										try{
											setLevel();
										}catch(Exception ex){
											System.out.println("LOL");
										}
									}return;
								}else { // if action source is a direction
									
									// the prsAtom is the prevAtom
									prsAtom = prevAtom;

									// Moving the atom
									// preserve the position of the atom before it moves
									gamePanel.remove(prevAtom.getPos());
									gamePanel.add(new SpaceButton(), prevAtom.getPos());

									board.move(prevAtom, e.getActionCommand().charAt(0));

									gamePanel.remove(prevAtom.getPos());
									gamePanel.add(prevAtomBtn, prevAtom.getPos());
									
									prevBoard.add(step, board.makeCopy());
									step++;
									redoFlag = false;
									redoCount=0;
								}
							}
						
								
						}
					}
				}

			}
		}
		// if action source is an atom
		
		// getting the position of the atom
		int pos = prsAtom.getPos();

		// adding the directions
		if (board.canMove(prsAtom, 'U')) {
			gamePanel.remove(pos - board.getBoardWidth());
			gamePanel.add(up, pos - board.getBoardWidth());
		}
		if (board.canMove(prsAtom, 'D')) {

			gamePanel.remove(pos + board.getBoardWidth());
			gamePanel.add(down, pos + board.getBoardWidth());
		}
		if (board.canMove(prsAtom, 'R')) {
			gamePanel.remove(pos + 1);
			gamePanel.add(right, pos + 1);
		}
		if (board.canMove(prsAtom, 'L')) {
			gamePanel.remove(pos - 1);
			gamePanel.add(left, pos - 1);
		}

		validate();
		repaint();
		
		
		if (board.gameover()) {

			JFrame gameover = new JFrame();
			gameover.setLayout(new BorderLayout());
			JLabel label = new JLabel("Congratulations");
			gameover.add(label, BorderLayout.CENTER);
			gameover.setSize(200, 100);
			gameover.setVisible(true);

		}
	}
	
	
	
	public void undo()
	{
		this.getContentPane().remove(gamePanel);
		validate();
		repaint();
		
		step = step-2;
		board = (prevBoard.get(step)).makeCopy();
		step= step+1;
		
		setGamePanel();
		this.getContentPane().add(drawBoard());
		validate();
		repaint();
		
		redoCount++;
		redoFlag=true;
	}
	public void redo()
	{
		this.getContentPane().remove(gamePanel);
		
		board= (prevBoard.get(step)).makeCopy();
		step++;
		
		setGamePanel();
		this.getContentPane().add(drawBoard());
		validate();
		repaint();
		
		redoCount--;
	}
	
	

	public static void main(String[] args) throws Exception {
		/*
		 * AudioInputStream audioStream = AudioSystem.getAudioInputStream(new
		 * File
		 * ("C:/Users/Public/Music/Sample Music/Maid with the Flaxen Hair.mp3"
		 * )); AudioFormat audioFormat = audioStream.getFormat();
		 */
		/*FileReader file2 = new FileReader("D:/GUC/Computer Programming Lab/Atomix/Levels_4384/Levels/level_2.txt");
		Board board2 = new Board(file2);
		
		FileReader file = new FileReader("D:/GUC/Computer Programming Lab/Atomix/SolvedLevels_4494/SolvedLevels/SolvedLevel_2.txt");
		Board board = new Board(file);
		//board = board.generate(file, 10);*/
		AtomixGui run = new AtomixGui();
		run.setVisible(true);

	}
}
