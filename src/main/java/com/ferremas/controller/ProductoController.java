package com.ferremas.controller;

import com.ferremas.model.Producto;
import com.ferremas.service.ProductoService;
import com.ferremas.repository.UsuarioRepository;

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
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
@Tag(name = "Productos", description = "API para gestión de productos de ferretería")
public class ProductoController {    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Operation(summary = "Obtener todos los productos", 
               description = "Retorna una lista de todos los productos activos en el catálogo")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    @GetMapping
    public List<Producto> obtenerTodos() {
        return productoService.obtenerTodos();
    }

    @Operation(summary = "Obtener producto por código", 
               description = "Busca un producto específico usando su código único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{codigo}")
    public ResponseEntity<Producto> obtenerPorCodigo(
            @Parameter(description = "Código único del producto") @PathVariable String codigo) {
        return productoService.obtenerPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener productos por categoría")
    @GetMapping("/categoria/{categoria}")
    public List<Producto> obtenerPorCategoria(
            @Parameter(description = "Categoría del producto") @PathVariable Producto.CategoriaProducto categoria) {
        return productoService.obtenerPorCategoria(categoria);
    }

    @Operation(summary = "Obtener productos por subcategoría")
    @GetMapping("/subcategoria/{subcategoria}")
    public List<Producto> obtenerPorSubcategoria(
            @Parameter(description = "Subcategoría del producto") @PathVariable Producto.SubcategoriaProducto subcategoria) {
        return productoService.obtenerPorSubcategoria(subcategoria);
    }

    @Operation(summary = "Buscar productos por marca")
    @GetMapping("/buscar/marca")
    public List<Producto> buscarPorMarca(
            @Parameter(description = "Nombre de la marca a buscar") @RequestParam String marca) {
        return productoService.buscarPorMarca(marca);
    }

    @Operation(summary = "Buscar productos por nombre")
    @GetMapping("/buscar/nombre")
    public List<Producto> buscarPorNombre(
            @Parameter(description = "Nombre del producto a buscar") @RequestParam String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    @Operation(summary = "Obtener productos disponibles", 
               description = "Retorna productos que tienen stock disponible")
    @GetMapping("/disponibles")
    public List<Producto> obtenerProductosDisponibles() {
        return productoService.obtenerProductosDisponibles();
    }

    @Operation(summary = "Obtener productos con stock bajo", 
               description = "Retorna productos con menos de 10 unidades en stock")
    @GetMapping("/stock-bajo")
    public List<Producto> obtenerProductosStockBajo() {
        return productoService.obtenerProductosStockBajo();
    }

    @Operation(summary = "Buscar productos por rango de precio")
    @GetMapping("/buscar/precio")
    public List<Producto> buscarPorRangoPrecio(
            @Parameter(description = "Precio mínimo") @RequestParam Double precioMin, 
            @Parameter(description = "Precio máximo") @RequestParam Double precioMax) {
        return productoService.buscarPorRangoPrecio(precioMin, precioMax);
    }

    @Operation(summary = "Crear nuevo producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del producto inválidos")
    })
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.crearProducto(producto);
            return ResponseEntity.ok(nuevoProducto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar producto completo")
    @PutMapping("/{codigo}")
    public ResponseEntity<Producto> actualizarProducto(
            @Parameter(description = "Código del producto a actualizar") @PathVariable String codigo, 
            @RequestBody Producto producto) {
        return productoService.actualizarProducto(codigo, producto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar stock del producto")
    @PatchMapping("/{codigo}/stock")
    public ResponseEntity<Producto> actualizarStock(
            @Parameter(description = "Código del producto") @PathVariable String codigo, 
            @RequestBody Map<String, Integer> request) {
        Integer nuevoStock = request.get("stock");
        return productoService.actualizarStock(codigo, nuevoStock)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar precio del producto")
    @PatchMapping("/{codigo}/precio")
    public ResponseEntity<Producto> actualizarPrecio(
            @Parameter(description = "Código del producto") @PathVariable String codigo, 
            @RequestBody Map<String, Double> request) {
        Double nuevoPrecio = request.get("precio");
        return productoService.actualizarPrecio(codigo, nuevoPrecio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Desactivar producto")
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> desactivarProducto(
            @Parameter(description = "Código del producto a desactivar") @PathVariable String codigo) {
        if (productoService.desactivarProducto(codigo)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Verificar disponibilidad del producto")
    @GetMapping("/{codigo}/disponibilidad")
    public ResponseEntity<Map<String, Object>> verificarDisponibilidad(
            @Parameter(description = "Código del producto") @PathVariable String codigo, 
            @Parameter(description = "Cantidad requerida") @RequestParam Integer cantidad) {
        boolean disponible = productoService.verificarDisponibilidad(codigo, cantidad);
        return ResponseEntity.ok(Map.of(
            "codigo", codigo,
            "cantidad", cantidad,
            "disponible", disponible
        ));
    }

    @Operation(summary = "Obtener todas las categorías disponibles")
    @GetMapping("/categorias")
    public Producto.CategoriaProducto[] obtenerCategorias() {
        return Producto.CategoriaProducto.values();
    }

    @Operation(summary = "Obtener todas las subcategorías disponibles")
    @GetMapping("/subcategorias")
    public Producto.SubcategoriaProducto[] obtenerSubcategorias() {
        return Producto.SubcategoriaProducto.values();
    }

    // Endpoints para administración de productos

    @Operation(summary = "Obtener todos los productos (admin)", 
               description = "Retorna una lista de todos los productos, incluyendo inactivos")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    @GetMapping("/admin")
    public List<Producto> obtenerTodosAdmin() {
        return productoService.obtenerTodosAdmin();
    }

    @Operation(summary = "Obtener producto por código (admin)", 
               description = "Busca un producto específico usando su código único, incluyendo inactivos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/admin/{codigo}")
    public ResponseEntity<Producto> obtenerPorCodigoAdmin(
            @Parameter(description = "Código único del producto") @PathVariable String codigo) {
        return productoService.obtenerPorCodigoAdmin(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear o actualizar producto (admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto creado o actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del producto inválidos")
    })
    @PutMapping("/admin/{codigo}")
    public ResponseEntity<Producto> crearOActualizarProductoAdmin(
            @Parameter(description = "Código del producto") @PathVariable String codigo, 
            @RequestBody Producto producto) {
        try {
            // Verificar si el producto existe
            Optional<Producto> productoExistente = productoService.obtenerPorCodigo(codigo);
            if (productoExistente.isPresent()) {
                // Actualizar producto existente
                Producto actualizado = productoService.actualizarProducto(codigo, producto)
                        .orElseThrow();
                return ResponseEntity.ok(actualizado);
            } else {
                // Crear nuevo producto
                Producto nuevoProducto = productoService.crearProducto(producto);
                return ResponseEntity.ok(nuevoProducto);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Eliminar producto (admin)")
    @DeleteMapping("/admin/{codigo}")
    public ResponseEntity<Void> eliminarProductoAdmin(
            @Parameter(description = "Código del producto a eliminar") @PathVariable String codigo) {
        if (productoService.eliminarProducto(codigo)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }    @Operation(summary = "Reactivar producto (admin)")
    @PatchMapping("/admin/{codigo}/reactivar")
    public ResponseEntity<Producto> reactivarProductoAdmin(
            @Parameter(description = "Código del producto a reactivar") @PathVariable String codigo) {
        return productoService.reactivarProducto(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Nuevo endpoint para crear productos desde admin
    @Operation(summary = "Crear producto nuevo (admin)")
    @PostMapping("/admin")
    public ResponseEntity<Map<String, Object>> crearProductoAdmin(@RequestBody Map<String, Object> productoData) {
        try {
            // Validar datos básicos
            String correoAdmin = (String) productoData.get("correoAdmin");
            if (correoAdmin == null || !esUsuarioAdmin(correoAdmin)) {
                return ResponseEntity.status(403).body(Map.of(
                    "success", false,
                    "mensaje", "Acceso denegado. Solo administradores pueden crear productos."
                ));
            }

            // Crear el producto
            Producto nuevoProducto = new Producto();
            nuevoProducto.setCodigoProducto((String) productoData.get("codigoProducto"));
            nuevoProducto.setNombre((String) productoData.get("nombre"));
            nuevoProducto.setMarca((String) productoData.get("marca"));
            nuevoProducto.setDescripcion((String) productoData.get("descripcion"));
            nuevoProducto.setPrecioActual(Double.parseDouble(productoData.get("precio").toString()));
            nuevoProducto.setStock(Integer.parseInt(productoData.get("stock").toString()));
            
            // Establecer categoría
            String categoria = (String) productoData.get("categoria");
            if (categoria != null) {
                nuevoProducto.setCategoria(Producto.CategoriaProducto.valueOf(categoria));
            }

            Producto productoGuardado = productoService.crearProducto(nuevoProducto);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "mensaje", "Producto creado exitosamente",
                "producto", productoGuardado
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "mensaje", "Error al crear producto: " + e.getMessage()
            ));
        }
    }

    private boolean esUsuarioAdmin(String correo) {
        if (correo == null) return false;
        return usuarioRepository.findByCorreo(correo)
                .map(usuario -> usuario.isEsAdmin())
                .orElse(false);
    }
}
