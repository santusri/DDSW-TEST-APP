package com.amazonaws.lambda.demo;

public class RequestClass {
	String limit;
    String customergroup;
    String dealercode;
    public String getDelcustomernumber() {
		return delcustomernumber;
	}

	public void setDelcustomernumber(String delcustomernumber) {
		this.delcustomernumber = delcustomernumber;
	}

	String delcustomernumber;
    
    public String getDealercode() {
		return dealercode;
	}

	public void setDealercode(String dealercode) {
		this.dealercode = dealercode;
	}

	public String getDealercustomercode() {
		return delcustomernumber;
	}

	  
   

    public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getCustomergroup() {
		return customergroup;
	}

	public void setCustomergroup(String customergroup) {
		this.customergroup = customergroup;
	}

	public RequestClass(String limit, String customergroup, String dealercode,String delcustomernumber) {
        this.limit = limit;
        this.customergroup = customergroup;
        this.dealercode = dealercode;
        this.delcustomernumber = delcustomernumber;
    }
    
    public RequestClass() {
    }

}