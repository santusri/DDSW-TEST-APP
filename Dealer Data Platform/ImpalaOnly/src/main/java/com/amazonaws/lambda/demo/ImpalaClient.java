package com.amazonaws.lambda.demo;

import java.sql.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImpalaClient {
	private static String JDBC_DRIVER = "com.cloudera.impala.jdbc41.Driver";
	private static final String CONNECTION_URL = "jdbc:impala://ec2-52-208-236-210.eu-west-1.compute.amazonaws.com:21050";
	static final Logger logger = LogManager.getLogger(ImpalaClient.class);

	private static Connection con = null;

	public static String query(String query) {
		try {
			logger.info("query to execute: " + query);
			Statement stmt = null;
			ResultSet rs = null;
			// Define a plain query
			Class.forName(JDBC_DRIVER);
			if (con == null) {
				con = DriverManager.getConnection(CONNECTION_URL);
			}
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();
			logger.info("numberOfColumns:" + numberOfColumns);
			StringBuilder results = new StringBuilder();
			while (rs.next()) {
				for (int i = 1; i <= numberOfColumns; i++) {
					if (i > 1) {
						results.append(", ");
					}
					String columnValue = rs.getString(i);
					results.append(columnValue);
				}
				results.append("  ; ");
			}
			String result = results.toString();
			logger.info("result: " + result);
			return result;
		} catch (Exception e) {
			logger.error("failed", e);
			return e.getMessage();
		}
	}

	public static void main(String[] args) throws Exception {
		String query = "SELECT COUNT(*) FROM customers WHERE name = 'Harrison SMITH'";
		ImpalaClient.query(query);
	}
}
