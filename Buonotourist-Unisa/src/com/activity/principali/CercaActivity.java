package com.activity.principali;


import com.example.proveandroid.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;

public class CercaActivity extends Activity  {    
	private static final int PartenzaDa = 1;
	private static final int ArrivoA = 2;
	private static final int TimePiker = 3;
	private static final int Alert = 4;
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
				Button Partenza=(Button)findViewById(R.id.idBottoni_Provenienza);
				Button Destinazione =(Button) findViewById(R.id.idBottoni_Destinazione);
				Button Orario =(Button) findViewById(R.id.idBottoni_Orario);
				String partenza=Partenza.getText().toString();
				String destinazione=Destinazione.getText().toString();
				String orario=Orario.getText().toString();	
				if(partenza.compareTo(getString(R.string.partenza))==0 || destinazione.compareTo(getString(R.string.destinazione))==0 || orario.compareTo(getString(R.string.orario))==0 )
				{
					showDialog(Alert);
					
				}
				else
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
			if( partenza.getText().toString().compareTo("Fisciano") == 0 ||  partenza.getText().toString().compareTo("Lancusi") == 0 ){
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
					case Alert:
						dialog = Alert();
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
			
				final String[] options = { "Fisciano","Lancusi","Nola", "Sarno", "Caserta", "Palma Campania", "San Paolo Bel Sito" };
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(getString(R.string.partenzaDa));
				builder.setSingleChoiceItems(options, 99999, new DialogInterface.OnClickListener() {
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
				
				builder.setNegativeButton(getString(R.string.annulla), new DialogInterface.OnClickListener() {
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
				
				final String[] options = {"Fisciano","Lancusi","Nola","Sarno"};
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(getString(R.string.arrivoA));
				
				builder.setSingleChoiceItems(options, 99999, new DialogInterface.OnClickListener() {
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
				
				builder.setNegativeButton(getString(R.string.annulla), new DialogInterface.OnClickListener() {
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
				 String aTime="";
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
		       
		        if(timeSet=="PM"){
		        // Append in a StringBuilder
		        if( hours==1)
		        {
		        	 aTime = new StringBuilder().append(13).append(':')
			                .append(minutes).append(':').append('0').append('0').toString();
			 
		        	
		        }
		        if( hours==2)
		        {
		        	 aTime = new StringBuilder().append(14).append(':')
			                .append(minutes).append(':').append('0').append('0').toString();
			 
		        	
		        }
		        if( hours==3)
		        {
		        	 aTime = new StringBuilder().append(15).append(':')
			                .append(minutes).append(':').append('0').append('0').toString();
			 
		        	
		        }
		        if( hours==4)
		        {
		        	 aTime = new StringBuilder().append(16).append(':')
			                .append(minutes).append(':').append('0').append('0').toString();
			 
		        	
		        }
		        if(hours==5)
		        {
		        	 aTime = new StringBuilder().append(17).append(':')
			                .append(minutes).append(':').append('0').append('0').toString();
			 
		        	
		        }
		        if(hours==6)
		        {
		        	 aTime = new StringBuilder().append(18).append(':')
			                .append(minutes).append(':').append('0').append('0').toString();
			 
		        	
		        }
		        if(hours==7)
		        {
		        	 aTime = new StringBuilder().append(19).append(':')
			                .append(minutes).append(':').append('0').append('0').toString();
			 
		        	
		        }
		        if(hours==8)
		        {
		        	 aTime = new StringBuilder().append(20).append(':')
			                .append(minutes).append(':').append('0').append('0').toString();
			 
		        	
		        }
		        if(hours==9)
		        {
		        	 aTime = new StringBuilder().append(21).append(':')
			                .append(minutes).append(':').append('0').append('0').toString();
			 
		        	
		        }
		        if(hours==10)
		        {
		        aTime = new StringBuilder().append(22).append(':')
			                .append(minutes).append(':').append('0').append('0').toString();
			 
		        	
		        }
		        if(hours==11)
		        {
		        	 aTime = new StringBuilder().append(23).append(':')
			                .append(minutes).append(':').append('0').append('0').toString();
			 
		        	
		        }
		        if( hours==12)
		        {
		        	 aTime = new StringBuilder().append(24).append(':')
			                .append(minutes).append(':').append('0').append('0').toString();
			 
		        	
		        }
			}
		        else{
		          aTime = new StringBuilder().append(hours).append(':')
		                .append(minutes).append(':').append('0').append('0').toString();
		 
		        }
		        
		       Button widgetOrario=(Button)findViewById(R.id.idBottoni_Orario);
		       widgetOrario.setText(aTime);
		    }


			
			   //gestione rotazione metodo che si attiva ogni volta che tuoto il dispositivo
		    public void onConfigurationChanged(Configuration newConfig){
		    	super.onConfigurationChanged(newConfig);    
		    
		    }
		    public Dialog Alert(){
		    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    	builder.setTitle(getString(R.string.Attenzione));
		    	builder.setMessage(getString(R.string.Messaggio));
		    	builder.setCancelable(false);
		    	
		    	builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
		    	@Override
		    		public void onClick(DialogInterface dialog, int which) {
		    	// Annullato!
		    		dismissDialog(Alert);
		    		}
		    	});
		    	AlertDialog alert = builder.create();
		    	return alert;
		    }
	
	
}
