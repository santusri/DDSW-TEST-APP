package com.amazonaws.lambda.demo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClient;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.amazonaws.Request;

public class LambdaFunctionHandler implements RequestHandler<RequestClass, ResponseClass> {
	static final Logger logger = LogManager.getLogger(ImpalaClient.class);
	static final String USERNAME_PARAM_KEY = "/impala/dev/username";
	static final String USERNAME_PARAM_PASSWORD = "/impala/dev/password";

//	static final AWSSimpleSystemsManagementClient client = (AWSSimpleSystemsManagementClient) AWSSimpleSystemsManagementClientBuilder.defaultClient();
//			(AWSSimpleSystemsManagementClient) ((AWSSimpleSystemsManagementClientBuilder.standard())
//			.withEndpointConfiguration(new EndpointConfiguration("10.221.86.116", "us-east-1")).build());

	/*
	 * public static void main(String[] args) { try { List<HashMap<String,
	 * String>> l = new ImpalaTest().
	 * executeQuery("select * from ddsw_dev.customer_current limit 5");
	 * System.out.println("Rows: " + l.size()); Iterator<HashMap<String,
	 * String>> i = l.iterator(); while (i.hasNext()) { HashMap<String, String>
	 * m = i.next(); Set<String> s = m.keySet(); Iterator<String> i_s =
	 * s.iterator(); while (i_s.hasNext()) { String col = i_s.next(); String val
	 * = m.get(col); System.out.println("Column: " + col + " Value: " + val); }
	 * } //printList(l); } catch (Exception e) { System.out.println (e); } }
	 */
	@Override
	public ResponseClass handleRequest(RequestClass request, Context context) {
//		client.setEndpoint("10.221.86.116");
		logger.info("Input: " + request);
		String username = "hdddwi04@MW.NA.CAT.COM";
		String password = "DDSWnaslong#";
		String response = null;
		
		//List<HashMap<String, String>> l= null;
		//ResponseClass responseClass=new ResponseClass();

//		String username = readParameter(USERNAME_PARAM_KEY);
//		String password = readParameter(USERNAME_PARAM_PASSWORD);
		logger.info("read username: " + username);
		logger.info("read request 1: " + request.limit);
		logger.info("read request 2: " + request.customergroup);
		System.out.println("read request 1: " + request.limit);
		System.out.println("read request 2: " + request.customergroup);




		//try {
//			List<HashMap<String, String>> l = new LambdaFunctionJsonHandler()
//					.executeQuery("select t2.* from ddsw_proxy.ddsw_dealer_security as t1 inner join ddsw_proxy.customer_current as t2 on t1.dealer_code = t2.dealer_code where t1.consumer_group like"  + request.customergroup + "%' and t1.customer='Y' limit" + request.limit+ "", username, password);
//			
			 /*l = new LambdaFunctionJsonHandler()
					.executeQuery("select * from ddsw_dev.customer_current limit " +request.limit , username, password);
			*/
			List<HashMap<String, String>> l;
			try {
				l = new LambdaFunctionHandler()
				.executeQuery("select t2.* from ddsw_proxy.ddsw_dealer_security as t1 inner join ddsw_proxy.customer_current as t2 on t1.dealer_code = t2.dealer_code where t1.consumer_group like '" +request.customergroup+"%' and t1.customer='Y' limit" +" "+request.limit , username, password);
				
				logger.info("Rows: " + l.size());
				System.out.println("Rows: " + l.size());
				//cust = new Customer();
				
				//cust.setCustomerlist(l);
				
				response = new Gson().toJson(l);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			
			
	        String customers = String.format(response);
	        /*customers.replace("\\", "\\\\")
	        .replace("\"", "\\\"")
	        .replace("\r", "\\r")
	        .replace("\n", "\\n"); */
	        
	        if(customers.contains("\"")) {
	        	customers = customers.replaceAll("\"", "\\\\\"");
	        }
	        
			System.out.print("Custmers Response "+customers);
			
           			
			
					
			
			//System.out.println("json Response"+response);
			/*Iterator<HashMap<String, String>> i = l.iterator();
			while (i.hasNext()) {
				HashMap<String, String> m = i.next();
				Set<String> s = m.keySet();
				Iterator<String> i_s = s.iterator();
				while (i_s.hasNext()) {
					String col = i_s.next();
					String val = m.get(col);
					logger.info("Column: " + col + " Value: " + val);
				}
			}*/
			// printList(l ,context);
		/*} catch (Exception e) {
			logger.info(e.toString());
			logger.error("error", e);
			e.printStackTrace();
		}*/

		//return new ResponseClass(customers);
			return null;

	}

	/**
	 * Read value from Parameter store
	 * @param paramKey name of the parameter/secret to read
	 * @return
	 */
	private String readParameter(String paramKey) {
		return null;
//		GetParameterRequest request = (new GetParameterRequest()).withName(paramKey);
//		GetParameterResult result = client.getParameter(request);
//		return result.getParameter().getValue();
	}

	public ArrayList<HashMap<String, String>> executeQuery(String query, String impalaUserName, String impalaPassword)
			throws Exception {

		ArrayList<HashMap<String, String>> data = new ArrayList();

		try (Connection conn = getImpalaConn(impalaUserName, impalaPassword); Statement stmt = conn.createStatement()) {

			stmt.execute("SET MEM_LIMIT=2G");
			stmt.execute("SET REQUEST_POOL=ddsw");

			ResultSet queryResults = stmt.executeQuery(query);
			ResultSetMetaData queryMetadata = queryResults.getMetaData();

			int numColumns = queryMetadata.getColumnCount();

			while (queryResults.next()) {
				HashMap<String, String> row = new HashMap<>();
				for (int i = 1; i <= numColumns; i++) {
					String columnName = queryMetadata.getColumnName(i);
					String columnValue = queryResults.getString(i);
					row.put(columnName, columnValue);
				}
				data.add(row);
			}
			return data;
		} catch (Throwable ex) {
			throw (ex);
		}
	}

	private Connection getImpalaConn(String impalaUserName, String impalaPassword) throws Exception {

		// Class.forName("org.apache.hive.jdbc.HiveDriver");
		// Class.forName("com.cloudera.hive.jdbc41.HS2Driver");
		Class.forName("com.cloudera.impala.jdbc41.Driver");
	//	Class.forName("org.apache.hive.jdbc.HiveDriver");
//
		// String connectionString =
		// "jdbc:hive2://ue1lmgmtd02.na.aws.cat.com:21051/ddsw_dev;ssl=1;AuthMech=3";
		// String connectionString =
		// "jdbc:impala://ue1lmgmtd02.na.aws.cat.com:21051/ddsw_dev;ssl=1;AuthMech=3";
		// String connectionString =
		// "jdbc:impala://ue1lmgmtd02.na.aws.cat.com:21051/ddsw_dev;ssl=1;AuthMech=3;CAIssuedCertNamesMismatch=1";
		String connectionString = 
//				"jdbc:hive2://10.221.86.208:21051/ddsw_dev;ssl=1;AuthMech=3;CAIssuedCertNamesMismatch=1";
		//"jdbc:hive2://10.221.86.209:10000/ddsw_dev;ssl=1;AuthMech=3";

	 "jdbc:impala://10.221.86.208:21051/ddsw_dev;ssl=1;AuthMech=3;CAIssuedCertNamesMismatch=1";

		// String jssecacertPath =
		// "/opt/cloudera/security/jks/truststore/jssecacerts";
		String jssecacertPath = "jssecacerts";
		String sslConnectionFilePassword = "changeit";

		System.setProperty("javax.net.ssl.trustStore", jssecacertPath);
		System.setProperty("javax.net.ssl.trustStorePassword", sslConnectionFilePassword);

		// String impalaUserName = "hdddwi04@MW.NA.CAT.COM";
		// String impalaPassword = "DDSWnaslong#";

		DriverManager.setLoginTimeout(120);

		return DriverManager.getConnection(connectionString, impalaUserName, impalaPassword);
	}

	private static void printList(List<HashMap<String, String>> l) {
		Iterator<HashMap<String, String>> i = l.iterator();
		while (i.hasNext()) {
			HashMap<String, String> m = i.next();
			Set<String> s = m.keySet();
			Iterator<String> i_s = s.iterator();
			while (i_s.hasNext()) {
				String col = i_s.next();
				String val = m.get(col);
				System.out.println("Column: " + col + " Value: " + val);
			}
		}
	}

	
}
