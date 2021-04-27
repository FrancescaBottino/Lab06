package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			rs.close();
			st.close();
			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, Citta citta) {
		
		final String sql = "SELECT localita, data, umidita FROM situazione " +
				   "WHERE localita=? AND MONTH(data)=?  ORDER BY data ASC";
		
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

			try {
				Connection conn = ConnectDB.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				
				st.setString(1, citta.getNome());
				//st.setString(2, mese.getValue()); se fosse un oggetto month
				st.setString(2, Integer.toString(mese)); 
			
				ResultSet rs = st.executeQuery();
				
				while (rs.next()) {

					Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
					rilevamenti.add(r);
				}
				
			
				conn.close();
				return rilevamenti;
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		

		
	}
	
	public String getUmiditaMediaPerMese(int mese) {
		
		String sql="SELECT localita, AVG(Umidita) as media "
				+ " FROM situazione "
				+ " WHERE MONTH(data)=? "
				+ " GROUP BY localita ";
		
		
		String s="";
		
		try {
			
			Connection conn=ConnectDB.getConnection();
			PreparedStatement st=conn.prepareStatement(sql);
			
			st.setInt(1, mese);
			
			ResultSet rs= st.executeQuery();
			
			while(rs.next()) {
				
				s=s+rs.getString("Localita")+"  "+rs.getDouble("media")+"\n";
			}
			
			rs.close();
			st.close();
			conn.close();
			return s;
			
			
		}catch(SQLException e) {
			
			e.printStackTrace();
			
			throw new RuntimeException(e);
			
		}
		
		
		
	}
	
	public List<Citta> getCitta(){
		
		String sql= "SELECT localita FROM situazione GROUP BY localita ";
		
		List<Citta> result= new ArrayList<Citta>();
		
		try {
			

			Connection conn=ConnectDB.getConnection();
			PreparedStatement st=conn.prepareStatement(sql);
			ResultSet rs= st.executeQuery();
			
			while(rs.next()) {
				Citta c= new Citta(rs.getString("localita"));
				result.add(c);
				
			}
			rs.close();
			st.close();
			conn.close();
			
		}catch(SQLException e) {
			
			e.printStackTrace();
			
			throw new RuntimeException(e);
			
		}
		
		return result;
	}


}
