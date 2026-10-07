import java.nio.charset.StandardCharsets;

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
	 * A revoir
	 * Permmet de transformer une chaine de caractere en un tableau d'entier (0 et 1)
	 */
	private int [] stringToBits(String message) {
		byte [] bytes = message.getBytes(StandardCharsets.UTF_8);
		StringBuilder binaire = new StringBuilder();
		
		
		return null;
	}
	
	
	/*
	 * Permet de transforer un tableau d'entier (0 et1) en chaine de caractere
	 */
	private String bitsToString(int[] blocs) {
		
		return null;
	}
	
	
	/*
	 * Permet de generer une clé maitre aléatoire de taille choisie (128 ou 192 ou 256)
	 */
	private int [] genereMasterKey() {
		return null;
		
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
