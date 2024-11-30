package joueurs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import partie.Partie;
import plateau.Hex;
import plateau.Sector;

/**
 * La classe BotOffensif représente un robot virtuel de type offensif dans le
 * jeu. Elle étend la
 * classe Joueur
 * Le bot implémenté par cette classe attaque dès qu'il peut. Il se place en
 * priorité à côté des hexagones où un
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
     * Ajoute en priorité des vaisseaux aux hexagones qui ont des voisins où il y a
     * des vaisseaux d'autres joueurs
     * Sinon choisi au hasard un hexagone qu'il contrôle.
     * 
     * @param playersChoosingExpand
     */
    @Override
    public void expand(int playersChoosingExpand) {
        int shipsToAdd;
        shipsToAdd = switch (playersChoosingExpand) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };
        if (this.getControlledHex(this).isEmpty()){
            System.out.println("Plus de vaisseau sur le plateau");
            return;
        }
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
            boolean isAdded = false;
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
                            isAdded = true;
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
     * Le bot se place en priorité à côté de joueur, en prenant en compte les
     * contraintes liées au déploiement initial.
     * Sinon il se place sur un hexagone aléaoire de niveau 1.
     * 
     * @param i Permet de connaitre le numéro du tour
     * @param j Permet de savoir si ce joueur est le premier a jouer dans le
     *          tour
     */
    @Override
    public void initialDeployment(Integer i, Integer j) {
        Partie partie = Partie.getInstance();
        ArrayList<Hex> level1HexsTaken = new ArrayList<Hex>();
        ArrayList<Hex> level1Hexs = new ArrayList<Hex>();
        for (int k = 0; k < partie.sector.length; k++) {
            for (int l = 0; l < partie.sector[k].hex.length; l++) {
                if (!partie.sector[k].hex[l].getShips().isEmpty()) {
                    level1HexsTaken.add(partie.sector[k].hex[l]);
                }
                if (partie.sector[k].hex[l].getPlanetContained() == 1 && partie.sector[k].hex[l].getShips().isEmpty()
                        && !partie.sectorIsTakenL1(partie.sector[k])) {
                    level1Hexs.add(partie.sector[k].hex[l]);
                }
            }
        }
        if (i != 0 || j != 0) { // si le joueur n'est pas le premier a jouer (et qu'il y a déjà des vaisseaux
                                // sur le plateaux)
            for (Hex hex : level1HexsTaken) {
                for (int[] hexAdj : hex.getAdjacents()) {
                    if (!partie.sectorIsTakenL1(partie.sector[hexAdj[0]])
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
     * 
     */
    @Override
    public void chooseStrat() {
        this.strat[0] = CommandCards.EXTERMINATE;
        this.strat[1] = CommandCards.EXPLORE;
        this.strat[2] = CommandCards.EXPAND;
    }

    /**
     * Dès qu'il peut, le bot envahi les hexagones adjacents où il y a quelqu'un.
     * Cela en mobilisant tous les vaisseau
     * Sinon, il envahi de manière aléatoir un hexagone
     * 
     * @param playersChoosingExterminate
     */
    @Override
    public void exterminate(int playersChoosingExterminate) {
        int systemsToInvade;
        Partie partie = Partie.getInstance();
        System.out.println("Bot " + (this.getColor() == Color.BLUE ? "bleu"
                : this.getColor() == Color.GREEN ? "vert" : "jaune") + " exécute l'action EXTERMINATE.");
        // Détermine le nombre de systèmes pouvant être envahis en fonction des joueurs
        // ayant choisi Exterminate
        systemsToInvade = switch (playersChoosingExterminate) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };
        ArrayList<int[]> doNotUse = new ArrayList<>();
        // Gérer les invasions
        while (systemsToInvade > 0) {
            ArrayList<Integer> myHexIds = new ArrayList<>();
            for (Hex hex : this.getControlledHex(this)) {
                if (!containsArray(doNotUse, new int[] { hex.getIdSector(), hex.getId() })) {
                    myHexIds.add(Hex.findIndex(Hex.plateau, new int[] { hex.getIdSector(), hex.getId() }));
                }
            }
            if (myHexIds.size() == 0) {
                System.out.println("Plus de vaisseaux valable");
                break;
            }
            int targetHexId = -1;
            ArrayList<Hex> myHexs = this.getControlledHex(this);
            boolean finded = false;
            int startHexId = -1;
            for (Hex hex : myHexs) {
                for (int[] hexAdj : hex.getAdjacents()) {
                    if (!partie.sector[hexAdj[0]].hex[hexAdj[1]].getShips().isEmpty()) {
                        if (partie.sector[hexAdj[0]].hex[hexAdj[1]].getShips().get(0).joueur != this && !finded) {
                            startHexId = Hex.findIndex(Hex.plateau, new int[] { hex.getIdSector(), hex.getId() });
                            targetHexId = Hex.findIndex(Hex.plateau, hexAdj);
                            doNotUse.add(hexAdj);
                            finded = true;
                        }
                    }
                }
            }
            if (finded) {
                Hex startHex = partie.sector[Hex.plateau[startHexId][0]].hex[Hex.plateau[startHexId][1]];

                int shipsToUse = startHex.getShips().size();
                // Demander le nombre de vaisseaux à utiliser pour l'invasion
                System.out.println("Le bot envahie l'hexagone n°" + targetHexId + "avec " + shipsToUse + " vaisseaux");
                // Simuler l'invasion avec le nombre choisi de vaisseaux
                invade(startHex, targetHexId, shipsToUse);

                // Réduire le nombre de systèmes restants à envahir

            } else {
                doNotUse.add(Hex.plateau[this.exterminateRandom(1, doNotUse)]);
            }
            systemsToInvade--;
        }

        System.out.println("Extermination terminée.");
    }

    /**
     * Récupère les positions hexagonales adjacentes qui sont occupées par
     * l'adversaire.
     *
     * @param myHexIds Une liste d'identifiants hexagonaux représentant les
     *                 positions à vérifier.
     * @return Un tableau de deux entiers où le premier élément est l'identifiant
     *         hexagonal de départ
     *         et le second élément est l'identifiant hexagonal adjacent occupé par
     *         l'adversaire.
     *         Retourne null si aucune position adjacente n'est occupée par
     *         l'adversaire.
     */
    private int[] getAdjacentsOpponent(ArrayList<Integer> myHexIds) {
        int[] res = new int[] { -1, -1 };
        Partie partie = Partie.getInstance();
        for (Integer hex : myHexIds) {
            Hex myHex = partie.sector[Hex.plateau[hex][0]].hex[Hex.plateau[hex][1]];
            boolean adjIsOpponent = false;
            for (int[] adj : myHex.getAdjacents()) {
                if (adjIsOpponent) {
                    continue;
                }
                Hex adjHex = partie.sector[adj[0]].hex[adj[1]];
                if (!adjHex.getShips().isEmpty()) {
                    if (adjHex.getShips().get(0).joueur != this) {
                        adjIsOpponent = true;
                        continue;
                    }
                }
                for (int[] adjofAdj : adjHex.getAdjacents()) {
                    Hex adjofAdjHex = partie.sector[adjofAdj[0]].hex[adjofAdj[1]];
                    if (!adjofAdjHex.getShips().isEmpty()) {
                        if (adjofAdjHex.getShips().get(0).joueur != this) {
                            res[1] = Hex.findIndex(Hex.plateau, adj);
                            res[0] = hex;
                            return res;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Exécute l'action d'exploration pour le bot.
     * Le bot se place en priorité à côté d'autres joueurs dans le but de provoquer
     * le combat.
     * Sinon, si personne n'est à deux hexagones de lui où qu'il ne peut plus bouger
     * d'autres vaisseaux, il bouge de manière
     * aléatoire.
     *
     * @param playersChoosingExplore Le nombre de joueurs choisissant d'explorer.
     */
    @Override
    public void explore(int playersChoosingExplore) {
        int fleetsToMove;
        Partie partie = Partie.getInstance();
        System.out.println("Bot " + (this.getColor() == Color.BLUE ? "bleu"
                : this.getColor() == Color.GREEN ? "vert" : "jaune") + " exécute l'action EXPLORE.");

        fleetsToMove = switch (playersChoosingExplore) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };
        ArrayList<int[]> doNotUse = new ArrayList<>();
        while (fleetsToMove > 0) {
            ArrayList<Integer> myHexIds = new ArrayList<>();
            for (Hex hex : this.getControlledHex(this)) {
                if (containsArray(doNotUse, new int[] { hex.getIdSector(), hex.getId() })) {
                    continue;
                }
                myHexIds.add(Hex.findIndex(Hex.plateau, new int[] { hex.getIdSector(), hex.getId() }));
            }
            if (myHexIds.size() == 0) {
                System.out.println("Plus aucun vaisseaux valable");
                break;
            }
            int startHexId = -1;
            int targetHexId = -1;
            if (this.getAdjacentsOpponent(myHexIds) != null) {
                startHexId = this.getAdjacentsOpponent(myHexIds)[0];

            } else {
                startHexId = myHexIds.get(0);
            }
            for (int i = 0; i < 2; i++) {
                int nbShipsMoving = partie.sector[Hex.plateau[startHexId][0]].hex[Hex.plateau[startHexId][1]]
                        .getShips()
                        .size();
                ArrayList<Integer> startListHex = new ArrayList<Integer>();
                startListHex.add(startHexId);
                if (getAdjacentsOpponent(startListHex) != null) {
                    targetHexId = this.getAdjacentsOpponent(startListHex)[1];

                    // Réduit le nombre de flottes restantes
                    moveFleet(startHexId, targetHexId, nbShipsMoving);
                    if (targetHexId == 24) {
                        doNotUse.add(Hex.plateau[targetHexId]);
                        break;
                    }

                } else {
                    targetHexId = this.exploreRandom(1, doNotUse, 1, startHexId);
                }
                startHexId = targetHexId;
                if (i == 1) {
                    doNotUse.add(Hex.plateau[targetHexId]);
                }
            }
            fleetsToMove--;
        }
        partie = Partie.getInstance();
        partie.closeImage();
        partie.affichagePlateau();
        System.out.println("Exploration terminée.");
    }

    public Sector chooseSector(ArrayList<Sector> cardsChosen) {
        Partie partie = Partie.getInstance();
        int maxPoints = -1;
        int nSecChosen=-1;
        Sector ans = new Sector(null);
        for (int i = 0; i < 9; i++) {
            if (cardsChosen.contains(partie.sector[i]) || i==4 || !partie.sectorIsTaken(partie.sector[i])){
                continue;
            }
            int nPoint = 0;
            for (int j = 0; j < 6; j++) {
                if (!partie.sector[i].hex[j].getShips().isEmpty()) {
                    nPoint += partie.sector[i].hex[j].getPlanetContained();
                }
            }
            if (nPoint > maxPoints) {
                maxPoints = nPoint;
                ans = partie.sector[i];
                nSecChosen=i;
            }
        }
        System.out.println("Joueur " + (this.getColor() == Color.BLUE ? "bleu "
                    : this.getColor() == Color.GREEN ? "vert " : "jaune ")+" choisi le secteur n°"+(nSecChosen+1));
        return ans;
    }

    public ArrayList<Sector> scoreSector(ArrayList<Sector> cardsChosen, int coef) {
        for (int j = 0; j < (this.controlsTriPrime() && coef!=2 ? 2 : 1); j++) {
            if (cardsChosen.size() == Sector.nbSectorTaken()) {
                break;
            }
            Sector choix = this.chooseSector(cardsChosen);

            cardsChosen.add(choix);
            for (Hex hex : choix.hex) {
                if (!hex.getShips().isEmpty()) {
                    if (hex.getShips().get(0).joueur == this) {
                        this.ajouterScore(hex.getPlanetContained() * coef);
                    }
                }
            }
            
            System.out.println("Joueur " + (this.getColor() == Color.BLUE ? "bleu "
                    : this.getColor() == Color.GREEN ? "vert " : "jaune ") + "a "
                    + this.getScore() + " points");
        }
        return cardsChosen;
    }
}
