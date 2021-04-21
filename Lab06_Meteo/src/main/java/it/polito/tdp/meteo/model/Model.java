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
	


	public Model() {
		
		meteoDao= new MeteoDAO();	
		

	}
	
	
	
	public List<Integer> getMesi(){
		
		List<Integer> mesi= new ArrayList<Integer>();
		
		for(int i=1; i<13; i++)
			mesi.add(i);
		
		return mesi;
		
	}
	
	public List<Citta> getCitta(){
		return meteoDao.getCitta();
	}
	
	public String getUmiditaMediaPerMese(int mese) {
		return meteoDao.getUmiditaMediaPerMese(mese);
	}
	
	//procedura ricorsiva 
	
	public List<Citta> trovaSequenza(int mese) {
		
		List<Citta> parziale= new ArrayList<Citta>(); //vuota per ora
		
		this.soluzioneMigliore=new ArrayList<Citta>(); //vuota
		this.costoMinore=0;
		
		
		cerca(parziale, 0);
		
		return soluzioneMigliore;
	}
	
	public void cerca(List<Citta> parziale, int livello) {
		
		//1) caso terminale
		
		if(livello == NUMERO_GIORNI_TOTALI) {
		
			double costo=calcoloCosto(parziale);
		
			if(costo<costoMinore) {
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
		
		if(parziale.size()==0)
			return true;
		
		if(parziale.size() == NUMERO_GIORNI_TOTALI)
			return false;
					
					
		for(Citta cc: parziale) {
			
			
		}
		
		
		
		return false;
	}



	private double calcoloCosto(List<Citta> parziale) {
		
		double costo=0.0;	
	
		for(int i=0; i<parziale.size(); i++) {
			
			if(parziale.get(i).getNome() != parziale.get(i+1).getNome())
				costo+=COST;
			
			
			costo+=parziale.get(i).getRilevamenti().get(i).getUmidita();
			
				
			
		}
		
		return costo;
	}
	
	
	

}
