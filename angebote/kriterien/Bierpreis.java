package angebote.kriterien;

import java.util.Vector;

public class Bierpreis extends Kriterium {
	
	protected static String[] wertebereich = {"G�nstig","Normal","Teuer","Unbezahlbar"};
	
	public Bierpreis(Vector<String> pwerte) {
		super(pwerte);
	}
	
	public String[] getWertebereich() {
		return wertebereich;
	}
	
}
