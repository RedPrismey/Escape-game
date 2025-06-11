import java.awt.EventQueue;
import UI.UI;

public class Game {
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			new UI();
		});
	}
}
