package joueurs;

import java.awt.Color;
import java.util.List;
import java.util.Scanner;
import plateau.Hex;


/**
 * La classe VraiJoueur représente un joueur humain dans le jeu. Elle étend la classe Joueur
 * et permet au joueur de choisir sa stratégie en sélectionnant trois cartes de commande.
 */
public class VraiJoueur extends Joueur {

    /**
     * Constructeur de la classe VraiJoueur.
     *
     * @param color La couleur du joueur.
     * @param controlledHexes La liste des hexagones contrôlés par le joueur.
     */
    public VraiJoueur(Color color, List<Hex> controlledHexes) {
        super(color, controlledHexes);
    }

    /**
     * Permet au joueur de choisir l'ordre de ses cartes de commande pour le round.
     *
     * @param scanner Le scanner utilisé pour lire les entrées de l'utilisateur.
     */
    @Override
    public void chooseStrat(Scanner scanner) {
        CommandCards[] strat = {null, null, null};
        int choix;
        for (int i = 0; i < 3; i++) {
            choix = -1;
            while (choix < 1 || choix > 3) {
                System.out.println("Choisissez votre " + (i + 1) + "e carte pour le round (expand : 1 / explore : 2 / exterminate : 3)");
                choix = Integer.parseInt(scanner.nextLine());
                
                CommandCards carteChoisie = choix == 1 ? CommandCards.EXPAND 
                                      : choix == 2 ? CommandCards.EXPLORE 
                                      : CommandCards.EXTERMINATE;
                
                if (hasAlreadyBeenChosen(strat, carteChoisie)) {
                    choix = -1;
                    System.out.println("Vous avez déjà choisi cette carte pour ce round.");
                }
            }
            strat[i] = choix == 1 ? CommandCards.EXPAND : choix == 2 ? CommandCards.EXPLORE : CommandCards.EXTERMINATE;
        }
        this.setStrat(strat); 
    }

    /**
     * Vérifie si une carte de commande a déjà été choisie dans la stratégie.
     *
     * @param strat Le tableau de cartes de commande représentant la stratégie du joueur.
     * @param elt La carte de commande à vérifier.
     * @return true si la carte de commande a déjà été choisie, false sinon.
     */
    private boolean hasAlreadyBeenChosen(CommandCards[] strat, CommandCards elt) {
        for (CommandCards card : strat) {
            if (card == elt) {
                return true;
            }
        }
        return false;
    }
}
