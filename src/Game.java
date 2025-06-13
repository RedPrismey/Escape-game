import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import UI.RoomPanel;
import UI.UI;
import gameState.*;
import player.Inventory;
import items.*;

public class Game {
  public static void main(String[] args) {
    JFrame frame = new JFrame("Escape Game");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);

    Cle cle = new Cle();
    Inventory inv = new Inventory();

    Room r1 = new Room("Test", 0);
    try {
      BufferedImage s = ImageIO.read(new File("./UI/Bridget_Guilty_Gear.png"));
      r1.setBackground(s);
    } catch (IOException e) {
      e.printStackTrace();
    }

    RoomPanel roomPanel = new RoomPanel();
    GameState game = new GameState(0, List.of(r1), inv);
    roomPanel.setRoom(game.getCurrentRoom());

    game.inventory.addItem(cle);
    game.inventory.addItem(cle);
    game.inventory.addItem(cle);

    game.inventory.removeItem(1);

    EventQueue.invokeLater(() -> new UI(game));
  }
}
