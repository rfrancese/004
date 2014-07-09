package com.classi.server;

import org.json.JSONObject;


public class UserFunctions implements UserFunctionsFacade{ 
    GestioneCorse corseManager;
    GestioneTariffe tariffeManager;
    // constructor
    public UserFunctions(){
        corseManager= new GestioneCorse();
        tariffeManager= new GestioneTariffe();
    }
    /**
     * Funzione che ritorna costo biglietto e abbonamento
     **/
	public JSONObject caricaTariffe(String partenza, String destinazione) {
       return tariffeManager.caricaTariffe(partenza, destinazione);
	}
	/**
     * Funzione che ritorna corse di Andata o di Ritorno
     **/
	public JSONObject caricaCorseAndataRitorno(String andataRitornoChar) {
		return corseManager.caricaCorseAndataRitorno(andataRitornoChar);
	}

	/**
     * Funzione che ricerca le corse in base ai parametri dati in input
     **/
	public JSONObject caricaCorseRicercate(String partenza,String destinazione, String orario, String andataRitorno) {
		return corseManager.caricaCorseRicercate(partenza, destinazione, orario, andataRitorno);
	}
	/**
     * Funzione che carica le fermate di una singola corsa
     **/
	public JSONObject caricaDettaglioCorsa(String codiceCorsaReale,String oraPartenzaCorsaReale, String andataRitornoCorsaReale) {
		return corseManager.caricaDettaglioCorsa(codiceCorsaReale, oraPartenzaCorsaReale, andataRitornoCorsaReale);
	}
	/**
     * Funzione che carica le corse di Andata o Ritorno di una specifica corsa
     **/
	public JSONObject caricaCorseAndataRitornoDettagli(String nomeCorsa,String codiceCorsaReale, String andataRitornoCorsaReale) {
		return corseManager.caricaCorseAndataRitornoDettagli(nomeCorsa, codiceCorsaReale, andataRitornoCorsaReale);
	}
	/**
     * Funzione che ritorna la fermata piu' vicina in base alla latitudine e longitudine dati(CALCOLATA IN LINEA D'ARIA)
     **/
	public JSONObject trovaFermataPiuVicina(double latitudinePosition,double longitudinePosition) {
		return corseManager.trovaFermataPiuVicina(latitudinePosition, longitudinePosition);
	}
	
}

