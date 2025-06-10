
package com.ferremas.controller;

import com.ferremas.model.Producto;
import com.ferremas.service.ProductoService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoService productoService = new ProductoService();

    @GetMapping
    public List<Producto> obtenerTodos() {
        return productoService.obtenerTodos();
    }

    @GetMapping("/{codigo}")
    public Producto obtenerPorCodigo(@PathVariable String codigo) {
        return productoService.obtenerPorCodigo(codigo);
    }
}
