package com.ferremas.repository;

import com.ferremas.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Buscar producto por código
    Optional<Producto> findByCodigoProducto(String codigoProducto);
    
    // Buscar productos por categoría
    List<Producto> findByCategoria(Producto.CategoriaProducto categoria);
    
    // Buscar productos por subcategoría
    List<Producto> findBySubcategoria(Producto.SubcategoriaProducto subcategoria);
    
    // Buscar productos por marca
    List<Producto> findByMarcaContainingIgnoreCase(String marca);
    
    // Buscar productos por nombre
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar productos activos
    List<Producto> findByActivoTrue();
    
    // Buscar productos con stock disponible
    @Query("SELECT p FROM Producto p WHERE p.stock > 0 AND p.activo = true")
    List<Producto> findProductosDisponibles();
    
    // Buscar productos con stock bajo (menos de 10 unidades)
    @Query("SELECT p FROM Producto p WHERE p.stock < 10 AND p.activo = true")
    List<Producto> findProductosStockBajo();
    
    // Buscar productos por rango de precio
    @Query("SELECT p FROM Producto p WHERE p.precioActual BETWEEN :precioMin AND :precioMax AND p.activo = true")
    List<Producto> findByPrecioRange(@Param("precioMin") Double precioMin, @Param("precioMax") Double precioMax);
}
