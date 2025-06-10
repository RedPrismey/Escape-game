package Player;

public class Test {
  public static void main(String[] args) {
    Item i1 = new Item("Carte");
    Item i2 = new Item("Clé");
    Item i3 = new Item("Bateau");

    Inventory inv = new Inventory();

    // check ajout item
    inv.addItem(i1);
    inv.addItem(i1);
    inv.addItem(i2);
    inv.addItem(i1);
    inv.addItem(i3);
    inv.addItem(i1);

    System.out.println(inv.toString()); // check si full + vérification de l'affichage

    // check InventoryFullException
    try {
      inv.addItem(i1);
    } catch (Inventory.InventoryFullException _err) {
      System.out.println("Inventory full catched");
    }

    // check suppression d'item + affichage
    inv.removeItem(1);
    inv.removeItem(3);
    System.out.println(inv.toString());

    // check ajout d'item au milieu de l'inventaire
    inv.addItem(i2);
    System.out.println(inv.toString());

    // check NoItemAtIndexException
    try {
      inv.removeItem(5);
    } catch (Inventory.NoItemAtIndexException _err) {
      System.out.println("No item at index catched");
    }

    // check IndexOutOfBoundsException
    try {
      inv.removeItem(7);
    } catch (IndexOutOfBoundsException _err) {
      System.out.println("Index out of bound catched");
    }

    inv.removeItem("Bateau"); // check suppression par nom

    // check ItemNotFoundException
    try {
      inv.removeItem("Zglorb");
    } catch (Inventory.ItemNotFoundException err) {
      System.out.println("No item with this name catched");
    }

  }
}
