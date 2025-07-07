package com.ferremas.service;

import com.ferremas.model.Compra;
import com.ferremas.model.Compra.DetalleCompra;
import com.ferremas.model.Producto;
import com.ferremas.model.Usuario;
import com.ferremas.repository.CompraRepository;
import com.ferremas.repository.UsuarioRepository;
import com.ferremas.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class CompraService {
    
    @Autowired
    private CompraRepository compraRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    // Crear una nueva compra
    @Transactional
    public Compra crearCompra(Long usuarioId, List<Map<String, Object>> itemsCarrito, Map<String, String> datosCompra) {
        // Buscar el usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Crear la compra
        Compra compra = new Compra(usuario, 0.0);
        compra.setMetodoPago(datosCompra.get("metodoPago"));
        compra.setDireccionEntrega(datosCompra.get("direccionEntrega"));
        compra.setNotasCompra(datosCompra.get("notas"));
        
        // Crear los detalles de la compra
        List<DetalleCompra> detalles = new ArrayList<>();
        double totalCompra = 0.0;
        
        for (Map<String, Object> item : itemsCarrito) {
            String codigoProducto = (String) item.get("codigoProducto");
            Integer cantidad = Integer.parseInt(item.get("cantidad").toString());
            
            Producto producto = productoRepository.findByCodigoProducto(codigoProducto)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + codigoProducto));
            
            // Verificar stock disponible
            if (producto.getStock() < cantidad) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }
            
            // Crear detalle de compra
            DetalleCompra detalle = new DetalleCompra(compra, producto, cantidad, producto.getPrecioActual());
            detalles.add(detalle);
            totalCompra += detalle.getSubtotal();
            
            // Reducir stock del producto
            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);
        }
        
        compra.setTotalCompra(totalCompra);
        compra.setDetalles(detalles);
        
        return compraRepository.save(compra);
    }
    
    // Crear una nueva compra (formato simple)
    @Transactional
    public Compra crearCompraSimple(Long usuarioId, Double totalCompra, String metodoPago, 
            String direccionEntrega, String notasCompra, List<Map<String, Object>> detalles) {
        
        // Buscar el usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Crear la compra
        Compra compra = new Compra(usuario, totalCompra);
        compra.setMetodoPago(metodoPago);
        compra.setDireccionEntrega(direccionEntrega);
        compra.setNotasCompra(notasCompra);
        
        // Crear los detalles de la compra
        List<DetalleCompra> detallesCompra = new ArrayList<>();
        
        for (Map<String, Object> detalle : detalles) {
            Long productoId = Long.parseLong(detalle.get("productoId").toString());
            Integer cantidad = Integer.parseInt(detalle.get("cantidad").toString());
            Double precioUnitario = Double.parseDouble(detalle.get("precioUnitario").toString());
            
            // Buscar el producto
            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productoId));
            
            // Verificar stock
            if (producto.getStock() < cantidad) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }
            
            // Actualizar stock
            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);
            
            // Crear detalle de compra
            DetalleCompra detalleCompra = new DetalleCompra(compra, producto, cantidad, precioUnitario);
            detallesCompra.add(detalleCompra);
        }
        
        compra.setDetalles(detallesCompra);
        
        // Guardar la compra
        return compraRepository.save(compra);
    }
    
    // Obtener compras por usuario
    public List<Compra> obtenerComprasPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return compraRepository.findByUsuarioOrderByFechaCompraDesc(usuario);
    }
    
    // Obtener una compra específica
    public Optional<Compra> obtenerCompraPorId(Long compraId) {
        return compraRepository.findById(compraId);
    }
    
    // Actualizar estado de una compra
    @Transactional
    public Compra actualizarEstadoCompra(Long compraId, Compra.EstadoCompra nuevoEstado) {
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        
        compra.setEstado(nuevoEstado);
        return compraRepository.save(compra);
    }
    
    // Cancelar una compra
    @Transactional
    public Compra cancelarCompra(Long compraId, String motivo) {
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        
        if (compra.getEstado() == Compra.EstadoCompra.ENTREGADA) {
            throw new RuntimeException("No se puede cancelar una compra ya entregada");
        }
        
        // Restaurar stock de los productos
        for (DetalleCompra detalle : compra.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }
        
        compra.setEstado(Compra.EstadoCompra.CANCELADA);
        compra.setNotasCompra((compra.getNotasCompra() != null ? compra.getNotasCompra() + " | " : "") + 
                             "Cancelada: " + motivo);
        
        return compraRepository.save(compra);
    }
    
    // Obtener todas las compras (para admin)
    public List<Compra> obtenerTodasLasCompras() {
        return compraRepository.findAll();
    }
    
    // Obtener compras por estado
    public List<Compra> obtenerComprasPorEstado(Compra.EstadoCompra estado) {
        return compraRepository.findByEstado(estado);
    }
    
    // Obtener estadísticas de compras de un usuario
    public Map<String, Object> obtenerEstadisticasUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Long totalCompras = compraRepository.countByUsuario(usuario);
        Double totalGastado = compraRepository.getTotalComprasByUsuario(usuario);
        
        return Map.of(
            "totalCompras", totalCompras != null ? totalCompras : 0,
            "totalGastado", totalGastado != null ? totalGastado : 0.0,
            "promedioCompra", (totalCompras != null && totalCompras > 0 && totalGastado != null) ? 
                             totalGastado / totalCompras : 0.0
        );
    }
    
    // Obtener compras recientes
    public List<Compra> obtenerComprasRecientes() {
        return compraRepository.findTop10ByOrderByFechaCompraDesc();
    }
    
    // Obtener ventas totales en un período
    public Double obtenerVentasPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Double total = compraRepository.getTotalVentasPeriodo(fechaInicio, fechaFin);
        return total != null ? total : 0.0;
    }
}
