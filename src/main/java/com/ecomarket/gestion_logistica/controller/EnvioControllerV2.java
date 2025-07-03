package com.ecomarket.gestion_logistica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecomarket.gestion_logistica.model.Envio;
import com.ecomarket.gestion_logistica.model.UsuarioDTO;
import com.ecomarket.gestion_logistica.service.EnvioService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/hateoas/envios")
public class EnvioControllerV2 {

    @Autowired
    private EnvioService envioService;

    @GetMapping(value = "/listar", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Envio>>> listarEnvios() {
        List<Envio> envios = envioService.listarEnvios();
        if (envios == null || envios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<EntityModel<Envio>> envioModels = envios.stream()
            .map(envio -> EntityModel.of(envio,
                linkTo(methodOn(EnvioControllerV2.class).obtenerEnvioPorId(envio.getId())).withSelfRel()))
            .toList();

        CollectionModel<EntityModel<Envio>> collectionModel = CollectionModel.of(
            envioModels,
            linkTo(methodOn(EnvioControllerV2.class).listarEnvios()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Envio>> obtenerEnvioPorId(@PathVariable Integer id) {
        Envio envio = envioService.listarEnvios().stream()
            .filter(e -> e.getId().equals(id))
            .findFirst()
            .orElse(null);
        if (envio == null) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<Envio> envioModel = EntityModel.of(envio,
            linkTo(methodOn(EnvioControllerV2.class).obtenerEnvioPorId(id)).withSelfRel(),
            linkTo(methodOn(EnvioControllerV2.class).listarEnvios()).withRel("envios"));

        return ResponseEntity.ok(envioModel);
    }

    @PostMapping(value = "/crear/{usuarioId}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Envio>> crearEnvio(@RequestBody Envio envio, @PathVariable Integer usuarioId) {
        UsuarioDTO usuario = envioService.getUsuarioById(usuarioId);
        if (usuario == null || usuario.getRol() != 4) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Envio nuevoEnvio = envioService.crearEnvio(envio);

        EntityModel<Envio> envioModel = EntityModel.of(nuevoEnvio,
            linkTo(methodOn(EnvioControllerV2.class).obtenerEnvioPorId(nuevoEnvio.getId())).withSelfRel(),
            linkTo(methodOn(EnvioControllerV2.class).listarEnvios()).withRel("envios"));

        return ResponseEntity.ok(envioModel);
    }

    @PutMapping(value = "/actualizar/{usuarioId}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Envio>> actualizarEnvio(@RequestBody Envio envio, @PathVariable Integer usuarioId) {
        UsuarioDTO usuario = envioService.getUsuarioById(usuarioId);
        if (usuario == null || usuario.getRol() != 4) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Envio envioActualizado = envioService.actualizarEnvio(envio);

        EntityModel<Envio> envioModel = EntityModel.of(envioActualizado,
            linkTo(methodOn(EnvioControllerV2.class).obtenerEnvioPorId(envioActualizado.getId())).withSelfRel(),
            linkTo(methodOn(EnvioControllerV2.class).listarEnvios()).withRel("envios"));

        return ResponseEntity.ok(envioModel);
    }

    @DeleteMapping(value = "/eliminar/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarEnvio(@PathVariable Integer id) {
        Envio envio = envioService.listarEnvios().stream()
            .filter(e -> e.getId().equals(id))
            .findFirst()
            .orElse(null);
        if (envio == null) {
            return ResponseEntity.notFound().build();
        }
        envioService.eliminarEnvio(id);
        return ResponseEntity.ok().build();
    }
}
