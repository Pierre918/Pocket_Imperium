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

    Hex[] adjacents;

    public void setAdjacents(Hex[] adjacents) {
        this.adjacents = adjacents;
    }

    /**
     * @return Hex[]
     */
    public Hex[] getAdjacents() {
        return adjacents;
    }

    public void addAdjacents(Hex[] new_adjacents) {
        for (int i = 0; i < new_adjacents.length; i++) {
            for (int j = 0; j < this.adjacents.length; j++) {
                if (this.adjacents[j] != null) {
                    this.adjacents[j] = new_adjacents[i];
                }
            }
        }
    }

    /**
     * @param sectors
     */
    public int[][] get_adjacents() {
        // Hex[]
        // hexs={sectors[0].getHex()[0],sectors[0].getHex()[1],sectors[0].getHex()[2],sectors[0].getHex()[3],sectors[0].getHex()[4],sectors[0].getHex()[5],};
        // hexs[0].setAdjacents(new plateau.Hex[]
        // {sectors[3].getHex()[1],sectors[3].getHex()[2],sectors[3].getHex()[3],sectors[0].getHex()[4],sectors[0].getHex()[5]});
        // hexs[1].setAdjacents(new plateau.Hex[]
        // {sectors[3].getHex()[0],sectors[3].getHex()[2],sectors[3].getHex()[3],sectors[0].getHex()[5],sectors[1].getHex()[4],sectors[4].getHex()[0]});
        // hexs[2].setAdjacents(new plateau.Hex[]
        // {sectors[3].getHex()[0],sectors[3].getHex()[3],sectors[3].getHex()[4]});
        // hexs[3].setAdjacents(new plateau.Hex[]
        // {sectors[3].getHex()[0],sectors[3].getHex()[1],sectors[3].getHex()[2],sectors[3].getHex()[4],sectors[3].getHex()[5],sectors[4].getHex()[0],});
        // hexs[4].setAdjacents(new plateau.Hex[]
        // {sectors[3].getHex()[2],sectors[3].getHex()[3],sectors[3].getHex()[5],sectors[6].getHex()[0],sectors[6].getHex()[2],});
        // hexs[5].setAdjacents(new plateau.Hex[]
        // {sectors[3].getHex()[1],sectors[3].getHex()[2],sectors[3].getHex()[3],sectors[6].getHex()[1],sectors[4].getHex()[0],sectors[7].getHex()[0],});
        // sectors[0].setHex(hexs);

        // hexs = new
        // Hex[]{sectors[5].getHex()[0],sectors[5].getHex()[1],sectors[5].getHex()[2],sectors[5].getHex()[3],sectors[5].getHex()[4],sectors[5].getHex()[5]};
        // hexs[0].setAdjacents(new plateau.Hex[]
        // {sectors[5].getHex()[1],sectors[5].getHex()[2],sectors[4].getHex()[0],sectors[2].getHex()[4],sectors[1].getHex()[5],});
        // hexs[1].setAdjacents(new plateau.Hex[]
        // {sectors[5].getHex()[0],sectors[5].getHex()[2],sectors[5].getHex()[3],sectors[2].getHex()[4],sectors[2].getHex()[5],});
        // hexs[2].setAdjacents(new plateau.Hex[]
        // {sectors[5].getHex()[0],sectors[5].getHex()[1],sectors[5].getHex()[3],sectors[5].getHex()[4],sectors[5].getHex()[5],sectors[4].getHex()[0],});
        // hexs[3].setAdjacents(new plateau.Hex[]
        // {sectors[5].getHex()[1],sectors[5].getHex()[2],sectors[5].getHex()[5],});
        // hexs[4].setAdjacents(new plateau.Hex[]
        // {sectors[5].getHex()[2],sectors[5].getHex()[5],sectors[4].getHex()[0],sectors[7].getHex()[1],sectors[8].getHex()[0],});
        // hexs[5].setAdjacents(new plateau.Hex[]
        // {sectors[5].getHex()[2],sectors[5].getHex()[3],sectors[5].getHex()[4],sectors[3].getHex()[0],});
        // sectors[5].setHex(hexs);

        // hexs = new
        // Hex[]{sectors[5].getHex()[0],sectors[5].getHex()[1],sectors[5].getHex()[2],sectors[5].getHex()[3],sectors[5].getHex()[4],sectors[5].getHex()[5]};
        // sectors[0].getHex()[0].setAdjacents(new plateau.Hex[]
        // {sectors[0].getHex()[1],sectors[0].getHex()[2]});
        // sectors[0].getHex()[1].setAdjacents(new plateau.Hex[]
        // {sectors[0].getHex()[0],sectors[0].getHex()[2],sectors[0].getHex()[3],});
        // sectors[0].getHex()[2].setAdjacents(new plateau.Hex[]
        // {sectors[0].getHex()[0],sectors[0].getHex()[1],sectors[0].getHex()[3],sectors[0].getHex()[4],sectors[0].getHex()[5]});
        // sectors[0].getHex()[3].setAdjacents(new plateau.Hex[]
        // {sectors[0].getHex()[1],sectors[0].getHex()[2],sectors[0].getHex()[5]});
        // sectors[0].getHex()[4].setAdjacents(new plateau.Hex[]
        // {sectors[0].getHex()[2],sectors[0].getHex()[5]});
        // sectors[0].getHex()[5].setAdjacents(new plateau.Hex[]
        // {sectors[0].getHex()[2],sectors[0].getHex()[3],sectors[0].getHex()[4]});
        // return sectors;

        int[][][] adj = {
                { { 0, 0 }, { 0, 1 }, { 1, 0 } },
                { { 0, 2 }, { 0, 3 }, { 1, 2 } },
        };
        List<int[]> res = new ArrayList<int[]>();
        for (int i = 0; i < adj.length; i++) {
            for (int j = 0; j < adj[i].length; j++) {
                if (adj[i][j][0] ==  id_sector && adj[i][j][1] ==  id) {
                    
                        System.out.println("coucou");
                    int is_corner=0;
                    try{
                        res.add(adj[i][j-1]);
                    }
                    catch (Exception e){
                        //System.out.println(e);
                        //System.out.println("res.add(adj[i][j-1]);");
                    }
                    try{
                        res.add(adj[i][j+1]);
                    }
                    catch (Exception e){
                        //System.out.println(e);
                        //System.out.println("res.add(adj[i][j+1]);");
                    }    
                    try{
                        res.add(adj[i+1][i%2==0?j-1:j]);
                    }
                    catch (Exception e){
                        //System.out.println(e);
                        //System.out.println("res.add(adj[i+1][i%2==0?j-1:j]);");
                    }    
                    try{
                        res.add(adj[i+1][i%2==0?j:j+1]);
                    }
                    catch (Exception e){
                        //System.out.println(e);
                        //System.out.println("res.add(adj[i+1][i%2==0?j:j+1]);");
                    }    
                    try{
                        res.add(adj[i-1][i%2==0?j-1:j]);
                    }
                    catch (Exception e){
                        //System.out.println(e);
                        //System.out.println("res.add(adj[i-1][i%2==0?j-1:j]);");
                    }      
                    try{
                        res.add(adj[i-1][i%2==0?j:j+1]);
                    }
                    catch (Exception e){
                        //System.out.println(e);
                        //System.out.println("res.add(adj[i-1][i%2==0?j:j+1]);");
                    }
                    
                }
                
            }
        }
        return res.toArray(new int[0][]);
    }

    /**
     * @param planet_contained
     */
    public Hex(int planet_contained) {
        this.planet_contained = planet_contained;
        setAdjacents(planet_contained == 3 ? new Hex[] { null, null, null, null, null, null, null, null, null, null }
                : new Hex[] { null, null, null, null, null, null });
    }
    public static void main(String[] args) {
        Hex hex = new Hex(0);
        hex.setId(1);
        hex.setId_sector(0);
        //System.out.println(hex.get_adjacents().length);
        System.out.print(hex.get_adjacents()[0][0]);
        
        System.out.println(hex.get_adjacents()[0][1]);
        System.out.print(hex.get_adjacents()[1][0]);
        System.out.println(hex.get_adjacents()[1][1]);
        System.out.print(hex.get_adjacents()[2][0]);
        System.out.println(hex.get_adjacents()[2][1]);
        System.out.print(hex.get_adjacents()[3][0]);
        System.out.println(hex.get_adjacents()[3][1]);
        System.out.print(hex.get_adjacents()[4][0]);
        System.out.println(hex.get_adjacents()[4][1]);
        System.out.println("null");
    }
}
