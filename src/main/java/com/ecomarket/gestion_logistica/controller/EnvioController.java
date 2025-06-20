package com.ecomarket.gestion_logistica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.ok(envios);
    }
    @PostMapping("/crear/{id}")
    public ResponseEntity<Envio> crearEnvio(@RequestBody Envio envio, @PathVariable Integer id) {
        UsuarioDTO usuario = envioService.getUsuarioById(id);
        if (usuario == null || usuario.getRol() != 4) {
            return ResponseEntity.status(403).build();
        }
        Envio nuevoEnvio = envioService.crearEnvio(envio);
        return ResponseEntity.ok(nuevoEnvio);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Envio> obtenerEnvioPorId(@PathVariable Integer id) {
        Envio envio = envioService.listarEnvios().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (envio != null) {
            return ResponseEntity.ok(envio);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarEnvio(@PathVariable Integer id) {
        Envio envio = envioService.listarEnvios().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (envio != null) {
            envioService.eliminarEnvio(envio.getId());
            return ResponseEntity.ok("Envío eliminado con éxito");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Envio> actualizarEnvio(@RequestBody Envio envio, @PathVariable Integer id) {
        UsuarioDTO usuario = envioService.getUsuarioById(id);
        if (usuario == null || usuario.getRol() != 4) {
            return ResponseEntity.status(403).build();
        }
        Envio envioActualizado = envioService.actualizarEnvio(envio);
        return ResponseEntity.ok(envioActualizado);
    }


    /*

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Envio> actualizarEnvio(@RequestBody Envio envio, @PathVariable Integer id) {
        Usuario userActual = usuarioService.getUserById(id);
        if (userActual == null || userActual.getRol() != 4) {
            return ResponseEntity.status(403).build();
        }
        Envio actualizado = envioService.actualizarEnvio(envio);
        return ResponseEntity.ok(actualizado);
    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarEnvio(@RequestBody Envio envio, @PathVariable Integer id){
        Usuario userActual = usuarioService.getUserById(id);
        if (userActual == null || userActual.getRol() != 4 ){
            return ResponseEntity.status(403).build();

        }
        envioService.deleteEnvio(envio.getId());
        return ResponseEntity.ok("Usuario eliminado con éxito");

    }   
    @PutMapping("/actualizarEstado/{id}")
    public ResponseEntity<Envio> actualizarEstadoEnvio(@RequestBody Envio envio, @PathVariable Integer id) {
        Usuario userActual = usuarioService.getUserById(id);
        if (userActual == null || userActual.getRol() != 4) {
            return ResponseEntity.status(403).build();
        }
        Envio envioExistente = envioService.updateEnvio(envio);
        if (envioExistente != null) {
            return ResponseEntity.ok(envioExistente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/

    /*@PutMapping("/reprogramarFecha/{id}")   
    public ResponseEntity<Envio> reprogramarFechaEnvio(@RequestBody Envio envio, @PathVariable Integer id) {
        Usuario userActual = usuarioService.getUserById(id);
        if (userActual == null || userActual.getRol() != 4) {
            return ResponseEntity.status(403).build();
        }
        Envio envioExistente = envioService.updateEnvio(envio);
        if (envioExistente != null) {
            return ResponseEntity.ok(envioExistente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/
}