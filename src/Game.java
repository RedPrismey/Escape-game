import UI.UI;
import gameLogic.*;
import items.*;
import java.awt.EventQueue;
import java.util.List;
import javax.swing.JFrame;
import rooms.*;

public class Game {
  public static void main(String[] args) {
    JFrame frame = new JFrame("Escape Game 2000");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Inventory inv = new Inventory();

    Bedroom bedroom = new Bedroom("Bedroom", 0);
    MinesweeperRoom mineRoom = new MinesweeperRoom("Mine Room", 1, List.of(bedroom));
    bedroom.addNeighbour(mineRoom);
    Book bookRoom = new Book("Book Room", 2, List.of(bedroom));
    bedroom.addNeighbour(bookRoom);

    MainRoom mainRoom = new MainRoom("Main Room", 3, List.of(bedroom));
    bedroom.addNeighbour(mainRoom);

    GameState game = new GameState(0, List.of(bedroom, mineRoom, bookRoom, mainRoom), inv);

    EventQueue.invokeLater(() -> {
      UI ui = new UI(game);
      frame.setContentPane(ui);

      java.awt.GraphicsEnvironment graphics = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
      java.awt.GraphicsDevice device = graphics.getDefaultScreenDevice();
      device.setFullScreenWindow(frame);

      frame.setVisible(true);
    });
  }
}
