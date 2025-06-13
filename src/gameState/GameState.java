package gameState;

import java.util.ArrayList;
import java.util.List;

import player.*;

public class GameState {
  private List<Room> rooms;
  public int seconde, minute;
  public int currentRoomID;
  public Inventory inventory;

  public GameState(int currentRoomID, List<Room> rooms, Inventory inv) {
    this.rooms = rooms;
    this.inventory = inv;
    this.currentRoomID = currentRoomID;
  }

  public GameState() {
    this.rooms = new ArrayList<>();
    this.inventory = new Inventory();
    this.currentRoomID = 0;
  }

  public Room getCurrentRoom() {
    return rooms.get(currentRoomID);
  }

  public void moveToRoom(int index) {
    if (index >= 0 && index < rooms.size()) {
      currentRoomID = index;
    }
  }

}
