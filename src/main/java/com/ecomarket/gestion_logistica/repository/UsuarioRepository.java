package com.ecomarket.gestion_logistica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecomarket.gestion_logistica.model.Usuario;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    List<Usuario> findAll();
     Usuario findById(int id);

     @SuppressWarnings("unchecked")
    Usuario save(Usuario usuario);
    
}
