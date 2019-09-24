package src;

import javax.swing.JFrame;

public class DisplayWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Display display;
	
	public DisplayWindow(int width, int height, String title) {
		display = new Display(width,height);
		add(display);
		display.Init();
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(3);
		setTitle(title);
		setVisible(true);
	}
	public void Update(){
		display.UpdateGraphics();
	}

}
