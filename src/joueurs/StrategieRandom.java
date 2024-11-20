package joueurs;

import java.util.Random;
import java.util.Scanner;

/**
 * Implémentation de la stratégie aléatoire pour un bot.
 */
public class StrategieRandom implements StrategieBot {

    @Override
    public void jouerTour(Joueur bot, Scanner scanner) {
        Random random = new Random();

        // Choisir une action aléatoire
        int action = random.nextInt(3); // 0 = expand, 1 = explore, 2 = exterminate
        CommandCards actionChoisie = CommandCards.values()[action];

        System.out.println("BotRandom a choisi l'action : " + actionChoisie);

        // Simuler le nombre de joueurs ayant choisi la même action (aléatoire entre 1 et 3)
        int playersChoosingSameAction = random.nextInt(3) + 1;

        // Exécuter l'action correspondante
        switch (actionChoisie) {
            case EXPAND -> bot.expand(playersChoosingSameAction, scanner);
            case EXPLORE -> bot.explore(playersChoosingSameAction, scanner);
            case EXTERMINATE -> bot.exterminate(playersChoosingSameAction, scanner);
        }
    }
}
