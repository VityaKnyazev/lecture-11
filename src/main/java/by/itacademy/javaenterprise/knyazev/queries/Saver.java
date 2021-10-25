package by.itacademy.javaenterprise.knyazev.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.itacademy.javaenterprise.knyazev.db.DbConnection;

public class Saver implements IQuery {
	private DbConnection dbConnection;
	private final Logger logger;

	public final static int SAVING_ID = 0;

	public Saver() {
		dbConnection = DbConnection.getDBO();
		logger = LoggerFactory.getLogger(getClass());
	}

	public int save(String tableName, String[] columnNames, String[] columnValues, int id) {
		int result = 0;
		String query = "";

		if (tableName == null)
			return result;
		if (columnNames == null || columnNames.length == 0)
			return result;
		if (columnValues == null || columnValues.length != columnValues.length)
			return result;

		if (id > 0) {

			String parameters = convertUpdateStr(columnNames);
			query = "UPDATE " + tableName + " SET " + parameters + " WHERE id = ?";

			logger.info("Preparing update query: " + query);

			try (Connection connection = dbConnection.getConnection();
					PreparedStatement ps = connection.prepareStatement(query)) {
				
				connection.setAutoCommit(false);
				
				for (int i = 0; i < columnValues.length; i++) {
					ps.setString(i + 1, columnValues[i]);
				}

				ps.setInt(columnNames.length + 1, id);

				result =  ps.executeUpdate();
				
				connection.commit();
				

			} catch (SQLException e) {
				logger.error("Error saving on save() method when executing update: " + e.getMessage());
			}
		} else {
			String columns = convertInsertStr(columnNames, false);
			String values = convertInsertStr(columnValues, true);

			query = "INSERT INTO " + tableName + " (" + columns + ")" + " values(" + values + ")";

			logger.info("Preparing insert query: " + query);
			try (Connection connection = dbConnection.getConnection();
					PreparedStatement ps = connection.prepareStatement(query)) {
				
				connection.setAutoCommit(false);
				
				for (int j = 0; j < columnValues.length; j++) {
					ps.setString(j + 1, columnValues[j]);
				}
				
				result = ps.executeUpdate();
				
				connection.commit();
				
			} catch (SQLException e) {
				logger.error("Error saving on save() method when executing insert: " + e.getMessage());
			}
		}
		return result;
	}

	public int saveNative(String nativeSqlQuery) {
		int result = 0;

		try (Connection connection = dbConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(nativeSqlQuery)) {
			connection.setAutoCommit(false);
			
			result = ps.executeUpdate();
			
			connection.commit();
		} catch (SQLException e) {
			logger.error("Error saving on native sql query " + e.getMessage());
		}

		return result;
	}

	private String convertInsertStr(String[] str, boolean questions) {
		String result = "";

		if (questions) {
			result = Arrays.asList(str).stream().map(s -> "?, ").collect(Collectors.joining());
		} else {
			result = Arrays.asList(str).stream().map(s -> s + ", ").collect(Collectors.joining());
		}
		result = result.substring(0, result.length() - 2);

		return result;
	}

	private String convertUpdateStr(String[] str) {
		String result = "";

		for (int i = 0; i < str.length; i++) {
			result += str[i] + " = " + "?, ";
		}

		result = result.substring(0, result.length() - 2);

		return result;
	}

}
