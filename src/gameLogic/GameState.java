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
  private static final int TIMER_DURATION = 300;

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

  public GameStatus update() {
    countdown.update();

    if (hotbarText != null && System.currentTimeMillis() > hotbarTextExpireAt) {
      hotbarText = null;
    }

    if (countdown.getSecondsLeft() <= 0) {
      status = GameStatus.LOST;
    }

    return status;
  }

  public Room getCurrentRoom() {
    return rooms.get(currentRoomID);
  }

  public Room getRoom(int roomID) {
    if (roomID >= 0 && roomID < rooms.size()) {
      return rooms.get(roomID);
    }
    throw new IndexOutOfBoundsException("Invalid room ID: " + roomID);
  }

  public Room getRoom(String name) {
    return rooms.stream()
        .filter(room -> room.name.equals(name))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Room not found: " + name));
  }

  public void setCurrentRoom(int roomID) {
    if (roomID >= 0 && roomID < rooms.size()) {
      this.currentRoomID = roomID;
    }
  }

  public void executeAction(List<Action> actions) {
    if (actions != null) {
      for (Action action : actions) {
        executeAction(action);
      }
    }
  }

  // private method that is executed for each individual action
  private void executeAction(Action action) {
    switch (action) {
      case Action.ChangeRoom changeRoom -> {
        Room current = getCurrentRoom();
        System.out.println(current.name);

        Room destination = getRoom(changeRoom.roomId);
        System.out.println("here");

        if (current.validMove(destination)) {
          setCurrentRoom(destination.id);
        } else {
          System.err.println("Invalid move to room: " + changeRoom.roomId);
        }
      }

      case Action.CollectItem addItem -> {
        if (!inventory.isFull()) {
          inventory.addItem(addItem.item);
        } else {
          System.err.println("Inventory full");
        }

      }

      case Action.ShowHotbarText showText -> {
        this.hotbarText = showText.text;
        this.hotbarTextExpireAt = System.currentTimeMillis() + 5000; // 5 secondes
      }

      case null, default -> {
      }
    }
  }

  public String getHotbarText() {
    return hotbarText;
  }
  public void setHotbarText(String text) {
    this.hotbarText = text;
}
}
