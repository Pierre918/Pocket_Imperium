package joueurs;

import java.awt.Color;
import java.util.List;
import java.util.Scanner;
import plateau.Hex;

/**
 * Bot aléatoire qui agit comme un joueur dans un tour, sans stratégie spécifique.
 */
public class BotRandom extends Joueur {

    private StrategieBot strategie;

    /**
     * Constructeur de la classe BotRandom.
     *
     * @param color La couleur associée au bot.
     * @param controlledHexes La liste des hexagones contrôlés par le bot.
     */
    public BotRandom(Color color, List<Hex> controlledHexes) {
        super(color, controlledHexes);
        this.strategie = new StrategieRandom(); // Stratégie aléatoire par défaut
    }

    /**
     * Permet de définir une stratégie pour le bot.
     *
     * @param strategie La stratégie à utiliser.
     */
    public void setStrategie(StrategieBot strategie) {
        this.strategie = strategie;
    }

    /**
     * Le bot choisit une stratégie pour le tour.
     * Ici, cela n'a pas de sens, mais est requis par la classe mère.
     */
    @Override
    public void chooseStrat(Scanner scanner) {
        System.out.println("BotRandom ne choisit pas de stratégie manuellement.");
    }

    /**
     * Exécute les actions du bot pour le tour.
     *
     * @param scanner Le scanner utilisé pour les entrées utilisateur (simulé pour le bot).
     */
    public void jouerTour(Scanner scanner) {
        if (strategie != null) {
            strategie.jouerTour(this, scanner);
        } else {
            System.out.println("Aucune stratégie définie pour le bot.");
        }
    }
}
