package com.ecomarket.gestion_logistica.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;

import com.ecomarket.gestion_logistica.model.Envio;
import com.ecomarket.gestion_logistica.model.UsuarioDTO;
import com.ecomarket.gestion_logistica.repository.EnvioRepository;

public class EnvioServiceTest {

    @InjectMocks
    private EnvioService envioService;

    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarEnvios() {
        Envio envio1 = new Envio();
        Envio envio2 = new Envio();
        when(envioRepository.findAll()).thenReturn(Arrays.asList(envio1, envio2));
        List<Envio> resultado = envioService.listarEnvios();
        assertEquals(2, resultado.size());
        verify(envioRepository).findAll();
    }


    @Test
    void testListarEnviosVacio() {
        when(envioRepository.findAll()).thenReturn(Arrays.asList());
        List<Envio> resultado = envioService.listarEnvios();
        assertTrue(resultado.isEmpty());
        verify(envioRepository).findAll();
    }

    @Test
    void testGetUsuarioById() {
        UsuarioDTO usuario = new UsuarioDTO();
        when(restTemplate.getForObject("http://localhost:8083/api/usuario/1", UsuarioDTO.class)).thenReturn(usuario);
        UsuarioDTO resultado = envioService.getUsuarioById(1);
        assertNotNull(resultado);
        verify(restTemplate).getForObject("http://localhost:8083/api/usuario/1", UsuarioDTO.class);
    }


    @Test
    void testCrearEnvioSuccess() {
        Envio envio = new Envio();
        envio.setUsuarioId(1);
        UsuarioDTO usuarioDTO = new UsuarioDTO();

        when(restTemplate.getForObject(anyString(), eq(UsuarioDTO.class))).thenReturn(usuarioDTO);
        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> i.getArgument(0));

        Envio resultado = envioService.crearEnvio(envio);
        assertEquals("Envío creado", resultado.getEstadoEnvio());
        verify(restTemplate).getForObject(anyString(), eq(UsuarioDTO.class));
        verify(envioRepository).save(envio);
    }


    @Test
    void testCrearEnvioUsuarioDestinoNull() {
        Envio envio = new Envio();
        envio.setUsuarioId(1);

        when(restTemplate.getForObject(anyString(), eq(UsuarioDTO.class))).thenReturn(null);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            envioService.crearEnvio(envio);
        });
        assertEquals("Usuario destino no existe", ex.getMessage());
        verify(restTemplate).getForObject(anyString(), eq(UsuarioDTO.class));
        verify(envioRepository, never()).save(any());
    }

    @Test
    void testEliminarEnvio() {
        doNothing().when(envioRepository).deleteById(1);
        envioService.eliminarEnvio(1);
        verify(envioRepository).deleteById(1);
    }


    @Test
    void testActualizarEnvioIdNull() {
        Envio envio = new Envio();
        envio.setId(null);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            envioService.actualizarEnvio(envio);
        });
        assertEquals("Envío no encontrado", ex.getMessage());
    }

    @Test
    void testActualizarEnvioIdNoExiste() {
        Envio envio = new Envio();
        envio.setId(1);
        when(envioRepository.existsById(1)).thenReturn(false);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            envioService.actualizarEnvio(envio);
        });
        assertEquals("Envío no encontrado", ex.getMessage());
    }

    @Test
    void testActualizarEnvioUsuarioIdNullO0() {
        Envio envioExistente = new Envio();
        envioExistente.setId(1);
        envioExistente.setUsuarioId(99);

        Envio envioActualizar = new Envio();
        envioActualizar.setId(1);
        envioActualizar.setUsuarioId(null);

        when(envioRepository.existsById(1)).thenReturn(true);
        when(envioRepository.findById(1)).thenReturn(Optional.of(envioExistente));
        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> i.getArgument(0));

        Envio resultado = envioService.actualizarEnvio(envioActualizar);
        assertEquals(99, resultado.getUsuarioId());
        envioActualizar.setUsuarioId(0);
        resultado = envioService.actualizarEnvio(envioActualizar);
        assertEquals(99, resultado.getUsuarioId());
    }
    @Test
    void testActualizarEnvioUsuarioIdNuevoValido() {
        Envio envioExistente = new Envio();
        envioExistente.setId(1);
        envioExistente.setUsuarioId(10);

        Envio envioActualizar = new Envio();
        envioActualizar.setId(1);
        envioActualizar.setUsuarioId(20);

        UsuarioDTO usuarioDTO = new UsuarioDTO();

        when(envioRepository.existsById(1)).thenReturn(true);
        when(envioRepository.findById(1)).thenReturn(Optional.of(envioExistente));
        when(restTemplate.getForObject(anyString(), eq(UsuarioDTO.class))).thenReturn(usuarioDTO);
        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> i.getArgument(0));

        Envio resultado = envioService.actualizarEnvio(envioActualizar);
        assertEquals(20, resultado.getUsuarioId());
        verify(restTemplate).getForObject(anyString(), eq(UsuarioDTO.class));
    }


    @Test
    void testActualizarEnvioUsuarioIdNuevoInvalido() {
        Envio envioExistente = new Envio();
        envioExistente.setId(1);
        envioExistente.setUsuarioId(10);

        Envio envioActualizar = new Envio();
        envioActualizar.setId(1);
        envioActualizar.setUsuarioId(20);

        when(envioRepository.existsById(1)).thenReturn(true);
        when(envioRepository.findById(1)).thenReturn(Optional.of(envioExistente));
        when(restTemplate.getForObject(anyString(), eq(UsuarioDTO.class))).thenReturn(null);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            envioService.actualizarEnvio(envioActualizar);
        });
        assertEquals("Usuario destino no existe", ex.getMessage());
    }
}
