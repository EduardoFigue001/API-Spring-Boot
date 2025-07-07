package com.ferremas.service;

import com.ferremas.model.Producto;
import com.ferremas.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Obtener todos los productos activos
    public List<Producto> obtenerTodos() {
        return productoRepository.findByActivoTrue();
    }

    // Obtener producto por código
    public Optional<Producto> obtenerPorCodigo(String codigo) {
        return productoRepository.findByCodigoProducto(codigo);
    }

    // Obtener productos por categoría
    public List<Producto> obtenerPorCategoria(Producto.CategoriaProducto categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    // Obtener productos por subcategoría
    public List<Producto> obtenerPorSubcategoria(Producto.SubcategoriaProducto subcategoria) {
        return productoRepository.findBySubcategoria(subcategoria);
    }

    // Buscar productos por marca
    public List<Producto> buscarPorMarca(String marca) {
        return productoRepository.findByMarcaContainingIgnoreCase(marca);
    }

    // Buscar productos por nombre
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Obtener productos disponibles (con stock)
    public List<Producto> obtenerProductosDisponibles() {
        return productoRepository.findProductosDisponibles();
    }

    // Obtener productos con stock bajo
    public List<Producto> obtenerProductosStockBajo() {
        return productoRepository.findProductosStockBajo();
    }

    // Buscar productos por rango de precio
    public List<Producto> buscarPorRangoPrecio(Double precioMin, Double precioMax) {
        return productoRepository.findByPrecioRange(precioMin, precioMax);
    }    // Estos métodos se movieron a la sección de administración con mejor funcionalidad

    // Actualizar stock
    public Optional<Producto> actualizarStock(String codigo, Integer nuevoStock) {
        return productoRepository.findByCodigoProducto(codigo)
                .map(producto -> {
                    producto.setStock(nuevoStock);
                    return productoRepository.save(producto);
                });
    }

    // Actualizar precio
    public Optional<Producto> actualizarPrecio(String codigo, Double nuevoPrecio) {
        return productoRepository.findByCodigoProducto(codigo)
                .map(producto -> {
                    // Crear nuevo registro de precio histórico
                    Producto.Precio precio = new Producto.Precio(LocalDateTime.now(), nuevoPrecio, producto);
                    producto.getPrecios().add(precio);
                    producto.setPrecioActual(nuevoPrecio);
                    return productoRepository.save(producto);
                });
    }

    // Desactivar producto (soft delete)
    public boolean desactivarProducto(String codigo) {
        return productoRepository.findByCodigoProducto(codigo)
                .map(producto -> {
                    producto.setActivo(false);
                    productoRepository.save(producto);
                    return true;
                }).orElse(false);
    }    // Verificar disponibilidad
    public boolean verificarDisponibilidad(String codigo, Integer cantidad) {
        return productoRepository.findByCodigoProducto(codigo)
                .map(producto -> producto.getStock() >= cantidad && producto.isActivo())
                .orElse(false);
    }

    // ===== MÉTODOS DE ADMINISTRACIÓN =====

    // Obtener todos los productos (incluyendo inactivos)
    public List<Producto> obtenerTodosAdmin() {
        return productoRepository.findAll();
    }

    // Obtener producto por código (incluyendo inactivos)
    public Optional<Producto> obtenerPorCodigoAdmin(String codigo) {
        return productoRepository.findByCodigoProducto(codigo);
    }    // Crear nuevo producto
    public Producto crearProducto(Producto producto) {
        // Establecer como activo por defecto
        producto.setActivo(true);
        
        // Inicializar lista de precios si no existe
        if (producto.getPrecios() == null) {
            producto.setPrecios(new java.util.ArrayList<>());
        }
        
        // Crear precio inicial
        if (producto.getPrecioActual() != null) {
            Producto.Precio precioInicial = new Producto.Precio(
                LocalDateTime.now(), 
                producto.getPrecioActual(), 
                producto
            );
            producto.getPrecios().add(precioInicial);
        }
        return productoRepository.save(producto);
    }

    // Actualizar producto existente
    public Optional<Producto> actualizarProducto(String codigo, Producto productoActualizado) {
        return productoRepository.findByCodigoProducto(codigo)
                .map(producto -> {
                    producto.setNombre(productoActualizado.getNombre());
                    producto.setMarca(productoActualizado.getMarca());
                    producto.setDescripcion(productoActualizado.getDescripcion());
                    producto.setCategoria(productoActualizado.getCategoria());
                    producto.setSubcategoria(productoActualizado.getSubcategoria());
                    producto.setStock(productoActualizado.getStock());
                    producto.setModelo(productoActualizado.getModelo());
                    
                    // Actualizar precio si cambió
                    if (productoActualizado.getPrecioActual() != null && 
                        !productoActualizado.getPrecioActual().equals(producto.getPrecioActual())) {
                        actualizarPrecio(codigo, productoActualizado.getPrecioActual());
                    }
                    
                    return productoRepository.save(producto);
                });
    }

    // Eliminar producto (soft delete)
    public boolean eliminarProducto(String codigo) {
        return productoRepository.findByCodigoProducto(codigo)
                .map(producto -> {
                    producto.setActivo(false);
                    productoRepository.save(producto);
                    return true;
                }).orElse(false);
    }

    // Reactivar producto
    public Optional<Producto> reactivarProducto(String codigo) {
        return productoRepository.findByCodigoProducto(codigo)
                .map(producto -> {
                    producto.setActivo(true);
                    return productoRepository.save(producto);
                });
    }
}
