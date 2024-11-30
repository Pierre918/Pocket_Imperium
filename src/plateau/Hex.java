package plateau;

import java.util.ArrayList;
import java.util.List;

import joueurs.Joueur;
import vaisseaux.Ship;

/**
 * La classe Hex représente un hexagone dans le plateau de jeu.
 * L'attribut static plateau permet de convertir un id représenté pour le joueur
 * en un id de secteur et un id d'hexagone au sein de ce secteur
 */
public class Hex {
    /**
     * Le type de planète contenue dans l'hexagone.
     */
    private int planetContained;
    /**
     * L'identifiant de l'hexagone au sein du secteur.
     * Est différent de l'id global représenté sur l'image du plateau
     */
    private int id;
    /**
     * L'identifiant du secteur auquel appartient l'hexagone.
     */
    private int idSector;
    /**
     * La liste des vaisseaux présents dans l'hexagone.
     */
    private List<Ship> ships = new ArrayList<>();

    /**
     * Constructeur de la classe Hex.
     *
     * @param planetContained Le type de planète contenues dans l'hexagone.
     */
    public Hex(int planetContained) {
        this.planetContained = planetContained;
    }

    /**
     * Obtient la liste des vaisseaux présents dans l'hexagone.
     *
     * @return La liste des vaisseaux.
     */
    public List<Ship> getShips() {
        return this.ships;
    }

    /**
     * Ajoute un certain nombre de vaisseaux à l'hexagone.
     *
     * @param nbShips Le nombre de vaisseaux à ajouter.
     * @param joueur  Le joueur auquel appartiennent les vaisseaux.
     */
    public void addShips(int nbShips, Joueur joueur) {
        if (!this.ships.isEmpty()) {
            if (this.ships.get(0).joueur != joueur) {
                if (this.ships.size() > nbShips) {
                    this.deleteShips(this.ships.size() - nbShips);
                    return;
                } else {
                    this.ships = new ArrayList<>();
                    nbShips = nbShips - this.ships.size();
                }
            }
        }
        for (int i = 0; i < nbShips; i++) {
            this.ships.add(new Ship(joueur));
        }

    }

    /**
     * Vérifie si un joueur contrôle cet hexagone.
     *
     * @param joueur Le joueur à vérifier.
     * @return true si le joueur contrôle l'hexagone, false sinon.
     */
    public boolean isControlledBy(Joueur joueur) {
        if (!this.ships.isEmpty()) {
            return this.ships.get(0).joueur == joueur;
        }
        return false;
    }

    /**
     * Supprime un certain nombre de vaisseaux de l'hexagone.
     *
     * @param n Le nombre de vaisseaux à supprimer.
     */
    public void deleteShips(int n) {
        for (int i = 0; i < n; i++) {
            if (!ships.isEmpty()) {
                ships.remove(ships.size() - 1);
            }
        }
    }

    /**
     * Déplace un certain nombre de vaisseaux vers un autre hexagone.
     *
     * @param sector   Le tableau des secteurs.
     * @param nbShips  Le nombre de vaisseaux à déplacer.
     * @param idHexMvg L'identifiant de l'hexagone de destination.
     * @return Le tableau des secteurs mis à jour.
     */
    public Sector[] moveShips(Sector[] sector, int nbShips, int idHexMvg) {
        int[] pos = Hex.plateau[idHexMvg];
        if (this.ships.size() >= nbShips) {
            this.deleteShips(nbShips);
            sector[pos[0]].hex[pos[1]].addShips(nbShips, this.ships.get(0).joueur);
        } else {
            System.out.println("Le système ne contient pas assez de vaisseaux");
        }
        return sector;
    }

    /**
     * Obtient l'identifiant du secteur de l'hexagone.
     *
     * @return L'identifiant du secteur.
     */
    public int getIdSector() {
        return idSector;
    }

    /**
     * Définit l'identifiant du secteur de l'hexagone.
     *
     * @param idSector L'identifiant du secteur.
     */
    public void setIdSector(int idSector) {
        this.idSector = idSector;
    }

    /**
     * Obtient l'identifiant de l'hexagone.
     *
     * @return L'identifiant de l'hexagone.
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant de l'hexagone.
     *
     * @param id L'identifiant de l'hexagone.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtient le nombre de planètes contenues dans l'hexagone.
     *
     * @return Le nombre de planètes contenues.
     */
    public int getPlanetContained() {
        return planetContained;
    }

    int[][] adjacents;

    public static int[][] plateau = {
            { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 }, { 2, 0 }, { 2, 1 },
            { 0, 2 }, { 0, 3 }, { 1, 2 }, { 1, 3 }, { 2, 2 }, { 2, 3 },
            { 0, 4 }, { 0, 5 }, { 1, 4 }, { 1, 5 }, { 2, 4 }, { 2, 5 },
            { 3, 0 }, { 3, 1 }, { 5, 0 }, { 5, 1 },
            { 3, 2 }, { 3, 3 }, { 4, 0 }, { 5, 2 }, { 5, 3 },
            { 3, 4 }, { 3, 5 }, { 5, 4 }, { 5, 5 },
            { 6, 0 }, { 6, 1 }, { 7, 0 }, { 7, 1 }, { 8, 0 }, { 8, 1 },
            { 6, 2 }, { 6, 3 }, { 7, 2 }, { 7, 3 }, { 8, 2 }, { 8, 3 },
            { 6, 4 }, { 6, 5 }, { 7, 4 }, { 7, 5 }, { 8, 4 }, { 8, 5 },
    };
    /**
     * Permet de définir les hexagones adjacents
     */
    public static int[][][] plateauAdj = {
            { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 }, { 2, 0 }, { 2, 1 } },
            { { 0, 2 }, { 0, 3 }, { 1, 2 }, { 1, 3 }, { 2, 2 }, { 2, 3 } },
            { { 0, 4 }, { 0, 5 }, { 1, 4 }, { 1, 5 }, { 2, 4 }, { 2, 5 } },
            { { 3, 0 }, { 3, 1 }, { 4, 0 }, { 5, 0 }, { 5, 1 } },
            { { 3, 2 }, { 3, 3 }, { 4, 0 }, { 4, 0 }, { 5, 2 }, { 5, 3 } },
            { { 3, 4 }, { 3, 5 }, { 4, 0 }, { 5, 4 }, { 5, 5 } },
            { { 6, 0 }, { 6, 1 }, { 7, 0 }, { 7, 1 }, { 8, 0 }, { 8, 1 } },
            { { 6, 2 }, { 6, 3 }, { 7, 2 }, { 7, 3 }, { 8, 2 }, { 8, 3 } },
            { { 6, 4 }, { 6, 5 }, { 7, 4 }, { 7, 5 }, { 8, 4 }, { 8, 5 } },
    };

    /**
     * Méthode permettant de retrouver l'id d'un hexagone (celui visible pour le
     * joueur), à partir de l'id du secteur et l'id de
     * l'hexagone dans le secteur
     * 
     * @param array
     * @param target
     * @return
     */
    public static int findIndex(int[][] array, int[] target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i][0] == target[0] && array[i][1] == target[1]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Obtient les hexagones adjacents.
     *
     * @return Un tableau d'hexagones adjacents.
     */
    public int[][] getAdjacents() {
        return adjacents;
    }

    /**
     * Vérifie si un hexagone est contenu dans une liste d'hexagones.
     *
     * @param res   La liste d'hexagones.
     * @param idSec L'identifiant du secteur.
     * @param idHex L'identifiant de l'hexagone.
     * @return true si l'hexagone est contenu dans la liste, false sinon.
     */
    public boolean resContains(List<int[]> res, int idSec, int idHex) {
        boolean[] answ = { false };
        res.forEach((elt) -> {
            if (elt[0] == idSec && elt[1] == idHex) {
                answ[0] = true;
            }
        });
        return answ[0];
    }

    /**
     * Définit les hexagones adjacents.
     */
    public void setAdjacents() {
        List<int[]> res = new ArrayList<int[]>();
        if (this.id == 0 && this.idSector == 4) {
            res.add(new int[] { 3, 1 });
            res.add(new int[] { 3, 3 });
            res.add(new int[] { 3, 5 });
            res.add(new int[] { 1, 4 });
            res.add(new int[] { 1, 5 });
            res.add(new int[] { 5, 0 });
            res.add(new int[] { 5, 2 });
            res.add(new int[] { 5, 4 });
            res.add(new int[] { 7, 0 });
            res.add(new int[] { 7, 1 });
            this.adjacents = res.toArray(new int[0][]);
            return;
        }
        for (int i = 0; i < Hex.plateauAdj.length; i++) {
            for (int j = 0; j < Hex.plateauAdj[i].length; j++) {
                if (Hex.plateauAdj[i][j][0] == idSector && Hex.plateauAdj[i][j][1] == id) {
                    try {
                        if (!resContains(res, Hex.plateauAdj[i][j - 1][0], Hex.plateauAdj[i][j - 1][1])) {
                            res.add(Hex.plateauAdj[i][j - 1]);
                        }
                    } catch (Exception e) {
                    }
                    try {
                        if (!resContains(res, Hex.plateauAdj[i][j + 1][0], Hex.plateauAdj[i][j + 1][1])) {
                            res.add(Hex.plateauAdj[i][j + 1]);
                        }
                    } catch (Exception e) {
                    }
                    try {
                        if (!resContains(res, Hex.plateauAdj[i + 1][i % 2 == 0 ? j - 1 : j][0],
                                Hex.plateauAdj[i + 1][i % 2 == 0 ? j - 1 : j][1])) {
                            res.add(Hex.plateauAdj[i + 1][i % 2 == 0 ? j - 1 : j]);
                        }
                    } catch (Exception e) {
                    }
                    try {
                        if (!resContains(res, Hex.plateauAdj[i + 1][i % 2 == 0 ? j : j + 1][0],
                                Hex.plateauAdj[i + 1][i % 2 == 0 ? j : j + 1][1])) {
                            res.add(Hex.plateauAdj[i + 1][i % 2 == 0 ? j : j + 1]);
                        }
                    } catch (Exception e) {
                    }
                    try {
                        if (!resContains(res, Hex.plateauAdj[i - 1][i % 2 == 0 ? j - 1 : j][0],
                                Hex.plateauAdj[i - 1][i % 2 == 0 ? j - 1 : j][1])) {
                            res.add(Hex.plateauAdj[i - 1][i % 2 == 0 ? j - 1 : j]);
                        }
                    } catch (Exception e) {
                    }
                    try {
                        if (!resContains(res, Hex.plateauAdj[i - 1][i % 2 == 0 ? j : j + 1][0],
                                Hex.plateauAdj[i - 1][i % 2 == 0 ? j : j + 1][1])) {
                            res.add(Hex.plateauAdj[i - 1][i % 2 == 0 ? j : j + 1]);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        this.adjacents = res.toArray(new int[0][]);
    }

    /**
     * Juste pour faire des tests
     *
     * @param args Les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        System.out.println("null");
        System.out.println(Hex.plateau[4][0] + " " + Hex.plateau[4][1]);
    }
}
