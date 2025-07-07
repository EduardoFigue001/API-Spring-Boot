package com.ferremas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/contacto")
@CrossOrigin(origins = "*")
@Tag(name = "Contacto", description = "API para gestión de consultas y contacto con vendedores")
public class ContactoController {    @Operation(summary = "Enviar consulta a vendedor", 
               description = "Permite a los clientes enviar consultas específicas a un vendedor")
    @PostMapping("/consulta")
    public ResponseEntity<Map<String, Object>> enviarConsulta(
            @RequestBody Map<String, Object> consultaData) {
        
        // Simular el envío de consulta
        String nombre = (String) consultaData.get("nombre");
        String email = (String) consultaData.get("email");
        String mensaje = (String) consultaData.get("mensaje");
        
        // Validaciones básicas
        if (nombre == null || email == null || mensaje == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "mensaje", "Nombre, email y mensaje son campos obligatorios"
            ));
        }
        
        // Simular procesamiento (en producción aquí se enviaría email, etc.)
        String numeroTicket = "TICKET-" + System.currentTimeMillis();
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "mensaje", "Consulta enviada exitosamente. Un vendedor se contactará contigo pronto.",
            "numeroTicket", numeroTicket,
            "fechaEnvio", LocalDateTime.now(),
            "tiempoRespuestaEstimado", "24-48 horas hábiles"
        ));
    }    @Operation(summary = "Solicitar cotización", 
               description = "Solicitar cotización personalizada para productos específicos")
    @PostMapping("/cotizacion")
    public ResponseEntity<Map<String, Object>> solicitarCotizacion(
            @RequestBody Map<String, Object> cotizacionData) {
        
        String nombre = (String) cotizacionData.get("nombre");
        String email = (String) cotizacionData.get("email");
        String productos = (String) cotizacionData.get("productos");
        
        if (nombre == null || email == null || productos == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "mensaje", "Nombre, email y productos son campos obligatorios"
            ));
        }
        
        String numeroCotizacion = "COT-" + System.currentTimeMillis();
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "mensaje", "Solicitud de cotización recibida. Te enviaremos la cotización por email.",
            "numeroCotizacion", numeroCotizacion,
            "fechaSolicitud", LocalDateTime.now(),
            "validezCotizacion", "15 días calendario"
        ));
    }

    @Operation(summary = "Obtener información de contacto", 
               description = "Obtiene información de contacto de sucursales y vendedores")
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> obtenerInfoContacto() {
        return ResponseEntity.ok(Map.of(
            "oficinasPrincipales", Map.of(
                "direccion", "Av. Providencia 1234, Santiago",
                "telefono", "+56-2-2234-5678",
                "email", "ventas@ferremas.cl",
                "horarioAtencion", "Lunes a Viernes 8:00-18:00, Sábados 9:00-14:00"
            ),
            "sucursales", Map.of(
                "santiago", Map.of(
                    "direccion", "Av. Las Condes 5678",
                    "telefono", "+56-2-2345-6789",
                    "gerente", "Juan Pérez"
                ),
                "valparaiso", Map.of(
                    "direccion", "Av. Pedro Montt 1234",
                    "telefono", "+56-32-234-5678",
                    "gerente", "María González"
                ),
                "concepcion", Map.of(
                    "direccion", "Av. O'Higgins 9876",
                    "telefono", "+56-41-234-5678",
                    "gerente", "Carlos Rodriguez"
                )
            ),
            "ventasOnline", Map.of(
                "email", "online@ferremas.cl",
                "whatsapp", "+56-9-8765-4321",
                "chatEnLinea", "Disponible en horario comercial"
            )
        ));
    }

    @Operation(summary = "Obtener horarios de atención", 
               description = "Obtiene los horarios de atención por sucursal")
    @GetMapping("/horarios")
    public ResponseEntity<Map<String, Object>> obtenerHorarios() {
        return ResponseEntity.ok(Map.of(
            "horarioGeneral", Map.of(
                "lunesAViernes", "8:00 - 18:00",
                "sabados", "9:00 - 14:00",
                "domingos", "Cerrado"
            ),
            "sucursalEspecial", Map.of(
                "mall", Map.of(
                    "lunesADomingo", "10:00 - 22:00",
                    "feriados", "10:00 - 18:00"
                )
            ),
            "atencionEmergencia", Map.of(
                "telefono", "+56-9-8888-7777",
                "disponibilidad", "24/7 para clientes empresariales"
            )
        ));
    }

    @Operation(summary = "Buscar vendedor por especialidad", 
               description = "Encuentra vendedores especializados en categorías específicas")
    @GetMapping("/vendedor/{categoria}")
    public ResponseEntity<Map<String, Object>> buscarVendedorEspecialista(
            @Parameter(description = "Categoría de productos") @PathVariable String categoria) {
        
        // Simular base de datos de vendedores especializados
        Map<String, Object> vendedor = switch (categoria.toLowerCase()) {
            case "herramientas" -> Map.of(
                "nombre", "Roberto Silva",
                "email", "roberto.silva@ferremas.cl",
                "telefono", "+56-9-1234-5678",
                "especialidad", "Herramientas eléctricas y manuales",
                "experiencia", "8 años",
                "certificaciones", "Bosch, Makita, DeWalt"
            );
            case "construccion" -> Map.of(
                "nombre", "Ana Morales",
                "email", "ana.morales@ferremas.cl",
                "telefono", "+56-9-2345-6789",
                "especialidad", "Materiales de construcción",
                "experiencia", "12 años",
                "certificaciones", "Cementos, Pinturas, Cerámicos"
            );
            case "seguridad" -> Map.of(
                "nombre", "Luis Fernández",
                "email", "luis.fernandez@ferremas.cl",
                "telefono", "+56-9-3456-7890",
                "especialidad", "Equipos de protección personal",
                "experiencia", "6 años",
                "certificaciones", "3M, MSA, Honeywell"
            );
            default -> Map.of(
                "nombre", "Vendedor General",
                "email", "ventas@ferremas.cl",
                "telefono", "+56-2-2234-5678",
                "especialidad", "Asesoría general",
                "experiencia", "Varios años",
                "nota", "Para especialidades específicas, contacta a nuestro centro de atención"
            );
        };
        
        return ResponseEntity.ok(Map.of(
            "vendedor", vendedor,
            "disponibilidad", "Lunes a Viernes 8:00-18:00",
            "formasContacto", Map.of(
                "email", "Respuesta en 24 horas",
                "telefono", "Llamada directa en horario laboral",
                "whatsapp", "Mensajes instantáneos"
            )
        ));
    }
}
