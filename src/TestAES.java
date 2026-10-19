import java.util.Arrays;

public class TestAES {

	public static void main(String[] args) {
		// Instanciation de l'AES avec une cle de 128 bits
		AES aes = new AES(128);
		
		/*
		 * Tests pour la première méthode stringToBits
		 */
        System.out.println("Test methode 1 : ");
		String message1 = "A";
        int[] bits1 = aes.stringToBits(message1);
        System.out.print("Message : " + message1 + " -> Bits : " + Arrays.toString(bits1));
        //Message : A -> Bits : [0, 1, 0, 0, 0, 0, 0, 1]
        System.out.println();
        
        String message2 = "AB";
        int[] bits2 = aes.stringToBits(message2);
        System.out.print("Message : " + message2 + " -> Bits : " + Arrays.toString(bits2));
        //Message : AB -> Bits : [0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0]
        System.out.println();
        
        String message3 = "Bonjour";
        int[] bits3 = aes.stringToBits(message3);
        System.out.print("Message : " + message3 + " -> Bits : " + Arrays.toString(bits3));
        //Message : Bonjour -> Bits : [0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0]
        System.out.println();
        System.out.println();
        
		
		/*
		 * Tests pour la deuxieme method bitsToString 
		 */
        System.out.println("Test methode 2 : ");
		String message4 = "Bonjour";
		int[] bits4 = aes.stringToBits(message4);

		System.out.println("Message original : " + message4);  //Message original : Bonjour
		System.out.println("Bits : " + Arrays.toString(bits4));  // Bits : [0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0]

		String resultat = aes.bitsToString(bits4);
		
		System.out.println("Message après conversion : " + resultat);  //Message apres conversion : Bonjour
		
		System.out.println();
		
		
		
		/*
		 * Tests pour la troisieme method genereMasterKey 
		 */
		System.out.println("Test methode 3 : ");
		int[][] cle = aes.genereMasterKey();
		for (int[] ligne : cle) {
		    for (int v : ligne) {
		    	System.out.print(v + " ");
		    }
		    System.out.println();
		}
		
		System.out.println();
		// 65 204 49 31 
		// 151 154 216 103 
		// 77 7 241 211 
		// 199 217 130 73 
		
		
		/*
		 * Tests pour la quatrieme method bourrage 
		 */
		System.out.println("Test methode 4 : ");
		String messageB = "Bonjour";
		int[] bitsB = aes.stringToBits(messageB);
		int[] bitsBourrage = aes.bourrage(bitsB);
		System.out.println("Taille avant : " + bitsB.length);             // Taille avant : 56
		System.out.println("Bits original : " + Arrays.toString(bits4));  // [0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0]
		System.out.println("Taille après : " + bitsBourrage.length);           // Taille après : 128
		System.out.println("Bits bourrés : " + Arrays.toString(bitsBourrage)); // [0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
		
		
		System.out.println();
		
		
		
		/*
		 * Tests pour la cinquieme method debourrage (les 2 methodes combinés)
		 */
		System.out.println("Test methode 5 : ");
		String message5 = "Bonjour";
		int[] bits5 = aes.stringToBits(message5);
		int[] bitsBourres = aes.bourrage(bits5);
		int[] bitsOriginaux = aes.debourrage(bitsBourres);
		System.out.println(bits5.length);                    // 56
		System.out.println(bitsBourres.length);              // 128
		System.out.println(bitsOriginaux.length);            // 56
		System.out.println(aes.bitsToString(bitsOriginaux)); // Bonjour
		
		System.out.println();
		
		
		
		/*
		 * Tests pour la sixieme method decoupage_en_blocs
		 */
		System.out.println("Test methode 6 : ");
		// Test1 -> Un bloc avec "A"
		int[][][] blocs = aes.decoupage_en_blocs(aes.bourrage(aes.stringToBits("A")));
	    System.out.println("Nombre de blocs : " + blocs.length);   // Nombre de blocs : 1
	    for (int[] ligne : blocs[0]) {
	        for (int v : ligne) {
	        	System.out.print(v + " ");
	        }
	        System.out.println();
	    }
	    // 65 15 15 15 
	    // 15 15 15 15 
	    // 15 15 15 15 
	    // 15 15 15 15 

	    
        System.out.println();
	    
	    // Test2 -> Verifier le remplissage par colonne
	    int[][][] blocs2 = aes.decoupage_en_blocs(aes.bourrage(aes.stringToBits("ABCDE")));
	    for (int[] ligne : blocs2[0]) {
	        for (int v : ligne) {
	        	System.out.print(v + " ");
	        }
	        System.out.println();
	    }
	    // 65 69 11 11 
	    // 66 11 11 11 
	    // 67 11 11 11 
	    // 68 11 11 11
	    
        System.out.println();
	    
	    //Test 3 -> plusieurs blocs
	    int[][][] blocs3 = aes.decoupage_en_blocs(aes.bourrage(aes.stringToBits("Bonjour tout le monde")));
	    System.out.println("Nombre de blocs : " + blocs3.length);  // Nombre de blocs : 2
	    for (int[] ligne : blocs3[0]) {
	        for (int v : ligne) {
	        	System.out.print(v + " ");
	        }
	        System.out.println();
	    } 
	    // 66 111 116 32 
	    // 111 117 111 108 
	    // 110 114 117 101 
	    // 106 32 116 32 

	    System.out.println();
	    
	    
	    
	    /*
		 * Tests pour la septieme method recollage_des_blocs
		 */
	    System.out.println("Test methode 7 : ");
	    int[] bits7 = aes.stringToBits("Bonjour");
	    int[] bitsAvecBourrage = aes.bourrage(bits7);
	    int[][][] blocs7 = aes.decoupage_en_blocs(bitsAvecBourrage);
	    int[] bitsRecollés = aes.recollage_des_blocs(blocs7);
	    System.out.println(Arrays.equals(bitsAvecBourrage, bitsRecollés));   //true
	    
	    //Test2 -> phrase complete + accent et caracteres particulier
	    String phrase = "Bonjour tout le monde é à ç";
	    int[] bits8 = aes.recollage_des_blocs(aes.decoupage_en_blocs(aes.bourrage(aes.stringToBits(phrase))));
	    System.out.println(aes.bitsToString(aes.debourrage(bits8)).equals(phrase));  // true
	    
	    System.out.println();
	    
	    
	    
	    /*
		 * Tests pour la huitieme method xor
		 */
	    System.out.println("Test methode 8 : ");
	    //Test1 -> Tableau de meme longueur non null
	    int[] tab1 = {1, 0, 1, 1};
	    int[] tab2 = {0, 0, 1, 0};
	    int[] resultat8 = aes.xor(tab1, tab2);
	    System.out.println(Arrays.toString(resultat8));   //[1, 0, 0, 1]
	    System.out.println();
	    /*
	    //Test2 -> Tableau de longueur differentes et non null
	    int [] tab3 = {1, 0, 1, 0} ;
	    int [] tab4 = {1, 0};
	    int[] resultat9 = aes.xor(tab3, tab4);
	    System.out.println(Arrays.toString(resultat9)); 
	    //Exception in thread "main" java.lang.IllegalArgumentException: Les tableaux doivent avoir la même longueur
		//at AES.xor(AES.java:253)
		//at TestAES.main(TestAES.java:189)
	    */
	    //Test3 -> Tableau de meme longueur differentes vide
	    int [] tab5 = {} ;
	    int [] tab6 = {};
	    int[] resultat10 = aes.xor(tab5, tab6);
	    System.out.println(Arrays.toString(resultat10)); // []
	    
	    System.out.println();
	    
	    
	    /*
	     * Test des 2 tables sBox et sBoxInv
	     */
	    System.out.println("Test tables sBox et sBoxInv : ");
	    boolean ok = true;
	    for (int x = 0; x < 256; x++) {
	        int s = AES.sBox[x >> 4][x & 15];
	        if (AES.sBoxInv[s >> 4][s & 15] != x) {
	        	ok = false;
	        }
	    }
	    System.out.println(ok);               // true
	    System.out.println(AES.sBox[5][3]);   // 237
	    
	    
	    System.out.println();
	    
	    
	    
	    /*
	     * Test pour la neuvieme method subByte
	     */
	    System.out.println("Test methode 9 : ");
	    int[][] s = {{0,1,2,3},{4,5,6,7},{8,9,10,11},{12,13,14,15}};
	    int[][] t = aes.subByte(s);
	    System.out.println(t[0][0]);   // 99
	    System.out.println(t[0][1]);   //124
	    System.out.println(s[0][0]);   //0
        System.out.println();
        
        // Test 1 -> vérifier une substitution connue
        int[][] state = {
            {83, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        };

        int[][] resultat9 = aes.subByte(state);
        System.out.println("Test 1 : " + (resultat9[0][0] == 237));  //Test 1 : true
        
        // Test 2 :-> vérifier que la matrice d'origine ne change pas
        System.out.println("Test 2 : " + (state[0][0] == 83));  //Test 2 : true
        
        // Test 3 -> matrice null
        try {
            aes.subByte(null);
            System.out.println("Test 3 : ECHEC");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 3 : réussi");  //Test 3 : réussi
        }

        // Test 4 -> matrice avec seulement 3 lignes
        int[][] troisLignes = {
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        };
        try {
            aes.subByte(troisLignes);
            System.out.println("Test 4 : ECHEC");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 4 : réussi");  //Test 4 : réussi
        }
        
        // Test 5 -> une ligne avec seulement 3 colonnes
        int[][] mauvaiseLigne = {
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0}
        };
        try {
            aes.subByte(mauvaiseLigne);
            System.out.println("Test 5 : ECHEC");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 5 : réussi");  //Test 5 : réussi
        }
        
        // Test 6 -> valeur supérieure à 255
        int[][] valeurTropGrande = {
            {256, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        };
        try {
            aes.subByte(valeurTropGrande);
            System.out.println("Test 6 : ECHEC");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 6 : réussi");  ////Test 6 : réussi
        }
	}
	

}
