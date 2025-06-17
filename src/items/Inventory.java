package items;

import java.awt.*;

public class Inventory {
  private final static int INVENTORY_SIZE = 6;
  private final Item[] items = new Item[INVENTORY_SIZE];
  private int numberOfItems = 0;
  private boolean full = false;
  private final boolean[] itemPresent = new boolean[INVENTORY_SIZE]; // permet de savoir où il y a des items
                                                                     // dans l'inventaire sans le parcourir à
                                                                     // chaque fois

  public Inventory() {}

  public void draw(Graphics g, int x, int y, int width, int height) {
    // Fond blanc
    g.setColor(Color.WHITE);
    g.fillRect(x, y, width, height);

    int totalSlots = Inventory.getInventorySize();

    // Pour des cases carrées, hauteur = largeur
    int slotSize = (int) (height * 0.8);

    int padding = (int) (width * 0.01);

    int totalWidth = totalSlots * slotSize + (totalSlots - 1) * padding;

    int startX = (int) (totalWidth * 0.02);
    int slotY = y + (height - slotSize) / 2;

    for (int i = 0; i < totalSlots; i++) {
      // Dessine les bordures
      int slotX = startX + i * (slotSize + padding);
      g.setColor(Color.GRAY);
      g.drawRect(slotX, slotY, slotSize, slotSize);

      // Dessine le sprite des items présents
      Item item = items[i];
      if (item != null && item.getSprite() != null) {
        g.drawImage(item.getSprite(), slotX + 4, slotY + 4, slotSize - 8, slotSize - 8, null);
      }
    }
    }

  /**
   * Add the Item `item` to the Inventory
   *
   * @param item the Item to add
   * @throws InventoryFullException if the Inventory is full
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
   * @param itemName the name of the Item to remove
   * @throws ItemNotFoundException if no Item with the name `itemName` is found in the Inventory
   */
  public void removeItem(String itemName) {
    boolean found = false;

    for (int i = 0; i < INVENTORY_SIZE; i++) {
      if (items[i] != null && items[i].getName().equals(itemName)) {
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
   * @param index the index of the Item to remove
   * @throws IndexOutOfBoundsException if `index` is not in the range [0, INVENTORY_SIZE)
   * @throws NoItemAtIndexException if there is no Item at index `index`
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
   * @param itemName the name of the Item to check
   * @return true if an Item with the name `itemName` is present in inventory
   */
  public boolean isPresent(String itemName) {
    boolean found = false;

    for (int i = 0; i < INVENTORY_SIZE; i++) {
      if (items[i] != null && items[i].getName().equals(itemName)) {
        found = true;
        break;
      }
    }

    return found;
  }

  public Item[] getItem() {
    return this.items;
  }

  public String itemsToString() {
    StringBuilder out = new StringBuilder("[");

    out.append("0:").append(items[0].getName());

    for (int i = 1; i < INVENTORY_SIZE; i++) {
      if (items[i] != null) {
        out.append(", ").append(i).append(":").append(items[i].getName());
      } else {
        out.append(", ").append(i).append(":null");
      }
    }

    return out + "]";
  }

  public String toString() {
    return "Inventory [\nitems=" + this.itemsToString() + ",\nnumberOfItems=" + numberOfItems + ",\nfull=" + full
        + "\n]";
  }

  public Item get(int i) {
    return items[i];
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

  public static class InventoryFullException extends RuntimeException {
  }

  public static class NoItemAtIndexException extends RuntimeException {
  }

  public static class ItemNotFoundException extends RuntimeException {
  }
}
