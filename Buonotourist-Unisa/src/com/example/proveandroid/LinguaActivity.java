package com.example.proveandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
public class LinguaActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lingua);
		// SETTO I LISTENER AGLI ELEMENTI CREATI CON XML
		Button buttonCercaNavbar =(Button)findViewById(R.id.idBottoniNavbar_Cerca);
		Button buttonCorseNavbar =(Button)findViewById(R.id.idBottoniNavbar_Corse);
		Button buttonTariffeNavbar =(Button)findViewById(R.id.idBottoniNavbar_Tariffe);
		Button buttonLinguaNavbar =(Button)findViewById(R.id.idBottoniNavbar_Lingua);
		settaListenerBottoniNavbar(buttonCercaNavbar,buttonCorseNavbar,buttonTariffeNavbar,buttonLinguaNavbar,savedInstanceState);
		Button buttonConfermaForm =(Button)findViewById(R.id.idBottoniFormLingua_Conferma);
		settaListenerBottoniForm(buttonConfermaForm);	
	
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
				createTariffeActivity();
			}
		});
		buttonLinguaNavbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onCreate(savedInstanceState);
			}
		});
	}

	protected void createTariffeActivity() {
		startActivity(new Intent(this,TariffeActivity.class));
	}
	protected void createCercaActivity() {
		startActivity(new Intent(this,CercaActivity.class));
	}
	protected void createCorseActivity() {
		startActivity(new Intent(this,CorseActivity.class));
	}
	
	
	private void settaListenerBottoniForm(Button buttonConfermaForm) {
		buttonConfermaForm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//FARA' QUALCOSA PER CAMBIARE LINGUA ( MAGARI FILE STRING.XML)
			}
		});
	}

}