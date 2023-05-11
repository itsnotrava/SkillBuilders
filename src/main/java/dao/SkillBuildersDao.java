package dao;

import exceptions.UtenteNonEsistente;
import exceptions.UtenteGiàEsistente;
import exceptions.EmailOPasswordErrati;
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

	public void insertUtente(String nome, String password, String email, int anno, String foto, String indirizzo, String comune, boolean flagTutor) throws SQLException, UtenteGiàEsistente {
		try{
			checkUtente(email);
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

	public void checkUtente(String email) throws SQLException, UtenteNonEsistente {
		String sql = "SELECT * FROM utente WHERE email=?";
		PreparedStatement preparedStatement = this.con.prepareStatement(sql);
		preparedStatement.setString(1, email);

		ResultSet resultSet = preparedStatement.executeQuery();
		if(!resultSet.next()) {
			throw new UtenteNonEsistente();
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


