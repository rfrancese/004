package com.activity.principali;


import com.example.proveandroid.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
		Button buttonLinguaNavbar =(Button)findViewById(R.id.idBottoniNavbar_Lingua);
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
		buttonLinguaNavbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createLinguaActivity();
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

	protected void createLinguaActivity() {
		try{
			startActivity(new Intent(this,LinguaActivity.class));
			this.overridePendingTransition(R.anim.late_in_left, R.anim.zero);		
		}finally{
			finish();
		}	}
	
	
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
				//INTERROGHERA' IL DATABASE
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
	
	
	
}
