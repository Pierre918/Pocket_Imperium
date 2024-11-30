package joueurs;

import java.awt.Color;
import java.util.ArrayList;

import command.Command;
import partie.Partie;
import plateau.Hex;
import plateau.Sector;

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
     */
    @Override
    public void chooseStrat() {
        CommandCards[] strat = { null, null, null };
        int choix;
        for (int i = 0; i < 3; i++) {
            choix = -1;
            while (choix < 1 || choix > 3) {
                System.out.println("Choisissez votre " + (i + 1)
                        + "e carte pour le round (expand : 1 / explore : 2 / exterminate : 3)");
                choix = Command.askInteger(0, 4, "Erreur");

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
     * Permet le déploiement initial du plateau.
     * Demande au joueur sur quel hexagone de niveau 1 il veut placer deux
     * vaisseaux.
     * 
     * @param i
     * @param j
     */
    public void initialDeployment(Integer i, Integer j) {
        Partie partie = Partie.getInstance();
        int choix = -1;
        boolean isCorrect = false;
        while (!isCorrect) {
            System.out.println(
                    "Joueur "
                            + (partie.joueurs.get(j).getColor() == Color.BLUE ? "bleu"
                                    : partie.joueurs.get(j).getColor() == Color.GREEN ? "vert" : "jaune")
                            + ", sélectionnez le numéro d'un système de niveau 1 innocupé  :");
            choix = Command.askInteger(-1, 49, "Erreur");
            if (partie.sector[Hex.plateau[choix][0]].hex[Hex.plateau[choix][1]].getShips().isEmpty()
                    && partie.sector[Hex.plateau[choix][0]].hex[Hex.plateau[choix][1]].getPlanetContained() == 1
                    && !partie.sectorIsTakenL1(partie.sector[Hex.plateau[choix][0]])) {
                isCorrect = true;
            } else if (partie.sectorIsTakenL1(partie.sector[Hex.plateau[choix][0]])) {
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

    /**
     * Méthode permettant d'exécuter l'action expand.
     * Vérifie si le joueur a assez de bateau.
     * Dans le while, a chaque tour de boucle, demande au joueur où est ce qu'il
     * veut placer ses vaisseaux tout en vérifiant si l'hexagone est valide.
     * Affiche le plateau a la fin de l'execution.
     * 
     * @param playersChoosingExpand Nombre de joueurs qui ont choisi la meme carte
     *                              que le joueur
     */
    @Override
    public void expand(int playersChoosingExpand) {
        int shipsToAdd;
        System.out.println("Joueur " + (this.getColor() == Color.BLUE ? "bleu"
                : this.getColor() == Color.GREEN ? "vert" : "jaune") + " :\nExécute l'action EXPAND.");
        System.out.println(playersChoosingExpand);
        shipsToAdd = switch (playersChoosingExpand) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };
        if (this.getControlledHex(this).isEmpty()){
            System.out.println("Plus de vaisseau sur le plateau");
            return;
        }
        System.out.println("Exécution de l'action EXPAND : vous pouvez ajouter " + shipsToAdd
                + " vaisseaux aux hexagones contrôlés.");

        if (this.nbShipsSupply < shipsToAdd) {
            System.out.println("Vous n'avez pas assez de vaisseaux en réserve.");
            shipsToAdd = this.nbShipsSupply;
        }

        while (shipsToAdd > 0) {
            Partie partie = Partie.getInstance();
            boolean isValid = false;
            while (!isValid) {
                System.out.println("Entrez l'ID de l'hexagone où vous souhaitez ajouter un vaisseau : ");
                Integer hexId = Command.askInteger(-1, 49, "Erreur");

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

    /**
     * Exécute l'action EXTERMINATE pour le joueur.
     * Cette méthode permet au joueur de choisir des systèmes à envahir en fonction
     * du nombre de joueurs ayant choisi la même carte.
     * Le joueur peut envahir un certain nombre de systèmes en fonction du nombre de
     * joueurs ayant choisi l'action EXTERMINATE.
     *
     * @param playersChoosingExterminate Le nombre de joueurs qui choisissent la
     *                                   même carte EXTERMINATE.
     */
    public void exterminate(int playersChoosingExterminate) {
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
        ArrayList<int[]> doNotUse = new ArrayList<>();
        // Gérer les invasions
        while (systemsToInvade > 0) {
            ArrayList<Integer> myHexIds = new ArrayList<>();
            for (Hex hex : this.getControlledHex(this)) {
                if (!containsArray(doNotUse, new int[] { hex.getIdSector(), hex.getId() })) {
                    myHexIds.add(Hex.findIndex(Hex.plateau, new int[] { hex.getIdSector(), hex.getId() }));
                }
            }
            if (myHexIds.size() == 0) {
                System.out.println("Plus de vaisseaux valables");
                break;
            }
            // Demander l'hexagone de départ
            boolean goodAnsw = false;
            Hex startHex = null;
            while (!goodAnsw) {
                System.out.println("Entrez l'ID de l'hexagone de départ que vous contrôlez pour l'invasion : ");
                int startHexId = Command.askInteger(-1, 49, "Erreur");

                // Vérifie si l'hexagone de départ est contrôlé
                startHex = partie.sector[Hex.plateau[startHexId][0]].hex[Hex.plateau[startHexId][1]];
                if (!myHexIds.contains(startHexId)) {
                    System.out.println(
                            "Hexagone non valide, sans vaisseaux, ou avec des vaisseaux déjà utilisés. Veuillez réessayer.");
                } else {
                    goodAnsw = true;
                }
            }
            goodAnsw = false;
            int shipsToUse = -1;

            System.out.println("Entrez le nombre de vaisseaux que vous souhaitez utiliser (maximum : "
                    + startHex.getShips().size() + ") : ");
            shipsToUse = Command.askInteger(0, startHex.getShips().size() + 1,
                    "Nombre de vaisseaux non valide. Veuillez réessayer.");

            goodAnsw = false;
            int targetHexId = -1;
            // Demander l'hexagone cible
            while (!goodAnsw) {
                System.out.println("Entrez l'ID de l'hexagone cible : ");
                targetHexId = Command.askInteger(-1, 49, "Erreur");

                // Vérifie si l'hexagone cible est adjacent et non contrôlé par ce joueur
                if (!this.isHexAdjacentAndNotMine(startHex, targetHexId)) {
                    System.out.println("Hexagone non adjacent ou déjà contrôlé. Veuillez réessayer.");
                } else {
                    goodAnsw = true;
                    doNotUse.add(Hex.plateau[targetHexId]);
                }
            }

            // Simuler l'invasion avec le nombre choisi de vaisseaux
            invade(startHex, targetHexId, shipsToUse);

            // Réduire le nombre de systèmes restants à envahir
            systemsToInvade--;
        }

        System.out.println("Extermination terminée.");
    }

    /**
     * Exécute l'action EXPLORE pour le joueur.
     * Cette méthode permet au joueur de déplacer un certain nombre de flottes en
     * fonction du nombre de joueurs ayant choisi la même carte.
     * Chaque flotte peut se déplacer de 2 hexagones.
     *
     * @param playersChoosingExplore Le nombre de joueurs qui choisissent la même
     *                               carte EXPLORE..
     */
    public void explore(int playersChoosingExplore) {
        int fleetsToMove;
        Partie partie = Partie.getInstance();
        System.out.println("Joueur " + (this.getColor() == Color.BLUE ? "bleu"
                : this.getColor() == Color.GREEN ? "vert" : "jaune") + " exécute l'action EXPLORE.");

        fleetsToMove = switch (playersChoosingExplore) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        System.out.println("Action EXPLORE : vous pouvez déplacer " + fleetsToMove
                + " flottes, chaque flotte pouvant se déplacer de 2 hexagones.");

        // Déplacement des flottes
        ArrayList<int[]> doNotUse = new ArrayList<int[]>();
        boolean inTriPrime = false;
        while (fleetsToMove > 0) {
            ArrayList<Integer> myHexIds = new ArrayList<>();
            for (Hex hex : this.getControlledHex(this)) {
                if (!containsArray(doNotUse, new int[] { hex.getIdSector(), hex.getId() })) {
                    myHexIds.add(Hex.findIndex(Hex.plateau, new int[] { hex.getIdSector(), hex.getId() }));
                }
            }
            if (myHexIds.size() == 0) {
                System.out.println("Plus aucun vaisseaux valables");
                break;
            }
            int startHexId = -1;
            while (!myHexIds.contains(startHexId)) {
                System.out.println("Entrez l'ID d'hexagone que vous contrôlez pour le départ de la flotte : ");

                startHexId = Command.askInteger(-1, 49, "Erreur");

                // Trouve l'hexagone de départ
                if (!myHexIds.contains(startHexId)) {
                    System.out.println("Hexagone non contrôlé ou non valide. Veuillez réessayer.");
                }
                if (partie.sector[Hex.plateau[startHexId][0]].hex[Hex.plateau[startHexId][1]].getShips().isEmpty()) {
                    System.out.println("Aucun vaisseau dans cet hexagone. Déplacement annulé.");
                    startHexId = -1;
                }

            }
            inTriPrime = false;
            for (int j = 0; j < 2; j++) {
                int targetHexId = -1;
                if (inTriPrime) {
                    System.out.println("Vous ne pouvez pas déplacer une flotte à travers Tri Prime");
                    break;
                }
                do {
                    System.out.println("Entrez l'ID de l'hexagone cible : ");
                    targetHexId = Command.askInteger(-1, 49, "Erreur");

                    // Vérifie si l'hexagone cible est adjacent et non occupé par un autre joueur
                    if (!isHexAdjacentAndFree(startHexId, targetHexId)) {
                        System.out
                                .println(
                                        "L'hexagone cible n'est pas valide (pas adjacent ou occupé). Déplacement annulé.");
                    } else if (targetHexId == 24) {
                        doNotUse.add(Hex.plateau[targetHexId]);
                        inTriPrime = true;
                    }
                } while (!isHexAdjacentAndFree(startHexId, targetHexId));

                int nbShipsMoving = -1;

                System.out.println("Choisissez le nombre de vaisseaux que vous souhaitez déplacer : ");
                nbShipsMoving = Command.askInteger(0,
                        partie.sector[Hex.plateau[startHexId][0]].hex[Hex.plateau[startHexId][1]]
                                .getShips().size() + 1,
                        "Sélection invalide");

                moveFleet(startHexId, targetHexId, nbShipsMoving);
                if (j == 1) {
                    doNotUse.add(Hex.plateau[targetHexId]);
                } else {
                    System.out.println("L'hexagone de départ est maintenant l'hexagone que vous venez d'envahir.");
                }
                startHexId = targetHexId;

                partie.closeImage();
                partie.affichagePlateau();
            }
            fleetsToMove--; // Réduit le nombre de flottes restantes
        }

        System.out.println("Exploration terminée.");
    }

    public ArrayList<Sector> scoreSector(ArrayList<Sector> cardsChosen, int coef) {
        Partie partie = Partie.getInstance();
        for (int j = 0; j < (this.controlsTriPrime() && coef!=2 ? 2 : 1); j++) {
            if (cardsChosen.size() == Sector.nbSectorTaken()){
                System.out.println("Plus aucune carte disponible, fin du scoring");
                break;
            }
            System.out.println("Joueur " + (this.getColor() == Color.BLUE ? "bleu :"
                    : this.getColor() == Color.GREEN ? "vert :" : "jaune :")
                    + "Choisissez un secteur occupé et non choisi à scorer (hors TriPrime)");

            int n = -1;
            int l = 0;
            while (n == -1) {
                if (l > 0) {
                    System.out.println("Erreur de saisi, réessayez.");
                }
                n = Command.askInteger(0, 10,
                        "Erreur, les secteurs sont numérotés de gauche à droite, de haut en bas");
                l++;
                if (!partie.sectorIsTaken(partie.sector[n - 1]) || n == 5
                        || cardsChosen.contains(partie.sector[n - 1])) {
                    n = -1;
                }
            }
            cardsChosen.add(partie.sector[n - 1]);
            for (Hex hex : partie.sector[n - 1].hex) {
                if (!hex.getShips().isEmpty()) {
                    if (hex.getShips().get(0).joueur == this) {
                        this.ajouterScore(hex.getPlanetContained() * coef);
                    }
                }
            }
            System.out.println("Joueur " + (this.getColor() == Color.BLUE ? "bleu "
                    : this.getColor() == Color.GREEN ? "vert " : "jaune ") + "a "
                    + this.getScore() + " points");
        }
        return cardsChosen;
    }
}
