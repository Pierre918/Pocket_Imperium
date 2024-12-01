package command;

import java.util.Scanner;

public class Command {
    /**
     * Attribut permettant de créer qu'une instance de Scanner pour toute la partie
     */
    static Scanner scanner = new Scanner(System.in);

    /**
     * Permet de centraliser les interactions avec l'utilisateur et la gestion d'erreur dans une seule méthode.
     * @param boundInf
     * @param boundSup
     * @param errMessage
     * @return
     */
    public static int askInteger(int boundInf, int boundSup, String errMessage) {
        int ans = -2;
        int i=0;
        while (ans <= boundInf || ans >= boundSup) {
            if (i>0){
                System.out.println(errMessage + ". Resaisissez");
            }
            try {
                ans = scanner.nextInt();
            } catch (Exception e) {
                ans = -2;
                scanner.next();
            }
            i++;
        }
        return ans;
    }
}
