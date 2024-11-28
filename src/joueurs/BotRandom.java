package joueurs;

import java.awt.Color;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import partie.Partie;
import plateau.Hex;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Bot aléatoire qui agit comme un joueur dans un tour, de manière totalement
 * aléatoire.
 */
public class BotRandom extends Joueur {

    /**
     * Constructeur de la classe BotRandom.
     *
     * @param color La couleur associée au bot.
     */
    public BotRandom(Color color) {
        super(color);
    }

    /**
     * Le bot choisit une stratégie pour le tour.
     * Ici, cela n'a pas de sens, mais est requis par la classe mère.
     */
    @Override
    public void chooseStrat(Scanner scanner) {
        // Convertir les valeurs de l'énumération en une liste
        List<CommandCards> cardList = new ArrayList<>(List.of(CommandCards.values()));

        // Mélanger la liste
        Collections.shuffle(cardList);

        // Convertir la liste en un tableau
        this.strat = cardList.toArray(new CommandCards[0]);

        this.strat[0] = CommandCards.EXPLORE;

        System.out.println("Le bot aléatoire " + (this.getColor() == Color.BLUE ? "bleu"
                : this.getColor() == Color.GREEN ? "vert" : "jaune") + " a choisi sa stratégie");
    }

    /**
     * Choisi aléatoirement un hexagone contrôlé où placer un nombre aléatoire de
     * vaisseau.
     * Tout cela en prenant en compte le nombre de personne qui jouent la même carte
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
        System.out.println("Joueur " + (this.getColor() == Color.BLUE ? "bleu"
                : this.getColor() == Color.GREEN ? "vert" : "jaune") + " :\nExécute l'action EXPAND avec " + shipsToAdd
                + " vaisseaux.");
        Partie partie = Partie.getInstance();
        ArrayList<int[]> idControlledHexs = new ArrayList<>();
        for (Hex hex : this.getControlledHex(this)) {
            idControlledHexs.add(new int[] { hex.getIdSector(), hex.getId() });
        }
        Random random = new Random();

        while (shipsToAdd > 0) {
            Integer randomIndex = random.nextInt(idControlledHexs.size());

            // Obtenir l'élément à l'index aléatoire
            int[] randomElement = idControlledHexs.get(randomIndex);

            partie.sector[randomElement[0]].hex[randomElement[1]].addShips(1, this);
            this.nbShipsSupply--;
            shipsToAdd--;
            System.out.println("Un vaisseau a été ajouté à l'hexagone n°" + Hex.findIndex(Hex.plateau, randomElement));
        }
        partie.closeImage();
        partie.affichagePlateau();
    }

    /**
     * Permet de faire la première disposition des vaisseaux
     * 
     * @param i
     * @param j
     * @param scanner
     */
    public void initialDeployment(Integer i, Integer j, Scanner scanner) {
        Partie partie = Partie.getInstance();
        ArrayList<Hex> level1Hexs = new ArrayList<Hex>();
        for (int k = 0; k < partie.sector.length; k++) {
            for (int l = 0; l < partie.sector[k].hex.length; l++) {
                if (partie.sector[k].hex[l].getPlanetContained() == 1 && partie.sector[k].hex[l].getShips().isEmpty()
                        && !partie.sectorIsTaken(partie.sector[k])) {
                    level1Hexs.add(partie.sector[k].hex[l]);
                }
            }
        }
        Random random = new Random();
        Integer randomIndex = random.nextInt(level1Hexs.size());
        Hex randomElement = level1Hexs.get(randomIndex);
        partie.closeImage();
        partie.sector[randomElement.getIdSector()].hex[randomElement.getId()].addShips(2, partie.joueurs.get(j));
        partie.affichagePlateau();
        System.out.println("Bot " + (this.getColor() == Color.BLUE ? "bleu"
                : this.getColor() == Color.GREEN ? "vert" : "jaune") + " a choisi un hexagone");
    }

    /**
     * Implémentation de la méthode exterminate de manière aléatoire.
     * Choisi un hexagone contrôlé depuis lequel mener l'invasion de manière
     * aléatoire
     * Choisi également un hexagone adjacent et un nombre de vaisseaux pour
     * attaquer.
     */
    @Override
    public void exterminate(int playersChoosingExterminate, Scanner scanner) {
        int systemsToInvade;
        Partie partie = Partie.getInstance();
        System.out.println("Joueur " + (this.getColor() == Color.BLUE ? "bleu"
                : this.getColor() == Color.GREEN ? "vert" : "jaune") + " exécute l'action EXTERMINATE.");
        // Détermine le nombre de systèmes pouvant être envahis en fonction des joueurs
        // ayant choisi Exterminate
        systemsToInvade = switch (playersChoosingExterminate) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        // Gérer les invasions
        while (systemsToInvade > 0) {
            ArrayList<Integer> myHexIds = new ArrayList<>();
            for (Hex hex : this.getControlledHex(this)) {
                myHexIds.add(Hex.findIndex(Hex.plateau, new int[] { hex.getIdSector(), hex.getId() }));
            }
            Collections.shuffle(myHexIds);
            Hex hexChoisi = partie.sector[Hex.plateau[myHexIds.get(0)][0]].hex[Hex.plateau[myHexIds.get(0)][1]];
            Random random = new Random();
            System.out.println("bound : " + hexChoisi.getShips().size() + 1);
            int nbShipsMoving = hexChoisi
                    .getShips().size() == 1 ? 1
                            : (random
                                    .nextInt(hexChoisi
                                            .getShips().size() + 1));

            int invadeHexId = myHexIds.get(0);
            while (myHexIds.contains(invadeHexId)) {
                invadeHexId = Hex.findIndex(Hex.plateau,
                        hexChoisi.getAdjacents()[random.nextInt(hexChoisi.getAdjacents().length)]);
            }
            System.out.println("Le bot envahie l'hexagone n°" + invadeHexId + "avec " + nbShipsMoving + " vaisseaux");
            this.invade(hexChoisi, invadeHexId, nbShipsMoving);
            // Réduire le nombre de systèmes restants à envahir
            systemsToInvade--;
        }

        System.out.println("Extermination terminée.");
    }

    /**
     * Implémentation de la méthode explore
     * Implémentation de la méthode exterminate de manière aléatoire.
     * Choisi un hexagone contrôlé depuis lequel mener l'invasion de manière
     * aléatoire
     * Choisi également un hexagone adjacent et un nombre de vaisseaux pour se
     * déplacer.
     * 
     * @param playersChoosingExplore
     * @param scanner
     */
    @Override
    public void explore(int playersChoosingExplore, Scanner scanner) {
        int fleetsToMove;
        Partie partie = Partie.getInstance();
        System.out.println("Bot " + (this.getColor() == Color.BLUE ? "bleu"
                : this.getColor() == Color.GREEN ? "vert" : "jaune") + " exécute l'action EXPLORE.");

        fleetsToMove = switch (playersChoosingExplore) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        // Déplacement des flottes
        Random random = new Random();
        while (fleetsToMove > 0) {
            ArrayList<Integer> myHexIds = new ArrayList<>();
            for (Hex hex : this.getControlledHex(this)) {
                myHexIds.add(Hex.findIndex(Hex.plateau, new int[] { hex.getIdSector(), hex.getId() }));
            }
            int startHexId = myHexIds.get(random.nextInt(myHexIds.size()));
            Hex startHex = partie.sector[Hex.plateau[startHexId][0]].hex[Hex.plateau[startHexId][1]];
            int targetHexId = -1;
            do {
                targetHexId = Hex.findIndex(Hex.plateau,
                        startHex.getAdjacents()[random.nextInt(startHex.getAdjacents().length)]);
                if (targetHexId == 24) {
                    myHexIds.remove(Integer.valueOf(targetHexId));
                }
            } while (!isHexAdjacentAndFree(startHexId, targetHexId));
            System.out.println("bound : "
                    + partie.sector[Hex.plateau[startHexId][0]].hex[Hex.plateau[startHexId][1]].getShips().size() + 1);
            int nbShipsMoving = partie.sector[Hex.plateau[startHexId][0]].hex[Hex.plateau[startHexId][1]].getShips()
                    .size() == 1 ? 1
                            : (random.nextInt(
                                    partie.sector[Hex.plateau[startHexId][0]].hex[Hex.plateau[startHexId][1]].getShips()
                                            .size() + 1));

            System.out.println(nbShipsMoving);
            moveFleet(startHexId, targetHexId, nbShipsMoving);

            fleetsToMove--; // Réduit le nombre de flottes restantes
        }
        partie.closeImage();
        partie.affichagePlateau();
        System.out.println("Exploration terminée.");
    }

}
