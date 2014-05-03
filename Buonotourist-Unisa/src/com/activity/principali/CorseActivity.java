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
		}else{
			TextView andataRitorno = (TextView) findViewById(R.id.idTextViewCorse_AndataRitorno);
			andataRitorno.setText(getString(R.string.andata));
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
 * Async Task che controlla se la rete è disponibile
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
            pDialog.setCancelable(true);
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
									TableRow nuovaRiga = new TableRow(getApplicationContext());
									
									TextView nomeCorsa = new TextView(getApplicationContext());
									nomeCorsa.setTextColor(Color.BLACK);
									nomeCorsa.setTextSize(15);
									nomeCorsa.setText(json_riga.getString("NomeCorsa"));
									TableLayout righeDellaCella = new TableLayout(getApplicationContext());
									TableRow  nome1= new TableRow(getApplicationContext());
									TableRow nome = new TableRow(getApplicationContext());
									
									
									
									TextView CORSA = new TextView(getApplicationContext());
									CORSA.setText(getString(R.string.nomeCorsa)); 
									CORSA.setTextColor(Color.BLACK);
									CORSA.setTextSize(15);
									CORSA.setTypeface(null,Typeface.BOLD);
									
									nome1.addView(CORSA);
									nome.addView(nomeCorsa);
									righeDellaCella.addView(nome1);
									righeDellaCella.addView(nome);
									nuovaRiga.addView(righeDellaCella);
									
									nuovaRiga.setBackgroundResource(R.drawable.riga_corse);
									//nuovaRiga.setBackgroundDrawable(getResources().getDrawable(R.drawable.riga_corse));
									//nuovaRiga.setBackground( getResources().getDrawable(R.drawable.riga_corse));
									
									
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
				            Toast.makeText(getApplicationContext(),"JSON NULL:", Toast.LENGTH_SHORT).show();            
					   }
				} catch (NumberFormatException e) {
		            Toast.makeText(getApplicationContext(),"ERROR NUMBER FORMAT", Toast.LENGTH_SHORT).show();            
				} catch (JSONException e) {
		            Toast.makeText(getApplicationContext(),"ERROR JSON SUCCESS", Toast.LENGTH_SHORT).show();            
				}
        }
    }
    
    
}