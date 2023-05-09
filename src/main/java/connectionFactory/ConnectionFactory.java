package connectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.sqlite.SQLiteDataSource;

/**
 * <h3>Connection Factory</h3>
 * <p>Questa classe crea e restituisce connessioni al DB, a seconda del tipo di DB:</p>
 * <ul>
 * <li>SQLite</li>
 * <li>MySQL</li>
 * </ul>
 * by @NicolaTravaglini
 */
public class ConnectionFactory {

	public Connection createConnection(String tipoDatabase) throws SQLException {
		switch (tipoDatabase) {
			case "sqlite" -> {
				SQLiteDataSource dataSource = new SQLiteDataSource();
				dataSource.setUrl("jdbc:sqlite:");
				return dataSource.getConnection();
			}
			case "mysql" -> { // PER COGLIONI
				MysqlDataSource dataSource = new MysqlDataSource();
				dataSource.setUrl("jdbc:mysql://13.42.233.112:3306/SkillBuilders");
				dataSource.setUser("root");
				dataSource.setPassword("Skillbuilders23!");
				return dataSource.getConnection();
			}
			default -> {
				return null;
			}
		}
	}

}
