
package com.ferremas.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class DivisaService {

    private final String API_URL = "https://mindicador.cl/api/dolar";

    public double obtenerValorDolar() {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(API_URL, String.class);

        JSONObject json = new JSONObject(response);
        JSONArray serie = json.getJSONArray("serie");
        JSONObject ultimo = serie.getJSONObject(0);
        return ultimo.getDouble("valor");
    }

    public double convertirCLPaUSD(double montoCLP) {
        double valorDolar = obtenerValorDolar();
        return montoCLP / valorDolar;
    }
}
