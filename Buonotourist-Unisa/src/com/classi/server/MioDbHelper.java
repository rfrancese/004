package com.classi.server;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MioDbHelper extends SQLiteOpenHelper {
	//Nome del database che vogliamo creare
	private static final String DB_NOME = "WIP";
	
	/**
	 * Numero della versione del database.
	 * 
	 * La numerazione della vesione del database deve iniziare dal numero 1.
	 * Quando viene specificata una nuova versione android useguirà la funzione onUpgrade.
	 */
	private static final int DB_VERSIONE = 1;

	public MioDbHelper(Context context) {
		super(context, DB_NOME, null, DB_VERSIONE);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/*
		 * Stringa contenente la sintassi SQL per la
		 * creazione della tabella RUBRICA
		 */
		String sql = "CREATE TABLE rubrica"; 
		sql += "(_id INTEGER PRIMARY KEY,";
		sql += "nome TEXT NOT NULL);";
		
		//Eseguiamo la query
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
