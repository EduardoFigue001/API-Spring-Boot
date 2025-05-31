
package com.ferremas.controller;

import com.ferremas.service.DivisaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/divisa")
public class DivisaController {

    private final DivisaService divisaService = new DivisaService();

    @GetMapping("/dolar")
    public double obtenerValorDolar() {
        return divisaService.obtenerValorDolar();
    }

    @GetMapping("/convertir")
    public double convertir(@RequestParam double clp) {
        return divisaService.convertirCLPaUSD(clp);
    }
}
