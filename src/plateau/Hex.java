package plateau;

import java.util.ArrayList;
import java.util.List;

public class Hex {
    private int planet_contained;
    private int id;
    private int id_sector;

    /**
     * @return int
     */
    public int getId_sector() {
        return id_sector;
    }

    /**
     * @param id_sector
     */
    public void setId_sector(int id_sector) {
        this.id_sector = id_sector;
    }

    public int getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return int
     */
    public int getPlanet_contained() {
        return planet_contained;
    }

    int[][] adjacents;
    private int[][] plateau = {
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

    public int[] num_to_pos(int num) {
        int[] res = { -1, -1 };
        for (int i = 0; i < this.plateau.length; i++) {
            System.out.println("i : " + i);
            for (int j = 0; j < this.plateau[i].length; j++) {
                if (i == 0) {
                    if (j == num) {
                        res = plateau[i];
                        return res;
                    }
                    continue;
                }
                if ((int) j + (i % 2 == 0 ? ((i / 2) * 6) + ((i - (i / 2) + 1) * 6)
                        : (Math.floor((double) i / 2) * 6) + (Math.ceil((double) i / 2) * 6)) == num) {
                    res = plateau[i][j];
                    return res;
                }
            }
        }
        return res;
    }

    /**
     * @return Hex[]
     */
    public int[][] getAdjacents() {
        return adjacents;
    }

    /**
     * @param sectors
     */
    public void setAdjacents() {

        List<int[]> res = new ArrayList<int[]>();
        if (this.id == 0 && this.id_sector == 4) {
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
        }
        for (int i = 0; i < this.plateau.length; i++) {
            for (int j = 0; j < this.plateau[i].length; j++) {
                if (this.plateau[i][j][0] == id_sector && this.plateau[i][j][1] == id) {

                    System.out.println("coucou");
                    int is_corner = 0;
                    try {
                        res.add(this.plateau[i][j - 1]);
                    } catch (Exception e) {
                        // System.out.println(e);
                        // System.out.println("res.add(adj[i][j-1]);");
                    }
                    try {
                        res.add(this.plateau[i][j + 1]);
                    } catch (Exception e) {
                        // System.out.println(e);
                        // System.out.println("res.add(adj[i][j+1]);");
                    }
                    try {
                        res.add(this.plateau[i + 1][i % 2 == 0 ? j - 1 : j]);
                    } catch (Exception e) {
                        // System.out.println(e);
                        // System.out.println("res.add(adj[i+1][i%2==0?j-1:j]);");
                    }
                    try {
                        res.add(this.plateau[i + 1][i % 2 == 0 ? j : j + 1]);
                    } catch (Exception e) {
                        // System.out.println(e);
                        // System.out.println("res.add(adj[i+1][i%2==0?j:j+1]);");
                    }
                    try {
                        res.add(this.plateau[i - 1][i % 2 == 0 ? j - 1 : j]);
                    } catch (Exception e) {
                        // System.out.println(e);
                        // System.out.println("res.add(adj[i-1][i%2==0?j-1:j]);");
                    }
                    try {
                        res.add(this.plateau[i - 1][i % 2 == 0 ? j : j + 1]);
                    } catch (Exception e) {
                        // System.out.println(e);
                        // System.out.println("res.add(adj[i-1][i%2==0?j:j+1]);");
                    }

                }

            }
        }
        this.adjacents = res.toArray(new int[0][]);
    }

    /**
     * @param planet_contained
     */
    public Hex(int planet_contained) {
        this.planet_contained = planet_contained;
    }

    public static void main(String[] args) {
        Hex hex = new Hex(0);
        hex.setId(1);
        hex.setId_sector(0);
        hex.setAdjacents();

        System.out.println(hex.num_to_pos(10)[0]);
        System.out.println(hex.num_to_pos(10)[1]);
        // System.out.print(hex.getAdjacents()[0][0]);

        // System.out.println(hex.getAdjacents()[0][1]);
        // System.out.print(hex.getAdjacents()[1][0]);
        // System.out.println(hex.getAdjacents()[1][1]);
        // System.out.print(hex.getAdjacents()[2][0]);
        // System.out.println(hex.getAdjacents()[2][1]);
        // System.out.print(hex.getAdjacents()[3][0]);
        // System.out.println(hex.getAdjacents()[3][1]);
        // System.out.print(hex.getAdjacents()[4][0]);
        // System.out.println(hex.getAdjacents()[4][1]);
        System.out.println("null");
    }
}
