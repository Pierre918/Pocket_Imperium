package joueurs;

import java.awt.Color;
import java.util.ArrayList;

import java.util.Scanner;
import partie.Partie;
import plateau.Hex;

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
     * @param scanner               Le scanner pour lire les entrées de
     *                              l'utilisateur.
     */
    public abstract void expand(int playersChoosingExpand, Scanner scanner);

    /**
     * Exécute l'action EXPLORE, permettant au joueur de déplacer des flottes vers
     * des hexagones adjacents.
     *
     * @param playersChoosingExplore Le nombre de joueurs ayant choisi l'action
     *                               EXPLORE ce round.
     * @param scanner                Le scanner pour lire les entrées de
     *                               l'utilisateur.
     */
    public abstract void explore(int playersChoosingExplore, Scanner scanner);

    /**
     * Déplace une flotte vers un nouvel hexagone.
     *
     * @param startHex    L'hexagone de départ.
     * @param targetHexId L'ID de l'hexagone cible.
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
     * Vérifie si l'hexagone dont l'id est spécifié est contrôlé par le joueur en paramètre
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
     * @param i
     * @param j
     * @param scanner
     */
    public abstract void initialDeployment(Integer i, Integer j, Scanner scanner);

    /**
     * Exécute l'action EXTERMINATE, permettant au joueur d'envahir des systèmes
     * adjacents.
     *
     * @param playersChoosingExterminate Le nombre de joueurs ayant choisi l'action
     *                                   EXTERMINATE ce round.
     * @param scanner                    Le scanner pour lire les entrées de
     *                                   l'utilisateur.
     */
    public abstract void exterminate(int playersChoosingExterminate, Scanner scanner);

    /**
     * Permet l'invasion d'un système par un joueur. Est utilisé la méthode exterminate implémentée par les classes 
     * filles de Joueur
     *
     * @param startHex    L'hexagone de départ.
     * @param targetHexId L'ID de l'hexagone cible.
     * @param shipsToUse  Le nombre de vaisseaux utilisés pour l'invasion.
     */
    protected void invade(Hex startHex, int targetHexId, int shipsToUse) {
        Partie partie = Partie.getInstance();

        // Supprime le nombre de vaisseaux choisi de l'hexagone de départ
        partie.sector[startHex.getIdSector()].hex[startHex.getId()].deleteShips(shipsToUse);

        int attackerShips = shipsToUse; // Vaisseaux attaquants
        int defenderShips = partie.sector[Hex.plateau[targetHexId][0]].hex[Hex.plateau[targetHexId][1]].getShips()
                .size(); // Vaisseaux défensifs

        System.out.println(
                "Attaquant : " + attackerShips + " vaisseau(x), Défenseur : " + defenderShips + " vaisseau(x)");

        int minShips = Math.min(attackerShips, defenderShips);

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
    protected boolean isHexAdjacent(Hex startHex, int targetHexId) {
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
     * @param scanner Le scanner pour lire les entrées utilisateur.
     */
    public abstract void chooseStrat(Scanner scanner);

    /**
     * Exécute une action selon la stratégie du joueur.
     * @param i Indique le numéro du tour joué
     * @param count
     * @param scanner
     */
    public void jouerAction(int i, int count, Scanner scanner) {
        if (this.strat[i] == CommandCards.EXPAND) {
            expand(count, scanner);
        } else if (this.strat[i] == CommandCards.EXPLORE) {
            explore(count, scanner);
        } else if (this.strat[i] == CommandCards.EXTERMINATE) {
            exterminate(count, scanner);
        }
    }

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

    /**
     * Permet d'obtenir tous les hexagones contrôlés par le joueur
     * 
     * @param joueur
     * @return
     */
    public ArrayList<Hex> getControlledHex(Joueur joueur) {
        Partie partie = Partie.getInstance();
        ArrayList<Hex> ControlledHexs = new ArrayList<Hex>();
        for (int i = 0; i < partie.sector.length; i++) {
            for (int j = 0; j < partie.sector[i].hex.length; j++) {
                if (!partie.sector[i].hex[j].getShips().isEmpty()) {
                    if (partie.sector[i].hex[j].getShips().get(0).joueur == joueur) {
                        ControlledHexs.add(partie.sector[i].hex[j]);
                    }
                }

            }
        }
        return ControlledHexs;
    }

    /**
     * Permet d'obtenir le nombre de personnes qui jouent la même carte que le joueur
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
     * Manipule les méthodes expand, explore et exterminate selon la stratégie définie par le joueur.
     * @param n_tour
     * @param scanner
     */
    public void jouerTour(int n_tour, Scanner scanner) {
        int conflictCount = numberSameCard(n_tour, this.strat[n_tour]);
        if (this.strat[n_tour] == CommandCards.EXPAND) {
            this.expand(conflictCount, scanner);
            return;
        }
        if (this.strat[n_tour] == CommandCards.EXPLORE) {
            this.explore(conflictCount, scanner);
            return;
        }
        if (this.strat[n_tour] == CommandCards.EXTERMINATE) {
            this.exterminate(conflictCount, scanner);
            return;
        }
    }
}
