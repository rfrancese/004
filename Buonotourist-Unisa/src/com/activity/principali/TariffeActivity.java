package com.activity.principali;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.classi.server.UserFunctions;
import com.example.proveandroid.R;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TariffeActivity extends Activity {

	private static final int TariffaDa = 1;
	private static final int TariffaA = 2;
	private String sceltaTariffaDa;
	private String sceltaTariffaA;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tariffe);
		
		
		View widgetTariffaDa=findViewById(R.id.idBottoni_TariffaDa);
		View widgetTariffaA=findViewById(R.id.idBottoni_TariffaA);
		
		widgetTariffaDa.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(TariffaDa);
			}
		});
		
		
		widgetTariffaA.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(TariffaA);
			}
		});
		
		
		
		
		// SETTO I LISTENER AGLI ELEMENTI CREATI CON XML
		settaListenerBottoniNavbar(savedInstanceState);
		settaListenerBottoniForm(savedInstanceState);	
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
				onCreate(savedInstanceState);
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
	
	private void settaListenerBottoniForm(final Bundle savedInstanceState) {
		Button buttonCercaForm =(Button)findViewById(R.id.idBottoniFormTariffe_Cerca);
		Button buttonAnnullaForm =(Button)findViewById(R.id.idBottoniFormTariffe_Annulla);
		
		buttonAnnullaForm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			 onCreate(savedInstanceState);
			}
		});
		buttonCercaForm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Button inputPartenza = (Button) findViewById(R.id.idBottoni_TariffaDa);
	            Button inputDestinazione = (Button) findViewById(R.id.idBottoni_TariffaA);
	            String partenza = inputPartenza.getText().toString();
	            String destinazione = inputDestinazione.getText().toString();
	            if( partenza.compareTo(getString(R.string.partenza)) !=0 && destinazione.compareTo(getString(R.string.destinazione))!= 0 ){
	            	 NetAsync(v);
	            }else{
		            Toast.makeText(getApplicationContext(),getString(R.string.datiNonValidi), Toast.LENGTH_SHORT).show();            
	            }
               
			}
		});
	}

	
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
			switch (id) {
				case TariffaDa:
					dialog = TariffaDa();
					break;
				case TariffaA:
					dialog = TariffaA();
					break;
				  
				default:
					dialog = null;
					break;
				}
		return dialog;
		}
	
public Dialog TariffaDa(){
		
		final String[] options = { "Nola", "Sarno", "Caserta", "Palma Campania", "San Paolo Bel Sito" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Tariffa Da:");
		builder.setSingleChoiceItems(options, 2, new DialogInterface.OnClickListener() {
		@Override
		
			public void onClick(DialogInterface dialog, int which) {
			sceltaTariffaDa = options[which];
			}
		});
		
		builder.setCancelable(false);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		@Override
			public void onClick(DialogInterface dialog, int which) {
			Button widgetPartenza=(Button)findViewById(R.id.idBottoni_TariffaDa);
			widgetPartenza.setText(sceltaTariffaDa);
				dismissDialog(TariffaDa);;
			}
		});
		
		builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
		       dismissDialog(TariffaDa);
		}
		});
		AlertDialog alert = builder.create();
		return alert;
	}
	
	
public Dialog TariffaA(){
		
		final String[] options = {"Fisciano","Lancusi"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Tariffa A:");
		builder.setSingleChoiceItems(options, 2, new DialogInterface.OnClickListener() {
		@Override
		
			public void onClick(DialogInterface dialog, int which) {
			sceltaTariffaA = options[which];
			}
		});
		
		builder.setCancelable(false);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		@Override
			public void onClick(DialogInterface dialog, int which) {
			Button widgetPartenza=(Button)findViewById(R.id.idBottoni_TariffaA);
			widgetPartenza.setText(sceltaTariffaA);
				dismissDialog(TariffaA);
			}
		});
		
		builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
		       dismissDialog(TariffaA);
		}
		});
		AlertDialog alert = builder.create();
		return alert;
	}
	
	public void NetAsync(View view){
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
            nDialog = new ProgressDialog(TariffeActivity.this);
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


        private static final String KEY_SUCCESS = "success";

		private ProgressDialog pDialog;

        String partenza,destinazione;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Button inputPartenza = (Button) findViewById(R.id.idBottoni_TariffaDa);
            Button inputDestinazione = (Button) findViewById(R.id.idBottoni_TariffaA);
            partenza = inputPartenza.getText().toString();
            destinazione = inputDestinazione.getText().toString();
            pDialog = new ProgressDialog(TariffeActivity.this);
            pDialog.setTitle(getString(R.string.contattoServer));
            pDialog.setMessage(getString(R.string.invioDati));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.caricaTariffe(partenza,destinazione);
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
					            JSONObject json_costi = json.getJSONObject("costi");
					            TextView abbonamento = (TextView)findViewById(R.id.idTextViewFormTariffe_Abbonamento);
					            abbonamento.setText(getString(R.string.costoA)+" "+json_costi.getString("costoAbbonamento") + "€");
					            abbonamento.setTextColor(Color.RED);
					            TextView biglietto = (TextView)findViewById(R.id.idTextViewFormTariffe_Biglietto);
					            biglietto.setText(getString(R.string.costoB)+" "+json_costi.getString("costoBiglietto") + "€");
					            biglietto.setTextColor(Color.RED);
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
