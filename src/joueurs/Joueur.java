package joueurs;

import java.awt.Color;
import java.util.Scanner;

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
     * Le nombre de vaisseaux en réserve pour le joueur.
     */
    private int nbShipsSupply=15;
    /**
     * Les cartes de commande (stratégies) du joueur.
     */
    private CommandCards[] strat={null,null,null};
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
     * Constructeur de la classe Joueur.
     *
     * @param color La couleur associée au joueur.
     */
    public Joueur(Color color){
        this.color= color;
    }
}
