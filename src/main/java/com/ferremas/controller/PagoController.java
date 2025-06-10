
package com.ferremas.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class PagoController {

    @GetMapping("/api/pago/iniciar")
    public String iniciarPago(@RequestParam double monto) {
        return "Redirigiendo a webpay (simulado)... monto: $" + monto;
    }

    @GetMapping("/api/pago/exito")
    public String pagoExitoso() {
        return "✅ Pago realizado con éxito (simulado)";
    }

    @GetMapping("/api/pago/error")
    public String pagoError() {
        return "❌ Error en el pago (simulado)";
    }
}
