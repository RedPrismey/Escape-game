import java.awt.EventQueue;
import UI.*;
import Player.*;

public class Game {
  public static void main(String[] args) {
    EventQueue.invokeLater(() -> {
      new UI();
    });
  }
}
