package com.amazonaws.lambda.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface CustomerService {

	 public List<HashMap<String, String>> getCustomerDetails(String limit , String customergroup , String dealercode, String delcustomernumber, String username, String password);
	 
}
