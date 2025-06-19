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

    GameState game = getGameState();

    EventQueue.invokeLater(() -> {
      UI ui = new UI(game);
      frame.setContentPane(ui);

      java.awt.GraphicsEnvironment graphics = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
      java.awt.GraphicsDevice device = graphics.getDefaultScreenDevice();
      device.setFullScreenWindow(frame);

      frame.setVisible(true);
    });
  }

  private static GameState getGameState() {
    Inventory inv = new Inventory();

    Bedroom bedroom = new Bedroom("Bedroom", 0);

    Book bookRoom = new Book("Book Room", 1, List.of(bedroom));
    bedroom.addNeighbour(bookRoom);
    PasswordValidationRoom passwordRoom = new PasswordValidationRoom("Password Room", 2, List.of(bedroom));
    bedroom.addNeighbour(passwordRoom);
    MinesweeperRoom mineRoom = new MinesweeperRoom("Mine Room", 3, List.of(bedroom));
    bedroom.addNeighbour(mineRoom);

    MainRoom mainRoom = new MainRoom("Main Room", 4);
    mainRoom.addNeighbour(bedroom);
    bedroom.addNeighbour(mainRoom);

    BrickBreakRoom brickBreakRoom = new BrickBreakRoom("Brick Break Room", 5, List.of(mainRoom));
    mainRoom.addNeighbour(brickBreakRoom);

    Outside outside = new Outside("Outside", 6, List.of(mainRoom));
    mainRoom.addNeighbour(outside);


    return new GameState(0, List.of(bedroom, mineRoom, bookRoom, passwordRoom, mainRoom, brickBreakRoom, outside), inv);
  }
}
