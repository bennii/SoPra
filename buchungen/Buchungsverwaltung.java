package buchungen;

import java.util.*;
import accounts.Kunde;
import angebote.typen.Angebot;

/**
 * @author Benjamin
 */
public class Buchungsverwaltung {
	/**
	 * Erstellt eine Buchung und weist sie einem Kunden zu.
	 * 
	 * @param kunde			Dem Kunden wird die Buchung zugewiesen.
	 * @param angebot		Gebuchtes Angebot.
	 * @param von			Start (Datum).
	 * @param bis			Ende (Datum).
	 * @throws InvalidDateException 
	 */
	public void createBuchung(Kunde kunde, Angebot angebot, Date von, Date bis) throws InvalidDateException {
		Buchung buchung = new Buchung(angebot, kunde);
		
		if (bis.after(von))
			throw new InvalidDateException();
		
		buchung.setBis(bis);
		buchung.setVon(von);
		
		kunde.addBuchung(buchung);
	}
	
	/**
	 * Cancelt eine Buchung (im Fall von stornieren etc. relevant)
	 * 
	 * @param buchung 		zu beartbeitende Buchung.
	 */
	public void cancelBuchung(Buchung buchung) {
		buchung.setBestaetigt(Bestaetigung.NEIN);
	}
	
	/**
	 * Gibt alle Buchungen eines Kunden aus.
	 * 
	 * @param kunde			DU KUNDE JUNGE!!!!!
	 * @return				Liste seiner Buchungen.
	 */
	public Buchung[] getBuchungen(Kunde kunde) {
		Buchung[] buchungen = (Buchung[])kunde.getBuchungen().toArray();
		
		return buchungen;
	}
	
	/**
	 * Setter.
	 * 
	 * @param buchung		zu best�tigenede Buchung.
	 * @param bestaetigt	Art der Best�tigung.
	 */
	public void setBestaetigt(Buchung buchung, Bestaetigung bestaetigt) {
		buchung.setBestaetigt(bestaetigt);
	}
	
	/**
	 * Getter.
	 * 
	 * @param buchung		zu best�tigenede Buchung.
	 * @return				Art der Bestaetigung wird ausgegeben.
	 */
	public Bestaetigung getBestaetigt(Buchung buchung) {
		return buchung.getBestaetigt();
	}
}
