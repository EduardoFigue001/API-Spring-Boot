package com.ferremas.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "compras")
public class Compra {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties({"clave", "esAdmin"})
    private Usuario usuario;
    
    @Column(nullable = false)
    private LocalDateTime fechaCompra;
    
    @Column(nullable = false)
    private Double totalCompra;
    
    @Enumerated(EnumType.STRING)
    private EstadoCompra estado;
    
    private String metodoPago;
    private String direccionEntrega;
    private String notasCompra;
    
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DetalleCompra> detalles;
    
    // Constructores
    public Compra() {
        this.fechaCompra = LocalDateTime.now();
        this.estado = EstadoCompra.PENDIENTE;
    }
    
    public Compra(Usuario usuario, Double totalCompra) {
        this();
        this.usuario = usuario;
        this.totalCompra = totalCompra;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public LocalDateTime getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(LocalDateTime fechaCompra) { this.fechaCompra = fechaCompra; }
    
    public Double getTotalCompra() { return totalCompra; }
    public void setTotalCompra(Double totalCompra) { this.totalCompra = totalCompra; }
    
    public EstadoCompra getEstado() { return estado; }
    public void setEstado(EstadoCompra estado) { this.estado = estado; }
    
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    
    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }
    
    public String getNotasCompra() { return notasCompra; }
    public void setNotasCompra(String notasCompra) { this.notasCompra = notasCompra; }
    
    public List<DetalleCompra> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleCompra> detalles) { this.detalles = detalles; }
    
    // Enums
    public enum EstadoCompra {
        PENDIENTE,
        CONFIRMADA,
        PROCESANDO,
        ENVIADA,
        ENTREGADA,
        CANCELADA
    }
    
    // Clase interna para los detalles de la compra
    @Entity
    @Table(name = "detalle_compras")
    public static class DetalleCompra {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        @ManyToOne
        @JoinColumn(name = "compra_id")
        @com.fasterxml.jackson.annotation.JsonBackReference
        private Compra compra;
        
        @ManyToOne
        @JoinColumn(name = "producto_id")
        private Producto producto;
        
        @Column(nullable = false)
        private Integer cantidad;
        
        @Column(nullable = false)
        private Double precioUnitario;
        
        @Column(nullable = false)
        private Double subtotal;
        
        // Constructores
        public DetalleCompra() {}
        
        public DetalleCompra(Compra compra, Producto producto, Integer cantidad, Double precioUnitario) {
            this.compra = compra;
            this.producto = producto;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.subtotal = cantidad * precioUnitario;
        }
        
        // Getters y Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public Compra getCompra() { return compra; }
        public void setCompra(Compra compra) { this.compra = compra; }
        
        public Producto getProducto() { return producto; }
        public void setProducto(Producto producto) { this.producto = producto; }
        
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { 
            this.cantidad = cantidad;
            if (this.precioUnitario != null) {
                this.subtotal = cantidad * this.precioUnitario;
            }
        }
        
        public Double getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(Double precioUnitario) { 
            this.precioUnitario = precioUnitario;
            if (this.cantidad != null) {
                this.subtotal = this.cantidad * precioUnitario;
            }
        }
        
        public Double getSubtotal() { return subtotal; }
        public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    }
}
