package com.classi.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class GestioneTariffe {
	private JSONParser jsonParser;

	    //URL of the PHP API
	    private static String tariffeURL = "http://buonotouristunisa.altervista.org/buonotourist_script/";
	    private static String tariffe_tag = "tariffe";
	    
    public GestioneTariffe(){
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
}
