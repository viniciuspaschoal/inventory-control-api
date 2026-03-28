package com.inventcontrol.inventory_control.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "deposito")
public class Deposito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_deposito")
    private Long id;

    @Column(unique = true, length = 5, nullable = false)
    private String sigla; // Ex: TEX01

    @Column(nullable = false, length = 100)
    private String descricao; // Ex: TEXFITY T1
}
