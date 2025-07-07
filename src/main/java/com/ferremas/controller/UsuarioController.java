package com.ferremas.controller;

import com.ferremas.model.Usuario;
import com.ferremas.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@Tag(name = "Usuarios", description = "API para gestión de usuarios y autenticación")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Operation(summary = "Registrar nuevo usuario", 
               description = "Registra un nuevo usuario en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Usuario ya existe o datos inválidos")
    })    @PostMapping("/registrar")
    public ResponseEntity<Map<String, Object>> registrar(@RequestBody Usuario nuevoUsuario) {
        
        try {
            // Logging para debug
            System.out.println("Intento de registro - Correo: " + nuevoUsuario.getCorreo());
            
            // Validaciones básicas
            if (nuevoUsuario.getCorreo() == null || nuevoUsuario.getCorreo().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "mensaje", "El correo es obligatorio"
                ));
            }
            
            if (nuevoUsuario.getClave() == null || nuevoUsuario.getClave().length() < 6) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "mensaje", "La clave debe tener al menos 6 caracteres"
                ));
            }
            
            // Verificar si el usuario ya existe
            Optional<Usuario> existente = usuarioRepository.findByCorreo(nuevoUsuario.getCorreo());
            if (existente.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "mensaje", "EXISTE",
                    "detalle", "Ya existe un usuario registrado con este correo electrónico"
                ));
            }
            
            // En producción, aquí se debería encriptar la contraseña
            Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
            System.out.println("Usuario registrado exitosamente con ID: " + usuarioGuardado.getId());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "mensaje", "REGISTRADO",
                "detalle", "Usuario registrado exitosamente",
                "fechaRegistro", LocalDateTime.now(),
                "usuario", Map.of(
                    "id", usuarioGuardado.getId(),
                    "correo", usuarioGuardado.getCorreo()
                )
            ));
            
        } catch (Exception e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "mensaje", "ERROR_INTERNO",
                "detalle", "Error interno del servidor: " + e.getMessage()
            ));
        }
    }

    @Operation(summary = "Iniciar sesión", 
               description = "Autentica un usuario con correo y contraseña")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Usuario usuario) {
        
        // Validaciones básicas
        if (usuario.getCorreo() == null || usuario.getClave() == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "mensaje", "ERROR",
                "detalle", "Correo y clave son obligatorios"
            ));
        }
        
        Optional<Usuario> encontrado = usuarioRepository.findByCorreo(usuario.getCorreo());
        
        if (encontrado.isPresent() && encontrado.get().getClave().equals(usuario.getClave())) {
            // En producción, aquí se generaría un JWT token
            return ResponseEntity.ok(Map.of(
                "success", true,
                "mensaje", "OK",
                "detalle", "Login exitoso",
                "usuario", Map.of(
                    "id", encontrado.get().getId(),
                    "correo", encontrado.get().getCorreo()
                ),
                "fechaLogin", LocalDateTime.now(),
                "token", "JWT_TOKEN_AQUI" // En producción sería un token real
            ));
        }
        
        return ResponseEntity.status(401).body(Map.of(
            "success", false,
            "mensaje", "ERROR",
            "detalle", "Credenciales inválidas"
        ));
    }

    @Operation(summary = "Obtener perfil de usuario", 
               description = "Obtiene los datos del perfil del usuario autenticado")
    @GetMapping("/perfil/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPerfil(@PathVariable Long id) {
        
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        
        if (usuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
          return ResponseEntity.ok(Map.of(
            "id", usuario.get().getId(),
            "correo", usuario.get().getCorreo(),
            "esAdmin", usuario.get().isEsAdmin(),
            "fechaConsulta", LocalDateTime.now()
        ));
    }

    @Operation(summary = "Cambiar contraseña", 
               description = "Permite al usuario cambiar su contraseña actual")
    @PutMapping("/cambiar-clave/{id}")
    public ResponseEntity<Map<String, Object>> cambiarClave(
            @PathVariable Long id, 
            @RequestBody Map<String, String> claves) {
        
        String claveActual = claves.get("claveActual");
        String claveNueva = claves.get("claveNueva");
        
        if (claveActual == null || claveNueva == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "mensaje", "Clave actual y nueva son obligatorias"
            ));
        }
        
        if (claveNueva.length() < 6) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "mensaje", "La nueva clave debe tener al menos 6 caracteres"
            ));
        }
        
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        
        if (usuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        if (!usuario.get().getClave().equals(claveActual)) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "mensaje", "La clave actual no es correcta"
            ));
        }
        
        // Actualizar la clave
        Usuario usuarioActualizado = usuario.get();
        usuarioActualizado.setClave(claveNueva);
        usuarioRepository.save(usuarioActualizado);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "mensaje", "Contraseña actualizada exitosamente",
            "fechaCambio", LocalDateTime.now()
        ));
    }

    @Operation(summary = "Validar token de sesión", 
               description = "Valida si un token de sesión sigue siendo válido")
    @PostMapping("/validar-token")
    public ResponseEntity<Map<String, Object>> validarToken(@RequestBody Map<String, String> tokenData) {
        
        String token = tokenData.get("token");
        
        if (token == null || token.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "valido", false,
                "mensaje", "Token no proporcionado"
            ));
        }
        
        // En producción, aquí se validaría el JWT token
        // Por ahora simulamos validación
        boolean tokenValido = "JWT_TOKEN_AQUI".equals(token);
        
        return ResponseEntity.ok(Map.of(
            "valido", tokenValido,
            "mensaje", tokenValido ? "Token válido" : "Token inválido o expirado",
            "fechaValidacion", LocalDateTime.now()
        ));
    }

    // Endpoint para manejar errores de parsing JSON
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonError(
            org.springframework.http.converter.HttpMessageNotReadableException ex) {
        
        System.err.println("Error de parsing JSON: " + ex.getMessage());
        return ResponseEntity.badRequest().body(Map.of(
            "success", false,
            "mensaje", "JSON_INVALIDO",
            "detalle", "El formato JSON enviado no es válido. Verifica la sintaxis.",
            "error", ex.getMessage()
        ));
    }    @Operation(summary = "Endpoint de prueba", 
               description = "Endpoint simple para probar conectividad")
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        return ResponseEntity.ok(Map.of(
            "success", true,
            "mensaje", "API funcionando correctamente",
            "timestamp", LocalDateTime.now()
        ));
    }

    @Operation(summary = "Crear usuario administrador", 
               description = "Crea un usuario con privilegios de administrador")
    @PostMapping("/crear-admin")
    public ResponseEntity<Map<String, Object>> crearAdmin(@RequestBody Map<String, String> adminData) {
        try {
            String correo = adminData.get("correo");
            String clave = adminData.get("clave");
            String claveAdmin = adminData.get("claveAdmin");
            
            // Clave maestra para crear admins (en producción esto debería ser más seguro)
            if (!"ferremas2024".equals(claveAdmin)) {
                return ResponseEntity.status(403).body(Map.of(
                    "success", false,
                    "mensaje", "Clave de administrador incorrecta"
                ));
            }

            // Validaciones básicas
            if (correo == null || correo.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "mensaje", "El correo es obligatorio"
                ));
            }
            
            if (clave == null || clave.length() < 6) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "mensaje", "La clave debe tener al menos 6 caracteres"
                ));
            }
            
            // Verificar si el usuario ya existe
            Optional<Usuario> existente = usuarioRepository.findByCorreo(correo);
            if (existente.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "mensaje", "Ya existe un usuario con este correo"
                ));
            }
            
            // Crear usuario admin
            Usuario admin = new Usuario();
            admin.setCorreo(correo);
            admin.setClave(clave);
            admin.setEsAdmin(true);
            
            Usuario adminGuardado = usuarioRepository.save(admin);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "mensaje", "Administrador creado exitosamente",
                "admin", Map.of(
                    "id", adminGuardado.getId(),
                    "correo", adminGuardado.getCorreo(),
                    "esAdmin", adminGuardado.isEsAdmin()
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "mensaje", "Error al crear administrador: " + e.getMessage()
            ));
        }
    }
}
