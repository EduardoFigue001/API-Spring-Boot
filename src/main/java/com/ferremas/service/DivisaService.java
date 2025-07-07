
package com.ferremas.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

@Service
public class DivisaService {

    private final String API_BASE_URL = "https://mindicador.cl/api";
    
    // Mapeo de códigos de moneda a endpoints
    private final Map<String, String> MONEDAS_ENDPOINTS = Map.of(
        "USD", "/dolar",
        "EUR", "/euro", 
        "UF", "/uf",
        "UTM", "/utm",
        "DOLAR_INTERCAMBIO", "/dolar_intercambio"
    );
    
    // Valores de fallback para desarrollo/testing
    private final Map<String, Double> VALORES_FALLBACK = Map.of(
        "USD", 900.0,
        "EUR", 980.0,
        "UF", 36000.0,
        "UTM", 63000.0,
        "DOLAR_INTERCAMBIO", 895.0
    );

    public double obtenerValorDolar() {
        return obtenerValorMoneda("USD");
    }
    
    public double obtenerValorMoneda(String codigoMoneda) {
        try {
            String endpoint = MONEDAS_ENDPOINTS.get(codigoMoneda.toUpperCase());
            if (endpoint == null) {
                throw new IllegalArgumentException("Moneda no soportada: " + codigoMoneda);
            }
            
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(API_BASE_URL + endpoint, String.class);

            JSONObject json = new JSONObject(response);
            JSONArray serie = json.getJSONArray("serie");
            
            if (serie.length() == 0) {
                throw new RuntimeException("No hay datos disponibles para " + codigoMoneda);
            }
            
            JSONObject ultimo = serie.getJSONObject(0);
            double valor = ultimo.getDouble("valor");
            
            if (valor <= 0) {
                throw new RuntimeException("Valor inválido para " + codigoMoneda);
            }
            
            return valor;
        } catch (Exception e) {
            System.err.println("Error al obtener valor de " + codigoMoneda + " desde API externa: " + e.getMessage());
            System.err.println("Usando valor de fallback para " + codigoMoneda);
            
            // Usar valor de fallback
            Double valorFallback = VALORES_FALLBACK.get(codigoMoneda.toUpperCase());
            if (valorFallback != null) {
                return valorFallback;
            }
            
            throw new RuntimeException("Error al obtener valor de " + codigoMoneda + " y no hay valor de fallback disponible: " + e.getMessage());
        }
    }
    
    public Map<String, Object> obtenerTodasLasMonedas() {
        Map<String, Object> monedas = new HashMap<>();
        
        for (String codigo : MONEDAS_ENDPOINTS.keySet()) {
            try {
                double valor = obtenerValorMoneda(codigo);
                monedas.put(codigo, Map.of(
                    "codigo", codigo,
                    "valor", valor,
                    "nombre", obtenerNombreMoneda(codigo),
                    "estado", "disponible"
                ));
            } catch (Exception e) {
                System.err.println("Error al obtener " + codigo + ": " + e.getMessage());
                monedas.put(codigo, Map.of(
                    "codigo", codigo,
                    "error", "No disponible",
                    "mensaje", e.getMessage(),
                    "estado", "error"
                ));
            }
        }
        
        // Agregar CLP (peso chileno) que siempre es 1
        monedas.put("CLP", Map.of(
            "codigo", "CLP",
            "valor", 1.0,
            "nombre", "Peso Chileno",
            "estado", "disponible"
        ));
        
        return monedas;
    }
    
    private String obtenerNombreMoneda(String codigo) {
        return switch (codigo) {
            case "USD" -> "Dólar Estadounidense";
            case "EUR" -> "Euro";
            case "UF" -> "Unidad de Fomento";
            case "UTM" -> "Unidad Tributaria Mensual";
            case "DOLAR_INTERCAMBIO" -> "Dólar Intercambio";
            default -> codigo;
        };
    }

    // Conversiones desde CLP
    public double convertirCLPaUSD(double montoCLP) {
        double valorDolar = obtenerValorMoneda("USD");
        return montoCLP / valorDolar;
    }
    
    public double convertirCLPaEUR(double montoCLP) {
        double valorEuro = obtenerValorMoneda("EUR");
        return montoCLP / valorEuro;
    }
    
    // Conversiones hacia CLP
    public double convertirUSDaCLP(double montoUSD) {
        double valorDolar = obtenerValorMoneda("USD");
        return montoUSD * valorDolar;
    }
    
    public double convertirEURaCLP(double montoEUR) {
        double valorEuro = obtenerValorMoneda("EUR");
        return montoEUR * valorEuro;
    }
    
    // Conversión universal
    public Map<String, Object> convertirMoneda(double monto, String monedaOrigen, String monedaDestino) {
        try {
            double resultado;
            double tasaCambio;
            
            if (monedaOrigen.equals("CLP") && !monedaDestino.equals("CLP")) {
                // De CLP a otra moneda
                double valorDestino = obtenerValorMoneda(monedaDestino);
                resultado = monto / valorDestino;
                tasaCambio = 1.0 / valorDestino;
            } else if (!monedaOrigen.equals("CLP") && monedaDestino.equals("CLP")) {
                // De otra moneda a CLP
                double valorOrigen = obtenerValorMoneda(monedaOrigen);
                resultado = monto * valorOrigen;
                tasaCambio = valorOrigen;
            } else if (monedaOrigen.equals("CLP") && monedaDestino.equals("CLP")) {
                // CLP a CLP (sin conversión)
                resultado = monto;
                tasaCambio = 1.0;
            } else {
                // Entre dos monedas extranjeras (vía CLP)
                double valorOrigen = obtenerValorMoneda(monedaOrigen);
                double valorDestino = obtenerValorMoneda(monedaDestino);
                double montoCLP = monto * valorOrigen;
                resultado = montoCLP / valorDestino;
                tasaCambio = valorOrigen / valorDestino;
            }
            
            // Usar HashMap para crear un mapa mutable
            Map<String, Object> response = new HashMap<>();
            response.put("montoOriginal", monto);
            response.put("monedaOrigen", monedaOrigen);
            response.put("monedaDestino", monedaDestino);
            response.put("resultado", Math.round(resultado * 100.0) / 100.0);
            response.put("tasaCambio", Math.round(tasaCambio * 10000.0) / 10000.0);
            response.put("fecha", java.time.LocalDateTime.now().toString());
            response.put("estado", "exitoso");
            
            return response;
        } catch (Exception e) {
            System.err.println("Error en conversión de " + monedaOrigen + " a " + monedaDestino + ": " + e.getMessage());
            
            // Usar HashMap para crear un mapa mutable
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Error en conversión: " + e.getMessage());
            response.put("montoOriginal", monto);
            response.put("monedaOrigen", monedaOrigen);
            response.put("monedaDestino", monedaDestino);
            response.put("estado", "error");
            
            return response;
        }
    }
}
