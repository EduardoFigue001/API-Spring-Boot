package com.ferremas.controller;

import com.ferremas.service.DivisaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestController
@RequestMapping("/api/divisa")
@CrossOrigin(origins = "*")
@Tag(name = "Divisas", description = "API para conversión de divisas usando datos del Banco Central de Chile")
public class DivisaController {

    private static final Logger logger = LoggerFactory.getLogger(DivisaController.class);

    @Autowired
    private DivisaService divisaService;

    @Operation(summary = "Obtener valor actual del dólar", 
               description = "Retorna el valor actual del dólar estadounidense en pesos chilenos")
    @GetMapping("/dolar")
    public ResponseEntity<Map<String, Object>> obtenerValorDolar() {
        double valorDolar = divisaService.obtenerValorDolar();
        return ResponseEntity.ok(Map.of(
            "moneda", "USD",
            "valor", valorDolar,
            "unidad", "CLP",
            "fuente", "Banco Central de Chile",
            "fechaActualizacion", "Datos simulados para desarrollo"
        ));
    }

    @Operation(summary = "Convertir pesos chilenos a dólares", 
               description = "Convierte un monto en pesos chilenos a dólares estadounidenses")
    @GetMapping("/convertir")
    public ResponseEntity<Map<String, Object>> convertir(
            @Parameter(description = "Monto en pesos chilenos") @RequestParam double clp) {
        
        if (clp < 0) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "El monto debe ser mayor o igual a 0"
            ));
        }
        
        double valorDolar = divisaService.obtenerValorDolar();
        double montoUSD = divisaService.convertirCLPaUSD(clp);
        
        return ResponseEntity.ok(Map.of(
            "montoOriginal", Map.of(
                "valor", clp,
                "moneda", "CLP"
            ),
            "montoConvertido", Map.of(
                "valor", Math.round(montoUSD * 100.0) / 100.0,
                "moneda", "USD"
            ),
            "tipoCambio", valorDolar,
            "fechaConversion", java.time.LocalDateTime.now()
        ));
    }

    @Operation(summary = "Convertir dólares a pesos chilenos", 
               description = "Convierte un monto en dólares estadounidenses a pesos chilenos")
    @GetMapping("/convertir-usd")
    public ResponseEntity<Map<String, Object>> convertirUSDaCLP(
            @Parameter(description = "Monto en dólares estadounidenses") @RequestParam double usd) {
        
        if (usd < 0) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "El monto debe ser mayor o igual a 0"
            ));
        }
        
        double valorDolar = divisaService.obtenerValorDolar();
        double montoCLP = usd * valorDolar;
        
        return ResponseEntity.ok(Map.of(
            "montoOriginal", Map.of(
                "valor", usd,
                "moneda", "USD"
            ),
            "montoConvertido", Map.of(
                "valor", Math.round(montoCLP * 100.0) / 100.0,
                "moneda", "CLP"
            ),
            "tipoCambio", valorDolar,
            "fechaConversion", java.time.LocalDateTime.now()
        ));
    }

    @Operation(summary = "Obtener histórico de valores del dólar", 
               description = "Retorna valores históricos simulados del dólar para análisis de tendencias")
    @GetMapping("/historico")
    public ResponseEntity<Map<String, Object>> obtenerHistorico(
            @Parameter(description = "Número de días hacia atrás") @RequestParam(defaultValue = "30") int dias) {
        
        if (dias < 1 || dias > 365) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "El número de días debe estar entre 1 y 365"
            ));
        }
        
        // Simular datos históricos
        java.util.List<Map<String, Object>> historico = new java.util.ArrayList<>();
        double valorBase = divisaService.obtenerValorDolar();
        
        for (int i = dias; i >= 0; i--) {
            // Simular variación del ±3%
            double variacion = (Math.random() - 0.5) * 0.06;
            double valor = valorBase * (1 + variacion);
            
            historico.add(Map.of(
                "fecha", java.time.LocalDate.now().minusDays(i),
                "valor", Math.round(valor * 100.0) / 100.0
            ));
        }
        
        return ResponseEntity.ok(Map.of(
            "periodo", dias + " días",
            "moneda", "USD/CLP",
            "datos", historico,
            "promedio", valorBase,
            "nota", "Datos simulados para desarrollo"
        ));
    }

    @Operation(summary = "Calcular equivalencias múltiples", 
               description = "Calcula equivalencias para múltiples montos en ambas direcciones")
    @PostMapping("/calcular-equivalencias")
    public ResponseEntity<Map<String, Object>> calcularEquivalencias(
            @RequestBody Map<String, Object> solicitud) {
        
        @SuppressWarnings("unchecked")
        java.util.List<Double> montosCLP = (java.util.List<Double>) solicitud.get("montosCLP");
        @SuppressWarnings("unchecked")
        java.util.List<Double> montosUSD = (java.util.List<Double>) solicitud.get("montosUSD");
        
        double valorDolar = divisaService.obtenerValorDolar();
        java.util.List<Map<String, Object>> resultados = new java.util.ArrayList<>();
        
        // Convertir CLP a USD
        if (montosCLP != null) {
            for (Double monto : montosCLP) {
                if (monto >= 0) {
                    resultados.add(Map.of(
                        "original", monto,
                        "monedaOriginal", "CLP",
                        "convertido", Math.round(divisaService.convertirCLPaUSD(monto) * 100.0) / 100.0,
                        "monedaConvertida", "USD"
                    ));
                }
            }
        }
        
        // Convertir USD a CLP
        if (montosUSD != null) {
            for (Double monto : montosUSD) {
                if (monto >= 0) {
                    resultados.add(Map.of(
                        "original", monto,
                        "monedaOriginal", "USD",
                        "convertido", Math.round(monto * valorDolar * 100.0) / 100.0,
                        "monedaConvertida", "CLP"
                    ));
                }
            }
        }
        
        return ResponseEntity.ok(Map.of(
            "tipoCambio", valorDolar,
            "fechaConversion", java.time.LocalDateTime.now(),
            "resultados", resultados
        ));
    }

    @Operation(summary = "Obtener todas las monedas disponibles", 
               description = "Retorna los valores actuales de todas las monedas soportadas")
    @GetMapping("/monedas")
    public ResponseEntity<Map<String, Object>> obtenerTodasLasMonedas() {
        try {
            Map<String, Object> monedas = divisaService.obtenerTodasLasMonedas();
            return ResponseEntity.ok(Map.of(
                "monedas", monedas,
                "fuente", "Banco Central de Chile",
                "fechaActualizacion", java.time.LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al obtener monedas: " + e.getMessage()
            ));
        }
    }

    @Operation(summary = "Obtener valor de una moneda específica", 
               description = "Retorna el valor actual de una moneda específica en pesos chilenos")
    @GetMapping("/moneda/{codigo}")
    public ResponseEntity<Map<String, Object>> obtenerValorMoneda(
            @Parameter(description = "Código de la moneda (USD, EUR, UF, etc.)") 
            @PathVariable String codigo) {
        try {
            double valor = divisaService.obtenerValorMoneda(codigo);
            return ResponseEntity.ok(Map.of(
                "moneda", codigo.toUpperCase(),
                "valor", valor,
                "unidad", "CLP",
                "fuente", "Banco Central de Chile",
                "fechaActualizacion", java.time.LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al obtener valor de " + codigo + ": " + e.getMessage()
            ));
        }
    }

    @Operation(summary = "Convertir entre cualquier par de monedas", 
               description = "Convierte un monto entre cualquier par de monedas soportadas")
    @PostMapping("/convertir-universal")
    public ResponseEntity<Map<String, Object>> convertirUniversal(
            @RequestBody Map<String, Object> solicitud) {
        try {
            // Validar que existan los campos requeridos
            if (!solicitud.containsKey("monto") || !solicitud.containsKey("monedaOrigen") || !solicitud.containsKey("monedaDestino")) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Se requieren los campos: monto, monedaOrigen, monedaDestino"
                ));
            }
            
            // Parsear valores con validación
            Object montoObj = solicitud.get("monto");
            double monto;
            if (montoObj instanceof Number) {
                monto = ((Number) montoObj).doubleValue();
            } else {
                monto = Double.parseDouble(montoObj.toString());
            }
            
            String monedaOrigen = solicitud.get("monedaOrigen").toString().trim().toUpperCase();
            String monedaDestino = solicitud.get("monedaDestino").toString().trim().toUpperCase();
            
            if (monto < 0) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "El monto debe ser mayor o igual a 0"
                ));
            }
            
            // Validar monedas soportadas
            if (!esMonedasoportada(monedaOrigen) || !esMonedasoportada(monedaDestino)) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Una o ambas monedas no están soportadas. Monedas válidas: CLP, USD, EUR, UF, UTM, DOLAR_INTERCAMBIO"
                ));
            }
            
            Map<String, Object> resultado = divisaService.convertirMoneda(monto, monedaOrigen, monedaDestino);
            
            if (resultado.containsKey("error")) {
                return ResponseEntity.badRequest().body(resultado);
            }
            
            return ResponseEntity.ok(resultado);
            
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "El monto debe ser un número válido"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error en la solicitud: " + e.getMessage(),
                "detalles", "Asegúrese de que el JSON tenga los campos: monto (número), monedaOrigen (string), monedaDestino (string)"
            ));
        }
    }

    @Operation(summary = "Calcular múltiples conversiones", 
               description = "Calcula conversiones para múltiples montos y monedas de una vez")
    @PostMapping("/convertir-multiples")
    public ResponseEntity<Map<String, Object>> convertirMultiples(
            @RequestBody Map<String, Object> solicitud) {
        try {
            logger.info("Solicitud recibida: {}", solicitud);
            
            if (!solicitud.containsKey("conversiones")) {
                logger.warn("Falta el campo 'conversiones' en la solicitud");
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Se requiere el campo 'conversiones' con una lista de objetos"
                ));
            }
            
            @SuppressWarnings("unchecked")
            java.util.List<Map<String, Object>> conversiones = 
                (java.util.List<Map<String, Object>>) solicitud.get("conversiones");
            
            logger.info("Conversiones recibidas: {}", conversiones);
            
            if (conversiones == null || conversiones.isEmpty()) {
                logger.warn("Lista de conversiones vacía o null");
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "La lista de conversiones no puede estar vacía"
                ));
            }
            
            java.util.List<Map<String, Object>> resultados = new java.util.ArrayList<>();
            
            for (int i = 0; i < conversiones.size(); i++) {
                Map<String, Object> conversion = conversiones.get(i);
                logger.info("Procesando conversión {}: {}", i, conversion);
                
                try {
                    // Validar campos requeridos
                    if (!conversion.containsKey("monto") || !conversion.containsKey("monedaOrigen") || !conversion.containsKey("monedaDestino")) {
                        logger.warn("Conversión {}: faltan campos requeridos", i);
                        resultados.add(Map.of(
                            "error", "Conversión " + (i+1) + ": Se requieren los campos monto, monedaOrigen, monedaDestino",
                            "indice", i
                        ));
                        continue;
                    }
                    
                    // Parsear valores
                    Object montoObj = conversion.get("monto");
                    logger.info("Conversión {}: monto object = {}", i, montoObj);
                    
                    if (montoObj == null) {
                        logger.warn("Conversión {}: monto es null", i);
                        resultados.add(Map.of(
                            "error", "Conversión " + (i+1) + ": El monto no puede ser null",
                            "indice", i
                        ));
                        continue;
                    }
                    
                    double monto;
                    if (montoObj instanceof Number) {
                        monto = ((Number) montoObj).doubleValue();
                    } else {
                        monto = Double.parseDouble(montoObj.toString());
                    }
                    
                    Object monedaOrigenObj = conversion.get("monedaOrigen");
                    Object monedaDestinoObj = conversion.get("monedaDestino");
                    
                    if (monedaOrigenObj == null || monedaDestinoObj == null) {
                        logger.warn("Conversión {}: monedaOrigen o monedaDestino son null", i);
                        resultados.add(Map.of(
                            "error", "Conversión " + (i+1) + ": Las monedas no pueden ser null",
                            "indice", i
                        ));
                        continue;
                    }
                    
                    String monedaOrigen = monedaOrigenObj.toString().trim().toUpperCase();
                    String monedaDestino = monedaDestinoObj.toString().trim().toUpperCase();
                    
                    logger.info("Conversión {}: {} {} -> {}", i, monto, monedaOrigen, monedaDestino);
                    
                    // Validar valores
                    if (monto < 0) {
                        resultados.add(Map.of(
                            "error", "Conversión " + (i+1) + ": El monto debe ser mayor o igual a 0",
                            "indice", i
                        ));
                        continue;
                    }
                    
                    if (!esMonedasoportada(monedaOrigen) || !esMonedasoportada(monedaDestino)) {
                        resultados.add(Map.of(
                            "error", "Conversión " + (i+1) + ": Una o ambas monedas no están soportadas",
                            "indice", i
                        ));
                        continue;
                    }
                    
                    // Realizar conversión
                    Map<String, Object> resultado = divisaService.convertirMoneda(monto, monedaOrigen, monedaDestino);
                    resultado.put("indice", i);
                    resultados.add(resultado);
                    
                    logger.info("Conversión {} completada: {}", i, resultado);
                    
                } catch (NumberFormatException e) {
                    logger.error("Error parseando número en conversión {}: {}", i, e.getMessage());
                    resultados.add(Map.of(
                        "error", "Conversión " + (i+1) + ": El monto debe ser un número válido",
                        "indice", i
                    ));
                } catch (Exception e) {
                    logger.error("Error en conversión {}: {}", i, e.getMessage(), e);
                    resultados.add(Map.of(
                        "error", "Conversión " + (i+1) + ": " + e.getMessage(),
                        "indice", i
                    ));
                }
            }
            
            return ResponseEntity.ok(Map.of(
                "resultados", resultados,
                "totalConversiones", resultados.size(),
                "fechaProcesamiento", java.time.LocalDateTime.now()
            ));
            
        } catch (ClassCastException e) {
            logger.error("Error de cast en conversiones múltiples: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "error", "El campo 'conversiones' debe ser una lista de objetos JSON"
            ));
        } catch (Exception e) {
            logger.error("Error general en conversiones múltiples: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al procesar conversiones múltiples: " + e.getMessage(),
                "detalles", "Formato esperado: {\"conversiones\": [{\"monto\": 100, \"monedaOrigen\": \"CLP\", \"monedaDestino\": \"USD\"}]}"
            ));
        }
    }

    @Operation(summary = "Obtener monedas soportadas", 
               description = "Lista todas las monedas que el sistema puede convertir")
    @GetMapping("/monedas-soportadas")
    public ResponseEntity<Map<String, Object>> obtenerMonedasSoportadas() {
        return ResponseEntity.ok(Map.of(
            "monedas", java.util.List.of(
                Map.of("codigo", "CLP", "nombre", "Peso Chileno", "simbolo", "$"),
                Map.of("codigo", "USD", "nombre", "Dólar Estadounidense", "simbolo", "US$"),
                Map.of("codigo", "EUR", "nombre", "Euro", "simbolo", "€"),
                Map.of("codigo", "UF", "nombre", "Unidad de Fomento", "simbolo", "UF"),
                Map.of("codigo", "UTM", "nombre", "Unidad Tributaria Mensual", "simbolo", "UTM"),
                Map.of("codigo", "DOLAR_INTERCAMBIO", "nombre", "Dólar Intercambio", "simbolo", "US$")
            ),
            "nota", "Todas las conversiones se realizan usando CLP como moneda base",
            "fuente", "API Banco Central de Chile"
        ));
    }

    // Método helper para validar monedas soportadas
    private boolean esMonedasoportada(String codigo) {
        return codigo.equals("CLP") || codigo.equals("USD") || codigo.equals("EUR") || 
               codigo.equals("UF") || codigo.equals("UTM") || codigo.equals("DOLAR_INTERCAMBIO");
    }
}
