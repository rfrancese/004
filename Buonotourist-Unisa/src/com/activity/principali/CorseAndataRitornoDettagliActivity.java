package com.activity.principali;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.activity.principali.SimpleGestureFilter.SimpleGestureListener;
import com.classi.server.UserFunctions;
import com.classi.server.UserFunctionsFacade;
import com.bdsirunisa.buonotouristunisa.R;
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

public class CorseAndataRitornoDettagliActivity extends Activity implements SimpleGestureListener {
	 /** AB BANNER CHE INSERISCO */
	  private AdView adView;

	  /* ID UNITA PUBBLICITARIO */
	  private static final String AD_UNIT_ID = "ca-app-pub-9936535009091025/4159664194";

    private SimpleGestureFilter detector;
	String nomeCorsa;
	String codiceCorsaReale;
	String andataRitornoCorsaReale;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_corse_andataritorno_dettagli);
		
		createAdModBanner();

		
		// SETTO I LISTENER AGLI ELEMENTI CREATI CON XML
		settaListenerBottoniNavbar(savedInstanceState);
		
		
		Intent intentApplicazione=getIntent();
		nomeCorsa= intentApplicazione.getStringExtra("nomeCorsa");
		codiceCorsaReale= intentApplicazione.getStringExtra("codiceCorsaReale");
		andataRitornoCorsaReale= intentApplicazione.getStringExtra("andataRitornoCorsaReale");
	
        //Toast.makeText(getApplicationContext(),nomeCorsa, Toast.LENGTH_SHORT).show();  
        //Toast.makeText(getApplicationContext(),codiceCorsaReale, Toast.LENGTH_SHORT).show();  
        //Toast.makeText(getApplicationContext(),andataRitornoCorsaReale, Toast.LENGTH_SHORT).show();  

		
		detector = new SimpleGestureFilter(this,this); // GESTORE SWIPE

		NetAsync();  //CARICA LE CORSE DAL DATABASE
		
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
				createTariffeActivity();
			}
		});
	}

	protected void createTariffeActivity() {
		try{
			startActivity(new Intent(this,TariffeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
			this.overridePendingTransition(R.anim.anim_late_in_left, R.anim.anim_zero);		
		}finally{
			finish();
		}
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
            nDialog = new ProgressDialog(CorseAndataRitornoDettagliActivity.this);
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
            pDialog = new ProgressDialog(CorseAndataRitornoDettagliActivity.this);
            pDialog.setTitle(getString(R.string.contattoServer));
            pDialog.setMessage(getString(R.string.invioDati));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
        	UserFunctionsFacade userFunction = new UserFunctions();
            JSONObject json = userFunction.caricaCorseAndataRitornoDettagli(nomeCorsa,codiceCorsaReale,andataRitornoCorsaReale);
            return json;
        }


		@Override
        protected void onPostExecute(JSONObject json) {
				try {
					   String successo= json.getString("success");
					   if (successo != null) {
				            	//Toast.makeText(getApplicationContext(),"SUCCESS", Toast.LENGTH_SHORT).show();            
							    String number = json.getString("success");
						        if(Integer.parseInt(number) == 1){
							            pDialog.setMessage(getString(R.string.caricamentoDatiRicevuti));
							            pDialog.setTitle(getString(R.string.RicevoDati));          
							            TableLayout tableLayoutCorse = (TableLayout) findViewById(R.id.idTableLayoutCorse_AndataRitorno);
							            JSONObject json_riga = null;
							            for (int i = 0; i < json.length()-1; i++) {
											json_riga= json.getJSONObject(""+i);	
											final TableRow nuovaRiga = new TableRow(getApplicationContext());
											nuovaRiga.setOnClickListener(new OnClickListener() {
												
												@Override
												public void onClick(View v) {
													//$corse["$i"]["NomeCorsa"]=$arrayValori["NomeCorsa"];
													//$corse["$i"]["CodiceCorsaReale"]=$arrayValori["CodiceCorsaR"];
													//$corse["$i"]["OraPartenzaCorsaReale"]=$arrayValori["OraPartenza"];
													//$corse["$i"]["AndataRitornoCorsaReale"]=$arrayValori["AndataRitorno"];
													String nomeCorsa= ((TextView)((TableRow)((TableLayout)nuovaRiga.getChildAt(0)).getChildAt(0)).getChildAt(1)).getText().toString();
													String codiceCorsaReale= ((TextView)((TableRow)((TableLayout)nuovaRiga.getChildAt(0)).getChildAt(0)).getChildAt(2)).getText().toString();
													String oraPartenzaCorsaReale= ((TextView)((TableRow)((TableLayout)nuovaRiga.getChildAt(0)).getChildAt(1)).getChildAt(1)).getText().toString();
													String andataRitornoCorsaReale= ((TextView)((TableRow)((TableLayout)nuovaRiga.getChildAt(0)).getChildAt(2)).getChildAt(1)).getText().toString();
													apriAttivitaDettaglio(nomeCorsa,codiceCorsaReale,oraPartenzaCorsaReale,andataRitornoCorsaReale);
												}

												
											});
											
											
											TableLayout righeDellaCella = new TableLayout(getApplicationContext());
									
											TableRow nomeCorsaRiga = new TableRow(getApplicationContext());
											TableRow oraPartenzaRiga = new TableRow(getApplicationContext());
											TableRow andataRitornoRiga = new TableRow(getApplicationContext());
											TableRow cliccaQuiPerIDettagli= new TableRow(getApplicationContext());

											TextView CORSA = new TextView(getApplicationContext());
											CORSA.setText(getString(R.string.nomeCorsa));  
											CORSA.setTextColor(Color.BLACK);
											CORSA.setTextSize(15);
											CORSA.setTypeface(null,Typeface.BOLD);
											TextView nomeCorsaText = new TextView(getApplicationContext());
											nomeCorsaText.setTextColor(Color.BLACK);
											nomeCorsaText.setTextSize(13);
											nomeCorsaText.setText(json_riga.getString("NomeCorsa"));
											TextView IDCORSA = new TextView(getApplicationContext());
											IDCORSA.setText(json_riga.getString("CodiceCorsaReale"));  
											IDCORSA.setTextColor(Color.TRANSPARENT);
											IDCORSA.setTextSize(13);
											
											nomeCorsaRiga.addView(CORSA);
											nomeCorsaRiga.addView(nomeCorsaText);
											nomeCorsaRiga.addView(IDCORSA);
											
											TextView ORAPARTENZA = new TextView(getApplicationContext());
											ORAPARTENZA.setText(getString(R.string.oraPartenza));  
											ORAPARTENZA.setTextColor(Color.BLACK);
											ORAPARTENZA.setTextSize(15);
											ORAPARTENZA.setTypeface(null,Typeface.BOLD);
											String capolineaPartenza="("+getString(R.string.daCapolinea)+" "+json_riga.getString("NomeCorsa").substring(0,json_riga.getString("NomeCorsa").indexOf("-"))+")";
											TextView oraPartenzaText = new TextView(getApplicationContext());
											oraPartenzaText.setTextColor(Color.BLACK);
											oraPartenzaText.setTextSize(13);
											oraPartenzaText.setText(json_riga.getString("OraPartenzaCorsaReale")+capolineaPartenza);			
											oraPartenzaRiga.addView(ORAPARTENZA);
											oraPartenzaRiga.addView(oraPartenzaText);
											
											TextView ANDATARITORNO = new TextView(getApplicationContext());
											ANDATARITORNO.setText(getString(R.string.andataRitorno)); 
											ANDATARITORNO.setTextColor(Color.BLACK);
											ANDATARITORNO.setTextSize(15);
											ANDATARITORNO.setTypeface(null,Typeface.BOLD);
											TextView andataRitornoText = new TextView(getApplicationContext());
											andataRitornoText.setTextColor(Color.BLACK);
											andataRitornoText.setTextSize(13);
											andataRitornoText.setText(json_riga.getString("AndataRitornoCorsaReale"));
											andataRitornoRiga.addView(ANDATARITORNO);
											andataRitornoRiga.addView(andataRitornoText);
											
											TextView scrittaDettagli = new TextView(getApplicationContext());
											scrittaDettagli.setText(getString(R.string.cliccaQuiFermate));
											scrittaDettagli.setTextColor(Color.GRAY);
											scrittaDettagli.setTextSize(13);
											cliccaQuiPerIDettagli.addView(new TableRow(getApplicationContext()));
											cliccaQuiPerIDettagli.addView(scrittaDettagli);
										
											nomeCorsaRiga.setPadding(10, 0, 0, 0);
											oraPartenzaRiga.setPadding(10, 0, 0, 0);
											andataRitornoRiga.setPadding(10, 0, 0, 0);
											righeDellaCella.addView(nomeCorsaRiga);
											righeDellaCella.addView(oraPartenzaRiga);
											righeDellaCella.addView(andataRitornoRiga);
											righeDellaCella.addView(cliccaQuiPerIDettagli);
											
											nuovaRiga.setBackgroundResource(R.drawable.drawable_statelist_row_table_fermatecorse_corse_a_r_dettagli);
											nuovaRiga.addView(righeDellaCella);
											
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
				            Toast.makeText(getApplicationContext(),"SUCCESS NULL ERROR", Toast.LENGTH_SHORT).show();
				            pDialog.dismiss();
					   }
				} catch (NumberFormatException e) {
		            Toast.makeText(getApplicationContext(),"ERROR SUCCESS NUMBER FORMAT", Toast.LENGTH_SHORT).show();  
		            pDialog.dismiss();
				} catch (JSONException e) {
		            Toast.makeText(getApplicationContext(),getString(R.string.zeroCorseTrovate), Toast.LENGTH_SHORT).show();  
		            pDialog.dismiss();
				}
        }
    }
    
    private void apriAttivitaDettaglio(String nomeCorsa,String codiceCorsaReale,String oraPartenzaCorsaReale,String andataRitornoCorsaReale) {
			Intent newIntent = new Intent(this,DettaglioCorsaActivity.class);
			newIntent.putExtra("nomeCorsa",nomeCorsa );
			newIntent.putExtra("codiceCorsaReale",codiceCorsaReale);
			newIntent.putExtra("oraPartenzaCorsaReale", oraPartenzaCorsaReale.substring(0,oraPartenzaCorsaReale.indexOf("(")));
			newIntent.putExtra("andataRitornoCorsaReale",andataRitornoCorsaReale);
			newIntent.putExtra("navbarSelect", "CORSE");
			startActivity(newIntent);
			this.overridePendingTransition(R.anim.anim_late_in_left, R.anim.anim_zero);		
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
      switch (direction) {
      
      case SimpleGestureFilter.SWIPE_RIGHT : createCercaActivity();
                                             break;
      case SimpleGestureFilter.SWIPE_LEFT :  createTariffeActivity();
                                             break;   
      }
     }
     @Override
     public void onDoubleTap() {
        //Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
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
