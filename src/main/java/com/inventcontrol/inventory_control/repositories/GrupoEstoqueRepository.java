package com.inventcontrol.inventory_control.repositories;

import com.inventcontrol.inventory_control.entities.GrupoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GrupoEstoqueRepository extends JpaRepository<GrupoEstoque, Long> {

    // O Spring transforma esse nome em: SELECT * FROM grupo_estoque WHERE descricao = ?
    Optional<GrupoEstoque> findByDescricao(String descricao);

    Optional<GrupoEstoque> findBySigla(String sigla);

    @Query( "SELECT g " +
            "FROM GrupoEstoque g " +
            "WHERE g.descricao = :descricao OR g.sigla = :sigla")
    Optional<GrupoEstoque> findByDescricaoOrSigla(@Param("descricao") String descricao,
                                                  @Param("sigla") String sigla);
}
