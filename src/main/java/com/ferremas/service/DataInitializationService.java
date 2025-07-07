package com.ferremas.service;

import com.ferremas.model.Producto;
import com.ferremas.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Solo cargar datos si la base de datos está vacía
        if (productoRepository.count() == 0) {
            cargarProductosIniciales();
        }
    }

    private void cargarProductosIniciales() {
        System.out.println("Cargando productos iniciales en la base de datos...");

        // Herramientas Manuales
        crearProducto("FER-001", "Stanley", "STM-001", "Martillo de Acero", 
                     "Martillo de acero forjado de 16 oz", 
                     Producto.CategoriaProducto.HERRAMIENTAS, 
                     Producto.SubcategoriaProducto.MARTILLOS, 
                     25000.0, 50, "HAM-16OZ");

        crearProducto("FER-002", "Bosch", "BOS-002", "Destornillador Phillips", 
                     "Destornillador Phillips #2", 
                     Producto.CategoriaProducto.HERRAMIENTAS, 
                     Producto.SubcategoriaProducto.DESTORNILLADORES, 
                     8500.0, 100, "DES-PH2");

        crearProducto("FER-003", "DeWalt", "DEW-003", "Llave Inglesa 12\"", 
                     "Llave inglesa ajustable de 12 pulgadas", 
                     Producto.CategoriaProducto.HERRAMIENTAS, 
                     Producto.SubcategoriaProducto.LLAVES, 
                     35000.0, 30, "LLA-12IN");

        // Herramientas Eléctricas
        crearProducto("FER-004", "Bosch", "BOS-004", "Taladro Percutor GSB 13 RE", 
                     "Taladro percutor 650W con mandril de 13mm", 
                     Producto.CategoriaProducto.HERRAMIENTAS, 
                     Producto.SubcategoriaProducto.TALADROS, 
                     89000.0, 15, "GSB-13RE");

        crearProducto("FER-005", "Makita", "MAK-005", "Sierra Circular 7 1/4\"", 
                     "Sierra circular 1400W, disco de 7 1/4 pulgadas", 
                     Producto.CategoriaProducto.HERRAMIENTAS, 
                     Producto.SubcategoriaProducto.SIERRAS, 
                     125000.0, 8, "SC-714");

        crearProducto("FER-006", "Black&Decker", "BDK-006", "Lijadora Orbital", 
                     "Lijadora orbital 200W con base de velcro", 
                     Producto.CategoriaProducto.HERRAMIENTAS, 
                     Producto.SubcategoriaProducto.LIJADORAS, 
                     45000.0, 20, "LO-200W");

        // Materiales de Construcción
        crearProducto("FER-007", "Cementos Bío Bío", "CEM-007", "Cemento Especial", 
                     "Cemento Portland Especial saco 25kg", 
                     Producto.CategoriaProducto.MATERIALES_CONSTRUCCION, 
                     Producto.SubcategoriaProducto.CEMENTO, 
                     6500.0, 200, "CEM-25KG");

        crearProducto("FER-008", "Ferrosur", "FER-008", "Arena Gruesa", 
                     "Arena gruesa para construcción m³", 
                     Producto.CategoriaProducto.MATERIALES_CONSTRUCCION, 
                     Producto.SubcategoriaProducto.ARENA, 
                     18000.0, 50, "ARN-GRU");

        crearProducto("FER-009", "Pizarreño", "PIZ-009", "Ladrillo Fiscal", 
                     "Ladrillo fiscal 6x12x24 cm", 
                     Producto.CategoriaProducto.MATERIALES_CONSTRUCCION, 
                     Producto.SubcategoriaProducto.LADRILLOS, 
                     450.0, 5000, "LAD-FIS");

        // Acabados
        crearProducto("FER-010", "Sherwin Williams", "SHW-010", "Pintura Látex Blanca", 
                     "Pintura látex interior blanco 1 galón", 
                     Producto.CategoriaProducto.MATERIALES_CONSTRUCCION, 
                     Producto.SubcategoriaProducto.PINTURAS, 
                     28000.0, 80, "PIN-LAT-1G");

        crearProducto("FER-011", "Ceresita", "CER-011", "Barniz Marino", 
                     "Barniz marino transparente 1 litro", 
                     Producto.CategoriaProducto.MATERIALES_CONSTRUCCION, 
                     Producto.SubcategoriaProducto.BARNICES, 
                     22000.0, 45, "BAR-MAR-1L");

        crearProducto("FER-012", "San Lorenzo", "CSL-012", "Cerámico Beige 30x30", 
                     "Cerámico para piso beige 30x30 cm", 
                     Producto.CategoriaProducto.MATERIALES_CONSTRUCCION, 
                     Producto.SubcategoriaProducto.CERAMICOS, 
                     8500.0, 300, "CER-30X30");

        // Equipos de Seguridad
        crearProducto("FER-013", "3M", "3M-013", "Casco de Seguridad", 
                     "Casco de seguridad blanco con barbiquejo", 
                     Producto.CategoriaProducto.EQUIPOS_SEGURIDAD, 
                     Producto.SubcategoriaProducto.CASCOS, 
                     15000.0, 60, "CAS-SEG");

        crearProducto("FER-014", "SteelPro", "STP-014", "Guantes de Cuero", 
                     "Guantes de cuero reforzados talla M", 
                     Producto.CategoriaProducto.EQUIPOS_SEGURIDAD, 
                     Producto.SubcategoriaProducto.GUANTES, 
                     12000.0, 120, "GUA-CUE-M");

        crearProducto("FER-015", "Uvex", "UVX-015", "Lentes de Seguridad", 
                     "Lentes de seguridad transparentes", 
                     Producto.CategoriaProducto.EQUIPOS_SEGURIDAD, 
                     Producto.SubcategoriaProducto.LENTES_SEGURIDAD, 
                     8500.0, 90, "LEN-SEG");

        // Tornillos y Anclajes
        crearProducto("FER-016", "Hilti", "HIL-016", "Anclajes Químicos", 
                     "Kit anclajes químicos M12 x 110mm", 
                     Producto.CategoriaProducto.TORNILLOS_ANCLAJES, 
                     Producto.SubcategoriaProducto.ANCLAJES, 
                     25000.0, 40, "ANC-QUI");

        crearProducto("FER-017", "Loctite", "LOC-017", "Adhesivo Estructural", 
                     "Adhesivo estructural epóxico 50ml", 
                     Producto.CategoriaProducto.FIJACIONES_ADHESIVOS, 
                     Producto.SubcategoriaProducto.ADHESIVOS, 
                     18000.0, 75, "ADH-EPO");

        crearProducto("FER-018", "Bosch", "BOS-018", "Medidor Láser GLM 50", 
                     "Medidor láser de distancia hasta 50m", 
                     Producto.CategoriaProducto.EQUIPOS_MEDICION, 
                     Producto.SubcategoriaProducto.MEDICION, 
                     95000.0, 12, "GLM-50");

        System.out.println("Productos iniciales cargados exitosamente!");
    }

    private void crearProducto(String codigo, String marca, String codigoInterno, String nombre, 
                             String descripcion, Producto.CategoriaProducto categoria, 
                             Producto.SubcategoriaProducto subcategoria, Double precio, 
                             Integer stock, String modelo) {
        
        Producto producto = new Producto();
        producto.setCodigoProducto(codigo);
        producto.setMarca(marca);
        producto.setCodigoInterno(codigoInterno);
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setCategoria(categoria);
        producto.setSubcategoria(subcategoria);
        producto.setPrecioActual(precio);
        producto.setStock(stock);
        producto.setModelo(modelo);
        producto.setActivo(true);

        // Crear precio histórico inicial
        Producto.Precio precioInicial = new Producto.Precio();
        precioInicial.setFecha(LocalDateTime.now());
        precioInicial.setValor(precio);
        precioInicial.setProducto(producto);
        
        producto.setPrecios(Arrays.asList(precioInicial));
        
        productoRepository.save(producto);
    }
}
