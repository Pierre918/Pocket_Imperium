import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import plateau.Hex;

public class Partie {
    public plateau.Sector[] sector = { null, null, null, null, null, null, null, null, null };

    public void affichage_plateau() {
        int[][] pos = {
                 { 10, 80 }, { 110, 80 }, { 210, 80 }, { 310, 80 }, { 410, 80 }, { 510, 80 }, // sector 1
                 { 60, 180 }, { 160, 180 }, { 260, 180 }, { 360, 180 }, { 460, 180 }, { 560, 180 }, // ...
                 { 10, 250 }, { 110, 250 }, { 210, 250 }, { 310, 250 }, { 410, 250 }, { 510, 250 },
                 { 60, 340 }, { 160, 340 },  { 360, 340 }, { 460, 340 }, 
                 { 10, 430 }, { 110, 430 },{ 230, 430 },  { 410, 430 }, { 510, 430 },
                 { 60, 520 }, { 160, 520 },  { 360, 520 }, { 460, 520 }, 
                 { 10, 600 }, { 110, 600 }, { 210, 600 }, { 310, 600 }, { 410, 600 }, { 510, 600 },
                 { 60, 680 }, { 160, 680 }, { 260, 680 }, { 360, 680 }, { 460, 680 }, { 560, 680 },
                 { 10, 770 }, { 110, 770 }, { 210, 770 }, { 310, 770 }, { 410, 770 }, { 510, 770 },
        };
        try {
            // Charger l'image de fond
            BufferedImage image = ImageIO.read(
                    new File("D:\\Documents\\documents Pierre\\UTT\\ISI\\ISI1\\LO02\\projet\\projet_lo2\\plateau.PNG"));

            // Obtenir le contexte graphique de l'image
            Graphics g = image.getGraphics();
            g.setFont(new Font("Arial", Font.BOLD, 20)); // Définir la police

            for (int i = 0; i < this.sector.length*6 -5; i++) {
                    // Définir la couleur du texte
                    g.setColor(Color.WHITE);

                    // Ajouter le texte à une position définie (x=50, y=50)
                    g.drawString(""+i, pos[i][0], pos[i][1]);

                    
                
            }
            g.dispose(); // Libérer les ressources graphiques
            // Afficher l'image modifiée dans une fenêtre JFrame
            JFrame frame = new JFrame("Image avec texte");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(image.getWidth(), image.getHeight());

            JLabel label = new JLabel(new ImageIcon(image));
            frame.add(label);

            frame.pack();
            frame.setVisible(true);

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setup() {
        // tri prime
        plateau.Hex[] hex = { new plateau.Hex(3) };
        this.sector[4] = new plateau.Sector(hex);
        // ligne du milieu
        plateau.Hex[] hexG = { null, null, null, null, null, null };
        hexG[0] = new plateau.Hex(2);
        hexG[1] = new plateau.Hex(0);
        hexG[2] = new plateau.Hex(1);
        hexG[3] = new plateau.Hex(0);
        hexG[4] = new plateau.Hex(1);
        hexG[5] = new plateau.Hex(0);
        plateau.Hex[] hexD = { null, null, null, null, null, null };
        hexD[0] = new plateau.Hex(0);
        hexD[1] = new plateau.Hex(1);
        hexD[2] = new plateau.Hex(0);
        hexD[3] = new plateau.Hex(2);
        hexD[4] = new plateau.Hex(0);
        hexD[5] = new plateau.Hex(1);
        plateau.Hex[][] choix = { hexG, hexD };
        List<plateau.Hex[]> list = Arrays.asList(choix);
        Collections.shuffle(list);
        sector[3] = new plateau.Sector(list.get(0));
        sector[5] = new plateau.Sector(list.get(1));
        // ligne du haut
        plateau.Hex[] hexGH = { null, null, null, null, null, null };
        hexGH[0] = new plateau.Hex(2);
        hexGH[1] = new plateau.Hex(0);
        hexGH[2] = new plateau.Hex(1);
        hexGH[3] = new plateau.Hex(0);
        hexGH[4] = new plateau.Hex(0);
        hexGH[5] = new plateau.Hex(1);
        plateau.Hex[] hexMH = { null, null, null, null, null, null };
        hexMH[0] = new plateau.Hex(1);
        hexMH[1] = new plateau.Hex(0);
        hexMH[2] = new plateau.Hex(1);
        hexMH[3] = new plateau.Hex(0);
        hexMH[4] = new plateau.Hex(2);
        hexMH[5] = new plateau.Hex(0);
        plateau.Hex[] hexDH = { null, null, null, null, null, null };
        hexDH[0] = new plateau.Hex(1);
        hexDH[1] = new plateau.Hex(1);
        hexDH[2] = new plateau.Hex(0);
        hexDH[3] = new plateau.Hex(0);
        hexDH[4] = new plateau.Hex(0);
        hexDH[5] = new plateau.Hex(2);
        // ligne du bas
        plateau.Hex[] hexGB = { null, null, null, null, null, null };
        hexGB[0] = new plateau.Hex(1);
        hexGB[1] = new plateau.Hex(1);
        hexGB[2] = new plateau.Hex(2);
        hexGB[3] = new plateau.Hex(0);
        hexGB[4] = new plateau.Hex(0);
        hexGB[5] = new plateau.Hex(0);
        plateau.Hex[] hexMB = { null, null, null, null, null, null };
        hexMB[0] = new plateau.Hex(0);
        hexMB[1] = new plateau.Hex(1);
        hexMB[2] = new plateau.Hex(2);
        hexMB[3] = new plateau.Hex(0);
        hexMB[4] = new plateau.Hex(0);
        hexMB[5] = new plateau.Hex(1);
        plateau.Hex[] hexDB = { null, null, null, null, null, null };
        hexDB[0] = new plateau.Hex(0);
        hexDB[1] = new plateau.Hex(0);
        hexDB[2] = new plateau.Hex(2);
        hexDB[3] = new plateau.Hex(0);
        hexDB[4] = new plateau.Hex(1);
        hexDB[5] = new plateau.Hex(1);

        plateau.Hex[][] choix1 = { hexGH, hexMH, hexDH, hexGB, hexMB, hexDB };
        list = Arrays.asList(choix1);
        // list.forEach((element) -> {
        // System.out.println(element);
        // for (int i = 0; i < element.length; i++)
        // System.out.print(element[i].getPlanet_contained());
        // });
        // System.out.println("\n");
        Collections.shuffle(list);
        // list.forEach((element) -> {
        // System.out.println(element);
        // for (int i = 0; i < element.length; i++)
        // System.out.print(element[i].getPlanet_contained());
        // });

        list = plateau.Sector.retourner_carte(list, hexGH, hexMH, hexDH, hexGB, hexMB, hexDB);
        // System.out.println("\n");
        // list.forEach((element) -> {
        // System.out.println(element);
        // for (int i = 0; i < element.length; i++)
        // System.out.print(element[i].getPlanet_contained());
        // });

        sector[0] = new plateau.Sector(list.get(0));
        sector[1] = new plateau.Sector(list.get(1));
        sector[2] = new plateau.Sector(list.get(2));
        sector[6] = new plateau.Sector(list.get(3));
        sector[7] = new plateau.Sector(list.get(4));
        sector[8] = new plateau.Sector(list.get(5));

        // getadjacents
        for (int i = 0; i < sector.length; i++) {
            for (int j = 0; j < sector[i].getHex().length; j++) {
                sector[i].getHex()[j].setId(j);
                sector[i].getHex()[j].setId_sector(i);
            }
        }
        // sector[0].getHex()[0].get_adjacents();

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Partie partie = new Partie();// a modif avec getInstance
        partie.setup();
        partie.affichage_plateau();

    }
}
