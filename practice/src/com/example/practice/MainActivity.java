package com.example.practice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ListIterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements SensorEventListener{

	//check for sensor data using sensor manager
	private SensorManager sensorManager;
	private Sensor gyro;
	private Sensor accelorometer;
	private Sensor temp;
	private TextView txtView;
	private TextView xAxisView;
	private TextView yAxisView;
	private TextView zAxisView;
	private String sensorNames;
	private long lastUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtView=(TextView) findViewById(R.id.textView2);
        xAxisView=(TextView) findViewById(R.id.textView3);
        yAxisView=(TextView) findViewById(R.id.textView4);
        zAxisView=(TextView) findViewById(R.id.textView5);
        
        sensorNames=new String("");
        if(savedInstanceState!= null) { 
            // So the orientation did change
            // Restore some field for example
            this.sensorNames = savedInstanceState.getString("sensorNames");
        }
        
        Button button1=(Button)findViewById(R.id.button1);
        
		ConnectivityManager connectivityManager =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nwInfo=connectivityManager.getActiveNetworkInfo();
		//String sensorNames= new String("");
		sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
		
		if(nwInfo!=null){
		if(nwInfo.getType()==connectivityManager.TYPE_WIFI){					
			sensorNames=sensorNames.concat("available wifi connection : "+nwInfo.getExtraInfo().toString()+"\n");
		}
		else if(nwInfo.getType()==connectivityManager.TYPE_MOBILE){
			sensorNames=sensorNames.concat("available mobile data connection : "+nwInfo.getExtraInfo().toString()+"\n");
		}
		else{
			sensorNames=sensorNames.concat("Not connected to internet"+"\n");
		}
		}
		
		//check for sensor data
		List<Sensor> sensorList=sensorManager.getSensorList(Sensor.TYPE_ALL);
		ListIterator listite = sensorList.listIterator(); 
		
		/*if(sensorList.size()!=0){
			txtView.setText("Available sensors on your mobile ");
			sensorNames=sensorNames.concat("Available sensors on your mobile"+"\n");
			while(listite.hasNext()){
				
				sensorNames=sensorNames.concat(listite.next().toString()+"\n");
				txtView.setText(listite.next().toString());
			}
			
		}*/
		gyro=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		accelorometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if(gyro !=null){
			sensorNames=sensorNames.concat("gyro data -"+gyro.toString()+"\n");
		}
		if(accelorometer != null){
			sensorNames=sensorNames.concat("accelorometer data -"+accelorometer.toString()+"\n");
		}
		
		temp=sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
		if(temp!=null){sensorManager.registerListener(tempListener, temp,SensorManager.SENSOR_DELAY_FASTEST);}
		//txtView.setText(sensorNames);
        
        button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		
    });
        }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("sensorNames", sensorNames);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
			long currentTime=System.currentTimeMillis();
			if(currentTime-lastUpdate>500){
				lastUpdate=currentTime;
				float x=event.values[0];
				float y=event.values[1];
				float z=event.values[2];
				
				xAxisView.setText("X :"+String.valueOf(x));
				yAxisView.setText("Y :"+String.valueOf(y));
				zAxisView.setText("Z :"+String.valueOf(z));
				try{
					//storing this data into Hbase
					String sensorValues[]={String.valueOf(x),String.valueOf(y),String.valueOf(z)};
					//new StoreData().execute(sensorValues);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
			}
		}
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	  protected void onResume() {
	    super.onResume();
	    
	    sensorManager.registerListener(this, accelorometer, SensorManager.SENSOR_DELAY_UI);
	    lastUpdate=System.currentTimeMillis();
	}

	  @Override
	  protected void onPause() {
	    super.onPause();
	    sensorManager.unregisterListener(this);
	  }
	  private final SensorEventListener tempListener=new SensorEventListener(){

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			txtView.setText("temperature is :"+event.values[0]);
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
		  
	  };

}
