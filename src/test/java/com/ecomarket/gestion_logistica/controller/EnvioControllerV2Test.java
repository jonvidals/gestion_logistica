package com.ecomarket.gestion_logistica.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import com.ecomarket.gestion_logistica.model.Envio;
import com.ecomarket.gestion_logistica.model.UsuarioDTO;
import com.ecomarket.gestion_logistica.service.EnvioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(EnvioControllerV2.class)
public class EnvioControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnvioService envioService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    private Envio crearEnvioEjemplo() {
        return new Envio(1, "Calle Falsa 123", LocalDate.of(2025, 7, 3), "Enviado", 10);
    }

    private UsuarioDTO crearUsuarioEjemplo(int rol) {
        UsuarioDTO u = new UsuarioDTO();
        u.setId(10);
        u.setRol(rol);
        u.setActivo(true);
        return u;
    }

    @Test
    public void listarEnvios_OK() throws Exception {
        when(envioService.listarEnvios()).thenReturn(List.of(crearEnvioEjemplo()));

        mockMvc.perform(get("/api/hateoas/envios/listar")
                .accept("application/hal+json"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/hal+json"));
    }

    @Test
    public void listarEnvios_NotFound() throws Exception {
        when(envioService.listarEnvios()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/hateoas/envios/listar")
                .accept("application/hal+json"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void crearEnvio_OK() throws Exception {
        Envio envio = crearEnvioEjemplo();
        UsuarioDTO usuario = crearUsuarioEjemplo(4);

        when(envioService.getUsuarioById(anyInt())).thenReturn(usuario);
        when(envioService.crearEnvio(any(Envio.class))).thenReturn(envio);

        mockMvc.perform(post("/api/hateoas/envios/crear/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio))
                .accept("application/hal+json"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/hal+json"));
    }

    @Test
    public void crearEnvio_Forbidden_usuarioNoAutorizado() throws Exception {
        Envio envio = crearEnvioEjemplo();
        UsuarioDTO usuario = crearUsuarioEjemplo(1); // rol != 4

        when(envioService.getUsuarioById(anyInt())).thenReturn(usuario);

        mockMvc.perform(post("/api/hateoas/envios/crear/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio))
                .accept("application/hal+json"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void crearEnvio_Forbidden_usuarioNull() throws Exception {
        Envio envio = crearEnvioEjemplo();

        when(envioService.getUsuarioById(anyInt())).thenReturn(null);

        mockMvc.perform(post("/api/hateoas/envios/crear/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio))
                .accept("application/hal+json"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void actualizarEnvio_OK() throws Exception {
        Envio envio = crearEnvioEjemplo();
        UsuarioDTO usuario = crearUsuarioEjemplo(4);

        when(envioService.getUsuarioById(anyInt())).thenReturn(usuario);
        when(envioService.actualizarEnvio(any(Envio.class))).thenReturn(envio);

        mockMvc.perform(put("/api/hateoas/envios/actualizar/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio))
                .accept("application/hal+json"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/hal+json"));
    }

    @Test
    public void actualizarEnvio_Forbidden_usuarioNoAutorizado() throws Exception {
        Envio envio = crearEnvioEjemplo();
        UsuarioDTO usuario = crearUsuarioEjemplo(2);

        when(envioService.getUsuarioById(anyInt())).thenReturn(usuario);

        mockMvc.perform(put("/api/hateoas/envios/actualizar/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio))
                .accept("application/hal+json"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void actualizarEnvio_Forbidden_usuarioNull() throws Exception {
        Envio envio = crearEnvioEjemplo();

        when(envioService.getUsuarioById(anyInt())).thenReturn(null);

        mockMvc.perform(put("/api/hateoas/envios/actualizar/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio))
                .accept("application/hal+json"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void eliminarEnvio_OK() throws Exception {
        Envio envio = crearEnvioEjemplo();

        when(envioService.listarEnvios()).thenReturn(List.of(envio));

        mockMvc.perform(delete("/api/hateoas/envios/eliminar/1")
                .accept("application/hal+json"))
            .andExpect(status().isOk());
    }

    @Test
    public void eliminarEnvio_NotFound() throws Exception {
        when(envioService.listarEnvios()).thenReturn(Collections.emptyList());

        mockMvc.perform(delete("/api/hateoas/envios/eliminar/1")
                .accept("application/hal+json"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void eliminarEnvio_Exception() throws Exception {
        Envio envio = crearEnvioEjemplo();

        when(envioService.listarEnvios()).thenReturn(List.of(envio));
        doThrow(new RuntimeException("Error")).when(envioService).eliminarEnvio(anyInt());

        mockMvc.perform(delete("/api/hateoas/envios/eliminar/1")
                .accept("application/hal+json"))
            .andExpect(status().isInternalServerError());
    }
    @Test
public void listarEnvios_enviosNull_devuelveNotFound() throws Exception {
    when(envioService.listarEnvios()).thenReturn(null);

    mockMvc.perform(get("/api/hateoas/envios/listar")
            .accept("application/hal+json"))
        .andExpect(status().isNotFound());
}

@Test
public void listarEnvios_enviosEmpty_devuelveNotFound() throws Exception {
    when(envioService.listarEnvios()).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/hateoas/envios/listar")
            .accept("application/hal+json"))
        .andExpect(status().isNotFound());
}

}
