package com.ecomarket.gestion_logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecomarket.gestion_logistica.model.Usuario;
import com.ecomarket.gestion_logistica.repository.UsuarioRepository;

@Service

public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario saveUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario getUserById(int id) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario != null) {
            return usuario;
        }
        return null;



                
    } 
}
