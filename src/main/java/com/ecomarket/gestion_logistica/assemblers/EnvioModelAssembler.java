package com.ecomarket.gestion_logistica.assemblers;

import com.ecomarket.gestion_logistica.controller.EnvioControllerV2;
import com.ecomarket.gestion_logistica.model.Envio;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EnvioModelAssembler extends RepresentationModelAssemblerSupport<Envio, EntityModel<Envio>> {

    public EnvioModelAssembler() {
        super(EnvioControllerV2.class, (Class<EntityModel<Envio>>) (Class<?>) EntityModel.class);
    }

    @Override
    public EntityModel<Envio> toModel(Envio envio) {
        return EntityModel.of(envio,
            linkTo(methodOn(EnvioControllerV2.class).obtenerEnvioPorId(envio.getId())).withSelfRel(),
            linkTo(methodOn(EnvioControllerV2.class).listarEnvios()).withRel("envios")
        );
    }
}
