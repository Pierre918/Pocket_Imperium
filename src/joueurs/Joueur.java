package joueurs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import partie.Partie;
import plateau.Hex;
import plateau.Sector;

/**
 * La classe abstraite Joueur représente un joueur dans le jeu.
 * Elle contient des informations sur la couleur du joueur, le nombre de
 * vaisseaux en réserve,
 * et les cartes de commande (stratégies) du joueur.
 */
public abstract class Joueur {

    /**
     * La couleur associée au joueur.
     */
    private final Color color;

    /**
     * Le score du joueur.
     */
    private int score;

    /**
     * Le nombre de vaisseaux en réserve pour le joueur.
     */
    protected int nbShipsSupply = 15;

    /**
     * Les cartes de commande (stratégies) du joueur.
     */
    protected CommandCards[] strat = { null, null, null };

    /**
     * Constructeur de la classe Joueur.
     *
     * @param color La couleur associée au joueur.
     */
    public Joueur(Color color) {
        this.color = color;
        this.score = 0;
    }

    /**
     * Retourne les cartes de commande (stratégies) du joueur.
     *
     * @return Un tableau de cartes de commande.
     */
    public CommandCards[] getCommandCards() {
        return strat;
    }

    /**
     * Exécute l'action EXPAND, représentant l'extension du joueur dans les
     * hexagones qu'il contrôle.
     * Permet au joueur de choisir les hexagones pour y ajouter des vaisseaux.
     *
     * @param playersChoosingExpand Le nombre de joueurs ayant choisi l'action
     *                              EXPAND ce round.
     */
    public abstract void expand(int playersChoosingExpand);

    /**
     * Exécute l'action EXPLORE, permettant au joueur de déplacer des flottes vers
     * des hexagones adjacents.
     *
     * @param playersChoosingExplore Le nombre de joueurs ayant choisi l'action
     *                               EXPLORE ce round.
     */
    public abstract void explore(int playersChoosingExplore);

    /**
     * Déplace une flotte vers un nouvel hexagone.
     *
     * @param startHexId    L'hexagone de départ.
     * @param targetHexId L'ID de l'hexagone cible.
     * @param nbShipsMoving 
     */
    protected void moveFleet(int startHexId, int targetHexId, int nbShipsMoving) {
        Partie partie = Partie.getInstance();
        System.out.println(
                "Déplacement de flotte de l'hexagone ID: " + startHexId + " vers l'hexagone ID: " + targetHexId);
        for (int i = 0; i < nbShipsMoving; i++)
        // Supprime un vaisseau de l'hexagone de départ
        {
            partie.sector[Hex.plateau[startHexId][0]].hex[Hex.plateau[startHexId][1]].deleteShips(1);

            partie.sector[Hex.plateau[targetHexId][0]].hex[Hex.plateau[targetHexId][1]].addShips(1, this);
        } // Ajoute un
          // vaisseau au
          // nouvel
          // hexagone

        System.out.println("Flotte déplacée avec succès.");
    }

    /**
     * Méthodes permettant à tous les bots de l'utiliser
     * Choisi un hexagone contrôlé depuis lequel mener l'invasion de manière
     * aléatoire
     * Choisi également un hexagone adjacent et un nombre de vaisseaux pour
     * attaquer.
     * 
     * @param systemsToInvade
     * @param doNotUse        hexagones a ne pas utilisé car contiennent des
     *                        vaisseaux déjà utilisés
     * @return
     */
    public int exterminateRandom(int systemsToInvade, ArrayList<int[]> doNotUse) {
        Partie partie = Partie.getInstance();

        // Détermine le nombre de systèmes pouvant être envahis en fonction des joueurs
        // ayant choisi Exterminate
        int invadeHexId = -1;
        // Gérer les invasions

        while (systemsToInvade > 0) {
            ArrayList<Integer> allMyHexs = new ArrayList<>();
            for (Hex hex : this.getControlledHex(this)) {
                allMyHexs.add(Hex.findIndex(Hex.plateau, new int[] { hex.getIdSector(), hex.getId() }));

            }
            ArrayList<Integer> myHexIds = new ArrayList<>();
            for (Hex hex : this.getControlledHex(this)) {
                if (!containsArray(doNotUse, new int[] { hex.getIdSector(), hex.getId() })) {
                    myHexIds.add(Hex.findIndex(Hex.plateau, new int[] { hex.getIdSector(), hex.getId() }));
                }
            }
            if (myHexIds.size() == 0) {
                break;
            }
            Collections.shuffle(myHexIds);
            Hex hexChoisi = partie.sector[Hex.plateau[myHexIds.get(0)][0]].hex[Hex.plateau[myHexIds.get(0)][1]];
            Random random = new Random();
            int nbShipsMoving = hexChoisi
                    .getShips().size() == 1 ? 1
                            : (random
                                    .nextInt(hexChoisi
                                            .getShips().size())
                                    + 1);

            invadeHexId = myHexIds.get(0);

            while (allMyHexs.contains(invadeHexId)) {
                invadeHexId = Hex.findIndex(Hex.plateau,
                        hexChoisi.getAdjacents()[random.nextInt(hexChoisi.getAdjacents().length)]);
            }
            System.out.println("Le bot envahie l'hexagone n°" + invadeHexId + "avec " + nbShipsMoving + " vaisseaux");
            doNotUse.add(Hex.plateau[invadeHexId]);
            this.invade(hexChoisi, invadeHexId, nbShipsMoving);
            // Réduire le nombre de systèmes restants à envahir
            systemsToInvade--;
        }
        return invadeHexId;
    }

    /** 
     * Permet de savoir si listOfArrays contient targetArray
     * @param listOfArrays
     * @param targetArray
     */
    public static boolean containsArray(List<int[]> listOfArrays, int[] targetArray) {
        for (int[] array : listOfArrays) {
            if (Arrays.equals(array, targetArray)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si un hexagone cible est adjacent et non occupé par un autre joueur.
     *
     * @param startHex    L'hexagone de départ.
     * @param targetHexId L'ID de l'hexagone cible.
     * @return true si l'hexagone est valide, false sinon.
     */
    protected boolean isHexAdjacentAndFree(int startHexId, int targetHexId) {
        Partie partie = Partie.getInstance();
        Hex startHex = partie.sector[Hex.plateau[startHexId][0]].hex[Hex.plateau[startHexId][1]];
        int[][] adjacents = startHex.getAdjacents();
        for (int[] adjacent : adjacents) {
            if (adjacent[0] == Hex.plateau[targetHexId][0] && adjacent[1] == Hex.plateau[targetHexId][1]) {
                if (!partie.sector[Hex.plateau[targetHexId][0]].hex[Hex.plateau[targetHexId][1]].getShips().isEmpty()) {
                    if (partie.sector[Hex.plateau[targetHexId][0]].hex[Hex.plateau[targetHexId][1]].getShips()
                            .get(0).joueur == this) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false; // Pas adjacent ou occupé
    }

    /**
     * Vérifie si l'hexagone dont l'id est spécifié est contrôlé par le joueur en
     * paramètre
     *
     * @param id L'ID de l'hexagone.
     * @return L'hexagone correspondant, ou null s'il n'est pas trouvé.
     */
    protected boolean isHexControlledByMe(int id, Joueur joueur) {
        for (Hex hex : getControlledHex(joueur)) {
            if (Hex.findIndex(Hex.plateau, new int[] { hex.getIdSector(), hex.getId() }) == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Permet de faire le déploiement initial du jeu.
     * 
     * @param i
     * @param j
     */
    public abstract void initialDeployment(Integer i, Integer j);

    /**
     * Exécute l'action EXTERMINATE, permettant au joueur d'envahir des systèmes
     * adjacents.
     *
     * @param playersChoosingExterminate Le nombre de joueurs ayant choisi l'action
     *                                   EXTERMINATE ce round.
     */
    public abstract void exterminate(int playersChoosingExterminate);

    public static void backToSupply(int hexId, int nbShip){
        Partie partie = Partie.getInstance();
        for (int i=0;i<3;i++){
            if(partie.joueurs.get(i)==partie.sector[Hex.plateau[hexId][0]].hex[Hex.plateau[hexId][1]].getShips().get(0).joueur){
                partie.joueurs.get(i).nbShipsSupply+=nbShip;
            }
        }
    }
    /**
     * Permet l'invasion d'un système par un joueur. Est utilisé la méthode
     * exterminate implémentée par les classes
     * filles de Joueur
     *
     * @param startHex    L'hexagone de départ.
     * @param targetHexId L'ID de l'hexagone cible.
     * @param shipsToUse  Le nombre de vaisseaux utilisés pour l'invasion.
     */
    protected void invade(Hex startHex, int targetHexId, int shipsToUse) {
        Partie partie = Partie.getInstance();
        
        int attackerShips = shipsToUse; // Vaisseaux attaquants
        int defenderShips = partie.sector[Hex.plateau[targetHexId][0]].hex[Hex.plateau[targetHexId][1]].getShips()
                .size(); // Vaisseaux défensifs


        int minShips = Math.min(attackerShips, defenderShips);

        if (!partie.sector[Hex.plateau[targetHexId][0]].hex[Hex.plateau[targetHexId][1]].getShips().isEmpty()){
            backToSupply(targetHexId,minShips);
            backToSupply(Hex.findIndex(Hex.plateau, new int[] {startHex.getIdSector(),startHex.getId()}),minShips);
        }
        // Supprime le nombre de vaisseaux choisi de l'hexagone de départ
        partie.sector[startHex.getIdSector()].hex[startHex.getId()].deleteShips(shipsToUse);

        partie.sector[Hex.plateau[targetHexId][0]].hex[Hex.plateau[targetHexId][1]].deleteShips(minShips);

        // Résultat de l'invasion
        if (attackerShips > defenderShips) {
            // Attaquant gagne
            System.out.println("L'attaquant contrôle maintenant l'hexagone.");
            partie.sector[Hex.plateau[targetHexId][0]].hex[Hex.plateau[targetHexId][1]]
                    .addShips(attackerShips - minShips, this); // Ajouter les vaisseaux restants
        } else if (attackerShips < defenderShips) {
            // Défenseur gagne
            System.out.println("Le défenseur garde le contrôle de l'hexagone.");
        } else {
            // Système reste inoccupé
            System.out.println("Le système est maintenant inoccupé.");
        }
        partie.closeImage();
        partie.affichagePlateau();
    }

    /**
     * Vérifie si un hexagone cible est adjacent à un hexagone contrôlé.
     *
     * @param startHex    L'hexagone de départ.
     * @param targetHexId L'ID de l'hexagone cible.
     * @return true si l'hexagone cible est adjacent, false sinon.
     */
    protected boolean isHexAdjacents(Hex startHex, int targetHexId) {
        int[][] adjacents = startHex.getAdjacents();
        for (int[] adjacent : adjacents) {
            if (Hex.findIndex(Hex.plateau, adjacent) == targetHexId) {
                return true; // Hexagone adjacent
            }
        }
        return false; // Pas adjacent
    }

    /**
     * Retourne les cartes de commande (stratégies) du joueur.
     *
     * @return Un tableau de cartes de commande.
     */
    public CommandCards[] getStrat() {
        return strat;
    }

    /**
     * Définit les cartes de commande (stratégies) du joueur.
     *
     * @param strat Un tableau de cartes de commande.
     */
    public void setStrat(CommandCards[] strat) {
        this.strat = strat;
    }

    /**
     * Retourne le nombre de vaisseaux en réserve pour le joueur.
     *
     * @return Le nombre de vaisseaux en réserve.
     */
    public int getNbShipsSupply() {
        return nbShipsSupply;
    }

    /**
     * Définit le nombre de vaisseaux en réserve pour le joueur.
     *
     * @param nbShipsSupply Le nombre de vaisseaux en réserve.
     */
    public void setNbShipsSupply(int nbShipsSupply) {
        this.nbShipsSupply = nbShipsSupply;
    }

    /**
     * Retourne la couleur associée au joueur.
     *
     * @return La couleur du joueur.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Méthode abstraite pour choisir une stratégie.
     * Cette méthode doit être implémentée par les sous-classes.
     *
     */
    public abstract void chooseStrat();

    /**
     * Retourne le score actuel du joueur.
     *
     * @return Le score du joueur.
     */
    public int getScore() {
        return score;
    }

    /**
     * Ajoute des points au score du joueur.
     *
     * @param points Le nombre de points à ajouter.
     */
    public void ajouterScore(int points) {
        this.score += points;
    }

    public boolean controlsTriPrime(){
        ArrayList<Hex> controlledHexs =this.getControlledHex(this);
        for (Hex hex: controlledHexs){
            if (hex.getIdSector()==4){
                return true;
            }
        }
        return false;
    }

    /**
     * Permet d'obtenir tous les hexagones contrôlés par le joueur
     * 
     * @param joueur
     * @return
     */
    public ArrayList<Hex> getControlledHex(Joueur joueur) {
        Partie partie = Partie.getInstance();
        ArrayList<Hex> controlledHexs = new ArrayList<Hex>();
        for (int i = 0; i < partie.sector.length; i++) {
            for (int j = 0; j < partie.sector[i].hex.length; j++) {
                if (!partie.sector[i].hex[j].getShips().isEmpty()) {
                    if (partie.sector[i].hex[j].getShips().get(0).joueur == joueur) {
                        controlledHexs.add(partie.sector[i].hex[j]);
                    }
                }

            }
        }
        return controlledHexs;
    }

    /**
     * Permet d'obtenir le nombre de personnes qui jouent la même carte que le
     * joueur
     * 
     * @param n_tour
     * @param card
     * @return
     */
    public int numberSameCard(int n_tour, CommandCards card) {
        Partie partie = Partie.getInstance();
        int conflictCount = 1;

        // Vérifier les conflits pour cette action
        for (Joueur autreJoueur : partie.joueurs) {
            if (this != autreJoueur &&
                    card == autreJoueur.getCommandCards()[n_tour]) {
                conflictCount++;
            }
        }

        return conflictCount;

    }

    /**
     * Permet de vérifier si le joueur possède encore des bateaux en réserve
     * 
     * @return
     */
    public boolean hasJoueurShipsLeft() {
        for (Hex hex : this.getControlledHex(this)) {
            if (!hex.getShips().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Manipule les méthodes expand, explore et exterminate selon la stratégie
     * définie par le joueur.
     * 
     * @param n_tour
     */
    public void jouerTour(int n_tour) {
        int conflictCount = numberSameCard(n_tour, this.strat[n_tour]);
        if (!hasJoueurShipsLeft()) {
            System.out.println("Aucun vaisseaux");
            return;
        }
        if (this.strat[n_tour] == CommandCards.EXPAND) {
            this.expand(conflictCount);
            return;
        }
        if (this.strat[n_tour] == CommandCards.EXPLORE) {
            this.explore(conflictCount);
            return;
        }
        if (this.strat[n_tour] == CommandCards.EXTERMINATE) {
            this.exterminate(conflictCount);
            return;
        }
    }

    /**
     * Permet à tous les robots qui héritent de cette classe de pouvoir explorer de
     * manière aléatoire
     * Choisi aléatoirement un hexagone adjacent et un nombre de vaisseaux pour se
     * déplacer.
     * @param fleetsToMove
     * @param doNotUse     hexagone à ne pas utiliser car contiennent des bateaux
     *                     déjà utilisé
     * @param nbMoves      nombre de déplacement pour chaque flotte
     * @param startHexId   L'hexagone d'où partir. Est égal à -1 si aucun hexagone
     *                     de départ n'est définie
     * @return
     */
    public int exploreRandom(int fleetsToMove,
            ArrayList<int[]> doNotUse, int nbMoves, int startHexId) {
        Partie partie = Partie.getInstance();

        Random random = new Random();
        int targetHexId = -1;
        int fleetsToMoveAtStart = fleetsToMove;
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
            startHexId = (startHexId == -1 || fleetsToMoveAtStart != fleetsToMove)
                    ? myHexIds.get(random.nextInt(myHexIds.size()))
                    : startHexId;
            Hex startHex = partie.sector[Hex.plateau[startHexId][0]].hex[Hex.plateau[startHexId][1]];
            fleetsToMove--; // Réduit le nombre de flottes restantes
            for (int i = 0; i < nbMoves; i++) {

                do {
                    targetHexId = Hex.findIndex(Hex.plateau,
                            startHex.getAdjacents()[random.nextInt(startHex.getAdjacents().length)]);
                } while (!isHexAdjacentAndFree(startHexId, targetHexId));

                int nbShipsMoving = partie.sector[Hex.plateau[startHexId][0]].hex[Hex.plateau[startHexId][1]].getShips()
                        .size() == 1 ? 1
                                : (random.nextInt(
                                        partie.sector[Hex.plateau[startHexId][0]].hex[Hex.plateau[startHexId][1]]
                                                .getShips()
                                                .size())
                                        + 1);

                moveFleet(startHexId, targetHexId, nbShipsMoving);
                if (i == 1) {
                    doNotUse.add(Hex.plateau[targetHexId]);
                }
                if (targetHexId == 24) {
                    doNotUse.add(Hex.plateau[targetHexId]);
                    break;
                }
                startHexId = targetHexId;
            }

        }
        partie.closeImage();
        partie.affichagePlateau();
        return targetHexId;
    }

    /**
     * Méthode abstraite pour scorer les secteurs en fin de round pour chaque type de joueur
     * @param cardsChosen
     * @param coef
     * @return
     */
    public abstract ArrayList<Sector> scoreSector(ArrayList<Sector> cardsChosen, int coef);
}
