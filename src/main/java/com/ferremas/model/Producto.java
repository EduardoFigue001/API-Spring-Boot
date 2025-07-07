
package com.ferremas.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "productos")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String codigoProducto;
    
    private String marca;
    private String codigoInterno;
    private String nombre;
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    private CategoriaProducto categoria;
    
    @Enumerated(EnumType.STRING)
    private SubcategoriaProducto subcategoria;
    
    private Integer stock;
    private Double precioActual;
    private String modelo;
    private boolean activo = true;
      @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Precio> precios;

    // Constructores
    public Producto() {}

    public Producto(String codigoProducto, String marca, String nombre, CategoriaProducto categoria, Double precioActual, Integer stock) {
        this.codigoProducto = codigoProducto;
        this.marca = marca;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precioActual = precioActual;
        this.stock = stock;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getCodigoInterno() { return codigoInterno; }
    public void setCodigoInterno(String codigoInterno) { this.codigoInterno = codigoInterno; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public CategoriaProducto getCategoria() { return categoria; }
    public void setCategoria(CategoriaProducto categoria) { this.categoria = categoria; }

    public SubcategoriaProducto getSubcategoria() { return subcategoria; }
    public void setSubcategoria(SubcategoriaProducto subcategoria) { this.subcategoria = subcategoria; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Double getPrecioActual() { return precioActual; }
    public void setPrecioActual(Double precioActual) { this.precioActual = precioActual; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public List<Precio> getPrecios() { return precios; }
    public void setPrecios(List<Precio> precios) { this.precios = precios; }

    @Entity
    @Table(name = "precios")
    public static class Precio {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        private LocalDateTime fecha;
        private Double valor;
          @ManyToOne
        @JoinColumn(name = "producto_id")
        @JsonBackReference
        private Producto producto;

        public Precio() {}

        public Precio(LocalDateTime fecha, Double valor, Producto producto) {
            this.fecha = fecha;
            this.valor = valor;
            this.producto = producto;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public LocalDateTime getFecha() { return fecha; }
        public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

        public Double getValor() { return valor; }
        public void setValor(Double valor) { this.valor = valor; }

        public Producto getProducto() { return producto; }
        public void setProducto(Producto producto) { this.producto = producto; }
    }

    // Enums para categorías según los requerimientos
    public enum CategoriaProducto {
        HERRAMIENTAS,
        MATERIALES_CONSTRUCCION,
        EQUIPOS_SEGURIDAD,
        ACCESORIOS_VARIOS,
        TORNILLOS_ANCLAJES,
        FIJACIONES_ADHESIVOS,
        EQUIPOS_MEDICION
    }

    public enum SubcategoriaProducto {
        // Herramientas
        HERRAMIENTAS_MANUALES, MARTILLOS, DESTORNILLADORES, LLAVES,
        HERRAMIENTAS_ELECTRICAS, TALADROS, SIERRAS, LIJADORAS,
        
        // Materiales de Construcción
        MATERIALES_BASICOS, CEMENTO, ARENA, LADRILLOS,
        ACABADOS, PINTURAS, BARNICES, CERAMICOS,
        
        // Equipos de Seguridad
        CASCOS, GUANTES, LENTES_SEGURIDAD,
        
        // Otros
        TORNILLOS, ANCLAJES, FIJACIONES, ADHESIVOS, MEDICION
    }
}
