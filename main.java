import java.awt.Dimension;

import javax.swing.JFrame;


public class main {

	public static void main(String[] args) {
		Game f = new Game(600,500);
		f.setPreferredSize(new Dimension(600,500));
		f.setSize(600,500);
		f.setVisible(true);	
	}
}
