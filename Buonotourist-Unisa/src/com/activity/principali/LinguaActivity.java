package com.activity.principali;

import com.example.proveandroid.R;

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
		settaListenerBottoniNavbar(savedInstanceState);
		settaListenerBottoniForm();	
	
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
	protected void createCorseActivity() {
		try{
			startActivity(new Intent(this,CorseActivity.class));
			this.overridePendingTransition(R.anim.late_in_left, R.anim.zero);		
		}finally{
			finish();
		}
	}
	
	
	private void settaListenerBottoniForm() {
		Button buttonConfermaForm =(Button)findViewById(R.id.idBottoniFormLingua_Conferma);
		buttonConfermaForm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//FARA' QUALCOSA PER CAMBIARE LINGUA ( MAGARI FILE STRING.XML)
			}
		});
	}

}