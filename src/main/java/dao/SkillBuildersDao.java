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

	private Utente getUtenteFromResultSet(ResultSet resultSet) throws SQLException {
		String email = resultSet.getString(1);
		String nome = resultSet.getString(2);
		String password = resultSet.getString(3);
		int anno = resultSet.getInt(4);
		String indirizzo = resultSet.getString(5);
		String nome_foto = resultSet.getString(6);
		String comune = resultSet.getString(7);
		boolean flag_tutor = resultSet.getBoolean(8);
		return new Utente(email, nome, password, anno, indirizzo, nome_foto, comune, flag_tutor);
	}

	private ArrayList<Utente> getUtentiFromResultSet(ResultSet resultSet) throws SQLException {
		ArrayList<Utente> utenti = new ArrayList<>();
		while (resultSet.next()) {
			utenti.add(this.getUtenteFromResultSet(resultSet));
		}
		return utenti;
	}

	public void insertUtente(String nome, String password, String email, int anno, String foto, String indirizzo, String comune, boolean flagTutor) throws SQLException, UtenteGiàEsistente {
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

	public void insertTicket(String testo, String materia, String email_cliente) throws SQLException {
		String sql = "INSERT INTO ticket (testo, materia, email_cliente) VALUES (?, ?, ?)";
		PreparedStatement preparedStatement = this.con.prepareStatement(sql);
		preparedStatement.setString(1, testo);
		preparedStatement.setString(2, materia);
		preparedStatement.setString(3, email_cliente);

		preparedStatement.execute();
	}

	public ArrayList<Utente> getTutors(int anno, String comune, String indirizzo) throws SQLException {
		String sql = "SELECT * FROM utente WHERE flag_tutor=1 AND (anno=? OR ?=0) AND (comune=? OR ?='') AND (indirizzo=? OR ?='')";
		PreparedStatement preparedStatement = this.con.prepareStatement(sql);
		preparedStatement.setInt(1, anno);
		preparedStatement.setInt(2, anno);
		preparedStatement.setString(3, comune);
		preparedStatement.setString(4, comune);
		preparedStatement.setString(5, indirizzo);
		preparedStatement.setString(6, indirizzo);
		ResultSet resultSet = preparedStatement.executeQuery();

		return this.getUtentiFromResultSet(resultSet);
	}

	public Utente getUtente(String email) throws SQLException, UtenteNonEsistente {
		String sql = "SELECT * FROM utente WHERE email=?";
		PreparedStatement preparedStatement = this.con.prepareStatement(sql);
		preparedStatement.setString(1, email);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (!resultSet.next()) {
			throw new UtenteNonEsistente();
		}
		return this.getUtenteFromResultSet(resultSet);
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


