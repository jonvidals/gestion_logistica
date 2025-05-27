package com.ecomarket.gestion_logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecomarket.gestion_logistica.model.Envio;
import com.ecomarket.gestion_logistica.repository.EnvioRepository;

@Service
public class EnvioService {
     @Autowired
        private EnvioRepository envioRepository;

        public List<Envio> listarEnvios() {
            return envioRepository.findAll();
        }
        public Envio saveEnvio(Envio envio) {
            return envioRepository.save(envio);
        }
        public Envio updateEnvio(Envio envio) {
            Envio envioExistente = envioRepository.findById(envio.getId()).orElse(null);
            if (envioExistente != null) {
                if (envio.getDireccionDestino() != null) {
                    envioExistente.setDireccionDestino(envio.getDireccionDestino());
                }
                if (envio.getFechaEnvio() != null) {
                    envioExistente.setFechaEnvio(envio.getFechaEnvio());
                }
                if (envio.getEstadoEnvio() != null) {
                    envioExistente.setEstadoEnvio(envio.getEstadoEnvio());
                }
                envioRepository.save(envioExistente);
                return envioExistente;
            }
            return null;
        }
        public void deleteEnvio(Integer id) {
            envioRepository.deleteById(id);
        }
    
}
