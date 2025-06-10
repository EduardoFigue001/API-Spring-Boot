
package com.ferremas.model;

import java.util.List;

public class Producto {
    private String codigoProducto;
    private String marca;
    private String codigoInterno;
    private String nombre;
    private List<Precio> precios;

    public String getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getCodigoInterno() { return codigoInterno; }
    public void setCodigoInterno(String codigoInterno) { this.codigoInterno = codigoInterno; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Precio> getPrecios() { return precios; }
    public void setPrecios(List<Precio> precios) { this.precios = precios; }

    public static class Precio {
        private String fecha;
        private double valor;

        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }

        public double getValor() { return valor; }
        public void setValor(double valor) { this.valor = valor; }
    }
}
