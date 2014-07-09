package com.classi.server;

import org.json.JSONObject;

public interface UserFunctionsFacade {
	public JSONObject caricaTariffe(String partenza, String destinazione);
	/**
     * Funzione che ritorna corse di Andata o di Ritorno
     **/
	public JSONObject caricaCorseAndataRitorno(String andataRitornoChar);

	/**
     * Funzione che ricerca le corse in base ai parametri dati in input
     **/
	public JSONObject caricaCorseRicercate(String partenza,String destinazione, String orario, String andataRitorno);

	/**
     * Funzione che carica le fermate di una singola corsa
     **/
	public JSONObject caricaDettaglioCorsa(String codiceCorsaReale,String oraPartenzaCorsaReale, String andataRitornoCorsaReale);
	
	/**
     * Funzione che carica le corse di Andata o Ritorno di una specifica corsa
     **/
	public JSONObject caricaCorseAndataRitornoDettagli(String nomeCorsa,String codiceCorsaReale, String andataRitornoCorsaReale);
	
	/**
     * Funzione che ritorna la fermata piu' vicina in base alla latitudine e longitudine dati(CALCOLATA IN LINEA D'ARIA)
     **/
	public JSONObject trovaFermataPiuVicina(double latitudinePosition,double longitudinePosition);
}
