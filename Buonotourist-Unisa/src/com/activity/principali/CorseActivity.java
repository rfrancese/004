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
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class CorseActivity extends Activity {
	private String andataRitornoCarattere;
	private boolean flag=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.corse);
		// SETTO I LISTENER AGLI ELEMENTI CREATI CON XML
		settaListenerBottoniNavbar(savedInstanceState);
		settaListnerBottoneAndataRitorno(savedInstanceState);
		if(flag){
			TextView andataRitorno = (TextView) findViewById(R.id.idTextViewCorse_AndataRitorno);
			andataRitorno.setText(getString(R.string.ritorno));
			andataRitornoCarattere="R";
		}else{
			TextView andataRitorno = (TextView) findViewById(R.id.idTextViewCorse_AndataRitorno);
			andataRitorno.setText(getString(R.string.andata));
			andataRitornoCarattere="A";
		}
		NetAsync();  //CARICA LE CORSE DAL DATABASE
		
	}
	private void settaListnerBottoneAndataRitorno(final Bundle savedInstanceState) {
		ImageButton andataRitornoImmagine = (ImageButton) findViewById(R.id.idBottone_navbar_imageButton);
		andataRitornoImmagine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				flag=!flag;
				onCreate(savedInstanceState);
			}
		});
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
				onCreate(savedInstanceState);
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
			startActivity(new Intent(this,TariffeActivity.class));
			this.overridePendingTransition(R.anim.late_in_left, R.anim.zero);		
		}finally{
			finish();
		}
	}
	protected void createCercaActivity() {
		try{
			startActivity(new Intent(this,CercaActivity.class));
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
            nDialog = new ProgressDialog(CorseActivity.this);
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

        String andataRitornoChar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
			TextView andataRitorno = (TextView) findViewById(R.id.idTextViewCorse_AndataRitorno);
			if((andataRitorno.getText().toString()).compareTo(getString(R.string.andata)) == 0){
				andataRitornoChar="A";
			}else{
				andataRitornoChar="R";
			}
            pDialog = new ProgressDialog(CorseActivity.this);
            pDialog.setTitle(getString(R.string.contattoServer));
            pDialog.setMessage(getString(R.string.invioDati));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.caricaCorseAndataRitorno(andataRitornoChar);
            return json;
        }


		@Override
        protected void onPostExecute(JSONObject json) {
				try {
					if (json.getString("success") != null) {
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
											String nomeCorsa= ((TextView)((TableRow)((TableLayout)nuovaRiga.getChildAt(0)).getChildAt(1)).getChildAt(0)).getText().toString();
											String codiceCorsaReale= ((TextView)((TableRow)((TableLayout)nuovaRiga.getChildAt(0)).getChildAt(1)).getChildAt(1)).getText().toString();
											apriAttivitaCorseAndataRitorno(nomeCorsa,codiceCorsaReale);
										}		
									});	
									
									TableLayout righeDellaCella = new TableLayout(getApplicationContext());
									TableRow  riga1= new TableRow(getApplicationContext());
									TableRow  riga2 = new TableRow(getApplicationContext());
									
									TextView CORSA = new TextView(getApplicationContext());
									CORSA.setText(getString(R.string.nomeCorsa)); 
									CORSA.setTextColor(Color.BLACK);
									CORSA.setTextSize(15);
									CORSA.setTypeface(null,Typeface.BOLD);
									
									
									TextView nomeCorsa = new TextView(getApplicationContext());
									nomeCorsa.setTextColor(Color.BLACK);
									nomeCorsa.setTextSize(15);
									nomeCorsa.setText(json_riga.getString("NomeCorsa"));

									TextView IDCORSA = new TextView(getApplicationContext());
									IDCORSA.setText(json_riga.getString("CodiceCorsa")); 
									IDCORSA.setTextColor(Color.WHITE);
									IDCORSA.setTextSize(15);
									
									
									riga1.addView(CORSA);
									riga2.addView(nomeCorsa);
									riga2.addView(IDCORSA);
									
									righeDellaCella.addView(riga1);
									righeDellaCella.addView(riga2);
									
									nuovaRiga.addView(righeDellaCella);
									nuovaRiga.setBackgroundResource(R.drawable.riga_corse);
									
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
				            Toast.makeText(getApplicationContext(),"JSON SUCCESS NOT 1(IMPOSSIBILE)", Toast.LENGTH_SHORT).show();            
					   }
				} catch (NumberFormatException e) {
		            Toast.makeText(getApplicationContext(),"ERROR NUMBER FORMAT", Toast.LENGTH_SHORT).show();            
				} catch (JSONException e) {
		            Toast.makeText(getApplicationContext(),"ERROR JSON SUCCESS O INTERNI(IMPOSSIBILE)", Toast.LENGTH_SHORT).show();            
				}
        }
    }
    
    private void apriAttivitaCorseAndataRitorno(String nomeCorsa,String codiceCorsaReale) {
    	try{
			Intent newIntent = new Intent(this,CorseAndataRitornoDettagliActivity.class);
			newIntent.putExtra("nomeCorsa",nomeCorsa );
			newIntent.putExtra("codiceCorsaReale",codiceCorsaReale);
			newIntent.putExtra("andataRitornoCorsaReale",andataRitornoCarattere);
			startActivity(newIntent);
			this.overridePendingTransition(R.anim.late_in_left, R.anim.zero);		
		}finally{
			finish();
		};	
	}
}