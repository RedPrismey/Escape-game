package gameLogic;

import items.Item;

/**
 * The Action class represents an action that can be performed in the game.
 */
public abstract class Action {
  public static class ChangeRoom extends Action {
    public final String roomName;

    public ChangeRoom(String roomName) {
      this.roomName = roomName;
    }
  }

  public static class CollectItem extends Action {
    public final Item item;

    public CollectItem(Item item) {
      this.item = item;
    }
  }

  public static class ShowHotbarText extends Action {
    public final String text;

    public ShowHotbarText(String text) {
      this.text = text;
    }
  }
}
