package com.ferremas.repository;

import com.ferremas.model.Compra;
import com.ferremas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    
    // Buscar compras por usuario
    List<Compra> findByUsuario(Usuario usuario);
    
    // Buscar compras por usuario ordenadas por fecha (más recientes primero)
    List<Compra> findByUsuarioOrderByFechaCompraDesc(Usuario usuario);
    
    // Buscar compras por estado
    List<Compra> findByEstado(Compra.EstadoCompra estado);
    
    // Buscar compras por usuario y estado
    List<Compra> findByUsuarioAndEstado(Usuario usuario, Compra.EstadoCompra estado);
    
    // Buscar compras en un rango de fechas
    @Query("SELECT c FROM Compra c WHERE c.fechaCompra BETWEEN :fechaInicio AND :fechaFin ORDER BY c.fechaCompra DESC")
    List<Compra> findByFechaCompraBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                         @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar compras por usuario en un rango de fechas
    @Query("SELECT c FROM Compra c WHERE c.usuario = :usuario AND c.fechaCompra BETWEEN :fechaInicio AND :fechaFin ORDER BY c.fechaCompra DESC")
    List<Compra> findByUsuarioAndFechaCompraBetween(@Param("usuario") Usuario usuario,
                                                   @Param("fechaInicio") LocalDateTime fechaInicio,
                                                   @Param("fechaFin") LocalDateTime fechaFin);
    
    // Obtener total de ventas por usuario
    @Query("SELECT SUM(c.totalCompra) FROM Compra c WHERE c.usuario = :usuario AND c.estado != 'CANCELADA'")
    Double getTotalComprasByUsuario(@Param("usuario") Usuario usuario);
    
    // Obtener total de ventas en un período
    @Query("SELECT SUM(c.totalCompra) FROM Compra c WHERE c.fechaCompra BETWEEN :fechaInicio AND :fechaFin AND c.estado != 'CANCELADA'")
    Double getTotalVentasPeriodo(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                @Param("fechaFin") LocalDateTime fechaFin);
    
    // Contar compras por usuario
    Long countByUsuario(Usuario usuario);
    
    // Obtener las últimas N compras
    List<Compra> findTop10ByOrderByFechaCompraDesc();
}
