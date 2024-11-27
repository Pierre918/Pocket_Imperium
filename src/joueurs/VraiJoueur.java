package joueurs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import partie.Partie;
import plateau.Hex;

/**
 * La classe VraiJoueur représente un joueur humain dans le jeu. Elle étend la
 * classe Joueur
 * et permet au joueur de choisir sa stratégie en sélectionnant trois cartes de
 * commande.
 */
public class VraiJoueur extends Joueur {

    /**
     * Constructeur de la classe VraiJoueur.
     *
     * @param color           La couleur du joueur.
     * @param controlledHexes La liste des hexagones contrôlés par le joueur.
     */
    public VraiJoueur(Color color) {
        super(color);
    }

    /**
     * Permet au joueur de choisir l'ordre de ses cartes de commande pour le round.
     *
     * @param scanner Le scanner utilisé pour lire les entrées de l'utilisateur.
     */
    @Override
    public void chooseStrat(Scanner scanner) {
        CommandCards[] strat = { null, null, null };
        int choix;
        for (int i = 0; i < 3; i++) {
            choix = -1;
            while (choix < 1 || choix > 3) {
                System.out.println("Choisissez votre " + (i + 1)
                        + "e carte pour le round (expand : 1 / explore : 2 / exterminate : 3)");
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

    public void initialDeployment(Integer i, Integer j, Scanner scanner) {
        Partie partie = Partie.getInstance();
        int choix = -1;
        boolean isCorrect = false;
        while ((choix < 0 || choix > 48) || !isCorrect) {
            System.out.println(
                    "Joueur "
                            + (partie.joueurs.get(j).getColor() == Color.BLUE ? "bleu"
                                    : partie.joueurs.get(j).getColor() == Color.GREEN ? "vert" : "jaune")
                            + ", sélectionnez le numéro d'un système de niveau 1 innocupé  :");
            choix = Integer.parseInt(scanner.nextLine());
            if (partie.sector[Hex.plateau[choix][0]].hex[Hex.plateau[choix][1]].getShips().isEmpty()
                    && partie.sector[Hex.plateau[choix][0]].hex[Hex.plateau[choix][1]].getPlanetContained() == 1
                    && !partie.sectorIsTaken(partie.sector[Hex.plateau[choix][0]])) {
                isCorrect = true;
            } else if (partie.sectorIsTaken(partie.sector[Hex.plateau[choix][0]])) {
                System.out.println("Ce secteur a déjà un de ses systèmes de niveaux 1 occupé.");
            } else {
                System.out.println("Sélection incorrect");
            }
        }
        partie.closeImage();
        partie.sector[Hex.plateau[choix][0]].hex[Hex.plateau[choix][1]].addShips(2, partie.joueurs.get(j));

        partie.affichagePlateau();

    }

    /**
     * Vérifie si une carte de commande a déjà été choisie dans la stratégie.
     *
     * @param strat Le tableau de cartes de commande représentant la stratégie du
     *              joueur.
     * @param elt   La carte de commande à vérifier.
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

    @Override
    public void expand(int playersChoosingExpand, Scanner scanner) {
        int shipsToAdd;
        System.out.println("Joueur " + (this.getColor() == Color.BLUE ? "bleu"
                : this.getColor() == Color.GREEN ? "vert" : "jaune") + " :\nExécute l'action EXPAND.");
        // Détermine le nombre de vaisseaux à ajouter en fonction du nombre de joueurs
        // ayant choisi EXPAND
        System.out.println(playersChoosingExpand);
        shipsToAdd = switch (playersChoosingExpand) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        System.out.println("Exécution de l'action EXPAND : vous pouvez ajouter " + shipsToAdd
                + " vaisseaux aux hexagones contrôlés.");

        // Vérifie si le joueur a suffisamment de vaisseaux dans sa réserve
        if (this.nbShipsSupply < shipsToAdd) {
            System.out.println("Vous n'avez pas assez de vaisseaux en réserve.");
            shipsToAdd = this.nbShipsSupply; // Limite à la réserve disponible
        }

        // Demande au joueur de choisir les hexagones où il souhaite placer les
        // vaisseaux
        while (shipsToAdd > 0) {
            Partie partie = Partie.getInstance();
            boolean isValid = false;
            while (!isValid) {
                System.out.println("Entrez l'ID de l'hexagone où vous souhaitez ajouter un vaisseau : ");
                Integer hexId = scanner.nextInt();

                // Trouve l'hexagone correspondant
                if (this.isHexControlledByMe(hexId, this)) {
                    partie.sector[Hex.plateau[hexId][0]].hex[Hex.plateau[hexId][1]].addShips(1, this); // Ajoute un
                                                                                                       // vaisseau dans
                                                                                                       // l'hexagone
                    this.nbShipsSupply--; // Réduit la réserve
                    shipsToAdd--; // Réduit le nombre de vaisseaux restants à ajouter
                    System.out.println("Un vaisseau a été ajouté à l'hexagone ID: " + hexId);
                    isValid = true;
                } else {
                    System.out.println("Hexagone non valide ou non contrôlé. Veuillez choisir un hexagone valide.");
                }
            }
            partie.closeImage();
            partie.affichagePlateau();
        }
    }

    public void exterminate(int playersChoosingExterminate, Scanner scanner) {
        int systemsToInvade;
        Partie partie = Partie.getInstance();
        System.out.println("Joueur " + (this.getColor() == Color.BLUE ? "bleu"
                : this.getColor() == Color.GREEN ? "vert" : "jaune") + " exécute l'action EXTERMINATE.");
        // Détermine le nombre de systèmes pouvant être envahis en fonction des joueurs
        // ayant choisi Exterminate
        systemsToInvade = switch (playersChoosingExterminate) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        System.out.println("Action EXTERMINATE : vous pouvez envahir " + systemsToInvade + " systèmes.");
        ArrayList<Integer> myHexIds=new ArrayList<>();
        for (Hex hex : this.getControlledHex(this)){
            myHexIds.add(Hex.findIndex(Hex.plateau, new int[] {hex.getIdSector(),hex.getId()}));
        }
        
        // Gérer les invasions
        while (systemsToInvade > 0) {
            // Demander l'hexagone de départ
            boolean goodAnsw = false;
            Hex startHex = null;
            while (!goodAnsw) {
                System.out.println("Entrez l'ID de l'hexagone de départ que vous contrôlez pour l'invasion : ");
                int startHexId = scanner.nextInt();

                // Vérifie si l'hexagone de départ est contrôlé
                startHex = partie.sector[Hex.plateau[startHexId][0]].hex[Hex.plateau[startHexId][1]];
                if (!myHexIds.contains(startHexId)) {
                    System.out.println("Hexagone non valide, sans vaisseaux, ou avec des vaisseaux déjà utilisés. Veuillez réessayer.");
                } else {
                    goodAnsw = true;
                }
            }
            goodAnsw = false;
            int shipsToUse=-1;
            // Demander le nombre de vaisseaux à utiliser pour l'invasion
            while (!goodAnsw) {
                System.out.println("Entrez le nombre de vaisseaux que vous souhaitez utiliser (maximum : "
                        + startHex.getShips().size() + ") : ");
                shipsToUse = scanner.nextInt();

                if (shipsToUse <= 0 || shipsToUse > startHex.getShips().size()) {
                    System.out.println("Nombre de vaisseaux non valide. Veuillez réessayer.");
                } else {
                    goodAnsw = true;
                }
            }
            goodAnsw = false;
            int targetHexId=-1;
            // Demander l'hexagone cible
            while (!goodAnsw) {
                System.out.println("Entrez l'ID de l'hexagone cible : ");
                targetHexId = scanner.nextInt();

                // Vérifie si l'hexagone cible est adjacent et non contrôlé par ce joueur
                if (!this.isHexAdjacent(startHex, targetHexId)) {
                    System.out.println("Hexagone non adjacent ou déjà contrôlé. Veuillez réessayer.");
                } else {
                    goodAnsw = true;
                    myHexIds.remove(Integer.valueOf(targetHexId));
                }
            }

            // Simuler l'invasion avec le nombre choisi de vaisseaux
            invade(startHex, targetHexId, shipsToUse);

            // Réduire le nombre de systèmes restants à envahir
            systemsToInvade--;
        }

        System.out.println("Extermination terminée.");
    }

}
