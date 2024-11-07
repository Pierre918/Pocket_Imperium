package joueurs;

import java.awt.Color;
import java.util.Scanner;

/**
 * La classe VraiJoueur représente un joueur humain dans le jeu. Elle étend la classe Joueur
 * et permet au joueur de choisir sa stratégie en sélectionnant trois cartes de commande.
 */
public class VraiJoueur extends Joueur {
    /**
     * Vérifie si une carte de commande a déjà été choisie dans la stratégie.
     *
     * @param strat le tableau de cartes de commande représentant la stratégie du joueur
     * @param elt la carte de commande à vérifier
     * @return true si la carte de commande a déjà été choisie, false sinon
     */
    private boolean hasAlreadyBeenChosen(CommandCards[] strat,CommandCards elt){
        for (int i=0;i<3;i++){
            if (strat[i]==elt){
                return true;
            }
        }
        return false;
    }
    /**
     * Permet au joueur de choisir sa stratégie en sélectionnant trois cartes de commande.
     *
     * @param scanner le scanner utilisé pour lire les entrées de l'utilisateur
     */
    public void chooseStrat(Scanner scanner) {
        CommandCards[] strat={null,null,null};
        int choix;
        for (int i = 0; i < 3; i++) {
            choix=-1;
            while (choix < 1 || choix > 3) {
                System.out.println("choisissez votre "+(i+1)+"e carte : (expand : 1/explore : 2/exterminate : 3)");
                choix = Integer.parseInt(scanner.nextLine());
                if (hasAlreadyBeenChosen(strat,choix==1?CommandCards.EXPAND:(choix==2?CommandCards.EXPLORE:CommandCards.EXTERMINATE))){
                    choix=-1;
                    System.out.println("Vous avez déjà choisi cette carte");
                }
            }
            strat[i]=choix==1?CommandCards.EXPAND:(choix==2?CommandCards.EXPLORE:CommandCards.EXTERMINATE);
        }
        this.setStrat(strat);
    }
    /**
     * Constructeur de la classe VraiJoueur.
     *
     * @param color la couleur du joueur
     */
    public VraiJoueur(Color color) {
        super(color);
    }
}
