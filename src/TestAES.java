import java.util.Arrays;

public class TestAES {

	public static void main(String[] args) {
		// Instanciation de l'AES avec une cle de 128 bits
		AES aes = new AES(128);
		
		/*
		 * Tests pour la première méthode stringToBits
		 
		String message1 = "A";
        int[] bits1 = aes.stringToBits(message1);
        System.out.print("Message : " + message1 + " -> Bits : " + Arrays.toString(bits1));
        
        System.out.println();
        
        String message2 = "AB";
        int[] bits2 = aes.stringToBits(message2);
        System.out.print("Message : " + message2 + " -> Bits : " + Arrays.toString(bits2));
        
        System.out.println();
        
        String message3 = "Bonjour";
        int[] bits3 = aes.stringToBits(message3);
        System.out.print("Message : " + message3 + " -> Bits : " + Arrays.toString(bits3));
		 */
		 		
		
		/*
		 * Tests pour la deuxieme method bitsToString 
		 
		String message = "Bonjour";

		int[] bits = aes.stringToBits(message);

		System.out.println("Message original : " + message);
		System.out.println("Bits : " + Arrays.toString(bits));

		String resultat = aes.bitsToString(bits);

		System.out.println("Message après conversion : " + resultat);
		
		System.out.println();
		*/
		
		
		/*
		 * Tests pour la troisieme method genereMasterKey 
		 
		int[][] cle = aes.genereMasterKey();
		for (int[] ligne : cle) {
		    for (int v : ligne) {
		    	System.out.print(v + " ");
		    }
		    System.out.println();
		}
		
		*/
		
		
		/*
		 * Tests pour la quatrieme method bourrage 
		 
		String messageB = "Bonjour";

		int[] bitsB = aes.stringToBits(messageB);
		int[] bitsBourrage = aes.bourrage(bitsB);

		System.out.println("Taille avant : " + bitsB.length);
		System.out.println("Taille après : " + bitsBourrage.length);
		*/
		
		
		/*
		 * Tests pour la cinquieme method debourrage (les 2 methodes combinés)
		 
		String message = "Bonjour";

		int[] bits = aes.stringToBits(message);
		int[] bitsBourres = aes.bourrage(bits);
		int[] bitsOriginaux = aes.debourrage(bitsBourres);

		System.out.println(bits.length);
		System.out.println(bitsBourres.length);
		System.out.println(bitsOriginaux.length);
		System.out.println(aes.bitsToString(bitsOriginaux));
		*/
		
		
		/*
		 * Tests pour la sixieme method decoupage_en_blocs
		 */
		// Test1 -> Un bloc avec "A"
		int[][][] blocs = aes.decoupage_en_blocs(aes.bourrage(aes.stringToBits("A")));
	    System.out.println("Nombre de blocs : " + blocs.length);   // attendu : 1
	    for (int[] ligne : blocs[0]) {
	        for (int v : ligne) System.out.print(v + " ");
	        System.out.println();
	    }
	    
        System.out.println();
	    
	    // Test2 -> Verifier le remplissage par colonne
	    int[][][] blocs2 = aes.decoupage_en_blocs(aes.bourrage(aes.stringToBits("ABCDE")));
	    for (int[] ligne : blocs2[0]) {
	        for (int v : ligne) System.out.print(v + " ");
	        System.out.println();
	    }
		
	}
	

}
