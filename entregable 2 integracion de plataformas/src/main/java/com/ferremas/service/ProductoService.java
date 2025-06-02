package com.ferremas.service;

import com.ferremas.model.Producto;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays; // <-- Agrega este import

public class ProductoService {

    public List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();

        productos.add(crearProducto("FER-001", "Stanley", "STM-001", "Martillo"));
        productos.add(crearProducto("FER-002", "Bosch", "BOS-002", "Destornillador"));
        productos.add(crearProducto("FER-003", "DeWalt", "DEW-003", "Llave inglesa"));

        productos.add(crearProducto("FER-004", "Bosch", "BOS-004", "Taladro"));
        productos.add(crearProducto("FER-005", "Makita", "MAK-005", "Sierra circular"));
        productos.add(crearProducto("FER-006", "Black&Decker", "BDK-006", "Lijadora"));

        productos.add(crearProducto("FER-007", "Cementos Bio Bio", "CEM-007", "Cemento"));
        productos.add(crearProducto("FER-008", "Ferrosur", "FER-008", "Arena"));
        productos.add(crearProducto("FER-009", "Pizarreño", "PIZ-009", "Ladrillos"));
        productos.add(crearProducto("FER-010", "Sodimac", "SOD-010", "Pintura Látex"));
        productos.add(crearProducto("FER-011", "Ceresita", "CER-011", "Barniz"));
        productos.add(crearProducto("FER-012", "Cerámica San Lorenzo", "CSL-012", "Cerámico Beige"));

        productos.add(crearProducto("FER-013", "3M", "3M-013", "Casco de seguridad"));
        productos.add(crearProducto("FER-014", "SteelPro", "STP-014", "Guantes de trabajo"));
        productos.add(crearProducto("FER-015", "Uvex", "UVX-015", "Lentes de seguridad"));

        productos.add(crearProducto("FER-016", "Sodimac", "SOD-016", "Anclajes"));
        productos.add(crearProducto("FER-017", "Loctite", "LOC-017", "Adhesivo industrial"));
        productos.add(crearProducto("FER-018", "Bosch", "BOS-018", "Equipo de medición láser"));

        return productos;
    }

    public Producto obtenerPorCodigo(String codigo) {
        return obtenerTodos().stream()
            .filter(p -> p.getCodigoProducto().equals(codigo))
            .findFirst()
            .orElse(null);
    }

    private Producto crearProducto(String codProd, String marca, String codInt, String nombre) {
        Producto p = new Producto();
        p.setCodigoProducto(codProd);
        p.setMarca(marca);
        p.setCodigoInterno(codInt);
        p.setNombre(nombre);

        Producto.Precio precio = new Producto.Precio();
        precio.setFecha("2023-05-10T03:00:00.000Z");
        precio.setValor(Math.round((Math.random() * 90000 + 10000) * 100.0) / 100.0);

        p.setPrecios(Arrays.asList(precio)); // <-- Cambiado para compatibilidad con Java 8
        return p;
    }
}
