package UI;

import java.awt.*;
import javax.swing.*;
import player.*;

class HotBar extends JPanel {
  private final Inventory inv;

  public HotBar(Inventory i) {
    this.inv = i;
    setOpaque(false);
  }

  public void draw(Graphics g, int x, int y, int width, int height) {
    /*---[Partie inventaire]---*/
    // Fond blanc
    g.setColor(Color.WHITE);
    g.fillRect(x, y, width, height);

    int totalSlots = Inventory.getInventorySize();

    // Pour des cases carrées, hauteur = largeur
    int slotSize = (int) (height * 0.8);

    int padding = (int) (width * 0.01);

    int totalWidth = totalSlots * slotSize + (totalSlots - 1) * padding;

    int startX = (int) (totalWidth * 0.02);
    int slotY = y + (height - slotSize) / 2;

    for (int i = 0; i < totalSlots; i++) {
      // Dessine les bordures
      int slotX = startX + i * (slotSize + padding);
      g.setColor(Color.GRAY);
        g.drawRect(slotX, slotY, slotSize, slotSize);

      // Dessine le sprite des items présents
      Item item = inv.get(i);
      if (item != null && item.getSprite() != null) {
        g.drawImage(item.getSprite(), slotX + 4, slotY + 4, slotSize - 8, slotSize - 8, null);
      }
    }
  }
}
