import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * 
 */
public class AES {
	
	/*
	 * constants
	 */
	static int taille_bloc = 128;
	static int [] Rcon = {
			0x00, // Indice 0 inutilisé
		    0x01, // RC1
		    0x02, // RC2
		    0x04, // RC3
		    0x08, // RC4
		    0x10, // RC5
		    0x20, // RC6
		    0x40, // RC7
		    0x80, // RC8
		    0x1B, // RC9
		    0x36  // RC10
	};
	static int [][] sBox = {
			{99,124,119,123,242,107,111,197,48,1,103,43,254,215,171,118},
			{202,130,201,125,250,89,71,240,173,212,162,175,156,164,114,192},
			{183,253,147,38,54,63,247,204,52,165,229,241,113,216,49,21},
			{4,199,35,195,24,150,5,154,7,18,128,226,235,39,178,117},
			{9,131,44,26,27,110,90,160,82,59,214,179,41,227,47,132},
			{83,209,0,237,32,252,177,91,106,203,190,57,74,76,88,207},
			{208,239,170,251,67,77,51,133,69,249,2,127,80,60,159,168},
			{81,163,64,143,146,157,56,245,188,182,218,33,16,255,243,210},
			{205,12,19,236,95,151,68,23,196,167,126,61,100,93,25,115},
			{96,129,79,220,34,42,144,136,70,238,184,20,222,94,11,219},
			{224,50,58,10,73,6,36,92,194,211,172,98,145,149,228,121},
			{231,200,55,109,141,213,78,169,108,86,244,234,101,122,174,8},
			{186,120,37,46,28,166,180,198,232,221,116,31,75,189,139,138},
			{112,62,181,102,72,3,246,14,97,53,87,185,134,193,29,158},
			{225,248,152,17,105,217,142,148,155,30,135,233,206,85,40,223},
			{140,161,137,13,191,230,66,104,65,153,45,15,176,84,187,22}
	};
	static int [][] sBoxInv = {
			{82,9,106,213,48,54,165,56,191,64,163,158,129,243,215,251},
			{124,227,57,130,155,47,255,135,52,142,67,68,196,222,233,203},
			{84,123,148,50,166,194,35,61,238,76,149,11,66,250,195,78},
			{8,46,161,102,40,217,36,178,118,91,162,73,109,139,209,37},
			{114,248,246,100,134,104,152,22,212,164,92,204,93,101,182,146},
			{108,112,72,80,253,237,185,218,94,21,70,87,167,141,157,132},
			{144,216,171,0,140,188,211,10,247,228,88,5,184,179,69,6},
			{208,44,30,143,202,63,15,2,193,175,189,3,1,19,138,107},
			{58,145,17,65,79,103,220,234,151,242,207,206,240,180,230,115},
			{150,172,116,34,231,173,53,133,226,249,55,232,28,117,223,110},
			{71,241,26,113,29,41,197,137,111,183,98,14,170,24,190,27},
			{252,86,62,75,198,210,121,32,154,219,192,254,120,205,90,244},
			{31,221,168,51,136,7,199,49,177,18,16,89,39,128,236,95},
			{96,81,127,169,25,181,74,13,45,229,122,159,147,201,156,239},
			{160,224,59,77,174,42,245,176,200,235,187,60,131,83,153,97},
			{23,43,4,126,186,119,214,38,225,105,20,99,85,33,12,125}
	};
	
	/*
	 * attributes
	 */
	private int taille_cle_maitre;
	private int nb_ronde;
	private int [][] master_Key;
	private int[][][] keys;
	
	/**
	 * Constructor
	 * 
	 * @param tcl  (128,192,256)
	 */
	public AES(int tcl) {
		if (tcl != 128 && tcl != 192 && tcl != 256) {
		        throw new IllegalArgumentException("La taille de clé doit être 128, 192 ou 256 bits");
		}
		this.taille_cle_maitre = tcl;
		//Le nombre de ronde dépend de la taille de la clé
		if (tcl==128) {
			this.nb_ronde = 10;
		}
		else if (tcl==192) {
			this.nb_ronde = 12;
		}
		else {
			this.nb_ronde = 14;
		}
		this.genereMasterKey();
		this.createKeys(this.master_Key);
	}
	
	
	/**
	 * Permet de transformer une chaine de caractere en un tableau d'entier (0 et 1)
	 * 
	 * @param message
	 * @return le tab de bits representant le message
	 */
	public int [] stringToBits(String message) {
		byte [] bytes = message.getBytes(StandardCharsets.UTF_8);  // recupere les octets du mess et les mets sous forme de tableau
		int[] bits = new int[bytes.length * 8];  //chaque octet contient 8 bits donc il nous faut la taille du mot *8 cases dans notre tableau
		int index = 0; // compteur pour avancer case par case dans le tableau de bits
		// Parcourt tous les octets du message
		for (int i = 0; i < bytes.length ; i++ ) {
			// Parcourt les bits de l'octet du plus fort (7) au plus faible (0)
			for (int j = 7; j >= 0; j--) {
				bits[index] = (bytes[i] >> j) & 1;   // >> // decale l'octet de j positions vers la droite et &1 permet de garder que le dernier bit
				index ++;
			}
		}
		return bits;
	}
	
	
	/**
	 * Permet de transforer un tableau d'entier (0 et1) en chaine de caractere
	 * 
	 * @param blocs
	 * @return la chaine de caractere representant le blocs en bits
	 */
	public String bitsToString(int[] blocs) {
		int nombrebytes = blocs.length / 8;   // 8 bits correspondent à 1 octet
		byte [] bytes = new byte [nombrebytes]; //tab qui va contenir les octets reconstruits
		int index = 0; // compteur pour avancer case par case dans le tableau de bits
		// Parcourt chaque octet que l'on doit reconstruire
		for (int i = 0; i < bytes.length ; i++ ) {
	        byte b = 0;   // Valeur de l'octet que l'on est en train de construire
	        // Parcourt les 8 bits correspondant à cet octet
	        for (int j = 0; j < 8; j++) {
	        	b = (byte) ((b << 1) | blocs[index]);  // Reconstruit l'octet bit par bit en décalant de 1 vers la gauche et en ajoutant le nouveau 0 ou 1
	        	index ++;
	        }
	        bytes[i] = b; // place l'octet reconstruit dans le tableau
		}
		return new String(bytes, StandardCharsets.UTF_8);
	}
	
	
	/**
	 * Permet de generer une cle maitre aleatoire de taille choisie (128 ou 192 ou 256)
	 * 
	 * @return la cle
	 */
	public int [][] genereMasterKey() {
		int lignes = 4; // la matrice de l'AES a toujours 4 lignes (4 octets par mot de cle)
		int colonnes = this.taille_cle_maitre/32;  // 1 mot = 32 bits donc le nombre de colonnes dépend de la taille de la cle
		int [][] cle = new int [lignes][colonnes]; // Cree la matrice de la cle maitre
		SecureRandom random = new SecureRandom();  // Generateur aleatoire securise
		// Remplit chaque case avec un octet aléatoire
		for (int i = 0; i < lignes; i++) {
			for (int j = 0; j < colonnes; j++) {
				cle[i][j] = random.nextInt(256);  // un octet peut prendre des valeurs de 0 à 255
			}
		}
		//Stocke la cle genere dans l'attribut 
		this.master_Key = cle;
		return cle;
	}
	

	/**
	 * Permet d'ajouter un bourrage pour que la taille du message soit un multiple de 128 bits
	 * Le bourrage commence par un bit 1, suivi de bits 0
	 * 
	 * @param bits Le message original représenté en bits
	 * @return le tableau contennat le message original en bits + le bourrage
	 */
	public int [] bourrage(int[] bits) {
		// Vérifie que le tableau bits ne soit pas null
		if (bits == null) {
		    throw new IllegalArgumentException("Le tableau de bits ne doit pas être nulll");
		}
		// Vérifie que les éléments du tableau bits soient bien des bits (1 ou 0)
		for (int bit : bits) {
		    if (bit != 0 && bit != 1) {
		        throw new IllegalArgumentException("Chaque élément doit être égal à 0 ou 1");
		    }
		}
		int reste = bits.length % 128;
		int bitsAAjouter = 128 - reste;  // calcule combien de bits il faut ajouter pour atteindre le prochain multiple de 128
		int [] resultat = new int [bits.length + bitsAAjouter]; // tab qui contiendra le message + le bourrage
		// Parcourt tous les bits du message original
		for (int i = 0; i < bits.length; i++) {
		    resultat[i] = bits[i]; // copie les bits du message original
		}
		resultat[bits.length] = 1;  // marqueur de fin du message
		// Les autres cases sont automatiquement remplies de 0 car qaund on crée un tab d'entiers avec new int[...], toutes ses cases sont initialisées à 0
		return resultat;	
	}
	
	
	
	/**
	 * Permet de retirer le bourrage ajouté à la fin du message pour retrouver le message original en bits
	 * 
	 * @param bits
	 * @return le message orignal sans le bourrage 
	 */
	public int [] debourrage(int[] bits) {
		// Vérifie que bits ne soit pas null
		if (bits == null) {
		    throw new IllegalArgumentException("Le tableau de bits ne doit pas être null");
		}
		int i = bits.length - 1 ;   // part du dernier bit du tableau
		// Ignore les 0 ajoutes a la fin
	    while (i >= 0 && bits[i] == 0) {
	        i--;
	    }
	    // Verifie qu'on a trouvé le marqueur 1
	    if (i < 0) {
	        throw new IllegalArgumentException("Marqueur de bourrage absent");
	    }
	    // Le marqueur se trouve a la position i
	    // Garde uniquement les bits qui le precedent
	    int[] resultat = new int[i];
	    for (int j = 0; j < i; j++) {
	        resultat[j] = bits[j];
	    }
	    return resultat;
		
	}
	
	
	/**
	 * Permet de découper le tableau de bits total (message binaire) en un ensemble de blocs (matrices State 4x4)
	 * 
	 * @param bitsTotal
	 * @return
	 */
	public int [][][] decoupage_en_blocs(int [] bitsTotal) {
		// Vérifications de bitsTotal
		if (bitsTotal == null) {
		    throw new IllegalArgumentException("Le tableau de bits ne doit pas être null");
		}
		if (bitsTotal.length % 128 != 0) {
		    throw new IllegalArgumentException("La longueur doit être un multiple de 128 bits");
		}
		for (int bit : bitsTotal) {
		    if (bit != 0 && bit != 1) {
		        throw new IllegalArgumentException("Chaque élément doit être égal à 0 ou 1");
		    }
		}
		int nombre_blocs = bitsTotal.length/128;  // calcule le nombre de blocs de 128 bits
		//tabl contenant tous les blocs
		int [][][] bloc = new int [nombre_blocs][4][4]; // chaque case de la matrice contiendra un octet représenté par un entier compris entre 0 et 255
		int indexBit = 0; // indique la position du prochain bit à lire dans le tableau bitsTotal.
		// Parcourt tous les blocs à construire
		for (int b=0; b<nombre_blocs;b++) {
			// Pour chaque bloc on construit la matrice State 4x4 par colonnes
			for (int j = 0; j<4; j++) {  //colonnes
				for (int i = 0; i<4; i++) { // lignes
					// on lit un octet (8 bits) a partir de bitsTotal
					int octet = 0;  // initialise la valeur de l'octet à reconstruire
					// Parcourt les 8 bits de l'octet actuel
					for (int k = 0; k<8; k++) {
						octet = (octet <<1) | bitsTotal[indexBit]; // decale les bits deja lus d'une position vers la gauche, puis ajoute le bit actuel
						indexBit++;
					}
					bloc[b][i][j] = octet;  // place l'octet reconstruit dans la matrice
				}
			}
		}
		return bloc;
		
	}
	
	
	
	/**
	 * Permet de réassembler un ensemble de blocs (matrices State) en un seul tableau de bits total
	 * 
	 * @param blocs
	 * @return
	 */
	public int [] recollage_des_blocs(int [][][] blocs) {
		//Vérifications de blocs
		if (blocs == null) {
		    throw new IllegalArgumentException("Le tableau de blocs ne doit pas être null");
		}
		for (int b = 0; b < blocs.length; b++) {
		    if (blocs[b] == null || blocs[b].length != 4) {
		        throw new IllegalArgumentException("Chaque bloc doit avoir 4 lignes");
		    }
		    for (int i = 0; i < 4; i++) {
		        if (blocs[b][i] == null || blocs[b][i].length != 4) {
		            throw new IllegalArgumentException("Chaque bloc doit être une matrice 4x4");
		        }
		        for (int j = 0; j < 4; j++) {
		            if (blocs[b][i][j] < 0 || blocs[b][i][j] > 255) {
		                throw new IllegalArgumentException("Chaque octet doit être compris entre 0 et 255");
		            }
		        }
		    }
		}
		int totalBits = blocs.length * 128;  // calcule le nombre total de bits : 128 bits par bloc
		int [] blocTotal = new int [totalBits];   // tableau qui contiendra tous les bits
		int indexBit = 0; // indique la position du prochain bit à ecrire
		// Parcourt tous les blocs
		for (int b=0; b<blocs.length;b++) {
			// Parcourt la matrice State 4x4 par colonnes
			for (int j = 0; j<4; j++) {  //colonnes
				for (int i = 0; i<4; i++) { // lignes
					int octet = blocs[b][i][j];  // recupere l'octet de la matrice
					// Extrait les 8 bits de l'octet pour les replacer dans le tableau global
					for (int k = 7; k>=0 ; k--) {
						int bit = (octet >> k) & 1;
						blocTotal[indexBit] = bit;
						indexBit++;
					}
				}
			}
		}
		return blocTotal;	
	}
	
	
	/**
	 * Realise le XOR entre tab1 et tab2	
	 * 
	 * @param tab1
	 * @param tab2
	 * @return Le resultat du xor des 2 tab passés en param
	 */
	public int [] xor(int[] tab1, int[] tab2 ) {
		// Verifie qu'aucun des deux tableaux n'est null
		if (tab1 == null || tab2 == null) {
		    throw new IllegalArgumentException("Les tableaux ne doivent pas être null");
		}
		// Verifie que les deux tableaux ont la meme longueur
		else if (tab1.length != tab2.length) {
		    throw new IllegalArgumentException("Les tableaux doivent avoir la même longueur");
		}
		int [] resultat = new int [tab1.length];
		// Parcourt toutes les positions des tableaux
		for (int i = 0; i<tab1.length; i++) {
			resultat[i] = tab1[i] ^ tab2[i];   // effectue le XOR entre les elements de meme position
		}
		return resultat;
	}
	
	
	
	/**
	 * Permet de parcourir les octets de l'etat AES et remplacer chacun par la valeur correspondante dans sBox
	 * 
	 * @param state La matrice d'état 4x4
	 * @return Une nouvelle matrice State transformée
	 */
	public int [][] subByte (int [][] state){
		// Verifie que la matrice n'est pas null
		if (state == null) {
			throw new IllegalArgumentException("La matrice State ne doit pas être null");
		}
		// Verifie que la matrice possede bien 4 lignes
		if (state.length != 4) {
			throw new IllegalArgumentException("La matrice State doit être de taille 4x4");
		}
		// Verifie chaque ligne pour s'assurer qu'elle n'est pas null et fait bien 4 colonnes
		for (int i = 0; i < 4; i++) {
		    if (state[i] == null || state[i].length != 4) {
		        throw new IllegalArgumentException("La matrice State doit être de taille 4x4");
		    }
		}		
		int [][] newState = new int[4][4];
		// Parcourt chaque ligne de la matrice
		for (int i=0; i<4; i++) {
			// Parcourt chaque colonne de la matrice
			for (int j=0; j<4; j++) {
				int octet = state[i][j]; // recupere l'octet situé dans state[i][j]
				// Verifie que l'octet est bien un octet valide (entre 0 et 255)
				if (octet < 0 || octet > 255) {
				    throw new IllegalArgumentException("Chaque octet doit être compris entre 0 et 255");
				}
	            // Calcule la ligne et la colonne dans sBox
				int ligne = octet / 16;  // divise l'octet par 16 pour obtenir la ligne de la S-Box
				int colonne = octet % 16 ;  // calcule le reste de la division par 16 pour obtenir la colonne de la S-Box
				newState[i][j] = this.sBox[ligne][colonne];  // range la valeur substituée dans newState, state n'est donc pas modifié
			}
		}
		return newState;
	}
	
	
	/**
	 * Effectue une rotation circulaire vers la gauche sur un mot de 4 octets
	 * 
	 * @param mot Le mot de 4 octets
	 * @return Un nouveau mot avec les octets décalés
	 */
	public int [] rotWord (int [] mot) {
		 // Verifie que mot n'est pas null et contient exactement 4 octets
		if (mot == null || mot.length != 4) {
			throw new IllegalArgumentException("Un mot doit contenir exactement 4 octets");
		}
		int [] resultat = new int [4];
		resultat[0] = mot[1];
		resultat[1] = mot[2];
		resultat[2] = mot[3];
		resultat[3] = mot[0];
		return resultat;
	}
	
	
	/**
	 * Applique la S-box sur les 4 octets d'un mot.
	 * 
	 * @param mot Le mot de 4 octets
	 * @return Un nouveau mot transformé par la S-box
	 */
	public int [] subWord(int []mot) {
		 // Verifie que mot n'est pas null et contient exactement 4 octets
		if (mot == null || mot.length != 4) {
	        throw new IllegalArgumentException("Un mot doit contenir exactement 4 octets.");
	    }
		int[] resultat = new int[4];
		 // ParcourT les quatre octets 
		for (int i=0; i<4; i++) {
			int octet = mot[i];
			// Verifie que l'octet est bien un octet valide (entre 0 et 255)
			if (octet < 0 || octet > 255) {
			    throw new IllegalArgumentException("Chaque octet doit être compris entre 0 et 255");
			}
			int ligne = octet / 16;  // divise l'octet par 16 pour obtenir la ligne de la S-Box
			int colonne = octet % 16 ;  // calcule le reste de la division par 16 pour obtenir la colonne de la S-Box
			// Calcule la ligne et la colonne dans sBox
			resultat[i] = sBox[ligne][colonne];  // range l’octet substitué dans le nouveau mot
		}
		return resultat;
	}
	
	
	/**
	 * Permet de generer les cles de rondes (Key Expansion)
	 * 
	 * @param masterKey La matrice de la clé maître (4 lignes, et Nk colonnes : 4, 6 ou 8).
	 * @return Un tableau de matrices 4x4 représentant les clés pour chaque ronde
	 * @throws IllegalArgumentException si la clé maître est nulle, si ses
	 *         dimensions sont incorrectes ou si un octet n'est pas compris
	 *         entre 0 et 255.
	 * @throws IllegalStateException si une constante Rcon nécessaire est absente.
	 */
	public int [][][] createKeys(int [][] masterKey) {
		// Verifie que masterKey n'est pas null et contient bien 4 lignes
		if (masterKey == null || masterKey.length != 4) {
		    throw new IllegalArgumentException("La clé maître doit contenir exactement 4 lignes");
		}
		// Verifie que chaque ligne existe et possède le nombre de colonnes attendu.
		for (int i = 0; i < 4; i++) {
		    if (masterKey[i] == null || masterKey[i].length != this.taille_cle_maitre / 32) {
		    	throw new IllegalArgumentException("La clé maître doit avoir 4 lignes et Nk colonnes");
		    }
		}
		// Nk représente le nombre de mots de 32 bits dans la clé maître. Chaque mot contient 4 octets soit 32 bits
		int Nk = this.taille_cle_maitre / 32;
		// matrice qui contiendra tous les mots de la clé étendue
	    int [][] mots = new int[4][4 * (this.nb_ronde + 1)];
	    // Copie les mots de la clé maître au début de la clé étendue
		for (int i = 0; i < 4; i++) {
		    for (int j = 0; j < Nk; j++) {
		        int octet = masterKey[i][j];
		        // Verifie que l'octet est bien un octet valide (entre 0 et 255)
				if (octet < 0 || octet > 255) {
				    throw new IllegalArgumentException("Chaque octet de la clé maitre doit être compris entre 0 et 255");
				}
				mots[i][j] = octet;
		    }
		}
		// Calcule les mots suivants de la clé étendue à partir des mots déjà calculés, jusqu'à obtenir toutes les clés nécessaires
	    for (int j = Nk; j < 4*(this.nb_ronde + 1); j++) {
	    	// Récupère le mot précédent qui est composé de 4 octets
	        int [] temp = new int [4];
			temp[0] = mots[0][j - 1];
			temp[1] = mots[1][j - 1];
			temp[2] = mots[2][j - 1];
			temp[3] = mots[3][j - 1];
			if (j % Nk == 0) {
				temp = this.subWord(this.rotWord(temp));
				// Vérifie que la constante Rcon nécessaire existe
				if (j / Nk >= this.Rcon.length) {
				    throw new IllegalStateException("Constante Rcon manquante");
				}
				// Applique la constante de ronde au premier octet du mot
				temp[0] ^= this.Rcon[j / Nk];
			// Cas particulier de l'AES-256 : une substitution supplémentaire est appliquée lorsque Nk vaut 8 et que l'indice du mot est congru à 4 modulo Nk
		    }else if (Nk == 8 && (j % Nk == 4)) {
				temp = this.subWord(temp);
			}
			// Calcule le nouveau mot en faisant un XOR entre le mot situé Nk positions avant et le mot temporaire transformé
			for (int i = 0; i < 4; i++) {
				mots[i][j] = mots[i][j - Nk] ^ temp[i];
			}
		}
	    //tableau qui contiendra les clés de toutes les rondes
		this.keys = new int[this.nb_ronde + 1][4][4];
		// Parcourt toutes les clés de ronde, de la clé initiale à la dernière
	    for (int r = 0; r <= this.nb_ronde; r++) {
		    for (int i = 0; i < 4; i++) {
		        for (int j = 0; j < 4; j++) {
		            this.keys[r][i][j] = mots[i][4 * r + j]; // Recopie les 4 colonnes correspondant à cette ronde dans une matrice de 4×4
	                
		        }
		    }
		}
		return this.keys;
	}
	
	
	
	/**
	 * Permet d'appliquer la clé de ronde sur la matrice d'état (XOR octet par octet)
     *
	 * @param state La matrice d'état 4x4
	 * @param cleRonde La matrice de clé de ronde 4x4
	 * @return Une nouvelle matrice State après application du XOR avec la clé de ronde
	 */
	public int [][] addRoundKey (int [][] state, int [][] cleRonde){
		// Verifie que les matrices ne sont pas nulles
        if (state == null || cleRonde == null) {
            throw new IllegalArgumentException("Les matrices State et cleRonde ne doivent pas être nulles");
        }
        // Verifie que les matrices possedent bien 4 lignes
        if (state.length != 4 || cleRonde.length != 4) {
            throw new IllegalArgumentException("Les matrices doivent être de taille 4x4");
        }
        // Vérifie chaque ligne pour s'assurer qu'elle n'est pas nulle et fait bien 4 colonnes
        for (int i = 0; i < 4; i++) {
            if (state[i] == null || state[i].length != 4 || cleRonde[i] == null || cleRonde[i].length != 4) {
                throw new IllegalArgumentException("Les matrices doivent être de taille 4x4");
            }
        }
		int [][] newState = new int [4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j<4; j++) {
				if (state[i][j] < 0 || state[i][j] > 255 || cleRonde[i][j] < 0 || cleRonde[i][j] > 255) {
				    throw new IllegalArgumentException("Chaque valeur doit être comprise entre 0 et 255");
				}
			}
			newState[i] = this.xor(state[i], cleRonde[i]) ;
		}
		return newState;	
	}
	
	
	/**
	 * Permet d'appliquer la substitution inverse sur la matrice d'état
     *
	 * @param state La matrice d'état 4x4
	 * @return Une nouvelle matrice State transformée par la S-box inverse
	 */
	public int [][] subByteInv (int [][] state){
		// Verifie que la matrice n'est pas null
		if (state == null) {
			throw new IllegalArgumentException("La matrice State ne doit pas être null");
		}
		// Verifie que la matrice possede bien 4 lignes
		if (state.length != 4) {
			throw new IllegalArgumentException("La matrice State doit être de taille 4x4");
		}
		// Verifie chaque ligne pour s'assurer qu'elle n'est pas null et fait bien 4 colonnes
		for (int i = 0; i < 4; i++) {
		    if (state[i] == null || state[i].length != 4) {
		        throw new IllegalArgumentException("La matrice State doit être de taille 4x4");
		    }
		}
		int [][] newState = new int[4][4];
		// Parcourt chaque case de la matrice
		for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				int octet = state[i][j]; // recupere l'octet situé dans state[i][j]
				// Verifie que l'octet est bien un octet valide (entre 0 et 255)
				if (octet < 0 || octet > 255) {
				    throw new IllegalArgumentException("Chaque octet doit être compris entre 0 et 255");
				}
		        // Calcule la ligne et la colonne dans sBoxInv
				int ligne = octet / 16;  // divise l'octet par 16 pour obtenir la ligne de la S-Box inverse
				int colonne = octet % 16 ;  // calcule le reste de la division par 16 pour obtenir la colonne de la S-Box inverse
				newState[i][j] = this.sBoxInv[ligne][colonne];  // range la valeur obtenue par la S-Box inverse
			}
		}
		return newState;
	}
	
	
	
	/**
	 * Effectue le décalage cyclique des lignes de la matrice d'état
	 * La première ligne n'est pas décalée. Les lignes suivantes sont décalées respectivement d'une, deux et trois positions vers la gauche
	 * 
	 * @param state La matrice d'état 4x4
	 * @return Une nouvelle matrice State après décalage des lignes
	 */
	public int [][] shiftRows (int [][] state){
		// Verifie que la matrice n'est pas null
		if (state == null) {
			throw new IllegalArgumentException("La matrice State ne doit pas être null");
		}
		// Verifie que la matrice possede bien 4 lignes
		if (state.length != 4) {
			throw new IllegalArgumentException("La matrice State doit être de taille 4x4");
		}
		// Verifie chaque ligne pour s'assurer qu'elle n'est pas null et fait bien 4 colonnes
		for (int i = 0; i < 4; i++) {
		    if (state[i] == null || state[i].length != 4) {
		        throw new IllegalArgumentException("La matrice State doit être de taille 4x4");
		    }
		}
		int [][] newState = new int[4][4];
		// Parcourt chaque ligne et chaque colonne de la matrice.
	    for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				// Vérifie que la valeur représente un octet valide (entre 0 et 255).
	            if (state[i][j] < 0 || state[i][j] > 255) {
				    throw new IllegalArgumentException("Chaque octet doit être compris entre 0 et 255");
				}
	            // Décale cycliquement la ligne i vers la gauche de i positions.
	            // Le modulo 4 permet de revenir au début de la ligne si nécessaire.
				newState[i][j] = state[i][(j + i) % 4];
			}
		}
		return newState;
	}
	
	
	
	/**
	 * Effectue le décalage cyclique inverse des lignes de la matrice d'état
     * La première ligne n'est pas décalée. Les lignes suivantes sont décalées respectivement d'une, deux et trois positions vers la droite
     *
	 * @param state La matrice d'état 4x4
	 * @return Une nouvelle matrice State après décalage inverse des lignes
	 */
	public int [][] shiftRowsInv (int [][] state){
		// Verifie que la matrice n'est pas null
		if (state == null) {
			throw new IllegalArgumentException("La matrice State ne doit pas être null");
		}
		// Verifie que la matrice possede bien 4 lignes
		if (state.length != 4) {
			throw new IllegalArgumentException("La matrice State doit être de taille 4x4");
		}
		// Verifie chaque ligne pour s'assurer qu'elle n'est pas null et fait bien 4 colonnes
		for (int i = 0; i < 4; i++) {
		    if (state[i] == null || state[i].length != 4) {
		        throw new IllegalArgumentException("La matrice State doit être de taille 4x4");
		    }
		}
		int [][] newState = new int[4][4];
		// Parcourt chaque ligne et chaque colonne de la matrice.
	    for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				// Vérifie que la valeur représente un octet valide (entre 0 et 255).
	            if (state[i][j] < 0 || state[i][j] > 255) {
				    throw new IllegalArgumentException("Chaque octet doit être compris entre 0 et 255");
				}
	            // Décale cycliquement la ligne i vers la droite de i positions
                // Le (+ 4) évite d'avoir un modulo négatif en Java
	            // Le modulo 4 permet de revenir au début de la ligne si nécessaire.
				newState[i][j] = state[i][(j - i + 4) % 4];
			}
		}
		return newState;
	}
	
	
	
	/**
	 * Multiplie un octet par 2 dans le corps fini GF(2^8)
     *
	 * @param a Un octet (entre 0 et 255)
	 * @return Le résultat de la multiplication par 2 dans GF(2^8)
	 */
	public int deux_a (int a) {
		// Vérifie que l'octet est valide
		if (a < 0 || a > 255) {
		    throw new IllegalArgumentException("L'octet doit être compris entre 0 et 255");
		}
		int resultat = 0;
		// Verifie si le bit de poids fort de a vaut 1
		// Dans un octet (8 bits), le bit de poids fort correspond à la valeur 0x80 en hexadécimal
		if ((a & 0x80) != 0) {
			resultat = (a << 1) ^ 0x1B ;
		}
		else {
			resultat = (a << 1);
		}
		return resultat & 0xFF;   // Garde uniquement les 8 bits de droite et supprime le bit en position 8
	}
	
	
	
	/**
	 * Multiplie un octet par 3 dans le corps fini GF(2^8)
	 * 
	 * @param a Un octet (entre 0 et 255)
	 * @return Le résultat de la multiplication par 3 dans GF(2^8)
	 */
	public int trois_a (int a) {
		// Vérifie que l'octet est valide
		if (a < 0 || a > 255) {
		    throw new IllegalArgumentException("L'octet doit être compris entre 0 et 255");
		}
		return this.deux_a(a) ^ a;	 // 3*a = 2*a XOR a
	}
	
	
	
	/**
	 * Multiplie un octet par 9 dans le corps fini GF(2^8) (9 = 8 + 1, et l’addition dans GF(2^8) est le XOR)
	 * @param a Un octet (entre 0 et 255)
	 * @return Le résultat de la multiplication par 9 dans GF(2^8)
	 */
	public int neuf_a (int a) {
		// Vérifie que l'octet est valide
		if (a < 0 || a > 255) {
		    throw new IllegalArgumentException("L'octet doit être compris entre 0 et 255");
		}
		// 9 = 8 + 1 or 8 = deux_a appliquée 3 fois (8 = 2^3), puis on ajoute a (XOR dans GF(2^8))
		int huitA = this.deux_a(this.deux_a(this.deux_a(a)));
		return huitA ^ a; 
	}
	
	
	
	/**
	 * Multiplie un octet par 11 dans le corps fini GF(2^8) (11 = 8 + 2 + 1)
	 * @param a Un octet (entre 0 et 255)
	 * @return Le résultat de la multiplication par 11 dans GF(2^8)
	 */
	public int onze_a (int a) {
		// Vérifie que l'octet est valide
		if (a < 0 || a > 255) {
		    throw new IllegalArgumentException("L'octet doit être compris entre 0 et 255");
		}
		// 11 = 8 + 2 + 1
		int deuxA = this.deux_a(a);
		return this.neuf_a(a) ^ deuxA;		 
	}
	
	
	
	/**
	 * Multiplie un octet par 13 dans le corps fini GF(2^8) (13 = 8 + 4 + 1  soit 9 + 4)
	 * @param a Un octet (entre 0 et 255)
	 * @return Le résultat de la multiplication par 13 dans GF(2^8)
	 */
	public int treize_a (int a) {
		// Vérifie que l'octet est valide
		if (a < 0 || a > 255) {
		    throw new IllegalArgumentException("L'octet doit être compris entre 0 et 255");
		}
		int quatreA = this.deux_a(this.deux_a(a));
		return this.neuf_a(a) ^ quatreA;		 
	}
	
	
	
	/**
	 * Multiplie un octet par 14 dans le corps fini GF(2^8) (14 = 8 + 4 + 2 )
	 * @param a Un octet (entre 0 et 255)
	 * @return Le résultat de la multiplication par 14 dans GF(2^8)
	 */
	public int quatorze_a (int a) {
		// Vérifie que l'octet est valide
		if (a < 0 || a > 255) {
		    throw new IllegalArgumentException("L'octet doit être compris entre 0 et 255");
		}
		int deuxA = this.deux_a(a);
		int quatreA = this.deux_a(deuxA);
		int huitA = this.deux_a(quatreA);
		return huitA ^ quatreA ^ deuxA;		 
	}
	
	
	
	/**
	 * Permet de multiplier une colonne d'octets par la matrice M pour l'étape MixColumns
	 * 
	 * @param c La colonne de 4 octets
	 * @return Une nouvelle colonne apres multiplication des octet
	 */
	public int [] prodAu(int[]c) {
		// Verifie que la colonne n'est pas null
		if (c == null) {
			throw new IllegalArgumentException("La colonne ne doit pas être null");
		}
		// Verifie que la colonne possede bien 4 octet
		if (c.length != 4) {
			throw new IllegalArgumentException("La colonne doit avoir 4 lignes");
		}
		for (int i=0; i<4; i++) {
			// Vérifie que la valeur représente un octet valide (entre 0 et 255).
            if (c[i] < 0 || c[i] > 255) {
			    throw new IllegalArgumentException("Chaque octet doit être compris entre 0 et 255");
			}
		}
		int [] resultat = new int [4];
		resultat[0] = this.deux_a(c[0]) ^ this.trois_a(c[1]) ^ c[2] ^ c[3];
		resultat[1] = c[0] ^ this.deux_a(c[1]) ^ this.trois_a(c[2]) ^ c[3];
		resultat[2] = c[0] ^ c[1] ^ this.deux_a(c[2]) ^ this.trois_a(c[3]);
		resultat[3] = this.trois_a(c[0]) ^ c[1] ^ c[2] ^ this.deux_a(c[3]);
		return resultat;
	}
	
	
	
	/**
	 * Permet de multiplie une colonne par la matrice inverse M⁻¹, utilisée au déchiffrement
	 * @param c La colonne de 4 octets
	 * @return Une nouvelle colonne après la multiplication
	 */
	public int [] prodAuInv (int[]c) {
		// Verifie que la colonne n'est pas null
		if (c == null) {
			throw new IllegalArgumentException("La colonne ne doit pas être null");
		}
		// Verifie que la colonne possede bien 4 octets
		if (c.length != 4) {
			throw new IllegalArgumentException("La colonne doit avoir 4 lignes");
		}
		for (int i=0; i<4; i++) {
			// Vérifie que la valeur représente un octet valide (entre 0 et 255).
	        if (c[i] < 0 || c[i] > 255) {
			    throw new IllegalArgumentException("Chaque octet doit être compris entre 0 et 255");
			}
		}
		int [] resultat = new int [4];
		resultat[0] = this.quatorze_a(c[0]) ^ this.onze_a(c[1]) ^ this.treize_a(c[2]) ^ this.neuf_a(c[3]);
		resultat[1] = this.neuf_a(c[0]) ^ this.quatorze_a(c[1]) ^ this.onze_a(c[2]) ^ this.treize_a(c[3]);
		resultat[2] = this.treize_a(c[0]) ^ this.neuf_a(c[1]) ^ this.quatorze_a(c[2]) ^ this.onze_a(c[3]);
		resultat[3] = this.onze_a(c[0]) ^ this.treize_a(c[1]) ^ this.neuf_a(c[2]) ^ this.quatorze_a(c[3]);
		return resultat;
	}
	
	
	
	/**
	 * Permet de transformer chaque colonne de la matrice d'état 4x4 indépendamment
     *
	 * @param state La matrice d'état (4x4)
	 * @return Une nouvelle matrice d'état après le mélange global
	 */
	public int[][] mixColumns (int[][] state){
		// Verifie que la matrice n'est pas null
		if (state == null) {
			throw new IllegalArgumentException("La matrice State ne doit pas être null");
		}
		// Verifie que la matrice possede bien 4 lignes
		if (state.length != 4) {
			throw new IllegalArgumentException("La matrice State doit être de taille 4x4");
		}
		// Verifie chaque ligne pour s'assurer qu'elle n'est pas null et fait bien 4 colonnes
		for (int i = 0; i < 4; i++) {
		    if (state[i] == null || state[i].length != 4) {
		        throw new IllegalArgumentException("La matrice State doit être de taille 4x4");
		    }
		}		
		int [][] newState = new int[4][4];
		// Parcourt chaque colonne de la matrice
		for (int j=0; j<4; j++) {
			// Extrait la colonne j de la matrice state
            int[] colonne = new int[4];
            for (int i = 0; i < 4; i++) {
                colonne[i] = state[i][j];
            }
            // Applique prodAu à la colonne j (produit par la matrice M)
            int [] resultat = this.prodAu(colonne);
            // Mettre la colonne mélangée dans la nouvelle matrice newState
            for (int i = 0; i < 4; i++) {
                newState[i][j] = resultat[i];
            }
		}
		return newState;
	}
	
	
	
	/**
	 * Permet de transformer chaque colonne de la matrice d'état 4x4 indépendamment
     *
	 * @param state La matrice d'état (4x4)
	 * @return Une nouvelle matrice d'état après le mélange global
	 */
	public int[][] mixColumnsInv (int[][] state){
		// Verifie que la matrice n'est pas null
		if (state == null) {
			throw new IllegalArgumentException("La matrice State ne doit pas être null");
		}
		// Verifie que la matrice possede bien 4 lignes
		if (state.length != 4) {
			throw new IllegalArgumentException("La matrice State doit être de taille 4x4");
		}
		// Verifie chaque ligne pour s'assurer qu'elle n'est pas null et fait bien 4 colonnes
		for (int i = 0; i < 4; i++) {
		    if (state[i] == null || state[i].length != 4) {
		        throw new IllegalArgumentException("La matrice State doit être de taille 4x4");
		    }
		}		
		int [][] newState = new int[4][4];
		// Parcourt chaque colonne de la matrice
		for (int j=0; j<4; j++) {
			// Extrait la colonne j de la matrice state
            int[] colonne = new int[4];
            for (int i = 0; i < 4; i++) {
                colonne[i] = state[i][j];
            }
            // Applique prodAuInv à la colonne j pour effectuer la multiplication par la matrice inverse M⁻¹
            int [] resultat = this.prodAuInv(colonne);
            // Mettre la colonne mélangée dans la nouvelle matrice newState
            for (int i = 0; i < 4; i++) {
                newState[i][j] = resultat[i];
            }
		}
		return newState;
	}
	
	
	
	/**
	 * Chiffre un message textuel complet en utilisant l'algorithme AES
	 * 
	 * @param message Le texte clair à chiffrer
	 * @return Un tableau d'entiers représentant le message chiffré
	 */
	public int[] crypte (String message) {
		if (message == null) {
            throw new IllegalArgumentException("Le message à chiffrer ne peut pas être null");
        }
		// Message -> bits, puis bourrage pour obtenir un multiple de 128 bits
		int[] bits = this.bourrage(this.stringToBits(message));
		// Découpage en blocs (matrices State 4x4)
		int[][][] blocs = this.decoupage_en_blocs(bits);
		// Chaque bloc est chiffré indépendamment
		for (int i=0; i<blocs.length; i++) {
			// Tour 0 : AddRoundKey avec K0
			int[][] S = this.addRoundKey(blocs[i], this.keys[0]);
			for (int r=1; r<this.nb_ronde;r++) {
				S = this.subByte(S);
				S = this.shiftRows(S);
				S = this.mixColumns(S);
				S = this.addRoundKey(S, this.keys[r]);
			}
			// Dernier tour : pas de MixColumns
			S = this.subByte(S);
			S = this.shiftRows(S);
			S = this.addRoundKey(S, this.keys[this.nb_ronde]);
			blocs[i] = S;
		}
		//Réassemblage de tous les blocs en un tableau de bits 
		return this.recollage_des_blocs(blocs);
	}
	
	
	
	
	public static void main(String[] args) {
		
	}

}
