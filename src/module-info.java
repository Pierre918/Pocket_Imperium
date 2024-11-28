/**
 * Pocket Imperium est un jeu de stratégie et de gestion où les joueurs incarnent des leaders de factions spatiales en compétition pour la domination galactique. Chaque joueur doit gérer ses ressources, développer ses technologies et conquérir des territoires pour étendre son empire. Le jeu combine des éléments de diplomatie, de combat et de développement économique, offrant une expérience riche et immersive dans un univers de science-fiction.
 * Ce projet modélise ce jeu via ligne de commande
 * @author Pierre, Charlie
 * @since 1.0
 */
module jeu{
    requires transitive java.desktop;
    exports partie;
    exports vaisseaux;
    exports plateau;
    exports joueurs;
}