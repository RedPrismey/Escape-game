import java.awt.EventQueue;

public class Game {
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			new UI();
		});
	}
}
