package com.inventcontrol.inventory_control.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grupo_estoque") //Nome exato da tabela no Postegres
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrupoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_grupoestoque") //Mapeia nome da coluna real
    @JsonProperty("id")
    private Long id;

    @Column(name = "sigla", unique = true, length = 5, nullable = false)
    @JsonProperty("sigla")
    private String sigla; // Aqui entra o T1, COMP, etc.

    @Column(name = "descricao", nullable = false, length = 100)
    @JsonProperty("descricao")
    private String descricao;
}
