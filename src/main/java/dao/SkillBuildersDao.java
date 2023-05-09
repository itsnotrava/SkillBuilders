package dao;

import factory.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class SkillBuildersDao {
	private final Connection con;

	public SkillBuildersDao() throws SQLException {
		this.con = ConnectionFactory.createConnection("mysql");
	}
}
