package joueurs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import partie.Partie;
import plateau.Hex;

/**
 * La classe BotOffensif représente un robot virtuel de type offensif dans le jeu. Elle étend la
 * classe Joueur
 * Le bot implémenté par cette classe attaque dès qu'il peut. Il se place en priorité à côté des hexagones où un
 * vaisseau est déjà présent et attaque toujours au premier tour.
 */
public class BotOffensif extends Joueur {
    /**
     * Constructeur de la classe BotRandom.
     *
     * @param color La couleur associée au bot.
     */
    public BotOffensif(Color color) {
        super(color);
    }

    
    /** 
     * Ajoute en priorité des vaisseaux aux hexagones qui ont des voisins où il y a des vaisseaux d'autres joueurs
     * Sinon choisi au hasard un hexagone qu'il contrôle.
     *   
     * @param playersChoosingExpand
     * @param scanner
     */
    @Override
    public void expand(int playersChoosingExpand, Scanner scanner) {
        int shipsToAdd;
        shipsToAdd = switch (playersChoosingExpand) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };
        if (this.nbShipsSupply < shipsToAdd) {
            System.out.println("Il n'y a pas assez de vaisseaux en réserve.");
            shipsToAdd = this.nbShipsSupply; // Limite à la réserve disponible
        }
        Partie partie = Partie.getInstance();
        ArrayList<int[]> idControlledHexs = new ArrayList<>();
        for (Hex hex : this.getControlledHex(this)) {
            idControlledHexs.add(new int[] { hex.getIdSector(), hex.getId() });
        }

        ArrayList<Hex> myHexs = this.getControlledHex(this);
        Random random = new Random();
        while (shipsToAdd > 0) {
            boolean isAdded=false;
            for (Hex hex : myHexs) {
                for (int[] hexAdj : hex.getAdjacents()) {
                    if (!partie.sector[hexAdj[0]].hex[hexAdj[1]].getShips().isEmpty()) {
                        if (partie.sector[hexAdj[0]].hex[hexAdj[1]].getShips().get(0).joueur != this && !isAdded) {
                            partie.sector[hex.getIdSector()].hex[hex.getId()].addShips(1, this);
                            shipsToAdd--;
                            nbShipsSupply--;
                            System.out.println("Bot " + (this.getColor() == Color.BLUE ? "bleu"
                                    : this.getColor() == Color.GREEN ? "vert" : "jaune")
                                    + "a ajouté un vaisseau a été ajouté à l'hexagone n°"
                                    + Hex.findIndex(Hex.plateau, new int[] { hex.getIdSector(), hex.getId() }));
                            isAdded=true;
                        }
                    }

                }

            }
            Integer randomIndex = random.nextInt(idControlledHexs.size());

            // Obtenir l'élément à l'index aléatoire
            int[] randomElement = idControlledHexs.get(randomIndex);

            partie.sector[randomElement[0]].hex[randomElement[1]].addShips(1, this);
            this.nbShipsSupply--;
            shipsToAdd--;
            System.out.println("Bot " + (this.getColor() == Color.BLUE ? "bleu"
                    : this.getColor() == Color.GREEN ? "vert" : "jaune")
                    + "a ajouté un vaisseau a été ajouté à l'hexagone n°" + Hex.findIndex(Hex.plateau, randomElement));
            partie.closeImage();
            partie.affichagePlateau();
        }
    }

    
    /** 
     * Le bot se place en priorité à côté de joueur, en prenant en compte les contraintes liées au déploiement initial.
     * Sinon il se place sur un hexagone aléaoire de niveau 1.
     * @param i Permet de connaitre le numéro du tour
     * @param j Permet de savoir si ce joueur est le premier a jouer dans le tour
     * @param scanner
     */
    @Override
    public void initialDeployment(Integer i, Integer j, Scanner scanner) {
        Partie partie = Partie.getInstance();
        ArrayList<Hex> level1HexsTaken = new ArrayList<Hex>();
        ArrayList<Hex> level1Hexs = new ArrayList<Hex>();
        for (int k = 0; k < partie.sector.length; k++) {
            for (int l = 0; l < partie.sector[k].hex.length; l++) {
                if (!partie.sector[k].hex[l].getShips().isEmpty()) {
                    level1HexsTaken.add(partie.sector[k].hex[l]);
                }
                if (partie.sector[k].hex[l].getPlanetContained() == 1 && partie.sector[k].hex[l].getShips().isEmpty()
                        && !partie.sectorIsTaken(partie.sector[k])) {
                    level1Hexs.add(partie.sector[k].hex[l]);
                }
            }
        }
        if (i != 0 || j != 0) { //si le joueur n'est pas le premier a jouer (et qu'il y a déjà des vaisseaux sur le plateaux)
            for (Hex hex : level1HexsTaken) {
                for (int[] hexAdj : hex.getAdjacents()) {
                    if (hexAdj[0] != hex.getIdSector()
                            && partie.sector[hexAdj[0]].hex[hexAdj[1]].getPlanetContained() == 1
                            && partie.sector[hexAdj[0]].hex[hexAdj[1]].getShips().isEmpty()) {
                        partie.sector[hexAdj[0]].hex[hexAdj[1]].addShips(2, this);
                        partie.closeImage();
                        partie.affichagePlateau();
                        System.out.println("Bot " + (this.getColor() == Color.BLUE ? "bleu"
                                : this.getColor() == Color.GREEN ? "vert" : "jaune") + " a choisi un hexagone");
                        return;
                    }

                }

            }
        }
        partie.closeImage();
        Collections.shuffle(level1Hexs);
        partie.sector[level1Hexs.get(0).getIdSector()].hex[level1Hexs.get(0).getId()].addShips(2,
                partie.joueurs.get(j));
        System.out.println("Bot " + (this.getColor() == Color.BLUE ? "bleu"
                : this.getColor() == Color.GREEN ? "vert" : "jaune") + " a choisi un hexagone");
        partie.affichagePlateau();
    }

    
    /** 
     * La stratégie est toujours la même : attaquer en priorité.
     * @param scanner
     */
    @Override
    public void chooseStrat(Scanner scanner) {
        this.strat[0] = CommandCards.EXTERMINATE; 
        this.strat[1] = CommandCards.EXPLORE;
        this.strat[2] = CommandCards.EXPAND;
    }

    @Override
    public void exterminate(int playersChoosingExterminate, Scanner scanner) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exterminate'");
    }

    @Override
    public void explore(int playersChoosingExplore, Scanner scanner) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'explore'");
    }
}
