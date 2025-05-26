package com.ecomarket.gestion_logistica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecomarket.gestion_logistica.model.Usuario;
import com.ecomarket.gestion_logistica.service.UsuarioService;

@RestController
 @RequestMapping("/api/usuario/logistica")
public class UsuarioController {
     @Autowired
        private UsuarioService usuarioService;

     @GetMapping
        public List<Usuario> listarUsuarios() {
            return usuarioService.listarUsuarios();
        }

     @PostMapping
        public Usuario guardarUsuario(@RequestBody Usuario usuario) {
            return usuarioService.saveUsuario(usuario);
        }
}
