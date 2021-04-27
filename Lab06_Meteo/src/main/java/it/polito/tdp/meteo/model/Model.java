package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private MeteoDAO meteoDao;
	private List<Citta> soluzioneMigliore;
	private double costoMinore;
	private List<Citta> leCitta;


	public Model() {
		
		meteoDao= new MeteoDAO();	
		this.leCitta=meteoDao.getCitta();
	
	}
	
	
	public List<Citta> getCitta(){
		return leCitta;
	}
	
	public String getUmiditaMediaPerMese(int mese) {
		
		return meteoDao.getUmiditaMediaPerMese(mese);
	}
	
	//procedura ricorsiva 
	
	public List<Citta> trovaSequenza(int mese) {
		
		List<Citta> parziale= new ArrayList<Citta>(); //vuota per ora
		
		this.soluzioneMigliore=new ArrayList<Citta>(); //vuota
		this.costoMinore=0;
		
		for(Citta c: leCitta)
			c.setRilevamenti(meteoDao.getAllRilevamentiLocalitaMese(mese, c));
		
		
		cerca(parziale, 0);
		
		return soluzioneMigliore;
	}
	
	public void cerca(List<Citta> parziale, int livello) {
		
		//1) caso terminale
		
		if(livello == NUMERO_GIORNI_TOTALI) {
		
			double costo=calcoloCosto(parziale);
		
			if(costo<costoMinore || soluzioneMigliore.size()==0) {
				soluzioneMigliore= new ArrayList<Citta>(parziale);
				costoMinore=costo;
				return;
			}
		}
		
		//2) generare sotto problemi
		
		//controllo se città da aggiungere è valida
		
		for(Citta c: this.getCitta()) {
		
			if(cittaValida(c, parziale)) {
			
				parziale.add(c);
				cerca(parziale, livello+1);
				parziale.remove(parziale.size()-1);
			
			}
		}
		
	}

	
	private boolean cittaValida(Citta c, List<Citta> parziale) {
		
		//controlli sui giorni consecutivi
		
		int conta=0; 
					
		//calcolo quante ce ne sono già della città che devo inserire
		
		for(Citta cc: parziale) {
			if(cc.equals(c))
				conta++;
				
		}
		
		if(conta >= NUMERO_GIORNI_CITTA_MAX)
			return false;
		
		if(parziale.size()==0)
			return true;
		
		
		if(parziale.size()<NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) { //se è 1 o 2
			
			return parziale.get(parziale.size()-1).equals(c); //guardo ultimo elemento della lista inserito, se sono uguali va bene inserirla (true)
		
		}
		
		//caso in cui non voglio cambiare citta dopo le 3 inserite
		if(parziale.get(parziale.size()-1).equals(c)) 
			return true;
		
		
		// se cambio città mi devo assicurare che nei tre giorni precedenti sono rimasto fermo 
		if (parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) && parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3)))
			return true;
					
		
		return false;
	}



	private double calcoloCosto(List<Citta> parziale) {
		
		double costo=0.0;	
	
		for(int gg=1; gg<=NUMERO_GIORNI_TOTALI; gg++) {
			
			Citta c=parziale.get(gg-1);
			double umid=c.getRilevamenti().get(gg-1).getUmidita();
			costo+=umid;
			
			
		}
		
		for(int gg=2; gg<=NUMERO_GIORNI_TOTALI; gg++) {
			
			if(!parziale.get(gg-1).equals(parziale.get(gg-2)))
				costo+=COST;
		}
		
		
		
		return costo;
	}
	
	
	

}
