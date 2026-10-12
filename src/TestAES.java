import java.util.Arrays;

public class TestAES {

	public static void main(String[] args) {
		// Instanciation de l'AES avec une cle de 128 bits
		AES aes = new AES(128);
		
		/*
		 * Tests pour la première méthode stringToBits
		 */
        System.out.println("Test methode 1 : ");
        System.out.println();
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
		 
		 		
		
		/*
		 * Tests pour la deuxieme method bitsToString 
		 */
        System.out.println("Test methode 2 : ");
        System.out.println();
		String message4 = "Bonjour";

		int[] bits4 = aes.stringToBits(message2);

		System.out.println("Message original : " + message4);
		System.out.println("Bits : " + Arrays.toString(bits4));

		String resultat = aes.bitsToString(bits4);

		System.out.println("Message après conversion : " + resultat);
		
		System.out.println();
		
		
		
		/*
		 * Tests pour la troisieme method genereMasterKey 
		 */
		System.out.println("Test methode 3 : ");
        System.out.println();
		int[][] cle = aes.genereMasterKey();
		for (int[] ligne : cle) {
		    for (int v : ligne) {
		    	System.out.print(v + " ");
		    }
		    System.out.println();
		}
		
		System.out.println();
		
		
		
		/*
		 * Tests pour la quatrieme method bourrage 
		 */
		System.out.println("Test methode 4 : ");
        System.out.println();
		
        String messageB = "Bonjour";
		int[] bitsB = aes.stringToBits(messageB);
		int[] bitsBourrage = aes.bourrage(bitsB);
		System.out.println("Taille avant : " + bitsB.length);
		System.out.println("Taille après : " + bitsBourrage.length);
		
		
		System.out.println();
		
		
		
		/*
		 * Tests pour la cinquieme method debourrage (les 2 methodes combinés)
		 */
		System.out.println("Test methode 5 : ");
        System.out.println();
        
		String message5 = "Bonjour";
		int[] bits5 = aes.stringToBits(message5);
		int[] bitsBourres = aes.bourrage(bits5);
		int[] bitsOriginaux = aes.debourrage(bitsBourres);
		System.out.println(bits5.length);
		System.out.println(bitsBourres.length);
		System.out.println(bitsOriginaux.length);
		System.out.println(aes.bitsToString(bitsOriginaux));
		
		System.out.println();
		
		
		
		/*
		 * Tests pour la sixieme method decoupage_en_blocs
		 */
		System.out.println("Test methode 6 : ");
        System.out.println();
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
	    
        System.out.println();
	    
	    //Test 3 -> plusieurs blocs
	    int[][][] blocs3 = aes.decoupage_en_blocs(aes.bourrage(aes.stringToBits("Bonjour tout le monde")));
	    System.out.println("Nombre de blocs : " + blocs3.length);
		
	    System.out.println();
	    
	    
	    
	    /*
		 * Tests pour la septieme method recollage_des_blocs
		 */
	    System.out.println("Test methode 7 : ");
        System.out.println();
	    int[] bits7 = aes.stringToBits("Bonjour");
	    int[] bitsAvecBourrage = aes.bourrage(bits7);
	    int[][][] blocs7 = aes.decoupage_en_blocs(bitsAvecBourrage);
	    int[] bitsRecollés = aes.recollage_des_blocs(blocs7);
	    System.out.println(Arrays.equals(bitsAvecBourrage, bitsRecollés));
	    
	    //Test2 -> phrase complete + accent et caracteres particulier
	    String phrase = "Bonjour tout le monde é à ç";
	    int[] bits8 = aes.recollage_des_blocs(aes.decoupage_en_blocs(aes.bourrage(aes.stringToBits(phrase))));
	    System.out.println(aes.bitsToString(aes.debourrage(bits8)).equals(phrase));
	    
	    System.out.println();
	    
	    
	    
	    /*
		 * Tests pour la huitieme method xor
		 */
	    System.out.println("Test methode 8 : ");
        System.out.println();
	    //Test1 -> Tableau de meme longueur non null
	    int[] tab1 = {1, 0, 1, 1};
	    int[] tab2 = {0, 0, 1, 0};
	    int[] resultat8 = aes.xor(tab1, tab2);
	    System.out.println(Arrays.toString(resultat8));
	    System.out.println();
	    /*
	    //Test2 -> Tableau de longueur differentes et non null
	    int [] tab3 = {1, 0, 1, 0} ;
	    int [] tab4 = {1, 0};
	    int[] resultat9 = aes.xor(tab3, tab4);
	    System.out.println(Arrays.toString(resultat9));
	    
	    //Test3 -> Tableau de meme longueur differentes vide
	    int [] tab5 = {} ;
	    int [] tab6 = {};
	    int[] resultat10 = aes.xor(tab5, tab6);
	    System.out.println(Arrays.toString(resultat10));
	    */
	    System.out.println();
	    
	    
	    /*
	     * Test des 2 tables sBox et sBoxInv
	     */
	    System.out.println("Test tables sBox et sBoxInv : ");
	    boolean ok = true;
	    for (int x = 0; x < 256; x++) {
	        int s = AES.sBox[x >> 4][x & 15];
	        if (AES.sBoxInv[s >> 4][s & 15] != x) ok = false;
	    }
	    System.out.println(ok);   // true
	    System.out.println(AES.sBox[5][3]);   // 237
	    
	    
	}
	

}
