package com.activity.principali;


import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;

import com.activity.principali.SimpleGestureFilter.SimpleGestureListener;
import com.classi.server.MioDbHelper;
import com.example.buonotouristunisa.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CercaActivity extends Activity implements SimpleGestureListener {
	 /** AB BANNER CHE INSERISCO */
	  private AdView adView;

	  /* ID UNITA PUBBLICITARIO */
	  private static final String AD_UNIT_ID = "ca-app-pub-9936535009091025/4159664194";

    private SimpleGestureFilter detector; 
	private MioDbHelper mMioDbHelper = null;
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
		setContentView(R.layout.layout_cerca);
		
		createAdModBanner();
		
		mMioDbHelper = new MioDbHelper(getApplicationContext());
		 delete();
		 riempiDB();
		
		// SETTO I LISTENER AGLI ELEMENTI CREATI CON XML
		settaListenerBottoniNavbar(savedInstanceState);
		settaListenerBottoniForm(savedInstanceState);
		settaChiamataBuonotourist();
		settaContattoEmail(savedInstanceState);
		settaListenerImageButtonLocalize();
		
		View widgetPartenza=findViewById(R.id.idBottoni_Provenienza);
		View widgetArrivo=findViewById(R.id.idBottoni_Destinazione);
		View widgetOrario=findViewById(R.id.idBottoni_Orario);
		
		
		widgetPartenza.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(PartenzaDa);
			}
		});
		
		
		widgetArrivo.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(ArrivoA);
			}
		});
		
		
		widgetOrario.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(TimePiker);
			}
		});
		
		detector = new SimpleGestureFilter(this,this); // GESTORE SWIPE
}

	private void settaChiamataBuonotourist() {
		TextView contattaBuono = (TextView) findViewById(R.id.idTextViewCerca_ChiamaBuonotourist);
		contattaBuono.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:081951761"));
				startActivity(callIntent);
			}
		});

	}

	private void settaListenerImageButtonLocalize(){
		ImageButton localizeButton = (ImageButton)findViewById(R.id.idBottone_navbar_geoLocalizeButton);
		localizeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createMapActivity();
			}
		});
	}
	private void settaContattoEmail(final Bundle savedInstanceState){
		TextView contattaci = (TextView) findViewById(R.id.idTextViewCerca_Email);
		contattaci.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					Intent intentM = Intent.parseUri("mailto:buonotouristunisa@altervista.org", Intent.URI_INTENT_SCHEME);
					startActivity(intentM);
				} catch (URISyntaxException e) {
		            Toast.makeText(getApplicationContext(),"Error mail", Toast.LENGTH_SHORT).show();            }
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
			this.overridePendingTransition(R.anim.anim_late_in_left, R.anim.anim_zero);		
			}finally{
			finish();
		}
	}


	protected void createTariffeeActivity() {
		try{
			startActivity(new Intent(this,TariffeActivity.class));
			this.overridePendingTransition(R.anim.anim_late_in_left, R.anim.anim_zero);		
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
		
			@SuppressWarnings("deprecation")
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
	protected void createMapActivity() {
			startActivity(new Intent(this,MapFragmentNearBusStop.class));
			this.overridePendingTransition(R.anim.anim_late_in_left, R.anim.anim_zero);		
	}
	protected void createRisultatiRicercaActivity() {
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
			this.overridePendingTransition(R.anim.anim_late_in_left, R.anim.anim_zero);			
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
					    Calendar cal = Calendar.getInstance(); 
			            int minute = cal.get(Calendar.MINUTE);
			            int hourofday = cal.get(Calendar.HOUR_OF_DAY);
			            
				            return new TimePickerDialog(this, timePickerListener, hourofday, minute,false);
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
		    	
				 ArrayList<String> options1=tuttiPaesi();
					
					final String options[]=new String[options1.size()];
					int lunghezza=options1.size();
					
					for(int i=0;i<lunghezza;i++){
						options[i]=options1.get(i);
					}
				//final String[] options = { "Fisciano","Lancusi","Nola", "Sarno", "Caserta", "Palma Campania", "San Paolo Bel Sito" };
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
				@SuppressWarnings("deprecation")
				@Override
					public void onClick(DialogInterface dialog, int which) {
					Button widgetPartenza=(Button)findViewById(R.id.idBottoni_Provenienza);
					
					if(sceltaPartenzaDa=="")
					{
						widgetPartenza.setText(getString(R.string.partenza));
						  
						dialog.dismiss();
						removeDialog(PartenzaDa);
					}
					else
					{
						Button widgetDestinazione=(Button)findViewById(R.id.idBottoni_Destinazione);
						if((sceltaPartenzaDa.compareTo("Fisciano")==0)||(sceltaPartenzaDa.compareTo("Lancusi")==0)){
							
					    	widgetDestinazione.setText(getString(R.string.destinazione));
							}
						else if((sceltaPartenzaDa.compareTo(sceltaArrivoA)==0) || (sceltaPartenzaDa.compareTo("Fisciano")!=0) || sceltaPartenzaDa.compareTo("Lancusi")!=0)
						    {
							  widgetDestinazione.setText(getString(R.string.destinazione));
							}
				    	widgetPartenza.setText(sceltaPartenzaDa);
				    	 
				    	dialog.dismiss();
				    	removeDialog(PartenzaDa);
					}	
					}
				});
				
				builder.setNegativeButton(getString(R.string.annulla), new DialogInterface.OnClickListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					removeDialog(PartenzaDa);
				}
				});
				AlertDialog alert = builder.create();
				return alert;
			}
			
			
			//finestra dialogo del secondo bottone
public Dialog createA(){
				
				final String[] options;
				Button widgetPartenza=(Button)findViewById(R.id.idBottoni_Provenienza);
				
				if((widgetPartenza.getText().toString().compareTo("Fisciano")==0) || (widgetPartenza.getText().toString().compareTo("Lancusi")==0)){
					    
				          ArrayList<String> options1=tuttoTranneFiscianoLancusi();
				          options=new String[options1.size()];
				          int lunghezza=options1.size();
				
				     for(int i=0;i<lunghezza;i++){
					        options[i]=options1.get(i);
				      }
			     }
				else
				{
				     options=soloUniversita();
				}
					
				
				//Toast.makeText(getApplicationContext(),options[0], Toast.LENGTH_SHORT).show();
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(getString(R.string.arrivoA));
				
				builder.setSingleChoiceItems(options, 999999, new DialogInterface.OnClickListener() {
				@Override
				
					public void onClick(DialogInterface dialog, int which) {
					sceltaArrivoA = options[which];
					}
				});
				
				builder.setCancelable(false);
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@SuppressWarnings("deprecation")
				@Override
					public void onClick(DialogInterface dialog, int which) {
					Button widgetPartenza=(Button)findViewById(R.id.idBottoni_Destinazione);
					if(sceltaArrivoA=="")
					{
						widgetPartenza.setText(getString(R.string.destinazione));
						dialog.dismiss();
						 
						removeDialog(ArrivoA);
					}
					else
					{
				    	widgetPartenza.setText(sceltaArrivoA);
				    
				    	dialog.dismiss();
				    	removeDialog(ArrivoA);
					}	
					}
				});
				
				builder.setNegativeButton(getString(R.string.annulla), new DialogInterface.OnClickListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					dialog.dismiss();
				       removeDialog(ArrivoA);
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


			
			
		    public Dialog Alert(){
		    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    	builder.setTitle(getString(R.string.Attenzione));
		    	builder.setMessage(getString(R.string.Messaggio));
		    	builder.setCancelable(false);
		    	
		    	builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
		    	@SuppressWarnings("deprecation")
				@Override
		    		public void onClick(DialogInterface dialog, int which) {
		    	// Annullato!
		    		dismissDialog(Alert);
		    		}
		    	});
		    	AlertDialog alert = builder.create();
		    	return alert;
		    }
		    
		    
		    
		    //riempo la tabella con tutti i paesi
		    public void riempiDB(){
		    	SQLiteDatabase db = mMioDbHelper.getWritableDatabase();
		    	ContentValues values= new ContentValues();
		    	values.put("nome", "Fisciano");
                db.insert("rubrica", null, values);
		    	values.put("nome", "Lancusi");
		    	db.insert("rubrica", null, values);
		    	values.put("nome", "Sicignano Scalo");
		    	db.insert("rubrica", null, values);
		    	values.put("nome", "Perrazze");
		    	db.insert("rubrica", null, values);
		    	values.put("nome", "Contursi");
		    	db.insert("rubrica", null, values);
		    	values.put("nome", "Eboli");
		    	db.insert("rubrica", null, values);
		    	values.put("nome", "Caserta");
		    	db.insert("rubrica", null, values);
		    	values.put("nome", "Castel Cisterna");
		    	db.insert("rubrica", null, values);
		    	values.put("nome", "S.Gennaro Vesuviano");
		    	db.insert("rubrica", null, values);
		    	values.put("nome", "Sarno");
		    	db.insert("rubrica", null, values);
		    	values.put("nome", "Pomigliano");
		    	db.insert("rubrica", null, values);
		    	values.put("nome", "Nola");
		    	db.insert("rubrica", null, values);
		    	values.put("nome", "Lauro");
		    	db.insert("rubrica", null, values);
		    	values.put("nome", "Boscoreale");
		    	db.insert("rubrica", null, values);
		    	values.put("nome", "Pompei");
		    	db.insert("rubrica", null, values);
		    	values.put("nome", "Nocera");
		    	db.insert("rubrica", null, values);
		    	values.put("nome", "Roccarainola");
		        db.insert("rubrica", null, values);
		    }
		    
		    
		    
		    //mi faccio dare tutti i record
		    private ArrayList<String> tuttiPaesi() {
		    
		    	SQLiteDatabase db = mMioDbHelper.getReadableDatabase();
		    	ArrayList<String> paesi =new ArrayList<String>();
		    	
		        final String sql = "SELECT * FROM rubrica order by nome";
		    	
		    	Cursor c = db.rawQuery(sql, null);
		    	
		    	while(c.moveToNext()) { 
		    		
		    		paesi.add(c.getString(1));
		    		
		    	}
		    	//Toast.makeText(getApplicationContext(),""+paesi.size(), Toast.LENGTH_SHORT).show();
		    	return paesi;
		    }
		    
		    //mi faccio dare solo Fisciano e Lancusi
		    private String[] soloUniversita() {
		        SQLiteDatabase db = mMioDbHelper.getReadableDatabase();
                String[] universita=new String[2];
		    	
		    	final String sql = "SELECT * FROM rubrica where _id=1 or _id=2 order by nome";
		    	int i=0;
		    	Cursor c = db.rawQuery(sql, null);
		    	
		    	while(c.moveToNext()) { 
		    		universita[i]=c.getString(1);
		    		i++;
		    	}
		    	return universita;
		    }
		    
		    private ArrayList<String> tuttoTranneFiscianoLancusi() {
		    
		    	SQLiteDatabase db = mMioDbHelper.getReadableDatabase();
		    	ArrayList<String> paesi =new ArrayList<String>();
		    	
		    	final String sql = "SELECT * FROM rubrica where _id!=1 and _id!=2 order by nome";
		    	
		    	Cursor c = db.rawQuery(sql, null);
		    	
		    	while(c.moveToNext()) { 
		    		paesi.add(c.getString(1));
		    		}
		    	return paesi;
		    }
		    
		    //elimino tutti i record della tabella
		    public void delete(){
			   	SQLiteDatabase db = mMioDbHelper.getWritableDatabase();
				db.delete("rubrica", null, null);
			}
		    // -------------------->  ************** SWIPE
		    
		    @Override
		    public boolean dispatchTouchEvent(MotionEvent me){
		        // Call onTouchEvent of SimpleGestureFilter class
		         this.detector.onTouchEvent(me);
		       return super.dispatchTouchEvent(me);
		    }
		    @Override
		     public void onSwipe(int direction) {
		      switch (direction) {
		      
		      case SimpleGestureFilter.SWIPE_RIGHT : createTariffeeActivity();
		                                             break;
		      case SimpleGestureFilter.SWIPE_LEFT :  createCorseActivity();
		                                             break;   
		      }
		     }
		     @Override
		     public void onDoubleTap() {
		        //Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
		     }
		     
		     
		     // *************************** ADMOB METHOD
		     
		 	private void createAdModBanner() {
		 		 // Create an ad.
			    adView = new AdView(this);
			    adView.setAdSize(AdSize.BANNER);
			    adView.setAdUnitId(AD_UNIT_ID);
			    // Add the AdView to the view hierarchy. The view will have no size
			    // until the ad is loaded.
			    LinearLayout layout = (LinearLayout) findViewById(R.id.idLayout_BannerAdmob);
			    layout.removeAllViews();
			    layout.addView(adView);
			    // Create an ad request. Check logcat output for the hashed device ID to
			    // get test ads on a physical device.
			    final TelephonyManager tm =(TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
			    String deviceid = tm.getDeviceId();
			    AdRequest adRequest = new AdRequest.Builder()
			        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
			        .addTestDevice(deviceid)
			        .build();
			    // Start loading the ad in the background.
			    adView.loadAd(adRequest);
			}
		    //gestione rotazione metodo che si attiva ogni volta che tuoto il dispositivo
		    public void onConfigurationChanged(Configuration newConfig){
		    	super.onConfigurationChanged(newConfig); 
		    }	
		     @Override
		     public void onResume() {
		       super.onResume();
		       if (adView != null) {
		         adView.resume();
		       }
		     }
		     @Override
		     public void onPause() {
		       if (adView != null) {
		         adView.pause();
		       }
		       super.onPause();
		     }
		     /** Called before the activity is destroyed. */
		     @Override
		     public void onDestroy() {
		       // Destroy the AdView.
		       if (adView != null) {
		         adView.destroy();
		       }
		       super.onDestroy();
		     }
}
