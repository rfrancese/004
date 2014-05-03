package com.activity.principali;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.classi.server.UserFunctions;
import com.example.proveandroid.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class DettaglioCorsaActivity extends Activity {
	String nomeCorsa;
	String codiceCorsaReale;
	String oraPartenzaCorsaReale;
	String andataRitornoCorsaReale;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dettaglio_corsa);
		
		// SETTO I LISTENER AGLI ELEMENTI CREATI CON XML(SOLO NAVBAR)
		settaListenerBottoniNavbar(savedInstanceState);
		
		Intent intentApplicazione=getIntent();
		nomeCorsa= intentApplicazione.getStringExtra("nomeCorsa");
		codiceCorsaReale= intentApplicazione.getStringExtra("codiceCorsaReale");
		oraPartenzaCorsaReale= intentApplicazione.getStringExtra("oraPartenzaCorsaReale");
		andataRitornoCorsaReale= intentApplicazione.getStringExtra("andataRitornoCorsaReale");
        //Toast.makeText(getApplicationContext(),nomeCorsa, Toast.LENGTH_SHORT).show();  
        //Toast.makeText(getApplicationContext(),codiceCorsaReale, Toast.LENGTH_SHORT).show();  
        //Toast.makeText(getApplicationContext(),oraPartenzaCorsaReale, Toast.LENGTH_SHORT).show();  
        //Toast.makeText(getApplicationContext(),andataRitornoCorsaReale, Toast.LENGTH_SHORT).show();  

        //METTO IL NOME ALLA TEXT VIEW SOPRA
		TextView nomeCentrale = (TextView) findViewById(R.id.idTextViewCerca_NomeCorsa);
		nomeCentrale.setText(nomeCorsa);
		
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
			startActivity(new Intent(this,CercaActivity.class));
			this.overridePendingTransition(R.anim.late_in_left, R.anim.zero);		
		}finally{
			finish();
		}
	}
	
	protected void createCorseActivity() {
		try{
			startActivity(new Intent(this,CorseActivity.class));
			this.overridePendingTransition(R.anim.late_in_left, R.anim.zero);		
			}finally{
			finish();
		}
	}


	protected void createTariffeeActivity() {
		try{
			startActivity(new Intent(this,TariffeActivity.class));
			this.overridePendingTransition(R.anim.late_in_left, R.anim.zero);		
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
            nDialog.setCancelable(true);
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
            pDialog.setCancelable(true);
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
									TableRow nuovaRiga = new TableRow(getApplicationContext());
									
									TableLayout righeDellaCella = new TableLayout(getApplicationContext());
							
									TableRow nomePaese = new TableRow(getApplicationContext());
									TableRow nomeFermata = new TableRow(getApplicationContext());
									TableRow orarioFermata = new TableRow(getApplicationContext());

									TextView NOMEPAESE = new TextView(getApplicationContext());
									NOMEPAESE.setText(getString(R.string.nomePaeseFermata));
									NOMEPAESE.setTextColor(Color.BLACK);
									NOMEPAESE.setTextSize(15);
									NOMEPAESE.setTypeface(null,Typeface.BOLD);
									
									TextView nomePaeseText = new TextView(getApplicationContext());
									nomePaeseText.setTextColor(Color.BLACK);
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
								
									righeDellaCella.addView(nomePaese);
									righeDellaCella.addView(nomeFermata);
									righeDellaCella.addView(orarioFermata);
									
									nuovaRiga.setBackgroundResource(R.drawable.riga_corse);
									nuovaRiga.addView(righeDellaCella);
									
									HorizontalScrollView scrollRiga = new HorizontalScrollView(getApplicationContext());
									scrollRiga.setFillViewport(true);
									scrollRiga.addView(nuovaRiga);
									tableLayoutCorse.addView(scrollRiga);
								}
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
				}
        }
    }
}
