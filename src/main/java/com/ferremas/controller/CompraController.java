package com.ferremas.controller;

import com.ferremas.model.Compra;
import com.ferremas.service.CompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*")
@Tag(name = "Compras", description = "API para gestión de compras y historial de transacciones")
public class CompraController {
    
    @Autowired
    private CompraService compraService;
    
    @Operation(summary = "Crear una nueva compra", 
               description = "Registra una nueva compra con los productos del carrito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Compra creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o stock insuficiente"),
        @ApiResponse(responseCode = "404", description = "Usuario o producto no encontrado")
    })
    @PostMapping("/crear")
    public ResponseEntity<Map<String, Object>> crearCompra(@RequestBody Map<String, Object> solicitud) {
        try {
            Long usuarioId = Long.parseLong(solicitud.get("usuarioId").toString());
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> itemsCarrito = (List<Map<String, Object>>) solicitud.get("items");
            
            @SuppressWarnings("unchecked")
            Map<String, String> datosCompra = (Map<String, String>) solicitud.get("datosCompra");
            
            if (itemsCarrito == null || itemsCarrito.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "mensaje", "El carrito está vacío"
                ));
            }
            
            Compra compra = compraService.crearCompra(usuarioId, itemsCarrito, datosCompra);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "mensaje", "Compra creada exitosamente",
                "compra", Map.of(
                    "id", compra.getId(),
                    "total", compra.getTotalCompra(),
                    "fecha", compra.getFechaCompra(),
                    "estado", compra.getEstado(),
                    "numeroItems", compra.getDetalles().size()
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "mensaje", "Error al crear la compra: " + e.getMessage()
            ));
        }
    }
    
    @Operation(summary = "Obtener historial de compras de un usuario", 
               description = "Retorna todas las compras realizadas por un usuario específico")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> obtenerComprasUsuario(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        try {
            List<Compra> compras = compraService.obtenerComprasPorUsuario(usuarioId);
            Map<String, Object> estadisticas = compraService.obtenerEstadisticasUsuario(usuarioId);
            
            return ResponseEntity.ok(Map.of(
                "compras", compras,
                "estadisticas", estadisticas,
                "totalRegistros", compras.size()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al obtener compras: " + e.getMessage()
            ));
        }
    }
    
    @Operation(summary = "Obtener detalles de una compra específica", 
               description = "Retorna los detalles completos de una compra incluyendo productos")
    @GetMapping("/{compraId}")
    public ResponseEntity<Map<String, Object>> obtenerDetalleCompra(
            @Parameter(description = "ID de la compra") @PathVariable Long compraId) {
        return compraService.obtenerCompraPorId(compraId)
                .map(compra -> ResponseEntity.ok(Map.of(
                    "compra", compra,
                    "detalles", compra.getDetalles()
                )))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Actualizar estado de una compra", 
               description = "Permite actualizar el estado de una compra (solo para administradores)")
    @PutMapping("/{compraId}/estado")
    public ResponseEntity<Map<String, Object>> actualizarEstadoCompra(
            @Parameter(description = "ID de la compra") @PathVariable Long compraId,
            @RequestBody Map<String, String> request) {
        try {
            String nuevoEstado = request.get("estado");
            Compra.EstadoCompra estado = Compra.EstadoCompra.valueOf(nuevoEstado.toUpperCase());
            
            Compra compra = compraService.actualizarEstadoCompra(compraId, estado);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "mensaje", "Estado actualizado exitosamente",
                "compra", Map.of(
                    "id", compra.getId(),
                    "estado", compra.getEstado(),
                    "fechaActualizacion", LocalDateTime.now()
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "mensaje", "Error al actualizar estado: " + e.getMessage()
            ));
        }
    }
    
    @Operation(summary = "Cancelar una compra", 
               description = "Cancela una compra y restaura el stock de los productos")
    @PutMapping("/{compraId}/cancelar")
    public ResponseEntity<Map<String, Object>> cancelarCompra(
            @Parameter(description = "ID de la compra") @PathVariable Long compraId,
            @RequestBody Map<String, String> request) {
        try {
            String motivo = request.get("motivo");
            if (motivo == null || motivo.trim().isEmpty()) {
                motivo = "Cancelación solicitada por el usuario";
            }
            
            Compra compra = compraService.cancelarCompra(compraId, motivo);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "mensaje", "Compra cancelada exitosamente",
                "compra", Map.of(
                    "id", compra.getId(),
                    "estado", compra.getEstado(),
                    "motivo", motivo
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "mensaje", "Error al cancelar compra: " + e.getMessage()
            ));
        }
    }
    
    @Operation(summary = "Obtener todas las compras (admin)", 
               description = "Retorna todas las compras del sistema para administradores")
    @GetMapping("/admin/todas")
    public ResponseEntity<List<Compra>> obtenerTodasLasCompras() {
        List<Compra> compras = compraService.obtenerTodasLasCompras();
        return ResponseEntity.ok(compras);
    }
    
    @Operation(summary = "Obtener compras por estado", 
               description = "Filtra las compras por su estado actual")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Compra>> obtenerComprasPorEstado(
            @Parameter(description = "Estado de las compras") @PathVariable String estado) {
        try {
            Compra.EstadoCompra estadoCompra = Compra.EstadoCompra.valueOf(estado.toUpperCase());
            List<Compra> compras = compraService.obtenerComprasPorEstado(estadoCompra);
            return ResponseEntity.ok(compras);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Operation(summary = "Obtener estadísticas de ventas", 
               description = "Retorna estadísticas de ventas para el período especificado")
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasVentas(
            @Parameter(description = "Días hacia atrás") @RequestParam(defaultValue = "30") int dias) {
        try {
            LocalDateTime fechaInicio = LocalDateTime.now().minusDays(dias);
            LocalDateTime fechaFin = LocalDateTime.now();
            
            Double totalVentas = compraService.obtenerVentasPeriodo(fechaInicio, fechaFin);
            List<Compra> comprasRecientes = compraService.obtenerComprasRecientes();
            
            return ResponseEntity.ok(Map.of(
                "periodo", dias + " días",
                "totalVentas", totalVentas,
                "fechaInicio", fechaInicio,
                "fechaFin", fechaFin,
                "comprasRecientes", comprasRecientes.size(),
                "promedioVentaDiaria", totalVentas / dias
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al obtener estadísticas: " + e.getMessage()
            ));
        }
    }
    
    @Operation(summary = "Crear compra simple", 
               description = "Endpoint simplificado para crear una compra desde el frontend")
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearCompraSimple(@RequestBody Map<String, Object> solicitud) {
        try {
            Long usuarioId = Long.parseLong(solicitud.get("usuarioId").toString());
            Double totalCompra = Double.parseDouble(solicitud.get("totalCompra").toString());
            String metodoPago = (String) solicitud.getOrDefault("metodoPago", "Efectivo");
            String direccionEntrega = (String) solicitud.getOrDefault("direccionEntrega", "");
            String notasCompra = (String) solicitud.getOrDefault("notasCompra", "");
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detalles = (List<Map<String, Object>>) solicitud.get("detalles");
            
            Compra compra = compraService.crearCompraSimple(usuarioId, totalCompra, metodoPago, 
                direccionEntrega, notasCompra, detalles);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "mensaje", "Compra creada exitosamente",
                "compra", compra,
                "id", compra.getId(),
                "fechaCompra", compra.getFechaCompra(),
                "totalCompra", compra.getTotalCompra()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "mensaje", "Error al crear la compra: " + e.getMessage(),
                "error", e.getMessage()
            ));
        }
    }
    
    @Operation(summary = "Obtener todas las compras públicas", 
               description = "Retorna todas las compras del sistema en formato JSON estructurado")
    @GetMapping("/todas")
    public ResponseEntity<Map<String, Object>> obtenerTodasLasComprasPublico() {
        try {
            List<Compra> compras = compraService.obtenerTodasLasCompras();
            
            // Calcular estadísticas
            double totalVentas = compras.stream()
                .mapToDouble(Compra::getTotalCompra)
                .sum();
            
            long ventasHoy = compras.stream()
                .filter(c -> c.getFechaCompra().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
                .count();
            
            double promedioVenta = compras.isEmpty() ? 0 : totalVentas / compras.size();
            
            Map<String, Object> estadisticas = Map.of(
                "totalVentas", totalVentas,
                "ventasHoy", ventasHoy,
                "totalCompras", compras.size(),
                "promedioVenta", Math.round(promedioVenta * 100.0) / 100.0
            );
            
            return ResponseEntity.ok(Map.of(
                "compras", compras,
                "totalRegistros", compras.size(),
                "estadisticas", estadisticas,
                "fechaConsulta", LocalDateTime.now()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al obtener compras: " + e.getMessage(),
                "success", false
            ));
        }
    }
    
    @Operation(summary = "Obtener resumen de compras", 
               description = "Retorna un resumen estadístico de todas las compras")
    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> obtenerResumenCompras() {
        try {
            List<Compra> compras = compraService.obtenerTodasLasCompras();
            
            // Estadísticas por estado
            Map<String, Long> porEstado = compras.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    c -> c.getEstado().toString(),
                    java.util.stream.Collectors.counting()
                ));
            
            // Estadísticas por método de pago
            Map<String, Long> porMetodoPago = compras.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    c -> c.getMetodoPago() != null ? c.getMetodoPago() : "No especificado",
                    java.util.stream.Collectors.counting()
                ));
            
            // Total de ventas
            double totalVentas = compras.stream()
                .mapToDouble(Compra::getTotalCompra)
                .sum();
            
            return ResponseEntity.ok(Map.of(
                "totalCompras", compras.size(),
                "totalVentas", totalVentas,
                "porEstado", porEstado,
                "porMetodoPago", porMetodoPago,
                "promedioVenta", compras.isEmpty() ? 0 : totalVentas / compras.size()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al obtener resumen: " + e.getMessage()
            ));
        }
    }
    
    @Operation(summary = "Obtener compras recientes", 
               description = "Retorna las últimas N compras realizadas")
    @GetMapping("/recientes")
    public ResponseEntity<Map<String, Object>> obtenerComprasRecientes(
            @RequestParam(defaultValue = "10") int limite) {
        try {
            List<Compra> todasLasCompras = compraService.obtenerTodasLasCompras();
            
            // Ordenar por fecha más reciente y limitar
            List<Compra> comprasRecientes = todasLasCompras.stream()
                .sorted((c1, c2) -> c2.getFechaCompra().compareTo(c1.getFechaCompra()))
                .limit(limite)
                .collect(java.util.stream.Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "compras", comprasRecientes,
                "totalMostradas", comprasRecientes.size(),
                "totalDisponibles", todasLasCompras.size()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al obtener compras recientes: " + e.getMessage()
            ));
        }
    }
}
