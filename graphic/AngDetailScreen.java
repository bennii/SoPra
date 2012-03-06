package graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolTip;
import javax.swing.text.MaskFormatter;

import main.Portal;
import accounts.Account;
import accounts.Anbieter;
import accounts.Kunde;
import accounts.LoeschenNichtMoeglichException;
import angebote.Kommentar;
import angebote.kriterien.Kriterium;
import angebote.typen.Angebot;
import buchungen.InvalidDateException;


public class AngDetailScreen extends JPanel{
	private JPanel up;
	private JPanel sub_a;
	private JPanel sub_b;
	private JPanel mid;
	private JPanel down;
	private JLabel name;
	private JLabel typ;
	private JLabel vondatum;
	private JLabel bisdatum;
	private JLabel anbieterl;
	
	private JTextArea fullinfo;
	private JLabel nullAcc;
	
	private JButton buchen = new JButton("Buchen");
	private JButton melden = new JButton("Melden");
	private JButton kommentieren = new JButton("Kommentieren");
	private JButton loeschen = new JButton("Loeschen");
	private JButton editieren = new JButton("Editieren");
	private JButton kontaktieren = new JButton("Kontaktieren");
	
	final Angebot angebot;
	final Anbieter anbieter;
	
	public AngDetailScreen(Angebot a){
		
		angebot = a;
		anbieter = Portal.Angebotsverwaltung().getAnbieter(angebot);
		
		this.setLayout(new BorderLayout());
		up = new JPanel(new GridLayout(0,2));
		mid = new JPanel(new GridLayout(0,1));
		down = new JPanel(new GridLayout(1,0));
		
		name = new JLabel(angebot.getName());
		typ = new JLabel (""+Angebot.typToString(angebot.getTyp()));
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		vondatum = new JLabel(formatter.format(angebot.getStartdatum()));
		bisdatum = new JLabel(formatter.format(angebot.getEnddatum()));
		anbieterl = new JLabel(); 
		
		sub_a = new JPanel(new GridLayout(6,2));
		JLabel name_l= new JLabel("Name:");
		sub_a.add(name_l);
		sub_a.add(name);
		JLabel typ_label= new JLabel("Typ:");
		sub_a.add(typ_label);
		sub_a.add(typ);
		JLabel vd_label= new JLabel("Startdatum:");
		sub_a.add(vd_label);
		sub_a.add(vondatum);
		JLabel bd_label= new JLabel("Enddatum:");
		sub_a.add(bd_label);
		sub_a.add(bisdatum);
		JLabel anbieter_label= new JLabel("Anbieter:");
		sub_a.add(anbieterl);
		sub_b = new JPanel(new GridLayout(0,2));
		JPanel sub_1=new JPanel(new GridLayout(6,1));
		JPanel sub_2= new JPanel(new GridLayout(6,1));
		//String k[] =angebot.getErlaubteKriterien();
		ArrayList<Kriterium> w=angebot.getKriterien();
		for (int i =0;i<w.size();i++){
			JLabel krit = new JLabel(w.get(i).getName());
			sub_1.add(krit);
			JLabel krit1 = new JLabel(w.get(i).getWert());
			sub_2.add(krit1);
			
		}
		sub_b.add(sub_1);
		sub_b.add(sub_2);
		up.add(sub_a);
		up.add(sub_b);
		
		fullinfo= new JTextArea(angebot.getFullInfo());
		fullinfo.setLineWrap(true);
		fullinfo.setWrapStyleWord(true);
		fullinfo.setBackground(Color.LIGHT_GRAY);
		fullinfo.setEditable(false);
		
		mid.add(fullinfo);
		
		/*
		for(Kommentar k : angebot.getKommentare()) {
			mid.add(new KommentarScreen(k));
		}*/
		
		///////////////////
		
		switch (Portal.Accountverwaltung().getLoggedIn().getTyp()){
		case Account.NONE:
			nullAcc = new JLabel(MeldeDienst.MSG_LOGIN_FEHLT);
			down.add(BorderLayout.CENTER, nullAcc);
			break;
		case Account.KUNDE :
			kommentieren.setEnabled(true);
			down.add(kommentieren);
			down.add(buchen);
			down.add(melden);
			down.add(kontaktieren);
			break;
		
		case Account.ANBIETER:
			if(Portal.Accountverwaltung().getLoggedIn().getName().equals(angebot.getAnbieterName()))
				down.add(loeschen);
			down.add(editieren);
			break;
		
		case Account.BETREIBER:
			down.add(loeschen);
			down.add(kontaktieren);
			break;
		
		}
		
		this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.LIGHT_GRAY));
		
		this.add(BorderLayout.NORTH, up);
		this.add(BorderLayout.CENTER, mid);
		this.add(BorderLayout.SOUTH, down);
		
		/////////////////
		
		
		buchen.addActionListener(new ActionListener()
		{			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					JLabel label = new JLabel("Geben Sie den Zeitraum an:");
					JLabel fromLabel = new JLabel("Von:");
					JLabel toLabel = new JLabel("Bis:");
					JFormattedTextField fromField = new JFormattedTextField(new MaskFormatter("##/##/####"));
					JFormattedTextField toField = new JFormattedTextField(new MaskFormatter("##/##/####"));
					
					if(JOptionPane.showConfirmDialog(null, new Object[]{label, fromLabel, fromField, toLabel, toField}, "Login", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
					{
						if (fromField.getText().length() == 0 || toField.getText().length() == 0) throw new IllegalArgumentException();
						
						SimpleDateFormat to = new SimpleDateFormat("dd/MM/yyyy");

						final Date toDate = to.parse(toField.getText());
						final Date fromDate = to.parse(fromField.getText());
						Date heute = new Date();
						Calendar cal = new GregorianCalendar();
						cal.setTime(heute);
						cal.set(Calendar.HOUR_OF_DAY, 0);
						cal.set(Calendar.MINUTE, 0);
						cal.set(Calendar.SECOND, 0);
						cal.set(Calendar.MILLISECOND, 0);
						heute = cal.getTime();
						
						/*if(fromDate.before(heute) || fromDate.after(toDate) || fromDate.before(angebot.getStartdatum()) || toDate.after(angebot.getEnddatum())){
							throw new InvalidDateException("Das Date ist falsch");
						}*/
						
						if (fromDate.before(heute) || toDate.before(heute)) {
							throw new InvalidDateException("Ihr Anfangs- oder Enddatum liegt vor heute.");
						}
						else if (fromDate.after(toDate)) {
							throw new InvalidDateException("Ihr Enddatum liegt vor dem Startdatum");
						}
						else if (fromDate.before(angebot.getStartdatum())) {
							throw new InvalidDateException("Ihr Anfangsdatum liegt vor Beginn des Angebots");
						}
						else if (toDate.after(angebot.getEnddatum())) {
							throw new InvalidDateException("Ihr Enddatum liegt nach Ende des Angebots");
						}

						
						DialogScreen dialog = new DialogScreen("Buchen", DialogScreen.OK_CANCEL_OPTION)
						{
							@Override
							public void onOK()
							{
								try 
								{
									// edit benjamin: angebot.getStartdatum() durch fromDate ersetzt etc.
									Portal.Buchungsverwaltung().createBuchung((Kunde) Portal.Accountverwaltung().getLoggedIn(), angebot, fromDate, toDate); 
								}
								catch (Exception e) 
								{
									e.printStackTrace();
								}
							}
						};
						dialog.setEditable(false);
						dialog.addOnPanel(new JLabel(MeldeDienst.MSG_AGB_ERKLAERUNG + anbieterl.getText()), DialogScreen.LABEL_LEFT);
						dialog.addOnPanel(new JLabel(MeldeDienst.MSG_GESAMMT_BEWERUNG + anbieter.getWertung()), DialogScreen.LABEL_RIGHT);
						dialog.setContent(anbieter.getAgb());
					}
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(up.getParent(), e.getMessage());
				}
			}
		});
		melden.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				try
				{
					if(JOptionPane.showConfirmDialog(up.getParent(), MeldeDienst.QSN_ANGEBOT_MELDEN, "Angebot melden", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
					{ 
						Account account = Portal.Accountverwaltung().getLoggedIn();
						Portal.Nachrichtenverwaltung().sendeMeldungAnAlleBetreiber(account, MeldeDienst.MSG_BESCHWERDE, MeldeDienst.MSG_ANGEBOT_GEMELDET+"\n"+"Anbieter: "+anbieter.getName()+"\n"+"Kunde: "+account.getName()+"\n"+"Angebot: "+angebot.getName(),angebot);
						Portal.Nachrichtenverwaltung().sendeNachricht(account, anbieter, MeldeDienst.MSG_BESCHWERDE, MeldeDienst.MSG_ANGEBOT_GEMELDET+"\n"+"Anbieter: "+anbieter.getName()+"\n"+"Kunde: "+account.getName()+"\n"+"Angebot: "+angebot.getName(), angebot);
					}
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(up.getParent(), e.toString());
				}
			}
		});
		kommentieren.addActionListener(new ActionListener()
		{
			int bewertung = 0;
			JComboBox bewertungCombo = new JComboBox(new String[]{"Auswahl", "1", "2", "3", "4", "5"});
			
			JLabel kundeLabel = new JLabel();
			JLabel bewertungLabel = new JLabel("Bewertung:");
			
			JButton okButton = new JButton("Abschicken");
			JButton cancelButton = new JButton("Abbrechen");
			
			DialogScreen dialog;
			Kommentar kommi;
			
			ActionListener okListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (dialog.getContent().length() <= 0)
						JOptionPane.showMessageDialog(dialog, "Sie m�ssen einen Text eingeben");
					else if (bewertungCombo.getSelectedIndex() == 0)
						JOptionPane.showMessageDialog(dialog, "Sie m�ssen eine Bewertung abgeben");
					else {
						bewertung = bewertungCombo.getSelectedIndex();
						
						kommi = new Kommentar(Portal.Accountverwaltung().getLoggedIn().getName(), dialog.getContent(), bewertung);
						Portal.Angebotsverwaltung().addKommentar(angebot, kommi);
						dialog.dispose();
					}
				}
			};
			
			ActionListener cancelListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dialog.dispose();
				}
			};
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Kunde loggedin = (Kunde)Portal.Accountverwaltung().getLoggedIn();
				
				okButton.addActionListener(okListener);
				cancelButton.addActionListener(cancelListener);
				kundeLabel.setText(Portal.Accountverwaltung().getLoggedIn().getName());
				
				dialog = new DialogScreen("Kommentieren", new JButton[]{okButton, cancelButton});
				
				dialog.addOnPanel(kundeLabel, DialogScreen.LABEL_LEFT);
				
				// ein Kunde darf nur bewerten, wenn er die Reise gebucht hat und noch keine bewertung abgegeben hat.
				if (Portal.Buchungsverwaltung().isBookedByKunde(angebot, loggedin) &&
					!Portal.Angebotsverwaltung().isCommentedByKunde(angebot, loggedin)) {
					bewertungCombo.setToolTipText("Je h�her, desto besser.");
					dialog.addOnPanel(bewertungLabel, DialogScreen.LABEL_RIGHT);
					dialog.addOnPanel(bewertungCombo, DialogScreen.LABEL_RIGHT);
				}
			};
		});
		
		loeschen.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				try 
				{
					Portal.Angebotsverwaltung().delAngebot(angebot);
					JOptionPane.showMessageDialog(up.getParent(), "Angebot erfolgreich geloescht");
					if(Portal.Accountverwaltung().getLoggedIn().getTyp() == Account.BETREIBER)
						Portal.Nachrichtenverwaltung().sendeNachricht(Portal.Accountverwaltung().getLoggedIn(), anbieter, "Angebot wurde Gel�scht", "Ihr Angebot wurde vom Betreiber gel�scht!", angebot);
					removeAll();
					repaint();
				} 
				catch (LoeschenNichtMoeglichException e) 
				{
					JOptionPane.showConfirmDialog(up.getParent(), "Loeschen nicht moeglich");
					e.printStackTrace();
				}
			}
		});
		editieren.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				
			}
		});
		kontaktieren.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				try
				{
					DialogScreen dialog = new DialogScreen("Kontaktieren", DialogScreen.OK_CANCEL_OPTION)
					{
						@Override
						public void onOK()
						{
							Portal.Nachrichtenverwaltung().sendeNachricht(Portal.Accountverwaltung().getLoggedIn(), anbieter, "Kontaktaufnahme",getContent(), angebot);
						}
					};
					dialog.addOnPanel(new JLabel(Portal.Accountverwaltung().getLoggedIn().getName()), DialogScreen.LABEL_LEFT);
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(up.getParent(), e.toString());
				}
			}
		});
	}
}
