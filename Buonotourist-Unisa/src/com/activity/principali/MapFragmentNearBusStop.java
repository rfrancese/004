package com.activity.principali;

import com.classi.server.HttpConnection;
import com.classi.server.PathJSONParser;
import com.classi.server.UserFunctions;
import com.example.buonotouristunisa.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.support.v4.app.FragmentActivity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import android.widget.Toast;

import com.google.android.gms.maps.model.PolylineOptions;


public class MapFragmentNearBusStop extends  FragmentActivity{
	LocationListener myLocationListener= new LocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			if(status == LocationProvider.AVAILABLE){
                Toast.makeText(getApplicationContext(),getString(R.string.gpsDisponibile), Toast.LENGTH_SHORT).show();                        
			}else if(status == LocationProvider.TEMPORARILY_UNAVAILABLE){
                Toast.makeText(getApplicationContext(),getString(R.string.gpsTemporaneamenteNonDisponibile), Toast.LENGTH_SHORT).show();                        
			}else{
                Toast.makeText(getApplicationContext(),getString(R.string.gpsFuoriServizio), Toast.LENGTH_SHORT).show();                        
			}
		}
		@Override
		public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(),getString(R.string.gpsAbilitato), Toast.LENGTH_SHORT).show();    
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30, 200,myLocationListener);
		}
		@Override
		public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(),getString(R.string.gpsDisabilitato), Toast.LENGTH_SHORT).show(); 
		}	
		@Override
		public void onLocationChanged(Location location) {
			latitudinePosition=location.getLatitude();
			longitudinePosition=location.getLongitude();
		    NetAsyncNearBusStop();
		}
	};
	TextView textViewFermataVicina;
	String nomeFermataVicina;
   	String fermataVicinaLatitudine;
   	String fermataVicinaLongitudine;
	double latitudinePosition;
	double longitudinePosition;
	LocationManager locationManager;
	LocationProvider gpsProvider;
	private LatLng PROVENIENCE_POINT ;
	private LatLng DESTINATION_POINT ;	 
	GoogleMap googleMap;
	final String TAG = "PathGoogleMapActivity";
	ComputeDistanceBetween calcolatoreDistanze = new ComputeDistanceBetween();
	boolean firstTime=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_mapfragmentnearbusstop);
		googleMap= ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		textViewFermataVicina=(TextView)findViewById(R.id.idTextViewMappa_fermataVicina);
		// SETTO I LISTNER PER CAMBIARE LE ATTIVITA !
		settaListenerBottoniNavbar(savedInstanceState);
		
		// METODI DELLA MAPPA (( SI CONNETTE SOLO LE E' DISPONIBILE LA RETE ))
		settaPosizioneLocalizzazioneReteGPS();  
	}
	@Override
	protected void onPause(){
		super.onPause();
		locationManager.removeUpdates(myLocationListener);
	}
	@Override
	protected void onResume(){
		super.onResume();
		if(firstTime){
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30, 200,myLocationListener);
		}else{
			firstTime=!firstTime;
		}
	}
	
	private void settaListenerBottoniNavbar(final Bundle savedInstanceState) {
		Button buttonCercaNavbar =(Button)findViewById(R.id.idBottoniNavbar_Cerca);
		Button buttonCorseNavbar =(Button)findViewById(R.id.idBottoniNavbar_Corse);
		Button buttonTariffeNavbar =(Button)findViewById(R.id.idBottoniNavbar_Tariffe);
		buttonCercaNavbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 createCercaActivity();
			}
		});
		buttonCorseNavbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createCorseActivity();
			}
		});
		buttonTariffeNavbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createTariffeeActivity();
			}
		});
	}
	
	
	protected void createCercaActivity() {
		try{
			startActivity(new Intent(this,CercaActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
			this.overridePendingTransition(R.anim.anim_late_in_left, R.anim.anim_zero);		
		}finally{
			finish();
		}
	}
	
	protected void createCorseActivity() {
		try{
			startActivity(new Intent(this,CorseActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
			this.overridePendingTransition(R.anim.anim_late_in_left, R.anim.anim_zero);		
			}finally{
			finish();
		}
	}


	protected void createTariffeeActivity() {
		try{
			startActivity(new Intent(this,TariffeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
			this.overridePendingTransition(R.anim.anim_late_in_left, R.anim.anim_zero);		
		}finally{
			finish();
		}
	}
	
	
	// METODI DI MAPPA CHE SETTANO IL PERCORSO
	
    public void settaPosizioneLocalizzazioneReteGPS(){
		locationManager=(LocationManager) getSystemService(LOCATION_SERVICE);
		gpsProvider= locationManager.getProvider(LocationManager.GPS_PROVIDER);
		PackageManager PM= this.getPackageManager();
		boolean gps = PM.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
		if(!gps){
	        Toast.makeText(getApplicationContext(),getString(R.string.gpsAssenteDispositivo), Toast.LENGTH_LONG).show();
	        createCercaActivity();
		}else if(gpsProvider != null){
			if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !isNetworkAvailable()){
		        Toast.makeText(getApplicationContext(),getString(R.string.erroreFermataVicina1), Toast.LENGTH_LONG).show();            
		        createCercaActivity();
			}else if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
				Toast.makeText(getApplicationContext(),getString(R.string.erroreFermataVicina2), Toast.LENGTH_LONG).show();            
		        createCercaActivity();
			}else if(!isNetworkAvailable()){
				Toast.makeText(getApplicationContext(),getString(R.string.erroreFermataVicina3), Toast.LENGTH_LONG).show();            
		        createCercaActivity();
			}else{
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30, 200,myLocationListener);
			}
		}
	}
    
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

	private void settaPercorsoMappa(){
		//map.moveCamera(CameraUpdateFactory.newLatLngZoom(start,5));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
		//map.addMarker(new MarkerOptions().position(start));
		//map.setOnMarkerClickListener(this);
		googleMap.setMyLocationEnabled(true);  // centra sulla mia pos.
		//googleMap.setTrafficEnabled(true);     // mostra stato traffico
		//aggiungo un marker sulla posizione start
		//map.addMarker(new MarkerOptions().position(start).title("Il mio pin").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
		MarkerOptions options = new MarkerOptions();
		PROVENIENCE_POINT= new LatLng(latitudinePosition,longitudinePosition);
	    options.position(PROVENIENCE_POINT);
	    DESTINATION_POINT = new LatLng(Double.parseDouble(fermataVicinaLatitudine),Double.parseDouble(fermataVicinaLongitudine));
	    options.position(DESTINATION_POINT);
	    googleMap.clear(); // LO USO PER RIMUOVERE TUTTI I MARKER
	    googleMap.addMarker(options);
	    String url = getMapsApiDirectionsUrl();
	    ReadTask downloadTask = new ReadTask();
	    downloadTask.execute(url);
	    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PROVENIENCE_POINT,10));
	    addMarkers();
	    String distanza=""+Math.floor(calcolatoreDistanze.distance(PROVENIENCE_POINT.latitude,DESTINATION_POINT.latitude,PROVENIENCE_POINT.longitude,DESTINATION_POINT.longitude));
	    textViewFermataVicina.setText(getString(R.string.laFermataPiuVicina)+"\n"+nomeFermataVicina+" - "+"("+distanza+"Km)");
	}
	
	private String getMapsApiDirectionsUrl() {
	    String waypoints = "waypoints=optimize:true|"
	        + PROVENIENCE_POINT.latitude + "," + PROVENIENCE_POINT.longitude
	        + "|" + "|" + DESTINATION_POINT.latitude + ","
	        + DESTINATION_POINT.longitude;
	 
	    String sensor = "sensor=false";
	    String params = waypoints + "&" + sensor;
	    String output = "json";
	    String url = "https://maps.googleapis.com/maps/api/directions/"
	        + output + "?" + params;
	    return url;
	  }
	 
	  private void addMarkers() {
	    if (googleMap != null) {
	      googleMap.addMarker(new MarkerOptions().position(DESTINATION_POINT)
	          .title(nomeFermataVicina));
	      googleMap.addMarker(new MarkerOptions().position(PROVENIENCE_POINT)
	          .title("Punto di provenienza"));
	    }
	  }
	 
	  private class ReadTask extends AsyncTask<String, Void, String> {
	    @Override
	    protected String doInBackground(String... url) {
	      String data = "";
	      try {
	        HttpConnection http = new HttpConnection();
	        data = http.readUrl(url[0]);
	      } catch (Exception e) {
	        Log.d("Background Task", e.toString());
	      }
	      return data;
	    }
	 
	    @Override
	    protected void onPostExecute(String result) {
	      super.onPostExecute(result);
	      new ParserTask().execute(result);
	    }
	  }
	 
	  private class ParserTask extends
	      AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
	 
	    @Override
	    protected List<List<HashMap<String, String>>> doInBackground(
	        String... jsonData) {
	 
	      JSONObject jObject;
	      List<List<HashMap<String, String>>> routes = null;
	 
	      try {
	        jObject = new JSONObject(jsonData[0]);
	        PathJSONParser parser = new PathJSONParser();
	        routes = parser.parse(jObject);
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	      return routes;
	    }
	 
	    @Override
	    protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
	      ArrayList<LatLng> points = null;
	      PolylineOptions polyLineOptions = null;
	 
	      // traversing through routes
	      for (int i = 0; i < routes.size(); i++) {
	        points = new ArrayList<LatLng>();
	        polyLineOptions = new PolylineOptions();
	        List<HashMap<String, String>> path = routes.get(i);
	 
	        for (int j = 0; j < path.size(); j++) {
	          HashMap<String, String> point = path.get(j);
	 
	          double lat = Double.parseDouble(point.get("lat"));
	          double lng = Double.parseDouble(point.get("lng"));
	          LatLng position = new LatLng(lat, lng);
	 
	          points.add(position);
	        }
	 
	        polyLineOptions.addAll(points);
	        polyLineOptions.width(5);
	        polyLineOptions.color(Color.BLUE);
	      }
	      googleMap.addPolyline(polyLineOptions);
	    }
	  }
	  
	  // **********************+   METODI PER PRENDERE DA PHP LA FERMATA PIU VICINA!!
	  public void NetAsyncNearBusStop(){
		    new NetCheckBusStop().execute();
	  }
	/**
	 * Async Task che controlla se la rete è disponibile
	 **/
	    private class NetCheckBusStop extends AsyncTask<String,String,Boolean>
	    {
	        private ProgressDialog nDialog;
	        @Override
	        protected void onPreExecute(){
	            super.onPreExecute();
	            nDialog = new ProgressDialog(MapFragmentNearBusStop.this);
	            nDialog.setTitle(getString(R.string.stoControllandoRete));
	            nDialog.setMessage(getString(R.string.caricamento));
	            nDialog.setIndeterminate(false);
	            nDialog.setCancelable(false);
	            nDialog.show();
	        }
	        /**
	         * Gets current device state and checks for working internet connection by trying Google.
	        **/
	        @Override
	        protected Boolean doInBackground(String... args){
	            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	            NetworkInfo netInfo = cm.getActiveNetworkInfo();
	            if (netInfo != null && netInfo.isConnected()) {
	                try {
	                    URL url = new URL("http://www.google.com");
	                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
	                    urlc.setConnectTimeout(3000);
	                    urlc.connect();
	                    if (urlc.getResponseCode() == 200) {
	                        return true;
	                    }
	                } catch (MalformedURLException e1) {
	                    // TODO Auto-generated catch block
	                    e1.printStackTrace();
	                } catch (IOException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }
	            return false;
	        }
	        @Override
	        protected void onPostExecute(Boolean th){

	            if(th == true){
	                nDialog.dismiss();
	                new ProcessFermataVicina().execute();
	            }
	            else{
	                nDialog.dismiss();
	                Toast.makeText(getApplicationContext(),getString(R.string.connessioneAssente)+"\n"+getString(R.string.impossibileAggiornamePercorso), Toast.LENGTH_SHORT).show();            }
	        }
	    }

	    /**
	     * Async Task che invia dati al My Sql database
	     **/
	    private class ProcessFermataVicina extends AsyncTask<String, String, JSONObject> {

			private ProgressDialog pDialog;

	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(MapFragmentNearBusStop.this);
	            pDialog.setTitle(getString(R.string.contattoServer));
	            pDialog.setMessage(getString(R.string.invioDati));
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }

	        @Override
	        protected JSONObject doInBackground(String... args) {
	            UserFunctions userFunction = new UserFunctions();
	            JSONObject json = userFunction.trovaFermataPiuVicina(latitudinePosition,longitudinePosition);
	            return json;
	        }


			@Override
	        protected void onPostExecute(JSONObject json) {
					try {
						   String successo= json.getString("success");
						   if (successo != null) {
								    String number = json.getString("success");
							        if(Integer.parseInt(number) == 1){
								            pDialog.setMessage(getString(R.string.caricamentoDatiRicevuti));
								            pDialog.setTitle(getString(R.string.RicevoDati));
								           	nomeFermataVicina=json.getString("NomeFermata");
								           	fermataVicinaLatitudine=json.getString("FermataLatitudine");
								           	fermataVicinaLongitudine=json.getString("FermataLongitudine");
								            //MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.notification_sound);
								            //mp.start();
								            pDialog.dismiss();
											settaPercorsoMappa();
							         }else{
							            pDialog.dismiss();
							            Toast.makeText(getApplicationContext(),getString(R.string.datiInviatiNonValidi), Toast.LENGTH_SHORT).show();            
							         }
						   }else{
					            Toast.makeText(getApplicationContext(),"SUCCESS NULL ERROR", Toast.LENGTH_SHORT).show();
						   }
					} catch (NumberFormatException e) {
			            Toast.makeText(getApplicationContext(),"ERROR SUCCESS NUMBER FORMAT", Toast.LENGTH_SHORT).show();  
					} catch (JSONException e) {
			            Toast.makeText(getApplicationContext(),getString(R.string.nessunaFermataTrovata), Toast.LENGTH_SHORT).show();  
					}
	        }
	    }

}
