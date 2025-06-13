package UI;

import gameState.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class RoomPanel extends JPanel {
  private BufferedImage background;

  public void setRoom(Room room) {
    background = room.getBackground();
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (background != null) {
      g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
  }
}
