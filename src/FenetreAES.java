import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.nio.file.Files;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * FenetreAE est l'interface graphique principale de l'application de cryptographie AES
 * 
 * Elle permet à l'utilisateur de :
 * 	Choisir la taille de la clé maître AES (128, 192 ou 256 bits)
 *	Saisir du texte ou charger un fichier texte (.txt) à chiffrer/déchiffrer
 *	Visualiser les résultats sous forme de flux binaire ou de texte clair restauré
 *	Exporter et sauvegarder les résultats dans de nouveaux fichiers .txt
 *
 * Le chiffrement et le déchiffrement sont réalisés par la classe {@link AES}. 
 * Cette fenêtre assure principalement la gestion de l'interface graphique et des interactions avec l'utilisateur
 *
 * @see AES
 */
public class FenetreAES extends JFrame {
	
	
	/**
     * Objet AES utilisé pour le chiffrement et le déchiffrement
     *
     * Il conserve notamment la clé maître générée par le constructeur de AES
     * Le même objet doit être utilisé pour les deux opérations
     */
    private AES aes;
	
	/** Référence vers le fichier .txt d'origine chargé via le bouton Browser
	 * Cette référence sert notamment à proposer un nom de fichier de sortie.
	 */
	private File fichierOriginal = null; // Pour garder une trace du fichier .txt chargé
	
	private JComboBox<String> tailleClé;  // Menu déroulant pour choisir la taille de la clé : 128, 192 ou 256 bits
	private JLabel l1, l2, l3;   // Étiquettes textuelles descriptives
	private JTextArea zoneClair, zoneRésultat;    // Zones de texte pour les entrées et les sorties
	private JButton boutonCrypter, boutonDecrypter, boutonBrowser;   // Boutons d'action de l'application 
	
	
	/**
	 * Constructeur de la fenêtre principale de l'application AES
	 * 
	 *Le constructeur initialise les composants graphiques, définit leur disposition et associe chaque bouton à la méthode correspondante
	 */
	public FenetreAES() {
		super("AES CRYPTER/DECRYPTER");   // Titre de la fenêtre
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);   // Arrête le programme proprement quand on clique sur la croix X
		this.setLayout(new BorderLayout(10,10));   // Disposition globale de la fenêtre
		
		// Configuration Panel principal avec une grille (GridBagLayout)
		JPanel mainPanel = new JPanel(new GridBagLayout());   // Crée le panneau principal avec une grille flexible
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));   // Marges intérieures globales
        GridBagConstraints gbc = new GridBagConstraints();   //GridBagConstraints définit la position et le comportement des composants ajoutés au GridBagLayou
        gbc.insets = new Insets(10, 10, 10, 10);   // Espacement externe autour de chaque composant
        gbc.anchor = GridBagConstraints.WEST;   // Alignement par défaut à l'ouest (gauche)
		
        // Initialisation des composants interactifs
		this.tailleClé = new JComboBox<>(new String[]{"128 bits", "192 bits", "256 bits"});   // Liste déroulante avec les trois tailles de clés autorisées
		// Étiquettes descriptives
		this.l1 = new JLabel("Clés maître de taille : ");  
		this.l2 = new JLabel("Texte à crypter/décrypter : ");
		this.l3 = new JLabel("Résultat du texte : ");
		
		// Zone de saisie du texte
		this.zoneClair = new JTextArea(5, 35);
		zoneClair.setLineWrap(true);   // Autorise le retour automatique à la ligne
		zoneClair.setWrapStyleWord(true);   // Coupe les lignes de préférence entre les mots
		zoneClair.setText("Veuillez entrer un texte");     // Texte indicatif affiché au démarrage
		// Effacer le texte par défaut au premier clic
		zoneClair.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (zoneClair.getText().equals("Veuillez entrer un texte")) {
                	zoneClair.setText("");
                }
            }
        });
		
		// Zone d'affichage du résultat  du chiffrement ou du déchiffrement
		this.zoneRésultat = new JTextArea(5, 35);
		zoneRésultat.setLineWrap(true);
		zoneRésultat.setWrapStyleWord(true);
		zoneRésultat.setEditable(false);    // Empêche la modification manuelle des résultats
        
		// Boutons d'action principale
        this.boutonCrypter = new JButton("Crypter");
        boutonCrypter.setPreferredSize(new Dimension(110, 30));    // Dimension du bouton
        boutonCrypter.addActionListener(e -> crypterTexte());    // Appelle CrypterTexte() lorsque l'utilisateur clique sur le bouton
        
        this.boutonDecrypter = new JButton("Décrypter");
        boutonDecrypter.setPreferredSize(new Dimension(110, 30));
        boutonDecrypter.addActionListener(e -> decrypterTexte());   // Appelle DecrypterTexte() lorsque l'utilisateur clique sur le bouton
		
        this.boutonBrowser = new JButton("Browser");
		boutonBrowser.setBackground(new Color(0, 150, 255));  // Couleur de fond du bouton
		boutonBrowser.setForeground(Color.WHITE);
		boutonBrowser.setFocusPainted(false);
        
		// Taille clé maitre
		// Première ligne, première colonne : l'étiquette
		gbc.gridx = 0; 
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
        mainPanel.add(this.l1, gbc);
        
        // Deuxième colonne : la liste déroulante
        tailleClé.setPreferredSize(new Dimension(250, 25));
        gbc.gridx = 1; 
        gbc.gridy = 0;
        gbc.gridwidth = 2;   // Le composant occupe les colonnes 1 et 2
        // Autorise l'élargissement horizontal du composant
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(tailleClé, gbc);
        gbc.gridwidth = 1; // Réinitialise la largeur pour les prochains composants
        
        
        //Texte à crypter / décrypter + Bouton Browser
        // L'étiquette se trouve au début de la deuxième ligne
        gbc.gridx = 0; 
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;   // Aligne l'étiquette en haut à gauche
        mainPanel.add(this.l2, gbc);
        
        // JScrollPane ajoute des barres de défilement à la zone de texte lorsqu'il y a trop de contenu pour tout afficher
        JScrollPane scrollInput = new JScrollPane(zoneClair);
        // La zone peut s'agrandir horizontalement et verticalement
        gbc.gridx = 1; 
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(scrollInput, gbc);

        // Le bouton ouvre un sélecteur de fichiers   
        boutonBrowser.addActionListener(e -> parcourirFichiers());
        // Position du bouton Browser, à droite de la zone de saisie
        gbc.gridx = 2; 
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(boutonBrowser, gbc);
        
        
        // Résultat du texte
        // L'étiquette du résultat se situe sur la troisième ligne
        gbc.gridx = 0; 
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(this.l3, gbc);

        // Ajoute des barres de défilement au résultat.
        JScrollPane scrollResult = new JScrollPane(zoneRésultat);
        // Positionne la zone de résultat sur la troisième ligne
        gbc.gridx = 1; 
        gbc.gridy = 2;
        // Autorise la zone à s'étendre dans les deux directions
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(scrollResult, gbc);
        gbc.gridwidth = 1; // Réinitialise la largeur occupée par un composant
        
        // Ajoute le panneau principal au centre de la fenêtre.  
        add(mainPanel, BorderLayout.CENTER);
        
        
        // Boutons Chiffrer / Déchiffrer
        // FlowLayout centre les boutons et ajoute un espace entre eux.
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelButtons.add(boutonCrypter);
        panelButtons.add(boutonDecrypter);
        // Place les deux boutons en bas de la fenêtre
        add(panelButtons, BorderLayout.SOUTH);
		
        // Définit la taille initiale de la fenêtre
		this.setSize(700, 500);   // Dimension de la fenêtre (largeur, hauteur)
		this.setLocationRelativeTo(null); // Centre la fenêtre sur l'écran
		this.setVisible(true);    // Rend la fenêtre visible
	} 
	
	
	/**
     * Ouvre un sélecteur de fichiers pour charger un document texte
     *
     * Seuls les fichiers dont le nom se termine par .txt sont acceptés
     * Après lecture, leur contenu est placé dans la zone de saisie
     *
     * Le chemin du fichier est conservé afin de proposer un nom pertinent lors de la sauvegarde du résultat.
     */
    private void parcourirFichiers() {
        JFileChooser fileChooser = new JFileChooser();   // Crée la fenêtre de sélection de fichiers
        int result = fileChooser.showOpenDialog(this);   // Affiche la fenêtre et récupère l'action de l'utilisateur
        // Continue uniquement si l'utilisateur a choisi un fichier
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            //Vérifie que le fichier choisi possède l'extension .txt
	        if (!selectedFile.getName().toLowerCase().endsWith(".txt")) {
	            JOptionPane.showMessageDialog(this, "Veuillez sélectionner uniquement un fichier au format .txt", "Erreur de format", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
            try {
            	// Lit tout le contenu du fichier textuel et l'affiche dans la zone clair
            	String contenu = Files.readString(fichierOriginal.toPath());
	            fichierOriginal = selectedFile;   // Conserve le fichier uniquement si la lecture a réussi
	            zoneClair.setText(contenu);   // Affiche le contenu du fichier dans la zone de saisie
            } catch (Exception e) {
            	// Informe l'utilisateur en cas de problème de lecture
                JOptionPane.showMessageDialog(this, "Impossible de lire le fichier : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            
        }
    }
    
    
    /**
     * Chiffre le texte présent dans la zone de saisie
     *
     * La taille de la clé est récupérée depuis la liste déroulante
     * Un nouvel objet AES est créé avec cette taille, puis utilisé pour chiffrer le texte
     *
     * Le résultat binaire est affiché dans la zone de résultat
     * L'utilisateur peut également choisir de sauvegarder ce résultat dans un nouveau fichier .txt
     *
     * @throws IllegalArgumentException si le texte saisi est vide ou correspond au texte indicatif
     */
    private void crypterTexte() {
        try {
            String text = zoneClair.getText();   // // Récupère le texte saisi par l'utilisateur
            // Vérifie que l'utilisateur a bien fourni un texte
            if (text.isEmpty() || text.equals("Veuillez entrer un texte")) {
                throw new IllegalArgumentException("Veuillez entrer un texte valide à chiffrer.");
            }
            
            // Récupère la taille de la clé choisie dans le JComboBox
            String choixTaille = (String) tailleClé.getSelectedItem();
            int tailleBits = 128; // Valeur par défaut
            if (choixTaille != null) {
                if (choixTaille.contains("192")) {
                    tailleBits = 192;
                } else if (choixTaille.contains("256")) {
                    tailleBits = 256;
                }
            }
            
            // Initialise l'objet AES avec la taille choisie
            aes = new AES(tailleBits);
            
            // Chiffre le texte et récupère le résultat sous forme de bits
            int[] encryptedBits = aes.crypte(text);
            System.out.println("Taille exacte générée par l'AES : " + encryptedBits.length);
            
            // Convertit chaque bit (0 ou 1) en caractère afin de l'afficher
            StringBuilder sb = new StringBuilder();
            for (int bit : encryptedBits) {
                sb.append(bit);
            }
            
            // Affiche le résultat binaire dans la zone de résultat.
            zoneRésultat.setText(sb.toString());
            
            // Demande à l'utilisateur s'il souhaite sauvegarder le résultat.
            int choix = JOptionPane.showConfirmDialog(this,"Voulez-vous sauvegarder le texte chiffré dans un nouveau fichier .txt ?", "Sauvegarder le fichier chiffré", JOptionPane.YES_NO_OPTION);
	        
            // Ouvre le sélecteur de sauvegarde uniquement si l'utilisateur accepte
            if (choix == JOptionPane.YES_OPTION) {
	            JFileChooser saveChooser = new JFileChooser();
	            // Propose un nom basé sur le fichier source lorsqu'il existe
                if (fichierOriginal != null) {
	                saveChooser.setSelectedFile(new File(fichierOriginal.getParent(), "chiffre_" + fichierOriginal.getName()));
	            } else {
	            	// Propose un nom générique
	                saveChooser.setSelectedFile(new File("texte_chiffre.txt"));
	            }
	            int saveResult = saveChooser.showSaveDialog(this);
	            // Continue uniquement si l'utilisateur confirme un emplacement
                if (saveResult == JFileChooser.APPROVE_OPTION) {
	                File fichierCible = saveChooser.getSelectedFile();
	                // Ajoute l'extension .txt si elle est absente
                    if (!fichierCible.getName().toLowerCase().endsWith(".txt")) {
	                    fichierCible = new File(fichierCible.getAbsolutePath() + ".txt");
	                }
                    // Écrit le résultat binaire dans le fichier
                    Files.writeString(fichierCible.toPath(), sb.toString());
	                JOptionPane.showMessageDialog(this, "Fichier chiffré créé avec succès !");
	            }
	        }
        } catch (Exception ex) {
        	// Affiche l'erreur rencontrée lors du chiffrement ou de la sauvegarde.
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur de Chiffrement", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    /**
     * Déchiffre un message binaire saisi ou chargé dans la zone de saisie
     *
     * La méthode supprime les espaces et les retours à la ligne, vérifie que les caractères sont uniquement des 0 et des 1, puis transforme la chaîne en tableau d'entiers avant d'appeler aes.decrypte()
     *
     * Le résultat en texte clair est affiché dans la zone de résultat
     * L'utilisateur peut choisir de le sauvegarder dans un fichier .txt
     *
     * Le même objet AES et la même clé que lors du chiffrement sont nécessaires
     *
     * @throws IllegalStateException si aucun chiffrement n'a encore été effectué dans cette fenêtre
     * @throws IllegalArgumentException si la taille ou le contenu du message binaire est invalide
     */
    private void decrypterTexte() {
        try {
        	// Vérifie qu'un objet AES existe déjà. Sans lui la clé maître du chiffrement n'est pas disponible
            if (aes == null) {
                throw new IllegalStateException("Aucun chiffrement n'a encore été effectué dans cette fenêtre " + "La clé nécessaire au déchiffrement n'est pas disponible");
            }
            
            String textBrut = zoneClair.getText();   // Récupère le texte à déchiffrer depuis la zone de saisie
            // Vérifie que la zone ne soit pas vide ou contient uniquement le texte indicatif
            if (textBrut.isEmpty() || textBrut.equals("Veuillez entrer un texte")) {
                throw new IllegalArgumentException("Veuillez entrer un texte déchiffré valide.");
            }
            
            // On supprime tous les espaces, tabulations et retours à la ligne
            String text = textBrut.replaceAll("\\s+", "");
             // Vérifie que la longueyr du texte soit bien un multiple de 128 ou non vide
            if (text.length() % 128 != 0 || text.length() == 0) {
                throw new IllegalArgumentException("Longueur invalide : " + text.length() + " caractères reçus (doit être un multiple de 8).");
            }
            System.out.println("Longueur exacte reçue pour déchiffrement : " + text.length());
            
            // Récupère la taille de la clé choisie dans le JComboBox
            String choixTaille = (String) tailleClé.getSelectedItem();
            int tailleBits = 128; // Valeur par défaut
            if (choixTaille != null) {
                if (choixTaille.contains("192")) {
                    tailleBits = 192;
                } else if (choixTaille.contains("256")) {
                    tailleBits = 256;
                }
            }
            
           // Transforme la chaîne de caractères de la zoneClair en tableau de bits
            int[] bitsCrypter = new int[text.length()];   // Prépare un tableau qui contiendra les bits du message chiffré
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c == '0') {
                	bitsCrypter[i] = 0;
                }
                else if (c == '1') {
                	bitsCrypter[i] = 1;
                }
                else {
                	// Refuse tout caractère qui n'appartient pas au flux binaire
                	throw new IllegalArgumentException("Le texte chiffré doit contenir uniquement des 0 et des 1.");
                }
            }
            
            // Déchiffre les bits avec le même objet AES que lors du chiffrement
            String textDecrypter = aes.decrypte(bitsCrypter);
            
            // Affiche le texte clair retrouvé
            zoneRésultat.setText(textDecrypter);
            
            // Demande si l'utilisateur souhaite sauvegarder le texte déchiffré.
            int choix = JOptionPane.showConfirmDialog(this,"Voulez-vous restaurer et sauvegarder le fichier déchiffré en .txt ?", "Restaurer le fichier original", JOptionPane.YES_NO_OPTION);
	        
	        if (choix == JOptionPane.YES_OPTION) {
	            JFileChooser saveChooser = new JFileChooser();
	            // Propose un nom par défaut pour le fichier restauré
                saveChooser.setSelectedFile(new File("original_restaure.txt"));
	            
	            int saveResult = saveChooser.showSaveDialog(this);
	            // Sauvegarde uniquement si l'utilisateur confirme
                if (saveResult == JFileChooser.APPROVE_OPTION) {
	                File fichierCible = saveChooser.getSelectedFile();
	                // Ajoute .txt si l'extension est absente
                    if (!fichierCible.getName().toLowerCase().endsWith(".txt")) {
	                    fichierCible = new File(fichierCible.getAbsolutePath() + ".txt");
	                }
                    // Écrit le texte restauré dans le fichier de sortie
                    Files.writeString(fichierCible.toPath(), textDecrypter);
	                JOptionPane.showMessageDialog(this, "Fichier original restauré avec succès !");
	            }
	        }
        } catch (Exception ex) {
        	// Affiche les erreurs de saisie, de déchiffrement ou de sauvegarde
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur de Déchiffrement", JOptionPane.ERROR_MESSAGE);
        }
    }


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new FenetreAES();
	}


}
