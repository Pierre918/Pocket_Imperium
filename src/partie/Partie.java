package partie;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;

import joueurs.BotOffensif;
import joueurs.BotRandom;
import joueurs.CommandCards;
import joueurs.Joueur;
import joueurs.VraiJoueur;
import plateau.Hex;
import plateau.Sector;

/**
 * La classe Partie représente une partie du jeu.
 * Elle gère l'initialisation, l'affichage et la gestion des joueurs et du
 * plateau de jeu.
 */
public class Partie {
    /**
     * Tableau représentant les secteurs du plateau de jeu.
     */
    public plateau.Sector[] sector = { null, null, null, null, null, null, null, null, null };

    /**
     * Instance unique de la classe Partie (Singleton).
     */
    private static Partie instance;

    /**
     * Liste des joueurs participant à la partie.
     */
    public List<Joueur> joueurs;

    /**
     * Fenêtre d'affichage du plateau de jeu.
     */
    JFrame frame = new JFrame("Plateau");

    /**
     * Constructeur privé pour empêcher l'instanciation directe.
     */
    private Partie() {
    }

    /**
     * Méthode publique pour obtenir l'instance unique de la classe Partie.
     *
     * @return L'instance unique de la classe Partie.
     */
    public static Partie getInstance() {
        if (instance == null) {
            instance = new Partie();
        }
        return instance;
    }

    /**
     * Méthode pour afficher le plateau de jeu.
     * La puissance des planètes s'affiche en rouge et le nombre de vaisseau de
     * chaque joueur s'affiche avec sa couleur
     */
    public void affichagePlateau() {

        int[][] pos = {
                { 10, 80 }, { 110, 80 }, { 210, 80 }, { 310, 80 }, { 410, 80 }, { 510, 80 },
                { 60, 180 }, { 160, 180 }, { 260, 180 }, { 360, 180 }, { 460, 180 }, { 560, 180 },
                { 10, 250 }, { 110, 250 }, { 210, 250 }, { 310, 250 }, { 410, 250 }, { 510, 250 },
                { 60, 340 }, { 160, 340 }, { 360, 340 }, { 460, 340 },
                { 10, 430 }, { 110, 430 }, { 230, 430 }, { 410, 430 }, { 510, 430 },
                { 60, 520 }, { 160, 520 }, { 360, 520 }, { 460, 520 },
                { 10, 600 }, { 110, 600 }, { 210, 600 }, { 310, 600 }, { 410, 600 }, { 510, 600 },
                { 60, 680 }, { 160, 680 }, { 260, 680 }, { 360, 680 }, { 460, 680 }, { 560, 680 },
                { 10, 770 }, { 110, 770 }, { 210, 770 }, { 310, 770 }, { 410, 770 }, { 510, 770 },
        };
        try {
            BufferedImage image = ImageIO.read(
                    new File("plateau.PNG"));

            Graphics g = image.getGraphics();
            g.setFont(new Font("Arial", Font.BOLD, 20));

            for (int i = 0; i < this.sector.length * 6 - 5; i++) {
                g.setColor(Color.WHITE);
                g.drawString("" + i, pos[i][0], pos[i][1]);
                if (i != 24 && sector[plateau.Hex.plateau[i][0]].hex[plateau.Hex.plateau[i][1]]
                        .getPlanetContained() != 0) {
                    g.setColor(Color.RED);
                    g.drawString(
                            "" + sector[plateau.Hex.plateau[i][0]].hex[plateau.Hex.plateau[i][1]].getPlanetContained(),
                            pos[i][0] + 35, pos[i][1]);
                }
                if (!sector[plateau.Hex.plateau[i][0]].hex[plateau.Hex.plateau[i][1]]
                        .getShips().isEmpty()) {
                    g.setColor(sector[plateau.Hex.plateau[i][0]].hex[plateau.Hex.plateau[i][1]]
                            .getShips().get(0).joueur.getColor());
                    g.drawString(
                            "" + sector[plateau.Hex.plateau[i][0]].hex[plateau.Hex.plateau[i][1]]
                                    .getShips().size(),
                            pos[i][0] + 35, pos[i][1] + 35);
                }
            }
            g.dispose();

            this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.frame.setSize(image.getWidth(), image.getHeight());

            JLabel label = new JLabel(new ImageIcon(image));
            this.frame.add(label);

            this.frame.pack();
            this.frame.setVisible(true);

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour fermer la fenêtre d'affichage du plateau de jeu.
     */
    public void closeImage() {
        this.frame.dispose();
        this.frame = new JFrame("Plateau");
    }

    /**
     * Méthode pour initialiser le plateau de jeu.
     */
    public void setup() {

        // Secteur central (secteur 4)
        plateau.Hex[] hex = { new plateau.Hex(3) };
        this.sector[4] = new plateau.Sector(hex);

        // Secteurs gauche et droit
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

        // Secteurs supplémentaires
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
        Collections.shuffle(list);

        list = plateau.Sector.retournerCarte(list, hexGH, hexMH, hexDH, hexGB, hexMB, hexDB);

        sector[0] = new plateau.Sector(list.get(0));
        sector[1] = new plateau.Sector(list.get(1));
        sector[2] = new plateau.Sector(list.get(2));
        sector[6] = new plateau.Sector(list.get(3));
        sector[7] = new plateau.Sector(list.get(4));
        sector[8] = new plateau.Sector(list.get(5));

        // Attribution d'IDs uniques aux hexagones
        for (int i = 0; i < sector.length; i++) {
            if (sector[i] != null) {
                for (int j = 0; j < sector[i].hex.length; j++) {
                    if (sector[i].hex[j] != null) { // Vérifier que l'hexagone n'est pas null
                        this.sector[i].hex[j].setId(Hex.findIndex(Hex.plateau, new int[] { i, j }));
                        this.sector[i].hex[j].setIdSector(i);
                    }
                }
            }
        }// Attribution d'IDs uniques aux hexagones
        for (int i = 0; i < sector.length; i++) {
            if (sector[i] != null) {
                for (int j = 0; j < sector[i].hex.length; j++) {
                    if (sector[i].hex[j] != null) { // Vérifier que l'hexagone n'est pas null
                    this.sector[i].hex[j].setAdjacents();
                    }
                }
            }
        }
    }

    public List<Hex> getPlateau() {
        List<Hex> allHexes = new ArrayList<>();
        for (Sector sector : this.sector) {
            if (sector != null) {
                for (Hex hex : sector.hex) {
                    if (hex != null) {
                        allHexes.add(hex);
                    }
                }
            }
        }
        return allHexes;
    }

    /**
     * Méthode pour vérifier si un secteur est occupé par un joueur.
     *
     * @param sec Le secteur à vérifier.
     * @return true si le secteur est occupé, false sinon.
     */
    public boolean sectorIsTaken(Sector sec) {
        for (int i = 0; i < sec.hex.length; i++) {
            if (sec.hex[i].getPlanetContained() == 1 && !sec.hex[i].getShips().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Méthode pour le déploiement initial des joueurs.
     *
     * @param scanner Le scanner pour lire les entrées utilisateur.
     */
    public void initialDeployment(Scanner scanner) {

        for (int i = 0; i < 2; i++) {
            System.out.println("Chaque joueur ajoute 1 vaisseaux.");
            for (int j = (i == 0 ? 0 : this.joueurs.size() - 1); (i == 0 ? j < this.joueurs.size()
                    : j >= 0); j = (i == 0 ? j + 1 : j - 1)) {
                this.joueurs.get(j).initialDeployment(i, j, scanner);
            }
        }
    }

    public void perform(Scanner scanner) {
        for (int i = 0; i < 3; i++) {
            System.out.println("C'est le " + (i + 1) + "e tour");
            for (int k = 0; k < 3; k++) {
                System.out.println("------------Affichage des cartes-------------\n" + "Joueur "
                        + (this.joueurs.get(k).getColor() == Color.BLUE ? "bleu :"
                                : this.joueurs.get(k).getColor() == Color.GREEN ? "vert :" : "jaune :"));
                System.out.println("\t - " + (this.joueurs.get(k).getStrat()[i] == CommandCards.EXPAND ? "Expand"
                        : this.joueurs.get(k).getStrat()[i] == CommandCards.EXPLORE ? "Explore" : "Exterminate"));
            }
            for (int j = 0; j < 3; j++) {
                this.joueurs.get(j).jouerTour(i, scanner);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println();
        Partie partie = Partie.getInstance();
        partie.setup();
        int nom = -1;
        Scanner scanner = new Scanner(System.in);

        // Sélection du type de joueurs
        while (nom < 1 || nom > 3) {
            System.out.println(
                    "Sélectionnez 1/2/3 : 2 joueurs virtuels (1)/1 joueur virtuel et un vrai joueur (2)/2 vrais joueurs (3) :");
            nom = Integer.parseInt(scanner.nextLine());
        }

        switch (nom) {
            case 1: {
                Integer[] choix = { -1, -1 };
                for (int i = 0; i < 2; i++) {
                    while (choix[i] < 1 || choix[i] >= 3) {
                        System.out.println(
                                "Le " + (i+1) + "e bot offensif(1) ou aléatoire (2) ? ");
                        choix[i] = Integer.parseInt(scanner.nextLine());
                    }
                }

                partie.joueurs = Arrays.asList(
                        choix[0] == 2 ? new BotRandom(Color.GREEN) : new BotOffensif(Color.GREEN),
                        choix[1] == 2 ? new BotRandom(Color.YELLOW) : new BotOffensif(Color.YELLOW),
                        new VraiJoueur(Color.BLUE));
                break;
            }
            case 2: {
                Integer choix = -1;
                while (choix < 1 || choix >= 3) {
                    System.out.println(
                            "Le bot est un bot offensif(1) ou aléatoire (2) ? ");
                    choix = Integer.parseInt(scanner.nextLine());
                }

                partie.joueurs = Arrays.asList(
                        choix == 2 ? new BotRandom(Color.GREEN) : new BotOffensif(Color.GREEN),
                        new VraiJoueur(Color.YELLOW),
                        new VraiJoueur(Color.BLUE));
                break;
            }
            default:
                partie.joueurs = Arrays.asList(
                        new VraiJoueur(Color.GREEN),
                        new VraiJoueur(Color.YELLOW),
                        new VraiJoueur(Color.BLUE));
                break;
        }

        // Mélange des joueurs pour l'ordre des tours
        Collections.shuffle(partie.joueurs);

        // Affichage initial du plateau
        partie.affichagePlateau();

        // Phase de déploiement initial
        partie.initialDeployment(scanner);
        // Phase de choix des stratégies
        for (Joueur joueur : partie.joueurs) {
            System.out.println("Joueur " + (joueur.getColor() == Color.BLUE ? "bleu :"
                    : joueur.getColor() == Color.GREEN ? "vert :" : "jaune :")+"choix de la stratégie");
            joueur.chooseStrat(scanner);
        }
        partie.perform(scanner);
    }

}
