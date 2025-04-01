Jeu d'Échecs – Projet en Java
Description
Ce projet est une implémentation du jeu d’échecs en Java, intégrant toutes les règles classiques ainsi que des variantes originales. 
Il offre une interface graphique intuitive permettant aux joueurs de s’affronter avec des fonctionnalités avancées comme le roque, 
la prise en passant, et la promotion des pions.

-Fonctionnalités principales
   Jeu d’échecs avec toutes les règles officielles
   Interface graphique interactive
   Détection de mise en échec et échec et mat
   Variantes avec de nouvelles pièces féeriques
   Possibilité pour un joueur de jouer deux fois d’affilée

-Architecture du projet
  Modélisation des pièces et du plateau
  Classe Grid : Contient la représentation interne du plateau sous forme de tableau d’objets.
  
  Classe PieceJeu (parent) et sous-classes (Tour, Pion, Cavalier…) : Définissent les déplacements et règles de chaque pièce.

-Interface graphique
  Classe Damier : Gère l’affichage du plateau et la gestion des clics pour déplacer les pièces.
  
  Classe Home : Permet de choisir le mode de jeu et la taille du damier.
  
  Contrôleur et gestion des règles
  Déplacements validés en fonction des règles du jeu
  
  Détection d’échec et mat (deplacement_RoiPossible)
  
  Gestion du roque, prise en passant et promotion

-Variantes ajoutées
  Nouvelles pièces féeriques :
  
  Catapulte : Attaque uniquement à l’horizontale, peut viser derrière une pièce.
  
  Jeune Fou : Se déplace en diagonale, attaque verticalement et horizontalement.
  
  Pion avancé : Peut avancer et reculer, attaque dans les deux sens.
  
  Paladin : Se déplace comme un roi, mais peut attaquer à distance.
  
  Mécanique spéciale : un joueur peut jouer deux fois d’affilée

-Lancer le jeu
  Cloner le dépôt 
  Compiler et exécuter le projet :
    javac *.java
    java LauncherJeu
