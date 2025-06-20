package com.ecomarket.gestion_logistica.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "envio")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String direccionDestino;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaEnvio;

    private String estadoEnvio;


    @Column(name = "id_usuario", nullable = false)
    private Integer usuarioId;

}