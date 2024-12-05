package plateau;

import java.util.List;

import partie.Partie;

public class Sector {
    public Hex[] hex;

    /**
     * Méthode permettant de "retourner" une carte dans le cas où elle était définie
     * comme une carte étant en haut du plateau
     * et se retrouve finalement en bas avec la disposition aléatoire
     * 
     * @param list
     * @param hexGH
     * @param hexMH
     * @param hexDH
     * @param hexGB
     * @param hexMB
     * @param hexDB
     * @return List<Hex[]>
     */
    public static List<Hex[]> retournerCarte(List<plateau.Hex[]> list, plateau.Hex[] hexGH, plateau.Hex[] hexMH,
            plateau.Hex[] hexDH,
            plateau.Hex[] hexGB, plateau.Hex[] hexMB, plateau.Hex[] hexDB) {
        if (list.subList(list.size() - 3, list.size()).contains(hexGH)) {
            int i = list.indexOf(hexGH);
            plateau.Hex temp = hexGH[0];
            hexGH[0] = hexGH[5];
            hexGH[5] = temp;
            temp = hexGH[1];
            hexGH[1] = hexGH[4];
            hexGH[4] = temp;
            temp = hexGH[2];
            hexGH[2] = hexGH[3];
            hexGH[3] = temp;
            list.set(i, hexGH);
        }
        if (list.subList(list.size() - 3, list.size()).contains(hexMH)) {
            int i = list.indexOf(hexMH);
            plateau.Hex temp = hexMH[0];
            hexMH[0] = hexMH[5];
            hexMH[5] = temp;
            temp = hexMH[1];
            hexMH[1] = hexMH[4];
            hexMH[4] = temp;
            temp = hexMH[2];
            hexMH[2] = hexMH[3];
            hexMH[3] = temp;
            list.set(i, hexMH);
        }
        if (list.subList(list.size() - 3, list.size()).contains(hexDH)) {
            int i = list.indexOf(hexDH);
            plateau.Hex temp = hexDH[0];
            hexDH[0] = hexDH[5];
            hexDH[5] = temp;
            temp = hexDH[1];
            hexDH[1] = hexDH[4];
            hexDH[4] = temp;
            temp = hexDH[2];
            hexDH[2] = hexDH[3];
            hexDH[3] = temp;
            list.set(i, hexDH);
        }
        if (list.subList(0, 3).contains(hexGB)) {
            int i = list.indexOf(hexGB);
            plateau.Hex temp = hexGB[0];
            hexGB[0] = hexGB[5];
            hexGB[5] = temp;
            temp = hexGB[1];
            hexGB[1] = hexGB[4];
            hexGB[4] = temp;
            temp = hexGB[2];
            hexGB[2] = hexGB[3];
            hexGB[3] = temp;
            list.set(i, hexGB);
        }
        if (list.subList(0, 3).contains(hexMB)) {
            int i = list.indexOf(hexMB);
            plateau.Hex temp = hexMB[0];
            hexMB[0] = hexMB[5];
            hexMB[5] = temp;
            temp = hexMB[1];
            hexMB[1] = hexMB[4];
            hexMB[4] = temp;
            temp = hexMB[2];
            hexMB[2] = hexMB[3];
            hexMB[3] = temp;
            list.set(i, hexMB);
        }
        if (list.subList(0, 3).contains(hexDB)) {
            int i = list.indexOf(hexDB);
            plateau.Hex temp = hexDB[0];
            hexDB[0] = hexDB[5];
            hexDB[5] = temp;
            temp = hexDB[1];
            hexDB[1] = hexDB[4];
            hexDB[4] = temp;
            temp = hexDB[2];
            hexDB[2] = hexDB[3];
            hexDB[3] = temp;
            list.set(i, hexDB);
        }
        return list;
    }

    /**
     * Constructeur de la classe
     * 
     * @param hex
     */
    public Sector(Hex[] hex) {
        this.hex = hex;
    }

    /**
     * Connaitre le nombre de secteur occupé
     * @return
     */
    public static int nbSectorTaken(){
        Partie partie = Partie.getInstance();
        int ans=0;
        for (int i=0;i<9;i++){
            if (partie.sectorIsTaken(partie.sector[i]) && i!=4){
                ans++;
            }
        }
        return ans;
    }
}
