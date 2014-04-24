package com.example.proveandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TariffeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tariffe);
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
		}finally{
			finish();
		}
	}
	protected void createCorseActivity() {
		try{
			startActivity(new Intent(this,CorseActivity.class));
		}finally{
			finish();
		}
	}

	protected void createLinguaActivity() {
		try{
			startActivity(new Intent(this,LinguaActivity.class));
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

}
