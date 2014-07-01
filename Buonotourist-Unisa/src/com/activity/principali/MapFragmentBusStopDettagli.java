package com.activity.principali;

import com.bdsirunisa.buonotouristunisa.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

 
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import android.widget.Toast;



public class MapFragmentBusStopDettagli extends  FragmentActivity{
	//VALORI PASSATI INTENT
	String navbarSelect;
	String nomePaese;
	String nomeFermata;
	String latitudineFermata;
	String longitudineFermata;
	
	TextView textViewFermataDettagliFermata;
	private LatLng DESTINATION_POINT ;	 
	GoogleMap googleMap;
	final String TAG = "PathGoogleMapActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// PRENDO VALORI DALL'INTENT
		Intent intentApplicazione=getIntent();
		navbarSelect=intentApplicazione.getStringExtra("navbarSelect");
		nomePaese= intentApplicazione.getStringExtra("nomePaese");
		nomeFermata= intentApplicazione.getStringExtra("nomeFermata");
		latitudineFermata= intentApplicazione.getStringExtra("latitudineFermata");
		longitudineFermata= intentApplicazione.getStringExtra("longitudineFermata");
		
		// SETTO IL LAYOUT A SECONDA DEL 'INTENT
		if(navbarSelect.compareTo("CERCA")== 0){
			setContentView(R.layout.layout_mapfragmenbusstopdettagli_cerca);
		}else{
			setContentView(R.layout.layout_mapfragmenbusstopdettagli_corse);
		}
		googleMap= ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		googleMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng point) {
				String geoCode = "geo:0,0?q=" + latitudineFermata + ","+ longitudineFermata + "(" + nomeFermata + ")";
				Intent sendLocationToMap = new Intent(Intent.ACTION_VIEW,
				Uri.parse(geoCode));
				startActivity(sendLocationToMap);
			}
			
		});
		textViewFermataDettagliFermata=(TextView)findViewById(R.id.idTextViewMappa_nomeFermata);
		textViewFermataDettagliFermata.setText(nomePaese+" - "+nomeFermata);
		
		// SETTO I LISTNER PER CAMBIARE LE ATTIVITA !
		settaListenerBottoniNavbar(savedInstanceState);
		
		// METODI DELLA MAPPA (( SI CONNETTE SOLO LE E' DISPONIBILE LA RETE ))
		NetAsyncBusStop();
	}
	@Override
	protected void onPause(){
		super.onPause();
		
	}
	@Override
	protected void onResume(){
		super.onResume();
		
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
	

	private void settaPosizioneMappa(){
		//map.moveCamera(CameraUpdateFactory.newLatLngZoom(start,5));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
		//map.addMarker(new MarkerOptions().position(start));
		//map.setOnMarkerClickListener(this);
		googleMap.setMyLocationEnabled(true);  // centra sulla mia pos.
		//googleMap.setTrafficEnabled(true);     // mostra stato traffico
		//aggiungo un marker sulla posizione start
		//map.addMarker(new MarkerOptions().position(start).title("Il mio pin").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
		MarkerOptions options = new MarkerOptions();
	    DESTINATION_POINT = new LatLng(Double.parseDouble(latitudineFermata),Double.parseDouble(longitudineFermata));
	    options.position(DESTINATION_POINT);
	    googleMap.clear(); // LO USO PER RIMUOVERE TUTTI I MARKER
	    googleMap.addMarker(options);
	    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DESTINATION_POINT,16));
	    addMarkers();
	}
	 
	  private void addMarkers() {
	    if (googleMap != null) {
	      googleMap.addMarker(new MarkerOptions().position(DESTINATION_POINT)
	          .title(nomeFermata));
	    }
	  }
  
	  // **********************+   METODI PER PRENDERE DA PHP LA FERMATA PIU VICINA!!
	  public void NetAsyncBusStop(){
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
	            nDialog = new ProgressDialog(MapFragmentBusStopDettagli.this);
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
	                settaPosizioneMappa();
	            }
	            else{
	                nDialog.dismiss();
	                Toast.makeText(getApplicationContext(),getString(R.string.connessioneAssente), Toast.LENGTH_SHORT).show();            }
	        }
	    }

}
