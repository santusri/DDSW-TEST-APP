package com.amazonaws.lambda.demo;

import com.amazonaws.Request;
import com.amazonaws.Response;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;

public class Test implements RequestHandler<Request, String> {
    public String handleRequest(Request request, Context context) {
        System.out.println("Method = "+request.getHttpMethod());
        System.out.println("Headers= "+request.getHeaders().toString());
        System.out.println("Content= "+request.getContent().toString());
        System.out.println("Servcie Name = "+request.getServiceName().toString());
        System.out.println("Resource Path = "+request.getResourcePath().toString());
        System.out.println("Parameters = "+request.getParameters());

         return request.toString();
    }

}