package com.activity.principali;

import com.example.buonotouristunisa.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import org.json.JSONObject;
 
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
 
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class WhereIAmMapFragment extends  FragmentActivity{
	private static final LatLng CIMITILE_MACELLO = new LatLng(40.9431238,14.5251615);
	private static final LatLng FISCIANO_UNI = new LatLng(40.7721084,14.7993696);	 
	GoogleMap googleMap;
	final String TAG = "PathGoogleMapActivity";
	
	//private final LatLng start= new LatLng(45.464711, 9.188736);
	//private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_whereiamapfragment);
		
		this.googleMap= ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		//map.moveCamera(CameraUpdateFactory.newLatLngZoom(start,5));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
		
		//map.addMarker(new MarkerOptions().position(start));
		//map.setOnMarkerClickListener(this);
		
		googleMap.setMyLocationEnabled(true);  // centra sulla mia pos.
		//googleMap.setTrafficEnabled(true);     // mostra stato traffico
		//aggiungo un marker sulla posizione start
		//map.addMarker(new MarkerOptions().position(start).title("Il mio pin").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
		
		
		MarkerOptions options = new MarkerOptions();
	    options.position(CIMITILE_MACELLO);
	    options.position(FISCIANO_UNI);
	    googleMap.addMarker(options);
	    String url = getMapsApiDirectionsUrl();
	    ReadTask downloadTask = new ReadTask();
	    downloadTask.execute(url);
	 
	    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CIMITILE_MACELLO,
	        10));
	    addMarkers();
	}
	
	private String getMapsApiDirectionsUrl() {
	    String waypoints = "waypoints=optimize:true|"
	        + CIMITILE_MACELLO.latitude + "," + CIMITILE_MACELLO.longitude
	        + "|" + "|" + FISCIANO_UNI.latitude + ","
	        + FISCIANO_UNI.longitude;
	 
	    String sensor = "sensor=false";
	    String params = waypoints + "&" + sensor;
	    String output = "json";
	    String url = "https://maps.googleapis.com/maps/api/directions/"
	        + output + "?" + params;
	    return url;
	  }
	 
	  private void addMarkers() {
	    if (googleMap != null) {
	      googleMap.addMarker(new MarkerOptions().position(FISCIANO_UNI)
	          .title("First Point"));
	      googleMap.addMarker(new MarkerOptions().position(CIMITILE_MACELLO)
	          .title("Second Point"));
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
	        polyLineOptions.width(2);
	        polyLineOptions.color(Color.BLUE);
	      }
	 
	      googleMap.addPolyline(polyLineOptions);
	    }
	  }

}
