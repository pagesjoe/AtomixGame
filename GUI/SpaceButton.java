package eg.edu.guc.atomix.gui;

import javax.swing.*;
import java.awt.Color;

public class SpaceButton extends JButton {

	public SpaceButton() {
		super();
		setBackground(Color.WHITE);
		setBorder(null);
		setEnabled(false);
		validate();
	}

	public SpaceButton(ImageIcon img) {
		super(img);
		setBackground(Color.WHITE);
		setBorder(null);
		setEnabled(false);
		validate();
	}
}
