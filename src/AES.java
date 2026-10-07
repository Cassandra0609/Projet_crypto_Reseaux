import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

public class AES {
	
	/*
	 * constants
	 */
	static int taille_bloc = 128;
	static int [] Rcon;
	static int [][] sBox;
	static int [][] sBoxInv;
	
	/*
	 * attributes
	 */
	private int taille_cle_maitre;
	private int nb_ronde;
	private int [][] master_Key;
	private int [][] keys;
	
	/*
	 * Constructor
	 */
	public AES(int tcl) {
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
		
	}
	
	
	/*
	 * Permmet de transformer une chaine de caractere en un tableau d'entier (0 et 1)
	 */
	private int [] stringToBits(String message) {
		byte [] bytes = message.getBytes(StandardCharsets.UTF_8);  // recupere les octets du mess et les mets sous forme de tableau
		int[] bits = new int[bytes.length * 8];  //chaque octet contient 8 bits donc il nous faut la taille du mot *8 cases dans notre tableau
		// Parcourt tous les octets du message
		for (int i = 0; i < bytes.length ; i++ ) {
			//Parcourt les bits de l'octet de la position 7 à 0 vu qu'un octet contient 8 bits
			for (int j  = 7; j >= 0; j--) {
				// i * 8 -> indique à partir de quelle position dans "bits" commence l'octet actuel
				//(7 - j) -> convertit j (qui descend de 7 à 0) en un indice qui monte de 0 à 7 ainsi le bit de poids fort est rangé en premier
				bits[i * 8 + (7-j)] = (bytes[i] >> j) & 1;   // >> decale les bits vers la droite de j positions et &1 permet de garder que le dernier bit
			}
		}
		return bits;
	}
	
	
	/*
	 * Permet de transforer un tableau d'entier (0 et1) en chaine de caractere
	 */
	private String bitsToString(int[] blocs) {
		int nombrebytes = blocs.length / 8;   // 8 bits correspondent à 1 octet
		byte [] bytes = new byte [nombrebytes]; //tab qui va contenir les octets reconstruits
		// Parcourt chaque octet que l'on doit reconstruire
		for (int i = 0; i < bytes.length ; i++ ) {
	        byte b = 0;   // Valeur de l'octet que l'on est en train de construire
	        // Parcourt les 8 bits correspondant à cet octet
	        for (int j = 0; j < 8; j++) {
	        	b = (byte) ((b << 1) | blocs[i*8 + j]);  // Reconstruit l'octet bit par bit en décalant de 1 vers la gauche et en ajoutant le nouveau 0 ou 1
	        }
	        bytes[i] = b; // place l'octet reconstruit dans le tableau
		}
		return new String(bytes, StandardCharsets.UTF_8);
	}
	
	
	/*
	 * Permet de generer une cle maitre aleatoire de taille choisie (128 ou 192 ou 256)
	 */
	private int [][] genereMasterKey() {
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
	

	/*
	 * Permet d'ajouter un bourrage pour que la taille du message soit un multiple de 128 bits
	 */
	private int [] bourrage(int[] bits) {
		int reste = bits.length % 128;
		int bitsAAjouter = 128 - reste;  // calcule combien de bits il faut ajouter pour atteindre le prochain multiple de 128
		int octetsAjouter = bitsAAjouter / 8; // 1 octet = 8 bits donc on transforme le nombre de bits a ajouter en nombre d'octets
		int [] resultat = new int [bits.length + bitsAAjouter]; // tab qui contiendra le message + le bourrage
		// Parcourt tous les bits du message original
		for (int i = 0; i < bits.length; i++) {
		    resultat[i] = bits[i]; // copie les bits du message original
		}
		int [] valeurBinaire = new int [8];
		// Parcourt les 8 positions possibles d'un octet
		for (int j  = 7; j >= 0; j--) {
		        valeurBinaire[7-j] = (octetsAjouter >> j) & 1 ;
		}
		// Ajoute la valeur de bourrage autant de fois qu'il y a d'octets a ajouter
		for (int i = 0; i < octetsAjouter; i++) {
			// Permet de copier les 8 bits de valeurBinaire dans resultat
	        for (int j = 0; j < 8; j++) {
	            resultat[bits.length + i * 8 + j] = valeurBinaire[j];  // calcule la position où placer le bit
	        }
	    }
	    return resultat;
		
	}
	
	
	
	
	
	
	
	/*
	 * Permet de generer les cles de rondes
	 */
	private int createKeys() {
		return 0;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		// Instanciation de l'AES avec une cle de 128 bits
		AES aes = new AES(128);				
		
		
		/*
		 * Tests pour la premiere method stringToBits
		 */
		String message1 = "A";
		int[] bits1 = aes.stringToBits(message1);
		System.out.print("Message : " + message1 + " -> Bits : " + Arrays.toString(bits1));
		
		System.out.println();
		       
		
		

		/*
		 * Tests pour la deuxieme method bitsToString 
		 */
		String message = "Bonjour";

		int[] bits = aes.stringToBits(message);

		System.out.println("Message original : " + message);
		System.out.println("Bits : " + Arrays.toString(bits));

		String resultat = aes.bitsToString(bits);

		System.out.println("Message après conversion : " + resultat);
		
		System.out.println();
		
		
		
		/*
		 * Tests pour la troisieme method genereMasterKey 
		 */
		int[][] cle = aes.genereMasterKey();
		for (int[] ligne : cle) {
		    for (int v : ligne) {
		    	System.out.print(v + " ");
		    }
		    System.out.println();
		}
	}

}
