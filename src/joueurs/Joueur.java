package joueurs;

import java.awt.Color;
import java.util.List;
import java.util.Scanner;
import plateau.Hex;

/**
 * La classe abstraite Joueur représente un joueur dans le jeu.
 * Elle contient des informations sur la couleur du joueur, le nombre de vaisseaux en réserve,
 * et les cartes de commande (stratégies) du joueur.
 */
public abstract class Joueur {

    /**
     * La couleur associée au joueur.
     */
    private Color color;

    /**
     * Le score du joueur.
     */
    private  int score;

    /**
     * Le nombre de vaisseaux en réserve pour le joueur.
     */
    private int nbShipsSupply=15;

    /**
     * Les cartes de commande (stratégies) du joueur.
     */
    private CommandCards[] strat={null,null,null};

    /**
     * Les secteurs contrôlés par le joueur.
     */
    protected List<Hex> controlledHexes;

    /**
     * La stratégie actuelle du joueur.
     */
    private StrategieJoueur strategie;

    /**
     * Constructeur de la classe Joueur.
     *
     * @param color La couleur associée au joueur.
     */
    public Joueur(Color color) {
        this.color = color;
        this.score = 0;
        this.controlledHexes = controlledHexes;
    }

    /**
     * Exécute l'action EXPAND, représentant l'extension du joueur dans les hexagones qu'il contrôle.
     * Permet au joueur de choisir les hexagones pour y ajouter des vaisseaux.
     *
     * @param playersChoosingExpand Le nombre de joueurs ayant choisi l'action EXPAND ce round.
     * @param scanner Le scanner pour lire les entrées de l'utilisateur.
     */
    public void expand(int playersChoosingExpand, Scanner scanner) {
        int shipsToAdd;

        // Détermine le nombre de vaisseaux à ajouter en fonction du nombre de joueurs ayant choisi EXPAND
        if (playersChoosingExpand == 1) {
            shipsToAdd = 3;
        } else if (playersChoosingExpand == 2) {
            shipsToAdd = 2;
        } else {
            shipsToAdd = 1;
        }

        System.out.println("Exécution de l'action EXPAND : vous pouvez ajouter " + shipsToAdd + " vaisseaux aux hexagones contrôlés.");

        // Vérifie si le joueur a suffisamment de vaisseaux dans sa réserve
        if (nbShipsSupply < shipsToAdd) {
            System.out.println("Vous n'avez pas assez de vaisseaux en réserve.");
            shipsToAdd = nbShipsSupply; // Limite à la réserve disponible
        }

        // Demande au joueur de choisir les hexagones où il souhaite placer les vaisseaux
        while (shipsToAdd > 0) {
            System.out.println("Hexagones contrôlés :");
            for (Hex hex : controlledHexes) {
                System.out.println("Hex ID: " + hex.getId() + " - Planète Contenue: " + hex.getPlanetContained());
            }

            System.out.println("Entrez l'ID de l'hexagone où vous souhaitez ajouter un vaisseau : ");
            int hexId = scanner.nextInt();

            // Trouve l'hexagone correspondant
            Hex selectedHex = findControlledHexById(hexId);

            if (selectedHex != null) {
                selectedHex.addShips(1, this); // Ajoute un vaisseau dans l'hexagone
                nbShipsSupply--; // Réduit la réserve
                shipsToAdd--; // Réduit le nombre de vaisseaux restants à ajouter
                System.out.println("Un vaisseau a été ajouté à l'hexagone ID: " + hexId);
            } else {
                System.out.println("Hexagone non valide ou non contrôlé. Veuillez choisir un hexagone valide.");
            }
        }
    }

    /**
     * Trouve un hexagone contrôlé par son ID.
     *
     * @param id L'ID de l'hexagone.
     * @return L'hexagone correspondant, ou null s'il n'est pas trouvé.
     */
    private Hex findControlledHexById(int id) {
        for (Hex hex : controlledHexes) {
            if (hex.getId() == id) {
                return hex;
            }
        }
        return null;
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
     * Définit la stratégie actuelle du joueur.
     *
     * @param strategie La stratégie à utiliser.
     */
    public void setStrategie(StrategieJoueur strategie) {
        this.strategie = strategie;
    }

    /**
     * Exécute une action selon la stratégie du joueur.
     *
     * @param action L'action à exécuter.
     */
    public void jouerAction(CommandCards action) {
        if (strategie != null) {
            strategie.executerAction(action);
        } else {
            System.out.println("Aucune stratégie définie pour ce joueur.");
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
}
