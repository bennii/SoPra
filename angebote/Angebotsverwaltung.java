package angebote;

import main.Portal;
import accounts.Anbieter;
import angebote.kriterien.Kriterium;
import angebote.typen.*;

/**
 * @author Benjamin
 */
public class Angebotsverwaltung {
	/**
	 * 
	 * @param anbieter			Dem Anbieter wird das Angebot zugeordnet.
	 * @param name				Name, den das Angebot haben wird.
	 * @param typ				Typ, entweder: Ausflug, Autovermietung, Hotel�bernachtung, Ausflug
	 * @param preis				Preis des Angebots.
	 * @param werte				erlaubte Werte f�r die Suche
	 * @param kriterien			Kriterien.
	 */
	public void createAngebot(Anbieter anbieter, String name, int typ, double preis, String[] werte, Kriterium[] kriterien) {
		Angebot offer = null;
		
		// kriterium array.. was da los? nicht n�tig = doppelt, wegen werte?
		// werte array.. was da los?
		
		//TODO: Angebote haben beschreibungen, createAngebot hat aber kein "Beschreibung"-Parameter
		// 		
		switch(typ) {
		case Angebot.AUTOVERMIETUNG:
			offer = new Autovermietung(name, null, 10, preis, null, werte[0], werte[1]);					// 10 isn troll-wert..ich geh jetzt einfach mal davon aus das die werte in der reihenfolge stehen -_-
		case Angebot.AUSFLUG:
			offer = new Ausfluege(name, null, 10, preis, null, null, null, null);
		case Angebot.HOTEL:
			offer = new Hoteluebernachtung(name, null, 10, preis, null, null, null, null, null, null, null);		// hauptsache alles ist null!
		case Angebot.FLUG:
			offer = new Flug(name, null, 10, preis, null, null, null, null, null, null);
		}
		
		anbieter.addAngebot(offer);
	}
	
	/**
	 * L�scht ein Angebot. Von wem oder was?
	 * 
	 * @param angebot			das zu l�schende Angebot
	 */
	public void delAngebot(Angebot angebot) {		
		int nAnbieter = Portal.getSingletonObject().getAccountverwaltung().getAnbieter().size();
		Anbieter[] anbieter = (Anbieter[])Portal.getSingletonObject().getAccountverwaltung().getAnbieter().toArray();
		
		for(int i = 0; i < nAnbieter; i++) {
			if (anbieter[i].getAngebote().contains(angebot)) anbieter[i].delAngebot(angebot);
		}
		// das l�schen in den Dateien �bernimmt XStream durch das Streamen der Entitit�tsklassen
	}
	
	/**
	 * Editiert ein Angebot. Von wem oder was?
	 * 
	 * @param angebot			?
	 */
	public void editAngebot(Angebot angebot) {
		// TODO: angebot suchen (ist �bergebene Angebot 100% anders als das Ursprungsangebot????)
		//		 neues Angebot erstellen und durch edited ersetzen => .replace(...)?
	}
}
