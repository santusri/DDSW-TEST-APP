package com.amazonaws.lambda.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Customer {
   
	public List<HashMap<String, String>> getCustomerlist() {
		return customer;
	}

	public void setCustomerlist(List<HashMap<String, String>> l) {
		this.customer = l;
	}

	List<HashMap<String, String>> customer;
			//new ArrayList<Customer>();

}