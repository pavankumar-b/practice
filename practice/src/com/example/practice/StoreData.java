package com.example.practice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class StoreData extends AsyncTask<String, Void, Void> {
	private static int rowCount=1;
	@Override
	protected Void doInBackground(String
			... xData) {
		// TODO Auto-generated method stub	
		String row = "row";
        String columnFamily="acdata";
        String column[]={"X","Y","Z"};
        int length=xData.length;
        for(int i=0;i<length;i++){
        try{
		  		DefaultHttpClient httpClient = new DefaultHttpClient();	  
				  HttpGet getRequest = new HttpGet(
		    			"http://134.193.136.114:8181/HBaseWS2/jaxrs/generic/hbaseInsertIndividual/sampleDataSensor/"+row+rowCount+"/"+columnFamily+"/"
		    			+column[i]+"/"+xData[i]);
				  HttpResponse response = httpClient.execute(getRequest);
						  if (response.getStatusLine().getStatusCode() != 200) {
							System.out.println("Failed : HTTP error code : "
				     			   + response.getStatusLine().getStatusCode());
							throw new RuntimeException("Failed : HTTP error code : "
							   + response.getStatusLine().getStatusCode());
						  	}
						BufferedReader br = new BufferedReader(
				                         new InputStreamReader((response.getEntity().getContent())));				 
						String output;
						System.out.println("Output from Server .... \n");
						while ((output = br.readLine()) != null) {
							System.out.println(output);
						} 
						httpClient.getConnectionManager().shutdown(); 		
			   
				  } catch (ClientProtocolException e) {
					e.printStackTrace();
				  } catch (IOException e) {
					e.printStackTrace();
				  }
        }
		rowCount = rowCount +1;
		return null;
	}
	protected Void hBaseStore(float xData){
		
		return null;
		
	}

}
