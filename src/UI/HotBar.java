package UI;

import java.awt.*;
import javax.swing.*;
import player.*;

class HotBar extends JPanel {
  private Inventory inv;

  public HotBar(Inventory i) {
    this.inv = i;
    setOpaque(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    int panelWidth = getWidth();
    int panelHeight = getHeight();

    int totalSlots = Inventory.getInventorySize();

    int slotWidth = (int) (panelWidth * 0.05); // 5% largeur par slot
    int slotHeight = (int) (panelHeight * 0.8); // 80% hauteur du panel
    int padding = (int) (panelWidth * 0.01); // 1% espace entre slots

    int totalWidth = totalSlots * slotWidth + (totalSlots - 1) * padding;
    int startX = (int) (totalWidth * 0.01);
    int y = (panelHeight - slotHeight) / 2;

    for (int i = 0; i < totalSlots; i++) {
      int x = startX + i * (slotWidth + padding);

      g.setColor(Color.GRAY);
      g.drawRect(x, y, slotWidth, slotHeight);

      Item item = inv.get(i);
      if (item != null && item.getSprite() != null) {
        g.drawImage(item.getSprite(), x + 4, y + 4, slotWidth - 8, slotHeight - 8, null);
      }
    }
  }
}
