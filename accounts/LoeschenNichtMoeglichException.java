package accounts;

/**
 * LoeschenNichtMoeglichException wird ausgel�st, wenn eine Account nicht gel�scht werden kann.
 * 
 * @author osca
 */
public class LoeschenNichtMoeglichException extends Exception {

	private static final long serialVersionUID = 5648913248746578922L;

	public LoeschenNichtMoeglichException(String s){
		super(s);
	}
}
