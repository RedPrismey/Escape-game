package Player;

import java.util.Arrays;

public class Inventory {
  private final static int INVENTORY_SIZE = 6;
  private Item[] items = new Item[INVENTORY_SIZE];
  private int numberOfItems = 0;
  private boolean full = false;
  private boolean[] itemPresent = new boolean[INVENTORY_SIZE]; // permet de savoir où il y a des items
                                                               // dans l'inventaire sans le parcourir à
                                                               // chaque fois

  public Inventory() {
  }

  /**
   * Add the Item `item` to the Inventory
   *
   * @param item
   * @throws InventoryFullException
   */
  public void addItem(Item item) {
    if (full) {
      throw new InventoryFullException();
    }

    for (int i = 0; i < INVENTORY_SIZE; i++) {
      if (!itemPresent[i]) {
        items[i] = item;
        itemPresent[i] = true;
        numberOfItems++;

        if (numberOfItems == INVENTORY_SIZE) {
          full = true;
        }

        break;
      }

    }
  }

  /**
   * Remove the first item with the name matching `itemName`
   * 
   * @param name
   * @throws ItemNotFoundException
   */
  public void removeItem(String itemName) {
    boolean found = false;

    for (int i = 0; i < INVENTORY_SIZE; i++) {
      if (items[i] != null && items[i].getName() == itemName) {
        found = true;

        items[i] = null;
        itemPresent[i] = false;
        numberOfItems--;

        if (full) {
          full = false;
        }

        break;
      }
    }

    if (!found) {
      throw new ItemNotFoundException();
    }
  }

  /**
   * Delete the Item at index `index`
   *
   * @param index
   * @throws IndexOutOfBoundsException
   * @throws NoItemAtIndexException
   */
  public void removeItem(int index) {
    if (index >= INVENTORY_SIZE || index < 0) {
      throw new IndexOutOfBoundsException(index);
    } else if (!itemPresent[index]) {
      throw new NoItemAtIndexException();
    }

    items[index] = null;
    itemPresent[index] = false;
    numberOfItems--;

    if (full) {
      full = false;
    }
  }

  /**
   * Check if Invertory contains an Item with the name `itemName`
   *
   * @param itemName
   * @return true if an Item with the name `itemName` is present in inventory
   */
  public boolean isPresent(String itemName) {
    boolean found = false;

    for (int i = 0; i < INVENTORY_SIZE; i++) {
      if (items[i] != null && items[i].getName() == itemName) {
        found = true;
      }
    }

    return found;
  }

  public String getItems() {
    String out = "[";

    out += "0:" + items[0].getName();

    for (int i = 1; i < INVENTORY_SIZE; i++) {
      if (items[i] != null) {
        out += ", " + i + ":" + items[i].getName();
      } else {
        out += ", " + i + ":null";
      }
    }

    return out + "]";
  }

  public String toString() {
    return "Inventory [\nitems=" + this.getItems() + ",\nnumberOfItems=" + numberOfItems + ",\nfull=" + full
        + "\n]";
  }

  public static int getInventorySize() {
    return INVENTORY_SIZE;
  }

  public int getNumberOfItems() {
    return numberOfItems;
  }

  public boolean isFull() {
    return full;
  }

  public boolean[] getItemPresent() {
    return itemPresent;
  }

  public class InventoryFullException extends RuntimeException {
  }

  public class NoItemAtIndexException extends RuntimeException {
  }

  public class ItemNotFoundException extends RuntimeException {
  }
}
