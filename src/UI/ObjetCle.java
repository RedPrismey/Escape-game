package UI;
import java.awt.image.BufferedImage;
public class ObjetCle {
    protected String nom;
    protected int numero;
    protected BufferedImage sprite;

    public ObjetCle(String nom, int numero, BufferedImage sprite){
        this.nom = nom;
        this.numero = numero;
        this.sprite = sprite;
    }
}