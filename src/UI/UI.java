package UI;

import gameState.GameState;
import gameState.Room;
import player.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class UI extends JPanel {
  private GameState game;
  private HotBar hotbar;

  public UI(GameState gameState) {
    this.game = gameState;
    this.hotbar = new HotBar(game.inventory);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    int width = getWidth();
    int height = getHeight();

    drawRoom(g, width, height - 100); // Reserve 100px for hotbar

    hotbar.draw(g, width, height);
  }

  private void drawRoom(Graphics g, int width, int height) {
    Room room = game.getCurrentRoom();

    BufferedImage bg = room.getBackground();
    g.drawImage(bg, 0, 0, width, height, this);
  }
}
