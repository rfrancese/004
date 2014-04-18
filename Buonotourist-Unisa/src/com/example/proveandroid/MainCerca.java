package com.example.proveandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainCerca extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final Button bottoneCerca=(Button)findViewById(R.id.idBottoni_cerca);
		if(bottoneCerca != null){
				bottoneCerca.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
						clickButtonCerca();
				}
	
				
			});
		}
		
		
	}
	private void clickButtonCerca() {
			Toast toast = Toast.makeText(this, "CERCA", Toast.LENGTH_LONG);
			toast.show();
	}
}