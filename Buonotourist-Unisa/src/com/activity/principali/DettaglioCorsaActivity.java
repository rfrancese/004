package com.activity.principali;

import com.example.proveandroid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
        Toast.makeText(getApplicationContext(),nomeCorsa, Toast.LENGTH_SHORT).show();  
        Toast.makeText(getApplicationContext(),codiceCorsaReale, Toast.LENGTH_SHORT).show();  
        Toast.makeText(getApplicationContext(),oraPartenzaCorsaReale, Toast.LENGTH_SHORT).show();  
        Toast.makeText(getApplicationContext(),andataRitornoCorsaReale, Toast.LENGTH_SHORT).show();  

        //AVVIO LA CONNESSIO AD INTERNET
		TextView nomeCentrale = (TextView) findViewById(R.id.idTextViewCerca_NomeCorsa);
		nomeCentrale.setText(nomeCorsa);
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
}
