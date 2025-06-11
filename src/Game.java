import java.awt.EventQueue;

import UI.UI;
import gameState.GameState;
import player.Inventory;
import items.*;

public class Game {
  public static void main(String[] args) {
    Cle cle = new Cle();
    Inventory inv = new Inventory();

    GameState game = new GameState(inv, 0);

    game.inventory.addItem(cle);

    EventQueue.invokeLater(() -> {
      new UI(game);
    });
  }
}
