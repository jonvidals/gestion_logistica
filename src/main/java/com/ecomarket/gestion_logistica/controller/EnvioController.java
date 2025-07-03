package com.ecomarket.gestion_logistica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecomarket.gestion_logistica.model.Envio;
import com.ecomarket.gestion_logistica.model.UsuarioDTO;
import com.ecomarket.gestion_logistica.service.EnvioService;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {
    @Autowired
    private EnvioService envioService;

    @GetMapping("/listar")
    public ResponseEntity<List<Envio>> listarEnvios() {
        List<Envio> envios = envioService.listarEnvios();
        if (envios == null || envios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(envios);
    }

    @PostMapping("/crear/{id}")
    public ResponseEntity<Envio> crearEnvio(@RequestBody Envio envio, @PathVariable Integer id) {
        UsuarioDTO usuario = envioService.getUsuarioById(id);
        if (usuario == null || usuario.getRol() != 4) {
            return ResponseEntity.status(403).build();
        }
        try {
            Envio nuevoEnvio = envioService.crearEnvio(envio);
            return ResponseEntity.ok(nuevoEnvio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Envio> obtenerEnvioPorId(@PathVariable Integer id) {
        Envio envio = envioService.listarEnvios().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (envio == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(envio);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarEnvio(@PathVariable Integer id) {
        Envio envio = envioService.listarEnvios().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (envio == null) {
            return ResponseEntity.notFound().build();
        }
        envioService.eliminarEnvio(id);
        return ResponseEntity.ok("Envío eliminado con éxito");
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Envio> actualizarEnvio(@RequestBody Envio envio, @PathVariable Integer id) {
        UsuarioDTO usuario = envioService.getUsuarioById(id);
        if (usuario == null || usuario.getRol() != 4) {
            return ResponseEntity.status(403).build();
        }
        try {
            Envio envioActualizado = envioService.actualizarEnvio(envio);
            return ResponseEntity.ok(envioActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
