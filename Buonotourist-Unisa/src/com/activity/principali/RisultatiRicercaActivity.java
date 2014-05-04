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

public class RisultatiRicercaActivity extends Activity{
	String partenza;
	String destinazione;
	String orario;
	String andataRitorno;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.corse_cercate);
		// SETTO I LISTENER AGLI ELEMENTI CREATI CON XML(SOLO NAVBAR)
		settaListenerBottoniNavbar(savedInstanceState);
		Intent intentApplicazione=getIntent();
		partenza= intentApplicazione.getStringExtra("partenza");
		destinazione= intentApplicazione.getStringExtra("destinazione");
		orario= intentApplicazione.getStringExtra("orario");
		andataRitorno= intentApplicazione.getStringExtra("andataRitorno");
        //Toast.makeText(getApplicationContext(),partenza, Toast.LENGTH_SHORT).show();  
        //Toast.makeText(getApplicationContext(),destinazione, Toast.LENGTH_SHORT).show();  
        //Toast.makeText(getApplicationContext(),orario, Toast.LENGTH_SHORT).show();  
        //Toast.makeText(getApplicationContext(),andataRitorno, Toast.LENGTH_SHORT).show();  

        //AVVIO LA CONNESSIO AD INTERNET
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
 * Async Task che controlla se la rete � disponibile
 **/

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(RisultatiRicercaActivity.this);
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
            pDialog = new ProgressDialog(RisultatiRicercaActivity.this);
            pDialog.setTitle(getString(R.string.contattoServer));
            pDialog.setMessage(getString(R.string.invioDati));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.caricaCorseRicercate(partenza,destinazione,orario,andataRitorno);
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
							            TableLayout tableLayoutCorse = (TableLayout) findViewById(R.id.idTableLayoutCerca_CorseRicercate);
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
											IDCORSA.setTextColor(Color.WHITE);
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
										
											righeDellaCella.addView(nomeCorsaRiga);
											righeDellaCella.addView(oraPartenzaRiga);
											righeDellaCella.addView(andataRitornoRiga);
											righeDellaCella.addView(cliccaQuiPerIDettagli);
											
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
    	try{
			Intent newIntent = new Intent(this,DettaglioCorsaActivity.class);
			newIntent.putExtra("nomeCorsa",nomeCorsa );
			newIntent.putExtra("codiceCorsaReale",codiceCorsaReale);
			newIntent.putExtra("oraPartenzaCorsaReale", oraPartenzaCorsaReale.substring(0,oraPartenzaCorsaReale.indexOf("(")));
			newIntent.putExtra("andataRitornoCorsaReale",andataRitornoCorsaReale);
			newIntent.putExtra("paeseFermata", partenza);
			startActivity(newIntent);
			this.overridePendingTransition(R.anim.late_in_left, R.anim.zero);		
		}finally{
			finish();
		};	
	}
}