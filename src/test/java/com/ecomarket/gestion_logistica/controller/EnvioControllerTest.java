package com.ecomarket.gestion_logistica.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.ecomarket.gestion_logistica.model.Envio;
import com.ecomarket.gestion_logistica.model.UsuarioDTO;
import com.ecomarket.gestion_logistica.service.EnvioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EnvioController.class)
public class EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnvioService envioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Envio envio1;
    private Envio envio2;

    private UsuarioDTO usuarioAdmin;
    private UsuarioDTO usuarioNoAdmin;

    @BeforeEach
    void setUp() {
        envio1 = new Envio();
        envio1.setId(1);
        envio1.setUsuarioId(10);
        envio1.setEstadoEnvio("Estado1");

        envio2 = new Envio();
        envio2.setId(2);
        envio2.setUsuarioId(20);
        envio2.setEstadoEnvio("Estado2");

        usuarioAdmin = new UsuarioDTO();
        usuarioAdmin.setId(100);
        usuarioAdmin.setRol(4);

        usuarioNoAdmin = new UsuarioDTO();
        usuarioNoAdmin.setId(101);
        usuarioNoAdmin.setRol(2);
    }

    @Test
    void listarEnviosOk() throws Exception {
        when(envioService.listarEnvios()).thenReturn(Arrays.asList(envio1, envio2));

        mockMvc.perform(get("/api/envios/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(envio1.getId()))
                .andExpect(jsonPath("$[1].id").value(envio2.getId()));
    }

    @Test
    void listarEnviosNotFound() throws Exception {
        when(envioService.listarEnvios()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/envios/listar"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearEnvioOk() throws Exception {
        when(envioService.getUsuarioById(100)).thenReturn(usuarioAdmin);
        when(envioService.crearEnvio(any())).thenReturn(envio1);

        mockMvc.perform(post("/api/envios/crear/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(envio1.getId()));
    }

    @Test
    void crearEnvioForbidden() throws Exception {
        when(envioService.getUsuarioById(101)).thenReturn(usuarioNoAdmin);

        mockMvc.perform(post("/api/envios/crear/101")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void crearEnvioBadRequest() throws Exception {
        when(envioService.getUsuarioById(100)).thenReturn(usuarioAdmin);
        when(envioService.crearEnvio(any())).thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/api/envios/crear/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerEnvioPorIdOk() throws Exception {
        when(envioService.listarEnvios()).thenReturn(Arrays.asList(envio1, envio2));

        mockMvc.perform(get("/api/envios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(envio1.getId()));
    }

    @Test
    void obtenerEnvioPorIdNotFound() throws Exception {
        when(envioService.listarEnvios()).thenReturn(Arrays.asList(envio2));

        mockMvc.perform(get("/api/envios/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarEnvioOk() throws Exception {
        when(envioService.listarEnvios()).thenReturn(Arrays.asList(envio1));

        mockMvc.perform(delete("/api/envios/eliminar/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Envío eliminado con éxito"));

        verify(envioService).eliminarEnvio(1);
    }

    @Test
    void eliminarEnvioNotFound() throws Exception {
        when(envioService.listarEnvios()).thenReturn(Collections.emptyList());

        mockMvc.perform(delete("/api/envios/eliminar/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarEnvioOk() throws Exception {
        when(envioService.getUsuarioById(100)).thenReturn(usuarioAdmin);
        when(envioService.actualizarEnvio(any())).thenReturn(envio1);

        mockMvc.perform(put("/api/envios/actualizar/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(envio1.getId()));
    }

    @Test
    void actualizarEnvioForbidden() throws Exception {
        when(envioService.getUsuarioById(101)).thenReturn(usuarioNoAdmin);

        mockMvc.perform(put("/api/envios/actualizar/101")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void actualizarEnvioNotFound() throws Exception {
        when(envioService.getUsuarioById(100)).thenReturn(usuarioAdmin);
        when(envioService.actualizarEnvio(any())).thenThrow(new IllegalArgumentException());

        mockMvc.perform(put("/api/envios/actualizar/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearEnvioUsuarioNull() throws Exception {
        when(envioService.getUsuarioById(999)).thenReturn(null);

        mockMvc.perform(post("/api/envios/crear/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void actualizarEnvioUsuarioNull() throws Exception {
        when(envioService.getUsuarioById(999)).thenReturn(null);

        mockMvc.perform(put("/api/envios/actualizar/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio1)))
                .andExpect(status().isForbidden());

    }

    @Test
    void testListarEnviosVacio() throws Exception {
        when(envioService.listarEnvios()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/envios/listar"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListarEnviosNull() throws Exception {
        when(envioService.listarEnvios()).thenReturn(null);

        mockMvc.perform(get("/api/envios/listar"))
                .andExpect(status().isNotFound());
    }

}
