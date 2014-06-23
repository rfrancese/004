package com.activity.principali;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.activity.principali.SimpleGestureFilter.SimpleGestureListener;
import com.classi.server.UserFunctions;
import com.example.buonotouristunisa.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class DettaglioCorsaActivity extends Activity  implements SimpleGestureListener {
	 /** AB BANNER CHE INSERISCO */
	  private AdView adView;

	  /* ID UNITA PUBBLICITARIO */
	  private static final String AD_UNIT_ID = "ca-app-pub-9936535009091025/4159664194";
    private SimpleGestureFilter detector;
	String nomeCorsa;
	String codiceCorsaReale;
	String oraPartenzaCorsaReale;
	String andataRitornoCorsaReale;
	String paeseFermata=null;
	String navbarSelect;
	String orarioFermataCercato=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_dettaglio_corsa);
		
		createAdModBanner();

		
		// SETTO I LISTENER AGLI ELEMENTI CREATI CON XML(SOLO NAVBAR)
		settaListenerBottoniNavbar(savedInstanceState);
		
		// VALORI ACTIVITY CHIAMANTE
		Intent intentApplicazione=getIntent();
		nomeCorsa= intentApplicazione.getStringExtra("nomeCorsa");
		codiceCorsaReale= intentApplicazione.getStringExtra("codiceCorsaReale");
		oraPartenzaCorsaReale= intentApplicazione.getStringExtra("oraPartenzaCorsaReale");
		andataRitornoCorsaReale= intentApplicazione.getStringExtra("andataRitornoCorsaReale");
		if(intentApplicazione.getStringExtra("paeseFermata") != null)
			paeseFermata =  intentApplicazione.getStringExtra("paeseFermata");
		
		if(intentApplicazione.getStringExtra("orarioFermata") != null){
			orarioFermataCercato =  intentApplicazione.getStringExtra("orarioFermata");
			if(orarioFermataCercato.length() < 8){
				orarioFermataCercato = "0"+orarioFermataCercato;
			}
		}
			
		
       Toast.makeText(getApplicationContext(),orarioFermataCercato, Toast.LENGTH_SHORT).show();  

		
		
        //Toast.makeText(getApplicationContext(),nomeCorsa, Toast.LENGTH_SHORT).show();  
        //Toast.makeText(getApplicationContext(),codiceCorsaReale, Toast.LENGTH_SHORT).show();  
        //Toast.makeText(getApplicationContext(),oraPartenzaCorsaReale, Toast.LENGTH_SHORT).show();  
        //Toast.makeText(getApplicationContext(),andataRitornoCorsaReale, Toast.LENGTH_SHORT).show();  

		// SETTO IL BOTTONE SELEZIONATO
		navbarSelect=intentApplicazione.getStringExtra("navbarSelect");
		if(navbarSelect.compareTo("CERCA")== 0){
			Button bottoneSelezione = (Button)findViewById(R.id.idBottoniNavbar_Cerca);
			bottoneSelezione.setText(R.string.tab1Selected);
			bottoneSelezione.setTextColor(getResources().getColor(R.color.col_button_navbar_selected_text));
		}else{  // E' CORSE
			Button bottoneSelezione = (Button)findViewById(R.id.idBottoniNavbar_Corse);
			bottoneSelezione.setText(R.string.tab2Selected);
			bottoneSelezione.setTextColor(getResources().getColor(R.color.col_button_navbar_selected_text));
		}	
        //METTO IL NOME ALLA TEXT VIEW SOPRA
		TextView nomeCentrale = (TextView) findViewById(R.id.idTextViewCerca_NomeCorsa);
		nomeCentrale.setText(nomeCorsa);
		
		detector = new SimpleGestureFilter(this,this); // GESTORE SWIPE

		
		// CONNESSIONE AT INTERNET
		NetAsync();
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
	
	
	
	public void NetAsync(){
	    new NetCheck().execute();
	}
	
/**
 * Async Task che controlla se la rete è disponibile
 **/

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(DettaglioCorsaActivity.this);
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
                new ProcessLogin().execute();
            }
            else{
                nDialog.dismiss();
                Toast.makeText(getApplicationContext(),getString(R.string.connessioneAssente), Toast.LENGTH_SHORT).show();            }
        }
    }

    /**
     * Async Task che invia dati al My Sql database
     **/
    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DettaglioCorsaActivity.this);
            pDialog.setTitle(getString(R.string.contattoServer));
            pDialog.setMessage(getString(R.string.invioDati));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.caricaDettaglioCorsa(codiceCorsaReale,oraPartenzaCorsaReale,andataRitornoCorsaReale);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
				try {
					//$corse["$i"]["NomePaeseCorsa"]=$arrayValori["NomePaese"];
					//$corse["$i"]["NomeFermataCorsa"]=$arrayValori["NomeFermata"];
					//$corse["$i"]["OrarioFermataCorsa"]=$arrayValori["OrarioFermata"];
					   if (json.getString("success") != null) {
							String number = json.getString("success");
					        if(Integer.parseInt(number) == 1){
					            pDialog.setMessage(getString(R.string.caricamentoDatiRicevuti));
					            pDialog.setTitle(getString(R.string.RicevoDati));
					           
					            TableLayout tableLayoutCorse = (TableLayout) findViewById(R.id.idTableLayoutCerca_DettagliCorsa);
					            JSONObject json_riga = null;
					            for (int i = 0; i < json.length()-1; i++) {
									json_riga= json.getJSONObject(""+i);	
									final TableRow nuovaRiga = new TableRow(getApplicationContext());
									
									TableLayout righeDellaCella = new TableLayout(getApplicationContext());
							
									TableRow nomePaese = new TableRow(getApplicationContext());
									TableRow nomeFermata = new TableRow(getApplicationContext());
									TableRow orarioFermata = new TableRow(getApplicationContext());
									TableRow latitutineLongitudineFermata= new TableRow(getApplicationContext());

									TextView NOMEPAESE = new TextView(getApplicationContext());
									NOMEPAESE.setText(getString(R.string.nomePaeseFermata));
									NOMEPAESE.setTextColor(Color.BLACK);
									NOMEPAESE.setTextSize(15);
									NOMEPAESE.setTypeface(null,Typeface.BOLD);
									
									TextView nomePaeseText = new TextView(getApplicationContext());
									if(paeseFermata != null && orarioFermataCercato != null){
										SimpleDateFormat format = new SimpleDateFormat("HH:MM:SS");										
									    Date date1 = format.parse(orarioFermataCercato);
									    Date date2 = format.parse(json_riga.getString("OrarioFermataCorsa"));
										if(paeseFermata.compareTo(json_riga.getString("NomePaeseCorsa"))== 0 && date1.getTime()  <= date2.getTime()   ){
											nomePaeseText.setTextColor(getResources().getColor(R.color.costiAbbonamentoBiglietto));
										}else{
											nomePaeseText.setTextColor(Color.BLACK);
										}
									}else{
										nomePaeseText.setTextColor(Color.BLACK);
									}
									
									nomePaeseText.setTextSize(13);
									nomePaeseText.setText(json_riga.getString("NomePaeseCorsa"));
							
									nomePaese.addView(NOMEPAESE);
									nomePaese.addView(nomePaeseText);
									
									//
									
									TextView NOMEFERMATA = new TextView(getApplicationContext());
									NOMEFERMATA.setText(getString(R.string.nomeFermataPaese));  
									NOMEFERMATA.setTextColor(Color.BLACK);
									NOMEFERMATA.setTextSize(15);
									NOMEFERMATA.setTypeface(null,Typeface.BOLD);
									TextView nomeFermataText = new TextView(getApplicationContext());
									nomeFermataText.setTextColor(Color.BLACK);
									nomeFermataText.setTextSize(13);
									nomeFermataText.setText(json_riga.getString("NomeFermataCorsa"));
									nomeFermata.addView(NOMEFERMATA);
									nomeFermata.addView(nomeFermataText);
									
									
									//
									TextView ORARIOFERMATA = new TextView(getApplicationContext());
									ORARIOFERMATA.setText(getString(R.string.orarioFermataPaese)); 
									ORARIOFERMATA.setTextColor(Color.BLACK);
									ORARIOFERMATA.setTextSize(15);
									ORARIOFERMATA.setTypeface(null,Typeface.BOLD);
									TextView orarioFermataText = new TextView(getApplicationContext());
									orarioFermataText.setTextColor(Color.BLACK);
									orarioFermataText.setTextSize(13);
									orarioFermataText.setText(json_riga.getString("OrarioFermataCorsa"));
									orarioFermata.addView(ORARIOFERMATA);
									orarioFermata.addView(orarioFermataText);
								
									//
									TextView LATITUDINEFERMATA= new TextView(getApplicationContext());
									LATITUDINEFERMATA.setText(json_riga.getString("FermataLatitudineCorsa"));
									LATITUDINEFERMATA.setTextColor(Color.TRANSPARENT);
									TextView LONGITUDINEFERMATA= new TextView(getApplicationContext());
									LONGITUDINEFERMATA.setText(json_riga.getString("FermataLongitudineCorsa"));
									LONGITUDINEFERMATA.setTextColor(Color.TRANSPARENT);
									latitutineLongitudineFermata.addView(LATITUDINEFERMATA);
									latitutineLongitudineFermata.addView(LONGITUDINEFERMATA);
				
									nomePaese.setPadding(10, 0, 0, 0);
									nomeFermata.setPadding(10, 0, 0, 0);
									orarioFermata.setPadding(10, 0, 0, 0);
									latitutineLongitudineFermata.setPadding(10, 0, 0, 0);
									
									righeDellaCella.addView(nomePaese);
									righeDellaCella.addView(nomeFermata);
									righeDellaCella.addView(orarioFermata);
									righeDellaCella.addView(latitutineLongitudineFermata);
									
									nuovaRiga.setBackgroundResource(R.drawable.drawable_statelist_row_table_fermatecorse_corse_a_r_dettagli);
									nuovaRiga.addView(righeDellaCella);
									
									// SETTO LA FERMATA VISUALIZZABILE SU MAPPA SE ABBIAMO A DISPOSIZIONE LATITUDINE E LONGITUDINE
									if(json_riga.getString("FermataLatitudineCorsa").compareTo("null")!= 0 && json_riga.getString("FermataLongitudineCorsa").compareTo("null") != 0 ){
										nuovaRiga.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												String nomePaese= ((TextView)((TableRow)((TableLayout)nuovaRiga.getChildAt(0)).getChildAt(0)).getChildAt(1)).getText().toString();
												String nomeFermata= ((TextView)((TableRow)((TableLayout)nuovaRiga.getChildAt(0)).getChildAt(1)).getChildAt(1)).getText().toString();
												String latitudineFermata= ((TextView)((TableRow)((TableLayout)nuovaRiga.getChildAt(0)).getChildAt(3)).getChildAt(0)).getText().toString();
												String longitudineFermata= ((TextView)((TableRow)((TableLayout)nuovaRiga.getChildAt(0)).getChildAt(3)).getChildAt(1)).getText().toString();
												
												apriMappaFermata(nomePaese, nomeFermata, latitudineFermata, longitudineFermata);
											}
										});
									}else{
										nuovaRiga.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
									            Toast.makeText(getApplicationContext(),getString(R.string.fermataNonDisponibile), Toast.LENGTH_SHORT).show();            
											}
										});
									}
									HorizontalScrollView scrollRiga = new HorizontalScrollView(getApplicationContext());
									scrollRiga.setFillViewport(true);
									scrollRiga.addView(nuovaRiga);
									tableLayoutCorse.addView(scrollRiga);
								}
					            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.notification_sound);
					            mp.start();
					            pDialog.dismiss();   
					        }else{
					            pDialog.dismiss();
					            Toast.makeText(getApplicationContext(),getString(R.string.datiInviatiNonValidi), Toast.LENGTH_SHORT).show();            
					         }
					   }else{
				            Toast.makeText(getApplicationContext(),"JSON SUCCESS NOT 1 (IMPOSSIBILE)", Toast.LENGTH_SHORT).show();            
					   }
				} catch (NumberFormatException e) {
		            Toast.makeText(getApplicationContext(),"ERROR NUMBER FORMAT", Toast.LENGTH_SHORT).show();            
				} catch (JSONException e) {
		            Toast.makeText(getApplicationContext(),"ERROR JSON SUCCESS O INTERNI(IMPOSSIBILE)", Toast.LENGTH_SHORT).show();            
				} catch (ParseException e) {
		            Toast.makeText(getApplicationContext(),"ERROR PARSE ORARY", Toast.LENGTH_SHORT).show();            

				}
        }
    }
    
    
    // -------------------->  ************** SWIPE
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
         this.detector.onTouchEvent(me);
       return super.dispatchTouchEvent(me);
    }
    @Override
     public void onSwipe(int direction) {
    	
    	if(navbarSelect.compareTo("CERCA")== 0){
    		 switch (direction) {
		      
		      case SimpleGestureFilter.SWIPE_RIGHT : createTariffeeActivity();
		                                             break;
		      case SimpleGestureFilter.SWIPE_LEFT :  createCorseActivity();
		                                             break;   
		      }		
		}else{  // E' CORSE
		      switch (direction) {
		      
		      case SimpleGestureFilter.SWIPE_RIGHT : createCercaActivity();
		                                             break;
		      case SimpleGestureFilter.SWIPE_LEFT :  createTariffeeActivity();
		                                             break;   
		      }	
		}	
     }
     @Override
     public void onDoubleTap() {
        //Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
     }
     
     public void apriMappaFermata(String nomePaese,String nomeFermata,String latitudineFermata,String longitudineFermata){
    	 Intent newIntent = new Intent(this,MapFragmentBusStopDettagli.class);
         //Toast.makeText(this, navbarSelect, Toast.LENGTH_SHORT).show();
    	 newIntent.putExtra("navbarSelect", navbarSelect);
         //Toast.makeText(this, nomePaese, Toast.LENGTH_SHORT).show();
		 newIntent.putExtra("nomePaese",nomePaese);
        // Toast.makeText(this, nomeFermata, Toast.LENGTH_SHORT).show();

		 newIntent.putExtra("nomeFermata",nomeFermata);
        // Toast.makeText(this,latitudineFermata, Toast.LENGTH_SHORT).show();

		 newIntent.putExtra("latitudineFermata", latitudineFermata);
		 
        // Toast.makeText(this, longitudineFermata, Toast.LENGTH_SHORT).show();

		 newIntent.putExtra("longitudineFermata",longitudineFermata);
		 startActivity(newIntent);
		 this.overridePendingTransition(R.anim.anim_late_in_left, R.anim.anim_zero);	
     }
     
     // *************************** ADMOB METHOD
     
 	private void createAdModBanner() {
 		 // Create an ad.
	    adView = new AdView(this);
	    adView.setAdSize(AdSize.BANNER);
	    adView.setAdUnitId(AD_UNIT_ID);
	    // Add the AdView to the view hierarchy. The view will have no size
	    // until the ad is loaded.
	    LinearLayout layout = (LinearLayout) findViewById(R.id.idLayout_BannerAdmob);
	    layout.removeAllViews();
	    layout.addView(adView);
	    // Create an ad request. Check logcat output for the hashed device ID to
	    // get test ads on a physical device.
	    //final TelephonyManager tm =(TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
	    //String deviceid = tm.getDeviceId();
	    AdRequest adRequest = new AdRequest.Builder()
	    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
	    .build();
	    // Start loading the ad in the background.
	    adView.loadAd(adRequest);
	}
    //gestione rotazione metodo che si attiva ogni volta che tuoto il dispositivo
    public void onConfigurationChanged(Configuration newConfig){
    	super.onConfigurationChanged(newConfig); 
    }	
     @Override
     public void onResume() {
       super.onResume();
       if (adView != null) {
         adView.resume();
       }
     }
     @Override
     public void onPause() {
       if (adView != null) {
         adView.pause();
       }
       super.onPause();
     }
     /** Called before the activity is destroyed. */
     @Override
     public void onDestroy() {
       // Destroy the AdView.
       if (adView != null) {
         adView.destroy();
       }
       super.onDestroy();
     }
}
