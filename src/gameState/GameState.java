package gameState;

import java.util.List;

import player.*;

/**
 * The GameState class represents the current state of the game.
 * It keeps track of the current room, the player's inventory, and the game status.
 * It also manages the countdown timer for the game.
 */
public class GameState {
  public GameStatus status = GameStatus.PLAYING;

  private final List<Room> rooms;
  private final int currentRoomID;

  public Inventory inventory;

  public Countdown countdown;


  public GameState(int currentRoomID, List<Room> rooms, Inventory inv) {
    this.rooms = rooms;
    this.currentRoomID = currentRoomID;

    this.inventory = inv;

    this.countdown = new Countdown(0);
    countdown.start();
  }

  public GameStatus update() {
    countdown.update();
    if (countdown.getSecondsLeft() <= 0) {
      status = GameStatus.LOST;
    }

    return status;
  }

  public Room getCurrentRoom() {
    return rooms.get(currentRoomID);
  }

}
