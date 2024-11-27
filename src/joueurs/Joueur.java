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
    public void explore(int playersChoosingExplore, Scanner scanner) {
        int fleetsToMove;
        System.out.println("Joueur " + (this.getColor() == Color.BLUE ? "bleu"
                : this.getColor() == Color.GREEN ? "vert" : "jaune") + " exécute l'action EXPLORE.");
        // Détermine le nombre de flottes pouvant être déplacées en fonction des joueurs
        // ayant choisi Explore
        fleetsToMove = switch (playersChoosingExplore) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        System.out.println("Action EXPLORE : vous pouvez déplacer " + fleetsToMove
                + " flottes, chaque flotte pouvant se déplacer de 2 hexagones.");

        // Déplacement des flottes
        while (fleetsToMove > 0) {
            System.out.println("Hexagones contrôlés :");
            for (Hex hex : getControlledHex()) {
                System.out.println("Hex ID: " + hex.getId() + " - Nombre de Vaisseaux: " + hex.getShips().size());
            }

            System.out.println("Entrez l'ID de l'hexagone de départ de la flotte : ");
            int startHexId = scanner.nextInt();

            // Trouve l'hexagone de départ
            Hex startHex = findControlledHexById(startHexId);
            if (startHex == null) {
                System.out.println("Hexagone non contrôlé ou non valide. Veuillez réessayer.");
                continue;
            }

            // Vérifie s'il y a des vaisseaux dans l'hexagone de départ
            if (startHex.getShips().isEmpty()) {
                System.out.println("Aucun vaisseau dans cet hexagone. Déplacement annulé.");
                continue;
            }

            System.out.println("Entrez l'ID de l'hexagone cible (1er déplacement) : ");
            int targetHexId = scanner.nextInt();

            // Vérifie si l'hexagone cible est adjacent et non occupé par un autre joueur
            if (!isHexAdjacentAndFree(startHex, targetHexId)) {
                System.out.println("L'hexagone cible n'est pas valide (pas adjacent ou occupé). Déplacement annulé.");
                continue;
            }

            System.out.println(
                    "Souhaitez-vous effectuer un deuxième déplacement pour cette flotte ? (oui : 1 / non : 0) : ");
            int secondMove = scanner.nextInt();

            if (secondMove == 1) {
                System.out.println("Entrez l'ID de l'hexagone pour le 2ème déplacement : ");
                int secondTargetHexId = scanner.nextInt();

                // Vérifie le 2ème hexagone
                if (!isHexAdjacentAndFree(startHex, secondTargetHexId)) {
                    System.out
                            .println("L'hexagone cible du deuxième déplacement n'est pas valide. Déplacement annulé.");
                    continue;
                }

                // Ajouter le 2ème déplacement
                moveFleet(startHex, secondTargetHexId);
            } else {
                moveFleet(startHex, targetHexId); // Déplacement simple
            }

            fleetsToMove--; // Réduit le nombre de flottes restantes
        }

        System.out.println("Exploration terminée.");
    }

    /**
     * Déplace une flotte vers un nouvel hexagone.
     *
     * @param startHex    L'hexagone de départ.
     * @param targetHexId L'ID de l'hexagone cible.
     */
    private void moveFleet(Hex startHex, int targetHexId) {
        System.out.println(
                "Déplacement de flotte de l'hexagone ID: " + startHex.getId() + " vers l'hexagone ID: " + targetHexId);

        // Supprime un vaisseau de l'hexagone de départ
        startHex.deleteShips(1);

        // Simule l'exploration ou la prise de contrôle
        Hex targetHex = new Hex(0); // Crée un nouvel hexagone si non existant
        targetHex.setId(targetHexId);
        targetHex.addShips(1, this); // Ajoute un vaisseau au nouvel hexagone

        System.out.println("Flotte déplacée avec succès.");
    }

    /**
     * Vérifie si un hexagone cible est adjacent et non occupé par un autre joueur.
     *
     * @param startHex    L'hexagone de départ.
     * @param targetHexId L'ID de l'hexagone cible.
     * @return true si l'hexagone est valide, false sinon.
     */
    private boolean isHexAdjacentAndFree(Hex startHex, int targetHexId) {
        int[][] adjacents = startHex.getAdjacents();
        for (int[] adjacent : adjacents) {
            if (adjacent[1] == targetHexId) {
                return true; // Vérifie si l'hexagone cible est adjacent
            }
        }
        return false; // Pas adjacent ou occupé
    }

    /**
     * Trouve un hexagone contrôlé par son ID.
     *
     * @param id L'ID de l'hexagone.
     * @return L'hexagone correspondant, ou null s'il n'est pas trouvé.
     */
    protected boolean isHexControlledByMe(int id, Joueur joueur) {
        for (Hex hex : getControlledHex(joueur)) {
            if (Hex.findIndex(Hex.plateau, new int[] {hex.getIdSector(), hex.getId()}) == id) {
                return true;
            }
        }
        return false;
    }

    public abstract void initialDeployment(Integer i, Integer j, Scanner scanner) ;

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
     * Simule une invasion d'un système par le joueur.
     *
     * @param startHex    L'hexagone de départ.
     * @param targetHexId L'ID de l'hexagone cible.
     * @param shipsToUse  Le nombre de vaisseaux utilisés pour l'invasion.
     */
    protected void invade(Hex startHex, int targetHexId, int shipsToUse) {
        Partie partie = Partie.getInstance();
        
        // Récupérer l'hexagone cible (simulation d'un système non contrôlé)

        // Supprime le nombre de vaisseaux choisi de l'hexagone de départ
        partie.sector[startHex.getIdSector()].hex[startHex.getId()].deleteShips(shipsToUse);

        // Simuler la résolution de l'invasion
        int attackerShips = shipsToUse; // Vaisseaux attaquants
        int defenderShips = partie.sector[Hex.plateau[targetHexId][0]].hex[Hex.plateau[targetHexId][1]].getShips().size(); // Vaisseaux défensifs

        System.out.println(
                "Attaquant : " + attackerShips + " vaisseau(x), Défenseur : " + defenderShips + " vaisseau(x)");

        int minShips = Math.min(attackerShips, defenderShips);
        
        partie.sector[Hex.plateau[targetHexId][0]].hex[Hex.plateau[targetHexId][1]].deleteShips(minShips);

        // Résultat de l'invasion
        if (attackerShips > defenderShips) {
            // Attaquant gagne
            System.out.println("L'attaquant contrôle maintenant l'hexagone.");
            partie.sector[Hex.plateau[targetHexId][0]].hex[Hex.plateau[targetHexId][1]].addShips(attackerShips - minShips, this); // Ajouter les vaisseaux restants
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
     *
     */
    public void jouerAction(int i, int count, Scanner scanner) {
        Partie partie = Partie.getInstance();
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

    public void jouerTour(int n_tour,Scanner scanner) {
        int conflictCount=numberSameCard(n_tour, this.strat[n_tour]);
        if (this.strat[n_tour]==CommandCards.EXPAND){
            this.expand(conflictCount, scanner);
            return;
        }
        if (this.strat[n_tour]==CommandCards.EXPLORE){
            this.explore(conflictCount, scanner);
            return;
        }
        if (this.strat[n_tour]==CommandCards.EXTERMINATE){
            this.exterminate(conflictCount, scanner);
            return;
        }
    }
}
