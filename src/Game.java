import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import UI.UI;
import gameState.*;
import player.Inventory;
import items.*;

public class Game {
  public static void main(String[] args) {
    JFrame frame = new JFrame("Escape Game 2000");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Cle cle = new Cle();
    Inventory inv = new Inventory();

    Room r1 = new Room("Test", 0);
    try {
      BufferedImage s = ImageIO.read(new File("./UI/Bridget_Guilty_Gear.png"));
      r1.setBackground(s);
    } catch (IOException e) {
      System.err.println("Erreur lors du chargement de l'image de fond de la room : " + e.getMessage());
    }

    GameState game = new GameState(0, List.of(r1), inv);

    game.inventory.addItem(cle);
    game.inventory.addItem(cle);
    game.inventory.addItem(cle);

    game.inventory.removeItem(1);

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
