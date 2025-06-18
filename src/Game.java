import java.awt.EventQueue;

import java.util.List;

import javax.swing.JFrame;

import UI.UI;
import gameLogic.*;
import items.*;
import rooms.*;

public class Game {
  public static void main(String[] args) {
    JFrame frame = new JFrame("Escape Game 2000");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Inventory inv = new Inventory();

    Bedroom bedroom = new Bedroom("Bedroom", 0);

    MainRoom mainRoom = new MainRoom("Main room", 1, List.of(bedroom));
    bedroom.addNeighbour(mainRoom);

    GameState game = new GameState(0, List.of(bedroom, mainRoom), inv);

    EventQueue.invokeLater(() -> {
      UI ui = new UI(game);
      frame.setContentPane(ui);

      // Fullscreen
      java.awt.GraphicsEnvironment graphics = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
      java.awt.GraphicsDevice device = graphics.getDefaultScreenDevice();
      device.setFullScreenWindow(frame);

      frame.setVisible(true);
    });
  }
}
