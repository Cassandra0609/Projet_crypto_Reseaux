import java.nio.charset.StandardCharsets;
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
	 * Permet de generer une clé maitre aléatoire de taille choisie (128 ou 192 ou 256)
	 */
	private int [] genereMasterKey() {
		return null;
		
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
	}

}
