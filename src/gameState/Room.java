package gameState;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

public class Room {
  public String name;
  public int id;
  private final ArrayList<Room> linkedTo;
  private BufferedImage background;

  public Room(String name, int id) {
    this.name = name;
    this.id = id;
    this.linkedTo = new ArrayList<>();
  }

  public Room(String name, int id, ArrayList<Room> linkedTo) {
    this.id = id;
    this.linkedTo = linkedTo;
  }

  /**
   * Tells if the move from the current room to the `destination` room is valid
   *
   * @param destination the room to which the player wants to move
   * @return true if the `destination` room is linked to the current room, false
   *         otherwise
   */
  public boolean validMove(Room destination) {
    boolean valid = false;

    for (Room r : linkedTo) {
      if (r.equals(destination)) {
        valid = true;
        break;
      }
    }

    return valid;
  }

  public void addNeighbour(Room neighbour) {
    this.linkedTo.addLast(neighbour);
  }

  /**
   * Delete all the rooms that have the same id as `neighbor`
   *
   * @param id the id of the room to remove
   * @throws RoomNotFoundException if no room with the given id is found
   */
  public void removeNeighbour(int id) {
    boolean found = linkedTo.removeIf(room -> (room.id == id));

    if (!found) {
      throw new RoomNotFoundException();
    }
  }

  /**
   * Delete all the rooms that have the same name as `neighbor`
   *
   * @param name the name of the room to remove
   * @throws RoomNotFoundException if no room with the given name is found
   */
  public void removeNeighbour(String name) {
    boolean found = linkedTo.removeIf(room -> (Objects.equals(room.name, name)));

    if (!found) {
      throw new RoomNotFoundException();
    }
  }

  /**
   * Delete all the rooms that are equal to `neighbor`
   *
   * @param neighbour the room to remove
   * @throws RoomNotFoundException if no room corresponding to neighbor is found
   */
  public void removeNeighbour(Room neighbour) {
    boolean found = linkedTo.removeIf(room -> (room.equals(neighbour)));

    if (!found) {
      throw new RoomNotFoundException();
    }
  }

  public void setBackground(BufferedImage background) {
    this.background = background;
  }

  public BufferedImage getBackground() {
    return this.background;
  }

  public void draw(Graphics g, int width, int height) {
    BufferedImage bg = getBackground();
    if (bg != null) {
      g.drawImage(bg, 0, 0, width, height, null);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Room other = (Room) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (id != other.id)
      return false;
    if (linkedTo == null) {
        return other.linkedTo == null;
    } else return linkedTo.equals(other.linkedTo);
  }

  public static class RoomNotFoundException extends RuntimeException {
  }

}
