package Player;

public class Inventory {
  private final static byte INVENTORY_SIZE = 6;
  private Item[] items;
  private byte numberOfItems = 0;
  private boolean full = false;
  private boolean[] itemPresent = new boolean[INVENTORY_SIZE]; // permet de savoir où il y a des items
                                                               // dans l'inventaire sans le parcourir à
                                                               // chaque fois

  public Inventory() {
    items = new Item[INVENTORY_SIZE];
  }

  /**
   * Ajoute l'Item `item` à l'inventaire
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
   * Parcours les items de l'inventaire jusqu'au premier qui a le même nom que
   * `name`, puis le supprime de l'inventaire
   * 
   * @param name
   * @throws ItemNotFoundException
   */
  public void removeItem(String name) {
    boolean found = false;

    for (int i = 0; i < INVENTORY_SIZE; i++) {
      if (items[i] != null && items[i].getName() == name) {
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
   * Supprime l'Item à l'index `index`.
   *
   * @param index
   * @throws IndexOutOfBoundsException
   * @throws NoItemAtIndexException
   */
  public void removeItem(byte index) {
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

  public class InventoryFullException extends RuntimeException {
  }

  public class NoItemAtIndexException extends RuntimeException {
  }

  public class ItemNotFoundException extends RuntimeException {
  }
}
