package com.amazonaws.lambda.demo;

import com.amazonaws.services.lambda.runtime.Context;

import com.amazonaws.services.lambda.runtime.RequestHandler;

public class TestJson implements RequestHandler<Object, String> {

       @Override

       public String handleRequest(Object input, Context context) {

         context.getLogger().log("Input: " + input);

         String outputString = input.toString();

         return outputString;

       }

}