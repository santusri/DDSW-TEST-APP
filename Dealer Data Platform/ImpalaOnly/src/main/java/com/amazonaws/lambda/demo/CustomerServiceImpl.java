package com.amazonaws.lambda.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.google.gson.Gson;

public class CustomerServiceImpl implements  CustomerService{
	
	
	 public List<HashMap<String, String>> getCustomerDetails(String limit , String customergroup , String dealercode, String delcustomernumber, String username , String password){
		
			List<HashMap<String, String>> customerlist = null;
			
			System.out.println("limit" + limit);
			System.out.println("customergroup" + customergroup);
			System.out.println("dealercode" + dealercode);
			System.out.println("sqlQuery" + delcustomernumber);
			String sqlQuery = "select t2.* from ddsw_proxy.ddsw_dealer_security as t1 inner join ddsw_proxy.customer_current as t2 on t1.dealer_code = t2.dealer_code where t1.consumer_group like '" +customergroup+"%' and t2.dealer_code ='" +dealercode+ "' "+" and t2.dealer_customer_number='"+delcustomernumber+ "'"+ " and t1.customer='Y' limit" +" "+limit ;
			
			System.out.println("sqlQuery  " + sqlQuery);

			try {
				/*customerlist = new CustomerServiceImpl()
						.executeQuery("select t2.* from ddsw_proxy.ddsw_dealer_security as t1 inner join ddsw_proxy.customer_current as t2 on t1.dealer_code = t2.dealer_code where t1.consumer_group like '" +customergroup+"%' and t2.dealer_code ='" +dealercode+ "' "+" and t2.dealer_customer_number='"+delcustomernumber+ "'"+ " and t1.customer='Y' limit" +" "+limit , username, password);
				*/
				customerlist = new CustomerServiceImpl()
						.executeQuery("select t2.* from ddsw_proxy.ddsw_dealer_security as t1 inner join ddsw_proxy.customer_current as t2 on t1.dealer_code = t2.dealer_code where t1.consumer_group like 'h%' and t2.dealer_code ='" +dealercode+ "' "+" and t2.dealer_customer_number='"+delcustomernumber+ "'"+ " and t1.customer='Y' limit" +" "+limit , username, password);
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		 return customerlist;
	 }
	 
	 public ArrayList<HashMap<String, String>> executeQuery(String query, String impalaUserName, String impalaPassword)
				throws Exception {

			ArrayList<HashMap<String, String>> data = new ArrayList();

			try (Connection conn = getImpalaConn(impalaUserName, impalaPassword); Statement stmt = conn.createStatement()) {

				
				System.out.println("query  " + query);
				System.out.println("impalaUserName  " + impalaUserName);

				System.out.println("impalaPassword  " + impalaPassword);


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

			Class.forName("com.cloudera.impala.jdbc41.Driver");
			String connectionString = DeclareConstants.CONNECTION_STRING;
			System.setProperty("javax.net.ssl.trustStore", DeclareConstants.jssecacerts);
			System.setProperty("javax.net.ssl.trustStorePassword", DeclareConstants.sslConnectionFilePassword);
			DriverManager.setLoginTimeout(DeclareConstants.loginTimeout);

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
