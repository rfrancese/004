package com.classi.server;

/**
 * Author :Raj Amal
 * Email  :raj.amalw@learn2crack.com
 * Website:www.learn2crack.com
 **/

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


public class UserFunctions {

    private JSONParser jsonParser;

    //URL of the PHP API
    private static String tariffeURL = "http://buonotouristunisa.altervista.org/buonotourist_script/";
    private static String tariffe_tag = "tariffe";
    
    private static String corseAndataRitornoURL = "http://buonotouristunisa.altervista.org/buonotourist_script/";
    private static String corseAndataRitorno_tag = "corseTotali";
    
    
    private static String ricercaCorseURL = "http://buonotouristunisa.altervista.org/buonotourist_script/";
    private static String ricercaCorse_tag = "cercaCorse";
    
    private static String dettaglioCorsaURL = "http://buonotouristunisa.altervista.org/buonotourist_script/";
    private static String dettaglioCorsa_tag = "dettaglioCorsa";
    
    private static String corseAndataRitornoDettagliURL = "http://buonotouristunisa.altervista.org/buonotourist_script/";
    private static String corseAndataRitornoDettagli_tag = "ARDettagli";
    
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * Funzione che ritorna costo biglietto e abbonamento
     **/
	public JSONObject caricaTariffe(String partenza, String destinazione) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", tariffe_tag));
        params.add(new BasicNameValuePair("partenza",partenza));
        params.add(new BasicNameValuePair("destinazione", destinazione));
        JSONObject json = jsonParser.getJSONFromUrl(tariffeURL, params);
        return json;
	}
	/**
     * Funzione che ritorna corse di Andata o di Ritorno
     **/
	public JSONObject caricaCorseAndataRitorno(String andataRitornoChar) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", corseAndataRitorno_tag));
        params.add(new BasicNameValuePair("andataRitorno",andataRitornoChar));
        JSONObject json = jsonParser.getJSONFromUrl(corseAndataRitornoURL, params);
        return json;
	}

	/**
     * Funzione che ricerca le corse in base ai parametri dati in input
     **/
	public JSONObject caricaCorseRicercate(String partenza,String destinazione, String orario, String andataRitorno) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag",ricercaCorse_tag));
        params.add(new BasicNameValuePair("provenienza",partenza));
        params.add(new BasicNameValuePair("destinazione",destinazione));
        params.add(new BasicNameValuePair("andataRitorno",andataRitorno));
        params.add(new BasicNameValuePair("orario",orario));
        JSONObject json = jsonParser.getJSONFromUrl(ricercaCorseURL, params);
        return json;
	}
	/**
     * Funzione che carica le fermate di una singola corsa
     **/
	public JSONObject caricaDettaglioCorsa(String codiceCorsaReale,String oraPartenzaCorsaReale, String andataRitornoCorsaReale) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag",dettaglioCorsa_tag));
        params.add(new BasicNameValuePair("codiceCorsaReale",codiceCorsaReale));
        params.add(new BasicNameValuePair("oraPartenzaCorsaReale",oraPartenzaCorsaReale));
        params.add(new BasicNameValuePair("andataRitornoCorsaReale",andataRitornoCorsaReale));
        JSONObject json = jsonParser.getJSONFromUrl(dettaglioCorsaURL, params);
        return json;
	}
	/**
     * Funzione che carica le corse di Andata o Ritorno di una specifica corsa
     **/
	public JSONObject caricaCorseAndataRitornoDettagli(String nomeCorsa,String codiceCorsaReale, String andataRitornoCorsaReale) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag",corseAndataRitornoDettagli_tag));
        params.add(new BasicNameValuePair("nomeCorsa",nomeCorsa));
        params.add(new BasicNameValuePair("codiceCorsaReale",codiceCorsaReale));
        params.add(new BasicNameValuePair("andataRitornoCorsaReale",andataRitornoCorsaReale));
        JSONObject json = jsonParser.getJSONFromUrl(corseAndataRitornoDettagliURL, params);
        return json;
	}
	
}

