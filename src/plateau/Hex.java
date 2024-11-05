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
    public int[][][] plateau_adj = {
        {{ 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 }, { 2, 0 }, { 2, 1 }},
        {{ 0, 2 }, { 0, 3 }, { 1, 2 }, { 1, 3 }, { 2, 2 }, { 2, 3 }},
        {{ 0, 4 }, { 0, 5 }, { 1, 4 }, { 1, 5 }, { 2, 4 }, { 2, 5 }},
        {{ 3, 0 }, { 3, 1 },{ 4, 0 }, { 5, 0 }, { 5, 1 }},
        {{ 3, 2 }, { 3, 3 }, { 4, 0 },{ 4, 0 }, { 5, 2 }, { 5, 3 }},
        {{ 3, 4 }, { 3, 5 }, { 4, 0 },{ 5, 4 }, { 5, 5 }},
        {{ 6, 0 }, { 6, 1 }, { 7, 0 }, { 7, 1 }, { 8, 0 }, { 8, 1 }}, 
        {{ 6, 2 }, { 6, 3 }, { 7, 2 }, { 7, 3 }, { 8, 2 }, { 8, 3 }}, 
        {{ 6, 4 }, { 6, 5 }, { 7, 4 }, { 7, 5 }, { 8, 4 }, { 8, 5 }}, 
};

    /**
     * @return Hex[]
     * renvoi {{id_sector,id_hex},{...}} des hexagones adjacents
     */
    public int[][] getAdjacents() {
        return adjacents; 
    }

    public boolean res_contains(List<int[]> res,int id_sec,int id_hex){
        boolean[] answ={false};
        res.forEach((elt)->{
            if(elt[0]==id_sec && elt[1]==id_hex){
                answ[0]=true;
            }
        });
        return answ[0];
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
            return;
        }
        for (int i = 0; i < this.plateau_adj.length; i++) {
            for (int j = 0; j < this.plateau_adj[i].length; j++) {
                if (this.plateau_adj[i][j][0] == id_sector && this.plateau_adj[i][j][1] == id) {

                    System.out.println("coucou");
                    try {
                        if (!res_contains(res,this.plateau_adj[i][j - 1][0],this.plateau_adj[i][j - 1][1]))
                        {res.add(this.plateau_adj[i][j - 1]);}
                    } catch (Exception e) {
                        // System.out.println(e);
                        // System.out.println("res.add(adj[i][j-1]);");
                    }
                    try {
                        if (!res_contains(res,this.plateau_adj[i][j + 1][0],this.plateau_adj[i][j + 1][1]))
                        {res.add(this.plateau_adj[i][j + 1]);}
                    } catch (Exception e) {
                        // System.out.println(e);
                        // System.out.println("res.add(adj[i][j+1]);");
                    }
                    try {
                        if (!res_contains(res,this.plateau_adj[i + 1][i % 2 == 0 ? j - 1 : j][0],this.plateau_adj[i + 1][i % 2 == 0 ? j - 1 : j][1]))
                        {res.add(this.plateau_adj[i + 1][i % 2 == 0 ? j - 1 : j]);}
                    } catch (Exception e) {
                        // System.out.println(e);
                        // System.out.println("res.add(adj[i+1][i%2==0?j-1:j]);");
                    }
                    try {
                        if(!res_contains(res,this.plateau_adj[i + 1][i % 2 == 0 ? j : j + 1][0],this.plateau_adj[i + 1][i % 2 == 0 ? j : j + 1][1]))
                        {res.add(this.plateau_adj[i + 1][i % 2 == 0 ? j : j + 1]);}
                    } catch (Exception e) {
                        // System.out.println(e);
                        // System.out.println("res.add(adj[i+1][i%2==0?j:j+1]);");
                    }
                    try {
                        if(!res_contains(res,this.plateau_adj[i - 1][i % 2 == 0 ? j - 1 : j][0],this.plateau_adj[i - 1][i % 2 == 0 ? j - 1 : j][1]))
                        {res.add(this.plateau_adj[i - 1][i % 2 == 0 ? j - 1 : j]);}
                    } catch (Exception e) {
                        // System.out.println(e);
                        // System.out.println("res.add(adj[i-1][i%2==0?j-1:j]);");
                    }
                    try {
                        if(!res_contains(res,this.plateau_adj[i - 1][i % 2 == 0 ? j : j + 1][0],this.plateau_adj[i - 1][i % 2 == 0 ? j : j + 1][1]))
                        {res.add(this.plateau_adj[i - 1][i % 2 == 0 ? j : j + 1]);}
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
        System.out.println("null");
        Hex hex = new Hex(0);
        hex.setId(4);
        hex.setId_sector(5);
        hex.setAdjacents();
        int[][] hexs=hex.getAdjacents();
        for (int i=0;i<hexs.length;i++){
            
        System.out.print(hexs[i][0]);
        System.out.println(hexs[i][1]);
        }
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
