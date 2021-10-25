package by.itacademy.javaenterprise.knyazev.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DbConnection {
	
	private final static String DB_CONFIG_PATH = "src/main/resources/database.properties";
	
	private HikariDataSource hikariDataSource;	
	private static DbConnection dbConnection;
	
	
	
	private DbConnection() {		
		HikariConfig hikariConfig = new HikariConfig(DB_CONFIG_PATH);
		hikariDataSource = new HikariDataSource(hikariConfig);
	}
	
	public static DbConnection getDBO() {
		if (dbConnection == null) {
			synchronized (DbConnection.class) {
				if (dbConnection == null) {
					dbConnection = new DbConnection();
				}
			}			
		}
				
		return dbConnection;
	}
	
	public Connection getConnection() throws SQLException {
		Connection connection =  hikariDataSource.getConnection();
		connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		return connection;
	}
	
	public void closePool() {
		if (!hikariDataSource.isClosed()) hikariDataSource.close();
	}

}
