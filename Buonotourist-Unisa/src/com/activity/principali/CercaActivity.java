package com.activity.principali;


import com.example.proveandroid.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;

public class CercaActivity extends Activity  {    
	private static final int PartenzaDa = 1;
	private static final int ArrivoA = 2;
	private static final int TimePiker = 3;
	private String sceltaPartenzaDa="";
	private String sceltaArrivoA="";
	private int hour;
    private int minute;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cerca);
		
		
		
		// SETTO I LISTENER AGLI ELEMENTI CREATI CON XML
		settaListenerBottoniNavbar(savedInstanceState);
		settaListenerBottoniForm(savedInstanceState);
		
		View widgetPartenza=findViewById(R.id.idBottoni_Provenienza);
		View widgetArrivo=findViewById(R.id.idBottoni_Destinazione);
		View widgetOrario=findViewById(R.id.idBottoni_Orario);
		
		
		widgetPartenza.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(PartenzaDa);
			}
		});
		
		
		widgetArrivo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(ArrivoA);
			}
		});
		
		
		widgetOrario.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(TimePiker);
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
				 onCreate(savedInstanceState);
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
	
	
	private void settaListenerBottoniForm(final Bundle savedInstanceState) {
		Button buttonCercaForm =(Button)findViewById(R.id.idBottoniFormCerca_Cerca);
		Button buttonAnnullaForm =(Button)findViewById(R.id.idBottoniFormCerca_Annulla);
		buttonAnnullaForm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					 onCreate(savedInstanceState);
			}
		});
		buttonCercaForm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createRisultatiRicercaActivity();
			}
		});
	}
	
	protected void createRisultatiRicercaActivity() {
		try{
			Intent newIntent = new Intent(this,RisultatiRicercaActivity.class);
			Button partenza = (Button)findViewById(R.id.idBottoni_Provenienza);
			Button destinazione = (Button)findViewById(R.id.idBottoni_Destinazione);
			Button orario=(Button) findViewById(R.id.idBottoni_Orario);
			newIntent.putExtra("partenza",partenza.getText().toString() );
			newIntent.putExtra("destinazione", destinazione.getText().toString());
			newIntent.putExtra("orario", orario.getText().toString());
			if( partenza.getText().toString().compareTo("Fisciano") == 0 &&  partenza.getText().toString().compareTo("Lancusi") == 0 ){
				newIntent.putExtra("andataRitorno", "R");
			}else{
				newIntent.putExtra("andataRitorno", "A");
			}
			startActivity(newIntent);
			this.overridePendingTransition(R.anim.late_in_left, R.anim.zero);		
		}finally{
			finish();
		};		
	}
	
	//sovrascrivo il metodo di activity ,e viene richiamato quando viene creata la prima volta la finestra di dialogo per associargli un id
		protected Dialog onCreateDialog(int id) {
			Dialog dialog;
				switch (id) {
					case PartenzaDa:
						dialog = createDa();
						break;
					case ArrivoA:
						dialog = createA();
						break;
					case TimePiker:
				            return new TimePickerDialog(this, timePickerListener, hour, minute,false);
				    default:
						dialog = null;
						break;
					}
			return dialog;
			}
		 private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
	         
			 
		        @Override
		        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
		            // TODO Auto-generated method stub
		            hour   = hourOfDay;
		            minute = minutes;
		 
		            updateTime(hour,minute);
		             
		         }
		 
		    };
		 // finestra di dialogo del primo bottone
			public Dialog createDa(){
			
				final String[] options = { "Nola", "Sarno", "Caserta", "Palma Campania", "San Paolo Bel Sito" };
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Partenza Da:");
				builder.setSingleChoiceItems(options, 190, new DialogInterface.OnClickListener() {
				@Override
				
					public void onClick(DialogInterface dialog, int which) {
					sceltaPartenzaDa = options[which];
					}
				});
				
				builder.setCancelable(false);
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
					public void onClick(DialogInterface dialog, int which) {
					Button widgetPartenza=(Button)findViewById(R.id.idBottoni_Provenienza);
					
					if(sceltaPartenzaDa=="")
					{
						widgetPartenza.setText(getString(R.string.partenza));
						dismissDialog(PartenzaDa);
					}
					else
					{
				    	widgetPartenza.setText(sceltaPartenzaDa);
				    	dismissDialog(PartenzaDa);
					}	
					}
				});
				
				builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				       dismissDialog(PartenzaDa);
				}
				});
				AlertDialog alert = builder.create();
				return alert;
			}
			
			
			//finestra dialogo del secondo bottone
			public Dialog createA(){
				
				final String[] options = {"Fisciano","Lancusi"};
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Arrivo A:");
				
				builder.setSingleChoiceItems(options, 2, new DialogInterface.OnClickListener() {
				@Override
				
					public void onClick(DialogInterface dialog, int which) {
					sceltaArrivoA = options[which];
					}
				});
				
				builder.setCancelable(false);
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
					public void onClick(DialogInterface dialog, int which) {
					Button widgetPartenza=(Button)findViewById(R.id.idBottoni_Destinazione);
					if(sceltaArrivoA=="")
					{
						widgetPartenza.setText(getString(R.string.destinazione));
						dismissDialog(ArrivoA);
					}
					else
					{
				    	widgetPartenza.setText(sceltaArrivoA);
				    	dismissDialog(ArrivoA);
					}	
					}
				});
				
				builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				       dismissDialog(ArrivoA);
				}
				});
				AlertDialog alert = builder.create();
				return alert;
			}
			
			//gestisce il tempo
			private void updateTime(int hours, int mins) {
		        
		        String timeSet = "";
		        if (hours > 12) {
		            hours -= 12;
		            timeSet = "PM";
		        } else if (hours == 0) {
		            hours += 12;
		            timeSet = "AM";
		        } else if (hours == 12)
		            timeSet = "PM";
		        else
		            timeSet = "AM";
		 
		         
		        String minutes = "";
		        if (mins < 10)
		            minutes = "0" + mins;
		        else
		            minutes = String.valueOf(mins);
		 
		        // Append in a StringBuilder
		         String aTime = new StringBuilder().append(hours).append(':')
		                .append(minutes).append(" ").append(timeSet).toString();
		 
		         
		       Button widgetOrario=(Button)findViewById(R.id.idBottoni_Orario);
		       widgetOrario.setText(aTime);
		    }


			
	
	
	
	
}
