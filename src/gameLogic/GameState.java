package gameLogic;

import items.Inventory;
import java.util.List;

/**
 * The GameState class represents the current state of the game.
 * It keeps track of the current room, the player's inventory, and the game
 * status.
 * It also manages the countdown timer for the game.
 */
public class GameState {
  private static final int TIMER_DURATION = 1800;

  public GameStatus status = GameStatus.PLAYING;

  private final List<Room> rooms;
  private int currentRoomID;

  public Inventory inventory;

  public Countdown countdown;

  private String hotbarText = null;
  private long hotbarTextExpireAt = 0;

  public GameState(int currentRoomID, List<Room> rooms, Inventory inv) {
    this.rooms = rooms;

    // checks if none of the rooms are null or have the same ID
    if (rooms.isEmpty() || rooms.stream().anyMatch(room -> room == null || room.id < 0)) {
      throw new IllegalArgumentException("Invalid room list provided.");
    }
    this.currentRoomID = currentRoomID;

    this.inventory = inv;

    this.countdown = new Countdown(TIMER_DURATION);
    countdown.start();
  }

  public void update() {
    countdown.update();

    if (hotbarText != null && System.currentTimeMillis() > hotbarTextExpireAt) {
      hotbarText = null;
    }

    if (countdown.getSecondsLeft() <= 0) {
      status = GameStatus.LOST;
    }
  }

  public Room getCurrentRoom() {
    // Recherche la pièce dont l'id correspond à currentRoomID
    for (Room room : rooms) {
      if (room != null && room.id == currentRoomID) {
        return room;
      }
    }
    throw new IllegalStateException("Current room ID does not match any room.");
  }

  public Room getRoom(int roomID) {
    // Recherche la pièce dont l'id correspond à roomID
    for (Room room : rooms) {
      if (room != null && room.id == roomID) {
        return room;
      }
    }
    throw new IllegalArgumentException("Invalid room ID: " + roomID);
  }

  public Room getRoom(String name) {
    for (Room r : rooms) {
      if (r != null && r.name.equals(name)) {
        return r;
      }
    }

    throw new IllegalArgumentException("Room " + name + " not found");
  }

  public void setCurrentRoom(int roomID) {
    // Vérifie que la pièce existe bien dans la liste rooms
    for (Room room : rooms) {
      if (room != null && room.id == roomID) {
        this.currentRoomID = roomID;
        return;
      }
    }
    throw new IllegalArgumentException("Room ID not found in rooms list: " + roomID);
  }

  public void executeAction(List<Action> actions) {
    if (actions != null) {
      for (Action action : actions) {
        executeAction(action);
      }
    }
  }

  private void executeAction(Action action) {
    switch (action) {
      case Action.ChangeRoom room -> {
        Room current = getCurrentRoom();
        Room destination = getRoom(room.roomName);

        if (current.validMove(destination)) {
          setCurrentRoom(destination.id);
        } else {
          System.err.println("Invalid move to room: " + room.roomName);
        }
      }

      case Action.CollectItem item -> {
        if (!inventory.isFull()) {
          inventory.addItem(item.item);
        } else {
          System.err.println("Inventory full");
        }

      }

      case Action.ShowHotbarText text -> {
        this.hotbarText = text.text;
        this.hotbarTextExpireAt = System.currentTimeMillis() + 5000;
      }

      case null -> {
      }
      default -> {
        getCurrentRoom().handleAction(action);
      }
    }
  }

  public String getHotbarText() {
    return hotbarText;
  }
}
