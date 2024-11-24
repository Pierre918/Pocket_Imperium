package joueurs;

import java.awt.Color;
import java.util.List;
import java.util.Scanner;
import partie.Partie;
import plateau.Hex;

/**
 * La classe abstraite Joueur représente un joueur dans le jeu.
 * Elle contient des informations sur la couleur du joueur, le nombre de vaisseaux en réserve,
 * et les cartes de commande (stratégies) du joueur.
 */
public abstract class Joueur {

    /**
     * La couleur associée au joueur.
     */
    private final Color color;

    /**
     * Le score du joueur.
     */
    private  int score;

    /**
     * Le nombre de vaisseaux en réserve pour le joueur.
     */
    private int nbShipsSupply=15;

    /**
     * Les cartes de commande (stratégies) du joueur.
     */
    private CommandCards[] strat={null,null,null};

    public CommandCards[] getCommandCards(){
        return strat;
    }
    /**
     * Les Hex contrôlés par le joueur.
     */
    protected List<Hex> controlledHexes;


    /**
     * Constructeur de la classe Joueur.
     *
     * @param color La couleur associée au joueur.
     */
    
    public Joueur(Color color, List<Hex> controlledHexes) {

        this.color = color;
        this.score = 0;
        this.controlledHexes = controlledHexes;

    }
    
    public void addControlledHex(Hex hex) {
        // Vérifie si l'hexagone est déjà dans la liste des hexagones contrôlés
        if (!controlledHexes.contains(hex)) {
            // Définit ce joueur comme contrôleur de l'hexagone
            hex.setOwner(this);
            controlledHexes.add(hex);
            System.out.println("Hex ID: " + hex.getId() + " ajouté à la liste des hexagones contrôlés.");
        } else {
            System.out.println("Hex ID: " + hex.getId() + " est déjà contrôlé par ce joueur.");
        }
    }
    
    public void removeControlledHex(Hex hex) {
        // Vérifie si l'hexagone est contrôlé par ce joueur
        if (controlledHexes.contains(hex)) {
            hex.setOwner(null); // Retire le contrôle de l'hexagone
            controlledHexes.remove(hex);
            System.out.println("Hex ID: " + hex.getId() + " retiré de la liste des hexagones contrôlés.");
        } else {
            System.out.println("Hex ID: " + hex.getId() + " n'est pas contrôlé par ce joueur.");
        }
    }

    public boolean controlsHex(Hex hex) {
        return controlledHexes.contains(hex);
    }

    /**
     * Exécute l'action EXPAND, représentant l'extension du joueur dans les hexagones qu'il contrôle.
     * Permet au joueur de choisir les hexagones pour y ajouter des vaisseaux.
     *
     * @param playersChoosingExpand Le nombre de joueurs ayant choisi l'action EXPAND ce round.
     * @param scanner Le scanner pour lire les entrées de l'utilisateur.
     */
    public void expand(int playersChoosingExpand, Scanner scanner) {
        int shipsToAdd;
        System.out.println("Joueur " + (this.getColor() == Color.BLUE ? "bleu"
        : this.getColor() == Color.GREEN ? "vert" : "jaune") + " exécute l'action EXPAND.");
        // Détermine le nombre de vaisseaux à ajouter en fonction du nombre de joueurs ayant choisi EXPAND
        shipsToAdd = switch (playersChoosingExpand) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        System.out.println("Exécution de l'action EXPAND : vous pouvez ajouter " + shipsToAdd + " vaisseaux aux hexagones contrôlés.");

        // Vérifie si le joueur a suffisamment de vaisseaux dans sa réserve
        if (nbShipsSupply < shipsToAdd) {
            System.out.println("Vous n'avez pas assez de vaisseaux en réserve.");
            shipsToAdd = nbShipsSupply; // Limite à la réserve disponible
        }

        // Demande au joueur de choisir les hexagones où il souhaite placer les vaisseaux
        while (shipsToAdd > 0) {
            System.out.println("Hexagones contrôlés :");
            for (Hex hex : controlledHexes) {
                System.out.println("Hex ID: " + hex.getId() + " - Planète Contenue: " + hex.getPlanetContained());
            }

            System.out.println((this.getColor() == Color.BLUE ? "bleu"
            : this.getColor() == Color.GREEN ? "vert" : "jaune") + "Entrez l'ID de l'hexagone où vous souhaitez ajouter un vaisseau : ");
            int hexId = scanner.nextInt();

            // Trouve l'hexagone correspondant
            Hex selectedHex = findControlledHexById(hexId);

            if (selectedHex != null) {
                selectedHex.addShips(1, this); // Ajoute un vaisseau dans l'hexagone
                nbShipsSupply--; // Réduit la réserve
                shipsToAdd--; // Réduit le nombre de vaisseaux restants à ajouter
                System.out.println("Un vaisseau a été ajouté à l'hexagone ID: " + hexId);
            } else {
                System.out.println("Hexagone non valide ou non contrôlé. Veuillez choisir un hexagone valide.");
            }
        }
    }

    /**
     * Exécute l'action EXPLORE, permettant au joueur de déplacer des flottes vers des hexagones adjacents.
     *
     * @param playersChoosingExplore Le nombre de joueurs ayant choisi l'action EXPLORE ce round.
     * @param scanner Le scanner pour lire les entrées de l'utilisateur.
     */
    public void explore(int playersChoosingExplore, Scanner scanner) {
        int fleetsToMove;
        System.out.println("Joueur " + (this.getColor() == Color.BLUE ? "bleu"
        : this.getColor() == Color.GREEN ? "vert" : "jaune") + " exécute l'action EXPLORE.");
        // Détermine le nombre de flottes pouvant être déplacées en fonction des joueurs ayant choisi Explore
        fleetsToMove = switch (playersChoosingExplore) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        System.out.println("Action EXPLORE : vous pouvez déplacer " + fleetsToMove + " flottes, chaque flotte pouvant se déplacer de 2 hexagones.");

        // Déplacement des flottes
        while (fleetsToMove > 0) {
            System.out.println("Hexagones contrôlés :");
            for (Hex hex : controlledHexes) {
                System.out.println("Hex ID: " + hex.getId() + " - Nombre de Vaisseaux: " + hex.getShips().size());
            }

            System.out.println("Entrez l'ID de l'hexagone de départ de la flotte : ");
            int startHexId = scanner.nextInt();

            // Trouve l'hexagone de départ
            Hex startHex = findControlledHexById(startHexId);
            if (startHex == null) {
                System.out.println("Hexagone non contrôlé ou non valide. Veuillez réessayer.");
                continue;
            }

            // Vérifie s'il y a des vaisseaux dans l'hexagone de départ
            if (startHex.getShips().isEmpty()) {
                System.out.println("Aucun vaisseau dans cet hexagone. Déplacement annulé.");
                continue;
            }

            System.out.println("Entrez l'ID de l'hexagone cible (1er déplacement) : ");
            int targetHexId = scanner.nextInt();

            // Vérifie si l'hexagone cible est adjacent et non occupé par un autre joueur
            if (!isHexAdjacentAndFree(startHex, targetHexId)) {
                System.out.println("L'hexagone cible n'est pas valide (pas adjacent ou occupé). Déplacement annulé.");
                continue;
            }

            System.out.println("Souhaitez-vous effectuer un deuxième déplacement pour cette flotte ? (oui : 1 / non : 0) : ");
            int secondMove = scanner.nextInt();

            if (secondMove == 1) {
                System.out.println("Entrez l'ID de l'hexagone pour le 2ème déplacement : ");
                int secondTargetHexId = scanner.nextInt();

                // Vérifie le 2ème hexagone
                if (!isHexAdjacentAndFree(startHex, secondTargetHexId)) {
                    System.out.println("L'hexagone cible du deuxième déplacement n'est pas valide. Déplacement annulé.");
                    continue;
                }

                // Ajouter le 2ème déplacement
                moveFleet(startHex, secondTargetHexId);
            } else {
                moveFleet(startHex, targetHexId); // Déplacement simple
            }

            fleetsToMove--; // Réduit le nombre de flottes restantes
        }

        System.out.println("Exploration terminée.");
    }

    /**
     * Déplace une flotte vers un nouvel hexagone.
     *
     * @param startHex L'hexagone de départ.
     * @param targetHexId L'ID de l'hexagone cible.
     */
    private void moveFleet(Hex startHex, int targetHexId) {
        System.out.println("Déplacement de flotte de l'hexagone ID: " + startHex.getId() + " vers l'hexagone ID: " + targetHexId);

        // Supprime un vaisseau de l'hexagone de départ
        startHex.deleteShips(1);

        // Simule l'exploration ou la prise de contrôle
        Hex targetHex = new Hex(0); // Crée un nouvel hexagone si non existant
        targetHex.setId(targetHexId);
        targetHex.addShips(1, this); // Ajoute un vaisseau au nouvel hexagone
        controlledHexes.add(targetHex); // Ajoute l'hexagone exploré à la liste des hexagones contrôlés

        System.out.println("Flotte déplacée avec succès.");
    }

    /**
     * Vérifie si un hexagone cible est adjacent et non occupé par un autre joueur.
     *
     * @param startHex L'hexagone de départ.
     * @param targetHexId L'ID de l'hexagone cible.
     * @return true si l'hexagone est valide, false sinon.
     */
    private boolean isHexAdjacentAndFree(Hex startHex, int targetHexId) {
        int[][] adjacents = startHex.getAdjacents();
        for (int[] adjacent : adjacents) {
            if (adjacent[1] == targetHexId) {
                return true; // Vérifie si l'hexagone cible est adjacent
            }
        }
        return false; // Pas adjacent ou occupé
    }

    /**
     * Trouve un hexagone contrôlé par son ID.
     *
     * @param id L'ID de l'hexagone.
     * @return L'hexagone correspondant, ou null s'il n'est pas trouvé.
     */
    private Hex findControlledHexById(int id) {
        for (Hex hex : controlledHexes) {
            if (hex.getId() == id) {
                return hex;
            }
        }
        return null;
    }
    
    /**
     * Exécute l'action EXTERMINATE, permettant au joueur d'envahir des systèmes adjacents.
     *
     * @param playersChoosingExterminate Le nombre de joueurs ayant choisi l'action EXTERMINATE ce round.
     * @param scanner Le scanner pour lire les entrées de l'utilisateur.
     */
    public void exterminate(int playersChoosingExterminate, Scanner scanner) {
        int systemsToInvade;

        System.out.println("Joueur " + (this.getColor() == Color.BLUE ? "bleu"
        : this.getColor() == Color.GREEN ? "vert" : "jaune") + " exécute l'action EXTERMINATE.");
        // Détermine le nombre de systèmes pouvant être envahis en fonction des joueurs ayant choisi Exterminate
        systemsToInvade = switch (playersChoosingExterminate) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };

        System.out.println("Action EXTERMINATE : vous pouvez envahir " + systemsToInvade + " systèmes.");

        // Gérer les invasions
        while (systemsToInvade > 0) {
            System.out.println("Hexagones contrôlés par vous :");
            for (Hex hex : controlledHexes) {
                System.out.println("Hex ID: " + hex.getId() + " - Nombre de Vaisseaux: " + hex.getShips().size());
            }

            // Demander l'hexagone de départ
            System.out.println("Entrez l'ID de l'hexagone de départ pour l'invasion : ");
            int startHexId = scanner.nextInt();

            // Vérifie si l'hexagone de départ est contrôlé
            Hex startHex = findControlledHexById(startHexId);
            if (startHex == null || startHex.getShips().isEmpty()) {
                System.out.println("Hexagone non valide ou sans vaisseaux. Veuillez réessayer.");
                continue;
            }

            // Demander le nombre de vaisseaux à utiliser pour l'invasion
            System.out.println("Entrez le nombre de vaisseaux que vous souhaitez utiliser (maximum : " + startHex.getShips().size() + ") : ");
            int shipsToUse = scanner.nextInt();

            if (shipsToUse <= 0 || shipsToUse > startHex.getShips().size()) {
                System.out.println("Nombre de vaisseaux non valide. Veuillez réessayer.");
                continue;
            }

            // Demander l'hexagone cible
            System.out.println("Entrez l'ID de l'hexagone cible : ");
            int targetHexId = scanner.nextInt();

            // Vérifie si l'hexagone cible est adjacent et non contrôlé par ce joueur
            if (!isHexAdjacent(startHex, targetHexId)) {
                System.out.println("Hexagone non adjacent ou déjà contrôlé. Veuillez réessayer.");
                continue;
            }

            // Simuler l'invasion avec le nombre choisi de vaisseaux
            simulateInvasion(startHex, targetHexId, shipsToUse);

            // Réduire le nombre de systèmes restants à envahir
            systemsToInvade--;
        }

        System.out.println("Extermination terminée.");
    }

    /**
     * Simule une invasion d'un système par le joueur.
     *
     * @param startHex L'hexagone de départ.
     * @param targetHexId L'ID de l'hexagone cible.
     * @param shipsToUse Le nombre de vaisseaux utilisés pour l'invasion.
     */
    private void simulateInvasion(Hex startHex, int targetHexId, int shipsToUse) {
        // Récupérer l'hexagone cible (simulation d'un système non contrôlé)
        Hex targetHex = new Hex(0);
        targetHex.setId(targetHexId);

        System.out.println("Invasion de l'hexagone ID: " + targetHexId + " depuis l'hexagone ID: " + startHex.getId());

        // Supprime le nombre de vaisseaux choisi de l'hexagone de départ
        startHex.deleteShips(shipsToUse);

        // Simuler la résolution de l'invasion
        int attackerShips = shipsToUse; // Vaisseaux attaquants
        int defenderShips = targetHex.getShips().size(); // Vaisseaux défensifs

        System.out.println("Attaquant : " + attackerShips + " vaisseau(x), Défenseur : " + defenderShips + " vaisseau(x)");

        int minShips = Math.min(attackerShips, defenderShips);

        // Retirer les vaisseaux des deux côtés
        startHex.deleteShips(minShips);
        targetHex.deleteShips(minShips);

        // Résultat de l'invasion
        if (attackerShips > defenderShips) {
            // Attaquant gagne
            System.out.println("L'attaquant contrôle maintenant l'hexagone.");
            targetHex.addShips(attackerShips - minShips, this); // Ajouter les vaisseaux restants
            controlledHexes.add(targetHex); // Ajouter l'hexagone à la liste contrôlée
        } else if (attackerShips < defenderShips) {
            // Défenseur gagne
            System.out.println("Le défenseur garde le contrôle de l'hexagone.");
        } else {
            // Système reste inoccupé
            System.out.println("Le système est maintenant inoccupé.");
        }
    }

    /**
     * Vérifie si un hexagone cible est adjacent à un hexagone contrôlé.
     *
     * @param startHex L'hexagone de départ.
     * @param targetHexId L'ID de l'hexagone cible.
     * @return true si l'hexagone cible est adjacent, false sinon.
     */
    private boolean isHexAdjacent(Hex startHex, int targetHexId) {
        int[][] adjacents = startHex.getAdjacents();
        for (int[] adjacent : adjacents) {
            if (adjacent[1] == targetHexId) {
                return true; // Hexagone adjacent
            }
        }
        return false; // Pas adjacent
    }

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
     * Exécute une action selon la stratégie du joueur.
     *
     */
    public void jouerAction(int i, int count, Scanner scanner) {
        Partie partie = Partie.getInstance();
        if(this.strat[i] == CommandCards.EXPAND){
            expand(count, scanner);
        }
        else if(this.strat[i] == CommandCards.EXPLORE){
            explore(count, scanner);
        }
        else if(this.strat[i] == CommandCards.EXTERMINATE){
            exterminate(count, scanner);
        }
    }

    /**
     * Retourne le score actuel du joueur.
     *
     * @return Le score du joueur.
     */
    public int getScore() {
        return score;
    }

    /**
     * Ajoute des points au score du joueur.
     *
     * @param points Le nombre de points à ajouter.
     */
    public void ajouterScore(int points) {
        this.score += points;
    }
}
