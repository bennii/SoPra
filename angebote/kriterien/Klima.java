package angebote.kriterien;

import java.util.Vector;

public class Klima extends Kriterium {
	
	protected static String[] wertebereich = {"Tropisch","Mediteran","Gem��igt","Kalt"};
	
	public Klima(Vector<String> pwerte) {
		super(pwerte);
	}
	
	public String[] getWertebereich() {
		return wertebereich;
	}
}