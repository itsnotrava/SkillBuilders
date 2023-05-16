package dao;

import exceptions.TicketNonEsistente;
import exceptions.UtenteNonEsistente;
import exceptions.UtenteGiàEsistente;
import exceptions.EmailOPasswordErrati;
import factory.ConnectionFactory;
import model.Ticket;
import model.Utente;
import org.springframework.security.crypto.bcrypt.BCrypt;

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

	private Ticket getTicketFromResultSet(ResultSet resultSet) throws SQLException {
		int id = resultSet.getInt(1);
		String testo = resultSet.getString(2);
		String materia = resultSet.getString(3);
		String emailUtente = resultSet.getString(4);
		String nomeUtente = resultSet.getString(5);
		String passwordUtente = resultSet.getString(6);
		int annoUtente = resultSet.getInt(7);
		String indirizzoUtente = resultSet.getString(8);
		String nome_fotoUtente = resultSet.getString(9);
		String comuneUtente = resultSet.getString(10);
		boolean flag_tutorUtente = resultSet.getBoolean(11);
		Utente utente = new Utente(emailUtente, nomeUtente, passwordUtente, annoUtente, indirizzoUtente, nome_fotoUtente, comuneUtente, flag_tutorUtente);
		return new Ticket(id, testo, materia, utente);
	}

	private ArrayList<Utente> getUtentiFromResultSet(ResultSet resultSet) throws SQLException {
		ArrayList<Utente> utenti = new ArrayList<>();
		while (resultSet.next()) {
			utenti.add(this.getUtenteFromResultSet(resultSet));
		}
		return utenti;
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

	public void insertTicket(String testo, String materia, String email_cliente) throws SQLException {
		String sql = "INSERT INTO ticket (testo, materia, email_cliente) VALUES (?, ?, ?)";
		PreparedStatement preparedStatement = this.con.prepareStatement(sql);
		preparedStatement.setString(1, testo);
		preparedStatement.setString(2, materia);
		preparedStatement.setString(3, email_cliente);
		preparedStatement.execute();
	}

	public void insertRecensione(int voto, String descrizione, String materia, String emailTutor, String emailCliente) {
		// TODO
	}

	public void updateRecensione(int id, int voto, String descrizione, String materia, String emailTutor, String emailCliente) {
		// TODO
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

	public Ticket getTicket(int id) throws SQLException, TicketNonEsistente {
		String sql = "SELECT t.id, t.testo, t.materia, u.* FROM ticket t INNER JOIN utente u ON t.email_cliente=u.email";
		Statement statement = this.con.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		if (!resultSet.next()) {
			throw new TicketNonEsistente();
		}
		return this.getTicketFromResultSet(resultSet);
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
		String sql = "SELECT password FROM utente WHERE email=?";
		PreparedStatement preparedStatement = this.con.prepareStatement(sql);
		preparedStatement.setString(1, email);

		ResultSet resultSet = preparedStatement.executeQuery();
		if(!resultSet.next() || !BCrypt.checkpw(password, resultSet.getString(1))) {
			throw new EmailOPasswordErrati();
		}
	}

	public void eliminaUtente(String email) throws SQLException {
		String sql = "DELETE FROM utente WHERE email=?";
		PreparedStatement preparedStatement  = this.con.prepareStatement(sql);
		preparedStatement.setString(1, email);

		ResultSet resultSet = preparedStatement.executeQuery();
	}

}


