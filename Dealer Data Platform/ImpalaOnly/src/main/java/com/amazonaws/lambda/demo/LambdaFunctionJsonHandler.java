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
import com.amazonaws.lambda.demo.CustomerService;
import com.amazonaws.lambda.demo.CustomerServiceImpl;


public class LambdaFunctionJsonHandler implements RequestHandler<RequestClass, ResponseClass> {
	static final Logger logger = LogManager.getLogger(ImpalaClient.class);
	static final String USERNAME_PARAM_KEY = "/impala/dev/username1";
	static final String USERNAME_PARAM_PASSWORD = "/impala/dev/password1";

	private static final String KEYID = "arn:aws:kms:us-east-1:084134011316:key/1447109c-a43a-4b77-a5c2-163b5ee0c86d";
	String ssmEndpoint = "vpce-0f26302cb297f41af-iji2bhxw.ssm.us-east-1.vpce.amazonaws.com";
	//String ssmEndpoint = "vpce-04ef28c268761a847-ktplkm39.ssm.us-east-1.vpce.amazonaws.com";

	String region = "us-east-1"; // adjust as appropriate to your setup

	EndpointConfiguration endpointConfiguration = new EndpointConfiguration(ssmEndpoint, region);
	AWSSimpleSystemsManagementClientBuilder builder =  AWSSimpleSystemsManagementClientBuilder.standard().withEndpointConfiguration(endpointConfiguration);
	AWSSimpleSystemsManagementClient client = (AWSSimpleSystemsManagementClient) builder.build(); 


	@Override
	public ResponseClass handleRequest(RequestClass request, Context context) {

		String username = reaSecuredParameter(USERNAME_PARAM_KEY);
		String password = reaSecuredParameter(USERNAME_PARAM_PASSWORD);

		//logger info

		System.out.println("read username: " + username);
		logger.info("read request 1: " + request.limit);
		//logger.info("read request 2: " + request.customergroup);
		System.out.println("read request 1: " + request.limit);
		System.out.println("read request 2: " + request.customergroup);
		System.out.println("read request 3: " + request.dealercode);
		System.out.println("read request 4: " + request.delcustomernumber);

		//fetch data from hadoop service 
		CustomerService customerserver = new CustomerServiceImpl();
		List<HashMap<String, String>> customerlist = customerserver.getCustomerDetails(request.limit, request.customergroup ,request.dealercode, request.delcustomernumber , username , password);
		System.out.println("Rows: " + customerlist.size());

		List<HashMap<String, String>> customers = new ArrayList<HashMap<String,String>>();
		Iterator<HashMap<String, String>> i = customerlist.iterator();
		while (i.hasNext()) {
			HashMap<String, String> m = i.next();
			Set<String> s = m.keySet();
			Iterator<String> i_s = s.iterator();
			while (i_s.hasNext()) {
				String col = i_s.next();
				String val = m.get(col);
				m.put(col, val);
				//logger.info("Column: " + col + " Value: " + val);
				System.out.println("Column: " + col + " Value: " + val);
				System.out.println("listS " + customers);

			}
			customers.add(m);

		}
		return new ResponseClass(customers);
	}


	/**
	 * Read value from Parameter store
	 * @param paramKey name of the parameter/secret to read
	 * @return
	 */
	private String readParameter(String paramKey) {
		GetParameterRequest request = (new GetParameterRequest()).withName(paramKey);
		GetParameterResult result = client.getParameter(request);
		return result.getParameter().getValue();
	}

	private String reaSecuredParameter(String paramKey) {
		GetParameterRequest parameterRequest = new GetParameterRequest();
		parameterRequest.withName(paramKey).setWithDecryption(Boolean.valueOf(true));

		// setWithDecryption will ensure that the results will be decrypted once they are returned

		// GetParameterResult parameterResult = simpleSystemsManagementClient.getParameter(parameterRequest);
		// return parameterResult.getParameter();
		//GetParameterRequest request = (new GetParameterRequest()).withName(paramKey);
		GetParameterResult result = client.getParameter(parameterRequest);
		return result.getParameter().getValue();
	}

}
