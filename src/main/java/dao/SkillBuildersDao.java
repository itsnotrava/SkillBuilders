package dao;

import exception.UtenteNonEsistente;
import exception.UtenteGiaEsistente;
import factory.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SkillBuildersDao {
	private final Connection con;

	public SkillBuildersDao() throws SQLException {
		this.con = ConnectionFactory.createConnection("mysql");
	}

	// INSERISCO UN NUOVO UTENTE
	public void insertUtente(String nome, String password, String email, int anno, String foto, String indirizzo, String comune, boolean flagTutor) throws SQLException, UtenteGiaEsistente {
		// CONTROLLO CHE LA EMAIL NON ESISTA GIA
		/*
		if(checkUtente(email)){
			throw new UtenteGiaEsistente();
		}
		*/

		try{
			checkUtente(email);
			throw new UtenteGiaEsistente(); // UTENTE ESISTE
		} catch (UtenteNonEsistente e) { // NON CONTIENE NULLA, QUINDI VA BENE
			String sql = "INSERT INTO Utenti (email, nome, password, anno, indirizzo, foto, comune, flagTutor) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = this.con.prepareStatement(sql);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, nome);
			preparedStatement.setString(3, password);
			preparedStatement.setInt(4, anno);
			preparedStatement.setString(5, indirizzo);
			preparedStatement.setString(6, foto);
			preparedStatement.setString(7, comune);
			preparedStatement.setBoolean(8, flagTutor);

			preparedStatement.execute();
		}
	}

	// CONTROLLO SE UN UTENTE ESISTE
	public void checkUtente(String email) throws SQLException, UtenteNonEsistente {
		String sql = "SELECT * FROM utente WHERE email=?";
		PreparedStatement preparedStatement = this.con.prepareStatement(sql);
		preparedStatement.setString(1, email);

		ResultSet resultSet = preparedStatement.executeQuery();
		if(resultSet.isClosed()) { // SE NON CONTIENE NULLA
			throw new UtenteNonEsistente();
		}
	}

}


