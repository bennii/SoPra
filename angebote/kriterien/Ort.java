//TODO wertebereich fehlt

package angebote.kriterien;

/**
 * Ort - Erbendes Kriterium
 * 
 * @author stephan
 */
public class Ort extends Kriterium{
	
	public final static String name = "Ort";

	/**
	 * Konstruktor
	 * 
	 * @param pwert Ort
	 * @throws IllegalArgumentException Ist die Eingabe valide?
	 */
	public Ort(String pwert) throws IllegalArgumentException {
		super(pwert);
	}
	
}
