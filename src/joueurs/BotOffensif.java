package joueurs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import partie.Partie;
import plateau.Hex;

public class BotOffensif extends Joueur {
    /**
     * Constructeur de la classe BotRandom.
     *
     * @param color La couleur associée au bot.
     */
    public BotOffensif(Color color) {
        super(color);
    }

    @Override
    public void expand(int playersChoosingExpand, Scanner scanner) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'expand'");
    }

    @Override
    public void initialDeployment(Integer i, Integer j, Scanner scanner) {
        System.out.println("Joueur " + (this.getColor() == Color.BLUE ? "bleu "
                : this.getColor() == Color.GREEN ? "vert " : "jaune "));
        Partie partie = Partie.getInstance();
        ArrayList<Hex> level1HexsTaken = new ArrayList<Hex>();
        ArrayList<Hex> level1Hexs = new ArrayList<Hex>();
        for (int k = 0; k < partie.sector.length; k++) {
            for (int l = 0; l < partie.sector[k].hex.length; l++) {
                if (partie.sector[k].hex[l].getPlanetContained() == 1
                        && !partie.sector[k].hex[l].getShips().isEmpty()) {
                    level1HexsTaken.add(partie.sector[k].hex[l]);
                }
                if (partie.sector[k].hex[l].getPlanetContained() == 1 && partie.sector[k].hex[l].getShips().isEmpty()
                        && !partie.sectorIsTaken(partie.sector[k])) {
                    level1Hexs.add(partie.sector[k].hex[l]);
                }
            }
        }

        if (i != 0 || j != 0) {

            for (Hex hex : level1HexsTaken) {
                System.out.println("atatck" + hex.getAdjacents());
                    for (int[] hexAdj : hex.getAdjacents()) {
                        System.out.println("hex :" + hex.getId());
                        System.out.println("hexAdj" + Hex.findIndex(Hex.plateau, hexAdj));
                        if (hexAdj[0] != Hex.plateau[hex.getId()][0]) {
                            partie.sector[hexAdj[0]].hex[hexAdj[1]].addShips(2, this);
                            partie.closeImage();
                            partie.affichagePlateau();
                            return;
                        }

                    }
                
            }

        }
        Random random = new Random();
        Integer randomIndex = random.nextInt(level1Hexs.size());
        Hex randomElement = level1Hexs.get(randomIndex);
        partie.closeImage();
        partie.sector[Hex.plateau[randomElement.getId()][0]].hex[Hex.plateau[randomElement.getId()][1]].addShips(2,
                partie.joueurs.get(j));
        System.out.println("Joueur AAAH" + (this.getColor() == Color.BLUE ? "bleu :"
                : this.getColor() == Color.GREEN ? "vert :" : "jaune :") + randomElement.getId());
        partie.affichagePlateau();
    }

    @Override
    public void chooseStrat(Scanner scanner) {
        this.strat[0] = CommandCards.EXTERMINATE;
        this.strat[1] = CommandCards.EXPLORE;
        this.strat[2] = CommandCards.EXPAND;
    }
}
