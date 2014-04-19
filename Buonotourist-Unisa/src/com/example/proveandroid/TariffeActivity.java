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
		Button buttonCercaNavbar =(Button)findViewById(R.id.idBottoniNavbar_Cerca);
		Button buttonCorseNavbar =(Button)findViewById(R.id.idBottoniNavbar_Corse);
		Button buttonTariffeNavbar =(Button)findViewById(R.id.idBottoniNavbar_Tariffe);
		Button buttonLinguaNavbar =(Button)findViewById(R.id.idBottoniNavbar_Lingua);
		settaListenerBottoniNavbar(buttonCercaNavbar,buttonCorseNavbar,buttonTariffeNavbar,buttonLinguaNavbar,savedInstanceState);
		Button buttonCercaForm =(Button)findViewById(R.id.idBottoniFormTariffe_Cerca);
		Button buttonAnnullaForm =(Button)findViewById(R.id.idBottoniFormTariffe_Annulla);
		settaListenerBottoniForm(buttonCercaForm,buttonAnnullaForm,savedInstanceState);	
	
	}
	private void settaListenerBottoniNavbar(Button buttonCercaNavbar,Button buttonCorseNavbar, Button buttonTariffeNavbar,Button buttonLinguaNavbar, final Bundle savedInstanceState) {
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
		startActivity(new Intent(this,CercaActivity.class));
	}
	protected void createCorseActivity() {
		startActivity(new Intent(this,CorseActivity.class));
	}

	protected void createLinguaActivity() {
		startActivity(new Intent(this,LinguaActivity.class));
	}
	
	
	private void settaListenerBottoniForm(Button buttonCercaForm,Button buttonAnnullaForm, final Bundle savedInstanceState) {
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
