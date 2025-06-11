package gameState;

import player.*;

public class GameState {
  public Inventory inventory;
  public int roomNumber;

  public GameState(Inventory inv, int roomNumber) {
    this.inventory = inv;
    this.roomNumber = roomNumber;
  }

  public void addItem(Item i) {
    inventory.addItem(i);
  }

}
