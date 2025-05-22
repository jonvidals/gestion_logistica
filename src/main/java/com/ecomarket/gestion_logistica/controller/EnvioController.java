package com.ecomarket.gestion_logistica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecomarket.gestion_logistica.model.Envio;
import com.ecomarket.gestion_logistica.model.Usuario;
import com.ecomarket.gestion_logistica.service.EnvioService;
import com.ecomarket.gestion_logistica.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {
    @Autowired
    private EnvioService envioService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Envio>> listarEnvios() {
        List<Envio> envios = envioService.listarEnvios();
        return ResponseEntity.ok(envios);
    }

    @PostMapping("/guardar/{id}")
    public ResponseEntity<Envio> guardarEnvio(@RequestBody Envio envio, @PathVariable Integer id) {
    Usuario userActual = usuarioService.getUserById(id);
    if (userActual == null || userActual.getRol() != 4) {
        return ResponseEntity.status(403).build();
    }
    Envio nuevoEnvio = envioService.saveEnvio(envio);
    return ResponseEntity.ok(nuevoEnvio);
    }
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Envio> actualizarEnvio(@RequestBody Envio envio, @PathVariable Integer id) {
        Usuario userActual = usuarioService.getUserById(id);
        if (userActual == null || userActual.getRol() != 4) {
            return ResponseEntity.status(403).build();
        }
        Envio actualizado = envioService.updateEnvio(envio);
        return ResponseEntity.ok(actualizado);
    }
    
}
