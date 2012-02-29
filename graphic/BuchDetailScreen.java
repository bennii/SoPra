package graphic;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import buchungen.Buchung;

public class BuchDetailScreen extends JPanel {
	private JScrollPane dscroll;
	private JPanel dPanel;
	private JPanel up;
	private JPanel mid;
	private JPanel down;
	private JLabel name;
	private JLabel typ;
	private JLabel datum;
	private JLabel anbieter;
	
	private JLabel fullinfo;
	
	private JButton aenderungsanfrage;
	private JButton stornieren;
	
	public BuchDetailScreen(Mainframe m,Buchung b){
		dscroll = new JScrollPane();
		dPanel = new JPanel(new BorderLayout(5,5));
		up = new JPanel(new GridLayout(4,0));
		mid = new JPanel(new GridLayout(1,0));
		down = new JPanel(new BorderLayout(5,5));
		
		name = new JLabel(b.getIdetifier());
		typ = new JLabel (""+b.getAngebot().getTyp());		//GUCKEN BITTE
		datum = new JLabel(b.getVon().toString()+ " - "+b.getBis().toString());	// DATE
		anbieter = new JLabel(); // edit  wenn implementiert
		
		up.add(name);
		up.add(typ);
		up.add(datum);
		up.add(anbieter);
		
		fullinfo = new JLabel(b.getAngebot().getFullInfo());
		mid.add(fullinfo);
		
		aenderungsanfrage = new JButton("Aenderungsanfrage");
		stornieren = new JButton("Stronieren");
		down.add(BorderLayout.EAST, aenderungsanfrage);
		down.add(BorderLayout.WEST, stornieren);
		
		dPanel.add(BorderLayout.NORTH, up);
		dPanel.add(BorderLayout.CENTER, mid);
		dPanel.add(BorderLayout.SOUTH,down);
		
		dscroll.add(dPanel);
		add(dscroll);
		setVisible(true);

	}
}
