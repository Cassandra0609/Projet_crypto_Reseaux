import java.util.Arrays;

public class TestAES {

	public static void main(String[] args) {
		AES aes128 = new AES(128);
		AES aes192 = new AES(192);
	    AES aes256 = new AES(256);
	    System.out.println("Les trois constructeurs fonctionnent.");  //Les trois constructeurs fonctionnent.

	    try {
	        AES invalide = new AES(64);
	        System.out.println("Erreur : une taille invalide a été acceptée.");
	    } catch (IllegalArgumentException e) {
	        System.out.println("La taille invalide est bien rejetée.");   //La taille invalide est bien rejetée.
	    }
		
	    
	    
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
        System.out.println("Test 1 : " + (resultat9[0][0] == 237));  // Test 1 : true
        
        // Test 2 :-> vérifier que la matrice d'origine ne change pas
        System.out.println("Test 2 : " + (state[0][0] == 83));  // Test 2 : true
        
        // Test 3 -> matrice null
        try {
            aes.subByte(null);
            System.out.println("Test 3 : ECHEC");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 3 : réussi");  // Test 3 : réussi
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
            System.out.println("Test 4 : réussi");  // Test 4 : réussi
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
            System.out.println("Test 5 : réussi");  // Test 5 : réussi
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
            System.out.println("Test 6 : réussi");  // Test 6 : réussi
        }
        System.out.println();
        
        
        
        /*
	     * Test method rotWord
	     */
        System.out.println("Tests de subWord :");

        // Test 1 -> vérifier la rotation circulaire
        int[] mot = {0x09, 0xCF, 0x4F, 0x3C};
        int[] resultat11 = aes.rotWord(mot);
        System.out.println("Test 1 : " + Arrays.toString(resultat11));  // Test 1 : [207, 79, 60, 9]

        // Test 2 -> vérifier que le mot original n'est pas modifié
        System.out.println("Test 2 : " + java.util.Arrays.toString(mot));  // Test 2 : [9, 207, 79, 60]

        // Test 3 -> vérifier le rejet de null
        try {
            aes.rotWord(null);
            System.out.println("Test 3 : ECHEC");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 3 : réussi");   // Test 3 : réussi
        }

        // Test 4 -> vérifier le rejet d'un mot trop court
        try {
            aes.rotWord(new int[]{0x09, 0xCF, 0x4F});
            System.out.println("Test 4 : ECHEC");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 4 : réussi");   // Test 4 : réussi
        }

        // Test 5 -> vérifier le rejet d'un mot trop long
        try {
            aes.rotWord(new int[]{0x09, 0xCF, 0x4F, 0x3C, 0x10});
            System.out.println("Test 5 : ECHEC");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 5 : réussi");   // Test 5 : réussi
        }
        System.out.println();
        
        
        
        /*
	     * Test method subWord
	     */
        System.out.println("Tests de subWord : ");

        // Test 1 -> vérifier le remplacement par la S-Box
        int[] mot12 = {0x09, 0xCF, 0x4F, 0x3C};
        int[] resultat12 = aes.subWord(mot12);
        System.out.println("Test 1 : " + java.util.Arrays.toString(resultat12));  // Test 1 : [1, 138, 132, 235]

        // Test 2 -> vérifier que le mot original n'est pas modifié
        System.out.println("Test 2 : " + java.util.Arrays.toString(mot));  // Test 2 : [9, 207, 79, 60]

        // Test 3 -> vérifier le rejet de null
        try {
        	aes.subWord(null);
        	System.out.println("Test 3 : ECHEC");
        } catch (IllegalArgumentException e) {
        	System.out.println("Test 3 : réussi");  // Test 3 : réussi
        }

        // Test 4 -> vérifier un mot de mauvaise taille
        try {
        	aes.subWord(new int[]{0x09, 0xCF, 0x4F});
        	System.out.println("Test 4 : ECHEC");
        } catch (IllegalArgumentException e) {
        	System.out.println("Test 4 : réussi");   // Test 3 : réussi
        }

        // Test 5 -> vérifier un octet supérieur à 255
        try {
        	aes.subWord(new int[]{0x09, 0xCF, 0x4F, 256});
        	System.out.println("Test 5 : ECHEC");
        } catch (IllegalArgumentException e) {
        	System.out.println("Test 5 : réussi");   // Test 3 : réussi
        }

        // Test 6 -> vérifier un octet négatif
        try {
        	aes.subWord(new int[]{0x09, 0xCF, 0x4F, -1});
        	System.out.println("Test 6 : ECHEC");
        } catch (IllegalArgumentException e) {
        	System.out.println("Test 6 : réussi");   // Test 3 : réussi
        } 
        System.out.println();
        
        
        
        /*
         * Test de la dixieme methode createKey
         */
        System.out.println("Tests méthode 10 : ");
        int[][] cle128 = new int[4][4];
        int[][] cle192 = new int[4][6];
        int[][] cle256 = new int[4][8];

        int[][][] rondes128 = aes128.createKeys(cle128);
        int[][][] rondes192 = aes192.createKeys(cle192);
        int[][][] rondes256 = aes256.createKeys(cle256);

        System.out.println(
            "AES-128 : " + (rondes128.length == 11 ? "Réussi" : "ÉCHEC")  // AES-128 : Réussi
        );
        System.out.println(
            "AES-192 : " + (rondes192.length == 13 ? "Réussi" : "ÉCHEC")  // AES-192 : Réussi
        );
        System.out.println(
            "AES-256 : " + (rondes256.length == 15 ? "Réussi" : "ÉCHEC")  // AES-256 : Réussi
        );

        // Test 2 -> vérifier les dimensions d'une clé de ronde
        boolean dimensionsCorrectes =
            rondes128[0].length == 4
            && rondes128[0][0].length == 4;
        System.out.println("Dimensions 4x4 : " + (dimensionsCorrectes ? "Réussi" : "ÉCHEC"));  // Dimensions 4x4 : Réussi

        // Test 3 -> vérifier les valeurs avec une clé AES-128 de référence
        int[][] cleReference = {
            {0x2b, 0x28, 0xab, 0x09},
            {0x7e, 0xae, 0xf7, 0xcf},
            {0x15, 0xd2, 0x15, 0x4f},
            {0x16, 0xa6, 0x88, 0x3c}
        };
        int[][][] rondesReference = aes128.createKeys(cleReference);
        // Valeurs attendues pour la deuxième clé de ronde (ronde 1).
        int[][] ronde1Attendue = {
            {0xa0, 0x88, 0x23, 0x2a},
            {0xfa, 0x54, 0xa3, 0x6c},
            {0xfe, 0x2c, 0x39, 0x76},
            {0x17, 0xb1, 0x39, 0x05}
        };
        boolean ronde1Correcte = Arrays.deepEquals(rondesReference[1], ronde1Attendue);
        System.out.println("Clé de ronde AES-128 de référence : " + (ronde1Correcte ? "Réussi" : "ÉCHEC"));  // Clé de ronde AES-128 de référence : Réussi

        // Test 4 -> vérifier qu'une clé maître avec de mauvaises dimensions est rejetée.
        try {
            aes128.createKeys(new int[4][3]);
            System.out.println("Dimensions invalides : ÉCHEC");
        } catch (IllegalArgumentException e) {
            System.out.println("Dimensions invalides : Réussi");  // Dimensions invalides : Réussi
        } 

        // Test 5 -> vérifier qu'un octet invalide est rejeté
        try {
            int[][] cleInvalide = new int[4][4];
            cleInvalide[0][0] = 256;
            aes128.createKeys(cleInvalide);
            System.out.println("Octet invalide : ÉCHEC");
        } catch (IllegalArgumentException e) {
            System.out.println("Octet invalide : Réussi");  //Octet invalide : Réussi
        }
        System.out.println();
        
        
        
        /*
         * Test de la onzieme methode addRoundKey
         */
        System.out.println("Tests méthode 11 : ");
        int[][] state11 = {
        	    {0x00, 0x11, 0x22, 0x33},
        	    {0x44, 0x55, 0x66, 0x77},
        	    {0x88, 0x99, 0xAA, 0xBB},
        	    {0xCC, 0xDD, 0xEE, 0xFF}
        	};

        int[][] cleRonde11 = {
        	    {0xFF, 0xFF, 0xFF, 0xFF},
        	    {0xFF, 0xFF, 0xFF, 0xFF},
        	    {0xFF, 0xFF, 0xFF, 0xFF},
        	    {0xFF, 0xFF, 0xFF, 0xFF}
        	};
        int[][] resultat13 = aes.addRoundKey(state11, cleRonde11);
      	
        // Vérifie que le XOR donne le résultat attendu.
       	boolean resultatCorrect = true;
       	for (int i = 0; i < 4; i++) {
       	    for (int j = 0; j < 4; j++) {
       	        if (resultat13[i][j] != (state11[i][j] ^ 0xFF)) {
       	            resultatCorrect = false;
       	        }
       	    }
       	}
       	System.out.println("Calcul du XOR : " + (resultatCorrect ? "Réussi" : "ÉCHEC"));   // Calcul du XOR : Réussi

        // Vérifie que la matrice d'origine n'a pas été modifiée.
        System.out.println("État initial conservé : " + (state11[0][0] == 0x00 && state11[3][3] == 0xFF ? "Réussi" : "ÉCHEC"));    // Etat initial conservé : Réussi

        // Vérifie le rejet d'une matrice de dimensions incorrectes.
        try {
            aes.addRoundKey(new int[3][4], cleRonde11);
            System.out.println("Matrice invalide : ÉCHEC");
        } catch (IllegalArgumentException e) {
            System.out.println("Matrice invalide : Réussi");  // Matrice invalide : Réussi
        }
        System.out.println();
        
        
        
        /*
         * Test de la douzieme methode subByteInv
         */
        System.out.println("Tests méthode 12 : ");
        // Test 1 -> vérifier que les deux substitutions s'annulent
        int[][] state12 = {
            {0x00, 0x11, 0x22, 0x33},
            {0x44, 0x55, 0x66, 0x77},
            {0x88, 0x99, 0xAA, 0xBB},
            {0xCC, 0xDD, 0xEE, 0xFF}
        };
        int[][] apresSubByte = aes.subByte(state12);
        int[][] apresSubByteInv = aes.subByteInv(apresSubByte);
        System.out.println("Substitution puis substitution inverse : " + (java.util.Arrays.deepEquals(state12, apresSubByteInv) ? "Réussi" : "ÉCHEC"));    // Substitution puis substitution inverse : Réussi

        // Test 2 -> vérifier que l'état initial n'est pas modifié
        System.out.println("État initial conservé : " + (state12[0][0] == 0x00 && state12[3][3] == 0xFF ? "Réussi" : "ÉCHEC"));   // État initial conservé : Réussi

        // Test 3 -> vérifier le rejet d'un octet invalide
        try {
            int[][] stateInvalide = new int[4][4];
            stateInvalide[0][0] = 256;
            aes.subByteInv(stateInvalide);
            System.out.println("Octet invalide : ÉCHEC");
        } catch (IllegalArgumentException e) {
            System.out.println("Octet invalide : Réussi");   // Octet invalide : Réussi
        }
        
        
        
        /*
         * Test de la treizieme methode shiftRows
         */
        System.out.println("Tests méthode 13 : ");
        // Test 1 -> vérifier le décalage cyclique des lignes
        int[][] state13 = {
        		{0x00, 0x01, 0x02, 0x03},
        		{0x10, 0x11, 0x12, 0x13},
        		{0x20, 0x21, 0x22, 0x23},
        		{0x30, 0x31, 0x32, 0x33}
        };
        // Résultat attendu après le décalage vers la gauche.
        int[][] attendu = {
        		{0x00, 0x01, 0x02, 0x03},
        		{0x11, 0x12, 0x13, 0x10},
        		{0x22, 0x23, 0x20, 0x21},
        		{0x33, 0x30, 0x31, 0x32}
        };
        int[][] resultat14 = aes.shiftRows(state13);
        System.out.println("Décalage cyclique correct : " + (Arrays.deepEquals(resultat14, attendu) ? "Réussi" : "ÉCHEC"));   // Décalage cyclique correct : Réussi
        
        // Test 2 -> vérifier que la matrice originale est conservée
        int[][] original = {
        		{0x00, 0x01, 0x02, 0x03},
        		{0x10, 0x11, 0x12, 0x13},
        		{0x20, 0x21, 0x22, 0x23},
        		{0x30, 0x31, 0x32, 0x33}
        };
        int[][] copieOriginale = new int[4][4];
        for (int i = 0; i < 4; i++) {
        	copieOriginale[i] = original[i].clone();
        }
        aes.shiftRows(original);
        System.out.println("Matrice originale inchangée : " + (Arrays.deepEquals(original, copieOriginale) ? "Réussi" : "ÉCHEC"));   // Matrice originale inchangée : Réussi 
        
        
        // Test 3 -> vérifier le rejet d'une matrice null
        try {
        	aes.shiftRows(null);
        	System.out.println("Matrice null : ÉCHEC");
        } catch (IllegalArgumentException e) {
        	System.out.println("Matrice null : Réussi");   // Matrice null : Réussi
        }

        // Test 4 -> vérifier le rejet d'une matrice de mauvaises dimensions
        try {
        	aes.shiftRows(new int[3][4]);
        	System.out.println("Dimensions invalides : ÉCHEC");
        } catch (IllegalArgumentException e) {
        	System.out.println("Dimensions invalides : Réussi");   // Dimensions invalides : Réussi
        }

        // Test 5 -> vérifier le rejet d'un octet invalide
        try {
        	int[][] stateInvalide = new int[4][4];
        	stateInvalide[0][0] = 256;

        	aes.shiftRows(stateInvalide);
        	System.out.println("Octet invalide : ÉCHEC");
        } catch (IllegalArgumentException e) {
        	System.out.println("Octet invalide : Réussi");    // Octet invalide : Réussi
        }
        System.out.println();
        
        
        /*
         * Test de la quatorzieme methode shiftRowsInv
         */
        System.out.println("Tests méthode 14 : ");
        // Test 1 -> vérifier le décalage cyclique inverse
        int[][] state14 = {
            {0x00, 0x01, 0x02, 0x03},
            {0x10, 0x11, 0x12, 0x13},
            {0x20, 0x21, 0x22, 0x23},
            {0x30, 0x31, 0x32, 0x33}
        };
        int[][] attendu14 = {
            {0x00, 0x01, 0x02, 0x03},
            {0x13, 0x10, 0x11, 0x12},
            {0x22, 0x23, 0x20, 0x21},
            {0x31, 0x32, 0x33, 0x30}
        };
        int[][] resultat15 = aes.shiftRowsInv(state14);
        System.out.println("Décalage inverse correct : " + (Arrays.deepEquals(resultat15, attendu14) ? "Réussi" : "ÉCHEC"));   // Décalage inverse correct : Réussi

        // Test 2 -> vérifier que shiftRowsInv annule shiftRows
        int[][] apresShift = aes.shiftRows(state14);
        int[][] retour = aes.shiftRowsInv(apresShift);
        System.out.println("ShiftRows puis ShiftRowsInv : " + (Arrays.deepEquals(state14, retour) ? "Réussi" : "ÉCHEC"));   // ShiftRows puis ShiftRowsInv : Réussi

        // Test 3 -> vérifier que la matrice originale n'est pas modifiée
        System.out.println("Matrice originale conservée : " + (state14[1][0] == 0x10 && state14[3][3] == 0x33 ? "Réussi" : "ÉCHEC"));   // Matrice originale conservée : Réussi

        // Test 4 -> vérifier le rejet d'un octet invalide
        try {
            int[][] stateInvalide = new int[4][4];
            stateInvalide[0][0] = 256;
            aes.shiftRowsInv(stateInvalide);
            System.out.println("Octet invalide : ÉCHEC");
        } catch (IllegalArgumentException e) {
            System.out.println("Octet invalide : Réussi");   // Octet invalide : Réussi
        }
        System.out.println();
        
        
        
        /*
         * Test de la quinzieme methode deux_a
         */
        System.out.println("Tests méthode 15 : ");
		// Test 1 -> décalage simple, sans réduction
		System.out.println("Test 1 (0x01) : " + (aes.deux_a(0x01) == 0x02 ? "Réussi" : "ÉCHEC"));   // Test 1 (0x01) : Réussi
		
		// Test 2 -> autre décalage simple
		System.out.println("Test 2 (0x57) : " + (aes.deux_a(0x57) == 0xAE ? "Réussi" : "ÉCHEC"));   // Test 2 (0x57) : Réussi
		
		// Test 3 -> bit de poids fort à 1, réduction nécessaire
		System.out.println("Test 3 (0x80) : " + (aes.deux_a(0x80) == 0x1B ? "Réussi" : "ÉCHEC"));   // Test 3 (0x80) : Réussi
		
		// Test 4 -> octet maximal
		System.out.println("Test 4 (0xFF) : " + (aes.deux_a(0xFF) == 0xE5 ? "Réussi" : "ÉCHEC"));  // Test 4 (0xFF) : Réussi
		
		// Test 5 -> zéro
		System.out.println("Test 5 (0x00) : "  + (aes.deux_a(0x00) == 0x00 ? "Réussi" : "ÉCHEC"));   // Test 5 (0x00) : Réussi
		
		// Test 6 -> valeur négative, qui doit être refusée
		try {
		    aes.deux_a(-1);
		    System.out.println("Test 6 (valeur négative) : ÉCHEC");
		} catch (IllegalArgumentException e) {
		    System.out.println("Test 6 (valeur négative) : Réussi");   // Test 6 (valeur négative) : Réussi
		}
		
		// Test 7 -> valeur supérieure à 255, qui doit être refusée
		try {
		    aes.deux_a(256);
		    System.out.println("Test 7 (valeur supérieure à 255) : ÉCHEC");
		} catch (IllegalArgumentException e) {
		    System.out.println("Test 7 (valeur supérieure à 255) : Réussi");    // Test 7 (valeur supérieure à 255) : Réussi
		}
		System.out.println();
        
        
        
        /*
         * Test de la seizieme methode trois_a
         */
        System.out.println("Tests méthode 16 : ");
		// Test 1 -> : zéro
		System.out.println("Test 1 (0x00) : " + (aes.trois_a(0x00) == 0x00 ? "Réussi" : "ÉCHEC"));   // Test 1 (0x00) : Réussi 

		// Test 2 -> un
		System.out.println("Test 2 (0x01) : " + (aes.trois_a(0x01) == 0x03 ? "Réussi" : "ÉCHEC"));   // Test 2 (0x01) : Réussi

		// Test 3 -> valeur classique
		System.out.println("Test 3 (0x57) : " + (aes.trois_a(0x57) == 0xF9 ? "Réussi" : "ÉCHEC"));   // Test 3 (0x57) : Réussi
 
		// Test 4 -> bit de poids fort à 1
		System.out.println("Test 4 (0x80) : " + (aes.trois_a(0x80) == 0x9B ? "Réussi" : "ÉCHEC"));   // Test 4 (0x80) : Réussi

		// Test 5 -> octet maximal
		System.out.println("Test 5 (0xFF) : " + (aes.trois_a(0xFF) == 0x1A ? "Réussi" : "ÉCHEC"));   // Test 5 (0xFF) : Réussi
 
		// Test 6 -> valeur négative
		try {
		    aes.trois_a(-1);
		    System.out.println("Test 6 (valeur négative) : ÉCHEC");
		} catch (IllegalArgumentException e) {
		    System.out.println("Test 6 (valeur négative) : Réussi");   // Test 6 (valeur négative) : Réussi
		}

		// TEST 7 : valeur supérieure à 255
		try {
		    aes.trois_a(256);
		    System.out.println("Test 7 (valeur supérieure à 255) : ÉCHEC");
		} catch (IllegalArgumentException e) {
		    System.out.println("Test 7 (valeur supérieure à 255) : Réussi");   // Test 7 (valeur supérieure à 255) : Réussi
		}
		
    }
}

