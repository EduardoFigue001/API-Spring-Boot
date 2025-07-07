
package com.ferremas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/pago")
@CrossOrigin(origins = "*")
@Tag(name = "Pagos", description = "API para procesamiento de pagos con WebPay (simulado)")
public class PagoController {

    @Operation(summary = "Iniciar proceso de pago", 
               description = "Inicia una transacción de pago simulando la integración con WebPay")
    @PostMapping("/iniciar")
    public ResponseEntity<Map<String, Object>> iniciarPago(@RequestBody Map<String, Object> datosPago) {
          Double monto = null;
        if (datosPago.get("monto") instanceof Number) {
            monto = ((Number) datosPago.get("monto")).doubleValue();
        }
        
        String numeroOrden = (String) datosPago.get("numeroOrden");
        
        // Validaciones
        if (monto == null || monto <= 0) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "mensaje", "El monto debe ser mayor a 0"
            ));
        }
        
        if (numeroOrden == null || numeroOrden.trim().isEmpty()) {
            numeroOrden = "ORD-" + System.currentTimeMillis();
        }
        
        // Simular creación de transacción WebPay
        String tokenTransaccion = UUID.randomUUID().toString();
        String urlWebPay = "https://webpay3gint.transbank.cl/webpayserver/initTransaction";
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "mensaje", "Transacción iniciada correctamente",
            "tokenTransaccion", tokenTransaccion,
            "numeroOrden", numeroOrden,
            "monto", monto,
            "urlRedirect", urlWebPay + "?token=" + tokenTransaccion,
            "fechaCreacion", LocalDateTime.now(),
            "estadoTransaccion", "INICIADA",
            "tiempoExpiracion", "15 minutos"
        ));
    }

    @Operation(summary = "Confirmar pago exitoso", 
               description = "Endpoint de retorno para pagos exitosos desde WebPay")
    @GetMapping("/exito")
    public ResponseEntity<Map<String, Object>> pagoExitoso(
            @Parameter(description = "Token de la transacción") @RequestParam(required = false) String token) {
        
        // Simular validación del token con WebPay
        if (token == null || token.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "mensaje", "Token de transacción requerido"
            ));
        }
        
        // Simular datos de respuesta de WebPay
        String numeroAutorizacion = String.valueOf(System.currentTimeMillis()).substring(0, 6);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "mensaje", "✅ Pago realizado con éxito",
            "token", token,
            "numeroAutorizacion", numeroAutorizacion,
            "fechaPago", LocalDateTime.now(),
            "estadoPago", "APROBADO",
            "tipoPago", "CREDITO",
            "cuotas", 1,
            "codigoRespuesta", "0", // 0 = Aprobado en WebPay
            "mensajeRespuesta", "Transacción aprobada"
        ));
    }

    @Operation(summary = "Manejar pago rechazado", 
               description = "Endpoint de retorno para pagos rechazados o con error")
    @GetMapping("/error")
    public ResponseEntity<Map<String, Object>> pagoError(
            @Parameter(description = "Token de la transacción") @RequestParam(required = false) String token,
            @Parameter(description = "Código de error") @RequestParam(required = false) String error) {
        
        String mensajeError = switch (error != null ? error : "UNKNOWN") {
            case "REJECTED" -> "Pago rechazado por el banco emisor";
            case "TIMEOUT" -> "Tiempo de sesión expirado";
            case "CANCELLED" -> "Pago cancelado por el usuario";
            case "INSUFFICIENT_FUNDS" -> "Fondos insuficientes";
            case "INVALID_CARD" -> "Tarjeta inválida o expirada";
            default -> "Error en el procesamiento del pago";
        };
        
        return ResponseEntity.ok(Map.of(
            "success", false,
            "mensaje", "❌ " + mensajeError,
            "token", token != null ? token : "N/A",
            "fechaError", LocalDateTime.now(),
            "estadoPago", "RECHAZADO",
            "codigoError", error != null ? error : "UNKNOWN",
            "sugerencia", "Intenta con otro método de pago o contacta a tu banco"
        ));
    }

    @Operation(summary = "Consultar estado de transacción", 
               description = "Consulta el estado actual de una transacción por su token")
    @GetMapping("/estado/{token}")
    public ResponseEntity<Map<String, Object>> consultarEstado(
            @Parameter(description = "Token de la transacción") @PathVariable String token) {
        
        // Simular consulta a WebPay
        // En implementación real, aquí se haría una llamada a la API de WebPay
        
        // Simular diferentes estados posibles
        String[] estadosPosibles = {"INICIADA", "EN_PROCESO", "APROBADA", "RECHAZADA", "EXPIRADA"};
        String estado = estadosPosibles[(int) (Math.random() * estadosPosibles.length)];
        
        Map<String, Object> respuesta = Map.of(
            "token", token,
            "estado", estado,
            "fechaConsulta", LocalDateTime.now()
        );
        
        // Agregar detalles adicionales según el estado
        if ("APROBADA".equals(estado)) {
            return ResponseEntity.ok(Map.of(
                "token", token,
                "estado", estado,
                "numeroAutorizacion", "123456",
                "monto", 45000.0,
                "fechaPago", LocalDateTime.now().minusMinutes(5),
                "tipoPago", "CREDITO"
            ));
        } else if ("RECHAZADA".equals(estado)) {
            return ResponseEntity.ok(Map.of(
                "token", token,
                "estado", estado,
                "motivoRechazo", "FONDOS_INSUFICIENTES",
                "fechaRechazo", LocalDateTime.now().minusMinutes(2)
            ));
        }
        
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Obtener métodos de pago disponibles", 
               description = "Retorna los métodos de pago soportados por WebPay")
    @GetMapping("/metodos")
    public ResponseEntity<Map<String, Object>> obtenerMetodosPago() {
        return ResponseEntity.ok(Map.of(
            "metodos", Map.of(
                "tarjetasCredito", Map.of(
                    "visa", Map.of("disponible", true, "cuotasMaximas", 36),
                    "mastercard", Map.of("disponible", true, "cuotasMaximas", 36),
                    "americanExpress", Map.of("disponible", true, "cuotasMaximas", 24),
                    "diners", Map.of("disponible", true, "cuotasMaximas", 12)
                ),
                "tarjetasDebito", Map.of(
                    "redbank", Map.of("disponible", true),
                    "magna", Map.of("disponible", true),
                    "maestro", Map.of("disponible", true)
                ),
                "otros", Map.of(
                    "webpayPrepago", Map.of("disponible", true),
                    "transferenciaElectronica", Map.of("disponible", false, "proximamente", true)
                )
            ),
            "montoMinimo", 1000,
            "montoMaximo", 2000000,
            "moneda", "CLP",
            "comisionWebPay", "2.9% + IVA"
        ));
    }

    @Operation(summary = "Anular transacción", 
               description = "Anula una transacción aprobada (funcionalidad administrativa)")
    @PostMapping("/anular/{token}")
    public ResponseEntity<Map<String, Object>> anularTransaccion(
            @Parameter(description = "Token de la transacción") @PathVariable String token,
            @RequestBody Map<String, Object> datosAnulacion) {
        
        String motivo = (String) datosAnulacion.get("motivo");
        
        if (motivo == null || motivo.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "mensaje", "Motivo de anulación es requerido"
            ));
        }
        
        // Simular proceso de anulación
        String numeroAnulacion = "ANU-" + System.currentTimeMillis();
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "mensaje", "Transacción anulada exitosamente",
            "tokenOriginal", token,
            "numeroAnulacion", numeroAnulacion,
            "motivo", motivo,
            "fechaAnulacion", LocalDateTime.now(),
            "estadoFinal", "ANULADA",
            "tiempoReintegro", "5-7 días hábiles"
        ));
    }
}
