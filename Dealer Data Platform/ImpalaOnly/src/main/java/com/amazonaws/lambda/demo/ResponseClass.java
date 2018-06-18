package com.amazonaws.lambda.demo;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;



public class ResponseClass {
    

	public List<HashMap<String, String>> getCustomers() {
		return customers;
	}

	public void setCustomers(List<HashMap<String, String>> customers) {
		this.customers = customers;
	}

	List<HashMap<String, String>> customers;
   

    public ResponseClass(List<HashMap<String, String>> customers) {
        this.customers = customers;
    }
    
    public ResponseClass() {
        
    }
   
}