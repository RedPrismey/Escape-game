import javax.swing.JFrame;

public class UI extends JFrame {
	private static final String TITLE = "Escape Game 2000";

	public UI() {
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(true);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setTitle(TITLE);

		this.setVisible(true);
	}

}
