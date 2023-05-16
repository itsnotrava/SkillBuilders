package dao;

import exceptions.UtenteNonEsistente;
import exceptions.UtenteGiàEsistente;
import exceptions.EmailOPasswordErrati;
import factory.ConnectionFactory;
import model.Utente;

import java.sql.*;
import java.util.ArrayList;


public class SkillBuildersDao {
	private final Connection con;

	public SkillBuildersDao() throws SQLException {
		this.con = ConnectionFactory.createConnection("mysql");
	}

	public void insertUtente(String nome, String password, String email, int anno, String indirizzo, String foto, String comune, boolean flagTutor) throws SQLException, UtenteGiàEsistente {
		try {
			/*
		    * FIXME: funziona tutto, ma avendo introdotto la verifica della mail questo controllo viene fatto a priori
		    *   da ServletVerificaEmail --> è ridondante il controllo;
			*/
			checkUtenteEsistente(email);
			throw new UtenteGiàEsistente();
		} catch (UtenteNonEsistente e) {
			String sql = "INSERT INTO utente (email, nome, password, anno, indirizzo, nome_foto, comune, flag_tutor) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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

	public ArrayList<String> getTutors(int anno, String comune, String indirizzo) throws SQLException {
		ArrayList<String> result = new ArrayList<>();

		String sql = "SELECT email FROM utente WHERE flag_tutor=1 AND (anno=? OR ?=0) AND (comune=? OR ?='') AND (indirizzo=? OR ?='')";
		PreparedStatement preparedStatement = this.con.prepareStatement(sql);
		preparedStatement.setInt(1, anno);
		preparedStatement.setInt(2, anno);
		preparedStatement.setString(3, comune);
		preparedStatement.setString(4, comune);
		preparedStatement.setString(5, indirizzo);
		preparedStatement.setString(6, indirizzo);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			String emailUtente = resultSet.getString(1);
			result.add(emailUtente);
		}

		return result;
	}

	public Utente getUtente(String email) throws SQLException, UtenteNonEsistente {
		String sql = "SELECT * FROM utente WHERE email=?";
		PreparedStatement preparedStatement = this.con.prepareStatement(sql);
		preparedStatement.setString(1, email);
		ResultSet resultSet = preparedStatement.executeQuery();

		if (!resultSet.next()) {
			throw new UtenteNonEsistente();
		}

		String emailUtente = resultSet.getString(1);
		String nomeUtente = resultSet.getString(2);
		String passwordUtente = resultSet.getString(3);
		int annoUtente = resultSet.getInt(4);
		String indirizzoUtente = resultSet.getString(5);
		String nome_fotoUtente = resultSet.getString(6);
		String comuneUtente = resultSet.getString(7);
		boolean flag_tutorUtente = resultSet.getBoolean(8);
		Utente utente = new Utente(emailUtente, nomeUtente, passwordUtente, annoUtente, indirizzoUtente, nome_fotoUtente, comuneUtente, flag_tutorUtente);

		return utente;
	}

	public void checkUtenteEsistente(String email) throws SQLException, UtenteNonEsistente {
		String sql = "SELECT * FROM utente WHERE email=?";
		PreparedStatement preparedStatement = this.con.prepareStatement(sql);
		preparedStatement.setString(1, email);

		ResultSet resultSet = preparedStatement.executeQuery();
		if(!resultSet.next()) {
			throw new UtenteNonEsistente();
		}
	}

	public void checkUtenteNonEsistente(String email) throws SQLException, UtenteGiàEsistente {
		String sql = "SELECT * FROM utente WHERE email=?";
		PreparedStatement preparedStatement = this.con.prepareStatement(sql);
		preparedStatement.setString(1, email);

		ResultSet resultSet = preparedStatement.executeQuery();
		if(resultSet.next()) {
			throw new UtenteGiàEsistente();
		}
	}

	public void checkUtenteConPassword(String email, String password) throws SQLException, EmailOPasswordErrati {
		String sql = "SELECT * FROM utente WHERE email=? AND password=?";
		PreparedStatement preparedStatement = this.con.prepareStatement(sql);
		preparedStatement.setString(1, email);
		preparedStatement.setString(2, password);

		ResultSet resultSet = preparedStatement.executeQuery();
		if(!resultSet.next()) {
			throw new EmailOPasswordErrati();
		}
	}

}


