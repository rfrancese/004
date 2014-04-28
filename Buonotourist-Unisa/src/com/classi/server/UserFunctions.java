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

  

}
