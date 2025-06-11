import java.awt.EventQueue;
import UI.*;

import player.Item;
import player.Inventory;

public class Game {
  public static void main(String[] args) {
    Item i1 = new Item("Carte");
    Inventory inv = new Inventory();
    inv.addItem(i1);

    EventQueue.invokeLater(() -> {
      new UI();
    });
  }
}
