package UI;
import java.awt.*;
import javax.swing.*;
class HotBar extends JPanel {
    protected ObjetCle[] inventaireObjetCles;
    public HotBar() {
        this.inventaireObjetCles = new ObjetCle[9];
        setOpaque(false); 
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    
        int panelWidth = getWidth();
        int panelHeight = getHeight();
    
        int totalSlots = inventaireObjetCles.length;
        int slotWidth = (int)(panelWidth * 0.05); // 5% largeur par slot
        int slotHeight = (int)(panelHeight * 0.8); // 80% hauteur du panel
        int padding = (int)(panelWidth * 0.01);   // 1% espace entre slots
    
        int totalWidth = totalSlots * slotWidth + (totalSlots - 1) * padding;
        int startX = (int)(totalWidth* 0.01);
        int y = (panelHeight - slotHeight) / 2;
    
        for (int i = 0; i < totalSlots; i++) {
            int x = startX + i * (slotWidth + padding);
    
            g.setColor(Color.GRAY);
            g.drawRect(x, y, slotWidth, slotHeight);
    
            ObjetCle obj = inventaireObjetCles[i];
            if (obj != null && obj.sprite != null) {
                g.drawImage(obj.sprite, x + 4, y + 4, slotWidth - 8, slotHeight - 8, null);
            }
        }
    }}