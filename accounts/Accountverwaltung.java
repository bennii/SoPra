package accounts;

import graphic.Methods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import main.Datenhaltung;
import main.Portal;
import angebote.Angebotsverwaltung;
import angebote.typen.Angebot;
import buchungen.Bestaetigung;
import buchungen.Buchung;
import buchungen.Buchungsverwaltung;

/**
 * Accoutnverwaltung
 * 
 * Verwaltet alle drei Accounttypen. Fuehrt saemtliche nicht-triviale Methoden
 * fuer Accounts aus.
 * 
 * @author jay, stephan, osca
 */
public class Accountverwaltung {

	private ArrayList<Anbieter> anbieter = new ArrayList<Anbieter>();
	private ArrayList<Betreiber> betreiber = new ArrayList<Betreiber>();
	private ArrayList<Kunde> kunden = new ArrayList<Kunde>();
	private Account loggedIn = new Default();

	/**
	 * Konstruktor mit leeren Benutzer-Listen
	 * 
	 * @param anb
	 *            Anbieter
	 * @param betr
	 *            Betreiber
	 * @param kund
	 *            Kunde
	 */
	public Accountverwaltung() {
	}

	/**
	 * Legt die Accountverwaltung mit den gewuenschten Listen an.
	 * 
	 * @param anbieter Anbieterliste
	 * @param betreiber Betreiberliste
	 * @param kunden Kundenliste
	 */
	public Accountverwaltung(ArrayList<Anbieter> anbieter,
			ArrayList<Betreiber> betreiber, ArrayList<Kunde> kunden) {
		this.anbieter = anbieter;
		this.betreiber = betreiber;
		this.kunden = kunden;
	}

	/**
	 * Erstellt den jeweiligen Account der als Accounttyp uebergeben wird
	 * 
	 * @param typFlag Flag des Accounttypen
	 * @param email	E-Mail Adresse
	 * @param name Username
	 * @param password Passwort
	 * @return erstellter Account
	 * @throws AlreadyInUseException Username oder E-Mail Adresse wird schon benutzt
	 * @throws IOException
	 */
	public Account createAccount(int typFlag, String email, String name,
			String password) throws AlreadyInUseException, IOException {
		switch (typFlag) {
		case Account.NONE:
			return new Default();
		case Account.ANBIETER:
			return createAnbieter(email, name, password);
		case Account.BETREIBER:
			return createBetreiber(email, name, password);
		case Account.KUNDE:
			return createKunde(email, name, password);
		default:
			return null;
		}
	}

	/**
	 * Erstelle Kunde
	 * 
	 * @param email
	 *            E-Mail Adresse
	 * @param name
	 *            Username
	 * @param password
	 *            Password
	 * @throws AlreadyInUseException
	 *             Account E-Mail oder Username schon vergeben
	 * @throws IOException 
	 */
	public Kunde createKunde(String email, String name, String password)
			throws AlreadyInUseException, IOException {
		if (!isFreeEmail(email) || !isFreeName(name))
			throw new AlreadyInUseException("Name oder E-Mail-Adresse werden bereits benutzt");
		Kunde k = new Kunde(email, name, password);
		kunden.add(k);
		Datenhaltung.saveAllData();
		return k;
	}

	/**
	 * Erstelle Anbieter
	 * 
	 * @param email
	 *            E-Mail Adresse
	 * @param name
	 *            Username
	 * @param password
	 *            Password
	 * @throws AlreadyInUseException
	 *             Account E-Mail oder Username schon vergeben
	 * @throws IOException 
	 */
	public Anbieter createAnbieter(String email, String name, String password)
			throws AlreadyInUseException, IOException {
		if (!isFreeEmail(email) || !isFreeName(name))
			throw new AlreadyInUseException("Name oder E-Mail-Adresse werden bereits benutzt");
		Anbieter a = new Anbieter(email, name, password);
		anbieter.add(a);
		Datenhaltung.saveAllData();
		return a;
	}

	/**
	 * Erstelle Betreiber
	 * 
	 * @param email
	 *            E-Mail Adresse
	 * @param name
	 *            Username
	 * @param password
	 *            Password
	 * @throws AlreadyInUseException
	 *             Account E-Mail oder Username schon vergeben
	 * @throws IOException 
	 */
	public Betreiber createBetreiber(String email, String name, String password)
			throws AlreadyInUseException, IOException {
		if (!isFreeEmail(email) || !isFreeName(name))
			throw new AlreadyInUseException("Name oder E-Mail-Adresse werden bereits benutzt");
		Betreiber b = new Betreiber(email, name, password);
		betreiber.add(b);
		Datenhaltung.saveAllData();
		return b;
	}

	/**
	 * Loggt einen Account an der Verwaltung als aktiv ein.
	 * 
	 * @param identifier
	 *            entweder E-Mail oder Nick
	 * @param password
	 *            passwort
	 * @throws LoginException
	 *             Falls Passwort nicht passend oder Account nicht gefunden (mit
	 *             entspr. Nachricht)
	 */
	public Account logIn(String identifier, String password) throws LoginException {
		Account acc = getAccountByIdentifier(identifier);
		if (acc == null)
			throw new LoginException("Account wurde nicht gefunden");
		if (!acc.getPassword().equals(Account.hashPassword(password)))
			throw new LoginException("Passwort war falsch");
		if (acc.isGesperrt()) 
			throw new LoginException("Sie sind gesperrt. Bitte wenden Sie sich an den Betreiber.");
		loggedIn = acc;
		
		return acc;
	}

	/**
	 * @return der aktuell eingeloggte Account
	 */
	public Account getLoggedIn() {
		return loggedIn;
	}

	/**
	 * Ausloggen
	 * 
	 * @throws IOException
	 * @pre Es ist ein Account eingeloggt
	 */
	public void logOut() throws IOException {
		assert !loggedIn.equals(new Default()): "Es ist niemand eingeloggt";
		Datenhaltung.saveAllData();
		loggedIn = new Default();
	}

	/**
	 * Account (de)aktivieren
	 * 
	 * @param acc
	 *            Freizuschaltender Account
	 * @param enable
	 *            Aktiv oder nicht
	 * @pre Es muss ein Betreiber eingeloggt sein
	 */
	public void setAccountGesperrt(Account acc, Gesperrt pgesperrt) {
		assert loggedIn.getTyp()==Account.BETREIBER : "Es ist kein Betreiber eingeloggt";

		acc.setGesperrt(pgesperrt);
	}

	/**
	 * Loesche einen Account und saemtliche Abhaengigkeiten
	 * 
	 * @param acc
	 *            Der zu loeschende Account
	 * @throws LoeschenNichtMoeglichException
	 *             Loeschen ist nicht moeglich
	 * @pre zu loeschender Account darf nicht null sein
	 */
	public void delAccount(Account acc) throws LoeschenNichtMoeglichException {
		assert acc != null : "null-Account kann nicht geloescht werden";

		Date heute = Methods.getHeuteNullUhr();

		switch (acc.getTyp()) {
			case (Account.ANBIETER): {
				Anbieter anbieteracc = (Anbieter) acc;
				ArrayList<Angebot> zuLoeschendeAngebote = Portal
						.Angebotsverwaltung().getAngebote(anbieteracc);
	
				// Gibt es noch offene Buchungen Schleife
				for (Angebot a : zuLoeschendeAngebote) {
					if (!a.getEnddatum().before(heute)) {
						ArrayList<Buchung> buchungen = Portal.Buchungsverwaltung().getBuchungen(a);
	
						for (Buchung b : buchungen) {
							if (!b.getBis().before(heute) && (Bestaetigung.JA.equals(b.getBestaetigt())))
								throw new LoeschenNichtMoeglichException(
										"Sie haben noch offene Buchungen");
						}
					}
				}
	
				// Angebote loeschen
				Angebotsverwaltung angebotsVerwaltung = Portal.Angebotsverwaltung();
	
				for (Angebot a : zuLoeschendeAngebote) 	
					angebotsVerwaltung.delAngebot(a);		//Buchungen werden mitgeloescht
				break;
			}
	
			case (Account.KUNDE): {
				Kunde kundenacc = (Kunde) acc;
				ArrayList<Buchung> kundenbuchungen = Portal.Buchungsverwaltung()
						.getBuchungen(kundenacc);
	
				// Hat der Kunde noch anstehende bestaetigte Buchungen?
				for (Buchung b : kundenbuchungen) {
					if (b.getBestaetigt().equals(Bestaetigung.JA)
							&& !b.getBis().before(heute)) {
						throw new LoeschenNichtMoeglichException(
								"Sie haben noch anstehende bestaetigte Buchungen");
					}
				}
	
				// Loesche saemtliche mit dem Kunden verbundene Buchungen
				Buchungsverwaltung buchungsVerwaltung = Portal.Buchungsverwaltung();
	
				for (Buchung b : buchungsVerwaltung.getBuchungen(kundenacc))
					buchungsVerwaltung.delBuchung(b);
	
				break;
			}
	
			case (Account.BETREIBER): {
				// Ist er der letzte Betreiber? Hoffentlich nicht...
				if (getBetreiber().size() < 2)
					throw new LoeschenNichtMoeglichException(
							"You're the last unicorn!" + "\n" + "You can't delete yourself!");
	
				break;
			}
		}

		// Loesche saemtliche mit dem Account verbundenen Nachrichten
		Nachrichtenverwaltung nachrichtenVerwaltung = Portal
				.Nachrichtenverwaltung();

		nachrichtenVerwaltung.delAllNachrichten(acc);

		boolean success = anbieter.remove(acc) || betreiber.remove(acc)
				|| kunden.remove(acc);
		// Ist der Account sicher aus der Liste geloescht?
		if (!success)
			throw new LoeschenNichtMoeglichException(
					"Der Account wurde nicht gefunden!");
	}

	/**
	 * Sucht einen Account ueber seinen Nick-/Unternehmensnamen - CaseInsensitive
	 * 
	 * @param name Username
	 * @return passender Account oder null falls nicht gefunden
	 */
	public Account getAccountByName(String name) {
		for (Account acc : getAccounts())
			if (acc.getName().toLowerCase().equals(name.toLowerCase()))
				return acc;
		return null;
	}

	/**
	 * Sucht einen Account ueber seine E-Mail-Adresse - CaseInsensitive
	 * 
	 * @param email E-Mail Adresse
	 * @return passender Account oder null falls nicht gefunden
	 */
	public Account getAccountByEmail(String email) {
		for (Account acc : getAccounts())
			if (acc.getEmail().toLowerCase().equals(email.toLowerCase()))
				return acc;
		return null;
	}

	/**
	 * Sucht einen Account ueber seine "Identifikation" (= Name oder email, siehe
	 * Pflichtenheft)
	 * 
	 * @param ident Username oder E-Mail Adresse
	 * @return passender Account oder null falls nicht gefunden
	 */
	public Account getAccountByIdentifier(String ident) {
		Account acc1 = getAccountByEmail(ident), acc2 = getAccountByName(ident);
		if (acc1 != null)
			return acc1;
		return acc2;
	}

	/**
	 * Gibt einen qualifizierten Namen zu einer Flag zurueck;
	 * 
	 * @param flag Typflag
	 * @return Typ als String
	 */
	public String convertFlagToName(int flag) {
		switch (flag) {
		case Account.NONE:
			return "Default";
		case Account.ANBIETER:
			return "Anbieter";
		case Account.BETREIBER:
			return "Betreiber";
		case Account.KUNDE:
			return "Kunde";
		default:
			return "Kein Account";
		}
	}

	/**
	 * Ist diese E-Mail Adresse schon vergeben?
	 * 
	 * @param email
	 *            E-Mail Adresse
	 * @return Vergeben oder nicht
	 * 
	 * @pre Emailstring darf nicht null sein
	 */
	public boolean isFreeEmail(String email) {
		assert email != null: "Email ist null";
		
		String emailValid = "[^äöü]+@[a-zA-Z0-9-\\.]+\\.[a-zA-Z]+";
		if(! email.matches(emailValid) && !email.contains(".."))
			throw new IllegalArgumentException("Die gewuenschte E-Mail-Adresse ist von keiner gueltigen Form");
		for (Account a : getAccounts())
			if (a.getEmail().toLowerCase().equals(email.toLowerCase()))
				return false;
		return true;
	}

	/**
	 * Ist dieser Username schon vergeben?
	 * 
	 * @param name Username
	 * @return Vergeben oder nicht
	 * 
	 * @pre Usernamestring darf nicht null sein
	 */
	public boolean isFreeName(String name) {
		assert name != null: "Name ist null";
		
		if(name.length()<2)
			throw new IllegalArgumentException("Bitte waehlen Sie einen Namen mit mehr als 2 Zeichen");
		for (Account a : getAccounts())
			if (a.getName().toLowerCase().equals(name.toLowerCase()))
				return false;
		return true;
	}
	
	/**
	 * Einen Betreiber hinzufuegen
	 * 
	 * @param email Email des neuen Betreibers
	 * @param name Username des neuen Betreibers
	 * @param password Password des neuen Betreibers
	 * @throws Exception Wenn Username < 2 Zeichen 
	 * 					 oder Wenn die E-Mail Adresse nicht valide ist
	 * 
	 * @pre Ein Betreiber muss eingeloggt sein
	 */
	public Betreiber addBetreiber(String email, String name, String password) throws Exception {
		assert loggedIn.getTyp()==Account.BETREIBER :"Es ist kein Betreiber eingeloggt";
		
		if(!isFreeName(name))
			throw new IllegalArgumentException("Bitte waehlen Sie einen Namen mit mehr als 2 Zeichen");
		if(!isFreeEmail(email)) 
			throw new IllegalArgumentException("Die gewuenschte E-Mail-Adresse ist von keiner gueltigen Form");
		return createBetreiber(email,name,password);
	}
	
	/**
	 * Liste neuangemeldeter Anbieter
	 * 
	 * @return ArrayList an neuangemeldeten Anbieter
	 * @throws Exception Sie sind kein Betreiber
	 * 
	 * @pre Ein Betreiber muss eingeloggt sein
	 */
	public ArrayList<Anbieter> getUnbearbeiteteAnbieter() throws Exception {
		assert loggedIn.getTyp()==Account.BETREIBER : "Es ist kein Betreiber eingeloggt";
		
		ArrayList<Anbieter> result = new ArrayList<Anbieter>();
		
		for(Anbieter a:anbieter) {
			if(a.gesperrt == Gesperrt.UNBEARBEITET)
				result.add(a);
		}
		
		return result;
	}
	
	/**
	 * Liste der gesperrten Accounts
	 * 
	 * @return ArrayList an gesperrten Accounts
	 * 
	 * @pre Ein Betreiber muss eingeloggt sein
	 */
	public ArrayList<Account> getGesperrteAccounts() {
		assert loggedIn.getTyp()==Account.BETREIBER : "Es ist kein Betreiber eingeloggt";
		
		ArrayList<Account> result = new ArrayList<Account>();
		
		for(Account a:getAccounts()) {
			if(a.gesperrt == Gesperrt.JA)
				result.add(a);
		}
		
		return result;
	}
	
	
	//-----------------------------------------------------------------------------//
	//	GETTER UND SETTER														   //
	//-----------------------------------------------------------------------------//
	
	/**
	 * Get Anbieterliste
	 * 
	 * @return Anbieterliste
	 */
	public ArrayList<Anbieter> getAnbieter() {
		ArrayList<Anbieter> result = new ArrayList<Anbieter>();
		
		result.addAll(anbieter);
		Collections.reverse(result);
		return result;
	}

	/**
	 * Get Betreiberliste
	 * 
	 * @return Betreiberliste
	 */
	public ArrayList<Betreiber> getBetreiber() {
		ArrayList<Betreiber> result = new ArrayList<Betreiber>();
		
		result.addAll(betreiber);
		Collections.reverse(result);
		return result;
	}

	/**
	 * Get Kundenliste
	 * 
	 * @return Kundenliste
	 */
	public ArrayList<Kunde> getKunden() {
		ArrayList<Kunde> result = new ArrayList<Kunde>();
		
		result.addAll(kunden);
		Collections.reverse(result);
		return result;
	}

	/**
	 * Get Accountliste
	 * 
	 * @return Accountliste
	 */
	public ArrayList<Account> getAccounts() {
		ArrayList<Account> result = new ArrayList<Account>();
		result.addAll(getAnbieter());
		result.addAll(getBetreiber());
		result.addAll(getKunden());
		if(loggedIn.getTyp() == Account.BETREIBER)
			result.remove(loggedIn);
		return result;
	}
}
