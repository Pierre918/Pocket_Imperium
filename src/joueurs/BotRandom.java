package joueurs;

import java.awt.Color;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import partie.Partie;
import plateau.Hex;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Bot aléatoire qui agit comme un joueur dans un tour, sans stratégie
 * spécifique.
 */
public class BotRandom extends Joueur {

    /**
     * Constructeur de la classe BotRandom.
     *
     * @param color La couleur associée au bot.
     */
    public BotRandom(Color color) {
        super(color);
    }

    /**
     * Le bot choisit une stratégie pour le tour.
     * Ici, cela n'a pas de sens, mais est requis par la classe mère.
     */
    @Override
    public void chooseStrat(Scanner scanner) {
        // Convertir les valeurs de l'énumération en une liste
        List<CommandCards> cardList = new ArrayList<>(List.of(CommandCards.values()));

        // Mélanger la liste
        Collections.shuffle(cardList);

        // Convertir la liste en un tableau
        this.strat = cardList.toArray(new CommandCards[0]);

        this.strat[0] = CommandCards.EXPAND;

        System.out.println("Le bot aléatoire " + (this.getColor() == Color.BLUE ? "bleu"
                : this.getColor() == Color.GREEN ? "vert" : "jaune") + " a choisi sa stratégie");
    }

    @Override
    public void expand(int playersChoosingExpand, Scanner scanner) {
        int shipsToAdd;
        shipsToAdd = switch (playersChoosingExpand) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };
        if (this.nbShipsSupply < shipsToAdd) {
            System.out.println("Il n'y a pas assez de vaisseaux en réserve.");
            shipsToAdd = this.nbShipsSupply; // Limite à la réserve disponible
        }
        System.out.println("Joueur " + (this.getColor() == Color.BLUE ? "bleu"
                : this.getColor() == Color.GREEN ? "vert" : "jaune") + " :\nExécute l'action EXPAND avec " + shipsToAdd
                + " vaisseaux.");
        Partie partie = Partie.getInstance();
        ArrayList<Integer> idControlledHexs = new ArrayList<>();
        for (Hex hex : this.getControlledHex(this)) {
            idControlledHexs.add(hex.getId());
        }
        Random random = new Random();

        while (shipsToAdd > 0) {
            System.out.println(idControlledHexs);
            System.out.println(idControlledHexs.size());
            Integer randomIndex = random.nextInt(idControlledHexs.size());

            // Obtenir l'élément à l'index aléatoire
            Integer randomElement = idControlledHexs.get(randomIndex);

            partie.sector[Hex.plateau[randomElement][0]].hex[Hex.plateau[randomElement][1]].addShips(1, this);
            this.nbShipsSupply--;
            shipsToAdd--;
            System.out.println("Un vaisseau a été ajouté à l'hexagone n°" + randomElement);
        }
        partie.closeImage();
        partie.affichagePlateau();
    }

    public void initialDeployment(Integer i, Integer j, Scanner scanner) {
        Partie partie = Partie.getInstance();
        ArrayList<Hex> level1Hexs = new ArrayList<Hex>();
        for (int k = 0; k < partie.sector.length; k++) {
            for (int l = 0; l < partie.sector[k].hex.length; l++) {
                if (partie.sector[k].hex[l].getPlanetContained() == 1 && partie.sector[k].hex[l].getShips().isEmpty() && !partie.sectorIsTaken(partie.sector[k])) {
                    level1Hexs.add(partie.sector[k].hex[l]);
                }
            }
        }
        Random random = new Random();
        Integer randomIndex = random.nextInt(level1Hexs.size());
        Hex randomElement = level1Hexs.get(randomIndex);
        partie.closeImage();
        partie.sector[Hex.plateau[randomElement.getId()][0]].hex[Hex.plateau[randomElement.getId()][1]].addShips(2, partie.joueurs.get(j));
        System.out.println("Joueur " + (this.getColor() == Color.BLUE ? "bleu :"
        : this.getColor() == Color.GREEN ? "vert :" : "jaune :")+randomElement.getId());
        partie.affichagePlateau();
        
    }
}
