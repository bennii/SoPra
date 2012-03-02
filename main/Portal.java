package main;
import accounts.Accountverwaltung;
import accounts.Nachrichtenverwaltung;
import angebote.Angebotsverwaltung;
import buchungen.Buchungsverwaltung;
import angebote.Angebotsverarbeitung;


public class Portal {

	private Accountverwaltung accverw;
	private Angebotsverwaltung angebverw;
	private Buchungsverwaltung buchverw;
	private Nachrichtenverwaltung nachrverw;
	private Angebotsverarbeitung angebverar;
	private static Portal single = new Portal();
	
	private Portal(){
		nachrverw = new Nachrichtenverwaltung();
		accverw = new Accountverwaltung();
		angebverw = new Angebotsverwaltung();
		buchverw = new Buchungsverwaltung();
		angebverar = new Angebotsverarbeitung();
	}
	
	/** Gibt das Portalobjekt zurück
	 * @return PortalSingleton
	 */
	public static Portal getSingletonObject(){
		return single;
	}
	
	/** gibt das Accountverwaltungsobjekt zurück
	 * @return Accountverwaltungsobjekt
	 */
	public Accountverwaltung getAccountverwaltung(){
		return accverw;
	}
	
	/**
	 * @return Angebotsverwaltungsobjekt
	 */
	public Angebotsverwaltung getAngebotsverwaltung() {
		return angebverw;
	}

	/**
	 * @return Buchungsverwaltungsobjekt
	 */
	public Buchungsverwaltung getBuchungsverwaltung() {
		return buchverw;
	}

	/**
	 * @return Nachrichtenverwaltungsobjekt
	 */
	public Nachrichtenverwaltung getNachrichtenverwaltung() {
		return nachrverw;
	}

	/**
	 * @return Angebotsverarbeitungsobjekt
	 */
	public Angebotsverarbeitung getAngebotsverarbeitung() {
		return angebverar;
	}
}
