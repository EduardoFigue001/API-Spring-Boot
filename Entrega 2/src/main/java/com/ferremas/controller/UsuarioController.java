package com.ferremas.controller;

import com.ferremas.model.Usuario;
import com.ferremas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/registrar")
    public String registrar(@RequestBody Usuario nuevoUsuario) {
        Optional<Usuario> existente = usuarioRepository.findByCorreo(nuevoUsuario.getCorreo());
        if (existente.isPresent()) {
            return "EXISTE";
        }
        usuarioRepository.save(nuevoUsuario);
        return "REGISTRADO";
    }

    @PostMapping("/login")
    public String login(@RequestBody Usuario usuario) {
        Optional<Usuario> encontrado = usuarioRepository.findByCorreo(usuario.getCorreo());
        if (encontrado.isPresent() && encontrado.get().getClave().equals(usuario.getClave())) {
            return "OK";
        }
        return "ERROR";
    }
}
