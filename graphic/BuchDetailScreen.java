package graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.Portal;

import buchungen.Buchung;

public class BuchDetailScreen extends JPanel {
	private JScrollPane dscroll;
	private JPanel dPanel;
	private JPanel up;
	private JPanel sub_a;
	private JPanel sub_b;
	private JPanel mid;
	private JPanel down;
	private JLabel name;
	private JLabel typ;
	private JLabel vondatum;
	private JLabel bisdatum;
	private JLabel anbieter;



	private JLabel fullinfo;
	
	private JButton aenderungsanfrage;
	private JButton stornieren;
	
	public BuchDetailScreen(final Buchung b){
		dscroll = new JScrollPane();
		dPanel = new JPanel(new BorderLayout(5,5));
		up = new JPanel(new GridLayout(0,2));
		mid = new JPanel(new GridLayout(1,0));
		down = new JPanel(new BorderLayout(5,5));
		
		//TODO nachher nochmal die Labels vor den labels^^^
		name = new JLabel(Portal.Buchungsverwaltung().getReferringAngebot(b).getName());
		typ = new JLabel (""+Portal.Buchungsverwaltung().getReferringAngebot(b).getTyp());		//GUCKEN BITTE
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		vondatum = new JLabel(formatter.format(b.getVon()));	// DATE
		bisdatum = new JLabel(formatter.format(b.getBis()));
		anbieter = new JLabel(Portal.Buchungsverwaltung().getReferringAngebot(b).getAnbieterName()); 
		
		sub_a = new JPanel(new GridLayout(6,0));
		sub_a.add(name);
		sub_a.add(typ);
		sub_a.add(vondatum);
		sub_a.add(bisdatum);
		sub_a.add(anbieter);
		
		sub_b = new JPanel(new GridLayout(6,0));
		String k[] = Portal.Buchungsverwaltung().getReferringAngebot(b).getErlaubteKriterien(); 
		for (int i =0;i<k.length;i++){
			JLabel krit = new JLabel(k[i]);
			sub_b.add(krit);
		}
		up.add(sub_a);
		up.add(sub_b);
		
		fullinfo = new JLabel(Portal.Buchungsverwaltung().getReferringAngebot(b).getFullInfo());
		mid.add(fullinfo);
		
		aenderungsanfrage = new JButton("Aenderungsanfrage");
		aenderungsanfrage.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				JTextField betreff = new JTextField("Betreff");
				JTextArea area = new JTextArea("");
				int result = JOptionPane.showConfirmDialog(null,new Object[]{betreff,area},"Login",JOptionPane.OK_CANCEL_OPTION);
				if(result == JOptionPane.OK_OPTION){
					Portal.Nachrichtenverwaltung().sendeNachricht(Portal.Accountverwaltung().getLoggedIn(), 
							Portal.Angebotsverwaltung().getAnbieter(Portal.Buchungsverwaltung().getReferringAngebot(b)), betreff.getText(), area.getText(), 
							Portal.Buchungsverwaltung().getReferringAngebot(b));
					JOptionPane.showConfirmDialog(null, "Ihre Aenderungsanfrage wurde gesendet");
				}
			}
		});
		stornieren = new JButton("Stronieren");
		stornieren.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int confirm = JOptionPane.showConfirmDialog(null, "Wollen Sie wirklich eine Stornierungsanfrage senden?");
				if(confirm == JOptionPane.OK_OPTION){
					Portal.Nachrichtenverwaltung().sendeNachricht(Portal.Accountverwaltung().getLoggedIn(), 
							Portal.Angebotsverwaltung().getAnbieter(Portal.Buchungsverwaltung().getReferringAngebot(b)),
							"Stornierunsganfrage", "Der Kunde moechte seine Buchung stornieren", 
							Portal.Buchungsverwaltung().getReferringAngebot(b));
					JOptionPane.showConfirmDialog(null, "Ihre Stornierungsanfrage wurde gesendet");
				}
			}
		});
		down.add(BorderLayout.EAST, aenderungsanfrage);
		down.add(BorderLayout.WEST, stornieren);
		
		dPanel.add(BorderLayout.NORTH, up);
		dPanel.add(BorderLayout.CENTER, mid);
		dPanel.add(BorderLayout.SOUTH,down);
		
		this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.LIGHT_GRAY));

		add(dPanel);
	}
}
