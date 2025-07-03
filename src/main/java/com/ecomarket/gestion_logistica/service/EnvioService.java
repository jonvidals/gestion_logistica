package com.ecomarket.gestion_logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ecomarket.gestion_logistica.model.Envio;
import com.ecomarket.gestion_logistica.model.UsuarioDTO;
import com.ecomarket.gestion_logistica.repository.EnvioRepository;

@Service
public class EnvioService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EnvioRepository envioRepository;
    public List<Envio> listarEnvios() {
        return envioRepository.findAll();
    }
    public UsuarioDTO getUsuarioById(Integer id) {
        String url = "http://localhost:8083/api/usuario/" + id;
        return restTemplate.getForObject(url, UsuarioDTO.class);
    }
    public Envio crearEnvio(Envio envio){
        String url = "http://localhost:8083/api/usuario/" + envio.getUsuarioId();
        UsuarioDTO usuarioDestino = restTemplate.getForObject(url, UsuarioDTO.class);
        if (usuarioDestino == null) {
            throw new IllegalArgumentException("Usuario destino no existe");
        }
        envio.setEstadoEnvio("Envío creado");
        return envioRepository.save(envio);
    }
    public void eliminarEnvio(Integer id) {
        envioRepository.deleteById(id);

    }

    public Envio actualizarEnvio(Envio envioActualizado) {
    if (envioActualizado.getId() == null || !envioRepository.existsById(envioActualizado.getId())) {
        throw new IllegalArgumentException("Envío no encontrado");
    }
    Envio envioExistente = envioRepository.findById(envioActualizado.getId()).get();
    if (envioActualizado.getUsuarioId() == null || envioActualizado.getUsuarioId() == 0) {
        envioActualizado.setUsuarioId(envioExistente.getUsuarioId());
    } else {
        String url = "http://localhost:8083/api/usuario/" + envioActualizado.getUsuarioId();
        UsuarioDTO usuarioDestino = restTemplate.getForObject(url, UsuarioDTO.class);
        if (usuarioDestino == null) {
            throw new IllegalArgumentException("Usuario destino no existe");
        }
    }
    return envioRepository.save(envioActualizado);
    }
    



}
