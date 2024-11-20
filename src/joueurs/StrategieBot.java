package joueurs;

import java.util.Scanner;

/**
 * Interface pour définir la stratégie d'un bot.
 */
public interface StrategieBot {
    void jouerTour(Joueur bot, Scanner scanner);
}
