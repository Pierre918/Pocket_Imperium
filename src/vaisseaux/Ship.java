package vaisseaux;

import joueurs.Joueur;

public class Ship {
    // Fleet fleet;
    public Joueur joueur;
    private Joueur owner;
    public Ship(Joueur joueur){
        this.joueur=joueur;
    }

    /**
     * Retourne le joueur propriétaire du vaisseau.
     *
     * @return Le joueur propriétaire.
     */
    public Joueur getOwner() {
        return owner;
    }

    /**
     * Définit le joueur propriétaire du vaisseau.
     *
     * @param owner Le joueur propriétaire.
     */
    public void setOwner(Joueur owner) {
        this.owner = owner;
    }
}
