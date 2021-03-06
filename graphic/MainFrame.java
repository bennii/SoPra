package graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;

import main.Datenhaltung;
import main.Portal;
import accounts.Account;
import accounts.AlreadyInUseException;
import accounts.Anbieter;
import accounts.Default;
import accounts.Kunde;
import accounts.Nachricht;
import angebote.typen.Angebot;
import buchungen.Buchung;

@SuppressWarnings("serial")

/**
 * Hauptoberflaeche von der aus die Screens gesteuert werden
 * 
 */
public class MainFrame extends JFrame
{
	public static final int BUTTONWIDTH = 180;
	public static final int BUTTONHEIGHT = 38;
	public static final int TEXTFIELDLENGTH = 42;
	
	private JButton loginButton;
	private JButton registerButton;
	private JButton nachrichtButton;
	private JButton eigeneButton;
	private JButton sucheButton;
	private JButton topButton;
	private JButton alleButton;
	private JButton erstelleButton;
	private JButton betreiberButton;
	private JButton offeneButton;
	private JButton loeschenButton;
	
	private Account account = new Default();
	private JPanel screen;
	private JScrollPane scroll;
	
	@SuppressWarnings("rawtypes")
	private ListeScreen list;
	
	private boolean logged = false;
	
	private MainFrame frame = this; //quick'n'dirty, wird genutzt um den dialogscreen die 
	private String agbFromFile;

	/**
	 * Der Konstruktor erstellt das Hauptbild mit den Buttons, ihren Funktionen und erstellt die unterteilten Screens
	 */
	public MainFrame()
	{
		// JFrame einstellungen
		
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (d.width - getSize().width/2);
	    int y = (d.height - getSize().height/2);
	    this.setLocation(x/8, y/8);
		this.setPreferredSize(new Dimension(x*2/3, y*2/3));
		
		// oberflaeche aendern
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
		
		//windowlistener
		
		this.addWindowListener(new WindowAdapter() 
        { 
			@Override
            public void windowClosing(WindowEvent evt) 
            {
        		if(Portal.Accountverwaltung().getLoggedIn().getTyp() == Account.BETREIBER)
        		{
        			try
        			{
	        			Portal.Accountverwaltung().logOut();
	        			System.exit(0);
        			}
        			catch(Exception e)
        			{
        				JOptionPane.showMessageDialog(frame, e.getMessage());
        			}
        		}
        		else
        		{
        			JOptionPane.showMessageDialog(frame, "Dafuer haben Sie keine Berechtigung");
        		}
            }
        });
		
		////////// border und bild

		Border border = BorderFactory.createMatteBorder(0, 2, 0, 2, Color.LIGHT_GRAY);
		
		/////////// alle panels die auf dem frame liegen, sowie die scrollpane
		
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBorder(border);
		this.add(headerPanel, BorderLayout.NORTH);
		
		JPanel menuPanel = new JPanel(new BorderLayout());
		menuPanel.setBorder(border);
		this.add(menuPanel, BorderLayout.WEST);
		
		JPanel registerPanel = new JPanel(new GridLayout(1,0));
		headerPanel.add(registerPanel, BorderLayout.EAST);
		
		GridLayout grid = new GridLayout(0,1);
		grid.setVgap(4);
		JPanel buttonPanel = new JPanel(grid);
		menuPanel.add(buttonPanel, BorderLayout.NORTH);
		
		screen = new JPanel(new GridLayout(1,0));
		screen.setBorder(border);
		
		scroll = new JScrollPane(screen);
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		this.add(scroll, BorderLayout.CENTER);

		JPanel homeButtonPanel = new JPanel();
		headerPanel.add(homeButtonPanel, BorderLayout.WEST);
		
		//////////////// alle buttons die auf dem frame liegen

		JButton homeButton = new JButton(new ImageIcon("house_unpressed.png"));

		homeButton.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT*2));
		homeButton.setPressedIcon(new ImageIcon("house_pressed.png"));
		homeButton.setOpaque(false);
		homeButton.setContentAreaFilled(false);
		homeButton.setBorderPainted(false);
		homeButtonPanel.add(homeButton);

		loginButton = new JButton("Login");
		loginButton.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
		registerButton = new JButton("Registrieren");
		registerButton.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
		sucheButton = new JButton("Suche Angebote");
		sucheButton.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
		topButton = new JButton("Top Angebote");
		topButton.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
		nachrichtButton = new JButton("Nachrichten");
		nachrichtButton.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
		nachrichtButton.setEnabled(false);
		eigeneButton = new JButton("Angebote/Buchungen");
		eigeneButton.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
		eigeneButton.setEnabled(false);
		alleButton = new JButton("Alle Angebote");
		alleButton.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
		erstelleButton = new JButton("Angebot erstellen");
		erstelleButton.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
		erstelleButton.setEnabled(false);
		betreiberButton = new JButton("Betreiber hinzufuegen");
		betreiberButton.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
		betreiberButton.setVisible(false);
		offeneButton = new JButton("Kundenbuchungen");
		offeneButton.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
		offeneButton.setVisible(false);
		loeschenButton = new JButton("Account Loeschen");
		loeschenButton.setEnabled(false);
		loeschenButton.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
		
		buttonPanel.add(loginButton);
		buttonPanel.add(eigeneButton);
		buttonPanel.add(sucheButton);
		buttonPanel.add(nachrichtButton);
		buttonPanel.add(erstelleButton);
		buttonPanel.add(topButton);
		buttonPanel.add(alleButton);
		buttonPanel.add(offeneButton);
		buttonPanel.add(betreiberButton);
		buttonPanel.add(loeschenButton);
		registerPanel.add(registerButton);

		// /////////	alle buttonactionlistener
		
		homeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showTopAngebote();}});
		topButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showTopAngebote();}});
		loginButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showLogin();}});
		registerButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showRegister();}});
		eigeneButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent argR0) {
				showEigene();}});
		sucheButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showSuche();}});
		nachrichtButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showNachrichten();}});
		alleButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showAlleAngebote();}});
		erstelleButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showErstelle();}});
		betreiberButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addBetreiber();}});
		offeneButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showOffeneBuchungen();}});
		loeschenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0){
				showLoeschen();}});
		
		//////////////// finish

		this.showTopAngebote();
		this.pack();
		this.setVisible(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	
	/**
	 * Diese Methode zeigt das uebergebene Listable im MainScreen an. Dabei kann es sich um ein Angebot, eine Buchung, einen Account oder eine Nachricht handeln.
	 * 
	 * @param obj
	 * @see Listable
	 * @see Angebot
	 * @see Buchung
	 * @see Nachricht
	 * @see Account
	 */
	public <T extends Listable> void showDetail(T obj) 
	{
		try
		{
			if(obj.getListableTyp() == Listable.ANGEBOT)
			{
				screen.removeAll();
				screen.add(new AngDetailScreen(this, (Angebot)obj)); // zeige die details des angebots an
				scroll.setViewportView(screen);
				scroll.repaint();
			}
			else if(obj.getListableTyp() == Buchung.BUCHUNG)
			{
				screen.removeAll();
				screen.add(new BuchDetailScreen(this,offeneButton,(Buchung)obj)); // zeige die details der buchung an
				scroll.setViewportView(screen);
				scroll.repaint();
			}
			else if(obj.getListableTyp() == Account.ACCOUNT)
			{
				screen.removeAll();
				screen.add(new AccountScreen((Account)obj, eigeneButton)); // zeige die details des accounts an
				scroll.setViewportView(screen);
				scroll.repaint();
			}
			else
			{
				
				/////// die nachricht wird als DialogScreen angezeigt. wenn es sich um den anbieter handelt, wird statt dem angebot die buchung gezeigt
				
				final Nachricht nachricht = (Nachricht)obj;
				final Account absender = Portal.Accountverwaltung().getAccountByName(nachricht.getAbsender());
				JButton del = new JButton("Nachricht loeschen");
				
				// hauptdialogscreen der die nachricht enthaelt
				
				final DialogScreen dialog = new DialogScreen(this, nachricht.getBetreff(), new JButton[]{del},DialogScreen.OK_OFFER_ANSWER_OPTION)
				{
					// ueberschreibe die buttons auf dem dialog
					@Override
					public void onAnswer() // neuer DialogScreen als antwortmoeglichkeit
					{
						DialogScreen dialog = new DialogScreen(frame, "Kontaktieren", DialogScreen.OK_CANCEL_OPTION)
						{
							@Override
							public void onOK()
							{
								if(getContent().length() != 0)
									Portal.Nachrichtenverwaltung().sendeNachricht(account, absender, "RE: "+nachricht.getBetreff(),getContent(), Portal.Angebotsverwaltung().getAngebotByNummer(nachricht.getAngebotsNummer()));
								else
									JOptionPane.showMessageDialog(frame, "Sie muessen eine Nachricht eingeben!");
							}
						};
						dialog.addOnPanel(new JLabel(account.getName()), DialogScreen.LABEL_LEFT);
					}
					@Override
					public void onOffer() // zeige buchung, wenn anbieter, sonst zeige angebot
					{
						int nummer = Portal.Nachrichtenverwaltung().getBuchungsNummer(nachricht);
						if(account.getTyp() == Account.ANBIETER)
							if(nummer != Nachricht.KEINE_BUCHUNG)
								showDetail(Portal.Buchungsverwaltung().getBuchungByBuchungsNummer(nummer));
							else
								JOptionPane.showMessageDialog(this, "Keine Buchung gefunden!");
						else
							showDetail(Portal.Angebotsverwaltung().getAngebotByNummer(nachricht.getAngebotsNummer()));
					}
				};
				
				// der loeschbutton bekommt auch eine funktion - das loeschen!
				
				del.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						if(JOptionPane.showConfirmDialog(dialog, "Wollen Sie diese Nachricht wirklich Loeschen?", "Nachricht loeschen", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
						{
							Portal.Nachrichtenverwaltung().delNachricht(nachricht);
							JOptionPane.showMessageDialog(dialog, "Ihre Nachricht wurde geloescht");
							showNachrichten();
							dialog.close();
						}
					}});
				
				///// resteinstellungen fuer den DialogScreen und den JButton
				
				dialog.setEditable(false);
				dialog.addOnPanel(new JLabel("Absender: "+nachricht.getAbsender()), DialogScreen.LABEL_LEFT);
				dialog.setContent(nachricht.getText());
				nachricht.setGelesen(true);
				nachrichtButton.setText("Nachricht"+" ("+Portal.Nachrichtenverwaltung().getAnzahlUngelesenerNachrichten(account)+")");
				
				if(account.getTyp() == Account.ANBIETER)
					dialog.setOfferButtonName("Zur Buchung");
				
				// repaint
				
				this.validate();
				this.repaint();
				scroll.repaint();
			}
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	/////////////////////////////////
	///////////////////////// alle Screen-Show-Methoden
	/////////////////////////////////
	
	/**
	 *  Zeigt den LoginScreen als JOptionPane.
	 * 
	 */
	private void showLogin() 
	{
		try
		{
			if(!logged) // nur wenn eingelogged
			{
				// alles was auf das JOptionPane kommt, was ausgefuellt werden muss
				JLabel nameLabel = new JLabel("Name");
				JTextField nameField = new JTextField();
				JLabel passwordLabel = new JLabel("Passwort");
				JPasswordField passwordField = new JPasswordField();
				JLabel label = new JLabel("Bitte geben Sie die Anmeldeinformationen an");
		
				if(JOptionPane.showConfirmDialog(this,new Object[]{label, nameLabel, nameField, passwordLabel, passwordField},"Login",JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
				{// bei OK:
					
					account = Portal.Accountverwaltung().logIn(nameField.getText(), new String(passwordField.getPassword()));
					
					// aendere die gestalt des mainframes
					eigeneButton.setEnabled(true);
					nachrichtButton.setEnabled(true);
					registerButton.setEnabled(false);
					loeschenButton.setEnabled(true);
					
					nachrichtButton.setText("Nachricht"+" ("+Portal.Nachrichtenverwaltung().getAnzahlUngelesenerNachrichten(account)+")");
					loginButton.setText("Logout");
					
					JOptionPane.showMessageDialog(this, "Erfolgreich angemeldet");
					
					// jenachdem wer angemeldet ist, andere ansicht:
					
					if(account.getTyp() == Account.KUNDE)
					{
						eigeneButton.setText("Eigene Buchungen");
						if(((Kunde)account).getFirstLogin()){
							JOptionPane.showMessageDialog(this, "Herzlich willkommen. Wir wuenschen Ihnen viel Spass.");
							((Kunde)account).setFirstLogin();
						}
					}
					else if(account.getTyp() == Account.ANBIETER)
					{	
						eigeneButton.setText("Eigene Angebote");
						erstelleButton.setEnabled(true);
						offeneButton.setText("Kundenbuchungen "+"("+Portal.Buchungsverwaltung().getAnzahlUnbearbeiteterBuchungen((Anbieter)account)+")");
						offeneButton.setVisible(true);
						if(((Anbieter)account).getFirstLogin()){
							JOptionPane.showMessageDialog(this, "Herzlich willkommen. Wir wuenschen Ihnen viel Erfolg.");
							((Anbieter)account).setFirstLogin();
						}
					}
					else if(account.getTyp() == Account.BETREIBER)
					{
						eigeneButton.setText("Alle Accounts ("+Portal.Accountverwaltung().getUnbearbeiteteAnbieter().size()+")");
						offeneButton.setVisible(true);
						offeneButton.setText("Alle Buchungen");
						betreiberButton.setVisible(true);
					}
					
					// finish:
					
					showTopAngebote();
					this.setTitle("Eingeloggt als: "+account.getName());
					this.repaint();
					logged = true;
				}
			}
			else // ansonsten ruf die methode logout auf
				logOut();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	/**
	 * Zeigt das Registrierungsfenster als JOptionPane an.
	 * 
	 */
	private void showRegister()
	{
		try
		{
			// alle elemente die auf das JOptionPane kommen
			
			JLabel label = new JLabel("Bitte geben Sie die Registrierinformationen an");
			JLabel nameLabel = new JLabel("Name");
			final JTextField nameField = new JTextField();
			JLabel emailLabel = new JLabel("E-Mail-Adresse");
			final JTextField emailField = new JTextField();
			JLabel passwordLabel = new JLabel("Passwort");
			final JPasswordField passwordField = new JPasswordField();
			
			JLabel choice = new JLabel("Waehlen sie bitte Ihren Accounttypen");
			JComboBox drop = new JComboBox(new String[]{"Kunde","Anbieter"});
			
			// //
			
			if(JOptionPane.showConfirmDialog(this,new Object[]{label,nameLabel,nameField,emailLabel,emailField,passwordLabel,passwordField,choice,drop},"Registrierung",JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
			{
				
				// nach bestaetigung des JOptionPanes:
				
				if(drop.getSelectedIndex() == 0) // wenn kunde gewaehlt wurde, erstelle neue :
				{
					Portal.Accountverwaltung().createKunde(emailField.getText(), nameField.getText(), new String(passwordField.getPassword()));
					JOptionPane.showMessageDialog(this, "Registrierung war Erfolgreich");
				}
				else // ansonsten, also anbieter
				{
					if(!Portal.Accountverwaltung().isFreeEmail(emailField.getText())) // probier ob email schon vergeben (muss hier gemacht werden, weil sonst falsche reihenfolge des exceptiohandlings)
						throw new AlreadyInUseException("Die E-Mail-Adresse wird bereits verwendet!");
					
					// agb laden mit filechooser, dafuer ein button
					JButton fcb = new JButton("AGB laden");
					fcb.setPreferredSize(new Dimension(new Dimension(DialogScreen.BUTTONWIDTH, DialogScreen.BUTTONHEIGHT)));
					final JFileChooser fc = new JFileChooser();
					fc.setFileFilter(new FileFilter() {
						    public boolean accept(File f) {	
						    	return (f.isDirectory() || f.getName().toLowerCase().endsWith(".txt"));
						    }
						    public String getDescription () { 
						    	return "'.txt'"; 
						    }  
					});
					fc.setAcceptAllFileFilterUsed(false);
					fc.setMultiSelectionEnabled(false);
					
					// agb werden mit dem DialogScreen dargestellt
					
					
					final DialogScreen dialog = new DialogScreen(this, "Allgemeine Geschaeftsbedingungen", new JButton[]{fcb}, DialogScreen.OK_CANCEL_OPTION)
					{
						@Override
						public void onOK()
						{
							try 
							{
								Anbieter an = Portal.Accountverwaltung().createAnbieter(emailField.getText(), nameField.getText(), new String(passwordField.getPassword()));
								an.setAgb(this.getContent());
								JOptionPane.showMessageDialog(this, "Registrierung war Erfolgreich");
							} 
							catch (Exception e) 
							{
								JOptionPane.showMessageDialog(this, e.getMessage());
							}
						}
						@Override
						public void onCancel()
						{
							JOptionPane.showMessageDialog(this, "Registrierung abgebrochen!");
						}
					};
					dialog.addOnPanel(new JLabel("Bitte geben Sie Ihre allgemeinen Geschaeftsbedingungen an!"), DialogScreen.LABEL_LEFT);
					
					// filechooser soll beim klick auf button erscheinen
					fcb.addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e) 
						{
							dialog.setAlwaysOnTop(false);
							try
							{
								int x =fc.showOpenDialog(null);
								if(x==JFileChooser.APPROVE_OPTION)
								{
									agbFromFile = Datenhaltung.getStringFromFile(fc.getSelectedFile());
									dialog.setContent(agbFromFile);
								}
							}
							catch(Exception ex)
							{
								JOptionPane.showMessageDialog(null, ex.getMessage());
							}
							dialog.setAlwaysOnTop(true);
						}
					});
				}
			}
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * Wenn ein Kunde angemeldet ist, wird die Liste an eigenen Buchungen angezeigt.
	 * Wenn ein Anbieter angemeldet ist, wird die Liste an eigenen Angeboten angezeigt.
	 * Wenn ein Betreiber angemeldet ist, wir die Liste an allen Angeboten angezeigt.
	 * 
	 */
	private void showEigene()
	{
		try
		{
			screen.removeAll();
			
			if(account.getTyp() == Account.KUNDE)
				list = new ListeScreen(this, Portal.Buchungsverwaltung().getBuchungen((Kunde)account));
			else if(account.getTyp() == Account.ANBIETER)
				list = new ListeScreen(this, Portal.Angebotsverwaltung().getAngebote((Anbieter)account));
			else
				list = new ListeScreen(this, Portal.Accountverwaltung().getAccounts());
			
			screen.add(list);
			scroll.setViewportView(screen);
			scroll.repaint();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	/**
	 * Zeigt das Suchformular auf dem Hauptbild an.
	 */
	private void showSuche()
	{
		try
		{
			screen.removeAll();
			screen.add(new SuchScreen() // das suchformular hat eine buttonfunktion die in hier ueberschrieben wird
			{
				@SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
				public void onSearch()
				{
					if(this.getList() != null)
					{
						screen.removeAll();
						list = new ListeScreen(frame, this.getList());
						screen.add(list);
						scroll.setViewportView(screen);
						scroll.repaint();
					}
					else
						JOptionPane.showMessageDialog(this, "Es wurden keine Ergebnisse gefunden");
				}
			});
			scroll.setViewportView(screen);
			scroll.repaint();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	/**
	 * Zeigt die Liste aller Nachrichten des Aktuellen Benutzers auf dem Hauptbild an.
	 */
	public void showNachrichten()
	{
		try
		{
			screen.removeAll();
			list = new ListeScreen<Nachricht>(this, Portal.Nachrichtenverwaltung().getErhalteneNachrichten(account));
			screen.add(list);
			scroll.setViewportView(screen);
			scroll.repaint();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());			
		}
	}
	
	/**
	 * Zeigt alle Topangebot in einer Liste auf dem Hauptbild an.
	 */
	public void showTopAngebote()
	{
		try
		{
			screen.removeAll();
			list = new ListeScreen<Angebot>(this, Portal.Angebotsverarbeitung().getTopAngebote());
			screen.add(list);
			scroll.setViewportView(screen);
			scroll.repaint();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	/**
	 * Zeige das Erstellungsformular auf dem Hauptbild an.
	 */
	private void showErstelle()
	{
		try
		{
			screen.removeAll();
			screen.add(new AngebotCreate((Anbieter)account));
			scroll.setViewportView(screen);
			scroll.repaint();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * Zeige alle Angebote in einer Liste auf dem Hauptbild an.
	 */
	private void showAlleAngebote()
	{
		try
		{
			screen.removeAll();
			list = new ListeScreen(this, Portal.Angebotsverarbeitung().getAktuelleAngebote());
			screen.add(list);
			scroll.setViewportView(screen);
			scroll.repaint();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * Zeige alle offene Buchungen des Anbieters an, bzw alle Buchungen aller Kunden als Betreiber
	 */
	private void showOffeneBuchungen()
	{
		try
		{

			screen.removeAll();
			if(account.getTyp() == Account.BETREIBER)
				list = new ListeScreen(this, Portal.Buchungsverwaltung().getAllBuchungen());
			else
				list = new ListeScreen(this, Portal.Buchungsverwaltung().getBuchungen((Anbieter)account));
			screen.add(list);
			scroll.setViewportView(screen);
			scroll.repaint();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	/**
	 * Loescht den aktuell angemeldeten Account.
	 */
	private void showLoeschen()
	{
		try
		{
			if(JOptionPane.showConfirmDialog(this, "Moechten Sie den Account wirklich loeschen?", "Loeschen?", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) 
			{
				Portal.Accountverwaltung().delAccount(account);
				JOptionPane.showMessageDialog(this, "Das Loeschen ihres Accounts war erfolgreich!");
				logOut();
			}
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	/**
	 * Meldet en aktuellen Account aus und setzt die JButtons wieder in die ursprungsvariante an.
	 */
	private void logOut() 
	{
		try
		{
			Portal.Accountverwaltung().logOut();
			JOptionPane.showMessageDialog(this, "Erfolgreich Abgemeldet"+"\n"+"Danke und auf Wiedersehen!");
			showTopAngebote();
			
			eigeneButton.setEnabled(false);
			eigeneButton.setText("Angebote/Buchungen");
			nachrichtButton.setEnabled(false);
			erstelleButton.setEnabled(false);
			loginButton.setText("Login");
			nachrichtButton.setText("Nachrichten");
			registerButton.setEnabled(true);
			betreiberButton.setVisible(false);
			offeneButton.setVisible(false);
			loeschenButton.setEnabled(false);
	
			this.setTitle("");
			this.repaint();
			logged = false;
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	/**
	 * Zeigt einen JOptionPane an, mit dem man einen neuen Betreiber erstellen kann.
	 */
	private void addBetreiber()
	{
		try
		{
			JLabel label = new JLabel("Bitte geben Sie die Registrierinformationen an");
			JLabel nameLabel = new JLabel("Name");
			final JTextField nameField = new JTextField();
			JLabel emailLabel = new JLabel("E-Mail-Adresse");
			final JTextField emailField = new JTextField();
			JLabel passwordLabel = new JLabel("Passwort");
			final JPasswordField passwordField = new JPasswordField();
			
			if(JOptionPane.showConfirmDialog(this,new Object[]{label,nameLabel,nameField,emailLabel,emailField,passwordLabel,passwordField},"Betreiber hinzufuegen",JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
				Portal.Accountverwaltung().addBetreiber(emailField.getText(), nameField.getText(), new String(passwordField.getPassword()));
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
}