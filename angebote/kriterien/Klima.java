package angebote.kriterien;

import java.util.Vector;

public class Klima extends Kriterium{
	
	String[] klimata = {"Tropisch","Mediteran","Gem��igt","Kalt"};
	
	Klima(Vector<String> pwerte) {
		super(pwerte);
		
		erlaubteWerte = klimata;
	}
	
	public boolean isValid() {
		return true;
	}
}